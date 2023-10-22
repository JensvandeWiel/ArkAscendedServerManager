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
	Servers   map[int]Server
	serverDir string
}

// NewServerController creates a new ServerController application struct
func NewServerController() *ServerController {
	return &ServerController{
		Servers: make(map[int]Server),
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

// GetServer returns the server with the given id if it does not exist it returns an empty server. This function checks the server dir too. If it does not exist in the map, and it does in the dir then it will add it to the map.
func (c *ServerController) GetServer(id int) Server {
	server, err := c.GetServerWithError(id)
	if err != nil {
		runtime.LogError(c.ctx, "Failed getting server: "+strconv.Itoa(id)+" with: "+err.Error())
		return server
	}
	return server
}

// GetServerWithError returns the server with the given id if it does not exist it returns an empty server. This function checks the server dir too. If it does not exist in the map, and it does in the dir then it will add it to the map.
func (c *ServerController) GetServerWithError(id int) (Server, error) {
	server, exists := c.Servers[id]
	// If it does not exist in the map check the server dir
	if !exists {
		s, err := c.getServerFromDir(id, false)
		if err != nil {
			return Server{}, fmt.Errorf("Error getting server instance: %s", err.Error())
		}

		c.Servers[id] = s
		runtime.EventsEmit(c.ctx, "gotServer", s.Id)
		return s, nil
	} else {
		runtime.EventsEmit(c.ctx, "gotServer", server.Id)
		return server, nil
	}
}

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

	return serv, nil
}

// CreateServer Creates a new server, returns it and adds it to the map, it also returns the key. If it fails it returns false and an empty server
func (c *ServerController) CreateServer(saveToConfig bool) (Server, bool, int) {
	id, server, err := c.CreateServerWithError(saveToConfig)
	if err != nil {
		runtime.LogError(c.ctx, "Failed saving new server: "+err.Error())
		return Server{}, false, -1
	}
	return server, true, id

}

// CreateServerWithError Creates a new server, returns it and adds it to the map, it also returns the key. If it fails it returns an error and an empty server and int -1
func (c *ServerController) CreateServerWithError(saveToConfig bool) (int, Server, error) {
	// get the highest in to generate new id
	maxKey := findHighestKey(c.Servers)
	id := maxKey + 1
	if _, exists := c.Servers[id]; exists {
		return -1, Server{}, fmt.Errorf("Found a server with an key higher than the maximum key, max key: " + strconv.Itoa(maxKey) + " exists: " + strconv.Itoa(id))
	}

	// Check if it exists
	if _, exists := c.Servers[id]; exists {
		return -1, Server{}, fmt.Errorf("Found a server with new key in c.Servers key: " + strconv.Itoa(id))
	}

	NewServer := generateNewDefaultServer(id)
	c.Servers[id] = NewServer

	if saveToConfig {
		err := c.SaveServerWithError(c.Servers[id])
		if err != nil {
			// Don't handle this error because the server is already made, just mention it.
			runtime.LogError(c.ctx, "Failed saving new server: "+err.Error())
		}
	}

	runtime.EventsEmit(c.ctx, "serverCreated", NewServer.Id)

	return id, NewServer, nil
}

// SaveServer saves the server with the given id, and returns bool if successful
func (c *ServerController) SaveServer(server Server) bool {
	err := c.SaveServerWithError(server)
	if err != nil {
		runtime.LogError(c.ctx, "Error saving server: "+err.Error())
		return false
	}
	return true
}

// SaveServerWithError saves the server, and returns an error if it fails
func (c *ServerController) SaveServerWithError(server Server) error {
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

// GetAllServers gets all servers and saves them to ServerController.Servers and also returns them, if it fails it returns nil and false. If they already exist in the map it will just get that.
func (c *ServerController) GetAllServers() (map[int]Server, bool) {

	servers, err := c.GetAllServersWithError()
	if err != nil {
		runtime.LogError(c.ctx, "Failed to get all servers "+c.serverDir+" error: "+err.Error())
		return nil, false
	}

	c.Servers = servers
	return c.Servers, true
}

// GetAllServersWithError gets all servers and saves them to ServerController.Servers and also returns them, if it fails it returns nil and false. If they already exist in the map it will just get that.
func (c *ServerController) GetAllServersWithError() (map[int]Server, error) {
	allServerDir := c.serverDir

	children, err := os.ReadDir(allServerDir)
	if err != nil {
		return nil, fmt.Errorf("Failed to read children in " + c.serverDir + " error: " + err.Error())
	}

	servers := make(map[int]Server)

	for _, child := range children {
		if child.IsDir() {
			index, err := strconv.Atoi(child.Name())
			if err != nil {
				return nil, fmt.Errorf("Failed to parse to int: " + child.Name() + " error: " + err.Error())
			}

			server, err := c.GetServerWithError(index)
			if err != nil {
				return nil, fmt.Errorf("Failed to get server: " + child.Name() + " error: " + err.Error())
			}

			servers[index] = server
		}
	}

	c.Servers = servers

	return c.Servers, nil
}

// GetAllServersFromDir gets all servers from dir and saves them to ServerController.Servers and also returns them, if it fails it returns nil and false. This will overwrite c.Servers!
func (c *ServerController) GetAllServersFromDir() (map[int]Server, bool) {
	allserverDir := c.serverDir

	children, err := os.ReadDir(allserverDir)
	if err != nil {
		runtime.LogError(c.ctx, "Failed to read children in "+c.serverDir+" error: "+err.Error())
	}

	servers := make(map[int]Server)

	for _, child := range children {
		if child.IsDir() {
			index, err := strconv.Atoi(child.Name())
			if err != nil {
				runtime.LogError(c.ctx, "Failed to parse to int: "+child.Name()+" error: "+err.Error())
				return nil, false
			}

			server, err := c.getServerFromDir(index, false)
			if err != nil {
				runtime.LogError(c.ctx, "Failed to get server: "+child.Name()+" error: "+err.Error())
				return nil, false
			}
			servers[index] = server
		}
	}

	c.Servers = servers

	return c.Servers, true
}

// Server contains the server "stuff"
type Server struct {
	Id          int    `json:"id"`
	ServerAlias string `json:"serverAlias"`

	//Server Name and Passwords
	ServerName string `json:"serverName"`

	ServerPassword    string `json:"serverPassword"`
	AdminPassword     string `json:"adminPassword"`
	SpectatorPassword string `json:"spectatorPassword"`

	//Server Networking
	IpAddress  string `json:"ipAddress"`
	ServerPort int    `json:"serverPort"`
	PeerPort   int    `json:"peerPort"`
	QueryPort  int    `json:"queryPort"`
	RCONPort   int    `json:"rconPort"`
}
