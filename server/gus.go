package server

type ServerSettings struct {
	ActiveMods string `json:"activeMods" ini:"ActiveMods"`
	//ActiveMapMod                 string `json:"activeMapMod" ini:"ActiveMapMod"` TODO IMPLEMENT IN GENERATION FUNC
	AdminLogging                               bool    `json:"adminLogging" ini:"AdminLogging"`
	AllowAnyoneBabyImprintCuddle               bool    `json:"allowAnyoneBabyImprintCuddle" ini:"AllowAnyoneBabyImprintCuddle"`
	AllowCaveBuildingPvE                       bool    `json:"allowCaveBuildingPvE" ini:"AllowCaveBuildingPvE"`
	AllowCaveBuildingPvP                       bool    `json:"allowCaveBuildingPvP" ini:"AllowCaveBuildingPvP"`                           //TODO: Usage unknown in asa
	AllowCrateSpawnsOnTopOfStructures          bool    `json:"allowCrateSpawnsOnTopOfStructures" ini:"AllowCrateSpawnsOnTopOfStructures"` //TODO: Usage unknown in asa
	AllowFlyerCarryPvE                         bool    `json:"allowFlyerCarryPvE" ini:"AllowFlyerCarryPvE"`
	AllowFlyingStaminaRecovery                 bool    `json:"allowFlyingStaminaRecovery" ini:"AllowFlyingStaminaRecovery"` //TODO: Usage unknown in asa
	AllowHideDamageSourceFromLogs              bool    `json:"allowHideDamageSourceFromLogs" ini:"AllowHideDamageSourceFromLogs"`
	AllowHitMarkers                            bool    `json:"allowHitMarkers" ini:"AllowHitMarkers"`
	AllowIntegratedSPlusStructures             bool    `json:"allowIntegratedSPlusStructures" ini:"AllowIntegratedSPlusStructures"` //TODO: Usage unknown in asa
	AllowMultipleAttachedC4                    bool    `json:"allowMultipleAttachedC4" ini:"AllowMultipleAttachedC4"`
	AllowRaidDinoFeeding                       bool    `json:"allowRaidDinoFeeding" ini:"AllowRaidDinoFeeding"`
	AllowSharedConnections                     bool    `json:"allowSharedConnections" ini:"AllowSharedConnections"`           //TODO: Usage unknown in asa
	AllowTekSuitPowersInGenesis                bool    `json:"allowTekSuitPowersInGenesis" ini:"AllowTekSuitPowersInGenesis"` //TODO: Usage unknown in asa
	AllowThirdPersonPlayer                     bool    `json:"allowThirdPersonPlayer" ini:"AllowThirdPersonPlayer"`
	AlwaysAllowStructurePickup                 bool    `json:"alwaysAllowStructurePickup" ini:"AlwaysAllowStructurePickup"`
	AlwaysNotifyPlayerLeft                     bool    `json:"alwaysNotifyPlayerLeft" ini:"AlwaysNotifyPlayerLeft"`                         //TODO: Usage unknown in asa
	AutoDestroyDecayedDinos                    bool    `json:"autoDestroyDecayedDinos" ini:"AutoDestroyDecayedDinos"`                       //TODO: Usage unknown in asa
	AutoDestroyOldStructuresMultiplier         float32 `json:"autoDestroyOldStructuresMultiplier" ini:"AutoDestroyOldStructuresMultiplier"` //TODO: Usage unknown in asa
	AutoSavePeriodMinutes                      float32 `json:"autoSavePeriodMinutes" ini:"AutoSavePeriodMinutes"`
	BanListUrl                                 string  `json:"banListUrl" ini:"BanListUrl"`                         //TODO: Usage unknown in asa
	BForceCanRideFliers                        bool    `json:"bForceCanRideFliers" ini:"bForceCanRideFliers"`       //TODO: Usage unknown in asa
	ClampItemSpoilingTimes                     bool    `json:"clampItemSpoilingTimes" ini:"ClampItemSpoilingTimes"` //TODO: Usage unknown in asa
	ClampItemStats                             bool    `json:"clampItemStats" ini:"ClampItemStats"`                 //TODO: Usage unknown in asa
	ClampResourceHarvestDamage                 bool    `json:"clampResourceHarvestDamage" ini:"ClampResourceHarvestDamage"`
	CustomDynamicConfigUrl                     bool    `json:"customDynamicConfigUrl" ini:"CustomDynamicConfigUrl"`
	CustomLiveTuningUrl                        bool    `json:"customLiveTuningUrl" ini:"CustomLiveTuningUrl"` //TODO Usage unknown in asa
	DayCycleSpeedScale                         float32 `json:"dayCycleSpeedScale" ini:"DayCycleSpeedScale"`
	DayTimeSpeedScale                          float32 `json:"dayTimeSpeedScale" ini:"DayTimeSpeedScale"`
	DifficultyOffset                           float32 `json:"difficultyOffset" ini:"DifficultyOffset"`
	DinoCharacterFoodDrainMultiplier           float32 `json:"dinoCharacterFoodDrainMultiplier" ini:"DinoCharacterFoodDrainMultiplier"`
	DinoCharacterHealthRecoveryMultiplier      float32 `json:"dinoCharacterHealthRecoveryMultiplier" ini:"DinoCharacterHealthRecoveryMultiplier"`
	DinoCharacterStaminaDrainMultiplier        float32 `json:"dinoCharacterStaminaDrainMultiplier" ini:"DinoCharacterStaminaDrainMultiplier"`
	DinoCountMultiplier                        float32 `json:"dinoCountMultiplier" ini:"DinoCountMultiplier"` //TODO: Usage unknown in asa
	DinoDamageMultiplier                       float32 `json:"dinoDamageMultiplier" ini:"DinoDamageMultiplier"`
	DinoResistanceMultiplier                   float32 `json:"dinoResistanceMultiplier" ini:"DinoResistanceMultiplier"`
	DisableDinoDecayPvE                        bool    `json:"disableDinoDecayPvE" ini:"DisableDinoDecayPvE"`
	DisableImprintDinoBuff                     bool    `json:"disableImprintDinoBuff" ini:"DisableImprintDinoBuff"`
	DisablePvEGamma                            bool    `json:"disablePvEGamma" ini:"DisablePvEGamma"`
	DisableStructureDecayPvE                   bool    `json:"disableStructureDecayPvE" ini:"DisableStructureDecayPvE"`
	DisableWeatherFog                          bool    `json:"disableWeatherFog" ini:"DisableWeatherFog"`
	DontAlwaysNotifyPlayerJoined               bool    `json:"dontAlwaysNotifyPlayerJoined" ini:"DontAlwaysNotifyPlayerJoined"`
	EnableExtraStructurePreventionVolumes      bool    `json:"enableExtraStructurePreventionVolumes" ini:"EnableExtraStructurePreventionVolumes"`
	EnablePvPGamma                             bool    `json:"enablePvPGamma" ini:"EnablePvPGamma"`
	ExtinctionEventTimeInterval                int32   `json:"extinctionEventTimeInterval" ini:"ExtinctionEventTimeInterval"`           //TODO: Usage unknown in asa
	FastDecayUnsnappedCoreStructures           bool    `json:"fastDecayUnsnappedCoreStructures" ini:"FastDecayUnsnappedCoreStructures"` //TODO: Usage unknown in asa
	ForceAllStructureLocking                   bool    `json:"forceAllStructureLocking" ini:"ForceAllStructureLocking"`
	GlobalVoiceChat                            bool    `json:"globalVoiceChat" ini:"globalVoiceChat"` //TODO if it actually starts with a non capital letter
	HarvestAmountMultiplier                    float32 `json:"harvestAmountMultiplier" ini:"HarvestAmountMultiplier"`
	HarvestHealthMultiplier                    float32 `json:"harvestHealthMultiplier" ini:"HarvestHealthMultiplier"`
	IgnoreLimitMaxStructuresInRangeTypeFlag    bool    `json:"ignoreLimitMaxStructuresInRangeTypeFlag" ini:"IgnoreLimitMaxStructuresInRangeTypeFlag"` //TODO: Usage unknown in asa
	ItemStackSizeMultiplier                    float32 `json:"itemStackSizeMultiplier" ini:"ItemStackSizeMultiplier"`
	KickIdlePlayersPeriod                      float32 `json:"kickIdlePlayersPeriod" ini:"KickIdlePlayersPeriod"`
	MaxGateFrameOnSaddles                      int32   `json:"maxGateFrameOnSaddles" ini:"MaxGateFrameOnSaddles"`     //TODO: Usage unknown in asa
	MaxHexagonsPerCharacter                    int32   `json:"maxHexagonsPerCharacter" ini:"MaxHexagonsPerCharacter"` //TODO: Usage unknown in asa
	MaxPersonalTamedDinos                      int32   `json:"maxPersonalTamedDinos" ini:"MaxPersonalTamedDinos"`
	MaxPlatformSaddleStructureLimit            int32   `json:"maxPlatformSaddleStructureLimit" ini:"MaxPlatformSaddleStructureLimit"`
	MaxTamedDinos                              float32 `json:"maxTamedDinos" ini:"MaxTamedDinos"`
	MaxTributeCharacters                       int32   `json:"maxTributeCharacters" ini:"MaxTributeCharacters"` //TODO: Usage unknown in asa
	MaxTributeDinos                            int32   `json:"maxTributeDinos" ini:"MaxTributeDinos"`
	MaxTributeItems                            int32   `json:"maxTributeItems" ini:"MaxTributeItems"`
	NightTimeSpeedScale                        float32 `json:"nightTimeSpeedScale" ini:"NightTimeSpeedScale"`
	NonPermanentDiseases                       bool    `json:"nonPermanentDiseases" ini:"NonPermanentDiseases"`
	NPCNetworkStasisRangeScalePlayerCountStart int32   `json:"npcNetworkStasisRangeScalePlayerCountStart" ini:"NPCNetworkStasisRangeScalePlayerCountStart"` //TODO: Usage unknown in asa
	NPCNetworkStasisRangeScalePlayerCountEnd   int32   `json:"npcNetworkStasisRangeScalePlayerCountEnd" ini:"NPCNetworkStasisRangeScalePlayerCountEnd"`     //TODO: Usage unknown in asa
	NPCNetworkStasisRangeScalePercentEnd       float32 `json:"npcNetworkStasisRangeScalePercentEnd" ini:"NPCNetworkStasisRangeScalePercentEnd"`             //TODO: Usage unknown in asa
	OnlyAutoDestroyCoreStructures              bool    `json:"onlyAutoDestroyCoreStructures" ini:"OnlyAutoDestroyCoreStructures"`                           //TODO: Usage unknown in asa
	OnlyDecayUnsnappedCoreStructures           bool    `json:"onlyDecayUnsnappedCoreStructures" ini:"OnlyDecayUnsnappedCoreStructures"`                     //TODO: Usage unknown in asa
	OverrideOfficialDifficulty                 float32 `json:"overrideOfficialDifficulty" ini:"OverrideOfficialDifficulty"`                                 //TODO: Usage unknown in asa
	OverrideStructurePlatformPrevention        bool    `json:"overrideStructurePlatformPrevention" ini:"OverrideStructurePlatformPrevention"`
	OxygenSwimSpeedStatMultiplier              float32 `json:"oxygenSwimSpeedStatMultiplier" ini:"OxygenSwimSpeedStatMultiplier"`
	PerPlatformMaxStructuresMultiplier         float32 `json:"perPlatformMaxStructuresMultiplier" ini:"PerPlatformMaxStructuresMultiplier"`
	PersonalTamedDinosSaddleStructureCost      int32   `json:"personalTamedDinosSaddleStructureCost" ini:"PersonalTamedDinosSaddleStructureCost"` //TODO: Usage unknown in asa
	PlatformSaddleBuildAreaBoundsMultiplier    float32 `json:"platformSaddleBuildAreaBoundsMultiplier" ini:"PlatformSaddleBuildAreaBoundsMultiplier"`
	PlayerCharacterFoodDrainMultiplier         float32 `json:"playerCharacterFoodDrainMultiplier" ini:"PlayerCharacterFoodDrainMultiplier"`
	PlayerCharacterHealthRecoveryMultiplier    float32 `json:"playerCharacterHealthRecoveryMultiplier" ini:"PlayerCharacterHealthRecoveryMultiplier"`
	PlayerCharacterStaminaDrainMultiplier      float32 `json:"playerCharacterStaminaDrainMultiplier" ini:"PlayerCharacterStaminaDrainMultiplier"`
	PlayerCharacterWaterDrainMultiplier        float32 `json:"playerCharacterWaterDrainMultiplier" ini:"PlayerCharacterWaterDrainMultiplier"`
	PlayerDamageMultiplier                     float32 `json:"playerDamageMultiplier" ini:"PlayerDamageMultiplier"`
	PlayerResistanceMultiplier                 float32 `json:"playerResistanceMultiplier" ini:"PlayerResistanceMultiplier"`
	PreventDiseases                            bool    `json:"preventDiseases" ini:"PreventDiseases"`
	PreventMateBoost                           bool    `json:"preventMateBoost" ini:"PreventMateBoost"`
	PreventOfflinePvP                          bool    `json:"preventOfflinePvP" ini:"PreventOfflinePvP"`
	PreventOfflinePvPInterval                  float32 `json:"preventOfflinePvPInterval" ini:"PreventOfflinePvPInterval"`
	PreventSpawnAnimations                     bool    `json:"preventSpawnAnimations" ini:"PreventSpawnAnimations"`
	PreventTribeAlliances                      bool    `json:"preventTribeAlliances" ini:"PreventTribeAlliances"`
	ProximityChat                              bool    `json:"proximityChat" ini:"proximityChat"`
	PvEAllowStructuresAtSupplyDrops            bool    `json:"pveAllowStructuresAtSupplyDrops" ini:"PvEAllowStructuresAtSupplyDrops"`
	PvEDinoDecayPeriodMultiplier               float32 `json:"pveDinoDecayPeriodMultiplier" ini:"PvEDinoDecayPeriodMultiplier"`
	PvEStructureDecayPeriodMultiplier          float32 `json:"pveStructureDecayPeriodMultiplier" ini:"PvEStructureDecayPeriodMultiplier"`
	PvPDinoDecay                               float32 `json:"pvpDinoDecay" ini:"PvPDinoDecay"`
	PvPStructureDecay                          bool    `json:"pvpStructureDecay" ini:"PvPStructureDecay"` //TODO: Usage unknown in asa
	RaidDinoCharacterFoodDrainMultiplier       float32 `json:"raidDinoCharacterFoodDrainMultiplier" ini:"RaidDinoCharacterFoodDrainMultiplier"`
	RandomSupplyCratePoints                    bool    `json:"randomSupplyCratePoints" ini:"RandomSupplyCratePoints"`
	RCONEnabled                                bool    `json:"rconEnabled" ini:"RCONEnabled"`
	RCONPort                                   int32   `json:"rconPort" ini:"RCONPort"`
	RCONServerGameLogBuffer                    float32 `json:"rconServerGameLogBuffer" ini:"RCONServerGameLogBuffer"`
	ResourcesRespawnPeriodMultiplier           float32 `json:"resourcesRespawnPeriodMultiplier" ini:"ResourcesRespawnPeriodMultiplier"`
	ServerAdminPassword                        string  `json:"serverAdminPassword" ini:"ServerAdminPassword"`
	ServerAutoForceRespawnWildDinosInterval    float32 `json:"serverAutoForceRespawnWildDinosInterval" ini:"ServerAutoForceRespawnWildDinosInterval"` //TODO: Usage unknown in asa
	ServerCrosshair                            bool    `json:"serverCrosshair" ini:"ServerCrosshair"`
	ServerForceNoHUD                           bool    `json:"serverForceNoHUD" ini:"ServerForceNoHUD"`
	ServerHardcore                             bool    `json:"serverHardcore" ini:"ServerHardcore"`
	ServerPassword                             string  `json:"serverPassword" ini:"ServerPassword"`
	ServerPVE                                  bool    `json:"serverPVE" ini:"serverPVE"`
	ShowFloatingDamageText                     bool    `json:"showFloatingDamageText" ini:"ShowFloatingDamageText"`
	ShowMapPlayerLocation                      bool    `json:"showMapPlayerLocation" ini:"ShowMapPlayerLocation"`
	SpectatorPassword                          string  `json:"spectatorPassword" ini:"SpectatorPassword"`
	StructureDamageMultiplier                  float32 `json:"structureDamageMultiplier" ini:"StructureDamageMultiplier"`
	StructurePickupHoldDuration                float32 `json:"structurePickupHoldDuration" ini:"StructurePickupHoldDuration"`
	StructurePickupTimeAfterPlacement          float32 `json:"structurePickupTimeAfterPlacement" ini:"StructurePickupTimeAfterPlacement"`
	StructurePreventResourceRadiusMultiplier   float32 `json:"structurePreventResourceRadiusMultiplier" ini:"StructurePreventResourceRadiusMultiplier"`
	StructureResistanceMultiplier              float32 `json:"structureResistanceMultiplier" ini:"StructureResistanceMultiplier"`
	TamedDinoDamageMultiplier                  float32 `json:"tamedDinoDamageMultiplier" ini:"TamedDinoDamageMultiplier"` //TODO: Usage unknown in asa
	TamedDinoResistanceMultiplier              float32 `json:"tamedDinoResistanceMultiplier" ini:"TamedDinoResistanceMultiplier"`
	TamingSpeedMultiplier                      float32 `json:"tamingSpeedMultiplier" ini:"TamingSpeedMultiplier"`
	TheMaxStructuresInRange                    int32   `json:"theMaxStructuresInRange" ini:"TheMaxStructuresInRange"`
	TribeLogDestroyedEnemyStructures           bool    `json:"tribeLogDestroyedEnemyStructures" ini:"TribeLogDestroyedEnemyStructures"`
	TribeNameChangeCooldown                    float32 `json:"tribeNameChangeCooldown" ini:"TribeNameChangeCooldown"`
	UseFjordurTraversalBuff                    bool    `json:"useFjordurTraversalBuff" ini:"UseFjordurTraversalBuff"` //TODO: Usage unknown in asa
	UseOptimizedHarvestingHealth               bool    `json:"useOptimizedHarvestingHealth" ini:"UseOptimizedHarvestingHealth"`
	XPMultiplier                               float32 `json:"xPMultiplier" ini:"XPMultiplier"`
	//CrossARK Transfers
	CrossARKAllowForeignDinoDownloads bool    `json:"crossARKAllowForeignDinoDownloads" ini:"CrossARKAllowForeignDinoDownloads"` //TODO: Usage unknown in asa
	MinimumDinoReuploadInterval       float32 `json:"minimumDinoReuploadInterval" ini:"MinimumDinoReuploadInterval"`             //TODO: Usage unknown in asa
	NoTributeDownloads                bool    `json:"noTributeDownloads" ini:"noTributeDownloads"`
	PreventDownloadDinos              bool    `json:"preventDownloadDinos" ini:"PreventDownloadDinos"`
	PreventDownloadItems              bool    `json:"preventDownloadItems" ini:"PreventDownloadItems"`
	PreventDownloadSurvivors          bool    `json:"preventDownloadSurvivors" ini:"PreventDownloadSurvivors"`
	PreventUploadDinos                bool    `json:"preventUploadDinos" ini:"PreventUploadDinos"` //TODO: Usage unknown in asa
	PreventUploadItems                bool    `json:"preventUploadItems" ini:"PreventUploadItems"`
	PreventUploadSurvivors            bool    `json:"preventUploadSurvivors" ini:"PreventUploadSurvivors"`
	TributeCharacterExpirationSeconds int32   `json:"tributeCharacterExpirationSeconds" ini:"TributeCharacterExpirationSeconds"` //TODO: Usage unknown in asa
	TributeDinoExpirationSeconds      int32   `json:"tributeDinoExpirationSeconds" ini:"TributeDinoExpirationSeconds"`
	TributeItemExpirationSeconds      int32   `json:"tributeItemExpirationSeconds" ini:"TributeItemExpirationSeconds"`
	// Cryo sickness and cryopod nerf
	//CryopodNerfDamageMult float32 `json:"cryopodNerfDamageMult" ini:"CryopodNerfDamageMult"` //TODO: Usage unknown in asa
	//CryopodNerfDuration int32 `json:"cryopodNerfDuration" ini:"CryopodNerfDuration"` //TODO: Usage unknown in asa
	//CryopodNerfIncomingDamageMultPercent float32 `json:"cryopodNerfIncomingDamageMultPercent" ini:"CryopodNerfIncomingDamageMultPercent"` //TODO: Usage unknown in asa
	//EnableCryopodNerf bool `json:"enableCryopodNerf" ini:"EnableCryopodNerf"`
	EnableCryoSicknessPVE bool `json:"enableCryoSicknessPVE" ini:"EnableCryoSicknessPVE"`
	//Text filtering
	BadWordListURL        string `json:"badWordListURL" ini:"BadWordListURL"`
	BadWordWhiteListURL   string `json:"badWordWhiteListURL" ini:"BadWordWhiteListURL"`
	BFilterCharacterNames bool   `json:"bFilterCharacterNames" ini:"bFilterCharacterNames"`
	BFilterChat           bool   `json:"bFilterChat" ini:"bFilterChat"`
	BFilterTribeNames     bool   `json:"bFilterTribeNames" ini:"bFilterTribeNames"`
	//Undocumented
	AllowedCheatersURL              string  `json:"allowedCheatersURL" ini:"AllowedCheatersURL"`
	ChatLogFileSplitIntervalSeconds int32   `json:"chatLogFileSplitIntervalSeconds" ini:"ChatLogFileSplitIntervalSeconds"`
	ChatLogFlushIntervalSeconds     int32   `json:"chatLogFlushIntervalSeconds" ini:"ChatLogFlushIntervalSeconds"`
	ChatLogMaxAgeInDays             int32   `json:"chatLogMaxAgeInDays" ini:"ChatLogMaxAgeInDays"`
	DontRestoreBackup               bool    `json:"dontRestoreBackup" ini:"DontRestoreBackup"`
	EnableAFKKickPlayerCountPercent float32 `json:"enableAFKKickPlayerCountPercent" ini:"EnableAFKKickPlayerCountPercent"`
	EnableMeshBitingProtection      bool    `json:"enableMeshBitingProtection" ini:"EnableMeshBitingProtection"`
	FreezeReaperPregnancy           bool    `json:"freezeReaperPregnancy" ini:"FreezeReaperPregnancy"`
	LogChatMessages                 bool    `json:"logChatMessages" ini:"LogChatMessages"`
	MaxStructuresInSmallRadius      int32   `json:"maxStructuresInSmallRadius" ini:"MaxStructuresInSmallRadius"`
	MaxStructuresToProcess          int32   `json:"maxStructuresToProcess" ini:"MaxStructuresToProcess"`
	PreventOutOfTribePinCodeUse     bool    `json:"preventOutOfTribePinCodeUse" ini:"PreventOutOfTribePinCodeUse"`
	RadiusStructuresInSmallRadius   float32 `json:"radiusStructuresInSmallRadius" ini:"RadiusStructuresInSmallRadius"`
	ServerEnableMeshChecking        bool    `json:"serverEnableMeshChecking" ini:"ServerEnableMeshChecking"`
	TribeMergeAllowed               bool    `json:"tribeMergeAllowed" ini:"TribeMergeAllowed"`
	TribeMergeCooldown              float32 `json:"tribeMergeCooldown" ini:"TribeMergeCooldown"`
	UpdateAllowedCheatersInterval   float32 `json:"updateAllowedCheatersInterval" ini:"UpdateAllowedCheatersInterval"`
	UseExclusiveList                bool    `json:"useExclusiveList" ini:"UseExclusiveList"`
}

type SessionSettings struct {
	MultiHome   string `json:"multiHome" ini:"MultiHome"`
	Port        int32  `json:"port" ini:"Port"`
	QueryPort   int32  `json:"queryPort" ini:"QueryPort"`
	SessionName string `json:"sessionName" ini:"SessionName"`
}

type MultiHome struct {
	MultiHome bool `json:"multiHome" ini:"MultiHome"`
}

type Ragnarok struct {
	AllowMultipleTamedUnicorns bool    `json:"allowMultipleTamedUnicorns" ini:"AllowMultipleTamedUnicorns"`
	EnableVolcano              bool    `json:"enableVolcano" ini:"EnableVolcano"`
	UnicornSpawnInterval       int32   `json:"unicornSpawnInterval" ini:"UnicornSpawnInterval"`
	VolcanoIntensity           float32 `json:"volcanoIntensity" ini:"VolcanoIntensity"`
	VolcanoInterval            int32   `json:"volcanoInterval" ini:"VolcanoInterval"`
}

type MessageOfTheDay struct {
	Duration int32  `json:"duration" ini:"Duration"`
	Message  string `json:"message" ini:"Message"`
}

type ScriptEngineGameSession struct {
	MaxPlayers int32 `json:"maxPlayers" ini:"MaxPlayers"`
}

type GameUserSettings struct {
	//https://ark.wiki.gg/wiki/Server_configuration#GameUserSettings.ini
	ServerSettings ServerSettings `json:"serverSettings" ini:"ServerSettings"`

	SessionSettings SessionSettings `json:"sessionSettings" ini:"SessionSettings"`

	MultiHome               MultiHome               `json:"multiHome" ini:"MultiHome"` //TODO: Check if its "MultiHome" or "/MultiHome" https://ark.wiki.gg/wiki/Server_configuration#[MultiHome]
	ScriptEngineGameSession ScriptEngineGameSession `json:"scriptEngineGameSession" ini:"/Script/Engine.GameSession"`

	Ragnarok Ragnarok `json:"ragnarok" ini:"Ragnarok"`

	MessageOfTheDay MessageOfTheDay
}

// TODO implement default values
func generateNewDefaultGameUserSettings() GameUserSettings {
	return GameUserSettings{
		ServerSettings: ServerSettings{
			ActiveMods:                        "",
			AdminLogging:                      false,
			AllowAnyoneBabyImprintCuddle:      false,
			AllowCaveBuildingPvE:              false,
			AllowCaveBuildingPvP:              false,
			AllowCrateSpawnsOnTopOfStructures: false,
			AllowFlyerCarryPvE:                false,
			AllowFlyingStaminaRecovery:        false,
			AllowHideDamageSourceFromLogs:     true,
			AllowHitMarkers:                   true,
			AllowIntegratedSPlusStructures:    true,
			AllowMultipleAttachedC4:           false,
			AllowRaidDinoFeeding:              false,
			AllowSharedConnections:            false,
			AllowTekSuitPowersInGenesis:       false,
			AllowThirdPersonPlayer:            true,
			AlwaysAllowStructurePickup:        false,
			AlwaysNotifyPlayerLeft:            false,
		},
	}

}
