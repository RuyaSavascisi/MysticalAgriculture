package com.blakebr0.mysticalagriculture.handler;

import com.blakebr0.mysticalagriculture.api.util.ExperienceCapsuleUtils;
import com.blakebr0.mysticalagriculture.item.ExperienceCapsuleItem;
import com.blakebr0.mysticalagriculture.network.payloads.ExperienceCapsulePickupPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public final class ExperienceCapsuleHandler {
    @SubscribeEvent
    public void onPlayerPickupXp(PlayerXpEvent.PickupXp event) {
        var orb = event.getOrb();
        var player = event.getEntity();
        var capsules = getExperienceCapsules(player);

        if (!capsules.isEmpty()) {
            for (var stack : capsules) {
                int remaining = ExperienceCapsuleUtils.addExperienceToCapsule(stack, orb.getValue());

                orb.value = remaining;

                if (remaining == 0) {
                    orb.discard();

                    PacketDistributor.sendToPlayer((ServerPlayer) player, new ExperienceCapsulePickupPayload());

                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

    private static List<ItemStack> getExperienceCapsules(Player player) {
        var items = new ArrayList<ItemStack>();

        var stack = player.getOffhandItem();
        if (stack.getItem() instanceof ExperienceCapsuleItem)
            items.add(stack);

        player.getInventory().items
                .stream()
                .filter(s -> s.getItem() instanceof ExperienceCapsuleItem)
                .forEach(items::add);

        return items;
    }
}
