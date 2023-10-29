import {Button, ButtonGroup, Card, Input, Tab, TabList, Tabs} from "@mui/joy";
import {Settings} from "./server/Settings";
import {General} from "./server/General";
import {useEffect, useState} from "react";
import {server} from "../../wailsjs/go/models";
import {CheckServerInstalled, GetServer, SaveServer} from "../../wailsjs/go/server/ServerController";
import {InstallUpdater} from "./InstallUpdater";


type Props = {
    id: number | undefined
    className?: string
}


export const Server = ({id, className}: Props) => {
    const defaultServer = new server.Server
    defaultServer.id = -1
    defaultServer.serverAlias = ""
    defaultServer.ipAddress = "0.0.0.0"



    const [serv, setServ] = useState<server.Server>(defaultServer)
    const [isInstalled, setIsInstalled] = useState(false)

    useEffect(() => {
        if (serv.id >= 0) {
            CheckServerInstalled(serv.id).then((val) => setIsInstalled(val)).catch((reason) => console.error(reason))
        }
    }, [serv]);

    useEffect(() => {
        if (id !== undefined) {
            GetServer(id).then((s) => setServ(s)).catch((reason) => console.error(reason))
        }
    }, [id]);

    useEffect(() => {
        if (serv.id == -1) {

        } else {
            SaveServer(serv).catch((reason) => console.error(reason))
        }


    }, [serv]);


    if (id !== undefined) {
        return (
            <Card className={className}>
                {isInstalled? (<Tabs size="sm" className={'flex h-full w-full overflow-y-auto'}>
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
                    <General serv={serv} setServ={setServ}/>
                    <Settings/>
                </Tabs>) : (<InstallUpdater serv={serv} setServ={setServ} onInstalled={() => setIsInstalled(true)}/>)}
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