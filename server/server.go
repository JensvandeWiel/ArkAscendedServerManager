package server

import (
	"fmt"
	"os/exec"
	"path"
	"strconv"
)

// Server contains the server "stuff"

type GameUserSettings struct {
}

type Server struct {
	Command *exec.Cmd

	//CONFIGURATION VARIABLES

	// Id is the id of the server
	Id int `json:"id"`

	// ServerAlias is the name of the config (an alias for the server)
	ServerAlias string `json:"serverAlias"`

	// ServerPath is the path where the server is installed/should be.
	ServerPath string `json:"serverPath"`

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

// UpdateConfig updates the configuration files for the server e.g.: GameUserSettings.ini
func (s *Server) UpdateConfig() error {

	return nil
}

//TODO Add configuration parsing/loading/saving for server specific files
//TODO Add startup arguments parsing

func (s *Server) Start() error {

	err := s.UpdateConfig()
	if err != nil {
		return fmt.Errorf("error starting server: failed updating server configuration: %v", err)
	}

	args := " TheIsland_WP?listen?SessionName=" + s.ServerName + "?Port=" + strconv.Itoa(s.ServerPort) + "?QueryPort=" + strconv.Itoa(s.QueryPort) + "?RCONEnabled=True?RCONServerGameLogBuffer=600?RCONPort=" + strconv.Itoa(s.RCONPort) + "?MaxPlayers=32?ServerAdminPassword=" + s.AdminPassword

	s.Command = exec.Command(path.Join(s.ServerPath, "ShooterGame\\Binaries\\Win64\\ArkAscendedServer.exe"), args)
	err = s.Command.Start()
	if err != nil {
		return fmt.Errorf("error starting server: %v", err)
	}

	return nil
}

// ForceStop forces the server to stop "quitting/killing the process"
func (s *Server) ForceStop() error {

	if s.Command == nil {
		return fmt.Errorf("error stopping server: s.Command is nil")
	}
	err := s.Command.Process.Kill()
	if err != nil {
		return fmt.Errorf("error stopping server: %v", err)
	}

	return nil
}
