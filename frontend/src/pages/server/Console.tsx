import {Button, Input, TabPanel, Typography} from "@mui/joy";
import React, {useEffect, useRef, useState} from "react";
import {server} from "../../../wailsjs/go/models";
import {useAlert} from "../../components/AlertProvider";
import {IconArrowRight, IconChevronRight} from "@tabler/icons-react";

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
            terminalRef.current.innerHTML += "<span>  " + sender + "&nbsp;>&nbsp;"+ text + " <br/></span>"
        }
    }

    function handleInput() {

    }

    if (!serverStatus) {
        return (
            <TabPanel value={0}>
                <div id={"terminal"} ref={terminalRef} className={"overflow-y-scroll font-bold bg-black w-[100%] h-[50vh] p-4 rounded "}></div>
                <Input className={"my-2 font-jetbrains font-bold"} value={input} onChange={(e) => setInput(e.target.value)} startDecorator={<IconChevronRight/>} endDecorator={<Button color={"neutral"} onClick={(e) => {writeToConsole(input); setInput("")}} className={"m-1"} >Send</Button>} onKeyPress={(e) => {
                    if (e.key === 'Enter') {
                        writeToConsole(input)
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