package server

import (
	"errors"
	ini "github.com/JensvandeWiel/ark-ini"
	"os"
	"path/filepath"
)

var duplicateKeys = []string{}

// getGus returns the GameUserSettings.ini file as an ini.IniFile struct
func (s *Server) getGus() (*ini.IniFile, error) {
	gusPath := filepath.Join(s.ServerPath, "ShooterGame", "Saved", "Config", "WindowsServer", "GameUserSettings.ini")

	gusContent, err := os.ReadFile(gusPath)
	if err != nil {
		return nil, errors.New("error reading GameUserSettings.ini: " + err.Error())
	}

	gus, err := ini.DeserializeIniFile(string(gusContent), duplicateKeys... /*insert allowed duplicate keys here*/)
	if err != nil {
		return nil, err
	}

	s.updateGus(gus)

	return gus, nil
}

// saveGus saves the GameUserSettings.ini file to disk
func (s *Server) saveGus(gus *ini.IniFile) error {
	gusPath := filepath.Join(s.ServerPath, "ShooterGame", "Saved", "Config", "WindowsServer", "GameUserSettings.ini")
	s.updateGus(gus)
	err := os.WriteFile(gusPath, []byte(gus.ToString()), 0644)
	if err != nil {
		return err
	}

	return nil
}

// updateGus updates the GameUserSettings.ini file with the settings from the server struct (settings that should be manually set)
func (s *Server) updateGus(gus *ini.IniFile) {
	{
		serverSettings := gus.GetOrCreateSection("ServerSettings")

		serverSettings.AddOrReplaceKey("RCONEnabled", true)
		serverSettings.AddOrReplaceKey("RCONPort", s.RCONPort)
		serverSettings.AddOrReplaceKey("ServerAdminPassword", s.AdminPassword)
		if s.ServerPassword != "" {
			serverSettings.AddOrReplaceKey("ServerPassword", s.ServerPassword)
		} else {
			serverSettings.RemoveMultipleKey("ServerPassword")
		}
		if s.SpectatorPassword != "" {
			serverSettings.AddOrReplaceKey("SpectatorPassword", s.SpectatorPassword)
		} else {
			serverSettings.RemoveMultipleKey("SpectatorPassword")
		}
		serverSettings.AddOrReplaceKey("ActiveMods", s.Mods)
	}

	{
		sessionSettings := gus.GetOrCreateSection("SessionSettings")

		sessionSettings.AddOrReplaceKey("MultiHome", true)
		sessionSettings.AddOrReplaceKey("Port", s.ServerPort)
		sessionSettings.AddOrReplaceKey("QueryPort", s.QueryPort)
		sessionSettings.AddOrReplaceKey("SessionName", s.ServerName)
	}

	{
		multiHome := gus.GetOrCreateSection("MultiHome")
		multiHome.AddOrReplaceKey("MultiHome", true)
	}

	{
		scriptEngineGameSession := gus.GetOrCreateSection("/Script/Engine.GameSession")

		scriptEngineGameSession.AddOrReplaceKey("MaxPlayers", s.MaxPlayers)
	}

}

func (s *Server) updateValueInGus(sectionName string, keyName string, value interface{}) error {
	gus, err := s.getGus()
	if err != nil {
		return err
	}

	gus.SafelyAddKeyToSection(sectionName, keyName, value)

	err = s.saveGus(gus)
	if err != nil {
		return err
	}

	return nil
}

func (s *Server) getGusAsMap() (map[string]map[string][]string, error) {
	gus, err := s.getGus()
	if err != nil {
		return nil, err
	}

	return ini.ToMap(gus), nil
}

func (s *Server) saveGusFromMap(gusMap map[string]map[string][]string) error {
	gus := ini.DeserializeFromMap(gusMap, duplicateKeys...)

	err := s.saveGus(gus)
	if err != nil {
		return err
	}

	return nil
}

func (s *Server) getValueFromGus(sectionName string, keyName string) (string, error) {
	gus, err := s.getGus()
	if err != nil {
		return "", err
	}

	key, err := gus.GetKeyFromSection(sectionName, keyName)
	if err != nil {
		return "", err
	}

	return key.ToValueString(), nil
}
