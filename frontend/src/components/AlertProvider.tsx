import React, { createContext, useContext, useState, ReactNode } from "react";
import { Alert } from "@mui/joy";
import { IconX } from "@tabler/icons-react";

type AlertContextType = {
	addAlert: (
		message: string,
		severity:
			| "success"
			| "warning"
			| "primary"
			| "neutral"
			| "danger"
			| undefined,
	) => void;
	alerts: Alert[];
};

const AlertContext = createContext<AlertContextType | undefined>(undefined);

type AlertProviderProps = {
	children: ReactNode;
};

type Alert = {
	message: string;
	severity:
		| "success"
		| "warning"
		| "primary"
		| "neutral"
		| "danger"
		| undefined;
};

export function AlertProvider({ children }: AlertProviderProps) {
	const [alerts, setAlerts] = useState<Alert[]>([]);

	const addAlert = (
		message: string,
		severity:
			| "success"
			| "warning"
			| "primary"
			| "neutral"
			| "danger"
			| undefined,
	) => {
		const newAlert = { message, severity };
		setAlerts([...alerts, newAlert]);

		console.log(alerts);
	};

	const removeAlert = (index: number) => {
		const updatedAlerts = [...alerts];
		updatedAlerts.splice(index, 1);
		setAlerts(updatedAlerts);
	};

	return (
		<AlertContext.Provider value={{ addAlert, alerts }}>
			{children}

			<div
				className={"m-4 absolute bottom-0 w-[80vw] left-1/2 -ml-[40vw]"}
			>
				{alerts.map((alert, index) => (
					<Alert
						key={index}
						className={"m-4 "}
						color={alert.severity}
						endDecorator={
							<IconX onClick={() => removeAlert(index)} />
						}
					>
						{alert.message}
					</Alert>
				))}
			</div>
		</AlertContext.Provider>
	);
}

export function useAlert() {
	const context = useContext(AlertContext);
	if (!context) {
		throw new Error("useAlert must be used within an AlertProvider");
	}
	return context;
}
