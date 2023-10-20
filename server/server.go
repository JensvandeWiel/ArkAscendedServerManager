package server

import (
	"context"
	"github.com/JensvandeWiel/ArkAscendedServerManager/helpers"
	"github.com/wailsapp/wails/v2/pkg/runtime"
)

// ServerController struct
type ServerController struct {
	ctx     context.Context
	servers []Server
}

// NewServerController creates a new ServerController application struct
func NewServerController() *ServerController {
	return &ServerController{}
}

// Startup is called when the app starts. The context is saved
// so we can call the runtime methods
func (c *ServerController) Startup(ctx context.Context) {
	c.ctx = ctx
}

func (c *ServerController) GetNetworkInterfaces() []helpers.NetworkInterface {

	i, err := helpers.GetNetworkInterfaces()
	if err != nil {
		runtime.LogErrorf(c.ctx, "Error getting network interfaces: %s", err.Error())
		i = []helpers.NetworkInterface{}
	}

	return i
}

// Server contains the server "stuff"
type Server struct {
	id int
}
