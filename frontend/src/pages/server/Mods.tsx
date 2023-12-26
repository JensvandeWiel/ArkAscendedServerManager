import React from "react";
import { server } from "../../../wailsjs/go/models";
import {
  Card,
  Divider,
  FormLabel,
  Input,
  TabPanel,
  Typography,
} from "@mui/joy";

type Props = {
  setServ: React.Dispatch<React.SetStateAction<server.Server>>;
  serv: server.Server;
};

function GeneralServerSettingsCard({
  setServ,
  serv,
}: {
  setServ: React.Dispatch<React.SetStateAction<server.Server>>;
  serv: server.Server;
}) {
  return (
    <Card variant="soft" className={""}>
      <div className={"space-x-4 w-full flex"}>
        <div className={"inline-block"}>
          <FormLabel>Mods (e.g.: modid1,modid2,modid3)</FormLabel>
          <Input
            className={"w-[60vw]"}
            value={serv?.mods}
            onChange={(e) => setServ((p) => ({ ...p, mods: e.target.value }))}
          ></Input>
        </div>
      </div>
    </Card>
  );
}

export function Mods({ setServ, serv }: Props) {
  return (
    <TabPanel value={2} className={"space-y-8"}>
      <GeneralServerSettingsCard setServ={setServ} serv={serv} />
    </TabPanel>
  );
}
