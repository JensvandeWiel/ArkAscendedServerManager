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


function GeneralMultipliers({ setServ, serv }: {setServ: React.Dispatch<React.SetStateAction<server.Server>>, serv: server.Server}) {
    return (
        <div className={"space-y-4"}>
            <Typography level="title-md">
                General Multipliers
            </Typography>
            <Divider className={'mx-2'}/>
            <div className={'space-x-4 w-full'}>
                <div className={'ml-4'}>
                    <Tooltip enterDelay={500} title={"Allow increasing or decreasing global item stack size, this means all default stack sizes will be multiplied by the value given (excluding items that have a stack size of 1 by default)."}>
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
                <div className={''}>
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for the experience received by players, tribes and dinosaurs for various actions. The default value 1 provides the same amounts of experience as in the single player experience (and official public servers). Higher values increase XP amounts awarded for various actions; lower values decrease it."}>
                        <FormLabel>XP Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.xPMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.xPMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Specifies the map base temperature scaling factor: lower value makes the environment colder, higher value makes the environment hotter."}>
                        <FormLabel>Base Temperature Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.baseTemperatureMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.baseTemperatureMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the speed of crop decay in plots. A higher value decrease (by percentage) speed of crop decay in plots."}>
                        <FormLabel>Crop Decay Speed Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.cropDecaySpeedMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.cropDecaySpeedMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the speed of crop growth in plots. A higher value increases (by percentage) speed of crop growth."}>
                        <FormLabel>Crop Growth Speed Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.cropGrowthSpeedMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.cropGrowthSpeedMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the effectiveness of custom recipes. A higher value increases (by percentage) their effectiveness."}>
                        <FormLabel>Custom Recipe Effectiveness Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.customRecipeEffectivenessMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.customRecipeEffectivenessMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the effectiveness of custom recipes. A higher value increases (by percentage) their effectiveness."}>
                        <FormLabel>Custom Recipe Effectiveness Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.customRecipeEffectivenessMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.customRecipeEffectivenessMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the effect of the players crafting speed level that is used as a base for the formula in creating a custom recipe. A higher number increases (by percentage) the effect."}>
                        <FormLabel>Custom Recipe Skill Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.customRecipeSkillMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.customRecipeSkillMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Sets the quality of items that have a quality when fishing. Valid values are from 1.0 to 5.0."}>
                        <FormLabel>Fishing Loot Quality Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.fishingLootQualityMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.fishingLootQualityMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Defines the interval of fuel consumption."}>
                        <FormLabel>Fuel Consumption Interval Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.fuelConsumptionIntervalMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.fuelConsumptionIntervalMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the amount of XP earned for generic XP (automatic over time)."}>
                        <FormLabel>Generic XP Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.genericXPMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.genericXPMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the decomposition time of corpses, (player and dinosaur), globally. Higher values prolong the time."}>
                        <FormLabel>Global Corpse Decomposition Time Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.globalCorpseDecompositionTimeMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.globalCorpseDecompositionTimeMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the decomposition time of dropped items, loot bags etc. globally. Higher values prolong the time."}>
                        <FormLabel>Global Item Decomposition Time Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.globalItemDecompositionTimeMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.globalItemDecompositionTimeMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the spoiling time of perishables globally. Higher values prolong the time."}>
                        <FormLabel>Global Spoiling Time Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.globalSpoilingTimeMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.globalSpoilingTimeMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"If bIncreasePvPRespawnInterval is True, scales the PvP additional re-spawn time (IncreasePvPRespawnIntervalBaseAmount) when a player is killed by a team within a certain amount of time (IncreasePvPRespawnIntervalCheckPeriod)."}>
                        <FormLabel>Increase PvP Respawn Interval Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.increasePvPRespawnIntervalMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.increasePvPRespawnIntervalMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scale the amount of XP earned for a kill."}>
                        <FormLabel>Kill XP Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.killXPMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.killXPMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Defines the falling speed multiplier at which players starts taking fall damage. The falling speed is based on the time players spent in the air while having a negated Z axis velocity meaning that the higher this setting is, the longer players can fall without taking fall damage. For example, having it set to 0.1 means players will no longer survive a regular jump while having it set very high such as to 100.0 means players will survive a fall from the sky limit, etc. This setting doesn't affect the gravity scale of the players so there won't be any physics difference to the character movements."}>
                        <FormLabel>Max Fall Speed Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.maxFallSpeedMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.maxFallSpeedMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scale the amount of XP earned for SpecialEvent."}>
                        <FormLabel>Special XP Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.specialXPMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.specialXPMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Increases the quality of items that have a quality in the supply crates. Valid values are from 1.0 to 5.0. The quality also depends on the Difficulty Offset."}>
                        <FormLabel>Supply Crate Loot Quality Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.supplyCrateLootQualityMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.supplyCrateLootQualityMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Modifies corpse and dropped box lifespan."}>
                        <FormLabel>Use Corpse Life Span Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.useCorpseLifeSpanMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.useCorpseLifeSpanMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                {/*<div className={'ml-4'}>
                    <Tooltip enterDelay={500} title={"Higher value increases (from a percentage scale) max number of items place-able on saddles and rafts."}>
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
                </div>*/}
                <div className={'ml-4'}>
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for structures decay times, e.g.: setting it at 2.0 will double all structure decay times, while setting at 0.5 will halve the timers. Note: despite the name, works in both PvP and PvE modes when structure decay is enabled."}>
                        <FormLabel>PvE Structure Decay Period Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.pveStructureDecayPeriodMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.pveStructureDecayPeriodMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
            </div>
        </div>
    )
}

function DinoMultipliers({ setServ, serv }: {setServ: React.Dispatch<React.SetStateAction<server.Server>>, serv: server.Server}) {
    return (
        <div className={"space-y-4"}>
            <Typography level="title-md">
                Dino Multipliers
            </Typography>
            <Divider className={'mx-2'}/>
            <div className={'space-x-4'}>
                <div className={"ml-4"}>
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for dinosaurs' food consumption. Higher values increase food consumption (dinosaurs get hungry faster). It also affects the taming-times."}>
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
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for dinosaurs' health recovery. Higher values increase the recovery rate (dinosaurs heal faster).\t"}>
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
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for dinosaurs' health recovery. Higher values increase the recovery rate (dinosaurs heal faster).\t"}>
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
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for dinosaurs' stamina consumption. Higher values increase stamina consumption (dinosaurs get tired faster).\t"}>
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
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for dinosaur spawns. Higher values increase the number of dinosaurs spawned throughout the ARK."}>
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
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for the resistance to damage wild dinosaurs receive when attacked. The default value 1 provides normal damage. Higher values decrease resistance, increasing damage per attack. Lower values increase it, reducing damage per attack. A value of 0.5 results in a creature taking half damage while a value of 2.0 would result in a creature taking double normal damage."}>
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
                <div>
                    <Tooltip enterDelay={500} title={"Affects how quickly the food drains on such \"Raid Dinos\" (e.g.: Titanosaurus)\t"}>
                        <FormLabel>Raid Dino Character Food Drain Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.raidDinoCharacterFoodDrainMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.raidDinoCharacterFoodDrainMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for the damage tamed creatures deal with their attacks. The default value 1 provides normal damage. Higher values increase damage. Lower values decrease it."}>
                        <FormLabel>Tamed Dino Damage Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.tamedDinoDamageMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.tamedDinoDamageMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for the resistance to damage tamed creatures receive when attacked. The default value 1 provides normal damage. Higher values decrease resistance, increasing damage per attack. Lower values increase it, reducing damage per attack. A value of 0.5 results in a structure taking half damage while a value of 2.0 would result in a structure taking double normal damage."}>
                        <FormLabel>Tamed Dino Resistance Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.tamedDinoResistanceMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.tamedDinoResistanceMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for dinosaur taming speed. Higher values make taming faster.\t"}>
                        <FormLabel>Taming Speed Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.tamingSpeedMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.tamingSpeedMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales how long after delaying cuddling with the Baby before Imprinting Quality starts to decrease."}>
                        <FormLabel>Baby Cuddle Grace Period Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.babyCuddleGracePeriodMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.babyCuddleGracePeriodMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales how often babies needs attention for imprinting. More often means you'll need to cuddle with them more frequently to gain Imprinting Quality. Scales according to BabyMatureSpeedMultiplier: if both set at 1.0 the imprint request is every 8 hours. "}>
                        <FormLabel>Baby Cuddle Interval Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.babyCuddleIntervalMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.babyCuddleIntervalMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales how fast Imprinting Quality decreases after the grace period if you haven't yet cuddled with the Baby."}>
                        <FormLabel>Baby Cuddle Lose Imprint Quality Speed Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.babyCuddleLoseImprintQualitySpeedMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.babyCuddleLoseImprintQualitySpeedMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the speed that baby dinos eat their food. A lower value decreases (by percentage) the food eaten by babies.\n"}>
                        <FormLabel>Baby Food Consumption Speed Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.babyFoodConsumptionSpeedMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.babyFoodConsumptionSpeedMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the percentage each imprint provides. For example, if an imprint usually give 10%, setting this multiplier to 0.5 means they would now give 5% each. While setting it to 2.0 means they would now give 20% each, etc. This multiplier is global, meaning it will affect the imprinting progression of every species."}>
                        <FormLabel>Baby Imprint Amount Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.babyImprintAmountMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.babyImprintAmountMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales how much of an effect on stats the Imprinting Quality has. Set it to 0 to effectively disable the system."}>
                        <FormLabel>Baby Imprinting Stat Scale Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.babyImprintingStatScaleMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.babyImprintingStatScaleMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the maturation speed of babies. A higher number decreases (by percentage) time needed for baby dino to mature. See Times for Breeding tables for values at 1.0"}>
                        <FormLabel>Baby Mature Speed Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.babyMatureSpeedMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.babyMatureSpeedMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the damage done to a harvestable item/entity by a Dino. A higher number increases (by percentage) the speed of harvesting."}>
                        <FormLabel>Dino Harvesting Damage Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.dinoHarvestingDamageMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.dinoHarvestingDamageMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the damage done by Turrets towards a Dino. A higher values increases it (by percentage)."}>
                        <FormLabel>Dino Turret Damage Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.dinoTurretDamageMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.dinoTurretDamageMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the time needed for a fertilised egg to hatch. A higher value decreases (by percentage) that time."}>
                        <FormLabel>Egg Hatch Speed Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.eggHatchSpeedMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.eggHatchSpeedMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the time between eggs are spawning / being laid. Higher number increases it (by percentage)."}>
                        <FormLabel>Lay Egg Interval Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.layEggIntervalMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.layEggIntervalMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the interval between dinos can mate. A lower value decreases it (on a percentage scale). Example: a value of 0.5 would allow dinosaurs to mate 50% sooner."}>
                        <FormLabel>Mating Interval Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.matingIntervalMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.matingIntervalMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the speed at which dinos mate with each other. A higher value increases it (by percentage). Example: MatingSpeedMultiplier=2.0 would cause dinosaurs to complete mating in half the normal time."}>
                        <FormLabel>Mating Speed Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.matingSpeedMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.matingSpeedMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales how often a survivor get tame requests for passive tame dinos."}>
                        <FormLabel>Passive Tame Interval Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.passiveTameIntervalMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.passiveTameIntervalMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales how fast tame dinos consume food."}>
                        <FormLabel>Tamed Dino Character Food Drain Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.tamedDinoCharacterFoodDrainMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.tamedDinoCharacterFoodDrainMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales how fast tamed dinos lose torpor."}>
                        <FormLabel>Tamed Dino Torpor Drain Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.tamedDinoTorporDrainMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.tamedDinoTorporDrainMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales how fast wild dinos consume food."}>
                        <FormLabel>Wild Dino Character Food Drain Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.wildDinoCharacterFoodDrainMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.wildDinoCharacterFoodDrainMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales how fast wild dinos lose torpor."}>
                        <FormLabel>Wild Dino Torpor Drain Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.wildDinoTorporDrainMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.wildDinoTorporDrainMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
            </div>
        </div>
    )
}

function HarvestingMultipliers({ setServ, serv }: {setServ: React.Dispatch<React.SetStateAction<server.Server>>, serv: server.Server}) {
    return (
        <div className={"space-y-4"}>
            <Typography level="title-md">
                Harvesting Multipliers
            </Typography>
            <Divider className={'mx-2'}/>
            <div className={'space-x-4 w-full'}>
                <div className={'ml-4'}>
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for yields from all harvesting activities (chopping down trees, picking berries, carving carcasses, mining rocks, etc.). Higher values increase the amount of materials harvested with each strike."}>
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
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for the \"health\" of items that can be harvested (trees, rocks, carcasses, etc.). Higher values increase the amount of damage (i.e., \"number of strikes\") such objects can withstand before being destroyed, which results in higher overall harvest yields."}>
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
                <div className={''}>
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for the re-spawn rate for resource nodes (trees, rocks, bushes, etc.). Lower values cause nodes to re-spawn more frequently.\t"}>
                        <FormLabel>Resources Respawn Period Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.resourcesRespawnPeriodMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.resourcesRespawnPeriodMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the amount of XP earned for harvesting."}>
                        <FormLabel>Harvest XP Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.harvestXPMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.harvestXPMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
            </div>
        </div>
    )
}

function PlayerMultipliers({ setServ, serv }: {setServ: React.Dispatch<React.SetStateAction<server.Server>>, serv: server.Server}) {
    return (
        <div className={"space-y-4"}>
            <Typography level="title-md">
                Player Multipliers
            </Typography>
            <Divider className={'mx-2'}/>
            <div className={'space-x-4 w-full'}>
                <div className={'ml-4'}>
                    <Tooltip enterDelay={500} title={"Use this to set how swim speed is multiplied by level spent in oxygen."}>
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
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for player characters' food consumption. Higher values increase food consumption (player characters get hungry faster).\t"}>
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
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for player characters' health recovery. Higher values increase the recovery rate (player characters heal faster).\t"}>
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
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for player characters' stamina consumption. Higher values increase stamina consumption (player characters get tired faster).\t"}>
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
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for player characters' water consumption. Higher values increase water consumption (player characters get thirsty faster)."}>
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
                <div >
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for the damage players deal with their attacks. The default value 1 provides normal damage. Higher values increase damage. Lower values decrease it."}>
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
                <div >
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for the resistance to damage players receive when attacked. The default value 1 provides normal damage. Higher values decrease resistance, increasing damage per attack. Lower values increase it, reducing damage per attack. A value of 0.5 results in a player taking half damage while a value of 2.0 would result in taking double normal damage."}>
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
                <div>
                    <Tooltip enterDelay={500} title={"Scales the bonus received from upgrading the Crafting Skill."}>
                        <FormLabel>Crafting Skill Bonus Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.craftingSkillBonusMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.craftingSkillBonusMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the amount of XP earned for crafting."}>
                        <FormLabel>Craft XP Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.craftXPMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.craftXPMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the hair growth. Higher values increase speed of growth."}>
                        <FormLabel>Hair Growth Speed Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.hairGrowthSpeedMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.hairGrowthSpeedMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales the damage done to a harvestable item/entity by a Player. A higher value increases it (by percentage): the higher number, the faster the survivors collects."}>
                        <FormLabel>Player Harvesting Damage Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.playerHarvestingDamageMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.playerHarvestingDamageMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Scales how frequently survivors can poop. Higher value decreases it (by percentage)"}>
                        <FormLabel>Poop Interval Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.poopIntervalMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.poopIntervalMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>

            </div>
        </div>
    )
}

function StructureBuildingMultipliers({ setServ, serv }: {setServ: React.Dispatch<React.SetStateAction<server.Server>>, serv: server.Server}) {
    return (
        <div className={"space-y-4"}>
            <Typography level="title-md">
                Structure/building Multipliers
            </Typography>
            <Divider className={'mx-2'}/>
            <div className={'space-x-4 w-full'}>
                <div className={'ml-4'}>
                    <Tooltip enterDelay={500} title={"Higher value increases (from a percentage scale) max number of items place-able on saddles and rafts."}>
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
                    <Tooltip enterDelay={500} title={"Increasing the number allows structures being placed further away from the platform."}>
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
                <div className={''}>
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for the damage structures deal with their attacks (i.e., spiked walls). Higher values increase damage. Lower values decrease it.\t"}>
                        <FormLabel>Structure Damage Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.structureDamageMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.structureDamageMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
                <div className={''}>
                    <Tooltip enterDelay={500} title={"Same as ResourceNoReplenishRadiusStructures in Game.ini. If both settings are set both multiplier will be applied. Can be useful when cannot change the Game.ini file as it works as a command line option too."}>
                        <FormLabel>Structure Prevent Resource Radius Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.gameUserSettings.serverSettings.structurePreventResourceRadiusMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.gameUserSettings.serverSettings.structurePreventResourceRadiusMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                    <div className={''}>
                        <Tooltip enterDelay={500} title={"Specifies the scaling factor for the resistance to damage structures receive when attacked. The default value 1 provides normal damage. Higher values decrease resistance, increasing damage per attack. Lower values increase it, reducing damage per attack. A value of 0.5 results in a structure taking half damage while a value of 2.0 would result in a structure taking double normal damage."}>
                            <FormLabel>Structure Resistance Multiplier</FormLabel>
                        </Tooltip>
                        <Slider
                            sliderStep={.1}
                            sliderMax={25}
                            value={serv?.gameUserSettings.serverSettings.structureResistanceMultiplier}
                            onChange={(v) => {
                                if (v >= 0) {
                                    setServ((p) => {
                                        const newState = {...p, convertValues: p.convertValues};
                                        newState.gameUserSettings.serverSettings.structureResistanceMultiplier = v;
                                        return newState;
                                    })
                                }
                            }}
                        />
                    </div>
                </div>
                <div>
                    <Tooltip enterDelay={500} title={"Specifies the scaling factor for damage structures take within caves. The lower the value, the less damage the structure takes (i.e. setting to 1.0 will make structure built in or near a cave receive the same amount of damage as those built on the surface)."}>
                        <FormLabel>PvP Zone Structure Damage Multiplier</FormLabel>
                    </Tooltip>
                    <Slider
                        sliderStep={.1}
                        sliderMax={25}
                        value={serv?.game.ScriptShootergameShootergamemode.pvPZoneStructureDamageMultiplier}
                        onChange={(v) => {
                            if (v >= 0) {
                                setServ((p) => {
                                    const newState = {...p, convertValues: p.convertValues};
                                    newState.game.ScriptShootergameShootergamemode.pvPZoneStructureDamageMultiplier = v;
                                    return newState;
                                })
                            }
                        }}
                    />
                </div>
            </div>
        </div>
    )
}

function AllModifiersCard({ setServ, serv }: {setServ: React.Dispatch<React.SetStateAction<server.Server>>, serv: server.Server}) {
    return (
        <Card variant="soft"  className={''}>
            <GeneralMultipliers setServ={setServ} serv={serv}/>
            <DinoMultipliers setServ={setServ} serv={serv}/>
            <HarvestingMultipliers setServ={setServ} serv={serv}/>
            <PlayerMultipliers setServ={setServ} serv={serv}/>
            <StructureBuildingMultipliers setServ={setServ} serv={serv}/>

        </Card>
    )
}

export function Multipliers({setServ, serv}: Props) {
    return (
        <TabPanel value={3} className={'space-y-8'}>
            <AllModifiersCard setServ={setServ} serv={serv}/>
        </TabPanel>
    );
}