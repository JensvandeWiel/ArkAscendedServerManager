package helpers

import (
	"context"
	"github.com/wailsapp/wails/v2/pkg/runtime"
)

type HelpersController struct {
	ctx context.Context
}

func NewHelpersController() *HelpersController {
	return &HelpersController{}
}

func (c *HelpersController) Startup(ctx context.Context) {
	c.ctx = ctx
}

func (c *HelpersController) OpenDirectoryDialog() (string, error) {
	return runtime.OpenDirectoryDialog(c.ctx, runtime.OpenDialogOptions{})
}
