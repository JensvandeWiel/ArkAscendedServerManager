package server

import (
	"context"
	"fmt"
	"github.com/keybase/go-ps"
	"github.com/wailsapp/wails/v2/pkg/runtime"
	"os/exec"
	"path"
	"strconv"
	"strings"
)

// Server contains the server "stuff"

type GameUserSettings struct {
}

type Server struct {
	Command *exec.Cmd `json:"-"`
	ctx     context.Context

	//PREFERENCES

	DisableUpdateOnStart bool `json:"disableUpdateOnStart"`
	RestartOnServerQuit  bool `json:"restartOnServerQuit"`

	//CONFIGURATION VARIABLES

	ExtraDashArgs              string `json:"extraDashArgs"`
	ExtraQuestionmarkArguments string `json:"extraQuestionmarkArguments"`

	Mods string `json:"mods"`

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

	if s.IsServerRunning() {
		return fmt.Errorf("error starting server: server is already running")
	} else {
		s.Command = s.GetServerCommand()
		err = s.Command.Start()
		if err != nil {
			return fmt.Errorf("error starting server: %v", err)
		}
		runtime.EventsEmit(s.ctx, "onServerStart", s.Id)
		go func() {
			_ = s.Command.Wait()

			runtime.EventsEmit(s.ctx, "onServerExit", s.Id)

			/*//restart server on crash
			if err != nil && s.RestartOnServerQuit {
				code := s.Command.ProcessState.ExitCode()
				time.Sleep(2 * time.Second)
				if code != 0 {
					err := s.Start()
					if err != nil {
						runtime.EventsEmit(s.ctx, "onRestartServerFailed", err)
					}
				}
			}*/

		}()
	}

	return nil
}

// GetServerCommand returns the command to start the server
func (s *Server) GetServerCommand() *exec.Cmd {
	args := s.CreateArguments()
	return exec.Command(path.Join(s.ServerPath, "ShooterGame\\Binaries\\Win64\\ArkAscendedServer.exe"), args...)
}

// ForceStop forces the server to stop "quitting/killing the process"
func (s *Server) ForceStop() error {

	if s.Command == nil {
		return fmt.Errorf("error stopping server: server is not running")
	}
	if s.Command.Process == nil {
		return fmt.Errorf("error stopping server: server is not running")
	}

	if !s.IsServerRunning() {
		return fmt.Errorf("error stopping server: server is not running")
	}

	err := s.Command.Process.Kill()
	if err != nil {
		return fmt.Errorf("error stopping server: %v", err)
	}

	return nil
}

// GetServerStatus returns the status of the server.
func (s *Server) IsServerRunning() bool {

	if s.Command == nil {
		return false
	}

	if s.Command.Process == nil {
		return false
	}

	// Retrieve a list of all processes.
	processList, err := ps.Processes()
	if err != nil {
		return false
	}

	// Iterate through the list of processes and check if the specified PID exists.
	processFound := false
	for _, process := range processList {
		if process.Pid() == s.Command.Process.Pid {
			processFound = true
			break
		}
	}

	if processFound {
		return true
	} else {
		return false
	}
}

// returns questionamrk arguments for the server and dash arguments for the server
func (s *Server) CreateArguments() []string {
	var args []string = []string{}

	args = append(args, s.ServerMap+"?listen")
	args = append(args, "?MultiHome="+s.IpAddress)
	args = append(args, "?SessionName="+s.ServerName)
	args = append(args, "?Port="+strconv.Itoa(s.ServerPort))
	args = append(args, "?QueryPort="+strconv.Itoa(s.QueryPort))
	args = append(args, "?RCONEnabled=true?RCONServerGameLogBuffer=600?RCONPort="+strconv.Itoa(s.RCONPort))
	args = append(args, "?MaxPlayers="+strconv.Itoa(s.MaxPlayers))
	if s.ServerPassword != "" {
		args = append(args, "?ServerPassword="+s.ServerPassword)
	}
	if s.SpectatorPassword != "" {
		args = append(args, "?SpectatorPassword="+s.SpectatorPassword)
	}

	args = append(args, s.ExtraQuestionmarkArguments)

	//TODO move AdminPassword to ini
	args = append(args, "?ServerAdminPassword="+s.AdminPassword)

	if s.Mods != "" {
		args = append(args, "-mods="+s.Mods)
	}

	extraArgs := strings.Split(s.ExtraDashArgs, " ")

	for _, arg := range extraArgs {
		args = append(args, arg)
	}

	return args
}
