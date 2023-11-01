package config

import (
	"context"
	"encoding/json"
	"github.com/adrg/xdg"
	"github.com/wailsapp/wails/v2/pkg/runtime"
	"os"
	"path"
	"path/filepath"
)

// region Config struct

type Config struct {
}

// endregion

// ConfigController struct handles the configuration and storing it, the config contains everything from app preferences to servers
type ConfigController struct {
	ctx           context.Context
	configFileDir string
	Config        Config
}

// NewConfigController creates a new ConfigController application struct
func NewConfigController() *ConfigController {

	c := &ConfigController{}

	// Set config dir
	configDir, err := xdg.ConfigFile(path.Join("ArkAscendedServerManager", "config.json"))
	if err != nil {
		runtime.LogErrorf(c.ctx, "Error getting config dir: %s", err.Error())
	}
	c.configFileDir = configDir

	return c
}

// Startup handles the initial loading of the config
func (c *ConfigController) Startup(ctx context.Context) {
	c.ctx = ctx

}

func (c *ConfigController) GetConfig() bool {

	// Check if config dir exists
	if _, err := os.Stat(filepath.Dir(c.configFileDir)); os.IsNotExist(err) {
		runtime.LogDebug(c.ctx, "Config file does not exist, creating a new one")
		if err := os.MkdirAll(filepath.Dir(c.configFileDir), os.ModePerm); err != nil {
			runtime.LogError(c.ctx, "Error creating config dir")
			return false
		}
	}

	// Check if the config file exists
	_, err := os.Stat(path.Join(c.configFileDir, "config.json"))
	if err != nil {
		if os.IsNotExist(err) {
			runtime.LogDebug(c.ctx, "Config file does not exist, returning new configuration")
			//TODO set new config
			c.Config = Config{}
			return true
		}
		runtime.LogError(c.ctx, "Error reading config file: "+err.Error())
		return false
	}

	//It exists so read the file
	cf, err := os.ReadFile(path.Join(c.configFileDir, "config.json"))
	if err != nil {
		runtime.LogError(c.ctx, "Error reading config file: "+err.Error())
		return false
	}

	conf := Config{}

	err = json.Unmarshal(cf, &conf)
	if err != nil {
		runtime.LogError(c.ctx, "Error unmarshalling config file: "+err.Error())
		return false
	}

	c.Config = conf
	return true
}

func (c *ConfigController) SaveConfig() bool {
	configFile, err := json.MarshalIndent(c.Config, "", " ")
	if err != nil {
		runtime.LogError(c.ctx, "Error marshalling config file: "+err.Error())
		return false
	}

	// Check if config dir exists
	if _, err := os.Stat(filepath.Dir(c.configFileDir)); os.IsNotExist(err) {
		runtime.LogDebug(c.ctx, "Config file does not exist, creating it")
		if err := os.MkdirAll(filepath.Dir(c.configFileDir), os.ModePerm); err != nil {
			runtime.LogError(c.ctx, "Error creating config dir")
			return false
		}
	}

	err = os.WriteFile(c.configFileDir, configFile, 0644)

	if err != nil {
		runtime.LogError(c.ctx, "Error writing config file: "+err.Error())
		return false
	}
	return true
}
