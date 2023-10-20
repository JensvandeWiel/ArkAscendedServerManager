package server

// region Local Helpers
// findHighestKey returns the highest key in a map with int as key
func findHighestKey(m map[int]Server) int {
	var highestKey int = -1

	for key := range m {
		if key > highestKey {
			highestKey = key
		}
	}

	return highestKey
}

func generateNewDefaultServer(id int) Server {
	return Server{
		Id: id,
	}
}

//endregion

//region Global and Frontend Helpers

/*func (c *ServerController) GetNetworkInterfaces() []helpers.NetworkInterface {

	i, err := helpers.GetNetworkInterfaces()
	if err != nil {
		runtime.LogErrorf(c.ctx, "Error getting network interfaces: %s", err.Error())
		i = []helpers.NetworkInterface{}
	}

	return i
}
*/

//endregion
