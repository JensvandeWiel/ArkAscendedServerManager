import {
    Button,
    Card,
    DialogActions,
    DialogTitle, Divider,
    Drawer, IconButton, List, ListItem,
    ListItemButton,
    ModalClose, Tooltip,
} from "@mui/joy";
import {ThemeSwitcher} from "./components/ThemeSwitcher";
import {HomeButton} from "./components/HomeButton";
import {useEffect, useState} from "react";
import {Server} from "./pages/Server";
import {IconArrowLeft, IconExternalLink, IconPlus, IconRefresh} from "@tabler/icons-react";
import {CreateServer, GetAllServers, GetAllServersFromDir, GetServerDir} from "../wailsjs/go/server/ServerController";
import {server} from "../wailsjs/go/models";
import {BrowserOpenURL, EventsOff, EventsOn, LogDebug, LogError} from "../wailsjs/runtime";

enum ServerListType {
    CARD,
    LIST,
}

function App() {
    const [activeServer, setActiveServer] = useState<number | undefined>(undefined)
    const [drawerOpen, setDrawerOpen] = useState(false);
    const [servers, setServers] = useState<{[key: number]: server.Server}|null>(null);

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

    function getServerList(serverListType: ServerListType) {
        return (
            <>
                <List className={'flex flex-row flex-wrap items-start'}>
                {
                    (servers === null) ? (
                        <ListItem>
                            No servers found or failed to find servers
                        </ListItem>
                    ) : (
                        Object.keys(servers).map((key) => {
                            const index = parseInt(key, 10); // The second argument is the base (radix), 10 for base 10 (decimal)

                            if (isNaN(index)) {
                                LogError("Parsing server key failed")
                            }

                            const server = servers[index]; // Parse the key to a number
                            if(serverListType === ServerListType.LIST){
                                return (
                                    <ListItem key={index}>
                                        <ListItemButton onClick={() => {setActiveServer(index); setDrawerOpen(false)}}>
                                            {index}: {server.serverAlias? server.serverAlias : "Unnamed Server"}
                                        </ListItemButton>
                                    </ListItem>
                                );
                            }
                            else if(serverListType === ServerListType.CARD){
                                console.log(server)
                                return (
                                    <ListItem className={'w-[calc(100%_*_(1/4))] p-[10px]'} key={index}>
                                        <Card className={'cursor-pointer w-full'} onClick={() => {setActiveServer(index)}}>
                                            <div className={'px-2'}>
                                                <div className={'text-xl font-bold break-all'}>{server.serverAlias? server.serverAlias : "Unnamed Server"}</div>
                                                <div className={'text-lg mb-2'}>{server.ipAddress}:{server.queryPort}</div>

                                                <div>Players: 0/40</div> {/* this still needs to be implemented! */}
                                                <div>Status: Running</div> {/* this still needs to be implemented! */}
                                            </div>
                                        </Card>
                                    </ListItem>
                                );
                            }
                        })
                    )
                }
                </List>
                {serverListType === ServerListType.CARD ? <Button className={'ml-[10px]'} onClick={() => handleCreateNewServerClicked()}><IconPlus/> Create new server</Button> : <></>}
            </>
        )
    }

    const ServerDrawer = (
            <Drawer open={drawerOpen} onClose={() => setDrawerOpen(false)} size="md">
                <ModalClose/>

                <DialogTitle>Servers:</DialogTitle>
                <List>
                    {getServerList(ServerListType.LIST)}
                </List>
                <Divider></Divider>
                <DialogActions>
                    <List>
                        <ListItem>
                            <Tooltip title={'Reload servers from disk'}><IconButton  color={'danger'} variant={"solid"}  onClick={() => getServersFromDir()}><IconRefresh/></IconButton></Tooltip>
                            <Tooltip title={'Refresh server list from cache'}><IconButton onClick={() => getServers()}><IconRefresh/></IconButton></Tooltip>
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
                    {getServerList(ServerListType.CARD)}
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
        <div className={'min-h-screen max-h-screen overflow-y-auto flex-col'}>
            <div className={'h-16 flex'}>
                <div className={'text-lg font-bold ml-8 my-auto'}>
                    <Button color={'neutral'} variant={'soft'} onClick={() => setDrawerOpen(true)}>
                        <IconArrowLeft/> Select server
                    </Button>
                </div>
                <div className={'ml-auto my-auto mr-8 gap-2 flex'}>
                    <ThemeSwitcher/>
                    <HomeButton setServ={setActiveServer}/>
                </div>
            </div>
            {mainUi}

            {ServerDrawer}
        </div>
    )
}

export default App
