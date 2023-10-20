package helpers

import (
	"net"
)

// This file contains networking "helper functions"

type NetworkInterface struct {
	Name string
	IP   net.IP
}

func GetNetworkInterfaces() ([]NetworkInterface, error) {
	interfaces, err := net.Interfaces()
	if err != nil {
		return nil, err
	}

	var networkInterfaces []NetworkInterface

	for _, iface := range interfaces {
		addrs, err := iface.Addrs()
		if err != nil {
			return nil, err
		}

		for _, addr := range addrs {
			ipNet, ok := addr.(*net.IPNet)
			if ok && !ipNet.IP.IsLoopback() && ipNet.IP.To4() != nil {
				networkInterfaces = append(networkInterfaces, NetworkInterface{
					Name: iface.Name,
					IP:   ipNet.IP,
				})
			}
		}
	}

	return networkInterfaces, nil
}
