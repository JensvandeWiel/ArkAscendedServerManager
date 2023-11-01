import {Button, Input, TabPanel, Typography} from "@mui/joy";
import React, {useEffect, useRef, useState} from "react";
import {server} from "../../../wailsjs/go/models";
import {useAlert} from "../../components/AlertProvider";
import {IconArrowRight, IconChevronRight, IconCurrencyDollar} from "@tabler/icons-react";
import {SendRconCommand} from "../../../wailsjs/go/helpers/HelpersController";

type Props = {
    setServ: React.Dispatch<React.SetStateAction<server.Server>>
    serv: server.Server;
    serverStatus: boolean;

}

export function Console({setServ, serv, serverStatus}:Props) {

    const { addAlert  } = useAlert()

    const [input, setInput] = useState("")
    const terminalRef = useRef<HTMLDivElement>(null);

    function writeToConsole(text: string, sender: string = "") {
        if (terminalRef.current) {
            terminalRef.current.scrollTop = terminalRef.current.scrollHeight;
            terminalRef.current.innerHTML += "<span><span class='text-blue-500'>[" + sender + "]<span class='text-green-400'>$</span></span>&nbsp;"+ text + " <br/></span>"
        }
    }

    function writeToConsoleAsServer(text: string, sender: string = "server") {
        if (terminalRef.current) {
            terminalRef.current.scrollTop = terminalRef.current.scrollHeight;
            terminalRef.current.innerHTML += "<span>"+ text + " <br/></span>"
        }
    }

    function doRconCommand(text: string) {
        SendRconCommand(text, serv.ipAddress, serv.rconPort, serv.adminPassword).then((resp) => writeToConsoleAsServer(resp, "server")).catch((err) => writeToConsole("error sending command: " + err, "server"))
    }

    if (serverStatus) {
        return (
            <TabPanel value={0}>
                <div id={"terminal"} ref={terminalRef} className={"overflow-y-scroll font-jetbrains bg-black w-[100%] h-[50vh] p-4 rounded "}></div>
                <Input className={"my-2 font-jetbrains"} value={input} onChange={(e) => setInput(e.target.value)} startDecorator={<span className={"text-green-400"}>$</span>} endDecorator={<Button color={"neutral"} onClick={(e) => {writeToConsole(input); setInput(""); doRconCommand(input)}} className={"m-1"} >Send</Button>} onKeyPress={(e) => {
                    if (e.key === 'Enter') {
                        writeToConsole(input, "user")
                        doRconCommand(input)
                        setInput("")
                    }
                }}></Input>
            </TabPanel>
        );
    } else {
        return (
            <TabPanel value={0}>
                <div className={'h-16 flex w-full'}>
                    <p className={'text-lg font-bold ml-8'}>Server is not running</p>
                </div>

            </TabPanel>
        );
    }

}