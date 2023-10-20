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

// GetServer returns the server with the given id if it does not exist it returns a new server. This function checks the server dir too. If it does not exist in the map it and does in the dir then it will add it to the map.
func (c *ServerController) GetServer(id int) Server {
	server, exists := c.Servers[id]
	// If it does not exist in the map check the server dir
	if !exists {
		s, err := c.getServerFromDir(id)
		if err != nil {
			runtime.LogErrorf(c.ctx, "Error getting server instance: %s", err.Error())
			return Server{
				Id: id,
			}
		}

		c.Servers[id] = s
		return s
	} else {
		return server
	}
}

// getServerFromDir gets the server from the server dir if it does not exist it returns a new server.
func (c *ServerController) getServerFromDir(id int) (Server, error) {
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
			runtime.LogDebug(c.ctx, "Server config "+strconv.Itoa(id)+" does not exist, returning new server instance")
			return Server{
				Id: id,
			}, nil
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

// SaveServer saves the server with the given id, and returns if successful
func (c *ServerController) SaveServer(server Server) bool {
	c.Servers[server.Id] = server
	serverDir := path.Join(c.serverDir, strconv.Itoa(server.Id))

	serverFile, err := json.MarshalIndent(server, "", " ")
	if err != nil {
		runtime.LogError(c.ctx, "Error marshalling config file: "+err.Error())
		return false
	}

	// If config dir does not exist create a new one
	_, err = os.Stat(serverDir)
	if err != nil {
		if os.IsNotExist(err) {
			runtime.LogDebug(c.ctx, "Server config "+strconv.Itoa(server.Id)+" directory does not exist, creating it")
			if err := os.MkdirAll(serverDir, os.ModePerm); err != nil {
				runtime.LogError(c.ctx, "Error creating server dir: "+serverDir+"with error: "+err.Error())
				return false
			}
		}
	}

	err = os.WriteFile(path.Join(serverDir, configFileName), serverFile, 0644)

	if err != nil {
		runtime.LogError(c.ctx, "Error writing config file: "+err.Error())
		return false
	}
	return true
}

// Server contains the server "stuff"
type Server struct {
	Id   int    `json:"id"`
	Test string `json:"test"`
}
