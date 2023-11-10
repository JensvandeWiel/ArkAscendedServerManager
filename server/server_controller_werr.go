package server

import (
	"encoding/json"
	"fmt"
	"github.com/wailsapp/wails/v2/pkg/runtime"
	"os"
	"path"
	"strconv"
)

//TODO refactor somehow
//THIS FILE CONTAINS FUNCTIONS THAT SHOULD NOT BE CALLED BY THE FRONTEND AKA WITH ERROR (returns error without logging)
//This is because these functions don't log errors to the log, the wrapper functions in server_controller.go are used for this

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

	err = CopyAndMakeOld(path.Join(serverDir, configFileName))
	if err != nil {
		return err
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
