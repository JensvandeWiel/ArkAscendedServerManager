/*
This file contains all the logic for saving and getting servers and making sure they stay updated through the application.

Events:
- "serverSaved" is called on a server save and has the server id as int as optional data.
- "serverCreated" is called when a server is created and has the server id as int as optional data.
- "gotServer" is called when a server is retrieved and has the server id as int as optional data.


*/

package server

import (
	"context"
	"encoding/json"
	"fmt"
	"github.com/adrg/xdg"
	"github.com/wailsapp/wails/v2/pkg/runtime"
	"os"
	"path"
	"strconv"
)

const (
	configFileName = "config.json"
)

// ServerController struct
type ServerController struct {
	ctx       context.Context
	Servers   map[int]*Server
	serverDir string
}

//region Struct Initialization and Creation

// NewServerController creates a new ServerController application struct
func NewServerController() *ServerController {
	return &ServerController{
		Servers: make(map[int]*Server),
	}
}

// Startup is called when the app starts. The context is saved,
// so we can call the runtime methods
func (c *ServerController) Startup(ctx context.Context) {
	c.ctx = ctx

	serverDir, err := xdg.ConfigFile(path.Join("ArkAscendedServerManager", "Servers"))
	if err != nil {
		runtime.LogErrorf(c.ctx, "Error getting config dir: %s", err.Error())
	}

	c.serverDir = serverDir
}

//endregion

//region Saving and Loading

// GetServerWithError returns the server with the given id if it does not exist it returns an empty server. This function checks the server dir too. If it does not exist in the map, and it does in the dir then it will add it to the map.
func (c *ServerController) GetServerWithError(id int, addToMap bool) (*Server, error) {
	server, exists := c.Servers[id]
	// If it does not exist in the map check the server dir
	if !exists {
		s, err := c.getServerFromDir(id, false)
		if err != nil {
			return nil, fmt.Errorf("Error getting server instance: %s", err.Error())
		}

		if addToMap {
			c.Servers[id] = &s
		}

		runtime.EventsEmit(c.ctx, "gotServer", s.Id)
		s.ctx = c.ctx
		return &s, nil
	} else {
		runtime.EventsEmit(c.ctx, "gotServer", server.Id)
		server.ctx = c.ctx
		return server, nil
	}
}

// CreateServerWithError Creates a new server, returns it and adds it to the map, it also returns the key. If it fails it returns an error and an empty server and int -1
func (c *ServerController) CreateServerWithError(saveToConfig bool) (int, *Server, error) {
	// get the highest in to generate new id
	maxKey := findHighestKey(c.Servers)
	id := maxKey + 1
	if _, exists := c.Servers[id]; exists {
		return -1, nil, fmt.Errorf("Found a server with an key higher than the maximum key, max key: " + strconv.Itoa(maxKey) + " exists: " + strconv.Itoa(id))
	}

	// Check if it exists
	if _, exists := c.Servers[id]; exists {
		return -1, nil, fmt.Errorf("Found a server with new key in c.Servers key: " + strconv.Itoa(id))
	}

	NewServer := generateNewDefaultServer(id)
	c.Servers[id] = &NewServer

	if saveToConfig {
		err := c.SaveServerWithError(c.Servers[id])
		if err != nil {
			// Don't handle this error because the server is already made, just mention it.
			runtime.LogError(c.ctx, "Failed saving new server: "+err.Error())
		}
	}

	runtime.EventsEmit(c.ctx, "serverCreated", NewServer.Id)
	NewServer.ctx = c.ctx

	return id, &NewServer, nil
}

// SaveServerWithError saves the server, and returns an error if it fails
func (c *ServerController) SaveServerWithError(server *Server) error {
	// Check if server is correct.
	if err := CheckIfServerCorrect(*server); err != nil {
		return fmt.Errorf("Parsing server instance failed: " + err.Error())
	}

	c.Servers[server.Id] = server
	serverDir := path.Join(c.serverDir, strconv.Itoa(server.Id))

	serverFile, err := json.MarshalIndent(server, "", " ")
	if err != nil {
		return fmt.Errorf("Error marshalling config file: " + err.Error())
	}

	// If config dir does not exist create a new one
	_, err = os.Stat(serverDir)
	if err != nil {
		if os.IsNotExist(err) {
			runtime.LogDebug(c.ctx, "Server config "+strconv.Itoa(server.Id)+" directory does not exist, creating it")
			if err := os.MkdirAll(serverDir, os.ModePerm); err != nil {
				return fmt.Errorf("Error creating server dir: " + serverDir + "with error: " + err.Error())
			}
		}
	}

	err = os.WriteFile(path.Join(serverDir, configFileName), serverFile, 0644)
	if err != nil {
		return fmt.Errorf("Error writing config file: " + err.Error())
	}

	runtime.EventsEmit(c.ctx, "serverSaved", server.Id)

	return nil
}

// GetAllServersWithError gets all servers and saves them to ServerController.Servers and also returns them, if it fails it returns nil and error. If they already exist in the map it will just get that.
func (c *ServerController) GetAllServersWithError() (map[int]*Server, error) {

	allServerDir := c.serverDir

	if _, err := os.Stat(allServerDir); err != nil {
		if os.IsNotExist(err) {
			return nil, fmt.Errorf(allServerDir + " does not exit")
		}
		return nil, fmt.Errorf("Getting all servers from directory failed: " + err.Error())
	}

	children, err := os.ReadDir(allServerDir)
	if err != nil {
		return nil, fmt.Errorf("Failed to read children in " + c.serverDir + " error: " + err.Error())
	}

	servers := make(map[int]*Server)

	for _, child := range children {
		if child.IsDir() {
			index, err := strconv.Atoi(child.Name())
			if err != nil {
				return nil, fmt.Errorf("Failed to parse to int: " + child.Name() + " error: " + err.Error())
			}

			server, err := c.GetServerWithError(index, false)
			if err != nil {
				return nil, fmt.Errorf("Failed to get server: " + child.Name() + " error: " + err.Error())
			}

			servers[index] = server
		}
	}

	c.Servers = servers

	return c.Servers, nil
}

// GetAllServersFromDir gets all servers from dir and saves them to ServerController.Servers and also returns them, if it fails it returns nil and an error which is catch-able in the frontend. This will overwrite c.Servers!
func (c *ServerController) GetAllServersFromDir() (map[int]*Server, error) {
	allserverDir := c.serverDir

	if _, err := os.Stat(allserverDir); err != nil {
		if os.IsNotExist(err) {
			newErr := fmt.Errorf(allserverDir + " does not exit")
			runtime.LogError(c.ctx, newErr.Error())
			return nil, newErr
		}
		newErr := fmt.Errorf("Getting all servers from directory failed: " + err.Error())
		runtime.LogError(c.ctx, newErr.Error())
		return nil, newErr
	}

	children, err := os.ReadDir(allserverDir)
	if err != nil {
		newErr := fmt.Errorf("Failed to read children in " + c.serverDir + " error: " + err.Error())
		runtime.LogError(c.ctx, newErr.Error())
		return nil, newErr
	}

	servers := make(map[int]*Server)

	for _, child := range children {
		if child.IsDir() {
			index, err := strconv.Atoi(child.Name())
			if err != nil {
				newErr := fmt.Errorf("Failed to parse to int: " + child.Name() + " error: " + err.Error())
				runtime.LogError(c.ctx, newErr.Error())
				return nil, newErr
			}

			server, err := c.getServerFromDir(index, false)
			if err != nil {
				newErr := fmt.Errorf("Failed to get server: " + child.Name() + " error: " + err.Error())
				runtime.LogError(c.ctx, newErr.Error())
				return nil, newErr
			}
			servers[index] = &server
		}
	}

	c.Servers = servers

	return c.Servers, nil
}

// DeleteServerFilesWithError deletes the server files from the server with the given id. If it fails it returns an error which is catch-able
func (c *ServerController) DeleteServerFilesWithError(id int) error {
	server, err := c.GetServerWithError(id, false)
	if err != nil {
		return fmt.Errorf("Failed to get server: " + strconv.Itoa(id) + " error: " + err.Error())
	}
	serverPath := server.ServerPath

	err = os.RemoveAll(serverPath)
	if err != nil {
		return fmt.Errorf("Failed to delete server " + strconv.Itoa(id) + " files: " + serverPath + " error: " + err.Error())
	}

	return nil
}

// DeleteProfileWithError deletes the profile with the given id. If it fails it returns an error which is catch-able
func (c *ServerController) DeleteProfileWithError(id int) error {

	serverDir := path.Join(c.serverDir, strconv.Itoa(id))

	err := os.RemoveAll(serverDir)
	if err != nil {
		return fmt.Errorf("Failed to delete server " + strconv.Itoa(id) + " profile error: " + err.Error())
	}

	delete(c.Servers, id)

	return nil
}

//region Boilerplate functions

// GetAllServers gets all servers and saves them to ServerController.Servers and also returns them, if it fails it returns nil and error. If they already exist in the map it will just get that.
func (c *ServerController) GetAllServers() (map[int]Server, error) {

	servers, err := c.GetAllServersWithError()
	if err != nil {
		newErr := fmt.Errorf("Failed to get all servers " + c.serverDir + " error: " + err.Error())
		runtime.LogError(c.ctx, newErr.Error())
		return nil, newErr
	}

	newMap := make(map[int]Server)

	// Copy the data from the original map to the new map
	for key, value := range servers {
		newMap[key] = *value
	}
	return newMap, nil
}

// SaveServer saves the server with the given id, and returns bool if successful
func (c *ServerController) SaveServer(server Server) error {

	//TODO Make sure oldserv members get passed over to new instance since frontend will change all members even Command (which should not be updated by the frontend)

	oldServ, err := c.GetServerWithError(server.Id, true)
	if err != nil {
		newErr := fmt.Errorf("Error saving server: " + err.Error())
		runtime.LogError(c.ctx, newErr.Error())
		return newErr
	}

	server.Command = oldServ.Command
	server.ctx = c.ctx

	err = c.SaveServerWithError(&server)
	if err != nil {
		newErr := fmt.Errorf("Error saving server: " + err.Error())
		runtime.LogError(c.ctx, newErr.Error())
		return newErr
	}
	return nil
}

// GetServer returns the server with the given id if it does not exist it returns an empty server. This function checks the server dir too. If it does not exist in the map, and it does in the dir then it will add it to the map. It can error which is catch-able in the frontend
func (c *ServerController) GetServer(id int) (Server, error) {
	server, err := c.GetServerWithError(id, true)
	if err != nil {
		newErr := fmt.Errorf("Failed getting server: " + strconv.Itoa(id) + " with: " + err.Error())
		runtime.LogError(c.ctx, newErr.Error())
		return *server, newErr
	}
	return *server, nil
}

// CreateServer Creates a new server, returns it and adds it to the map. If it fails it returns an error which is catch-able in the Frontend.
func (c *ServerController) CreateServer(saveToConfig bool) (Server, error) {
	_, server, err := c.CreateServerWithError(saveToConfig)
	if err != nil {
		newErr := fmt.Errorf("Failed saving new server: " + err.Error())
		runtime.LogError(c.ctx, newErr.Error())
		return Server{}, newErr
	}
	return *server, nil

}

// DeleteServerFiles deletes the server files from the server with the given id. If it fails it returns an error which is catch-able
func (c *ServerController) DeleteServerFiles(id int) error {
	err := c.DeleteServerFilesWithError(id)
	if err != nil {
		newErr := fmt.Errorf("Failed deleting server: " + err.Error())
		runtime.LogError(c.ctx, newErr.Error())
		return newErr
	}
	return nil

}

// DeleteProfile deletes the profile with the given id. If it fails it returns an error which is catch-able
func (c *ServerController) DeleteProfile(id int) error {
	err := c.DeleteProfileWithError(id)
	if err != nil {
		newErr := fmt.Errorf("Failed deleting profile: " + err.Error())
		runtime.LogError(c.ctx, newErr.Error())
		return newErr
	}
	return nil

}

//region Private

// getServerFromDir gets the server from the server dir if it does not exist and shouldReturnNew is true it returns a new server.
func (c *ServerController) getServerFromDir(id int, shouldReturnNew bool) (Server, error) {
	serverDir := path.Join(c.serverDir, strconv.Itoa(id))

	/*// Check if config dir exists
	if _, err := os.Stat(serverDir); os.IsNotExist(err) {
		runtime.LogDebug(c.ctx, "Server config "+strconv.Itoa(id)+" does not exist, returning new server instance")
		return Server{
			id: id,
		}
	}*/

	// If config file does not exist return a new instance
	_, err := os.Stat(path.Join(serverDir, configFileName))
	if err != nil {
		if os.IsNotExist(err) {
			if shouldReturnNew {
				runtime.LogDebug(c.ctx, "Server config "+strconv.Itoa(id)+" does not exist, returning new server instance")
				return Server{
					Id: id,
				}, nil
			} else {
				runtime.LogDebug(c.ctx, "Server config "+strconv.Itoa(id)+" does not exist, returning empty server instance, with error \"Server does not exist\"")
				return Server{}, fmt.Errorf("server does not exist")
			}
		}
		return Server{}, fmt.Errorf("Error reading server config file: " + err.Error())
	}

	//It exists so read the file
	scf, err := os.ReadFile(path.Join(serverDir, configFileName))
	if err != nil {
		return Server{}, fmt.Errorf("Error reading server config file: " + err.Error())
	}

	serv := Server{}

	err = json.Unmarshal(scf, &serv)
	if err != nil {
		return Server{}, fmt.Errorf("Error unmarshalling server config file: " + err.Error())
	}

	// Check if server is correct.
	if err := CheckIfServerCorrect(serv); err != nil {
		return Server{}, fmt.Errorf("Parsing server instance failed: " + err.Error())
	}

	return serv, nil
}

//endregion

//endregion

//endregion

//region Server starting & stopping

// StartServer starts the server. It will only start a server that exists in the map
func (c *ServerController) StartServer(id int) error {

	server, exists := c.Servers[id]
	if !exists {
		err := fmt.Errorf("error starting server " + strconv.Itoa(id) + ": server does not exist in map")
		runtime.LogError(c.ctx, err.Error())
		return err
	}

	err := server.Start()
	if err != nil {
		err := fmt.Errorf("error starting server " + strconv.Itoa(id) + ": " + err.Error())
		runtime.LogError(c.ctx, err.Error())
		return err
	}

	return nil
}

func (c *ServerController) ForceStopServer(id int) error {

	server, exists := c.Servers[id]
	if !exists {
		err := fmt.Errorf("error stopping server " + strconv.Itoa(id) + ": server does not exist in map")
		runtime.LogError(c.ctx, err.Error())
		return err
	}

	err := server.ForceStop()
	if err != nil {
		err := fmt.Errorf("error stopping server " + strconv.Itoa(id) + ": " + err.Error())
		runtime.LogError(c.ctx, err.Error())
		return err
	}

	return nil
}

func (c *ServerController) GetServerStatus(id int) (bool, error) {
	server, exists := c.Servers[id]
	if !exists {
		err := fmt.Errorf("error getting server status " + strconv.Itoa(id) + ": server does not exist in map")
		runtime.LogError(c.ctx, err.Error())
		return false, err
	}

	status := server.IsServerRunning()
	return status, nil
}

//endregion

//region ServerController helper functions

// CheckServerInstalled Checks if the server dir exists.
//
// TODO: This should check more in depth.
func (c *ServerController) CheckServerInstalled(id int) (bool, error) {

	server := c.Servers[id]

	if _, err := os.Stat(path.Join(server.ServerPath, "\\ShooterGame\\Binaries\\Win64\\ArkAscendedServer.exe")); os.IsNotExist(err) {
		return false, nil
	} else if err != nil {
		return false, fmt.Errorf("error checking if server installed: %v", err)
	}

	return true, nil
}

//endregion
