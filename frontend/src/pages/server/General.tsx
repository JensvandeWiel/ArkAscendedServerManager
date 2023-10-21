import {Card, Divider, FormLabel, Input, TabPanel, Typography} from "@mui/joy";

import {server} from "../../../wailsjs/go/models";
import React from "react";

type Props = {
    setServ: React.Dispatch<React.SetStateAction<server.Server>>
    serv: server.Server;
}


export function General({serv, setServ}: Props) {
    return (
        <TabPanel >
            <Card variant="soft"  className={''}>
                <Typography level="title-md">
                    Server Name and Passwords
                </Typography>
                <Divider className={'mx-2'}/>
                <FormLabel>Server Name:</FormLabel>
                <Input className={'w-2/3'} value={serv?.serverName} onChange={(e) => setServ((p) => ({ ...p, serverName: e.target.value }))} endDecorator={serv?.serverName.length} ></Input>
                <div className={'space-x-4 w-full flex'}>
                    <div className={'inline-block'}>
                        <FormLabel>Server Password:</FormLabel>
                        <Input className={''} type={'password'} value={serv?.serverPassword} onChange={(e) => setServ((p) => ({ ...p, serverPassword: e.target.value }))} ></Input>
                    </div>
                    <div className={'inline-block'}>
                        <FormLabel>Admin Password:</FormLabel>
                        <Input className={''} type={'password'} value={serv?.adminPassword} onChange={(e) => setServ((p) => ({ ...p, adminPassword: e.target.value }))} ></Input>
                    </div>
                    <div className={'inline-block'}>
                        <FormLabel>Spectator Password:</FormLabel>
                        <Input className={''} type={'password'} value={serv?.spectatorPassword} onChange={(e) => setServ((p) => ({ ...p, spectatorPassword: e.target.value }))} ></Input>
                    </div>
                </div>
            </Card>
        </TabPanel>
    );
}