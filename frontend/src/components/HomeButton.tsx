import React, { useState } from "react";
import { IconButton } from "@mui/joy";
import { IconHome } from "@tabler/icons-react";
import { server } from "../../wailsjs/go/models";

type Props = {
  setServ: React.Dispatch<React.SetStateAction<number | undefined>>;
};

export const HomeButton = ({ setServ }: Props) => {
  return (
    <IconButton
      variant="soft"
      color="neutral"
      onClick={() => setServ(undefined)}
    >
      <IconHome></IconHome>
    </IconButton>
  );
};
