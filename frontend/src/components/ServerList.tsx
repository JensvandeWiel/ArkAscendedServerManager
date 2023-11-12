import { Button, Card, List, ListItem, ListItemButton } from "@mui/joy";
import { server } from "../../wailsjs/go/models";
import { LogError } from "../../wailsjs/runtime";
import { IconPlus } from "@tabler/icons-react";

enum ServerListType {
  CARD,
  LIST,
}

type Props = {
  serverListType: ServerListType;
  servers: {[key: number]: server.Server}|null;
  setActiveServer: React.Dispatch<React.SetStateAction<number | undefined>>;
  setDrawerOpen: React.Dispatch<React.SetStateAction<boolean>>;
  handleCreateNewServerClicked: () => void;
}


export const ServerList = ({serverListType, servers, setActiveServer, setDrawerOpen, handleCreateNewServerClicked}:Props) => {
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
};