# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/) and this project adheres to [Forge Recommended Versioning](https://mcforge.readthedocs.io/en/latest/conventions/versioning/).

## [1.21.3-4.3.0.1] - 2024-02-22
### Fixed
- solar cooker lid was moving when a recipe was finished although a next recipe is available
- solar cooker opening sound was played after world loading after an active recipe was finished although a next recipe is available
- item names were not displayed correctly

## [1.21.3-4.3.0.0] - 2024-02-19
### Changed
- Updated to Minecraft 1.21.3 (Fabric 0.107.0+1.21.3, NeoForge 21.3.56, Forge 53.0.47)
- Updated Cloth Config support (16.0.141) (Fabric/Quilt)
- Updated ModMenu support (12.0.0) (Fabric/Quilt)
- updated REI support (17.0.789) (Fabric/Quilt & NeoForge)
- temporary removed JEI support until it is ported to 1.21.3 (Forge & NeoForge)
- temporary removed The One Probe support until it is ported to 1.21.3 (NeoForge)

## [1.21.1-4.2.0.0] - 2024-10-27
### Changed
- updated to Minecraft 1.21.1 (Fabric 0.105.0+1.21.1, Neoforge 21.1.62, Forge 52.0.21)
- updated Cloth Config support (15.0.140) (Fabric/Quilt)
- updated ModMenu support (11.0.2) (Fabric/Quilt)
- updated JEI support (19.19.0.219) (Forge & NeoForge)
- updated REI support (16.0.788) (Fabric/Quilt & NeoForge)
- updated The One Probe support (1.21_neo-12.0.3) (NeoForge)

### Fixed
- fixed JEI loading errors since 19.19.0.219 (all loaders)
- fixed recipes and advancements by using the new common tags in Forge

## [1.21-4.1.1.0] - 2024-08-30
### Changed
- number config options are now text fields instead of sliders (Fabric)

## [1.21-4.1.0.0] - 2024-07-14
### Changed
- updated NeoForge to 21.0.94-beta
- the `config` directory is used for the default configuration (NeoForge)

### Fixed
- crashed on startup with NeoForge (caused by a breaking change in 21.0.82-beta) (thanks to unspunreality for the report) #26

## [1.21-4.0.0.1] - 2024-07-08
### Fixed
- removed unknown recipe warning from log (all loaders) 

## [1.21-4.0.0.0] - 2024-07-07
### Changed
- updated to Minecraft 1.21 (Fabric 0.100.3+1.21, Neoforge 21.0.42-beta, Forge 51.0.18)
- updated Cloth Config support (15.0.127) (Fabric/Quilt)
- updated ModMenu support (11.0.1) (Fabric/Quilt)
- updated JEI support (19.0.0.11) (all loaders)
- updated REI support (16.0.729) (Fabric/Quilt & NeoForge)
- updated The One Probe support (1.21_neo-12.0.0) (NeoForge)
- reflectors can now be placed in the Solar Cooker GUI and are rendered as additional lids
- default reflector speed factor was raised to 0.5 (former value was 0.25) (configurable)
- default recipe type of the Solar Cooker was changed to "smelting" (former value was "smoking") (configurable)

### Removed
- reflector blocks were removed (they will disappear when you update your world from an older version) 

## [1.20.6-3.2.0.0] - 2024-06-30
### Changed
- updated to Minecraft 1.20.6 (Fabric 0.98.0+1.20.6, NeoForge 20.6.119, Forge 50.1.9)
- updated Cloth Config support (14.0.126) (Fabric/Quilt)
- updated ModMenu support (10.0.0) (Fabric/Quilt)
- updated JEI support (18.0.0.65) (Forge & NeoForge)
- updated REI support (15.0.728) (Fabric/Quilt & NeoForge)
- updated The One Probe support (1.20.5_neo-11.1.1) (NeoForge)

### Removed
- removed REI support for Forge

## [1.20.4-3.1.2.1] - 2024-06-04
### Fixed
- fixed version checking at mod loader startup for Roughly Enough Items (all loaders)

## [1.20.4-3.1.2.0] - 2024-06-04
### Added
- added Roughly Enough Items (REI) support (version 14.1.727) (all loaders)

## [1.20.4-3.1.1.0] - 2024-05-11
### Added
- registering ItemHandlers to be more compatible with other mods (NeoForge)

### Fixed
- Shining Block of Diamond recipe advancement triggered with first inventory change (Fabric/Quilt)

## [1.20.4-3.1.0.0] - 2024-04-27
### Added
- add Fabric (>=0.96.11+1.20.4) support (Fabric, Quilt)

## [1.20.4-3.0.0.0] - 2024-04-08
### Changed
- updated to Minecraft 1.20.4 (Forge 49.0.38, Neoforge 20.4.225)
- updated JEI support to 1.20.4-17.3.0.49 (Forge & Neoforge)
- updated The One Probe support to 1.20.4_neo-11.0.2-3 (Neoforge)
- rename config option "recipeBlacklist" to "recipeBlockedList"

## [1.20.2-2.2.0.0] - 2023-10-26
### Changed
- update and move back to Forge 1.20.2-48.0.23 (from NeoForge) until it is stable
- Temporary deactivation of The One Probe support until it is ported to 1.20.2
- deactivate game tests, because they are not working yet

## [1.20.1-2.1.0.0] - 2023-08-09
### Changed
- Changed Forge to NeoForge 1.20.1-47.1.54 (compatible with Forge 47.1.0)
- Updated compat with JEI to 1.20.1-15.2.0.23
- Updated compat with The One Probe to 1.20.1-10.0.1

## [1.20.1-2.0.0.0] - 2023-06-19
### Changed
- Update mod to Forge 1.20.1-47.0.1 #21
- Update compat with JEI to 1.20.1-15.0.0.17 #21
- Update compat with The One Probe to 1.20.0-9.0.0 #21

## [1.19.4-1.4.0.0] - 2023-04-04
### Changed
- Update mod to Forge 1.19.4-45.0.40
- Update compat with The One Probe to 1.19.4-8.0.0
- Update compat with JEI to 1.19.4-13.0.0.1

## [1.19.3-1.3.2.1] - 2023-01-18
### Fixed
- fix startup issue with mod "REI Plugin Compatibilities (REIPC)" version 10.0.45 (thanks to subsonicer for the report)

## [1.19.3-1.3.2.0] - 2023-01-17
### Added
- re-add JEI support

## [1.19.3-1.3.1.0] - 2023-01-02
### Added
- Added pt_br language support (thanks to FITFC) #19

## [1.19.3-1.3.0.0] - 2022-12-30
### Changed
- Update mod to Forge 1.19.3-44.0.41
- Changed shining block of diamond recipe to use the diamond storage block tag of forge
- Update compat with The One Probe to 1.19.3-7.0.0
- Temporary deactivation of JEI support until it is ported to 1.19.3

## [1.19-1.2.1.0] - 2022-09-20
### Added
- The One Probe support - Show cooking time of Solar Cooker

## [1.19-1.2.0.0] - 2022-07-18
### Changed
- Update mod to Forge 1.19-41.0.96

## [1.18.2-1.1.1.1] - 2022-06-13
### Added
- Chinese (zh_cn) language support #7 (thanks to rentommy)

## [1.18.2-1.1.1.0] - 2022-04-30
### Changed
- Update mod to Forge 1.18.2-40.0.7

### Fixed
- Fix compatibility with JEI 1.18.2-9.7.0.192 and later (startup crash)

## [1.18.2-1.1.0.0] - 2022-03-03
### Changed
- Update mod to Forge 1.18.2-40.0.2 (fix Log4J security issue)

## [1.18-1.0.2.0] - 2021-12-10
### Changed
- Update mod to Forge 1.18-38.0.17 (fix Log4J security issue)

### Fixed
- Hitbox change allowed player to fall throw #13 (thanks to JadeCrystalCat for the report)

## [1.18-1.0.1.2] - 2021-12-05
### Added
- jei compatibility readded

### Changed
- update to Forge 1.18-38.0.14

## [1.18-1.0.1.1] - 2021-12-04
### Changed
- port to Forge 1.18-38.0.6
- jei compatibility temporary deactivated

## [1.17.1-1.0.1.1] - 2021-08-24
### Added
- jei compatibility readded

## [1.17.1-1.0.1.0] - 2021-08-12
### Changed
- updated to Forge 1.17.1-37.0.32

### Fixed
- Startup issue since Forge 1.17.1-37.0.31
- Solar Cooker, Reflector, Shining Block of Diamond had no effective tool

## [1.17.1-1.0.0.2] - 2021-08-03 
### Added
- Russian and Ukrainian translation (thanks to vstannumdum aka DMHYT)

### Changed
- port to 1.17.1
- jei compatibility temporary deactivated
- automatic tests temporary deactivated

## [1.16.5-1.0.0.1] - 2021-07-20
### Changed
- Multiple Reflectors can now be placed into one block to face in multiple directions.
- Attention: All Reflector blocks are facing to north after updating from an older mod version (sorry for that :/)
- Update mod to Forge 1.16.5-36.1.0
- Use assemble method instead of getResultItem in tile entity (see Forge 1.16.5-36.1.51)
- changed versioning to fit [Forge Recommended Versioning](https://mcforge.readthedocs.io/en/latest/conventions/versioning/)
- add some automated tests

## [0.4.0_1.16] - 2021-02-01
### Changed
- Add a Shining Block of Diamond.
- Can be placed above a Solar Cooker to use it independently of the sun.
- Add the tag "solarcooker:solar_cooker_shining" to add other blocks with this functionality.
- The Solar Cooker can now be configured to use vanilla campfire_cooking recipes.

## [0.3.0_1.16] - 2021-01-21
### Changed
- Reflector block added which speed up the cooking time in an adjacent Solar Cooker
- Solar Cooker has now a opening & closing mechanism
- Solar Cooker recipe advancement triggers now for all wooden chests

## [0.2.0_1.16] - 2021-01-18
### Changed
- Render item inside the Solar Cooker
- Solar Cooker drops after breaking it (effective tool: axe)
- Crafting recipe is now more compatible with other mods (uses also colored glass panes & trapped chests)
- Added Solar Cooker as JEI catalyst to better find the recipes
- Change logo

## [0.1.0_1.16] - 2021-01-16
### Changed
* Adds a Solar Cooker which needs sunlight instead of fuel.
