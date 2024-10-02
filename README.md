### ItemPhysic 1.7.10 Kotmatross fork
A minecraft forge mod that adds some physic to thrown items.

---

#### Dependencies:
- [CreativeCore GTNH version](https://github.com/GTNewHorizons/CreativeCore/releases)
  - You can use the official version, but I don't recommend it

---
### Changes with the original version:


#### HRudyPlayZ:
- Added some settings related to the burning items list. You can choose which items will or will not burn in fire/lava etc.

  - You can also invert this setting.

- Added some settings related to floating items list. You can choose which items will or will not float in liquids.

  - You can also invert this setting.

- Made the config look better, with descriptions for each option.

- Added a fancy color format in the mod list.

---
#### My (Kotmatross):

- Fixed a bug where the item pickup panel wouldn't scale to fit the screen size


- Rewritten the entire list system (burnList and floatList) from scratch (fixes a LOT of bugs)
  - Added OreDictionary support (you can add huge groups of items without adding each one separately)

  - Added item metadata support (for greater accuracy)

  - Added forge fluid system support (for floatList)

  - Added default support for some vanilla items, as well as Thaumcraft and etfuturum

  -  Added patch for `OreDictionary.WILDCARD_VALUE`

- Fully formatted README


- Fixed floating items spinning very fast when they reach the surface


- Fixed item despawn option not working


- Backported items slowing down in the cobweb


- Added a config option to disable "Power:" text above HUD && make "Power:" translatable


- Fixed item textures not being hidden if the item has a 3D model


- Fixed explosive/hydroactive hazards from hbm's ntm not working


- Added config option responsible for the resistance of items to cactus


- Added List of items that are explosion resistant


- Added List of items that are undestroyable


- Added List of items that are sulfuric acid-resistant (hbm's ntm)

---

## Documentation on lists:

### parameters:


| Parameter name | Type                 | Default            | Example                                                                                                                |
|----------------|----------------------|--------------------|------------------------------------------------------------------------------------------------------------------------|
| `modid`        | String (abc)         | -                  | `minecraft` / `etfuturum` / `hbm` / `Thaumcraft`                                                                       |
| `itemname`     | String (abc)         | -                  | stick / beetroot / item.part_generic / blockCustomOre                                                                  |
| `metadata`     | Int (123)            | 0                  | 0 / 4 / 1 / 5                                                                                                          |
| `ignoremeta`   | Boolean (true/false) | false              | true / false                                                                                                           |
| `fluid`        | String (abc)         | `fluid.tile.water` | if vanilla (water/lava) - `fluid.tile.water` `fluid.tile.lava`, else (thermal foundation, etc) - `fluid.redstone`, etc |




### burnList:

**Minimum syntax:**

* `modid:itemname`
  - *Example:* `minecraft:blaze_rod`

Means that item "itemname" from "modid" will NOT burn in lava

**Maximum syntax:**

* 1)`modid:itemname:metadata`
  - *Example:* `minecraft:golden_apple:1` //enchanted

OR

* 2)`modid:itemname:ignoremeta`
  - *Example:* `etfuturum:netherite_sword:true`

Means that item "itemname" from "modid" : 1)with metadata "metadata" 2)with any metadata will NOT burn in lava

With `B:invertBurnList=true` everything is exactly the opposite: the items specified in this list will be the only ones that WILL burn

(it follows that absolutely all other items not on this list will NOT burn)

### floatList:

**Minimum syntax:**

* `modid:itemname`
  - *Example:* `minecraft:stick`

Means that item "itemname" from "modid" will float in water

**Maximum syntax:**

* 1)`modid:itemname:metadata:fluids[]...`
  - *Example:* `etfuturum:tipped_arrow:8227:fluid.tile.lava`

OR

* 2)`modid:itemname:ignoremeta:fluids[]...`
  - *Example:* `etfuturum:netherite_sword:true:fluid.tile.lava:fluid.redstone`

Means that item "itemname" from "modid": 1)with metadata "metadata" 2)with any metadata WILL float in fluids "fluids[]"

With `B:invertFloatList=true` everything is exactly the opposite: the items specified in this list will be the only ones that will NOT float in the specified fluids.

(it follows that absolutely all other items not on this list WILL float in all fluids)

**Note when specifying fluids: if it's vanilla water/lava, use `fluid.tile.water` or `fluid.tile.lava`. When specifying mod fluids, just use `fluid.fluidname` (without .tile)**


### For explosionList / undestroyableList / sulfuricAcidList the syntax is exactly the same as for burnList

---


#### TODO:

| Name of TODO feature                                                                 | Priority | State |
|--------------------------------------------------------------------------------------|----------|-------|
| Fix item despawn option not working                                                  | High     | ✅     |
| Fix floating items spinning eternally (very fast) when they reach the surface        | Medium   | ✅     |
| Add a config option to disable "Power:" text above HUD && make "Power:" translatable | Medium   | ✅     |
| Fix explosive/hydroactive hazards from hbm's ntm not working                         | High     | ✅     |
| Fix item textures not being hidden if the item has a 3D model                        | High     | ✅     |
| Add List of items that are explosion resistant                                       | High     | ✅     |
| Add List of items that are undestroyable (good luck getting rid of them)             | High     | ✅     |
| Add config option responsible for the resistance of items to cactus                  | High     | ✅     |
| Add List of items that are sulfuric acid-resistant (hbm's ntm)                       | High     | ✅     |
| Backport igniting items                                                              | Medium   | ❌     |
| Backport item fall sounds                                                            | Medium   | ❌     |
| Backport show tooltip option                                                         | Medium   | ❌     |
---

### In the future:

| Name of feature                            | Priority | State |
|--------------------------------------------|----------|-------|
| Switch to mixins                           | Low      | ❌     |
| Fix items spinning on thaumcraft pedestals | Low      | ❌     |

