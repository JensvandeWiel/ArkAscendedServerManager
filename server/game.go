package server

import (
	"errors"
	ini "github.com/JensvandeWiel/ark-ini"
	"os"
	"path/filepath"
)

// getGame returns the Game.ini file as an ini.IniFile struct
func (s *Server) getGame() (*ini.IniFile, error) {
	gamePath := filepath.Join(s.ServerPath, "ShooterGame", "Saved", "Config", "WindowsServer", "Game.ini")

	gameContent, err := os.ReadFile(gamePath)
	if err != nil {
		return nil, errors.New("error reading Game.ini: " + err.Error())
	}

	game, err := ini.DeserializeIniFile(string(gameContent) /*insert allowed duplicate keys here*/)
	if err != nil {
		return nil, err
	}

	//s.updateGame(game)

	return game, nil
}

// saveGame saves the GameUserSettings.ini file to disk
func (s *Server) saveGame(game *ini.IniFile) error {
	gamePath := filepath.Join(s.ServerPath, "ShooterGame", "Saved", "Config", "WindowsServer", "Game.ini")

	//s.updateGame(game)

	err := os.WriteFile(gamePath, []byte(game.ToString()), 0644)
	if err != nil {
		return err
	}

	return nil
}

/*// updateGame updates the GameUserSettings.ini file with the settings from the server struct (settings that should be manually set)
func (s *Server) updateGame(game *ini.IniFile) {
	{
		serverSettings := game.GetOrCreateSection("ServerSettings")

		serverSettings.AddOrReplaceKey("RCONEnabled", true)
		serverSettings.AddOrReplaceKey("RCONPort", s.RCONPort)
		serverSettings.AddOrReplaceKey("ServerAdminPassword", s.AdminPassword)
		serverSettings.AddOrReplaceKey("ActiveMods", s.Mods)
	}

	{
		sessionSettings := game.GetOrCreateSection("SessionSettings")

		sessionSettings.AddOrReplaceKey("MultiHome", true)
		sessionSettings.AddOrReplaceKey("Port", s.ServerPort)
		sessionSettings.AddOrReplaceKey("QueryPort", s.QueryPort)
		sessionSettings.AddOrReplaceKey("SessionName", s.ServerName)
	}

	{
		multiHome := game.GetOrCreateSection("MultiHome")
		multiHome.AddOrReplaceKey("MultiHome", true)
	}

	{
		scriptEngineGameSession := game.GetOrCreateSection("/Script/Engine.GameSession")

		scriptEngineGameSession.AddOrReplaceKey("MaxPlayers", s.MaxPlayers)
	}

}*/

func (s *Server) updateValueInGame(sectionName string, keyName string, value interface{}) error {
	game, err := s.getGame()
	if err != nil {
		return err
	}

	game.SafelyAddKeyToSection(sectionName, keyName, value)

	err = s.saveGame(game)
	if err != nil {
		return err
	}

	return nil
}

func (s *Server) getGameAsMap() (map[string]map[string]string, error) {
	game, err := s.getGame()
	if err != nil {
		return nil, err
	}

	return ini.ToMap(game), nil
}

func (s *Server) saveGameFromMap(gameMap map[string]map[string]string) error {
	game := ini.DeserializeFromMap(gameMap)

	err := s.saveGame(game)
	if err != nil {
		return err
	}

	return nil
}
