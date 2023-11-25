package server

import (
	"fmt"
	"github.com/StackExchange/wmi"
	"io"
	"io/ioutil"
	"net"
	"os"
	"os/exec"
	"path/filepath"
	runtime2 "runtime"
	"strconv"
	"strings"

	"github.com/JensvandeWiel/ArkAscendedServerManager/helpers"
	"github.com/go-ini/ini"
	"github.com/sethvargo/go-password/password"
	"github.com/wailsapp/wails/v2/pkg/runtime"
)

var iniOpts = ini.LoadOptions{
	AllowShadows: true,
}

func replaceForwardSlashInFile(filePath string) error {
	// Read the content of the file
	content, err := os.ReadFile(filePath)
	if err != nil {
		return fmt.Errorf("error reading file: %v", err)
	}

	// Convert content to string
	contentString := string(content)

	// Replace all occurrences of %2F with /
	replacedString := strings.Replace(contentString, "/", "%2F", -1)

	// Write the modified content back to the file
	err = ioutil.WriteFile(filePath, []byte(replacedString), 0644)
	if err != nil {
		return fmt.Errorf("error writing to file: %v", err)
	}

	return nil
}

// findHighestKey returns the highest key in a map with int as key
func findHighestKey(m map[int]*Server) int {
	var highestKey int = -1

	for key := range m {
		if key > highestKey {
			highestKey = key
		}
	}

	return highestKey
}

func generateNewDefaultServer(id int) Server {

	/*serverPassword, err := password.Generate(18, 6, 6, false, false)
	if err != nil {
		serverPassword = ""
	}*/

	adminPassword, err := password.Generate(18, 9, 0, false, false)
	if err != nil {
		adminPassword = "default"
	}

	/*spectatorPassword, err := password.Generate(18, 6, 6, false, false)
	if err != nil {
		spectatorPassword = "default"
	}*/

	return Server{
		Id: id,

		DisableUpdateOnStart: false,
		RestartOnServerQuit:  true,

		ExtraQuestionmarkArguments: "",
		ExtraDashArgs:              "",

		Mods: "",

		ServerAlias: "Server " + strconv.Itoa(id),

		ServerName: "A server managed by ArkAscendedServerManager",

		ServerPassword:    "",
		AdminPassword:     adminPassword,
		SpectatorPassword: "",

		IpAddress:  "0.0.0.0",
		ServerPort: 7777,
		PeerPort:   7778,
		QueryPort:  27015,
		RCONPort:   28015,

		GameUserSettings: generateNewDefaultGameUserSettings(),
		Game:             generateNewDefaultGame(),

		ServerMap:  "TheIsland_WP",
		MaxPlayers: 70,

		StartWithApplication: false,
	}
}

func CheckIfServerCorrect(server *Server) error {
	if server.Id < 0 {
		return fmt.Errorf("Checks failed: Server.Id is negative")
	}
	if server.ServerAlias == "" {
		return fmt.Errorf("Checks failed: Server.ServerAlias is empty")
	}

	if server.ServerName == "" {
		return fmt.Errorf("Checks failed: Server.ServerName is empty")
	}

	//serverpassword can be empty
	if server.ServerPassword == "" {
		//return fmt.Errorf("Checks failed: Server.ServerPassword is empty, this must be set")
	}

	// adminpassword must not be empty, because we need it for stopping the server and rcon
	if server.AdminPassword == "" {
		return fmt.Errorf("Checks failed: Server.AdminPassword is empty, you should set an admin password")
	}

	if server.IpAddress == "" {
		return fmt.Errorf("Checks failed: Server.IpAddress is empty.")
	} else {
		if server.IpAddress != "0.0.0.0" {
			interfaces, err := helpers.GetNetworkInterfaces()

			if err != nil {
				return fmt.Errorf("Check failed: Failed to get Network Interfaces: %v", err)
			}

			serverIp := net.ParseIP(server.IpAddress)

			found := false
			for _, iface := range interfaces {
				if iface.IP.Equal(serverIp) {
					found = true
					break
				}
			}
			if !found {
				return fmt.Errorf("Check failed: Ip address not found in system interfaces: %v", server.IpAddress)
			}

			if err := CheckServerPorts(server); err != nil {
				return fmt.Errorf("Check failed: ports failed to parse: %v", err)
			}
		}
	}

	if server.ServerMap == "" {
		return fmt.Errorf("server.serverMap is empty")
	}

	return nil
}

// CheckServerPorts validates the port numbers within a Server struct.
// It ensures that all port numbers are within the valid range (greater than 0 and less than or equal to 65535)
// and that there are no duplicate port numbers.
func CheckServerPorts(server *Server) error {
	ports := []int{
		server.ServerPort,
		server.PeerPort,
		server.QueryPort,
		server.RCONPort,
	}

	portCount := make(map[int]int)

	for _, port := range ports {
		if port <= 0 || port > 65535 {
			return fmt.Errorf("Port number out of range: " + string(port))
		}

		if _, exists := portCount[port]; exists {
			return fmt.Errorf("Duplicate port found: " + string(port))
		}

		portCount[port] = 1
	}

	return nil
}

func CopyAndMakeOld(path string) error {
	if _, err := os.Stat(path); os.IsNotExist(err) {
		// File does not exist, return nil
		return nil
	}
	// Open the source file
	originalFile, err := os.Open(path)
	if err != nil {
		return err
	}
	defer originalFile.Close()

	// Create the destination file
	newFile, err := os.Create(path + ".old")
	if err != nil {
		return err
	}
	defer newFile.Close()

	// Copy the contents of the source file to the destination file
	_, err = io.Copy(newFile, originalFile)
	if err != nil {
		return err
	}
	return nil
}

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

func ensureFilePath(filePath string) error {
	// Check if the file already exists
	if _, err := os.Stat(filePath); os.IsNotExist(err) {
		// File does not exist, create it along with required directories

		// Create any required directories
		err := os.MkdirAll(filepath.Dir(filePath), os.ModePerm)
		if err != nil {
			return err
		}

		// Create the file
		file, err := os.Create(filePath)
		if err != nil {
			return err
		}
		defer file.Close()

		// File created successfully
		return nil

	} else if err != nil {
		// There was an error checking the file's existence
		return err
	}

	// File already exists
	return nil
}

type Win32_Process struct {
	ProcessID      uint32
	ExecutablePath *string
}

func IsProcessRunningWithPath(processPath string) (bool, error) {
	var processes []Win32_Process

	// Use WMI to query processes
	query := "SELECT ProcessID, ExecutablePath FROM Win32_Process"
	err := wmi.QueryNamespace(query, &processes, `root\CIMv2`)
	if err != nil {
		return false, err
	}

	for _, process := range processes {
		if process.ExecutablePath != nil && strings.EqualFold(filepath.Clean(filepath.FromSlash(*process.ExecutablePath)), filepath.Clean(filepath.FromSlash(processPath))) {
			return true, nil
		}
	}

	return false, nil
}

func GetProcessPid(processPath string) (uint32, error) {
	var processes []Win32_Process

	// Use WMI to query processes
	query := "SELECT ProcessID, ExecutablePath FROM Win32_Process"
	err := wmi.QueryNamespace(query, &processes, `root\CIMv2`)
	if err != nil {
		return 0, err
	}

	for _, process := range processes {
		if process.ExecutablePath != nil && strings.EqualFold(filepath.Clean(filepath.FromSlash(*process.ExecutablePath)), filepath.Clean(filepath.FromSlash(processPath))) {
			return process.ProcessID, nil
		}
	}

	return 0, nil
}

func KillProcessUsingPid(pid uint32) error {
	var cmd *exec.Cmd

	switch runtime2.GOOS {
	case "windows":
		cmd = exec.Command("taskkill", "/F", "/PID", strconv.FormatUint(uint64(pid), 10))
	default:
		cmd = exec.Command("kill", "-9", strconv.FormatUint(uint64(pid), 10))
	}

	if err := cmd.Run(); err != nil {
		return fmt.Errorf("failed to kill process: %v", err)
	}

	return nil
}

// add a func that checks if asa server api is installed on s.ServerPath + ShooterGame\Binaries\Win64
func (s *Server) checkIfAsaApiInstalled() bool {
	// Check if the file already exists
	if _, err := os.Stat(filepath.Join(s.ServerPath, "ShooterGame", "Binaries", "Win64", "AsaApiLoader.exe")); os.IsNotExist(err) {
		// File does not exist, return false
		return false
	} else if err != nil {
		runtime.LogError(s.ctx, "Error checking if AsaApiLoader.exe exists: "+err.Error())
		// There was an error checking the file's existence
		return false
	}

	// File already exists
	return true

}
