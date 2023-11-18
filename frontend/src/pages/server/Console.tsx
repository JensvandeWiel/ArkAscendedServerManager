import {Button, IconButton, Input, TabPanel, Tooltip} from "@mui/joy";
import React, { useEffect, useRef, useState } from "react";
import { server } from "../../../wailsjs/go/models";
import { SendRconCommand } from "../../../wailsjs/go/helpers/HelpersController";
import {IconEraser, IconSend} from "@tabler/icons-react";

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
    
    const writeAndDoRconCommand = (text: string) => {
        writeToConsole(text);
        doRconCommand(text);
        setInput("");
    }

    useEffect(() => {
        if (terminalRef.current) {
            terminalRef.current.scrollTop = terminalRef.current.scrollHeight;
        }
    }, [messages]);

    useEffect(() => {
        if (!serverStatus) {
            setMessages([])
        }
    }, [serverStatus]);

    useEffect(() => {
        setMessages([])
    }, [serv.id]);

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
                        <span>
                            <Tooltip title={"send message"}>
                                <IconButton
                                    color={"neutral"}
                                    onClick={(e) => {
                                        writeAndDoRconCommand(input);
                                    }}
                                    className={"m-1"}
                                >
                            <IconSend/>
                                </IconButton>
                            </Tooltip>
                            <Tooltip title={"clear console"}>
                                <IconButton
                                    color={"neutral"}
                                    onClick={(e) => {
                                        setMessages([])
                                    }}
                                    className={"m-1"}>
                                    <IconEraser/>
                                </IconButton>
                            </Tooltip>
                        </span>
                    }
                    onKeyPress={(e) => {
                        if (e.key === "Enter") {
                            writeAndDoRconCommand(input);
                        }
                    }}
                ></Input>
                <div className={'space-x-4 w-full flex'}>
                    <div className={'inline-block'}>
                        <Button color={'neutral'} variant="solid" disabled={!serverStatus} onClick={()=>writeAndDoRconCommand("destroywilddinos")}>Dino Wipe</Button>
                    </div>
                    <div className={'inline-block'}>
                        <Button color={'neutral'} variant="solid" disabled={!serverStatus} onClick={()=>writeAndDoRconCommand("saveworld")}>Save World</Button>
                    </div>
                </div>
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
