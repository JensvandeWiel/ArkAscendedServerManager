import {
    Button,
    DialogActions,
    DialogTitle, Divider,
    Drawer, IconButton, List, ListItem,
    ListItemButton,
    ModalClose, Tooltip,
} from "@mui/joy";
import {ThemeSwitcher} from "./components/ThemeSwitcher";
import {useEffect, useState} from "react";
import {Server} from "./pages/Server";
import {IconArrowLeft, IconExternalLink, IconPlus, IconRefresh} from "@tabler/icons-react";
import {CreateServer, GetAllServers, GetAllServersFromDir, GetServerDir} from "../wailsjs/go/server/ServerController";
import {server} from "../wailsjs/go/models";
import {BrowserOpenURL, EventsOff, EventsOn, LogDebug, LogError} from "../wailsjs/runtime";




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
        CreateServer(true).then(() => {
            getServers()
        })
    }

    const ServerList = (
        <List>
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
                        return (
                            <ListItem key={index}>

                                <ListItemButton onClick={() => {setActiveServer(index); setDrawerOpen(false)}}>
                                    {index}: {server.serverAlias? server.serverAlias : "Unnamed Server"}
                                </ListItemButton>
                            </ListItem>
                        );
                    })
                )
            }
        </List>
    )



    const ServerDrawer = (
            <Drawer open={drawerOpen} onClose={() => setDrawerOpen(false)} size="md">
                <ModalClose/>

                <DialogTitle>Servers:</DialogTitle>
                <List>
                    <ListItemButton onClick={() => setActiveServer(undefined)}>
                        None
                    </ListItemButton>
                    {ServerList}
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

    return (
        <div className={'min-h-screen max-h-screen overflow-y-auto flex-col'}>
            <div className={'h-16 flex'}>
                <div className={'text-lg font-bold ml-8 my-auto'}>
                    <Button color={'neutral'} variant={'soft'} onClick={() => setDrawerOpen(true)}>
                        <IconArrowLeft/> Select server
                    </Button>
                </div>
                <div className={'ml-auto my-auto mr-8'}><ThemeSwitcher/></div>
            </div>
            <Server className={'row-span-5 m-5'} id={activeServer}/>

            {ServerDrawer}
        </div>
    )
}

export default App
