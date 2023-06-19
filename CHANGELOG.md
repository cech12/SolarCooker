# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/) and this project adheres to [Forge Recommended Versioning](https://mcforge.readthedocs.io/en/latest/conventions/versioning/).

## [1.20.1-2.0.0.0] - 2023-06-19
- Update mod to Forge 1.20.1-47.0.1 #21
- Update compat with JEI to 1.20.1-15.0.0.17 #21
- Update compat with The One Probe to 1.20.0-9.0.0 #21

## [1.19.4-1.4.0.0] - 2023-04-04
- Update mod to Forge 1.19.4-45.0.40
- Update compat with The One Probe to 1.19.4-8.0.0
- Update compat with JEI to 1.19.4-13.0.0.1

## [1.19.3-1.3.2.1] - 2023-01-18
### Fixed
- fix startup issue with mod "REI Plugin Compatibilities (REIPC)" version 10.0.45 (thanks to subsonicer for the report)

## [1.19.3-1.3.2.0] - 2023-01-17
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
