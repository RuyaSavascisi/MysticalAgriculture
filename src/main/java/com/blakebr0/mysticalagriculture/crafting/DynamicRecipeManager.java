package com.blakebr0.mysticalagriculture.crafting;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.cucumber.event.RecipeManagerLoadingEvent;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.blakebr0.mysticalagriculture.config.ModConfigs;
import com.blakebr0.mysticalagriculture.crafting.recipe.InfusionRecipe;
import com.blakebr0.mysticalagriculture.crafting.recipe.ReprocessorRecipe;
import com.blakebr0.mysticalagriculture.registry.CropRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.Optional;

public class DynamicRecipeManager {
    public static final DynamicRecipeManager INSTANCE = new DynamicRecipeManager();

    @SubscribeEvent
    public void onRecipeManagerLoading(RecipeManagerLoadingEvent event) {
        for (var crop : CropRegistry.getInstance().getCrops()) {
            var seed = makeSeedRecipe(crop);
            var seedRegular = makeRegularSeedRecipe(crop);
            var reprocessor = makeReprocessorRecipe(crop);

            if (seed != null)
                event.addRecipe(seed);

            if (seedRegular != null)
                event.addRecipe(seedRegular);

            if (reprocessor != null)
                event.addRecipe(reprocessor);
        }
    }

    private static RecipeHolder<ISpecialRecipe> makeSeedRecipe(Crop crop) {
        if (!crop.isEnabled() || !crop.getRecipeConfig().isSeedInfusionRecipeEnabled())
            return null;

        var essenceItem = crop.getTier().getEssence();
        if (essenceItem == null)
            return null;

        var craftingSeedItem = crop.getType().getCraftingSeed();
        if (craftingSeedItem == null)
            return null;

        var material = crop.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var essence = Ingredient.of(essenceItem);
        var craftingSeed = Ingredient.of(craftingSeedItem);
        var inputs = NonNullList.of(Ingredient.EMPTY,
                material, essence, material, essence, material, essence, material, essence
        );

        var id = MysticalAgriculture.resource(crop.getNameWithSuffix("seeds_infusion"));
        var result = new ItemStack(crop.getSeedsItem());

        return new RecipeHolder<>(id, new InfusionRecipe(craftingSeed, inputs, result, false));
    }

    private static RecipeHolder<Recipe<?>> makeRegularSeedRecipe(Crop crop) {
        if (!crop.isEnabled() || !crop.getRecipeConfig().isSeedCraftingRecipeEnabled())
            return null;

        if (!ModConfigs.SEED_CRAFTING_RECIPES.get())
            return null;

        var essenceItem = crop.getTier().getEssence();
        if (essenceItem == null)
            return null;

        var craftingSeedItem = crop.getType().getCraftingSeed();
        if (craftingSeedItem == null)
            return null;

        var material = crop.getCraftingMaterial();
        if (material == Ingredient.EMPTY)
            return null;

        var essence = Ingredient.of(essenceItem);
        var craftingSeed = Ingredient.of(craftingSeedItem);
        var inputs = NonNullList.of(Ingredient.EMPTY,
                material, essence, material,
                essence, craftingSeed, essence,
                material, essence, material
        );

        var id = MysticalAgriculture.resource(crop.getNameWithSuffix("seeds_vanilla"));
        var pattern = new ShapedRecipePattern(3, 3, inputs, Optional.empty());
        var result = new ItemStack(crop.getSeedsItem());

        return new RecipeHolder<>(id, new ShapedRecipe("", CraftingBookCategory.MISC, pattern, result));
    }

    private static RecipeHolder<ISpecialRecipe> makeReprocessorRecipe(Crop crop) {
        if (!crop.isEnabled() || !crop.getRecipeConfig().isSeedReprocessorRecipeEnabled())
            return null;

        var input = Ingredient.of(crop.getSeedsItem());
        var id = MysticalAgriculture.resource(crop.getNameWithSuffix("seeds_reprocessor"));
        var result = new ItemStack(crop.getEssenceItem(), 2);

        return new RecipeHolder<>(id, new ReprocessorRecipe(input, result));
    }
}
