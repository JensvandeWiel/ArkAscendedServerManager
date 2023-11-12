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
import {IconDownload, IconTrash} from "@tabler/icons-react";
import {InstallUpdateVerify} from "../../wailsjs/go/installer/InstallerController";
import {EventsOn} from "../../wailsjs/runtime";
import {useAlert} from "../components/AlertProvider";
import {DeleteProfile} from "../../wailsjs/go/server/ServerController";

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
        OpenDirectoryDialog().then((val) => setServ((p) => ({ ...p, serverPath: val, convertValues: p.convertValues })))
    }

    function onStartInstallButtonClicked() {
        if (serv.serverPath == "") {
            addAlert("Server Path must be set to a path", "warning")
            return
        }
        setInstallerModalOpen(true)
        InstallUpdateVerify(serv.serverPath).catch((err) => {
            setAction("failed installing: " + err.message);
            setInstallerModalOpen(false);
            console.error(err);
            addAlert("Installer failed: " + err, "danger")
        })
    }

    function onCancelButtonClicked() {
        DeleteProfile(serv.id).then(() => {addAlert("Deleted profile", "success"); setTimeout(() => {location.reload()}, 500) }).catch((err) => {console.error(err); addAlert(err, "danger")})
    }

    useEffect(() => {
        EventsOn("installingUpdateAction", (data) => {setAction(data);})
        EventsOn("installingUpdateProgress", (data) => {setProgress(data);})
        EventsOn("appInstalled", () => {setIsCompleted(true);  setAction("Done"); setProgress(100)})
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
                <div className={"text-center pt-5"}>
                    <Button endDecorator={<IconDownload/>} onClick={onStartInstallButtonClicked} className={"mx-5"}>Install server</Button>
                    <Button endDecorator={<IconTrash/>} onClick={onCancelButtonClicked} className={"mx-5"} color={"danger"}>Delete profile</Button>
                </div>
            </Card>
        </div>
    );
}