package installer

import (
	"context"
	"fmt"
	"github.com/jensvandewiel/gosteamcmd"
	"github.com/jensvandewiel/gosteamcmd/console"
	"github.com/wailsapp/wails/v2/pkg/runtime"
	"io"
	"strconv"
)

type InstallerController struct {
	ctx context.Context
}

func NewInstallerController() *InstallerController {
	return &InstallerController{}
}

func (c *InstallerController) Startup(ctx context.Context) {
	c.ctx = ctx
}

/*
Events:
- "installingUpdateAction" sends updates about the installers args: "action"
- "installingUpdateProgress" sends updates about the installers args: "progress"
- "appInstalled" sends when app ist installed with the id as arg

*/

// Install installs the server and returns true is successful and error and false if failed
func (c *InstallerController) Install(installPath string) (error, bool) {

	prompts := []*gosteamcmd.Prompt{
		gosteamcmd.ForceInstallDir(installPath),
		gosteamcmd.Login("", "", ""),
		gosteamcmd.AppUpdate(2430930, "", true),
	}

	cmd := gosteamcmd.New(io.Discard, prompts, "")

	cmd.Console.Parser.OnInformationReceived = func(action console.Action, progress float64, currentWritten, total uint64) {
		actionString := ""

		switch action {
		case console.Downloading:
			actionString = "downloading"
		case console.Verifying:
			actionString = "verifying"
		case console.Preallocating:
			actionString = "preallocating"
		default:
			actionString = "unknown"
		}

		runtime.EventsEmit(c.ctx, "installingUpdateAction", actionString)
		runtime.EventsEmit(c.ctx, "installingUpdateProgress", progress)

		runtime.LogDebug(c.ctx, "Installer info received: action: "+actionString+" progress: "+fmt.Sprintf("%f", progress))
	}
	cmd.Console.Parser.OnAppInstalled = func(app uint32) {
		runtime.LogDebug(c.ctx, "Installer info received: App installed: "+strconv.Itoa(int(app)))

		runtime.EventsEmit(c.ctx, "appInstalled", app)
	}
	i, err := cmd.Run()

	if err != nil {
		return fmt.Errorf("failed to install: " + err.Error()), false
	}

	if i != 0 {
		return fmt.Errorf("failed to install: returned non 0 retorn code: " + strconv.Itoa(int(i))), false
	}
	return nil, true
}
