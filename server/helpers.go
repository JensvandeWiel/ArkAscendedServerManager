package server

import (
	"github.com/JensvandeWiel/ArkAscendedServerManager/helpers"
	"github.com/sethvargo/go-password/password"
	"github.com/wailsapp/wails/v2/pkg/runtime"
	"strconv"
)

// region Local Helpers
// findHighestKey returns the highest key in a map with int as key
func findHighestKey(m map[int]Server) int {
	var highestKey int = -1

	for key := range m {
		if key > highestKey {
			highestKey = key
		}
	}

	return highestKey
}

func generateNewDefaultServer(id int) Server {

	serverPassword, err := password.Generate(18, 6, 6, false, false)
	if err != nil {
		serverPassword = ""
	}

	adminPassword, err := password.Generate(18, 6, 6, false, false)
	if err != nil {
		adminPassword = "default"
	}

	spectatorPassword, err := password.Generate(18, 6, 6, false, false)
	if err != nil {
		spectatorPassword = "default"
	}

	return Server{
		Id:          id,
		ServerAlias: "Server " + strconv.Itoa(id),

		ServerName: "A server managed by ArkAscendedServerManager",

		ServerPassword:    serverPassword,
		AdminPassword:     adminPassword,
		SpectatorPassword: spectatorPassword,

		IpAddress:  "0.0.0.0",
		ServerPort: 7777,
		PeerPort:   7778,
		QueryPort:  27015,
		RCONPort:   28015,
	}
}

//endregion

//region Global and Frontend Helpers

func (c *ServerController) GetNetworkInterfacesIp() map[string]string {

	i, err := helpers.GetNetworkInterfaces()
	if err != nil {
		runtime.LogErrorf(c.ctx, "Error getting network interfaces: %s", err.Error())
		i = []helpers.NetworkInterface{}
	}

	ips := make(map[string]string)

	for _, interf := range i {
		ips[interf.Name] = interf.IP.String()
	}

	return ips
}

// GetServerDir returns the server directory
func (c *ServerController) GetServerDir() string {
	return c.serverDir
}

//endregion
