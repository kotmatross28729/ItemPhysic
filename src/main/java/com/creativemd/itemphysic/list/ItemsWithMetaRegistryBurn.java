package com.creativemd.itemphysic.list;

import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemsWithMetaRegistryBurn {
    public static List<ItemWithMetaBurn> BurnItems = new ArrayList<>();
    public static class ItemWithMetaBurn {
        public final Item item;
        public final int metadata;
        public ItemWithMetaBurn(Item item, int metadata) {
            this.item = item;
            this.metadata = metadata;
        }
    }
}
