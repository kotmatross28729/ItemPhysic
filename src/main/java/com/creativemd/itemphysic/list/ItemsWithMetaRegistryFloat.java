package com.creativemd.itemphysic.list;

import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemsWithMetaRegistryFloat {
    public static List<ItemWithMetaFloat> FloatItems = new ArrayList<>();
    public static class ItemWithMetaFloat {
        public final Item item;
        public final int metadata;
        public ItemWithMetaFloat(Item item, int metadata) {
            this.item = item;
            this.metadata = metadata;
        }
    }
}
