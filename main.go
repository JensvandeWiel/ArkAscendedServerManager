package main

import (
	"context"
	"embed"
	"os"

	"github.com/JensvandeWiel/ArkAscendedServerManager/config"
	"github.com/JensvandeWiel/ArkAscendedServerManager/helpers"
	"github.com/JensvandeWiel/ArkAscendedServerManager/installer"
	"github.com/JensvandeWiel/ArkAscendedServerManager/logger"
	"github.com/JensvandeWiel/ArkAscendedServerManager/server"
	"github.com/wailsapp/wails/v2"
	wailsLogger "github.com/wailsapp/wails/v2/pkg/logger"
	"github.com/wailsapp/wails/v2/pkg/options"
	"github.com/wailsapp/wails/v2/pkg/options/assetserver"
)

//go:embed all:frontend/dist
var assets embed.FS

//go:embed wails.json
var WailsConfigFile []byte

//go:embed KEY
var KEY string

const (
	logFilePath = "main.log"
)

func main() {
	file, err := os.Create(logFilePath)
	if err != nil {
		println("Error creating log file")
	}

	defer file.Close()

	l := logger.New(file)

	helpers.CheckForUpdates(l, WailsConfigFile)

	// Create an instance of the app structure
	app := NewApp()
	h := helpers.NewHelpersController()
	c := config.NewConfigController()
	s := server.NewServerController(h)
	i := installer.NewInstallerController(c)

	// Create application with options
	err = wails.Run(&options.App{
		Title:  "Ark Ascended Server Manager",
		Width:  1024,
		Height: 768,
		AssetServer: &assetserver.Options{
			Assets: assets,
		},
		BackgroundColour: &options.RGBA{R: 255, G: 255, B: 255, A: 1},
		OnStartup: func(ctx context.Context) {
			app.startup(ctx)
			h.Startup(ctx, WailsConfigFile)
			c.Startup(ctx)
			s.Startup(ctx)
			i.Startup(ctx)
		},
		Logger:   l,
		LogLevel: wailsLogger.TRACE,
		Bind: []interface{}{
			app,
			h,
			c,
			s,
			i,
		},
	})

	if err != nil {
		l.Error("Error: " + err.Error())
	}
}
