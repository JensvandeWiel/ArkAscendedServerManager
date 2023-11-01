package helpers

import (
	"fmt"
	"github.com/gorcon/rcon"
	"strconv"
)

func (c *HelpersController) SendRconCommand(command string, ip string, port int, password string) (string, error) {
	conn, err := rcon.Dial(ip+":"+strconv.Itoa(port), password)
	if err != nil {
		return "", fmt.Errorf("failed connectting to rcon server: %v", err)
	}
	defer conn.Close()

	response, err := conn.Execute(command)
	if err != nil {
		return "", fmt.Errorf("failed executing rcon command: %v", err)
	}

	return response, nil
}
