import { Button, Card, List, ListItem, ListItemButton } from "@mui/joy";
import { server } from "../../wailsjs/go/models";
import { IconPlus } from "@tabler/icons-react";
import { GetConnectedPlayerCount, GetServerStatus } from "../../wailsjs/go/server/ServerController";
import React, { useState, useEffect } from 'react';
import {useAlert} from "../components/AlertProvider";

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

export const ServerList = ({ serverListType, servers, setActiveServer, setDrawerOpen, handleCreateNewServerClicked }: Props) => {
    const [playerCounts, setPlayerCounts] = useState<Record<number, number | undefined>>({});
    const [serverStatuses, setServerStatuses] = useState<Record<number, boolean | undefined>>({});

    const {addAlert} = useAlert()

    // Having a separate useEffect for player counts and server statuses means that each value doesn't have to wait for the other
    // I found that having them joined created a situation where player count would be loading as the server started, which forced
    // the server status to wait for player count to be fetched.
    useEffect(() => {
        const refreshPlayerCounts = async () => {
            if (servers) {
                await Promise.all(
                    Object.keys(servers).map(async (key) => {
                        const serverId = parseInt(key, 10);
                        if (!isNaN(serverId)) {
                            GetConnectedPlayerCount(serverId).then((count) => {
                                setPlayerCounts((prevPlayerCounts) => {
                                    return {
                                        ...prevPlayerCounts,
                                        [serverId]: count as number
                                    };
                                });
                            }).catch(() => {
                                setPlayerCounts((prevPlayerCounts) => {
                                    return {
                                    ...prevPlayerCounts,
                                    [serverId]: 0
                                    };
                                });
                            })
                        } else {
                            console.error("Parsing server key failed");
                        }
                    })
                );
            }
        };

        refreshPlayerCounts();
    }, [servers]);

    useEffect(() => {
        const refreshServerStatuses = async () => {
            if (servers) {
                await Promise.all(
                    Object.keys(servers).map(async (key) => {
                        const serverId = parseInt(key, 10);
                        if (!isNaN(serverId)) {
                            GetServerStatus(serverId).catch((reason) => {console.error("serverstatus: " + reason); addAlert(reason, "danger")}).then((count) => {
                                setServerStatuses((prevServerStatuses) => {
                                    return {
                                        ...prevServerStatuses,
                                        [serverId]: count as boolean | undefined
                                    };
                                });
                            });
                        } else {
                            console.error("Parsing server key failed");
                        }
                    })
                );
            }
        };

        refreshServerStatuses();
    }, [servers]);

    return (
        <>
            <List className={'flex flex-row flex-wrap items-start'}>
            {servers === null ? (
                <ListItem>No servers found or failed to find servers</ListItem>
            ) : (
                    Object.keys(servers).map((key) => {
                        const index = parseInt(key, 10);
                        if (isNaN(index)) {
                            console.error("Parsing server key failed");
                            return null;
                        }
                
                        const server = servers[index];
                        const playerCount = playerCounts[index];
                        const serverStatus = serverStatuses[index];
                        if (serverListType === ServerListType.LIST) {
                            return (
                                <ListItem key={index}>
                                    <ListItemButton onClick={() => { setActiveServer(index); setDrawerOpen(false) }}>
                                        {index}: {server.serverAlias ? server.serverAlias : "Unnamed Server"}
                                    </ListItemButton>
                                </ListItem>
                            );
                        } else if (serverListType === ServerListType.CARD) {
                            return (
                                <ListItem className={'w-[calc(100%_*_(1/4))] p-[10px]'} key={index}>
                                    <Card className={'cursor-pointer w-full'} onClick={() => { setActiveServer(index) }}>
                                        <div className={'px-2'}>
                                        <div className={'text-xl font-bold break-all'}>{server.serverAlias ? server.serverAlias : "Unnamed Server"}</div>
                                        <div className={'text-lg mb-2'}>{server.ipAddress}:{server.queryPort}</div>
                                        
                                        <div>Players: {playerCount !== undefined ? playerCount : 'Loading...'}</div>
                                        <div>Status: {serverStatus !== undefined ? (serverStatus ? 'Running' : 'Stopped') : 'Loading...'}</div>
                                        </div>
                                    </Card>
                                </ListItem>
                            );
                        }
                    })
                )}
            </List>
                {serverListType === ServerListType.CARD ? <Button className={'ml-[10px]'} onClick={() => handleCreateNewServerClicked()}><IconPlus /> Create new server</Button> : <></>}
            </>
        );
};