package com.blakebr0.mysticalagriculture.data.recipe;

import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.blakebr0.mysticalagriculture.crafting.condition.CropEnabledCondition;
import com.blakebr0.mysticalagriculture.crafting.recipe.ReprocessorRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.ArrayList;
import java.util.List;

public class ReprocessorRecipeBuilder {
    private final Ingredient input;
    private final ItemStack result;
    private final List<ICondition> conditions;

    public ReprocessorRecipeBuilder(Ingredient input, ItemStack result) {
        this.input = input;
        this.result = result;
        this.conditions = new ArrayList<>();
    }

    public void addCondition(ICondition condition) {
        this.conditions.add(condition);
    }

    public void build(RecipeOutput consumer, ResourceLocation id) {
        consumer.accept(id, new ReprocessorRecipe(this.input, this.result), null, this.conditions.toArray(new ICondition[0]));
    }

    public static ReprocessorRecipeBuilder newSeedReprocessingRecipe(Crop crop) {
        var input = Ingredient.of(crop.getSeedsItem());
        var result = crop.getEssenceItem();

        var builder = new ReprocessorRecipeBuilder(input, new ItemStack(result, 2));

        builder.addCondition(new CropEnabledCondition(crop.getId()));

        return builder;
    }
}
