package helpers

import (
	"encoding/json"
	"io"
	"net/http"
	"os"
	"slices"
	"strconv"
	"strings"
	"syscall"

	"github.com/hashicorp/go-version"
	"github.com/inconshreveable/go-update"
	"github.com/sqweek/dialog"
	wailsLogger "github.com/wailsapp/wails/v2/pkg/logger"
	"golang.org/x/sys/windows"
)

type Asset struct {
	Name        string `json:"name"`
	DownloadUrl string `json:"browser_download_url"`
}

type Release struct {
	ID      int64   `json:"id"`
	Url     string  `json:"url"`
	TagName string  `json:"tag_name"`
	Name    string  `json:"name"`
	Assets  []Asset `json:"assets"`
}

type Env struct {
	Version     string `json:"version"`
	Environment string `json:"environment"`
	IsNightly   bool   `json:"isNightly"`
	Info        struct {
		ProductName string `json:"productName"`
	} `json:"info"`
}

var cf Env
var release Release

func CheckForUpdates(logger wailsLogger.Logger, WailsConfigFile []byte) {
	err := json.Unmarshal(WailsConfigFile, &cf)
	if err != nil {
		logger.Error("Error: Could not parse wails.json " + err.Error())
		logger.Error("Skipping update check")
		return
	}

	if cf.Environment == "dev" {
		logger.Info("Environment is in dev mode")
		logger.Info("Skipping update check")
		return
	}

	logger.Info("Checking for updates...")

	var res *http.Response
	if cf.IsNightly {
		res, err = http.Get("https://api.github.com/repos/JensvandeWiel/ArkAscendedServerManager/releases/tags/nightly")
	} else {
		res, err = http.Get("https://api.github.com/repos/JensvandeWiel/ArkAscendedServerManager/releases/latest")
	}

	if err != nil {
		logger.Error("Error: Could not get latest release info:" + err.Error())
		logger.Error("Skipping update check")
		return
	}

	resBody, err := io.ReadAll(res.Body)
	if err != nil {
		logger.Error("server: Could not read request body:" + err.Error())
		logger.Error("Skipping update check")
		return
	}

	err = json.Unmarshal(resBody, &release)
	if err != nil {
		logger.Error("Error: Could not parse release info:" + err.Error())
		logger.Error("Skipping update check")
		return
	}

	if cf.IsNightly {
		nameArr := strings.Split(release.Name, " ")
		release.TagName = nameArr[len(nameArr)-1]
	}

	installedVersion, err := version.NewVersion(cf.Version)
	if err != nil {
		logger.Error("Error: Could not parse installed version:" + err.Error())
		logger.Error("Skipping update check")
		return
	}
	latestVersion, err := version.NewVersion(release.TagName)
	if err != nil {
		logger.Error("Error: Could not parse latest version:" + err.Error())
		logger.Error("Skipping update check")
		return
	}

	if installedVersion.GreaterThanOrEqual(latestVersion) {
		logger.Info("Already on a test release: " + release.Name)
		return
	}

	if len(release.Assets) == 0 {
		logger.Info("No release files exist for " + release.Name)
		return
	}

	fileName := cf.Info.ProductName + ".exe"
	asset := release.Assets[slices.IndexFunc(release.Assets, func(c Asset) bool { return c.Name == fileName })]
	if asset == (Asset{}) {
		logger.Error("Could not find download url for " + release.Name)
		return
	}

	logger.Info("Found newer release: " + release.Name)
	installNewRelease := dialog.Message("%s", "Do you want to install it?\n"+release.Name).Title("New update available!").YesNo()

	if installNewRelease {
		DownladAndInstallUpdate(logger, asset.DownloadUrl)
	}
}

func DownladAndInstallUpdate(logger wailsLogger.Logger, source string) {
	if !IsAdmin(logger) {
		RunMeElevated(logger)
		return
	}

	logger.Info("Installing new release...")
	res, err := http.Get(source)

	if err != nil {
		logger.Error("Error: Could not download release: " + err.Error())
		return
	}

	defer res.Body.Close()
	err = update.Apply(res.Body, update.Options{})

	if err != nil {
		logger.Error("Error: Could not install release: " + err.Error())
		return
	}

	logger.Info("Successfully installed new release!")
	dialog.Message("%s", "Successfully installed new release!").Title("Update installed!").Info()
	os.Exit(0)
}

func RunMeElevated(logger wailsLogger.Logger) {
	verb := "runas"
	exe, _ := os.Executable()
	cwd, _ := os.Getwd()
	args := strings.Join(os.Args[1:], " ")

	verbPtr, _ := syscall.UTF16PtrFromString(verb)
	exePtr, _ := syscall.UTF16PtrFromString(exe)
	cwdPtr, _ := syscall.UTF16PtrFromString(cwd)
	argPtr, _ := syscall.UTF16PtrFromString(args)

	var showCmd int32 = 1 //SW_NORMAL

	err := windows.ShellExecute(0, verbPtr, exePtr, argPtr, cwdPtr, showCmd)
	if err != nil {
		logger.Error(err.Error())
	}
	os.Exit(0)
}

func IsAdmin(logger wailsLogger.Logger) bool {
	elevated := windows.GetCurrentProcessToken().IsElevated()
	logger.Info("admin " + strconv.FormatBool(elevated))
	return elevated
}
