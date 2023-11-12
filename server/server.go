package server

import (
	"context"
	"fmt"
	"os/exec"
	"path"
	"path/filepath"
	"strconv"
	"strings"
	"time"

	"github.com/JensvandeWiel/ArkAscendedServerManager/helpers"
	"github.com/keybase/go-ps"
	"github.com/wailsapp/wails/v2/pkg/runtime"
)

// Server contains the server "stuff"

type Server struct {
	Command   *exec.Cmd `json:"-"`
	ctx       context.Context
	IsRunning bool `json:"-"`
	helpers   *helpers.HelpersController

	//PREFERENCES

	DisableUpdateOnStart  bool   `json:"disableUpdateOnStart"`
	RestartOnServerQuit   bool   `json:"restartOnServerQuit"`
	UseIniConfig          bool   `json:"useIniConfig"`
	DiscordWebHook        string `json:"discordWebHook"`
	DiscordWebHookEnabled bool   `json:"discordWebHookEnabled"`

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

	//INI
	GameUserSettings GameUserSettings `json:"gameUserSettings"`
	Game             Game             `json:"game"`

	ServerMap  string `json:"serverMap"`
	MaxPlayers int    `json:"maxPlayers"`
}

func (s *Server) SetStatus(status bool) {
	s.IsRunning = status
}

// UpdateConfig updates the configuration files for the server e.g.: GameUserSettings.ini
func (s *Server) UpdateConfig() error {

	err := CopyAndMakeOld(filepath.Join(s.ServerPath, "ShooterGame", "Saved", "Config", "WindowsServer", "Game.ini"))
	if err != nil {
		return err
	}
	err = CopyAndMakeOld(filepath.Join(s.ServerPath, "ShooterGame", "Saved", "Config", "WindowsServer", "GameUserSettings.ini"))
	if err != nil {
		return err
	}

	err = s.SaveGameIni()
	if err != nil {
		return err
	}

	err = s.SaveGameUserSettingsIni()
	if err != nil {
		return err
	}

	return nil
}

func (s *Server) GetRestartOnServerQuit() bool {
	return s.RestartOnServerQuit
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
		s.Command = s.CreateServerCmd()
		err = s.Command.Start()
		if err != nil {
			return fmt.Errorf("error starting server: %v", err)
		}
		runtime.EventsEmit(s.ctx, "onServerStart", s.Id)

		s.SetStatus(true)

		if s.DiscordWebHookEnabled {
			err := helpers.SendToDiscord(time.Now().Format(time.RFC822)+" ("+s.ServerAlias+") Server has started", s.DiscordWebHook)
			if err != nil {
				runtime.LogError(s.ctx, "Error sending message to discord: "+err.Error())
			}
		}
		go func() {
			_ = s.Command.Wait()

			runtime.EventsEmit(s.ctx, "onServerExit", s.Id)

			if s.DiscordWebHookEnabled {
				err := helpers.SendToDiscord(time.Now().Format(time.RFC822)+" ("+s.ServerAlias+") Server has stopped", s.DiscordWebHook)
				if err != nil {
					runtime.LogError(s.ctx, "Error sending message to discord: "+err.Error())
				}
			}
		}()
	}

	return nil
}

func (s *Server) HandleCrash() {
	if s.GetRestartOnServerQuit() {
		// 3221225786 is the exit code for an intended exit (i hope)
		if s.IsRunning {
			runtime.LogError(s.ctx, fmt.Sprintf("Server %v crashed, attempting to restart...", s.Id))
			time.Sleep(2 * time.Second)
			err := s.Start()
			if err != nil {
				runtime.EventsEmit(s.ctx, "onRestartServerFailed", err)
			}
		}
	}
}

// CreateServerCmd returns the command to start the server
func (s *Server) CreateServerCmd() *exec.Cmd {
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

	s.SetStatus(false)
	err := s.Command.Process.Kill()
	if err != nil {
		return fmt.Errorf("error stopping server: %v", err)
	}

	if s.DiscordWebHookEnabled {
		err := helpers.SendToDiscord(time.Now().Format(time.RFC822)+" ("+s.ServerAlias+") Initiated force stop", s.DiscordWebHook)
		if err != nil {
			runtime.LogError(s.ctx, "Error sending message to discord: "+err.Error())
		}
	}

	return nil
}

func (s *Server) Stop() error {

	if s.DiscordWebHookEnabled {
		err := helpers.SendToDiscord(time.Now().Format(time.RFC822)+" ("+s.ServerAlias+") Initiated stop", s.DiscordWebHook)
		if err != nil {
			runtime.LogError(s.ctx, "Error sending message to discord: "+err.Error())
		}
	}

	_, err := s.helpers.SendRconCommand("saveworld", s.IpAddress, s.RCONPort, s.AdminPassword)
	if err != nil {
		return err
	}
	_, err = s.helpers.SendRconCommand("doexit", s.IpAddress, s.RCONPort, s.AdminPassword)
	if err != nil {
		return err
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
	args = append(args, "?Port="+strconv.Itoa(s.ServerPort))

	/*if s.ServerPassword != "" {
		args = append(args, "?ServerPassword="+s.ServerPassword)
	}
	if s.SpectatorPassword != "" {
		args = append(args, "?SpectatorPassword="+s.SpectatorPassword)
	}*/

	args = append(args, s.ExtraQuestionmarkArguments)

	if s.Mods != "" {
		args = append(args, "-mods="+s.Mods)
	}
	//args = append(args, "?ServerAdminPassword="+s.AdminPassword)

	args = append(args, "-WinLiveMaxPlayers="+strconv.Itoa(s.MaxPlayers))

	extraArgs := strings.Split(s.ExtraDashArgs, " ")

	for _, arg := range extraArgs {
		args = append(args, arg)
	}

	return args
}
