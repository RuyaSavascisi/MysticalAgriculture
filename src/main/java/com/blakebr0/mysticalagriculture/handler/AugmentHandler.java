package com.blakebr0.mysticalagriculture.handler;

import com.blakebr0.mysticalagriculture.api.lib.AbilityCache;
import com.blakebr0.mysticalagriculture.api.util.AugmentUtils;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class AugmentHandler {
    private static final AbilityCache ABILITY_CACHE = new AbilityCache();

    @SubscribeEvent
    public void onPlayerUpdate(PlayerTickEvent.Pre event) {
        var player = event.getEntity();

        if (!player.isDeadOrDying()) {
            var level = player.level();
            var augments = AugmentUtils.getArmorAugments(player);

            for (var augment : augments) {
                augment.onPlayerTick(level, player, ABILITY_CACHE);
            }

            for (var augment : ABILITY_CACHE.getCachedAbilities(player)) {
                if (augments.stream().noneMatch(a -> augment.equals(a.getId().toString()))) {
                    ABILITY_CACHE.remove(augment, player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingFall(LivingFallEvent event) {
        var entity = event.getEntity();

        if (entity instanceof Player player) {
            var level = player.level();

            for (var augment : AugmentUtils.getArmorAugments(player)) {
                augment.onPlayerFall(level, player, event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        var player = event.getEntity();

        for (var augment : ABILITY_CACHE.getCachedAbilities(player)) {
            ABILITY_CACHE.removeQuietly(augment, player);
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        var entity = event.getEntity();

        if (entity instanceof Player player) {
            for (var augment : ABILITY_CACHE.getCachedAbilities(player)) {
                ABILITY_CACHE.removeQuietly(augment, player);
            }
        }
    }
}
