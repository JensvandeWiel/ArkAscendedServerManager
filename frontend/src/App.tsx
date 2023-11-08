import {
    Alert, AspectRatio,
    Button,
    Card,
    DialogActions,
    DialogTitle, Divider,
    Drawer, IconButton, List, ListItem,
    ListItemButton, Modal,
    ModalClose, ModalDialog, Tooltip, Typography,
} from "@mui/joy";
import {ThemeSwitcher} from "./components/ThemeSwitcher";
import {HomeButton} from "./components/HomeButton";
import { ServerList } from "./components/ServerList";
import React, {useEffect, useState} from "react";
import {Server} from "./pages/Server";
import {IconArrowLeft, IconExternalLink, IconHome, IconInfoCircle, IconPlus, IconRefresh} from "@tabler/icons-react";
import {CreateServer, GetAllServers, GetAllServersFromDir, GetServerDir} from "../wailsjs/go/server/ServerController";
import {server} from "../wailsjs/go/models";
import {BrowserOpenURL, EventsOff, EventsOn, LogDebug} from "../wailsjs/runtime";
import {AlertProvider} from "./components/AlertProvider";
import banner from "./assets/AASM_V3_banner2.png"
import {GetVersion} from "../wailsjs/go/helpers/HelpersController";

enum ServerListType {
    CARD,
    LIST,
}

function App() {
    const [activeServer, setActiveServer] = useState<number | undefined>(undefined)
    const [drawerOpen, setDrawerOpen] = useState(false);
    const [servers, setServers] = useState<{[key: number]: server.Server}|null>(null);
    const [infoModalOpen, setInfoModalOpen] = useState(false);
    const [appVersion, setAppVersion] = useState("");




    //This gets all the servers but if one server is changed manually it does not update it!
    function getServers() {
        GetAllServers()
            .then(result => {
                if (typeof result === 'object') {
                    setServers(result);
                } else {
                    // Handle the case where the function returns a boolean.
                    LogDebug('GetAllServers status: ' + result)
                }
            })
            .catch(error => {
                console.error(error);
            });
    }

    function getServersFromDir() {
        GetAllServersFromDir()
            .then(result => {
                if (typeof result === 'object') {
                    setServers(result);
                    console.log(result)
                } else {
                    // Handle the case where the function returns a boolean.
                    LogDebug('GetAllServers status: ' + result)
                }
            })
            .catch(error => {
                console.error(error);
            });
    }

    useEffect(() => {
        getServers()
        GetVersion().then(version => setAppVersion(version))

    }, []);

    //events
    useEffect(() => {
        EventsOn("serverSaved", getServers)
        return () => {
            EventsOff("serverSaved")
        }
    }, []);

    const handleCreateNewServerClicked = () => {
        CreateServer(true).then((server) => {
            getServers()
            setActiveServer(server.id)
            setDrawerOpen(false)
        }).catch((r) => console.error(r))
    }

    const ServerDrawer = (
            <Drawer open={drawerOpen} onClose={() => setDrawerOpen(false)} size="md">
                <ModalClose/>

                <DialogTitle>Servers:</DialogTitle>
                <ServerList serverListType={ServerListType.LIST} servers={servers} setActiveServer={setActiveServer} setDrawerOpen={setDrawerOpen} handleCreateNewServerClicked={handleCreateNewServerClicked} />
                <Divider></Divider>
                <DialogActions>
                    <List>
                        <ListItem>

                            <Tooltip title={'Reload servers from disk'}><IconButton  color={'danger'} variant={"solid"}  onClick={() => {getServersFromDir(); setActiveServer(undefined)}}><IconRefresh/></IconButton></Tooltip>
                            <Tooltip title={'Refresh server list from cache'}><IconButton onClick={() => {getServers(); setActiveServer(undefined)}}><IconRefresh/></IconButton></Tooltip>
                            <Tooltip title={'Open servers folder'}><IconButton onClick={() => {GetServerDir().then((dir: string) => BrowserOpenURL("file:///" + dir))}}><IconExternalLink/></IconButton></Tooltip>
                        </ListItem>
                        <ListItem>
                            <ListItemButton onClick={handleCreateNewServerClicked}>
                                <IconPlus/> Create new server
                            </ListItemButton>
                        </ListItem>
                    </List>
                </DialogActions>
            </Drawer>
    );

    let mainUi = null;
    if (activeServer !== undefined) {
        mainUi = <Server id={activeServer} className={'row-span-5 m-5'}/>
    } else {
        if(servers !== null && Object.keys(servers).length > 0) {
            mainUi = (
                <div className={'row-span-5 m-5'}>
                    <h2>Select a server:</h2>
                    <ServerList serverListType={ServerListType.CARD} servers={servers} setActiveServer={setActiveServer} setDrawerOpen={setDrawerOpen} handleCreateNewServerClicked={handleCreateNewServerClicked} />
                </div>
            )
        }
        else {
            mainUi = (
                <div className={'row-span-5 m-5'}>
                    <h2>You have no servers yet!</h2>
                    <Button onClick={() => handleCreateNewServerClicked()}><IconPlus/> Create new server</Button>
                </div>
            )
        }
    }




    return (
        <div className={'min-h-screen max-h-screen overflow-y-auto flex-col flex'}>
            <AlertProvider>
                <div className={'h-16 flex'}>
                    <div className={'text-lg font-bold ml-8 my-auto'}>
                        <Button color={'neutral'} variant={'soft'} onClick={() => setDrawerOpen(true)}>
                            <IconArrowLeft/> Select server
                        </Button>
                    </div>
                    <div className={'ml-auto my-auto mr-8 gap-2 flex'}>
                        <ThemeSwitcher/>
                        <HomeButton setServ={setActiveServer}/>
                        <IconButton
                            variant="soft"
                            color="neutral"
                            onClick={() => setInfoModalOpen(true)}
                        >
                            <IconInfoCircle/>
                        </IconButton>
                        <Modal open={infoModalOpen} onClose={() => setInfoModalOpen(false)}>
                            <ModalDialog>
                                <AspectRatio minHeight="120px" maxHeight="200px">
                                    <img
                                        src={banner}
                                        loading="lazy"
                                        alt=""
                                    />
                                </AspectRatio>
                                <Divider/>
                                <div className={"p-4"}>
                                    <Typography level={"title-lg"}>
                                        Info:
                                    </Typography>
                                    <Typography level={"body-sm"} className={"flex-grow"}>
                                        Version: {appVersion}
                                    </Typography>
                                    <div style={{height: 200}}></div>
                                    <Button sx={{margin: 1}} onClick={() => BrowserOpenURL("https://github.com/JensvandeWiel/ArkAscendedServerManager")}>Github</Button>
                                    <Button sx={{margin: 1}} onClick={() => BrowserOpenURL("https://discord.gg/RmesnZ8FWf")}>Discord</Button>
                                    <Button sx={{margin: 1}} onClick={() => BrowserOpenURL("https://github.com/sponsors/JensvandeWiel")}>Sponsor me</Button>


                                </div>
                            </ModalDialog>
                        </Modal>
                    </div>
                </div>
                {mainUi}

                {ServerDrawer}
            </AlertProvider>
        </div>
    )
}

export default App
