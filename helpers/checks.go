package helpers

import (
	"fmt"
	"github.com/JensvandeWiel/ArkAscendedServerManager/server"
	"net"
)

func CheckIfServerCorrect(server server.Server) error {
	if server.Id < 0 {
		return fmt.Errorf("Checks failed: Server.Id is negative")
	}
	if server.ServerAlias == "" {
		return fmt.Errorf("Checks failed: Server.ServerAlias is empty")
	}

	if server.ServerName == "" {
		return fmt.Errorf("Checks failed: Server.ServerName is empty")
	}

	if server.ServerPassword != "" && len(server.ServerPassword) < 8 {
		return fmt.Errorf("Checks failed: Server.ServerPassword is too short")
	}

	if server.AdminPassword == "" {
		return fmt.Errorf("Checks failed: Server.AdminPassword is empty you should set an admin password")
	}
	if len(server.AdminPassword) < 8 {
		return fmt.Errorf("Checks failed: Server.AdminPassword is too short.")
	}

	if server.IpAddress != "0.0.0.0" {
		interfaces, err := GetNetworkInterfaces()

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

		if err := CheckServerPorts(&server); err != nil {
			return fmt.Errorf("Check failed: ports failed to parse: %v", err)
		}
	}

	return nil
}

// CheckServerPorts validates the port numbers within a Server struct.
// It ensures that all port numbers are within the valid range (greater than 0 and less than or equal to 65535)
// and that there are no duplicate port numbers.
func CheckServerPorts(server *server.Server) error {
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
