import {
    Button,
    ButtonGroup,
    Card, DialogActions,
    DialogContent,
    DialogTitle,
    Divider, IconButton,
    Input,
    Modal,
    ModalDialog,
    Tab,
    TabList,
    Tabs, Tooltip
} from "@mui/joy";
import {Administration} from "./server/Administration";
import {General} from "./server/General";
import {useEffect, useState} from "react";
import {server} from "../../wailsjs/go/models";
import {
    CheckServerInstalled,
    ForceStopServer,
    GetServer, GetServerStatus,
    SaveServer,
    StartServer
} from "../../wailsjs/go/server/ServerController";
import {InstallUpdater} from "./InstallUpdater";
import {useAlert} from "../components/AlertProvider";
import {BrowserOpenURL, EventsOff, EventsOn} from "../../wailsjs/runtime";
import {IconAlertCircleFilled, IconExternalLink} from "@tabler/icons-react";
import {Console} from "./server/Console";
import {UpdaterModal} from "./UpdaterModal";
import {InstallUpdateVerify} from "../../wailsjs/go/installer/InstallerController";
import {SendRconCommand} from "../../wailsjs/go/helpers/HelpersController";


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
    const [serverStatus, setServerStatus] = useState(false)
    const [forceStopModalOpen, setForceStopModalOpen] = useState(false)
    const [updaterModalOpen, setUpdaterModalOpen] = useState(false)
    const {addAlert} = useAlert()

    //region useEffect land :)

    useEffect(() => {
        if (serv.id >= 0) {
            CheckServerInstalled(serv.id).then((val) => setIsInstalled(val)).catch((reason) => console.error(reason))
        }
    }, [serv]);

    useEffect(() => {
        if (id !== undefined) {
            GetServer(id).then((s) => {setServ(s)}).catch((reason) => console.error(reason))
        }
    }, [id]);

    useEffect(() => {
        if (serv.id >= 0) {
            SaveServer(serv).catch((reason) => {console.error(reason); addAlert(reason, "danger")})
        }
    }, [serv]);

    useEffect(() => {
        EventsOn("onServerExit", () => setServerStatus(false))
        return () => EventsOff("onServerExit")
    }, []);

    useEffect(() => {
        if (serv.id >= 0) {
            refreshServerStatus()
        }
    }, [serv]);

    //endregion

    function onServerStartButtonClicked() {

        if (serv.serverPath == "") {
            addAlert("Server Path must be set to a path", "warning")
            return
        }

        setUpdaterModalOpen(true)
        InstallUpdateVerify(serv.serverPath).catch((err) => {
            addAlert("failed installing: " + err.message, "danger");
            setUpdaterModalOpen(false);
            console.error(err);
        }).then(() => {
            setUpdaterModalOpen(false);
            startServer()
        })

    }

    function startServer() {
        StartServer(serv.id).catch((err) => {addAlert(err, "danger"); console.error(err)}).then(() => setTimeout(function () {
            refreshServerStatus()
        }, 200))
    }

    function onServerStopButtonClicked() {
        addAlert("Stopping server...", "neutral")
        SendRconCommand("saveworld", serv.ipAddress, serv.rconPort, serv.adminPassword)
            .then((resp) => {
                //send quit command
                SendRconCommand("doexit", serv.ipAddress, serv.rconPort, serv.adminPassword)
                    .catch((err) => addAlert("error sending exit command: " + err, "danger"));
            })
            .catch((err) => addAlert("error sending save command: " + err, "danger"));
    }

    function onServerForceStopButtonClicked() {
        ForceStopServer(serv.id).catch((err) => {addAlert(err, "danger"); console.error(err)}).then(() => setServerStatus(false))
    }

    function refreshServerStatus() {
        GetServerStatus(serv.id).catch((reason) => {console.error("serverstatus: " + reason); addAlert(reason, "danger")}).then((s) => {
            if (typeof s === "boolean") {
                setServerStatus(s)
            }
        })

    }

    if (id !== undefined) {
        return (
            <Card className={className}>
                {isInstalled? (<Tabs size="sm" className={'flex h-full w-full overflow-y-auto'}>
                    <div className={'h-16 flex w-full'}>
                        <div className="flex items-center">
                            <Input value={serv?.serverAlias} onChange={(e) => setServ((p) => ({ ...p, serverAlias: e.target.value }))}/>
                            <Tooltip title={"Open server install directory"}>
                                <IconButton className="text-lg font-bold ml-2" onClick={() => BrowserOpenURL("file:///" + serv.serverPath)}><IconExternalLink/></IconButton>
                            </Tooltip>
                        </div>


                        <div className={'ml-auto my-auto mr-8'}>
                            <ButtonGroup aria-label="outlined primary button group">
                                <Button color={'success'} variant="solid" disabled={serverStatus} onClick={onServerStartButtonClicked}>Start</Button>
                                <Button color={'danger'} variant="solid" disabled={!serverStatus} onClick={onServerStopButtonClicked}>Stop</Button>
                                <Button color={'danger'} variant="solid" disabled={!serverStatus} onClick={() => setForceStopModalOpen(true)}>Force stop</Button>
                            </ButtonGroup>
                            <UpdaterModal open={updaterModalOpen} onClose={() => {setUpdaterModalOpen(false);}}></UpdaterModal>
                            <Modal open={forceStopModalOpen} onClose={() => setForceStopModalOpen(false)}>
                                <ModalDialog variant="outlined" role="alertdialog">
                                    <DialogTitle>
                                        <IconAlertCircleFilled/>
                                        Confirmation
                                    </DialogTitle>
                                    <Divider />
                                    <DialogContent>
                                        Are you sure you want to forcefully stop the server? No save action will be performed!
                                    </DialogContent>
                                    <DialogActions>
                                        <Button variant="solid" color="danger" onClick={() => {setForceStopModalOpen(false); onServerForceStopButtonClicked()}}>
                                            Force stop
                                        </Button>
                                        <Button variant="plain" color="neutral" onClick={() => setForceStopModalOpen(false)}>
                                            Cancel
                                        </Button>
                                    </DialogActions>
                                </ModalDialog>
                            </Modal>
                        </div>
                    </div>
                    <TabList className={'w-full'}>
                        <Tab variant="plain" indicatorInset color="neutral">Console</Tab>
                        <Tab variant="plain" indicatorInset color="neutral">General</Tab>
                        <Tab variant="plain" indicatorInset color="neutral">Administration</Tab>
                    </TabList>
                    <Console serv={serv} setServ={setServ} serverStatus={serverStatus}/>
                    <General serv={serv} setServ={setServ}/>
                    <Administration/>
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
