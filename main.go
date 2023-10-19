package main

import (
	"ark-ascended-server-manager/config"
	"context"
	"embed"
	"github.com/wailsapp/wails/v2"
	"github.com/wailsapp/wails/v2/pkg/options"
	"github.com/wailsapp/wails/v2/pkg/options/assetserver"
)

//go:embed all:frontend/dist
var assets embed.FS

func main() {
	// Create an instance of the app structure
	app := NewApp()
	c := config.NewConfigController()

	// Create application with options
	err := wails.Run(&options.App{
		Title:  "Ark Ascended Server Manager",
		Width:  1024,
		Height: 768,
		AssetServer: &assetserver.Options{
			Assets: assets,
		},
		BackgroundColour: &options.RGBA{R: 255, G: 255, B: 255, A: 1},
		OnStartup: func(ctx context.Context) {
			app.startup(ctx)
			c.Startup(ctx)
		},
		Bind: []interface{}{
			app,
			c,
		},
	})

	if err != nil {
		println("Error:", err.Error())
	}
}
