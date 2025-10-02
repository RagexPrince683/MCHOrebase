package com.ragex.mcheli.helper.client.renderer.color;

import com.ragex.mcheli.MCH_MOD;
import com.ragex.mcheli.mob.MCH_ItemSpawnGunner;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.client.event.ColorHandlerEvent.Item;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(
        modid = "mcheli",
        value = {Side.CLIENT}
)
public class ItemColorRegistration {
    @SubscribeEvent
    static void onRegisterItemColor(Item event) {
        ItemColors itemColors = event.getItemColors();
        itemColors.registerItemColorHandler(
                MCH_ItemSpawnGunner::getColorFromItemStack,
                MCH_MOD.itemSpawnGunnerVsMonster, MCH_MOD.itemSpawnGunnerVsPlayer);
    }
}
