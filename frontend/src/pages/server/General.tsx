import {
    Card,
    Divider,
    FormLabel,
    Input,
    Select,
    TabPanel,
    Typography,
    Option, Autocomplete, Textarea, Tooltip, Checkbox,
} from "@mui/joy";

import {server} from "../../../wailsjs/go/models";
import React, {useEffect, useState} from "react";
import {GetNetworkInterfacesIp, GetValueFromGus, UpdateValueInGus} from "../../../wailsjs/go/server/ServerController";
import {PasswordInput} from "../../components/PasswordInput";
import {Slider} from "../../components/Slider";
import {useAlert} from "../../components/AlertProvider";

type Props = {
    setServ: React.Dispatch<React.SetStateAction<server.Server>>
    serv: server.Server;

}

function GeneralSettings({ setServ, serv }: {setServ: React.Dispatch<React.SetStateAction<server.Server>>, serv: server.Server}) {


    const {addAlert} = useAlert()

    const [autoSavePeriodMinutes, setAutoSavePeriodMinutes] = useState<number>()
    const [message, setMessage] = useState<string>()
    const [duration, setDuration] = useState<number>()
    const [kickIdlePlayersPeriod, setKickIdlePlayersPeriod] = useState<number>()
    useEffect(() => {
        GetValueFromGus(serv.id, "ServerSettings", "AutoSavePeriodMinutes").then((val) => setAutoSavePeriodMinutes(parseInt(val))).catch((reason) => {console.error(reason); addAlert(reason, "danger")})
        GetValueFromGus(serv.id, "MessageOfTheDay", "Message").then((val) => setMessage(val)).catch((reason) => {console.error(reason); addAlert(reason, "danger")})
        GetValueFromGus(serv.id, "MessageOfTheDay", "Duration").then((val) => setDuration(parseInt(val))).catch((reason) => {console.error(reason); addAlert(reason, "danger")})
        GetValueFromGus(serv.id, "ServerSettings", "KickIdlePlayersPeriod").then((val) => setKickIdlePlayersPeriod(parseInt(val))).catch((reason) => {console.error(reason); addAlert(reason, "danger")})
    }, []);

    return (
        <Card variant="soft"  className={''}>
            <Typography level="title-md">
            Server Name and Passwords
            </Typography>
            <Divider className={'mx-2'}/>
            <div className={'w-[100%] space-y-4'}>
                <div className={''}>
                    <FormLabel>Server Name:</FormLabel>
                    <Input value={serv?.serverName} onChange={(e) => setServ((p) => ({ ...p, serverName: e.target.value }))} ></Input>
                </div>
                <div className={''}>
                    <FormLabel>Server Password:</FormLabel>
                    <PasswordInput value={serv?.serverPassword} onChange={(e) => setServ((p) => ({ ...p, serverPassword: e.target.value }))} ></PasswordInput>
                </div>
                <div className={''}>
                    <FormLabel>Admin Password:</FormLabel>
                    <PasswordInput value={serv?.adminPassword} onChange={(e) => setServ((p) => ({ ...p, adminPassword: e.target.value }))} ></PasswordInput>
                </div>
                <div className={''}>
                    <FormLabel>Spectator Password:</FormLabel>
                    <PasswordInput value={serv?.spectatorPassword} onChange={(e) => setServ((p) => ({ ...p, spectatorPassword: e.target.value }))} ></PasswordInput>
                </div>
            </div>
            <Typography level="title-md">
                Server Map
            </Typography>
            <Divider className={'mx-2'}/>
            <div className={'w-[100%] space-y-4'}>
                <div className={''}>
                    <FormLabel>Map Name of Mod Map path:</FormLabel>
                    <Autocomplete freeSolo disableClearable inputValue={serv?.serverMap} options={["TheIsland_WP"]} onChange={(e, v) => setServ((p) => ({ ...p, serverMap: v }))} ></Autocomplete>
                </div>
            </div>
            <Typography level="title-md">
                Auto Save
            </Typography>
            <Divider className={'mx-2'}/>
            <div className={'w-[100%] space-y-4'}>
                <div className={''}>
                    <FormLabel>Auto Save interval:</FormLabel>
                    <Tooltip title={"Duration that the message is visible in seconds"}>
                        <Slider
                            value={autoSavePeriodMinutes?? 0}
                            onChange={(v) => {
                                setAutoSavePeriodMinutes(v)
                                UpdateValueInGus(serv.id, "ServerSettings", "AutoSavePeriodMinutes", v.toString()).catch((reason) => {console.error(reason); addAlert(reason, "danger")})
                            }}
                        />
                    </Tooltip>

                </div>
            </div>
            <Typography level="title-md">
                Message of the Day
            </Typography>
            <Divider className={'mx-2'}/>
            <div className={'w-[100%] space-y-4'}>
                <div className={''}>
                    <FormLabel>Message</FormLabel>
                    <Textarea minRows={5} value={message} onChange={(e) => {
                        setMessage(e.target.value)
                        UpdateValueInGus(serv.id, "MessageOfTheDay", "Message", e.target.value).catch((reason) => {console.error(reason); addAlert(reason, "danger")})
                    }}></Textarea>
                </div>
                <div className={''}>
                    <FormLabel>Duration:</FormLabel>
                    <Tooltip title={"Duration that the message is visible in seconds"}>
                        <Slider
                            sliderStep={1}
                            sliderMax={240}
                            value={duration?? 0}
                            onChange={(v) => {
                                if (v >= 0) {
                                    setDuration(v)
                                    UpdateValueInGus(serv.id, "MessageOfTheDay", "Duration", v.toString()).catch((reason) => {console.error(reason); addAlert(reason, "danger")})
                                }
                            }}
                        />
                    </Tooltip>

                </div>
            </div>
            <Typography level="title-md">
                Server Settings
            </Typography>
            <Divider className={'mx-2'}/>
            <div className={'w-[100%] space-y-4'}>
                <div className={'flex space-x-2'}>
                    <div className={"flex-grow"}>
                        <FormLabel>Max Players:</FormLabel>
                        <Slider
                            className={""}
                            sliderStep={1}
                            sliderMax={240}
                            value={serv?.maxPlayers}
                            onChange={(v) => {
                                if (v >= 0) {
                                    setServ((p) => {
                                        const newState = {...p};
                                        newState.maxPlayers = v;
                                        return newState;
                                    })
                                }
                            }}
                        />
                    </div>
                    <div className={"flex-grow"}>
                        <Tooltip title={"The duration before an idle player gets kicked in seconds"}>
                            <FormLabel> <Checkbox className={"mr-2"} checked={serv?.kickIdlePlayers} onChange={(e) => setServ((p) => ({ ...p, kickIdlePlayers: e.target.checked }))}/> Kick Idle Players Period:</FormLabel>
                        </Tooltip>
                        <Slider
                            className={""}
                            disabled={!(serv?.kickIdlePlayers)}
                            sliderStep={1}
                            sliderMax={3600}
                            value={kickIdlePlayersPeriod?? 0}
                            onChange={(v) => {
                                if (v >= 0) {
                                    setKickIdlePlayersPeriod(v)
                                    UpdateValueInGus(serv.id, "ServerSettings", "KickIdlePlayersPeriod", v.toString()).catch((reason) => {console.error(reason); addAlert(reason, "danger")})
                                }
                            }}
                        />

                    </div>
                </div>
            </div>
        </Card>
    )
}
function NetworkingCard({ setServ, serv }: {setServ: React.Dispatch<React.SetStateAction<server.Server>>, serv: server.Server}) {

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
        <Card variant="soft"  className={''}>
            <Typography level="title-md">
                Networking
            </Typography>
            <Divider className={'mx-2'}/>
            <FormLabel>IP Address:</FormLabel>
            <Select
                value={serv.ipAddress?? "0.0.0.0"}
                onChange={(e, value) => {
                    const newValue = value;
                    setServ(
                        prevState =>{
                            return{
                                ...prevState,
                                ipAddress: newValue?? "0.0.0.0",
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
                    <Input className={''} type={'number'} required value={serv?.serverPort} onChange={(e) => (parseInt(e.target.value) >= 1 && parseInt(e.target.value) <= 65535) ? setServ((p) => ({ ...p, serverPort: parseInt(e.target.value), peerPort: parseInt(e.target.value) + 1})) : null} ></Input>
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
    )
}


export function General({serv, setServ}: Props) {
    return (
        <TabPanel value={1} className={'space-y-8'}>
            <GeneralSettings setServ={setServ} serv={serv} />
            <NetworkingCard setServ={setServ} serv={serv} />
        </TabPanel>
    );
}