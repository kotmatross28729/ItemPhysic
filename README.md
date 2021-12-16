### ItemPhysic
A minecraft forge mod that adds some physic to thrown items. 
This is the 1.7.10 branch, as of now, i do not plan on working on other versions, no need to ask.
For any other version, please check out [the original repo](https://github.com/CreativeMD/ItemPhysic).

This release is mainly made for my modpack, feel free to report any incompatibility you find.

#### Dependencies:
- [CreativeCore](https://github.com/CreativeMD/CreativeCore/tree/1.7.10)
- [In-Game Config Manager](https://github.com/CreativeMD/IGCM/tree/1.7.10) (optional, will disappear later on)



#### Differences with the original version:

- Added some settings related to the burning items list. You can choose which items will or will not burn in fire/lava etc. 

  You can also invert this setting.

- Added some settings related to floating items list. You can choose which items will or will not float in liquids. 

  You can also invert this setting.

- Made the config look better, with descriptions for each option.

- Added a fancy color format in the mod list.

- Upped the version number to 1.1.6

- Updated the workspace to use Forge 1614, Java 8, the latest gradle-wrapper and to work with IntelliJ.

- Removed the requirement to manually edit the META-INF/MANIFEST.MF file after each build.



#### TODO:
- Replace in-game config manager support with the standard forge in-game config. (Might add it back at some point)
- Fix floating items spinning eternally when they reach the surface
- Fix item despawn option not working.
- Maybe backport some functions to be able to choose stuff like if an item should be cactus-resistant from newer versions.
