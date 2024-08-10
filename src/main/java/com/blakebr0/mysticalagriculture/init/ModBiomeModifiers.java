package com.blakebr0.mysticalagriculture.init;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.world.modifiers.InferiumOreModifier;
import com.blakebr0.mysticalagriculture.world.modifiers.ProsperityOreModifier;
import com.blakebr0.mysticalagriculture.world.modifiers.SoulstoneModifier;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModBiomeModifiers {
    public static final DeferredRegister<MapCodec<? extends BiomeModifier>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.BIOME_MODIFIER_SERIALIZERS, MysticalAgriculture.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<InferiumOreModifier>> INFERIUM_ORE = REGISTRY.register("inferium_ore", () ->
            RecordCodecBuilder.mapCodec(builder -> builder.group(
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(InferiumOreModifier::biomes),
                    PlacedFeature.CODEC.fieldOf("feature").forGetter(InferiumOreModifier::feature)
            ).apply(builder, InferiumOreModifier::new)));
    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<ProsperityOreModifier>> PROSPERITY_ORE = REGISTRY.register("prosperity_ore", () ->
            RecordCodecBuilder.mapCodec(builder -> builder.group(
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(ProsperityOreModifier::biomes),
                    PlacedFeature.CODEC.fieldOf("feature").forGetter(ProsperityOreModifier::feature)
            ).apply(builder, ProsperityOreModifier::new)));
    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<SoulstoneModifier>> SOULSTONE = REGISTRY.register("soulstone", () ->
            RecordCodecBuilder.mapCodec(builder -> builder.group(
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(SoulstoneModifier::biomes),
                    PlacedFeature.CODEC.fieldOf("feature").forGetter(SoulstoneModifier::feature)
            ).apply(builder, SoulstoneModifier::new)));
}
