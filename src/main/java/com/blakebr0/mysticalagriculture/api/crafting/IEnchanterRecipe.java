package com.blakebr0.mysticalagriculture.api.crafting;

import net.minecraft.core.Holder;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Used to represent am Enchanter recipe for the recipe type
 */
public interface IEnchanterRecipe extends Recipe<RecipeInput> {
    Holder<Enchantment> getEnchantment();

    /**
     * Get the count for the ingredient at the requested index
     * @param index the ingredient index
     * @return either the count or -1 if invalid
     */
    int getCount(int index);
}
