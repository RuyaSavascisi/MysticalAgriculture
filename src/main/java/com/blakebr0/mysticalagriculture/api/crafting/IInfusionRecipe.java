package com.blakebr0.mysticalagriculture.api.crafting;

import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

/**
 * Used to represent an Infusion recipe for the recipe type
 */
public interface IInfusionRecipe extends Recipe<CraftingInput> {
    /**
     * The item that is placed on the Altar
     *
     * @return the altar ingredient
     */
    Ingredient getAltarIngredient();
}
