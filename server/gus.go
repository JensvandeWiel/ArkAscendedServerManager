package server

import (
	"github.com/go-ini/ini"
	"path/filepath"
)

type ServerSettings struct {
	ActiveMods string `json:"activeMods" ini:"ActiveMods"`
	//ActiveMapMod                 string `json:"activeMapMod" ini:"ActiveMapMod"` TODO IMPLEMENT IN GENERATION FUNC
	AdminLogging                       bool    `json:"adminLogging" ini:"AdminLogging"`
	AllowAnyoneBabyImprintCuddle       bool    `json:"allowAnyoneBabyImprintCuddle" ini:"AllowAnyoneBabyImprintCuddle"`
	AllowCaveBuildingPvE               bool    `json:"allowCaveBuildingPvE" ini:"AllowCaveBuildingPvE"`
	AllowCaveBuildingPvP               bool    `json:"allowCaveBuildingPvP" ini:"AllowCaveBuildingPvP"`                           //TODO: Usage unknown in asa
	AllowCrateSpawnsOnTopOfStructures  bool    `json:"allowCrateSpawnsOnTopOfStructures" ini:"AllowCrateSpawnsOnTopOfStructures"` //TODO: Usage unknown in asa
	AllowFlyerCarryPvE                 bool    `json:"allowFlyerCarryPvE" ini:"AllowFlyerCarryPvE"`
	AllowFlyingStaminaRecovery         bool    `json:"allowFlyingStaminaRecovery" ini:"AllowFlyingStaminaRecovery"` //TODO: Usage unknown in asa
	AllowHideDamageSourceFromLogs      bool    `json:"allowHideDamageSourceFromLogs" ini:"AllowHideDamageSourceFromLogs"`
	AllowHitMarkers                    bool    `json:"allowHitMarkers" ini:"AllowHitMarkers"`
	AllowIntegratedSPlusStructures     bool    `json:"allowIntegratedSPlusStructures" ini:"AllowIntegratedSPlusStructures"` //TODO: Usage unknown in asa
	AllowMultipleAttachedC4            bool    `json:"allowMultipleAttachedC4" ini:"AllowMultipleAttachedC4"`
	AllowRaidDinoFeeding               bool    `json:"allowRaidDinoFeeding" ini:"AllowRaidDinoFeeding"`
	AllowSharedConnections             bool    `json:"allowSharedConnections" ini:"AllowSharedConnections"`           //TODO: Usage unknown in asa
	AllowTekSuitPowersInGenesis        bool    `json:"allowTekSuitPowersInGenesis" ini:"AllowTekSuitPowersInGenesis"` //TODO: Usage unknown in asa
	AllowThirdPersonPlayer             bool    `json:"allowThirdPersonPlayer" ini:"AllowThirdPersonPlayer"`
	AlwaysAllowStructurePickup         bool    `json:"alwaysAllowStructurePickup" ini:"AlwaysAllowStructurePickup"`
	AlwaysNotifyPlayerLeft             bool    `json:"alwaysNotifyPlayerLeft" ini:"AlwaysNotifyPlayerLeft"`                         //TODO: Usage unknown in asa
	AutoDestroyDecayedDinos            bool    `json:"autoDestroyDecayedDinos" ini:"AutoDestroyDecayedDinos"`                       //TODO: Usage unknown in asa
	AutoDestroyOldStructuresMultiplier float32 `json:"autoDestroyOldStructuresMultiplier" ini:"AutoDestroyOldStructuresMultiplier"` //TODO: Usage unknown in asa
	AutoSavePeriodMinutes              float32 `json:"autoSavePeriodMinutes" ini:"AutoSavePeriodMinutes"`
	BanListURL                         string  `json:"banListUrl" ini:"BanListURL"` //TODO: Usage unknown in asa
	//BForceCanRideFliers                        bool    `json:"bForceCanRideFliers" ini:"bForceCanRideFliers"`       //TODO: Usage unknown in asa
	ClampItemSpoilingTimes     bool `json:"clampItemSpoilingTimes" ini:"ClampItemSpoilingTimes"` //TODO: Usage unknown in asa
	ClampItemStats             bool `json:"clampItemStats" ini:"ClampItemStats"`                 //TODO: Usage unknown in asa
	ClampResourceHarvestDamage bool `json:"clampResourceHarvestDamage" ini:"ClampResourceHarvestDamage"`
	//CustomDynamicConfigUrl                     bool    `json:"customDynamicConfigUrl" ini:"CustomDynamicConfigUrl"`
	//CustomLiveTuningUrl                        bool    `json:"customLiveTuningUrl" ini:"CustomLiveTuningUrl"` //TODO Usage unknown in asa
	DayCycleSpeedScale                    float32 `json:"dayCycleSpeedScale" ini:"DayCycleSpeedScale"`
	DayTimeSpeedScale                     float32 `json:"dayTimeSpeedScale" ini:"DayTimeSpeedScale"`
	DifficultyOffset                      float32 `json:"difficultyOffset" ini:"DifficultyOffset"`
	DinoCharacterFoodDrainMultiplier      float32 `json:"dinoCharacterFoodDrainMultiplier" ini:"DinoCharacterFoodDrainMultiplier"`
	DinoCharacterHealthRecoveryMultiplier float32 `json:"dinoCharacterHealthRecoveryMultiplier" ini:"DinoCharacterHealthRecoveryMultiplier"`
	DinoCharacterStaminaDrainMultiplier   float32 `json:"dinoCharacterStaminaDrainMultiplier" ini:"DinoCharacterStaminaDrainMultiplier"`
	DinoCountMultiplier                   float32 `json:"dinoCountMultiplier" ini:"DinoCountMultiplier"` //TODO: Usage unknown in asa
	DinoDamageMultiplier                  float32 `json:"dinoDamageMultiplier" ini:"DinoDamageMultiplier"`
	DinoResistanceMultiplier              float32 `json:"dinoResistanceMultiplier" ini:"DinoResistanceMultiplier"`
	DisableDinoDecayPvE                   bool    `json:"disableDinoDecayPvE" ini:"DisableDinoDecayPvE"`
	DisableImprintDinoBuff                bool    `json:"disableImprintDinoBuff" ini:"DisableImprintDinoBuff"`
	DisablePvEGamma                       bool    `json:"disablePvEGamma" ini:"DisablePvEGamma"`
	DisableStructureDecayPvE              bool    `json:"disableStructureDecayPvE" ini:"DisableStructureDecayPvE"`
	DisableWeatherFog                     bool    `json:"disableWeatherFog" ini:"DisableWeatherFog"`
	DontAlwaysNotifyPlayerJoined          bool    `json:"dontAlwaysNotifyPlayerJoined" ini:"DontAlwaysNotifyPlayerJoined"`
	EnableExtraStructurePreventionVolumes bool    `json:"enableExtraStructurePreventionVolumes" ini:"EnableExtraStructurePreventionVolumes"`
	EnablePvPGamma                        bool    `json:"enablePvPGamma" ini:"EnablePvPGamma"`
	//ExtinctionEventTimeInterval                int   `json:"extinctionEventTimeInterval" ini:"ExtinctionEventTimeInterval"`           //TODO: Usage unknown in asa
	FastDecayUnsnappedCoreStructures           bool    `json:"fastDecayUnsnappedCoreStructures" ini:"FastDecayUnsnappedCoreStructures"` //TODO: Usage unknown in asa
	ForceAllStructureLocking                   bool    `json:"forceAllStructureLocking" ini:"ForceAllStructureLocking"`
	GlobalVoiceChat                            bool    `json:"globalVoiceChat" ini:"globalVoiceChat"` //TODO if it actually starts with a non capital letter
	HarvestAmountMultiplier                    float32 `json:"harvestAmountMultiplier" ini:"HarvestAmountMultiplier"`
	HarvestHealthMultiplier                    float32 `json:"harvestHealthMultiplier" ini:"HarvestHealthMultiplier"`
	IgnoreLimitMaxStructuresInRangeTypeFlag    bool    `json:"ignoreLimitMaxStructuresInRangeTypeFlag" ini:"IgnoreLimitMaxStructuresInRangeTypeFlag"` //TODO: Usage unknown in asa
	ItemStackSizeMultiplier                    float32 `json:"itemStackSizeMultiplier" ini:"ItemStackSizeMultiplier"`
	KickIdlePlayersPeriod                      float32 `json:"kickIdlePlayersPeriod" ini:"KickIdlePlayersPeriod"`
	MaxGateFrameOnSaddles                      int     `json:"maxGateFrameOnSaddles" ini:"MaxGateFrameOnSaddles"`     //TODO: Usage unknown in asa
	MaxHexagonsPerCharacter                    int     `json:"maxHexagonsPerCharacter" ini:"MaxHexagonsPerCharacter"` //TODO: Usage unknown in asa
	MaxPersonalTamedDinos                      int     `json:"maxPersonalTamedDinos" ini:"MaxPersonalTamedDinos"`
	MaxPlatformSaddleStructureLimit            int     `json:"maxPlatformSaddleStructureLimit" ini:"MaxPlatformSaddleStructureLimit"`
	MaxTamedDinos                              float32 `json:"maxTamedDinos" ini:"MaxTamedDinos"`
	MaxTributeCharacters                       int     `json:"maxTributeCharacters" ini:"MaxTributeCharacters"` //TODO: Usage unknown in asa
	MaxTributeDinos                            int     `json:"maxTributeDinos" ini:"MaxTributeDinos"`
	MaxTributeItems                            int     `json:"maxTributeItems" ini:"MaxTributeItems"`
	NightTimeSpeedScale                        float32 `json:"nightTimeSpeedScale" ini:"NightTimeSpeedScale"`
	NonPermanentDiseases                       bool    `json:"nonPermanentDiseases" ini:"NonPermanentDiseases"`
	NPCNetworkStasisRangeScalePlayerCountStart int     `json:"npcNetworkStasisRangeScalePlayerCountStart" ini:"NPCNetworkStasisRangeScalePlayerCountStart"` //TODO: Usage unknown in asa
	NPCNetworkStasisRangeScalePlayerCountEnd   int     `json:"npcNetworkStasisRangeScalePlayerCountEnd" ini:"NPCNetworkStasisRangeScalePlayerCountEnd"`     //TODO: Usage unknown in asa
	NPCNetworkStasisRangeScalePercentEnd       float32 `json:"npcNetworkStasisRangeScalePercentEnd" ini:"NPCNetworkStasisRangeScalePercentEnd"`             //TODO: Usage unknown in asa
	OnlyAutoDestroyCoreStructures              bool    `json:"onlyAutoDestroyCoreStructures" ini:"OnlyAutoDestroyCoreStructures"`                           //TODO: Usage unknown in asa
	OnlyDecayUnsnappedCoreStructures           bool    `json:"onlyDecayUnsnappedCoreStructures" ini:"OnlyDecayUnsnappedCoreStructures"`                     //TODO: Usage unknown in asa
	OverrideOfficialDifficulty                 float32 `json:"overrideOfficialDifficulty" ini:"OverrideOfficialDifficulty"`                                 //TODO: Usage unknown in asa
	OverrideStructurePlatformPrevention        bool    `json:"overrideStructurePlatformPrevention" ini:"OverrideStructurePlatformPrevention"`
	OxygenSwimSpeedStatMultiplier              float32 `json:"oxygenSwimSpeedStatMultiplier" ini:"OxygenSwimSpeedStatMultiplier"`
	PerPlatformMaxStructuresMultiplier         float32 `json:"perPlatformMaxStructuresMultiplier" ini:"PerPlatformMaxStructuresMultiplier"`
	PersonalTamedDinosSaddleStructureCost      int     `json:"personalTamedDinosSaddleStructureCost" ini:"PersonalTamedDinosSaddleStructureCost"` //TODO: Usage unknown in asa
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
	PvPDinoDecay                               bool    `json:"pvpDinoDecay" ini:"PvPDinoDecay"`
	PvPStructureDecay                          bool    `json:"pvpStructureDecay" ini:"PvPStructureDecay"` //TODO: Usage unknown in asa
	RaidDinoCharacterFoodDrainMultiplier       float32 `json:"raidDinoCharacterFoodDrainMultiplier" ini:"RaidDinoCharacterFoodDrainMultiplier"`
	RandomSupplyCratePoints                    bool    `json:"randomSupplyCratePoints" ini:"RandomSupplyCratePoints"`
	RCONEnabled                                bool    `json:"rconEnabled" ini:"RCONEnabled"`
	RCONPort                                   int     `json:"rconPort" ini:"RCONPort"`
	RCONServerGameLogBuffer                    float32 `json:"rconServerGameLogBuffer" ini:"RCONServerGameLogBuffer"`
	ResourcesRespawnPeriodMultiplier           float32 `json:"resourcesRespawnPeriodMultiplier" ini:"ResourcesRespawnPeriodMultiplier"`
	ServerAdminPassword                        string  `json:"serverAdminPassword" ini:"ServerAdminPassword"`
	ServerAutoForceRespawnWildDinosInterval    float32 `json:"serverAutoForceRespawnWildDinosInterval" ini:"ServerAutoForceRespawnWildDinosInterval"` //TODO: Usage unknown in asa
	ServerCrosshair                            bool    `json:"serverCrosshair" ini:"ServerCrosshair"`
	ServerForceNoHUD                           bool    `json:"serverForceNoHUD" ini:"ServerForceNoHUD"`
	ServerHardcore                             bool    `json:"serverHardcore" ini:"ServerHardcore"`
	//ServerPassword                             string  `json:"serverPassword" ini:"ServerPassword"` //todo add in pre start function
	ServerPVE              bool `json:"serverPVE" ini:"serverPVE"`
	ShowFloatingDamageText bool `json:"showFloatingDamageText" ini:"ShowFloatingDamageText"`
	ShowMapPlayerLocation  bool `json:"showMapPlayerLocation" ini:"ShowMapPlayerLocation"`
	//SpectatorPassword                        string  `json:"spectatorPassword" ini:"SpectatorPassword"` //todo add in pre start function
	StructureDamageMultiplier                float32 `json:"structureDamageMultiplier" ini:"StructureDamageMultiplier"`
	StructurePickupHoldDuration              float32 `json:"structurePickupHoldDuration" ini:"StructurePickupHoldDuration"`
	StructurePickupTimeAfterPlacement        float32 `json:"structurePickupTimeAfterPlacement" ini:"StructurePickupTimeAfterPlacement"`
	StructurePreventResourceRadiusMultiplier float32 `json:"structurePreventResourceRadiusMultiplier" ini:"StructurePreventResourceRadiusMultiplier"`
	StructureResistanceMultiplier            float32 `json:"structureResistanceMultiplier" ini:"StructureResistanceMultiplier"`
	TamedDinoDamageMultiplier                float32 `json:"tamedDinoDamageMultiplier" ini:"TamedDinoDamageMultiplier"` //TODO: Usage unknown in asa
	TamedDinoResistanceMultiplier            float32 `json:"tamedDinoResistanceMultiplier" ini:"TamedDinoResistanceMultiplier"`
	TamingSpeedMultiplier                    float32 `json:"tamingSpeedMultiplier" ini:"TamingSpeedMultiplier"`
	TheMaxStructuresInRange                  int     `json:"theMaxStructuresInRange" ini:"TheMaxStructuresInRange"`
	TribeLogDestroyedEnemyStructures         bool    `json:"tribeLogDestroyedEnemyStructures" ini:"TribeLogDestroyedEnemyStructures"`
	TribeNameChangeCooldown                  float32 `json:"tribeNameChangeCooldown" ini:"TribeNameChangeCooldown"`
	UseFjordurTraversalBuff                  bool    `json:"useFjordurTraversalBuff" ini:"UseFjordurTraversalBuff"` //TODO: Usage unknown in asa
	UseOptimizedHarvestingHealth             bool    `json:"useOptimizedHarvestingHealth" ini:"UseOptimizedHarvestingHealth"`
	XPMultiplier                             float32 `json:"xPMultiplier" ini:"XPMultiplier"`
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
	TributeCharacterExpirationSeconds int     `json:"tributeCharacterExpirationSeconds" ini:"TributeCharacterExpirationSeconds"` //TODO: Usage unknown in asa
	TributeDinoExpirationSeconds      int     `json:"tributeDinoExpirationSeconds" ini:"TributeDinoExpirationSeconds"`
	TributeItemExpirationSeconds      int     `json:"tributeItemExpirationSeconds" ini:"TributeItemExpirationSeconds"`
	// Cryo sickness and cryopod nerf
	//CryopodNerfDamageMult float32 `json:"cryopodNerfDamageMult" ini:"CryopodNerfDamageMult"` //TODO: Usage unknown in asa
	//CryopodNerfDuration int `json:"cryopodNerfDuration" ini:"CryopodNerfDuration"` //TODO: Usage unknown in asa
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
	//AllowedCheatersURL              string  `json:"allowedCheatersURL" ini:"AllowedCheatersURL"`
	ChatLogFileSplitIntervalSeconds int     `json:"chatLogFileSplitIntervalSeconds" ini:"ChatLogFileSplitIntervalSeconds"`
	ChatLogFlushIntervalSeconds     int     `json:"chatLogFlushIntervalSeconds" ini:"ChatLogFlushIntervalSeconds"`
	ChatLogMaxAgeInDays             int     `json:"chatLogMaxAgeInDays" ini:"ChatLogMaxAgeInDays"`
	DontRestoreBackup               bool    `json:"dontRestoreBackup" ini:"DontRestoreBackup"`
	EnableAFKKickPlayerCountPercent float32 `json:"enableAFKKickPlayerCountPercent" ini:"EnableAFKKickPlayerCountPercent"`
	EnableMeshBitingProtection      bool    `json:"enableMeshBitingProtection" ini:"EnableMeshBitingProtection"`
	FreezeReaperPregnancy           bool    `json:"freezeReaperPregnancy" ini:"FreezeReaperPregnancy"`
	LogChatMessages                 bool    `json:"logChatMessages" ini:"LogChatMessages"`
	MaxStructuresInSmallRadius      int     `json:"maxStructuresInSmallRadius" ini:"MaxStructuresInSmallRadius"`
	MaxStructuresToProcess          int     `json:"maxStructuresToProcess" ini:"MaxStructuresToProcess"`
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
	Port        int    `json:"port" ini:"Port"`
	QueryPort   int    `json:"queryPort" ini:"QueryPort"`
	SessionName string `json:"sessionName" ini:"SessionName"`
}

type MultiHome struct {
	MultiHome bool `json:"multiHome" ini:"MultiHome"`
}

type Ragnarok struct {
	AllowMultipleTamedUnicorns bool    `json:"allowMultipleTamedUnicorns" ini:"AllowMultipleTamedUnicorns"`
	EnableVolcano              bool    `json:"enableVolcano" ini:"EnableVolcano"`
	UnicornSpawnInterval       int     `json:"unicornSpawnInterval" ini:"UnicornSpawnInterval"`
	VolcanoIntensity           float32 `json:"volcanoIntensity" ini:"VolcanoIntensity"`
	VolcanoInterval            int     `json:"volcanoInterval" ini:"VolcanoInterval"`
}

type MessageOfTheDay struct {
	Duration int    `json:"duration" ini:"Duration"`
	Message  string `json:"message" ini:"Message"`
}

type ScriptEngineGameSession struct {
	MaxPlayers int `json:"maxPlayers" ini:"MaxPlayers"`
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

func generateNewDefaultGameUserSettings() GameUserSettings {
	return GameUserSettings{
		ServerSettings: ServerSettings{
			ActiveMods:                                 "",
			AdminLogging:                               false,
			AllowAnyoneBabyImprintCuddle:               false,
			AllowCaveBuildingPvE:                       false,
			AllowCaveBuildingPvP:                       false,
			AllowCrateSpawnsOnTopOfStructures:          false,
			AllowFlyerCarryPvE:                         false,
			AllowFlyingStaminaRecovery:                 false,
			AllowHideDamageSourceFromLogs:              true,
			AllowHitMarkers:                            true,
			AllowIntegratedSPlusStructures:             true,
			AllowMultipleAttachedC4:                    false,
			AllowRaidDinoFeeding:                       false,
			AllowSharedConnections:                     false,
			AllowTekSuitPowersInGenesis:                false,
			AllowThirdPersonPlayer:                     true,
			AlwaysAllowStructurePickup:                 false,
			AlwaysNotifyPlayerLeft:                     false,
			AutoDestroyDecayedDinos:                    false,
			AutoDestroyOldStructuresMultiplier:         0.0,
			AutoSavePeriodMinutes:                      15.0,
			BanListURL:                                 "http://arkdedicated.com/banlist.txt",
			ClampItemSpoilingTimes:                     false,
			ClampItemStats:                             false,
			ClampResourceHarvestDamage:                 false,
			DayCycleSpeedScale:                         1.0,
			DayTimeSpeedScale:                          1.0,
			DifficultyOffset:                           1.0,
			DinoCharacterFoodDrainMultiplier:           1.0,
			DinoCharacterHealthRecoveryMultiplier:      1.0,
			DinoCharacterStaminaDrainMultiplier:        1.0,
			DinoCountMultiplier:                        1.0,
			DinoDamageMultiplier:                       1.0,
			DinoResistanceMultiplier:                   1.0,
			DisableDinoDecayPvE:                        false,
			DisableImprintDinoBuff:                     false,
			DisablePvEGamma:                            false,
			DisableStructureDecayPvE:                   false,
			DisableWeatherFog:                          false,
			DontAlwaysNotifyPlayerJoined:               false,
			EnableExtraStructurePreventionVolumes:      false,
			EnablePvPGamma:                             false,
			FastDecayUnsnappedCoreStructures:           false,
			ForceAllStructureLocking:                   false,
			GlobalVoiceChat:                            false,
			HarvestAmountMultiplier:                    1.0,
			HarvestHealthMultiplier:                    1.0,
			IgnoreLimitMaxStructuresInRangeTypeFlag:    false,
			ItemStackSizeMultiplier:                    1.0,
			KickIdlePlayersPeriod:                      3600.0,
			MaxGateFrameOnSaddles:                      0,
			MaxHexagonsPerCharacter:                    2000000000,
			MaxPersonalTamedDinos:                      0,
			MaxPlatformSaddleStructureLimit:            75,
			MaxTamedDinos:                              5000.0,
			MaxTributeCharacters:                       10,
			MaxTributeDinos:                            20,
			MaxTributeItems:                            50,
			NightTimeSpeedScale:                        1.0,
			NonPermanentDiseases:                       false,
			NPCNetworkStasisRangeScalePlayerCountStart: 0,
			NPCNetworkStasisRangeScalePlayerCountEnd:   0,
			NPCNetworkStasisRangeScalePercentEnd:       0.55000001,
			OnlyAutoDestroyCoreStructures:              false,
			OnlyDecayUnsnappedCoreStructures:           false,
			OverrideOfficialDifficulty:                 0.0,
			OverrideStructurePlatformPrevention:        false,
			OxygenSwimSpeedStatMultiplier:              1.0,
			PerPlatformMaxStructuresMultiplier:         1.0,
			PersonalTamedDinosSaddleStructureCost:      0,
			PlatformSaddleBuildAreaBoundsMultiplier:    1.0,
			PlayerCharacterFoodDrainMultiplier:         1.0,
			PlayerCharacterHealthRecoveryMultiplier:    1.0,
			PlayerCharacterStaminaDrainMultiplier:      1.0,
			PlayerCharacterWaterDrainMultiplier:        1.0,
			PlayerDamageMultiplier:                     1.0,
			PlayerResistanceMultiplier:                 1.0,
			PreventDiseases:                            false,
			PreventMateBoost:                           false,
			PreventOfflinePvP:                          false,
			PreventOfflinePvPInterval:                  0.0,
			PreventSpawnAnimations:                     false,
			PreventTribeAlliances:                      false,
			ProximityChat:                              false,
			PvEAllowStructuresAtSupplyDrops:            false,
			PvEDinoDecayPeriodMultiplier:               1.0,
			PvEStructureDecayPeriodMultiplier:          1.0,
			PvPDinoDecay:                               false,
			PvPStructureDecay:                          false,
			RaidDinoCharacterFoodDrainMultiplier:       1.0,
			RandomSupplyCratePoints:                    false,
			RCONEnabled:                                true,
			RCONPort:                                   27015, //TODO set value correct on server save
			RCONServerGameLogBuffer:                    600.0,
			ResourcesRespawnPeriodMultiplier:           1.0,
			ServerAdminPassword:                        "bob", //TODO set value correct on server save
			ServerAutoForceRespawnWildDinosInterval:    0.0,
			ServerCrosshair:                            true,
			ServerForceNoHUD:                           false,
			ServerHardcore:                             false,
			//ServerPassword:                             "", //Make sure this is set correctly
			ServerPVE:                                false,
			ShowFloatingDamageText:                   false,
			ShowMapPlayerLocation:                    true,
			StructureDamageMultiplier:                1.0,
			StructurePickupHoldDuration:              0.5,
			StructurePickupTimeAfterPlacement:        30.0,
			StructurePreventResourceRadiusMultiplier: 1.0,
			StructureResistanceMultiplier:            1.0,
			TamedDinoDamageMultiplier:                1.0,
			TamedDinoResistanceMultiplier:            1.0,
			TamingSpeedMultiplier:                    1.0,
			TheMaxStructuresInRange:                  10500,
			TribeLogDestroyedEnemyStructures:         false,
			TribeNameChangeCooldown:                  15.0,
			UseFjordurTraversalBuff:                  false,
			UseOptimizedHarvestingHealth:             false,
			XPMultiplier:                             1.0,

			CrossARKAllowForeignDinoDownloads: false,
			MinimumDinoReuploadInterval:       0.0,
			NoTributeDownloads:                false,
			PreventDownloadDinos:              false,
			PreventDownloadItems:              false,
			PreventDownloadSurvivors:          false,
			PreventUploadDinos:                false,
			PreventUploadItems:                false,
			PreventUploadSurvivors:            false,
			TributeCharacterExpirationSeconds: 0,
			TributeDinoExpirationSeconds:      86400,
			TributeItemExpirationSeconds:      86400,
			BadWordListURL:                    "http://arkdedicated.com/badwords.txt",
			BadWordWhiteListURL:               "http://arkdedicated.com/goodwords.txt",
			BFilterCharacterNames:             false,
			BFilterChat:                       false,
			BFilterTribeNames:                 false,
			ChatLogFileSplitIntervalSeconds:   86400,
			ChatLogFlushIntervalSeconds:       86400,
			ChatLogMaxAgeInDays:               5,
			DontRestoreBackup:                 false,
			EnableAFKKickPlayerCountPercent:   0.0,
			EnableMeshBitingProtection:        true,
			FreezeReaperPregnancy:             false,
			LogChatMessages:                   false,
			MaxStructuresInSmallRadius:        0,
			MaxStructuresToProcess:            0,
			PreventOutOfTribePinCodeUse:       false,
			RadiusStructuresInSmallRadius:     0.0,
			ServerEnableMeshChecking:          true,
			TribeMergeAllowed:                 true,
			TribeMergeCooldown:                0.0,
			UpdateAllowedCheatersInterval:     600.0,
			UseExclusiveList:                  false,
		},

		SessionSettings: SessionSettings{
			MultiHome:   "0.0.0.0", //TODO set value correct on server save
			Port:        7777,      //TODO set value correct on server save
			QueryPort:   28015,     //TODO set value correct on server save
			SessionName: "Ark Ascended server managed by AASM",
		},

		MultiHome: MultiHome{
			MultiHome: true, //TODO set value correct on server save
		},

		ScriptEngineGameSession: ScriptEngineGameSession{
			MaxPlayers: 70, //TODO set value correct on server save
		},

		Ragnarok: Ragnarok{
			AllowMultipleTamedUnicorns: false,
			EnableVolcano:              true,
			UnicornSpawnInterval:       24,
			VolcanoIntensity:           1,
			VolcanoInterval:            0,
		},

		MessageOfTheDay: MessageOfTheDay{
			Duration: 20,
			Message:  "",
		},
	}

}

// SaveGameUserSettingsIni saves the game user settings to the ini file in the server directory
func (s *Server) SaveGameUserSettingsIni() error {

	ini.PrettyFormat = false

	gusIni := ini.Empty()

	gusIni, err := ini.Load(filepath.Join(s.ServerPath, "ShooterGame\\Saved\\Config\\WindowsServer\\GameUserSettings.ini"))
	if err != nil {
		return err
	}

	err = gusIni.ReflectFrom(&s.GameUserSettings)
	if err != nil {
		return err
	}

	err = gusIni.SaveTo(filepath.Join(s.ServerPath, "ShooterGame\\Saved\\Config\\WindowsServer\\GameUserSettings.ini"))
	if err != nil {
		return err
	}

	return nil
}
