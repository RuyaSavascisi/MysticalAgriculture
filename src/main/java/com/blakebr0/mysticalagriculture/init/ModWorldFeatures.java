package com.blakebr0.mysticalagriculture.init;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.world.feature.SoulstoneFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModWorldFeatures {
    public static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(Registries.FEATURE, MysticalAgriculture.MOD_ID);

    public static final DeferredHolder<Feature<?>, Feature<OreConfiguration>> SOULSTONE = REGISTRY.register("soulstone", () -> new SoulstoneFeature(OreConfiguration.CODEC));
}
