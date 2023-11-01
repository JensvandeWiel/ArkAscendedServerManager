import {TabPanel, Typography} from "@mui/joy";
import React, {useEffect, useState} from "react";
import {server} from "../../../wailsjs/go/models";
import {GetServerStatus} from "../../../wailsjs/go/server/ServerController";
import {useAlert} from "../../components/AlertProvider";
import {EventsOn} from "../../../wailsjs/runtime";

type Props = {
    setServ: React.Dispatch<React.SetStateAction<server.Server>>
    serv: server.Server;
    serverStatus: boolean;

}

export function Console({setServ, serv, serverStatus}:Props) {

    const {addAlert} = useAlert()


    if (serverStatus) {
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