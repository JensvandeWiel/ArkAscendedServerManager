package main

import (
	"context"
	"embed"
	"github.com/JensvandeWiel/ArkAscendedServerManager/config"
	"github.com/JensvandeWiel/ArkAscendedServerManager/logger"
	"github.com/JensvandeWiel/ArkAscendedServerManager/server"
	"github.com/wailsapp/wails/v2"
	wailsLogger "github.com/wailsapp/wails/v2/pkg/logger"
	"github.com/wailsapp/wails/v2/pkg/options"
	"github.com/wailsapp/wails/v2/pkg/options/assetserver"
	"os"
)

//go:embed all:frontend/dist
var assets embed.FS

//go:embed .KEY
var Key string

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

	// Create an instance of the app structure
	app := NewApp()
	c := config.NewConfigController()
	s := server.NewServerController()

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
			c.Startup(ctx)
			s.Startup(ctx)
		},
		Logger:   l,
		LogLevel: wailsLogger.TRACE,
		Bind: []interface{}{
			app,
			c,
			s,
		},
	})

	if err != nil {
		println("Error:", err.Error())
	}
}
