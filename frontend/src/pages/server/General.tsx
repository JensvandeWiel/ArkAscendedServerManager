import {
    Card,
    Divider,
    FormLabel,
    Input,
    Select,
    TabPanel,
    Typography,
    Option,
} from "@mui/joy";

import {server} from "../../../wailsjs/go/models";
import React, {useEffect, useState} from "react";
import {GetNetworkInterfacesIp} from "../../../wailsjs/go/server/ServerController";
import {PasswordInput} from "../../components/PasswordInput";

type Props = {
    setServ: React.Dispatch<React.SetStateAction<server.Server>>
    serv: server.Server;

}


export function General({serv, setServ}: Props) {
    const defInter: {[p: string]: string} = {
        "none": ""
    }


    const [interfaces, setInterfaces] = useState<{[p: string]: string}>(defInter)

    const interfaceEntries = Object.entries(interfaces);


    const interfaceElements = interfaceEntries.map(([key, value], index) => (
        <Option key={index} value={value.toString()}>{value} - "{key}" </Option>
    ));

    useEffect(() => {
        GetNetworkInterfacesIp().then((val) => setInterfaces(val)).catch((reason) => console.error(reason))

    }, []);

    return (
        <TabPanel className={'space-y-8'}>
            {/* Server Name and Passwords */}
            <Card variant="soft"  className={''}>
                <Typography level="title-md">0.
                    Server Name and Passwords
                </Typography>
                <Divider className={'mx-2'}/>

{/*
                <Portal container={document.getElementById("alert-root")}><Alert className={"fixed"}>dsadasd</Alert></Portal>
*/}

                <FormLabel>Server Name:</FormLabel>
                <Input className={'w-2/3'} required value={serv?.serverName} onChange={(e) => setServ((p) => ({ ...p, serverName: e.target.value }))}  ></Input>
                <div className={'space-x-4 w-full flex'}>
                    <div className={'inline-block'}>
                        <FormLabel>Server Password:</FormLabel>
                        <PasswordInput value={serv?.serverPassword} onChange={(e) => setServ((p) => ({ ...p, serverPassword: e.target.value }))} ></PasswordInput>
                    </div>
                    <div className={'inline-block'}>
                        <FormLabel>Admin Password:</FormLabel>
                        <PasswordInput value={serv?.adminPassword} onChange={(e) => setServ((p) => ({ ...p, adminPassword: e.target.value }))} ></PasswordInput>
                    </div>
                    <div className={'inline-block'}>
                        <FormLabel>Spectator Password:</FormLabel>
                        <PasswordInput value={serv?.spectatorPassword} onChange={(e) => setServ((p) => ({ ...p, spectatorPassword: e.target.value }))} ></PasswordInput>
                    </div>
                </div>
            </Card>

            {/* Server Name and Passwords */}
            <Card variant="soft"  className={''}>
                <Typography level="title-md">
                    Networking
                </Typography>
                <Divider className={'mx-2'}/>
                <FormLabel>IP Address:</FormLabel>
                <Select
                        value={serv.ipAddress?? '0.0.0.0'}
                        onChange={(e, value) => {
                            const newValue = value;
                            setServ(
                                prevState =>{
                                    return{
                                        ...prevState,
                                        ipAddress: newValue?? "0.0.0.0"
                                    }
                                }
                            )
                        }}
                >
                    <Option value={"0.0.0.0"}>0.0.0.0 - All</Option>
                    {interfaceElements}
                </Select>
                <FormLabel>Ports: </FormLabel>
                <div className={'space-x-4 w-11/12'}>
                    <div className={'w-32 inline-block'}>
                        <FormLabel>Server Port:</FormLabel>
                        <Input className={''} type={'number'} required value={serv?.serverPort} onChange={(e) => (parseInt(e.target.value) >= 1 && parseInt(e.target.value) <= 65535) ? setServ((p) => ({ ...p, serverPort: parseInt(e.target.value), peerPort: parseInt(e.target.value) + 1 })) : null} ></Input>
                    </div>
                    <div className={'w-32 inline-block'}>
                        <FormLabel>Peer Port:</FormLabel>
                        <Input className={''} disabled type={'number'} value={serv.peerPort} ></Input>
                    </div>
                    <div className={'w-32 inline-block'}>
                        <FormLabel>Query Port:</FormLabel>
                        <Input className={''} type={'number'} value={serv?.queryPort} onChange={(e) => (parseInt(e.target.value) >= 1 && parseInt(e.target.value) <= 65535) ? setServ((p) => ({ ...p, queryPort: parseInt(e.target.value)})) : null}></Input>
                    </div>
                    <div className={'w-32 inline-block'}>
                        <FormLabel>RCON Port:</FormLabel>
                        <Input className={''} value={serv?.rconPort} onChange={(e) => (parseInt(e.target.value) >= 1 && parseInt(e.target.value) <= 65535) ? setServ((p) => ({ ...p, rconPort: parseInt(e.target.value)})) : null} type={'number'}></Input >
                    </div>
                </div>
            </Card>
        </TabPanel>
    );
}