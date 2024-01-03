import { IconButton, Input } from "@mui/joy";
import React, { useState } from "react";
import { IconEye, IconEyeClosed } from "@tabler/icons-react";

type Props = {
	onChange: React.ChangeEventHandler<HTMLInputElement> | undefined;
	value: string | ReadonlyArray<string> | number | undefined;
	children?: any;
	className?: string | undefined;
	disabled?: boolean;
};

export function PasswordInput({
	onChange,
	value,
	children,
	className,
	disabled,
}: Props) {
	const [textVisible, setTextVisible] = useState(false);
	return (
		<Input
			type={textVisible ? "text" : "password"}
			endDecorator={
				<IconButton onClick={() => setTextVisible(!textVisible)}>
					{textVisible ? <IconEye /> : <IconEyeClosed />}
				</IconButton>
			}
			value={value}
			onChange={onChange}
			className={className}
			disabled={disabled}
		>
			{children}
		</Input>
	);
}
