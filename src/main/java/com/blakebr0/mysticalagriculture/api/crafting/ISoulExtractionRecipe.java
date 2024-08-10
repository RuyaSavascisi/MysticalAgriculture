package com.blakebr0.mysticalagriculture.api.crafting;

import com.blakebr0.mysticalagriculture.api.soul.MobSoulType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;

/**
 * Used to represent a Reprocessor recipe for the recipe type
 */
public interface ISoulExtractionRecipe extends Recipe<RecipeInput> {
    MobSoulType getMobSoulType();
    double getSouls();
}
