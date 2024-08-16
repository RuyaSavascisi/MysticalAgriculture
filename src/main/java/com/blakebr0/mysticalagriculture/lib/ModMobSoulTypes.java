package com.blakebr0.mysticalagriculture.lib;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.registry.IMobSoulTypeRegistry;
import com.blakebr0.mysticalagriculture.api.soul.MobSoulType;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

import java.util.Arrays;
import java.util.Set;

public final class ModMobSoulTypes {
    private static final boolean DEBUG = false;

    private static final Set<ResourceLocation> FISH_IDS = Sets.newHashSet(ResourceLocation.parse("minecraft:cod"), ResourceLocation.parse("minecraft:salmon"), ResourceLocation.parse("minecraft:tropical_fish"), ResourceLocation.parse("minecraft:pufferfish"));
    private static final Set<ResourceLocation> SLIME_IDS = Sets.newHashSet(ResourceLocation.parse("minecraft:slime"), ResourceLocation.parse("tconstruct:earth_slime"));
    private static final Set<ResourceLocation> ZOMBIE_IDS = Sets.newHashSet(ResourceLocation.parse("minecraft:zombie"), ResourceLocation.parse("minecraft:zombie_villager"));
    private static final Set<ResourceLocation> SPIDER_IDS = Sets.newHashSet(ResourceLocation.parse("minecraft:spider"), ResourceLocation.parse("minecraft:cave_spider"));

    public static final MobSoulType PIG = new MobSoulType(MysticalAgriculture.resource("pig"), ResourceLocation.parse("minecraft:pig"), 8, 15771042);
    public static final MobSoulType CHICKEN = new MobSoulType(MysticalAgriculture.resource("chicken"), ResourceLocation.parse("minecraft:chicken"), 8, 10592673);
    public static final MobSoulType COW = new MobSoulType(MysticalAgriculture.resource("cow"), ResourceLocation.parse("minecraft:cow"), 8, 4470310);
    public static final MobSoulType SHEEP = new MobSoulType(MysticalAgriculture.resource("sheep"), ResourceLocation.parse("minecraft:sheep"), 8, 15198183);
    public static final MobSoulType SQUID = new MobSoulType(MysticalAgriculture.resource("squid"), ResourceLocation.parse("minecraft:squid"), 6, 2243405);
    public static final MobSoulType FISH = new MobSoulType(MysticalAgriculture.resource("fish"), FISH_IDS, "fish", 6, 12691306);
    public static final MobSoulType SLIME = new MobSoulType(MysticalAgriculture.resource("slime"), SLIME_IDS, "slime", 12, 5349438);
    public static final MobSoulType TURTLE = new MobSoulType(MysticalAgriculture.resource("turtle"), ResourceLocation.parse("minecraft:turtle"), 6, 44975);
    public static final MobSoulType ARMADILLO = new MobSoulType(MysticalAgriculture.resource("armadillo"), ResourceLocation.parse("minecraft:armadillo"), 6, 0xb67b76);
    public static final MobSoulType ZOMBIE = new MobSoulType(MysticalAgriculture.resource("zombie"), ZOMBIE_IDS, "zombie", 10, 7969893);
    public static final MobSoulType SKELETON = new MobSoulType(MysticalAgriculture.resource("skeleton"), ResourceLocation.parse("minecraft:skeleton"), 10, 12698049);
    public static final MobSoulType CREEPER = new MobSoulType(MysticalAgriculture.resource("creeper"), ResourceLocation.parse("minecraft:creeper"), 10, 894731);
    public static final MobSoulType SPIDER = new MobSoulType(MysticalAgriculture.resource("spider"), SPIDER_IDS, "spider", 10, 3419431);
    public static final MobSoulType RABBIT = new MobSoulType(MysticalAgriculture.resource("rabbit"), ResourceLocation.parse("minecraft:rabbit"), 6, 10051392);
    public static final MobSoulType BREEZE = new MobSoulType(MysticalAgriculture.resource("breeze"), ResourceLocation.parse("minecraft:breeze"), 4, 0x7982c7);
    public static final MobSoulType BLAZE = new MobSoulType(MysticalAgriculture.resource("blaze"), ResourceLocation.parse("minecraft:blaze"), 10, 16167425);
    public static final MobSoulType GHAST = new MobSoulType(MysticalAgriculture.resource("ghast"), ResourceLocation.parse("minecraft:ghast"), 4, 16382457);
    public static final MobSoulType ENDERMAN = new MobSoulType(MysticalAgriculture.resource("enderman"), ResourceLocation.parse("minecraft:enderman"), 8, 1447446);
    public static final MobSoulType WITHER = new MobSoulType(MysticalAgriculture.resource("wither_skeleton"), ResourceLocation.parse("minecraft:wither_skeleton"), 8, 1315860);

    // THERMAL SERIES
    public static final MobSoulType BLIZZ = new MobSoulType(MysticalAgriculture.resource("blizz"), ResourceLocation.parse("thermal:blizz"), 6, 0x7BD4FF);
    public static final MobSoulType BLITZ = new MobSoulType(MysticalAgriculture.resource("blitz"), ResourceLocation.parse("thermal:blitz"), 6, 0xECFEFC);
    public static final MobSoulType BASALZ = new MobSoulType(MysticalAgriculture.resource("basalz"), ResourceLocation.parse("thermal:basalz"), 6, 0x363840);

    public static void onRegisterMobSoulTypes(IMobSoulTypeRegistry registry) {
        registry.register(PIG);
        registry.register(CHICKEN);
        registry.register(COW);
        registry.register(SHEEP);
        registry.register(SQUID);
        registry.register(FISH);
        registry.register(SLIME);
        registry.register(TURTLE);
        registry.register(ARMADILLO);
        registry.register(ZOMBIE);
        registry.register(SKELETON);
        registry.register(CREEPER);
        registry.register(SPIDER);
        registry.register(RABBIT);
        registry.register(BREEZE);
        registry.register(BLAZE);
        registry.register(GHAST);
        registry.register(ENDERMAN);
        registry.register(WITHER);

        registry.register(withRequiredMods(BLIZZ, "thermal"));
        registry.register(withRequiredMods(BLITZ, "thermal"));
        registry.register(withRequiredMods(BASALZ, "thermal"));
    }

    private static MobSoulType withRequiredMods(MobSoulType type, String... mods) {
        if (DEBUG) return type;

        boolean enabled = Arrays.stream(mods).anyMatch(ModList.get()::isLoaded);
        return type.setEnabled(enabled);
    }
}
