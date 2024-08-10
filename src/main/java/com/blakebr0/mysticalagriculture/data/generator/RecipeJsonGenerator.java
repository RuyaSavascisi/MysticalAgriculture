package com.blakebr0.mysticalagriculture.data.generator;

import com.blakebr0.mysticalagriculture.data.recipe.CraftingRecipeBuilder;
import com.blakebr0.mysticalagriculture.data.recipe.InfusionRecipeBuilder;
import com.blakebr0.mysticalagriculture.data.recipe.ReprocessorRecipeBuilder;
import com.blakebr0.mysticalagriculture.lib.ModCrops;
import com.blakebr0.mysticalagriculture.registry.CropRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public class RecipeJsonGenerator extends RecipeProvider {
    public RecipeJsonGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        for (var crop : CropRegistry.getInstance().getCrops()) {
            if (crop != ModCrops.INFERIUM) {
                var craftingId = "seed/crafting/" + crop.getName();
                CraftingRecipeBuilder.newSeedRecipe(crop).build(consumer, ResourceLocation.fromNamespaceAndPath(crop.getModId(), craftingId));

                var infusionId = "seed/infusion/" + crop.getName();
                InfusionRecipeBuilder.newSeedRecipe(crop).build(consumer, ResourceLocation.fromNamespaceAndPath(crop.getModId(), infusionId));
            }

            var reprocessorId = "seed/reprocessor/" + crop.getName();
            ReprocessorRecipeBuilder.newSeedReprocessingRecipe(crop).build(consumer, ResourceLocation.fromNamespaceAndPath(crop.getModId(), reprocessorId));
        }
    }
}
