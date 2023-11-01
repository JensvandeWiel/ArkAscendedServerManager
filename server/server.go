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
	Command *exec.Cmd `json:"-"`

	//CONFIGURATION VARIABLES

	// Id is the id of the server
	Id int `json:"id"`

	// ServerAlias is the name of the config (an alias for the server)
	ServerAlias string `json:"serverAlias"`

	// ServerPath is the path where the server is installed/should be.
	ServerPath string `json:"serverPath"`

	//Server Name and Passwords
	ServerName string `json:"serverName"`

	ServerPassword    string `json:"serverPassword"` //TODO: Implement in startup/config
	AdminPassword     string `json:"adminPassword"`
	SpectatorPassword string `json:"spectatorPassword"` //TODO: Implement in startup/config

	//Server Networking
	IpAddress  string `json:"ipAddress"`
	ServerPort int    `json:"serverPort"`
	PeerPort   int    `json:"peerPort"` //TODO: Implement in startup/config
	QueryPort  int    `json:"queryPort"`
	RCONPort   int    `json:"rconPort"`

	//Server configuration
	ServerMap  string `json:"serverMap"`
	MaxPlayers int    `json:"maxPlayers"`
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

	if s.Command != nil {
		return fmt.Errorf("error starting server: server is already started")
	}

	s.Command = exec.Command(path.Join(s.ServerPath, "ShooterGame\\Binaries\\Win64\\ArkAscendedServer.exe"), s.CreateArguments())
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

	s.Command = nil

	return nil
}

func (s *Server) CreateArguments() string {
	basePrompt := s.ServerMap + "?listen"
	basePrompt += "?MultiHome=" + s.IpAddress
	basePrompt += "?Port=" + strconv.Itoa(s.ServerPort)
	basePrompt += "?QueryPort=" + strconv.Itoa(s.QueryPort)
	basePrompt += "?RCONEnabled=true?RCONServerGameLogBuffer=600?RCONPort=" + strconv.Itoa(s.RCONPort)
	basePrompt += "?MaxPlayers=" + strconv.Itoa(s.MaxPlayers)
	basePrompt += "?ServerAdminPassword=" + s.AdminPassword

	return basePrompt
}
