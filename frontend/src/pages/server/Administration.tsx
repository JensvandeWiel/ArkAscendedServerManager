import {
    Button,
    Card, Checkbox, DialogActions,
    DialogContent,
    DialogTitle,
    Divider,
    FormLabel,
    Input, Modal,
    ModalDialog,
    TabPanel,
    Typography
} from "@mui/joy";
import {PasswordInput} from "../../components/PasswordInput";
import React, {useState} from "react";
import {DeleteProfile, DeleteServerFiles} from "../../../wailsjs/go/server/ServerController";
import {server} from "../../../wailsjs/go/models";
import {useAlert} from "../../components/AlertProvider";
import {IconAlertCircleFilled} from "@tabler/icons-react";

type Props = {
    setServ: React.Dispatch<React.SetStateAction<server.Server>>
    serv: server.Server;
    onServerFilesDeleted: () => void;

}

export function Administration({setServ, serv, onServerFilesDeleted}: Props) {

    const [deleteServerFilesModalOpen, setDeleteServerFilesModalOpen] = useState(false)
    const [deleteProfileModalOpen, setDeleteProfileModalOpen] = useState(false)
    const [deleteEverythingModalOpen, setDeleteEverythingModalOpen] = useState(false)

    const {addAlert} = useAlert();

    function onDeleteServerFilesButtonClicked() {
        DeleteServerFiles(serv.id).then(() => {addAlert("Deleted server files", "success"); setServ(serv); onServerFilesDeleted()}).catch((err) => {console.error(err); addAlert(err, "danger")})
    }

    function onDeleteProfileButtonClicked() {
        DeleteProfile(serv.id).then(() => {addAlert("Deleted profile", "success"); location.reload()}).catch((err) => {console.error(err); addAlert(err, "danger")})
    }

    function onDeleteEverythingButtonClicked() {
        DeleteServerFiles(serv.id).then(() => DeleteProfile(serv.id)).then(() => {addAlert("Deleted everything", "success"); location.reload()}).catch((err) => {console.error(err); addAlert(err, "danger")})
    }



    return (
        <TabPanel value={3} className={'space-y-8'}>
            <Card variant="soft"  className={''}>
                <Typography level="title-md">
                    Server Administration
                </Typography>
                <Divider className={'mx-2'}/>

                <div className={'space-x-4 w-full flex'}>
                    <div className={'inline-block'}>
                        <Modal open={deleteServerFilesModalOpen} onClose={() => setDeleteServerFilesModalOpen(false)}>
                            <ModalDialog variant="outlined" role="alertdialog">
                                <DialogTitle>
                                    <IconAlertCircleFilled/>
                                    Confirmation
                                </DialogTitle>
                                <Divider />
                                <DialogContent>
                                    Are you sure you want to delete the server files? You cannot reverse this action!
                                </DialogContent>
                                <DialogActions>
                                    <Button variant="solid" color="danger" onClick={() => {setDeleteServerFilesModalOpen(false); onDeleteServerFilesButtonClicked()}}>
                                        Delete Server Files
                                    </Button>
                                    <Button variant="plain" color="neutral" onClick={() => setDeleteServerFilesModalOpen(false)}>
                                        Cancel
                                    </Button>
                                </DialogActions>
                            </ModalDialog>
                        </Modal>
                        <Button color='danger' onClick={() => setDeleteServerFilesModalOpen(true)}>Delete server files</Button>
                    </div>
                    <div className={'inline-block'}>
                        <Modal open={deleteProfileModalOpen} onClose={() => setDeleteProfileModalOpen(false)}>
                            <ModalDialog variant="outlined" role="alertdialog">
                                <DialogTitle>
                                    <IconAlertCircleFilled/>
                                    Confirmation
                                </DialogTitle>
                                <Divider />
                                <DialogContent>
                                    Are you sure you want to delete the profile? You cannot reverse this action!
                                </DialogContent>
                                <DialogActions>
                                    <Button variant="solid" color="danger" onClick={() => {setDeleteProfileModalOpen(false); onDeleteProfileButtonClicked()}}>
                                        Delete Profile
                                    </Button>
                                    <Button variant="plain" color="neutral" onClick={() => setDeleteProfileModalOpen(false)}>
                                        Cancel
                                    </Button>
                                </DialogActions>
                            </ModalDialog>
                        </Modal>
                        <Button color='danger' onClick={() => setDeleteProfileModalOpen(true)}>Delete profile</Button>
                    </div>
                    <div className={'inline-block'}>
                        <Modal open={deleteEverythingModalOpen} onClose={() => setDeleteEverythingModalOpen(false)}>
                            <ModalDialog variant="outlined" role="alertdialog">
                                <DialogTitle>
                                    <IconAlertCircleFilled/>
                                    Confirmation
                                </DialogTitle>
                                <Divider />
                                <DialogContent>
                                    Are you sure you want to delete everything? You cannot reverse this action!
                                </DialogContent>
                                <DialogActions>
                                    <Button variant="solid" color="danger" onClick={() => {setDeleteEverythingModalOpen(false); onDeleteEverythingButtonClicked()}}>
                                        Delete Everything
                                    </Button>
                                    <Button variant="plain" color="neutral" onClick={() => setDeleteEverythingModalOpen(false)}>
                                        Cancel
                                    </Button>
                                </DialogActions>
                            </ModalDialog>
                        </Modal>
                        <Button color='danger' onClick={() => setDeleteEverythingModalOpen(true)}>Delete Everything</Button>
                    </div>
                </div>
            </Card>
            <Card variant="soft"  className={''}>
                <Typography level="title-md">
                    Server startup
                </Typography>
                <Divider className={'mx-2'}/>

                <div className={'space-x-4 w-full flex'}>
                    <div className={'inline-block'}>
                        <Checkbox label="Disable update on server start" checked={serv?.disableUpdateOnStart} onChange={(e) => setServ((p) => ({ ...p, disableUpdateOnStart: e.target.checked }))} />
                        <FormLabel>Custom server "dash" arguments (only use args like: -EnableIdlePlayerKick -ForceAllowCaveFlyers)</FormLabel>
                        <Input value={serv?.extraDashArgs} onChange={(e) => setServ((p) => ({ ...p, extraDashArgs: e.target.value }))}></Input>
                        <FormLabel>Custom server "questionmark" arguments (only use args like: ?PreventSpawnAnimations=true?PreventTribeAlliances=true)</FormLabel>
                        <Input value={serv?.extraQuestionmarkArguments} onChange={(e) => setServ((p) => ({ ...p, extraQuestionmarkArguments: e.target.value }))}></Input>
                    </div>
                </div>
            </Card>
        </TabPanel>
    );
}