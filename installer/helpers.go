package installer

import (
	_ "embed"
	"fmt"
	"os"
	"path"
)

//go:embed steamcmd.exe
var steamExe []byte

func (c *InstallerController) setupSteamCMD() error {

	exePath, err := os.Executable()
	if err != nil {
		return fmt.Errorf("failed to get executable directory: %v", err)
	}

	steamExePath := path.Join(path.Dir(exePath), "steamcmd", "steamcmd.exe")

	if _, err := os.Stat(path.Join(steamExePath)); os.IsNotExist(err) {
		//steamcmd is not installed

		err := os.MkdirAll(path.Join(path.Dir(steamExePath)), 0666)
		if err != nil {
			return fmt.Errorf("failed to create steamcmd directory: %v", err)
		}

		err = os.WriteFile(steamExePath, steamExe, 0666)
		if err != nil {
			return fmt.Errorf("failed to create steamcmd directory: %v", err)
		}

		c.config.GetConfig()
		c.config.Config.SteamCMDPath = path.Join(path.Dir(exePath), "steamcmd", "steamcmd.exe")
		c.config.SaveConfig()

	} else {
		//steamCMD is installed so set config to the steamCMD path
		c.config.GetConfig()
		c.config.Config.SteamCMDPath = path.Join(path.Dir(exePath), "steamcmd", "steamcmd.exe")
		c.config.SaveConfig()
	}

	return nil
}
