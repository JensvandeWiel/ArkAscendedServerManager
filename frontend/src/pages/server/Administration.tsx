import {
    Button,
    Card, Checkbox, DialogActions,
    DialogContent,
    DialogTitle,
    Divider,
    FormLabel,
    Input, Modal,
    ModalDialog,
    TabPanel, Tooltip,
    Typography
} from "@mui/joy";
import React, {useState} from "react";
import {DeleteProfile, DeleteServerFiles, GetServerStartupCommand} from "../../../wailsjs/go/server/ServerController";
import {server} from "../../../wailsjs/go/models";
import {useAlert} from "../../components/AlertProvider";
import {IconAlertCircleFilled, IconInfoCircle} from "@tabler/icons-react";

type Props = {
    setServ: React.Dispatch<React.SetStateAction<server.Server>>
    serv: server.Server;
    onServerFilesDeleted: () => void;

}

function ServerAdministrationCard({setServ, serv, onServerFilesDeleted}: {setServ: React.Dispatch<React.SetStateAction<server.Server>>, serv: server.Server, onServerFilesDeleted: () => void}) {

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



    return <Card variant="soft" className={""}>
        <Typography level="title-md">
            Server Administration
        </Typography>
        <Divider className={"mx-2"}/>

        <div className={"space-x-4 w-full flex"}>
            <div className={"inline-block"}>
                <Modal open={deleteServerFilesModalOpen} onClose={() => setDeleteServerFilesModalOpen(false)}>
                    <ModalDialog variant="outlined" role="alertdialog">
                        <DialogTitle>
                            <IconAlertCircleFilled/>
                            Confirmation
                        </DialogTitle>
                        <Divider/>
                        <DialogContent>
                            Are you sure you want to delete the server files? You cannot reverse this action!
                        </DialogContent>
                        <DialogActions>
                            <Button variant="solid" color="danger" onClick={() => onDeleteServerFilesButtonClicked()}>
                                Delete Server Files
                            </Button>
                            <Button variant="plain" color="neutral" onClick={() => setDeleteServerFilesModalOpen(false)}>
                                Cancel
                            </Button>
                        </DialogActions>
                    </ModalDialog>
                </Modal>
                <Button color="danger" onClick={() => setDeleteServerFilesModalOpen(true)}>Delete server files</Button>
            </div>
            <div className={"inline-block"}>
                <Modal open={deleteProfileModalOpen} onClose={() => setDeleteProfileModalOpen(false)}>
                    <ModalDialog variant="outlined" role="alertdialog">
                        <DialogTitle>
                            <IconAlertCircleFilled/>
                            Confirmation
                        </DialogTitle>
                        <Divider/>
                        <DialogContent>
                            Are you sure you want to delete the profile? You cannot reverse this action!
                        </DialogContent>
                        <DialogActions>
                            <Button variant="solid" color="danger" onClick={() => onDeleteProfileButtonClicked()}>
                                Delete Profile
                            </Button>
                            <Button variant="plain" color="neutral" onClick={() => setDeleteProfileModalOpen(false)}>
                                Cancel
                            </Button>
                        </DialogActions>
                    </ModalDialog>
                </Modal>
                <Button color="danger" onClick={() => setDeleteProfileModalOpen(true)}>Delete profile</Button>
            </div>
            <div className={"inline-block"}>
                <Modal open={deleteEverythingModalOpen} onClose={() => setDeleteEverythingModalOpen(false)}>
                    <ModalDialog variant="outlined" role="alertdialog">
                        <DialogTitle>
                            <IconAlertCircleFilled/>
                            Confirmation
                        </DialogTitle>
                        <Divider/>
                        <DialogContent>
                            Are you sure you want to delete everything? You cannot reverse this action!
                        </DialogContent>
                        <DialogActions>
                            <Button variant="solid" color="danger" onClick={() => onDeleteEverythingButtonClicked()}>
                                Delete Everything
                            </Button>
                            <Button variant="plain" color="neutral" onClick={() => setDeleteEverythingModalOpen(false)}>
                                Cancel
                            </Button>
                        </DialogActions>
                    </ModalDialog>
                </Modal>
                <Button color="danger" onClick={() => setDeleteEverythingModalOpen(true)}>Delete Everything</Button>
            </div>
        </div>
    </Card>;
}

function ServerStartupCard({setServ, serv}: {setServ: React.Dispatch<React.SetStateAction<server.Server>>, serv: server.Server}) {

    const [showServerCommandModalOpen, setShowServerCommandModalOpen] = useState(false)
    const [serverCommand, setServerCommand] = useState("");

    const {addAlert} = useAlert();

    return (
        <Card variant="soft" className={''}>
            <div className={'space-x-4 w-full flex justify-between'}>
                <Typography level="title-md">
                    Server startup
                </Typography>
                <Typography level="title-md">
                    <div className={'space-x-4 w-full flex'}>
                        <div className={'inline-block'}>
                            <Modal open={showServerCommandModalOpen}
                                   onClose={() => setShowServerCommandModalOpen(false)}>
                                <ModalDialog variant="outlined" role="dialog">
                                    <DialogTitle>
                                        <IconInfoCircle/>
                                        Server startup command
                                    </DialogTitle>
                                    <Divider/>
                                    <DialogContent>
                                        {serverCommand}
                                    </DialogContent>
                                    <DialogActions>
                                        <Button variant="solid" color="neutral"
                                                onClick={() => setShowServerCommandModalOpen(false)}>
                                            Close
                                        </Button>
                                    </DialogActions>
                                </ModalDialog>
                            </Modal>

                            <Button color='neutral' onClick={() => {
                                setShowServerCommandModalOpen(true)
                                return GetServerStartupCommand(serv.id)
                                    .then((cmd: string) => {
                                        setServerCommand(cmd)
                                    }).catch((err) => {
                                        console.error(err);
                                        addAlert(err, "danger")
                                    })
                            }}>Show startup command</Button>
                        </div>
                    </div>
                </Typography>
            </div>
            <Divider className={'mx-2'}/>

            <div className={'space-x-4 w-full flex'}>
                <div className={'inline-block'}>
                    <Checkbox label="Disable update on server start" checked={serv?.disableUpdateOnStart}
                              onChange={(e) => setServ((p) => ({
                                  ...p,
                                  disableUpdateOnStart: e.target.checked,
                                  convertValues: p.convertValues
                              }))}/><br/>
                    {/*<Checkbox label="Restart server on server quit" checked={serv?.restartOnServerQuit} onChange={(e) => setServ((p) => ({ ...p, restartOnServerQuit: e.target.checked }))} />*/}
                    <Checkbox label="Start server when application opens" checked={serv?.startWithApplication}
                              onChange={(e) => setServ((p) => ({
                                  ...p,
                                  startWithApplication: e.target.checked,
                                  convertValues: p.convertValues
                                }))}/><br/>

                    <FormLabel>Custom server "dash" arguments (only use args like: -EnableIdlePlayerKick
                        -ForceAllowCaveFlyers)</FormLabel>
                    <Input value={serv?.extraDashArgs} onChange={(e) => setServ((p) => ({
                        ...p,
                        extraDashArgs: e.target.value,
                        convertValues: p.convertValues
                    }))}></Input>
                    <FormLabel>Custom server "questionmark" arguments (only use args like:
                        ?PreventSpawnAnimations=true?PreventTribeAlliances=true)</FormLabel>
                    <Input value={serv?.extraQuestionmarkArguments} onChange={(e) => setServ((p) => ({
                        ...p,
                        extraQuestionmarkArguments: e.target.value,
                        convertValues: p.convertValues
                    }))}></Input>
                </div>
            </div>
        </Card>
    )
}

function AutoSaveSettingsCard({ setServ, serv }: {setServ: React.Dispatch<React.SetStateAction<server.Server>>, serv: server.Server}) {
    
    const {addAlert} = useAlert();
    
    const handleAutoSaveIntervalChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const newAutoSaveInterval = parseInt(e.target.value, 10);
    
        if (newAutoSaveInterval > 0) {
            setServ((prev) => ({
              ...prev,
              autoSaveInterval: newAutoSaveInterval,
              convertValues: prev.convertValues,
            }));
          } else {
            addAlert("'Auto-Save Interval' must be greater than 0", "danger")
          }
      };
    
    return (
        <Card variant="soft"  className={''}>
            <Typography level="title-md">
                Auto-Save Settings
            </Typography>
            <Divider className={'mx-2'}/>

            <div className={'space-x-4 w-full flex'}>
                <div className={'inline-block'}>
                    <Checkbox label="Enable Auto-Save" checked={serv?.autoSaveEnabled}
                              onChange={(e) => setServ((p) => ({
                                  ...p,
                                  autoSaveEnabled: e.target.checked,
                                  convertValues: p.convertValues
                                }))}/><br/>

                    <FormLabel>Auto-Save Interval (minutes)</FormLabel>
                    <Input
                        className={''}
                        type={'number'}
                        required
                        value={serv?.autoSaveInterval}
                        disabled={!serv?.autoSaveEnabled}
                        onChange={handleAutoSaveIntervalChange}
                        />
                </div>
            </div>
        </Card>
    )
}

function ExtraSettingsCard({setServ, serv}: {setServ: React.Dispatch<React.SetStateAction<server.Server>>, serv: server.Server}) {
    return (
        <Card variant="soft" className={''}>
            <Typography level="title-md">
                Extra Settings
            </Typography>
            <Divider className={'mx-2'}/>

            <div className={'space-x-4 w-full flex'}>
                <div className={'inline-block'}>
                    <Tooltip title={"Loads server config form ini first instead of json"}>
                        <Checkbox label="Use ini config (only reloads on server start) (not recommended)"
                                  checked={serv?.useIniConfig} onChange={(e) => setServ((p) => ({
                            ...p,
                            useIniConfig: e.target.checked,
                            convertValues: p.convertValues
                        }))}/>
                    </Tooltip>
                    <br/>
                    <Tooltip title={"Enables discord webhook messages"}>
                        <Checkbox label="Discord webhook messages" checked={serv?.discordWebHookEnabled}
                                  onChange={(e) => setServ((p) => ({
                                      ...p,
                                      discordWebHookEnabled: e.target.checked,
                                      convertValues: p.convertValues
                                  }))}/>
                    </Tooltip>
                    <Tooltip title={"The url from the webhook (if not set it will fail)"}>
                            <span>
                                <FormLabel>Discord webhook url</FormLabel>
                            <Input value={serv?.discordWebHook} onChange={(e) => setServ((p) => ({
                                ...p,
                                discordWebHook: e.target.value,
                                convertValues: p.convertValues
                            }))}></Input>
                            </span>
                    </Tooltip>


                </div>
            </div>
        </Card>
    )
}

export function Administration({setServ, serv, onServerFilesDeleted}: Props) {
    return (
        <TabPanel value={3} className={'space-y-8'}>
            <ServerAdministrationCard serv={serv} setServ={setServ} onServerFilesDeleted={onServerFilesDeleted}/>
            <ServerStartupCard serv={serv} setServ={setServ} />
            <AutoSaveSettingsCard setServ={setServ} serv={serv}/>
            <ExtraSettingsCard setServ={setServ} serv={serv}/>
        </TabPanel>
    );
}