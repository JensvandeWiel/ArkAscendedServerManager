import {TabPanel} from "@mui/joy";
import React from "react";
import {server} from "../../../wailsjs/go/models";

type Props = {
    setServ: React.Dispatch<React.SetStateAction<server.Server>>
    serv: server.Server;

}

export function Console({setServ, serv}:Props) {
    return (
        <TabPanel value={0} className={''}>
            sdssd
        </TabPanel>
    );
}