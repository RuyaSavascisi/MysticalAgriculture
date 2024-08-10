package com.blakebr0.mysticalagriculture.api.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.Optional;

public interface ISouliumSpawnerRecipe extends Recipe<RecipeInput> {
    WeightedRandomList<WeightedEntry.Wrapper<ResourceLocation>> getEntityTypes();
    ResourceLocation getFirstEntityType();
    Optional<WeightedEntry.Wrapper<ResourceLocation>> getRandomEntityType(RandomSource random);

    /**
     * Get the count for the ingredient at the requested index
     * @param index the ingredient index
     * @return either the count or -1 if invalid
     */
    int getCount(int index);
}
