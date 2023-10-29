import React, {useEffect, useState} from "react";
import {server} from "../../wailsjs/go/models";
import {
    Button,
    Card,
    Divider,
    FormLabel,
    Input,
    LinearProgress,
    Modal,
    ModalDialog,
    Typography
} from "@mui/joy";
import {OpenDirectoryDialog} from "../../wailsjs/go/helpers/HelpersController";
import {IconDownload} from "@tabler/icons-react";
import {Install} from "../../wailsjs/go/installer/InstallerController";
import {EventsOn} from "../../wailsjs/runtime";
import {useAlert} from "../components/AlertProvider";

type Props = {
    setServ: React.Dispatch<React.SetStateAction<server.Server>>
    serv: server.Server;
    onInstalled: () => void;
}

export function InstallUpdater({setServ, serv, onInstalled}: Props) {

    const [action, setAction] = useState("Preparing")
    const [progress, setProgress] = useState(0.00)
    const [isCompleted, setIsCompleted] = useState(false)
    const [installerModalOpen, setInstallerModalOpen] = useState(false)
    const {addAlert} = useAlert()
    function onServerPathClicked() {
        OpenDirectoryDialog().then((val) => setServ((p) => ({ ...p, serverPath: val })))
    }

    function onStartInstallButtonClicked() {
        if (serv.serverPath == "") {
            addAlert("Server Path must be set to a path", "warning")
            return
        }
        setInstallerModalOpen(true)
        Install(serv.serverPath).catch((err) => {setAction("failed installing: " + err.message); console.error(err); addAlert("Installer failed: " + err, "danger")})
    }

    useEffect(() => {
        EventsOn("installingUpdateAction", (data) => {setAction(data);})
        EventsOn("installingUpdateProgress", (data) => {setProgress(data);})
        EventsOn("appInstalled", (i) => {setIsCompleted(true);  setAction("Done"); setProgress(100)})
    }, []);





    return (
        <div>
            <Modal open={installerModalOpen} >
                <ModalDialog>
                    <Typography level="title-md">
                        Install/Update server
                    </Typography>
                    <Divider className={'mx-2'}/>
                    <Typography fontWeight={700} level="title-md">
                        Status: {action}
                    </Typography>
                    <Typography fontWeight={700} level="title-md">
                        Progress:
                    </Typography>
                    <div className={'w-1/2 mt-4'}>
                        <LinearProgress determinate value={progress} />
                    </div>
                    <Button disabled={!isCompleted} onClick={() => {setInstallerModalOpen(false); setServ(serv); onInstalled()}} className={"w-2/12"}>
                        Go Back
                    </Button>
                </ModalDialog>
            </Modal>
            <Card variant="soft"  className={''}>
                <Typography level="title-md">
                    Install server
                </Typography>
                <Divider className={'mx-2'}/>
                <FormLabel>Server Path</FormLabel>
                <Input className={'w-1/3'} value={serv.serverPath} required onClick={onServerPathClicked} ></Input>
                <div className={"text-center"}>
                    <Button endDecorator={<IconDownload/>} onClick={onStartInstallButtonClicked}>Install server</Button>
                </div>


            </Card>
        </div>
    );
}