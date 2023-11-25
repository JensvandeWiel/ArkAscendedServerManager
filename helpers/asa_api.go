package helpers

import (
	"archive/zip"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"os"
	"path/filepath"
	"strings"
)

// Download the latest release of AsaApi from GitHub and extract it to the specified path
func PlantLatestAsaAPIRelease(extractionPath string) error {

	owner := "ServersHub"
	repo := "ServerAPI"

	// Get the latest release information
	release, err := getLatestRelease(owner, repo)
	if err != nil {
		return fmt.Errorf("Error getting latest release: %v", err)
	}

	// Get the tag name of the latest release
	tagName := release.TagName

	// Construct the zip file name with AsaApi
	zipFileName := fmt.Sprintf("AsaApi_%s.zip", strings.TrimPrefix(tagName, "v"))

	// Create a file to store the downloaded zip file
	zipFilePath := filepath.Join(extractionPath, zipFileName)
	zipFile, err := os.Create(zipFilePath)
	if err != nil {
		return fmt.Errorf("Error creating zip file: %v", err)
	}
	defer zipFile.Close()

	// Download the first release asset
	if len(release.Assets) > 0 {
		assetURL := release.Assets[0].DownloadUrl
		if err := downloadFile(zipFile, assetURL); err != nil {
			return fmt.Errorf("Error downloading zip file: %v", err)
		}
	} else {
		return fmt.Errorf("No release assets found.")
	}

	// Extract the downloaded zip file
	if err := extractZipFile(zipFilePath, extractionPath); err != nil {
		return fmt.Errorf("Error extracting zip file: %v", err)
	}

	fmt.Printf("Release extracted to: %s\n", extractionPath)

	return nil
}

func extractZipFile(zipFilePath, extractionPath string) error {
	// Add code to extract the zip file to the specified path
	// You can use a library like "archive/zip" or "github.com/mholt/archiver" for this purpose
	// For example, using "archive/zip":
	reader, err := zip.OpenReader(zipFilePath)
	if err != nil {
		return err
	}
	defer reader.Close()

	for _, file := range reader.File {
		path := filepath.Join(extractionPath, file.Name)

		if file.FileInfo().IsDir() {
			err := os.MkdirAll(path, os.ModePerm)
			if err != nil {
				return err
			}
		} else {
			fileReader, err := file.Open()
			if err != nil {
				return err
			}
			defer fileReader.Close()

			targetFile, err := os.Create(path)
			if err != nil {
				return err
			}
			defer targetFile.Close()

			if _, err := io.Copy(targetFile, fileReader); err != nil {
				return err
			}
		}
	}

	return nil
}

func getLatestRelease(owner, repo string) (*Release, error) {
	url := fmt.Sprintf("https://api.github.com/repos/%s/%s/releases/latest", owner, repo)

	resp, err := http.Get(url)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("failed to fetch latest release: %s", resp.Status)
	}

	var release Release
	if err := json.NewDecoder(resp.Body).Decode(&release); err != nil {
		return nil, err
	}

	// Fetch the release assets
	assets, err := getReleaseAssets(owner, repo, release.ID)
	if err != nil {
		return nil, err
	}

	release.Assets = assets

	return &release, nil
}

func getReleaseAssets(owner, repo string, releaseID int64) ([]Asset, error) {
	url := fmt.Sprintf("https://api.github.com/repos/%s/%s/releases/%d/assets", owner, repo, releaseID)

	resp, err := http.Get(url)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("failed to fetch release assets: %s", resp.Status)
	}

	var assets []Asset
	if err := json.NewDecoder(resp.Body).Decode(&assets); err != nil {
		return nil, err
	}

	return assets, nil
}

func downloadFile(file *os.File, url string) error {
	resp, err := http.Get(url)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("failed to download file: %s", resp.Status)
	}

	_, err = io.Copy(file, resp.Body)
	return err
}
