import {
  Button,
  Card,
  Textarea,
  FormLabel,
  TabPanel,
  Tooltip,
  Input,
  Typography,
  Accordion,
  AccordionDetails,
  AccordionGroup,
  AccordionSummary,
} from "@mui/joy";
import { server } from "../../../wailsjs/go/models";
import { Slider } from "../../components/Slider";
import React from "react";

import { useAlert } from "../../components/AlertProvider";
import { OpenFileDialog } from "../../../wailsjs/go/helpers/HelpersController";
type Props = {
  setServ: React.Dispatch<React.SetStateAction<server.Server>>;
  serv: server.Server;
};

export function Ini({ setServ, serv }: Props) {
  return (
    <TabPanel value={3} className={"space-y-8"}>
      Not implemented yet, use manual configuration trough ini files for now.
    </TabPanel>
  );
}
