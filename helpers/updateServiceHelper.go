package helpers

import (
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"os"
	"slices"
	"strings"
	"syscall"

	"github.com/hashicorp/go-version"
	"github.com/inconshreveable/go-update"
	"github.com/sqweek/dialog"
	"golang.org/x/sys/windows"
)

type Asset struct {
	Name        string `json:"name"`
	DownloadUrl string `json:"browser_download_url"`
}

type Release struct {
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

func CheckForUpdates(WailsConfigFile []byte) {
	err := json.Unmarshal(WailsConfigFile, &cf)
	if err != nil {
		fmt.Printf("Error: Could not parse wails.json: %s\n", err)
		println("Skipping update check")
		return
	}

	if cf.Environment == "dev" {
		println("Environment is in dev mode")
		println("Skipping update check")
		return
	}

	println("Checking for updates...")

	var res *http.Response
	if cf.IsNightly {
		res, err = http.Get("https://api.github.com/repos/ItsMePepijn/ArkAscendedServerManager/releases/tags/nightly")
	} else {
		res, err = http.Get("https://api.github.com/repos/ItsMePepijn/ArkAscendedServerManager/releases/latest")
	}

	if err != nil {
		fmt.Printf("Error: Could not get latest release info: %s\n", err)
		println("Skipping update check")
		return
	}

	resBody, err := io.ReadAll(res.Body)
	if err != nil {
		fmt.Printf("server: could not read request body: %s\n", err)
		println("Skipping update check")
		return
	}

	err = json.Unmarshal(resBody, &release)
	if err != nil {
		fmt.Printf("Error: Could not parse release info: %s\n", err)
		println("Skipping update check")
		return
	}

	if cf.IsNightly {
		nameArr := strings.Split(release.Name, " ")
		release.TagName = nameArr[len(nameArr)-1]
	}

	installedVersion, err := version.NewVersion(cf.Version)
	if err != nil {
		fmt.Printf("Error: Could not parse installed version: %s\n", err)
		println("Skipping update check")
		return
	}
	latestVersion, err := version.NewVersion(release.TagName)
	if err != nil {
		fmt.Printf("Error: Could not parse latest version: %s\n", err)
		println("Skipping update check")
		return
	}

	if installedVersion.GreaterThanOrEqual(latestVersion) {
		fmt.Printf("Already on atest release: %s\n", release.Name)
		return
	}

	if len(release.Assets) == 0 {
		println("No release files exist for " + release.Name)
		return
	}

	fileName := cf.Info.ProductName + ".exe"
	asset := release.Assets[slices.IndexFunc(release.Assets, func(c Asset) bool { return c.Name == fileName })]
	if asset == (Asset{}) {
		println("Could not find download url for " + release.Name)
		return
	}

	fmt.Printf("Found newer release: %s\n", release.Name)
	installNewRelease := dialog.Message("%s", "Do you want to install it?\n"+release.Name).Title("New update available!").YesNo()

	if installNewRelease {
		DownladAndInstallUpdate(asset.DownloadUrl)
	}
}

func DownladAndInstallUpdate(source string) {
	if !IsAdmin() {
		RunMeElevated()
		return
	}

	println("Installing new release...")
	res, err := http.Get(source)

	if err != nil {
		fmt.Printf("Error: Could not download release: %s\n", err)
		return
	}

	defer res.Body.Close()
	err = update.Apply(res.Body, update.Options{})

	if err != nil {
		fmt.Printf("Error: Could not install release: %s\n", err)
		return
	}

	println("Successfully installed new release!")
	dialog.Message("%s", "Successfully installed new release!").Title("Update installed!").Info()
	os.Exit(0)
}

func RunMeElevated() {
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
		fmt.Println(err)
	}
	os.Exit(0)
}

func IsAdmin() bool {
	elevated := windows.GetCurrentProcessToken().IsElevated()
	fmt.Printf("admin %v\n", elevated)
	return elevated
}
