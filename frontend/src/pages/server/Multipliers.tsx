import React from "react";
import {server} from "../../../wailsjs/go/models";
import {
    Card,
    Divider, FormLabel, Input,
    TabPanel, Tooltip,
    Typography
} from "@mui/joy";
import {Slider} from "../../components/Slider";

type Props = {
    setServ: React.Dispatch<React.SetStateAction<server.Server>>
    serv: server.Server;
}
//TODO Last value that was done is PlayerResistanceMultiplier, i'm adding them in order of https://ark.wiki.gg/wiki/Server_configuration

function GeneralServerSettingsCard({ setServ, serv }: {setServ: React.Dispatch<React.SetStateAction<server.Server>>, serv: server.Server}) {
    return (
        <Card variant="soft"  className={''}>
            <Typography level="title-md">
                General Multipliers
            </Typography>
            <Divider className={'mx-2'}/>

            <div className={'space-x-4 w-full'}>
                <div className={'ml-4'}>
                    <Tooltip title={"Allow increasing or decreasing global item stack size, this means all default stack sizes will be multiplied by the value given (excluding items that have a stack size of 1 by default)."}>
                        <FormLabel>Item Stack Size Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.itemStackSizeMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.itemStackSizeMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
            </div>
            <Typography level="title-md">
                Dino Multipliers
            </Typography>
            <Divider className={'mx-2'}/>
            <div className={'space-x-4'}>
                <div className={"ml-4"}>
                    <Tooltip title={"Specifies the scaling factor for dinosaurs' food consumption. Higher values increase food consumption (dinosaurs get hungry faster). It also affects the taming-times."}>
                        <FormLabel>Dino Character Food Drain Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.dinoCharacterFoodDrainMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.dinoCharacterFoodDrainMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip title={"Specifies the scaling factor for dinosaurs' health recovery. Higher values increase the recovery rate (dinosaurs heal faster).\t"}>
                        <FormLabel>Dino Character Health Recovery Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.dinoCharacterHealthRecoveryMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.dinoCharacterFoodDrainMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip title={"Specifies the scaling factor for dinosaurs' health recovery. Higher values increase the recovery rate (dinosaurs heal faster).\t"}>
                        <FormLabel>Dino Character Health Recovery Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.dinoCharacterHealthRecoveryMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.dinoCharacterHealthRecoveryMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip title={"Specifies the scaling factor for dinosaurs' stamina consumption. Higher values increase stamina consumption (dinosaurs get tired faster).\t"}>
                        <FormLabel>Dino Character Stamina Drain Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.dinoCharacterStaminaDrainMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.dinoCharacterStaminaDrainMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip title={"Specifies the scaling factor for dinosaur spawns. Higher values increase the number of dinosaurs spawned throughout the ARK."}>
                        <FormLabel>Dino Count Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.dinoCountMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.dinoCountMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip title={"Specifies the scaling factor for the resistance to damage wild dinosaurs receive when attacked. The default value 1 provides normal damage. Higher values decrease resistance, increasing damage per attack. Lower values increase it, reducing damage per attack. A value of 0.5 results in a creature taking half damage while a value of 2.0 would result in a creature taking double normal damage."}>
                        <FormLabel>Dino Damage Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.dinoDamageMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.dinoDamageMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
            </div>
            <Typography level="title-md">
                Harvesting Multipliers
            </Typography>
            <Divider className={'mx-2'}/>
            <div className={'space-x-4 w-full'}>
                <div className={'ml-4'}>
                    <Tooltip title={"Specifies the scaling factor for yields from all harvesting activities (chopping down trees, picking berries, carving carcasses, mining rocks, etc.). Higher values increase the amount of materials harvested with each strike."}>
                        <FormLabel>Harvest Amount Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.harvestAmountMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.harvestAmountMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div className={''}>
                    <Tooltip title={"Specifies the scaling factor for the \"health\" of items that can be harvested (trees, rocks, carcasses, etc.). Higher values increase the amount of damage (i.e., \"number of strikes\") such objects can withstand before being destroyed, which results in higher overall harvest yields."}>
                        <FormLabel>Harvest Health Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.harvestHealthMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.harvestHealthMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
            </div>
            <Typography level="title-md">
                Player Multipliers
            </Typography>
            <Divider className={'mx-2'}/>
            <div className={'space-x-4 w-full'}>
                <div className={'ml-4'}>
                    <Tooltip title={"Use this to set how swim speed is multiplied by level spent in oxygen."}>
                        <FormLabel>Oxygen Swim Speed Stat Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.oxygenSwimSpeedStatMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.oxygenSwimSpeedStatMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div className={'ml-4'}>
                    <Tooltip title={"Specifies the scaling factor for player characters' food consumption. Higher values increase food consumption (player characters get hungry faster).\t"}>
                        <FormLabel>Player Character Food Drain Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.playerCharacterFoodDrainMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.playerCharacterFoodDrainMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div className={'ml-4'}>
                    <Tooltip title={"Specifies the scaling factor for player characters' health recovery. Higher values increase the recovery rate (player characters heal faster).\t"}>
                        <FormLabel>Player Character Health Recovery Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.playerCharacterHealthRecoveryMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.playerCharacterHealthRecoveryMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div className={'ml-4'}>
                    <Tooltip title={"Specifies the scaling factor for player characters' stamina consumption. Higher values increase stamina consumption (player characters get tired faster).\t"}>
                        <FormLabel>Player Character Stamina Drain Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.playerCharacterStaminaDrainMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.playerCharacterStaminaDrainMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div className={'ml-4'}>
                    <Tooltip title={"Specifies the scaling factor for player characters' water consumption. Higher values increase water consumption (player characters get thirsty faster)."}>
                        <FormLabel>Player Character Water Drain Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.playerCharacterWaterDrainMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.playerCharacterWaterDrainMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div className={'ml-4'}>
                    <Tooltip title={"Specifies the scaling factor for the damage players deal with their attacks. The default value 1 provides normal damage. Higher values increase damage. Lower values decrease it."}>
                        <FormLabel>Player Damage Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.playerDamageMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.playerDamageMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div className={'ml-4'}>
                    <Tooltip title={"Specifies the scaling factor for the resistance to damage players receive when attacked. The default value 1 provides normal damage. Higher values decrease resistance, increasing damage per attack. Lower values increase it, reducing damage per attack. A value of 0.5 results in a player taking half damage while a value of 2.0 would result in taking double normal damage."}>
                        <FormLabel>Player Resistance Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.playerResistanceMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.playerResistanceMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>

            </div>
            <Typography level="title-md">
                Structure/building Multipliers
            </Typography>
            <Divider className={'mx-2'}/>
            <div className={'space-x-4 w-full'}>
                <div className={'ml-4'}>
                    <Tooltip title={"Higher value increases (from a percentage scale) max number of items place-able on saddles and rafts."}>
                        <FormLabel>Per Platform Max Structures Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.perPlatformMaxStructuresMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.perPlatformMaxStructuresMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div className={'ml-4'}>
                    <Tooltip title={"Increasing the number allows structures being placed further away from the platform."}>
                        <FormLabel>Platform Saddle Build Area Bounds Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.platformSaddleBuildAreaBoundsMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.platformSaddleBuildAreaBoundsMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
            </div>
        </Card>
    )
}

export function Multipliers({setServ, serv}: Props) {
    return (
        <TabPanel value={3} className={'space-y-8'}>
            <GeneralServerSettingsCard setServ={setServ} serv={serv}/>
        </TabPanel>
    );
}