package com.creativemd.itemphysic;

import com.creativemd.itemphysic.physics.ServerPhysic;
import com.hbm.util.I18nUtil;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class ItemPhysicHandler {
    @SubscribeEvent
    public void drawTooltip(ItemTooltipEvent event) {
        if (event.itemStack != null) {
            if(ServerPhysic.canItemIgnite(event.itemStack)){
                event.toolTip.add(EnumChatFormatting.GOLD + "[" + I18nUtil.resolveKey("itemphysic.igniting") + "]");
            }
        }
    }
}
