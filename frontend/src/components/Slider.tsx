import React from "react";
import {server} from "../../wailsjs/go/models";
import {Input, Slider as Sl} from "@mui/joy";
type Props = {
    value: number;
    onChange: (value: number) => void;

}

export function Slider({value, onChange}:Props) {
    return (
        <div className={"flex space-x-4"}>
            <Sl valueLabelDisplay="auto" className={"w-1/3"} max={240} step={1} value={value} onChange={(e, v) => {
                let val = v as number
                onChange(val);
            }} ></Sl>
            <Input type={"number"} required className={"w-32"} value={value} onChange={(e) => {
                let val = parseInt(e.target.value);
                onChange(val);
            }}></Input>
        </div>
    );
}