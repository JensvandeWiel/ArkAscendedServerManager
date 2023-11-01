import {TabPanel, Typography} from "@mui/joy";
import React, {useEffect, useState} from "react";
import {server} from "../../../wailsjs/go/models";
import {GetServerStatus} from "../../../wailsjs/go/server/ServerController";
import {useAlert} from "../../components/AlertProvider";
import {EventsOn} from "../../../wailsjs/runtime";

type Props = {
    setServ: React.Dispatch<React.SetStateAction<server.Server>>
    serv: server.Server;

}

export function Console({setServ, serv}:Props) {

    const [serverStarted, setServerStarted] = useState(false)
    const {addAlert} = useAlert()

    // useEffect for checking if the server is running
    useEffect(() => {
        GetServerStatus(serv.id).then((status) => {setServerStarted(status)}).catch((err) => {console.error(err); addAlert(err, "danger")})
    }, []);

    // Register server status events
    useEffect(() => {
        EventsOn("onServerStart", (id: number) => {
            if (id === serv.id) {
                setServerStarted(true)
            }
        })
        EventsOn("onServerExit", (id: number) => {
            if (id === serv.id) {
                setServerStarted(false)
            }
        })
    }, []);

    if (serverStarted) {
        return (
            <TabPanel value={0}>
                server started
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