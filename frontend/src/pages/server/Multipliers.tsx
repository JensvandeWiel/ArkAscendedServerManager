import React from "react";
import {server} from "../../../wailsjs/go/models";
import {
    Card,
    Divider, FormLabel, Input,
    TabPanel,
    Typography
} from "@mui/joy";

type Props = {
    setServ: React.Dispatch<React.SetStateAction<server.Server>>
    serv: server.Server;
}

function GeneralServerSettingsCard({ setServ, serv }: {setServ: React.Dispatch<React.SetStateAction<server.Server>>, serv: server.Server}) {
    return (
        <Card variant="soft"  className={''}>
            <Typography level="title-md">
                Multipliers
            </Typography>
            <Divider className={'mx-2'}/>

            <div className={'space-x-4 w-full flex'}>
                <div className={'inline-block'}>

                </div>
            </div>
        </Card>
    )
}

export function Multipliers({setServ, serv}: Props) {
    return (
        <TabPanel value={3} className={'space-y-8'}>
            <GeneralServerSettingsCard setServ={setServ} serv={serv}/>
        </TabPanel>
    );
}