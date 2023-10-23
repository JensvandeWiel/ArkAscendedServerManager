package helpers

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"

	"github.com/wailsapp/wails/v2/pkg/runtime"
)

// SendToDiscord sends the given message to the webhook url
func SendToDiscord(message string, webhookUrl string) bool {

	body := map[string]string{
		"content": message,
	}

	jsonBody, err := json.Marshal(body)

	if err != nil {
		runtime.LogError(context.Background(), "Error marshalling body json: "+err.Error())
		return false
	}

	response, err := http.Post(webhookUrl, "application/json", bytes.NewBuffer(jsonBody))

	if err != nil {
		runtime.LogError(context.Background(), "Error sending message to discord: "+err.Error())
		return false
	}

	if response.StatusCode != 204 {
		runtime.LogError(context.Background(), "Error sending message to discord: "+response.Status)
		return false
	}

	return true
}
