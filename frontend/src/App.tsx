import {
    Button,
    DialogActions,
    DialogTitle, Divider,
    Drawer, List, ListItem,
    ListItemButton,
    ModalClose,
} from "@mui/joy";
import {ThemeSwitcher} from "./components/ThemeSwitcher";
import {useState} from "react";
import {Server} from "./pages/Server";
import {IconArrowLeft, IconPlus} from "@tabler/icons-react";


function App() {
    const [activeServer, setActiveServer] = useState<number | undefined>(0)
    const [drawerOpen, setDrawerOpen] = useState(false);

    const ServerDrawer = (
            <Drawer open={drawerOpen} onClose={() => setDrawerOpen(false)} size="md">
                <ModalClose/>
                <DialogTitle>Servers:</DialogTitle>
                <List>

                    <ListItemButton onClick={() => setActiveServer(undefined)}>
                        None
                    </ListItemButton>
                    {[...new Array(4)].map((_, index) => (
                        <ListItem key={index}>

                            <ListItemButton onClick={() => setActiveServer(index)}>
                                Server {index}
                            </ListItemButton>
                        </ListItem>
                    ))}
                </List>
                <Divider></Divider>
                <DialogActions>
                    <List>
                        <ListItem>
                            <ListItemButton>
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
            <Server className={'row-span-5 m-5'} server={activeServer}/>

            {ServerDrawer}
        </div>
    )
}

export default App
