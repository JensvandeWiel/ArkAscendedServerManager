import {Button, ButtonGroup, Card, IconButton, Input, Tab, TabList, TabPanel, Tabs} from "@mui/joy";
import {Settings} from "./server/Settings";
import {General} from "./server/General";
import {useEffect, useState} from "react";
import {server} from "../../wailsjs/go/models";
import {GetServer, SaveServer} from "../../wailsjs/go/server/ServerController";


type Props = {
    id: number | undefined
    className?: string
}


export const Server = ({id, className}: Props) => {
    const defaultServer = new server.Server
    defaultServer.id = -1
    defaultServer.serverAlias = ""



    const [serv, setServ] = useState<server.Server>(defaultServer)

    useEffect(() => {
        if (id !== undefined) {
            GetServer(id).then((s) => setServ(s))
        }
    }, [id]);

    useEffect(() => {
        if (serv.id == -1) {

        } else {
            SaveServer(serv)
        }
    }, [serv]);


    if (id !== undefined) {
        return (
            <Card className={className}>
                <Tabs size="sm" className={'flex h-full w-full overflow-y-auto'}>
                    <div className={'h-16 flex w-full'}>
                        <p className={'text-lg font-bold ml-8'}>{}<Input value={serv?.serverAlias} onChange={(e) => setServ((p) => ({ ...p, serverAlias: e.target.value }))}/></p>


                        <div className={'ml-auto my-auto mr-8'}>
                            <ButtonGroup aria-label="outlined primary button group">
                                <Button color={'success'} variant="solid">Start</Button>
                                <Button color={'danger'} variant="solid">Stop</Button>
                            </ButtonGroup>
                        </div>
                    </div>
                    <TabList className={'w-full'}>
                        <Tab variant="plain" indicatorInset color="neutral">General</Tab>
                        <Tab variant="plain" indicatorInset color="neutral">Settings</Tab>
                        <Tab variant="plain" indicatorInset color="neutral">Mods</Tab>
                        <Tab variant="plain" indicatorInset color="neutral">Plugins</Tab>
                        <Tab variant="plain" indicatorInset color="neutral">Modifiers</Tab>
                    </TabList>
                    <General/>
                    <Settings/>
                </Tabs>
            </Card>
        );
    } else {
        return (
            <Card className={className}>
                <Tabs size="sm" className={'flex h-full w-full overflow-y-auto'}>
                    <div className={'h-16 flex w-full'}>
                        <p className={'text-lg font-bold ml-8'}>No server found/selected</p>
                    </div>
                </Tabs>
            </Card>
        );
    }
};