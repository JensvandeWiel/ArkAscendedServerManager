import { Button, IconButton, useColorScheme } from "@mui/joy";
import React from "react";
import { IconMoon, IconSun } from "@tabler/icons-react";

export const ThemeSwitcher = () => {
	const { mode, setMode } = useColorScheme();
	const [mounted, setMounted] = React.useState(false);

	React.useEffect(() => {
		setMounted(true);
	}, []);

	if (!mounted) {
		return null;
	}
	return (
		<IconButton
			variant="soft"
			color="neutral"
			onClick={() => setMode(mode === "dark" ? "light" : "dark")}
		>
			{mode === "dark" ? <IconSun /> : <IconMoon />}
		</IconButton>
	);
};
