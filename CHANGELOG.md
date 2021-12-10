# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/) and this project adheres to [Forge Recommended Versioning](https://mcforge.readthedocs.io/en/latest/conventions/versioning/).

## [1.16.5-1.0.2.0] - 2021-12-10
### Changed
- Update mod to Forge 1.16.5-36.2.20 (fix Log4J security issue)

### Fixed
- Hitbox change allowed player to fall throw #13

## [1.16.5-1.0.0.2] - 2021-08-01
### Added
- Russian and Ukrainian translation (thanks to vstannumdum aka DMHYT)

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
