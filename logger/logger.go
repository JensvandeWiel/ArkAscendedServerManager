package logger

import (
	"github.com/wailsapp/wails/v2/pkg/logger"
	"io"
	"log"
	"os"
)

type LoggerWithFileOut struct {
	Log *log.Logger
}

// New creates a new Logger, that outputs to a file and to stdout.
func New(file *os.File) logger.Logger {
	l := &LoggerWithFileOut{
		Log: log.Default(),
	}

	multiwriter := io.MultiWriter(file, os.Stdout)

	l.Log.SetOutput(multiwriter)

	return l
}

// Print works like Sprintf.
func (l *LoggerWithFileOut) Print(message string) {
	l.Log.Println(message)
}

// Trace level logging. Works like Sprintf.
func (l *LoggerWithFileOut) Trace(message string) {
	l.Log.Println("TRA | " + message)
}

// Debug level logging. Works like Sprintf.
func (l *LoggerWithFileOut) Debug(message string) {
	l.Log.Println("DEB | " + message)
}

// Info level logging. Works like Sprintf.
func (l *LoggerWithFileOut) Info(message string) {
	l.Log.Println("INF | " + message)
}

// Warning level logging. Works like Sprintf.
func (l *LoggerWithFileOut) Warning(message string) {
	l.Log.Println("WAR | " + message)
}

// Error level logging. Works like Sprintf.
func (l *LoggerWithFileOut) Error(message string) {
	l.Log.Println("ERR | " + message)
}

// Fatal level logging. Works like Sprintf.
func (l *LoggerWithFileOut) Fatal(message string) {
	l.Log.Println("FAT | " + message)
	os.Exit(1)
}
