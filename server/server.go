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
	"github.com/wailsapp/wails/v2/pkg/runtime"
)

// Server contains the server "stuff"

type Server struct {
	Command    *exec.Cmd `json:"-"`
	ctx        context.Context
	helpers    *helpers.HelpersController
	stopSignal chan bool

	//PREFERENCES

	DisableUpdateOnStart  bool   `json:"disableUpdateOnStart"`
	RestartOnServerQuit   bool   `json:"restartOnServerQuit"`
	UseIniConfig          bool   `json:"useIniConfig"`
	DiscordWebHook        string `json:"discordWebHook"`
	DiscordWebHookEnabled bool   `json:"discordWebHookEnabled"`

	//CONFIGURATION VARIABLES

	ExtraDashArgs              string `json:"extraDashArgs"`
	ExtraQuestionmarkArguments string `json:"extraQuestionmarkArguments"`
	KickIdlePlayers            bool   `json:"kickIdlePlayers"`

	Mods string `json:"mods"`

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
	PeerPort   int    `json:"peerPort"` //TODO: find out if used
	QueryPort  int    `json:"queryPort"`
	RCONPort   int    `json:"rconPort"`

	//Server configuration

	//INI
	GameUserSettings GameUserSettings `json:"gameUserSettings"`
	Game             Game             `json:"game"`

	ServerMap  string `json:"serverMap"`
	MaxPlayers int    `json:"maxPlayers"`

	StartWithApplication bool `json:"startWithApplication"`
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

//TODO Add configuration parsing/loading/saving for server specific files
//TODO Add startup arguments parsing
//TODO Add check for running application (ensures no accidental duplicated servers, especially with addition of start with application)

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
		if s.DiscordWebHookEnabled {
			err := helpers.SendToDiscord(time.Now().Format(time.RFC822)+" ("+s.ServerAlias+") Server has started", s.DiscordWebHook)
			if err != nil {
				runtime.LogError(s.ctx, "Error sending message to discord: "+err.Error())
			}
		}
		go func() {
			_ = s.Command.Wait()
			runtime.EventsEmit(s.ctx, "onServerExit", s.Id)

			// Check if the server exited unexpectedly (not through Stop or ForceStop)
			select {
			case <-s.stopSignal:
				// Server was intentionally stopped, do not restart
				runtime.LogInfo(s.ctx, "Server was intentionally stopped. Not restarting.")
			default:
				// Restart the server
				time.Sleep(2 * time.Second)
				runtime.EventsEmit(s.ctx, "RestartServer", s.Id)
			}
		}()
	}

	return nil
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

	if pid, err := GetProcessPid(path.Join(s.ServerPath, "ShooterGame\\Binaries\\Win64\\ArkAscendedServer.exe")); err != nil {
		return fmt.Errorf("error stopping server: %v", err)
	} else {
		err := KillProcessUsingPid(pid)
		if err != nil {
			return fmt.Errorf("error killing process: %v", err)
		}
	}

	if s.DiscordWebHookEnabled {
		err := helpers.SendToDiscord(time.Now().Format(time.RFC822)+" ("+s.ServerAlias+") Initiated force stop", s.DiscordWebHook)
		if err != nil {
			runtime.LogError(s.ctx, "Error sending message to discord: "+err.Error())
		}
	}

	s.stopSignal <- true

	return nil
}

func (s *Server) Stop() error {

	if s.DiscordWebHookEnabled {
		err := helpers.SendToDiscord(time.Now().Format(time.RFC822)+" ("+s.ServerAlias+") Initiated stop", s.DiscordWebHook)
		if err != nil {
			runtime.LogError(s.ctx, "Error sending message to discord: "+err.Error())
		}
	}

	err := s.SaveWorld()
	if err != nil {
		return fmt.Errorf("error sending save world command: %v", err)
	}
	_, err = s.helpers.SendRconCommand("doexit", s.IpAddress, s.RCONPort, s.AdminPassword)
	if err != nil {
		return fmt.Errorf("error sending exit command: %v", err)
	}

	s.stopSignal <- true

	return nil
}

//TODO BROKEN

// GetServerStatus returns the status of the server.
func (s *Server) IsServerRunning() bool {

	//THIS CHECK IS ONLY HERE BECAUSE WE DO NOT OFFICIALY SUPPORT MANAGING SERVERS THAT ARE ALREADY STARTED (WILL BE POSSIBLE IN THE FUTURE)
	if s.Command == nil || s.Command.Process == nil {
		return false
	}

	isRunning, err := IsProcessRunningWithPath(path.Join(s.ServerPath, "ShooterGame\\Binaries\\Win64\\ArkAscendedServer.exe"))
	if err != nil {
		return false
	}
	return isRunning
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
	if s.KickIdlePlayers {
		args = append(args, "-EnableIdlePlayerKick")
	}

	extraArgs := strings.Split(s.ExtraDashArgs, " ")

	for _, arg := range extraArgs {
		args = append(args, arg)
	}

	return args
}

// save the world
func (s *Server) SaveWorld() error {
	_, err := s.helpers.SendRconCommand("saveworld", s.IpAddress, s.RCONPort, s.AdminPassword)
	if err != nil {
		return err
	}
	return nil
}

func getPlayerCount(s Server) (int, error) {
	players, err := s.helpers.SendRconCommand("listplayers", s.IpAddress, s.RCONPort, s.AdminPassword)
	if err != nil {
		return 0, err
	}

	if strings.HasPrefix(players, "No Players Connected") {
		return 0, nil
	}

	// This returns the following format:
	// 0. Username, ARK-Internal-ID
	// So if more information is needed, it can be extracted
	lines := strings.Split(players, "\n")
	var nonEmptyLines []string
	for _, line := range lines {
		if strings.TrimSpace(line) != "" {
			nonEmptyLines = append(nonEmptyLines, line)
		}
	}

	return len(nonEmptyLines), nil
}

func (s *Server) GetServerPlayerCount() (int, error) {
	playerCount, err := getPlayerCount(*s)

	if err != nil {
		return 0, err
	}

	return playerCount, nil
}
