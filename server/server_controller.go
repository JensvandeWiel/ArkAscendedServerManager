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

func (c *ServerController) GetServerStartupCommand(id int) (string, error) {
	server, err := c.GetServerWithError(id, false)
	if err != nil {
		err := fmt.Errorf("error getting server startup command " + strconv.Itoa(id) + ": server does not exist")
		runtime.LogError(c.ctx, err.Error())
		return "error getting server startup command " + strconv.Itoa(id) + ": server does not exist", err
	}
	return server.CreateServerCmd().String(), nil
}

//endregion
