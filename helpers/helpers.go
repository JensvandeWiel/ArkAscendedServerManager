package helpers

import (
	"context"
	"encoding/json"
	"fmt"

	"github.com/wailsapp/wails/v2/pkg/runtime"
)

type HelpersController struct {
	ctx      context.Context
	WailsCfg *Env
}

func NewHelpersController() *HelpersController {
	return &HelpersController{}
}

func (c *HelpersController) Startup(ctx context.Context, wailsCfgFile []byte) {
	c.ctx = ctx
	err := json.Unmarshal(wailsCfgFile, &c.WailsCfg)
	if err != nil {
		fmt.Printf("Error: Could not parse wails.json: %s\n", err)
		println("Skipping update check")
		return
	}
}

func (c *HelpersController) OpenDirectoryDialog() (string, error) {
	return runtime.OpenDirectoryDialog(c.ctx, runtime.OpenDialogOptions{})
}

func (c *HelpersController) OpenFileDialog() (string, error) {
	filters := []runtime.FileFilter{
		{DisplayName: "Ini Files (*.ini)", Pattern: "*.ini"},
	}
	return runtime.OpenFileDialog(c.ctx, runtime.OpenDialogOptions{Filters: filters})
}

func (c *HelpersController) GetVersion() string {
	return c.WailsCfg.Version
}
