import {Button, Divider, LinearProgress, Modal, ModalClose, ModalDialog, Typography} from "@mui/joy";
import React, {useEffect, useState} from "react";
import {server} from "../../wailsjs/go/models";
import {useAlert} from "../components/AlertProvider";
import {EventsOn} from "../../wailsjs/runtime";


type Props = {
    onCompleted?: () => void;
    open: boolean;
    onClose?: () => void;
}

export function UpdaterModal({onCompleted, open, onClose}: Props) {

    const [action, setAction] = useState("Preparing")
    const [progress, setProgress] = useState(0.00)
    const [isCompleted, setIsCompleted] = useState(false)

    useEffect(() => {
        EventsOn("installingUpdateAction", (data) => {setAction(data);})
        EventsOn("installingUpdateProgress", (data) => {setProgress(data);})
        EventsOn("appInstalled", (i) => {setIsCompleted(true);  setAction("Preparing"); setProgress(0.00)})
    }, []);

    useEffect(() => {
        if (isCompleted && onCompleted) {
            onCompleted()

        }
    }, [isCompleted]);

    return (
        <Modal open={open} onClose={onClose} >
            <ModalDialog>
                <Typography level="title-md">
                    Updating server...
                </Typography>
                <Typography level="body-sm" >
                    The server will start automatically after the update
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
            </ModalDialog>
        </Modal>
    );
}