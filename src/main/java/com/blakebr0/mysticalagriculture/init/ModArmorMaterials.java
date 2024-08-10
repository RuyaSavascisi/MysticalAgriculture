package com.blakebr0.mysticalagriculture.init;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

public final class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> REGISTRY = DeferredRegister.create(Registries.ARMOR_MATERIAL, MysticalAgriculture.MOD_ID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> INFERIUM = REGISTRY.register("inferium", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 4);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 3);
            }),
            12, SoundEvents.ARMOR_EQUIP_GOLD,
            () -> Ingredient.of(ModItems.INFERIUM_INGOT.get()),
            List.of(
                    new ArmorMaterial.Layer(MysticalAgriculture.resource("inferium"), "", true),
                    new ArmorMaterial.Layer(MysticalAgriculture.resource("inferium"), "_overlay", false)
            ),
            2.0F, 0.0F
    ));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PRUDENTIUM = REGISTRY.register("prudentium", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 4);
                map.put(ArmorItem.Type.LEGGINGS, 7);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 4);
            }),
            14, SoundEvents.ARMOR_EQUIP_GOLD,
            () -> Ingredient.of(ModItems.PRUDENTIUM_INGOT.get()),
            List.of(
                    new ArmorMaterial.Layer(MysticalAgriculture.resource("prudentium"), "", true),
                    new ArmorMaterial.Layer(MysticalAgriculture.resource("prudentium"), "_overlay", false)
            ),
            2.25F, 0.0F
    ));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> TERTIUM = REGISTRY.register("tertium", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 5);
                map.put(ArmorItem.Type.LEGGINGS, 8);
                map.put(ArmorItem.Type.CHESTPLATE, 9);
                map.put(ArmorItem.Type.HELMET, 4);
            }),
            16, SoundEvents.ARMOR_EQUIP_GOLD,
            () -> Ingredient.of(ModItems.TERTIUM_INGOT.get()),
            List.of(
                    new ArmorMaterial.Layer(MysticalAgriculture.resource("tertium"), "", true),
                    new ArmorMaterial.Layer(MysticalAgriculture.resource("tertium"), "_overlay", false)
            ),
            2.5F, 0.0F
    ));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> IMPERIUM = REGISTRY.register("imperium", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 5);
                map.put(ArmorItem.Type.LEGGINGS, 8);
                map.put(ArmorItem.Type.CHESTPLATE, 9);
                map.put(ArmorItem.Type.HELMET, 5);
            }),
            18, SoundEvents.ARMOR_EQUIP_GOLD,
            () -> Ingredient.of(ModItems.IMPERIUM_INGOT.get()),
            List.of(
                    new ArmorMaterial.Layer(MysticalAgriculture.resource("imperium"), "", true),
                    new ArmorMaterial.Layer(MysticalAgriculture.resource("imperium"), "_overlay", false)
            ),
            2.75F, 0.0F
    ));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SUPREMIUM = REGISTRY.register("supremium", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 6);
                map.put(ArmorItem.Type.LEGGINGS, 8);
                map.put(ArmorItem.Type.CHESTPLATE, 10);
                map.put(ArmorItem.Type.HELMET, 5);
            }),
            20, SoundEvents.ARMOR_EQUIP_GOLD,
            () -> Ingredient.of(ModItems.SUPREMIUM_INGOT.get()),
            List.of(
                    new ArmorMaterial.Layer(MysticalAgriculture.resource("supremium"), "", true),
                    new ArmorMaterial.Layer(MysticalAgriculture.resource("supremium"), "_overlay", false)
            ),
            3.0F, 0.0F
    ));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> AWAKENED_SUPREMIUM = REGISTRY.register("awakened_supremium", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 8);
                map.put(ArmorItem.Type.LEGGINGS, 10);
                map.put(ArmorItem.Type.CHESTPLATE, 12);
                map.put(ArmorItem.Type.HELMET, 6);
            }),
            22, SoundEvents.ARMOR_EQUIP_GOLD,
            () -> Ingredient.of(ModItems.AWAKENED_SUPREMIUM_INGOT.get()),
            List.of(
                    new ArmorMaterial.Layer(MysticalAgriculture.resource("awakened_supremium"), "", true),
                    new ArmorMaterial.Layer(MysticalAgriculture.resource("awakened_supremium"), "_overlay", false)
            ),
            3.5F, 0.0F
    ));
}
