package server

// Server contains the server "stuff"
type Server struct {
	Id          int    `json:"id"`
	ServerAlias string `json:"serverAlias"`

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
