package server

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
	DestroyTamesOverLevelClamp          int32   `json:"destroyTamesOverLevelClamp" ini:"DestroyTamesOverLevelClamp"`
	//DinoClassDamageMultipliers https://ark.wiki.gg/wiki/Server_configuration#DinoClassDamageMultipliers
	//DinoClassResistanceMultipliers https://ark.wiki.gg/wiki/Server_configuration#DinoClassResistanceMultipliers
	DinoHarvestingDamageMultiplier float64 `json:"dinoHarvestingDamageMultiplier" ini:"DinoHarvestingDamageMultiplier"`
	//DinoSpawnWeightMultipliers https://ark.wiki.gg/wiki/Server_configuration#DinoSpawnWeightMultipliers
	DinoTurretDamageMultiplier float32 `json:"dinoTurretDamageMultiplier" ini:"DinoTurretDamageMultiplier"`
	EggHatchSpeedMultiplier    float32 `json:"eggHatchSpeedMultiplier" ini:"EggHatchSpeedMultiplier"`
	//EngramEntryAutoUnlocks https://ark.wiki.gg/wiki/Server_configuration#EngramEntryAutoUnlocks
	//ExcludeItemIndices https://ark.wiki.gg/wiki/Item_IDs
	FastDecayInterval                               int32   `json:"fastDecayInterval" ini:"FastDecayInterval"`
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
	LimitNonPlayerDroppedItemsCount int32   `json:"limitNonPlayerDroppedItemsCount" ini:"LimitNonPlayerDroppedItemsCount"`
	LimitNonPlayerDroppedItemsRange int32   `json:"limitNonPlayerDroppedItemsRange" ini:"LimitNonPlayerDroppedItemsRange"`
	MatingIntervalMultiplier        float32 `json:"matingIntervalMultiplier" ini:"MatingIntervalMultiplier"`
	MatingSpeedMultiplier           float32 `json:"matingSpeedMultiplier" ini:"MatingSpeedMultiplier"`
	MaxAlliancesPerTribe            int32
	MaxFallSpeedMultiplier          float32 `json:"maxFallSpeedMultiplier" ini:"MaxFallSpeedMultiplier"`
	MaxNumberOfPlayersInTribe       int32   `json:"maxNumberOfPlayersInTribe" ini:"MaxNumberOfPlayersInTribe"`
	MaxTribeLogs                    int32   `json:"maxTribeLogs" ini:"MaxTribeLogs"`
	MaxTribesPerAlliance            int32   `json:"maxTribesPerAlliance" ini:"MaxTribesPerAlliance"`
	//NPCReplacements https://ark.wiki.gg/wiki/Server_configuration#NPCReplacements
	OverrideMaxExperiencePointsDino   int32 `json:"overrideMaxExperiencePointsDino" ini:"OverrideMaxExperiencePointsDino"`
	OverrideMaxExperiencePointsPlayer int32 `json:"overrideMaxExperiencePointsPlayer" ini:"OverrideMaxExperiencePointsPlayer"`
	//OverrideEngramEntries https://ark.wiki.gg/wiki/Server_configuration#OverrideEngramEntries_and_OverrideNamedEngramEntries
	//OverrideNamedEngramEntries https://ark.wiki.gg/wiki/Server_configuration#OverrideEngramEntries_and_OverrideNamedEngramEntries
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
	StructureDamageRepairCooldown         int32   `json:"structureDamageRepairCooldown" ini:"StructureDamageRepairCooldown"`
	SupplyCrateLootQualityMultiplier      float32 `json:"supplyCrateLootQualityMultiplier" ini:"SupplyCrate"`
	TamedDinoCharacterFoodDrainMultiplier float32 `json:"tamedDinoCharacterFoodDrainMultiplier" ini:"TamedDinoCharacterFoodDrainMultiplier"`
	//TamedDinoClassDamageMultipliers https://ark.wiki.gg/wiki/Server_configuration#DinoClassDamageMultipliers
	//TamedDinoClassResistanceMultipliers https://ark.wiki.gg/wiki/Server_configuration#DinoClassResistanceMultipliers
	TamedDinoTorporDrainMultiplier       float32 `json:"tamedDinoTorporDrainMultiplier" ini:"TamedDinoTorporDrainMultiplier"`
	TribeSlotReuseCooldown               float32 `json:"tribeSlotReuseCooldown" ini:"TribeSlotReuseCooldown"`
	UseCorpseLifeSpanMultiplier          float32 `json:"useCorpseLifeSpanMultiplier" ini:"UseCorpseLifeSpanMultiplier"`
	WildDinoCharacterFoodDrainMultiplier float32 `json:"wildDinoCharacterFoodDrainMultiplier" ini:"WildDinoCharacterFoodDrainMultiplier"`
	WildDinoTorporDrainMultiplier        float32 `json:"WildDinoTorporDrainMultiplier" ini:"WildDinoTorporDrainMultiplier"`
	BHardLimitTurretsInRange             bool    `json:"bHardLimitTurretsInRange" ini:"bHardLimitTurretsInRange"`
	BLimitTurretsInRange                 bool    `json:"bLimitTurretsInRange" ini:"bLimitTurretsInRange"`
	LimitTurretsNum                      int32   `json:"limitTurretsNum" ini:"LimitTurretsNum"`
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

// TODO implement default values
func generateNewDefaultGame() Game {
	return Game{}

}
