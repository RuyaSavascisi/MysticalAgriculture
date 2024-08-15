package com.blakebr0.mysticalagriculture.handler;

import com.blakebr0.cucumber.event.ItemBreakEvent;
import com.blakebr0.mysticalagriculture.api.tinkering.ITinkerable;
import com.blakebr0.mysticalagriculture.api.util.AugmentUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;

public final class TinkerableHandler {
    @SubscribeEvent
    public void onItemBreak(ItemBreakEvent event) {
        var item = event.getItem();
        var entity = event.getEntity();

        if (item instanceof ITinkerable && entity instanceof Player player) {
            var stack = event.getItemStack();
            var augments = AugmentUtils.getAugments(stack);

            for (var augment : augments) {
                player.getInventory().placeItemBackInInventory(new ItemStack(augment.getItem()));
            }
        }
    }
}
