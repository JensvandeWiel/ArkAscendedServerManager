package server

import (
	"os"
	"path/filepath"

	"github.com/go-ini/ini"
	"github.com/wailsapp/wails/v2/pkg/runtime"
)

type ScriptShootergameShootergamemode struct {
	AutoPvEStartTimeSeconds                     float32 `json:"autoPvEStartTimeSeconds" ini:"AutoPvEStartTimeSeconds"`
	AutoPvEStopTimeSeconds                      float32 `json:"autoPvEStopTimeSeconds" ini:"AutoPvEStopTimeSeconds"`
	BabyCuddleGracePeriodMultiplier             float32 `json:"babyCuddleGracePeriodMultiplier" ini:"BabyCuddleGracePeriodMultiplier"`
	BabyCuddleIntervalMultiplier                float32 `json:"babyCuddleIntervalMultiplier" ini:"BabyCuddleIntervalMultiplier"`
	BabyCuddleLoseImprintQualitySpeedMultiplier float32 `json:"babyCuddleLoseImprintQualitySpeedMultiplier" ini:"BabyCuddleLoseImprintQualitySpeedMultiplier"`
	BabyFoodConsumptionSpeedMultiplier          float32 `json:"babyFoodConsumptionSpeedMultiplier" ini:"BabyFoodConsumptionSpeedMultiplier"`
	BabyImprintAmountMultiplier                 float32 `json:"babyImprintAmountMultiplier" ini:"BabyImprintAmountMultiplier"`
	BabyImprintingStatScaleMultiplier           float32 `json:"babyImprintingStatScaleMultiplier" ini:"BabyImplrintingStatScaleMultiplier"`
	BabyMatureSpeedMultiplier                   float32 `json:"babyMatureSpeedMultiplier" ini:"BabyMatureSpeedMultiplier"`
	BAllowUnclaimDinos                          bool    `json:"bAllowUnclaimDinos" ini:"bAllowUnclaimDinos"`
	BAllowCustomRecipes                         bool    `json:"bAllowCustomRecipes" ini:"bAllowCustomRecipes"`
	BAllowFlyerSpeedLeveling                    bool    `json:"bAllowFlyerSpeedLeveling" ini:"bAllowFlyerSpeedLeveling"`
	BAllowPlatformSaddleMultiFloors             bool    `json:"bAllowPlatformSaddleMultiFloors" ini:"bAllowPlatformSaddleMultiFloors"`
	BAllowUnlimitedRespecs                      bool    `json:"bAllowUnlimitedRespecs" ini:"bAllowUnlimitedRespecs"`
	BaseTemperatureMultiplier                   float32 `json:"baseTemperatureMultiplier" ini:"BaseTemperatureMultiplier"`
	BAutoPvETimer                               bool    `json:"bAutoPvETimer" ini:"bAutoPvETimer"`
	BAutoPvEUseSystemTime                       bool    `json:"bAutoPvEUseSystemTime" ini:"bAutoPvEUseSystemTime"`
	BAutoUnlockAllEngrams                       bool    `json:"bAutoUnlockAllEngrams" ini:"bAutoUnlockAllEngrams"`
	BDisableDinoBreeding                        bool    `json:"bDisableDinoBreeding" ini:"bDisableDinoBreeding"`
	BDisableDinoRiding                          bool    `json:"bDisableDinoRiding" ini:"BDisableDinoRiding"`
	BDisableDinoTaming                          bool    `json:"bDisableDinoTaming" ini:"BDisableDinoTaming"`
	BDisableFriendlyFire                        bool    `json:"bDisableFriendlyFire" ini:"bDisableFriendlyFire"`
	BDisableLootCrates                          bool    `json:"bDisableLootCrates" ini:"bDisableLootCrates"`
	BDisableStructurePlacementCollision         bool    `json:"bDisableStructurePlacementCollision" ini:"bDisableStructurePlacementCollision"`
	BFlyerPlatformAllowUnalignedDinoBasing      bool    `json:"bFlyerPlatformAllowUnalignedDinoBasing" ini:"BFlyerPlatformAllowUnalignedDinoBasing"`
	BIgnoreStructuresPreventionVolumes          bool    `json:"bIgnoreStructuresPreventionVolumes" ini:"BIgnoreStructuresPreventionVolumes"`
	BIncreasePvPRespawnInterval                 bool    `json:"bIncreasePvPRespawnInterval" ini:"BIncreasePvPRespawnInterval"`
	BOnlyAllowSpecifiedEngrams                  bool    `json:"bOnlyAllowSpecifiedEngrams" ini:"BOnlyAllowSpecifiedEngrams"`
	BPassiveDefensesDamageRiderlessDinos        bool    `json:"bPassiveDefensesDamageRiderlessDinos" ini:"BPassiveDefensesDamageRiderlessDinos"`
	BPvEAllowTribeWar                           bool    `json:"bPvEAllowTribeWar" ini:"BPvEAllowTribeWar"`
	BPvEAllowTribeWarCancel                     bool    `json:"bPvEAllowTribeWarCancel" ini:"BPvEAllowTribeWarCancel"`
	BPvEDisableFriendlyFire                     bool    `json:"bPvEDisableFriendlyFire" ini:"BPvEDisableFriendlyFire"`
	BShowCreativeMode                           bool    `json:"bShowCreativeMode" ini:"BShowCreativeMode"`
	BUseCorpseLocator                           bool    `json:"bUseCorpseLocator" ini:"BUseCorpseLocator"`
	BUseDinoLevelUpAnimations                   bool    `json:"bUseDinoLevelUpAnimations" ini:"BUseDinoLevelUpAnimations"`
	BUseSingleplayerSettings                    bool    `json:"bUseSingleplayerSettings" ini:"BUseSingleplayerSettings"`
	BUseTameLimitForStructuresOnly              bool    `json:"bUseTameLimitForStructuresOnly" ini:"BUseTameLimitForStructuresOnly"`
	//ConfigAddNPCSpawnEntriesContainer https://ark.wiki.gg/wiki/Server_configuration#ConfigAddNPCSpawnEntriesContainer
	//ConfigOverrideItemCraftingCosts https://ark.wiki.gg/wiki/Server_configuration#ConfigOverrideItemCraftingCosts
	//ConfigOverrideItemMaxQuantity https://ark.wiki.gg/wiki/Server_configuration#ConfigOverrideItemMaxQuantity
	//ConfigOverrideNPCSpawnEntriesContainer https://ark.wiki.gg/wiki/Server_configuration#ConfigOverrideNPCSpawnEntriesContainer
	//ConfigOverrideSupplyCrateItems https://ark.wiki.gg/wiki/Server_configuration#ConfigOverrideSupplyCrateItems
	//ConfigSubtractNPCSpawnEntriesContainer https://ark.wiki.gg/wiki/Server_configuration#ConfigSubtractNPCSpawnEntriesContainer
	CraftingSkillBonusMultiplier        float32 `json:"craftingSkillBonusMultiplier" ini:"CraftingSkillBonusMultiplier"`
	CraftXPMultiplier                   float32 `json:"craftXPMultiplier" ini:"CraftXPMultiplier"`
	CropDecaySpeedMultiplier            float32 `json:"cropDecaySpeedMultiplier" ini:"CropDecaySpeedMultiplier"`
	CropGrowthSpeedMultiplier           float32 `json:"cropGrowthSpeedMultiplier" ini:"CropGrowthSpeedMultiplier"`
	CustomRecipeEffectivenessMultiplier float32 `json:"customRecipeEffectivenessMultiplier" ini:"CustomRecipeEffectivenessMultiplier"`
	CustomRecipeSkillMultiplier         float32 `json:"customRecipeSkillMultiplier" ini:"CustomRecipeSkillMultiplier"`
	DestroyTamesOverLevelClamp          int     `json:"destroyTamesOverLevelClamp" ini:"DestroyTamesOverLevelClamp"`
	//DinoClassDamageMultipliers https://ark.wiki.gg/wiki/Server_configuration#DinoClassDamageMultipliers
	//DinoClassResistanceMultipliers https://ark.wiki.gg/wiki/Server_configuration#DinoClassResistanceMultipliers
	DinoHarvestingDamageMultiplier float64 `json:"dinoHarvestingDamageMultiplier" ini:"DinoHarvestingDamageMultiplier"`
	//DinoSpawnWeightMultipliers https://ark.wiki.gg/wiki/Server_configuration#DinoSpawnWeightMultipliers
	DinoTurretDamageMultiplier float32 `json:"dinoTurretDamageMultiplier" ini:"DinoTurretDamageMultiplier"`
	EggHatchSpeedMultiplier    float32 `json:"eggHatchSpeedMultiplier" ini:"EggHatchSpeedMultiplier"`
	//EngramEntryAutoUnlocks https://ark.wiki.gg/wiki/Server_configuration#EngramEntryAutoUnlocks
	//ExcludeItemIndices https://ark.wiki.gg/wiki/Item_IDs
	FastDecayInterval                               int     `json:"fastDecayInterval" ini:"FastDecayInterval"`
	FishingLootQualityMultiplier                    float32 `json:"fishingLootQualityMultiplier" ini:"FishingLootQualityMultiplier"`
	FuelConsumptionIntervalMultiplier               float32 `json:"fuelConsumptionIntervalMultiplier" ini:"FuelConsumptionIntervalMultiplier"`
	GenericXPMultiplier                             float32 `json:"genericXPMultiplier" ini:"GenericXPMultiplier"`
	GlobalCorpseDecompositionTimeMultiplier         float32 `json:"globalCorpseDecompositionTimeMultiplier" ini:"GlobalCorpseDecompositionTimeMultiplier"`
	GlobalItemDecompositionTimeMultiplier           float32 `json:"globalItemDecompositionTimeMultiplier" ini:"GlobalItemDecompositionTimeMultiplier"`
	GlobalPoweredBatteryDurabilityDecreasePerSecond float32 `json:"globalPoweredBatteryDurabilityDecreasePerSecond" ini:"GlobalPoweredBatteryDurabilityDecreasePerSecond"`
	GlobalSpoilingTimeMultiplier                    float32 `json:"globalSpoilingTimeMultiplier" ini:"GlobalSpoilingTimeMultiplier"`
	HairGrowthSpeedMultiplier                       float32 `json:"hairGrowthSpeedMultiplier" ini:"HairGrowthSpeedMultiplier"`
	//HarvestResourceItemAmountClassMultipliers https://ark.wiki.gg/wiki/Server_configuration#HarvestResourceItemAmountClassMultipliers
	HarvestXPMultiplier                   float32 `json:"harvestXPMultiplier" ini:"HarvestXPMultiplier"`
	IncreasePvPRespawnIntervalBaseAmount  float32 `json:"increasePvPRespawnIntervalBaseAmount" ini:"IncreasePvPRespawnIntervalBaseAmount"`
	IncreasePvPRespawnIntervalCheckPeriod float32 `json:"increasePvPRespawnIntervalCheckPeriod" ini:"IncreasePvPRespawnIntervalCheckPeriod"`
	IncreasePvPRespawnIntervalMultiplier  float32 `json:"increasePvPRespawnIntervalMultiplier" ini:"IncreasePvPRespawnIntervalMultiplier"`
	//ItemStatClamps[<attribute>] https://ark.wiki.gg/wiki/Server_configuration#ItemStatClamps
	KillXPMultiplier         float32 `json:"killXPMultiplier" ini:"KillXPMultiplier"`
	LayEggIntervalMultiplier float32 `json:"layEggIntervalMultiplier" ini:"LayEggIntervalMultiplier"`
	// LevelExperienceRampOverrides https://ark.wiki.gg/wiki/Server_configuration#Players_and_dinos_levels_override
	LimitNonPlayerDroppedItemsCount int     `json:"limitNonPlayerDroppedItemsCount" ini:"LimitNonPlayerDroppedItemsCount"`
	LimitNonPlayerDroppedItemsRange int     `json:"limitNonPlayerDroppedItemsRange" ini:"LimitNonPlayerDroppedItemsRange"`
	MatingIntervalMultiplier        float32 `json:"matingIntervalMultiplier" ini:"MatingIntervalMultiplier"`
	MatingSpeedMultiplier           float32 `json:"matingSpeedMultiplier" ini:"MatingSpeedMultiplier"`
	MaxAlliancesPerTribe            int
	MaxFallSpeedMultiplier          float32 `json:"maxFallSpeedMultiplier" ini:"MaxFallSpeedMultiplier"`
	MaxNumberOfPlayersInTribe       int     `json:"maxNumberOfPlayersInTribe" ini:"MaxNumberOfPlayersInTribe"`
	MaxTribeLogs                    int     `json:"maxTribeLogs" ini:"MaxTribeLogs"`
	//MaxTribesPerAlliance            int   `json:"maxTribesPerAlliance" ini:"MaxTribesPerAlliance"` // TODO: Add this in save function
	//NPCReplacements https://ark.wiki.gg/wiki/Server_configuration#NPCReplacements // TODO: Add this in save function
	//OverrideMaxExperiencePointsDino   int `json:"overrideMaxExperiencePointsDino" ini:"OverrideMaxExperiencePointsDino"`// TODO: Add this in save function
	//OverrideMaxExperiencePointsPlayer int `json:"overrideMaxExperiencePointsPlayer" ini:"OverrideMaxExperiencePointsPlayer"`// TODO: Add this in save function
	//OverrideEngramEntries https://ark.wiki.gg/wiki/Server_configuration#OverrideEngramEntries_and_OverrideNamedEngramEntries // TODO: Add this in save function
	//OverrideNamedEngramEntries https://ark.wiki.gg/wiki/Server_configuration#OverrideEngramEntries_and_OverrideNamedEngramEntries // TODO: Add this in save function
	//OverridePlayerLevelEngramPoints
	PassiveTameIntervalMultiplier float32 `json:"passiveTameIntervalMultiplier" ini:"PassiveTameIntervalMultiplier"`
	//PerLevelStatsMultiplier_Player[<integer>] https://ark.wiki.gg/wiki/Server_configuration#PerLevelStatsMultiplier
	//PerLevelStatsMultiplier_DinoTamed<_type>[<integer>] https://ark.wiki.gg/wiki/Server_configuration#PerLevelStatsMultiplier
	//PerLevelStatsMultiplier_DinoWild[<integer>] https://ark.wiki.gg/wiki/Server_configuration#PerLevelStatsMultiplier
	//PlayerBaseStatMultipliers[<attribute>] https://ark.wiki.gg/wiki/Server_configuration#PlayerBaseStatMultipliers
	PlayerHarvestingDamageMultiplier float32 `json:"playerHarvestingDamageMultiplier" ini:"PlayerHarvestingDamageMultiplier"`
	PoopIntervalMultiplier           float32 `json:"poopIntervalMultiplier" ini:"PoopIntervalMultiplier"`
	//PreventBreedingForClassNames https://ark.wiki.gg/wiki/Creature_IDs
	//PreventDinoTameClassNames https://ark.wiki.gg/wiki/Creature_IDs
	PreventOfflinePvPConnectionInvincibleInterval float32 `json:"preventOfflinePvPConnectionInvincibleInterval" ini:"PreventOfflinePvPConnectionInvincibleInterval"`
	//PreventTransferForClassNames
	PvPZoneStructureDamageMultiplier      float32 `json:"pvPZoneStructureDamageMultiplier" ini:"PvPZoneStructureDamageMultiplier"`
	ResourceNoReplenishRadiusPlayers      float32 `json:"resourceNoReplenishRadiusPlayers" ini:"ResourceNoReplenishRadiusPlayers"`
	ResourceNoReplenishRadiusStructures   float32 `json:"resourceNoReplenishRadiusStructures" ini:"ResourceNoReplenishRadiusStructures"`
	SpecialXPMultiplier                   float32 `json:"specialXPMultiplier" ini:"SpecialXPMultiplier"`
	StructureDamageRepairCooldown         int     `json:"structureDamageRepairCooldown" ini:"StructureDamageRepairCooldown"`
	SupplyCrateLootQualityMultiplier      float32 `json:"supplyCrateLootQualityMultiplier" ini:"SupplyCrate"`
	TamedDinoCharacterFoodDrainMultiplier float32 `json:"tamedDinoCharacterFoodDrainMultiplier" ini:"TamedDinoCharacterFoodDrainMultiplier"`
	//TamedDinoClassDamageMultipliers https://ark.wiki.gg/wiki/Server_configuration#DinoClassDamageMultipliers
	//TamedDinoClassResistanceMultipliers https://ark.wiki.gg/wiki/Server_configuration#DinoClassResistanceMultipliers
	TamedDinoTorporDrainMultiplier       float32 `json:"tamedDinoTorporDrainMultiplier" ini:"TamedDinoTorporDrainMultiplier"`
	TribeSlotReuseCooldown               float32 `json:"tribeSlotReuseCooldown" ini:"TribeSlotReuseCooldown"`
	UseCorpseLifeSpanMultiplier          float32 `json:"useCorpseLifeSpanMultiplier" ini:"UseCorpseLifeSpanMultiplier"`
	WildDinoCharacterFoodDrainMultiplier float32 `json:"wildDinoCharacterFoodDrainMultiplier" ini:"WildDinoCharacterFoodDrainMultiplier"`
	WildDinoTorporDrainMultiplier        float32 `json:"wildDinoTorporDrainMultiplier" ini:"WildDinoTorporDrainMultiplier"`
	BHardLimitTurretsInRange             bool    `json:"bHardLimitTurretsInRange" ini:"bHardLimitTurretsInRange"`
	BLimitTurretsInRange                 bool    `json:"bLimitTurretsInRange" ini:"bLimitTurretsInRange"`
	LimitTurretsNum                      int     `json:"limitTurretsNum" ini:"LimitTurretsNum"`
	LimitTurretsRange                    float32 `json:"limitTurretsRange" ini:"LimitTurretsRange"`

	AdjustableMutagenSpawnDelayMultiplier  float32 `json:"adjustableMutagenSpawnDelayMultiplier" ini:"AdjustableMutagenSpawnDelayMultiplier"`
	BaseHexagonRewardMultiplier            float32 `json:"baseHexagonRewardMultiplier" ini:"BaseHexagonRewardMultiplier"`
	BDisableHexagonStore                   bool    `json:"bDisableHexagonStore" ini:"bDisableHexagonStore"`
	BDisableDefaultMapItemSets             bool    `json:"bDisableDefaultMapItemSets" ini:"bDisableDefaultMapItemSets"`
	BDisableGenesisMissions                bool    `json:"bDisableGenesisMissions" ini:"bDisableGenesisMissions"`
	BDisableWorldBuffs                     bool    `json:"bDisableWorldBuffs" ini:"bDisableWorldBuffs"`
	BEnableWorldBuffScaling                bool    `json:"bEnableWorldBuffScaling" ini:"bEnableWorldBuffScaling"`
	BGenesisUseStructuresPreventionVolumes bool    `json:"bGenesisUseStructuresPreventionVolumes" ini:"bGenesisUseStructuresPreventionVolumes"`
	BHexStoreAllowOnlyEngramTradeOption    bool    `json:"bHexStoreAllowOnlyEngramTradeOption" ini:"bHexStoreAllowOnlyEngramTradeOption"`
	HexagonCostMultiplier                  float32 `json:"hexagonCostMultiplier" ini:"HexagonCostMultiplier"`
	//MutagenLevelBoost[<Stat_ID>]
	//MutagenLevelBoost_Bred[<Stat_ID>]
	WorldBuffScalingEfficacy float32 `json:"worldBuffScalingEfficacy" ini:"WorldBuffScalingEfficacy"`
}

type Game struct {
	ScriptShootergameShootergamemode ScriptShootergameShootergamemode `json:"ScriptShootergameShootergamemode" ini:"/script/shootergame.shootergamemode"`
}

func generateNewDefaultGame() Game {
	return Game{
		ScriptShootergameShootergamemode: ScriptShootergameShootergamemode{
			AutoPvEStartTimeSeconds:                         0.0,
			AutoPvEStopTimeSeconds:                          0.0,
			BabyCuddleGracePeriodMultiplier:                 1.0,
			BabyCuddleIntervalMultiplier:                    1.0,
			BabyCuddleLoseImprintQualitySpeedMultiplier:     1.0,
			BabyFoodConsumptionSpeedMultiplier:              1.0,
			BabyImprintAmountMultiplier:                     1.0,
			BabyImprintingStatScaleMultiplier:               1.0,
			BabyMatureSpeedMultiplier:                       1.0,
			BAllowUnclaimDinos:                              true,
			BAllowCustomRecipes:                             true,
			BAllowFlyerSpeedLeveling:                        false,
			BAllowPlatformSaddleMultiFloors:                 false,
			BAllowUnlimitedRespecs:                          false,
			BaseTemperatureMultiplier:                       1.0,
			BAutoPvETimer:                                   false,
			BAutoPvEUseSystemTime:                           false,
			BAutoUnlockAllEngrams:                           false,
			BDisableDinoBreeding:                            false,
			BDisableDinoRiding:                              false,
			BDisableDinoTaming:                              false,
			BDisableFriendlyFire:                            false,
			BDisableLootCrates:                              false,
			BDisableStructurePlacementCollision:             false,
			BFlyerPlatformAllowUnalignedDinoBasing:          false,
			BIgnoreStructuresPreventionVolumes:              false,
			BIncreasePvPRespawnInterval:                     true,
			BOnlyAllowSpecifiedEngrams:                      false,
			BPassiveDefensesDamageRiderlessDinos:            false,
			BPvEAllowTribeWar:                               true,
			BPvEAllowTribeWarCancel:                         false,
			BPvEDisableFriendlyFire:                         false,
			BShowCreativeMode:                               false,
			BUseCorpseLocator:                               true,
			BUseDinoLevelUpAnimations:                       true,
			BUseSingleplayerSettings:                        false,
			BUseTameLimitForStructuresOnly:                  false,
			CraftingSkillBonusMultiplier:                    1.0,
			CraftXPMultiplier:                               1.0,
			CropDecaySpeedMultiplier:                        1.0,
			CropGrowthSpeedMultiplier:                       1.0,
			CustomRecipeEffectivenessMultiplier:             1.0,
			CustomRecipeSkillMultiplier:                     1.0,
			DestroyTamesOverLevelClamp:                      450,
			DinoHarvestingDamageMultiplier:                  3.2,
			DinoTurretDamageMultiplier:                      1.0,
			EggHatchSpeedMultiplier:                         1.0,
			FastDecayInterval:                               43200,
			FishingLootQualityMultiplier:                    1.0,
			FuelConsumptionIntervalMultiplier:               1.0,
			GenericXPMultiplier:                             1.0,
			GlobalCorpseDecompositionTimeMultiplier:         1.0,
			GlobalItemDecompositionTimeMultiplier:           1.0,
			GlobalPoweredBatteryDurabilityDecreasePerSecond: 3.0,
			GlobalSpoilingTimeMultiplier:                    1.0,
			HairGrowthSpeedMultiplier:                       1.0,
			HarvestXPMultiplier:                             1.0,
			IncreasePvPRespawnIntervalBaseAmount:            60.0,
			IncreasePvPRespawnIntervalCheckPeriod:           300.0,
			IncreasePvPRespawnIntervalMultiplier:            2.0,
			KillXPMultiplier:                                1.0,
			LayEggIntervalMultiplier:                        1.0,
			LimitNonPlayerDroppedItemsCount:                 0,
			LimitNonPlayerDroppedItemsRange:                 0,
			MatingIntervalMultiplier:                        1.0,
			MatingSpeedMultiplier:                           1.0,
			MaxFallSpeedMultiplier:                          1.0,
			MaxNumberOfPlayersInTribe:                       0,
			MaxTribeLogs:                                    400,
			PassiveTameIntervalMultiplier:                   1.0,
			PlayerHarvestingDamageMultiplier:                1.0,
			PoopIntervalMultiplier:                          1.0,
			PreventOfflinePvPConnectionInvincibleInterval:   5.0,
			PvPZoneStructureDamageMultiplier:                6.0,
			ResourceNoReplenishRadiusPlayers:                1.0,
			ResourceNoReplenishRadiusStructures:             1.0,
			SpecialXPMultiplier:                             1.0,
			StructureDamageRepairCooldown:                   180,
			SupplyCrateLootQualityMultiplier:                1.0,
			TamedDinoCharacterFoodDrainMultiplier:           1.0,
			TamedDinoTorporDrainMultiplier:                  1.0,
			TribeSlotReuseCooldown:                          0.0,
			UseCorpseLifeSpanMultiplier:                     1.0,
			WildDinoCharacterFoodDrainMultiplier:            1.0,
			WildDinoTorporDrainMultiplier:                   1.0,
			BHardLimitTurretsInRange:                        false,
			BLimitTurretsInRange:                            true,
			LimitTurretsNum:                                 100,
			LimitTurretsRange:                               10000,
			AdjustableMutagenSpawnDelayMultiplier:           1.0,
			BaseHexagonRewardMultiplier:                     1.0,
			BDisableHexagonStore:                            false,
			BDisableDefaultMapItemSets:                      false,
			BDisableGenesisMissions:                         false,
			BDisableWorldBuffs:                              false,
			BEnableWorldBuffScaling:                         false,
			BGenesisUseStructuresPreventionVolumes:          false,
			BHexStoreAllowOnlyEngramTradeOption:             false,
			HexagonCostMultiplier:                           1.0,
			WorldBuffScalingEfficacy:                        1.0,
		},
	}

}

// SaveGameIni saves the game to the ini file in the server directory
func (s *Server) SaveGameIni(filePathToLoadFrom string, overrideUseIniConfig bool) error {

	ini.PrettyFormat = false

	filePath := filepath.Join(s.ServerPath, "ShooterGame", "Saved", "Config", "WindowsServer", "Game.ini")
	// if exists, remove it, fixes any issues with saving additional settings that may have since been removed
	if overrideUseIniConfig {
		if _, err := os.Stat(filePath); err == nil {
			// File exists, so remove it
			err := os.Remove(filePath)
			if err != nil {
				return err
			}
		}
	}

	err := ensureFilePath(filePath)
	if err != nil {
		return err
	}

	gIni, err := ini.LoadSources(iniOpts, filePathToLoadFrom)
	if err != nil {
		return err
	}

	if s.UseIniConfig || overrideUseIniConfig {
		err = gIni.MapTo(&s.Game)
		if err != nil {
			return err
		}
	}

	//modify ini file here

	err = gIni.ReflectFrom(&s.Game)
	if err != nil {
		return err
	}

	gIni.Append([]byte(s.AdditionalGameSections))

	err = gIni.SaveTo(filepath.Join(s.ServerPath, "ShooterGame\\Saved\\Config\\WindowsServer\\Game.ini"))
	if err != nil {
		return err
	}

	runtime.EventsEmit(s.ctx, "reloadServers")

	return nil
}
