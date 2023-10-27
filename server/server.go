package server

// Server contains the server "stuff"
type Server struct {
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
