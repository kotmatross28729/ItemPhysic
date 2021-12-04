### ItemPhysic
A minecraft forge mod that adds some physic to thrown items. 
This is the 1.7.10 branch, as of now, i do not plan on working on other versions, no need to ask.
For any other version, please check out [the original repo](https://github.com/CreativeMD/ItemPhysic).

This release is mainly made for my modpack, feel free to report any incompatibility you find.

#### Dependencies:
- [CreativeCore](https://github.com/CreativeMD/CreativeCore/tree/1.7.10)
- [In-Game Config Manager](https://github.com/CreativeMD/IGCM/tree/1.7.10) (optional)

#### TODO:
- ~~Add a config to change the swimItem list (the one that determines if an item should float).~~
- ~~Add a config to change the burnItem list (the one that determines if an item should burn when thrown in fire/lava).~~
- ~~Add a config to invert either one of, or all those lists. (Some people don't want items to be fireproof by default)~~
- ~~Fix the build.gradle to correctly generate the manifest file, and to stop using the eclipse folder for .minecraft (Sorry, you will have to deal with it for now).~~
- ~~Fix lists even more (some items still seem to be hardcoded somehow)~~

- Add in-game config manager support for all the above.
- Fix floating items spinning eternally when they reach the surface
- Fix item despawn option not working.
- Maybe backport some functions to be able to choose stuff like if an item should be cactus-resistant from newer versions.
