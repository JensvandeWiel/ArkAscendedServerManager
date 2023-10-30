package helpers

import (
	"encoding/json"
	"fmt"
	"io"
	"net/http"
)

type Release struct {
	Url     string `json:"url"`
	TagName string `json:"tag_name"`
	Assets  []struct {
		Name        string `json:"name"`
		DownloadUrl string `json:"browser_download_url"`
	} `json:"assets"`
}

var release Release

func CheckForUpdates() {
	res, err := http.Get("https://api.github.com/repos/JensvandeWiel/ArkAscendedServerManager/releases/latest")
	if err != nil {
		println("Error: Could not fetch update info")
		println(err.Error())
	} else {
		resBody, err := io.ReadAll(res.Body)
		if err != nil {
			fmt.Printf("server: could not read request body: %s\n", err)
		}

		json.Unmarshal(resBody, &release)
		fmt.Printf("Latest release: %s\n", release.TagName)
	}
}

func Update() {
}
