package helpers

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
)

// SendToDiscord sends the given message to the webhook url
func SendToDiscord(message string, webhookUrl string) error {

	body := map[string]string{
		"content": message,
	}

	jsonBody, err := json.Marshal(body)

	if err != nil {
		return fmt.Errorf("error marshalling body json: %s", err.Error())
	}

	response, err := http.Post(webhookUrl, "application/json", bytes.NewBuffer(jsonBody))

	if err != nil {
		return fmt.Errorf("error sending message to discord: %s", err.Error())
	}

	if response.StatusCode != 204 {
		return fmt.Errorf("error sending message to discord: %s", response.Status)
	}

	return nil
}
