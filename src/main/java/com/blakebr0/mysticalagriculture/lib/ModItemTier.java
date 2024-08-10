package com.blakebr0.mysticalagriculture.lib;

import com.blakebr0.mysticalagriculture.init.ModItems;
import com.google.common.base.Suppliers;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public enum ModItemTier implements Tier {
    INFERIUM(ModTags.INCORRECT_FOR_INFERIUM_TOOL, 2000, 9.0F, 4.0F, 12, () -> Ingredient.of(ModItems.INFERIUM_INGOT.get())),
    PRUDENTIUM(ModTags.INCORRECT_FOR_PRUDENTIUM_TOOL, 2800, 11.0F, 6.0F, 14, () -> Ingredient.of(ModItems.PRUDENTIUM_INGOT.get())),
    TERTIUM(ModTags.INCORRECT_FOR_TERTIUM_TOOL, 4000, 14.0F, 9.0F, 16, () -> Ingredient.of(ModItems.TERTIUM_INGOT.get())),
    IMPERIUM(ModTags.INCORRECT_FOR_IMPERIUM_TOOL, 6000, 19.0F, 13.0F, 18, () -> Ingredient.of(ModItems.IMPERIUM_INGOT.get())),
    SUPREMIUM(ModTags.INCORRECT_FOR_SUPREMIUM_TOOL, -1, 25.0F, 20.0F, 20, () -> Ingredient.of(ModItems.SUPREMIUM_INGOT.get())),
    AWAKENED_SUPREMIUM(ModTags.INCORRECT_FOR_AWAKENED_SUPREMIUM_TOOL, -1, 30.0F, 25.0F, 22, () -> Ingredient.of(ModItems.AWAKENED_SUPREMIUM_INGOT.get())),
    SOULIUM(ModTags.INCORRECT_FOR_SOULIUM_TOOL, 400, 5.0F, 3.0F, 15, () -> Ingredient.of(ModItems.SOULIUM_INGOT.get()));

    private final TagKey<Block> incorrectBlocksForDrops;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairMaterial;

    ModItemTier(TagKey<Block> incorrectBlocksForDrops, int maxUses, float efficiency, float attackDamage, int enchantability, Supplier<Ingredient> repairMaterial) {
        this.incorrectBlocksForDrops = incorrectBlocksForDrops;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairMaterial = Suppliers.memoize(repairMaterial::get);
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return this.incorrectBlocksForDrops;
    }

    @Override
    public int getUses() {
        return this.maxUses;
    }

    @Override
    public float getSpeed() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }
}
