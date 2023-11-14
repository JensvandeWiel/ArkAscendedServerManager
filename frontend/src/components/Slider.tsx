import React from "react";
import {server} from "../../wailsjs/go/models";
import {Input, Slider as Sl} from "@mui/joy";
type Props = {
    value: number;
    onChange: (value: number) => void;
    className?: string;
    sliderMax?: number;
    sliderStep?: number;
    sliderMin?: number;

}

export function Slider({value, onChange, className, sliderMax, sliderStep, sliderMin}:Props) {
    return (
        <div className={"flex space-x-4" + className}>
            <Sl valueLabelDisplay="auto" className={""} max={sliderMax} step={sliderStep} min={sliderMin} value={value} onChange={(e, v) => {
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