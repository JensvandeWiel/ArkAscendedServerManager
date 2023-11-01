import { Button, Input, TabPanel } from "@mui/joy";
import React, { useEffect, useRef, useState } from "react";
import { server } from "../../../wailsjs/go/models";
import { SendRconCommand } from "../../../wailsjs/go/helpers/HelpersController";

type Message = {
    text: string;
    sender: string;
};

type Props = {
    setServ: React.Dispatch<React.SetStateAction<server.Server>>;
    serv: server.Server;
    serverStatus: boolean;
};

export function Console({ setServ, serv, serverStatus }: Props) {
    const [input, setInput] = useState("");
    const [messages, setMessages] = useState<Message[]>([]);

    const terminalRef = useRef<HTMLDivElement>(null);

    const writeToConsole = (text: string, sender: string = "user") => {
        const newMessage: Message = { text, sender };
        setMessages((prevMessages) => [...prevMessages, newMessage]);
    };

    const doRconCommand = (text: string) => {
        SendRconCommand(text, serv.ipAddress, serv.rconPort, serv.adminPassword)
            .then((resp) => writeToConsole(resp, "server"))
            .catch((err) => writeToConsole("error sending command: " + err, "server"));
    };

    useEffect(() => {
        if (terminalRef.current) {
            terminalRef.current.scrollTop = terminalRef.current.scrollHeight;
        }
    }, [messages]);

    if (serverStatus) {
        return (
            <TabPanel value={0}>
                <div
                    id={"terminal"}
                    ref={terminalRef}
                    className={"overflow-y-scroll font-jetbrains bg-black w-[100%] h-[50vh] p-4 rounded "}
                >
                    {messages.map((message, index) => (
                        <div key={index}>
                            {message.sender === "server" ? (
                                <span>{message.text}<br /></span>
                            ) : (
                                <span>
                                    <span style={{ color: "blue" }}>[{message.sender}]</span>{" "}
                                    <span style={{ color: "green" }}>$</span> {message.text}<br />
                                </span>
                            )}
                        </div>
                    ))}
                </div>
                <Input
                    className={"my-2 font-jetbrains"}
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    startDecorator={<span className={"text-green-400"}>$</span>}
                    endDecorator={
                        <Button
                            color={"neutral"}
                            onClick={(e) => {
                                writeToConsole(input);
                                setInput("");
                                doRconCommand(input);
                            }}
                            className={"m-1"}
                        >
                            Send
                        </Button>
                    }
                    onKeyPress={(e) => {
                        if (e.key === "Enter") {
                            writeToConsole(input, "user");
                            doRconCommand(input);
                            setInput("");
                        }
                    }}
                ></Input>
            </TabPanel>
        );
    } else {
        return (
            <TabPanel value={0}>
                <div className={"h-16 flex w-full"}>
                    <p className={"text-lg font-bold ml-8"}>Server is not running</p>
                </div>
            </TabPanel>
        );
    }
}
