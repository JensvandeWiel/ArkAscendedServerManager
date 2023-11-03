import React from "react";
import {server} from "../../../wailsjs/go/models";
import {
    Button,
    Card, Checkbox,
    DialogActions,
    DialogContent,
    DialogTitle,
    Divider, FormLabel, Input,
    Modal,
    ModalDialog,
    TabPanel,
    Typography
} from "@mui/joy";
import {IconAlertCircleFilled} from "@tabler/icons-react";

type Props = {
    setServ: React.Dispatch<React.SetStateAction<server.Server>>
    serv: server.Server;

}

export function Settings({setServ, serv}: Props) {
    return (
        <TabPanel value={2} className={'space-y-8'}>
            <Card variant="soft"  className={''}>
                <Typography level="title-md">
                    Mods
                </Typography>
                <Divider className={'mx-2'}/>

                <div className={'space-x-4 w-full flex'}>
                    <div className={'inline-block'}>
                        <FormLabel>Mods (e.g.: modid1,modid2,modid3)</FormLabel>
                        <Input className={"w-[60vw]"} value={serv?.mods} onChange={(e) => setServ((p) => ({ ...p, mods: e.target.value }))}></Input>
                    </div>
                </div>
            </Card>
        </TabPanel>
    );
}