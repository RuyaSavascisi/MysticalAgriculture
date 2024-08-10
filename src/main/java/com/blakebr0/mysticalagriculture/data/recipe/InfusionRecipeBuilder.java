package com.blakebr0.mysticalagriculture.data.recipe;

import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.blakebr0.mysticalagriculture.crafting.condition.CropEnabledCondition;
import com.blakebr0.mysticalagriculture.crafting.condition.CropHasMaterialCondition;
import com.blakebr0.mysticalagriculture.crafting.ingredient.CropComponentIngredient;
import com.blakebr0.mysticalagriculture.crafting.recipe.InfusionRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;

import java.util.ArrayList;
import java.util.List;

public class InfusionRecipeBuilder {
    private final NonNullList<Ingredient> inputs;
    private final ItemStack result;
    private final List<ICondition> conditions;
    private Ingredient input = Ingredient.EMPTY;

    public InfusionRecipeBuilder(ItemStack result) {
        this.inputs = NonNullList.withSize(8, Ingredient.EMPTY);
        this.result = result;
        this.conditions = new ArrayList<>();
    }

    public void addIngredient(int index, Ingredient ingredient) {
        this.inputs.set(index, ingredient);
    }

    public void addCondition(ICondition condition) {
        this.conditions.add(condition);
    }

    public void build(RecipeOutput consumer, ResourceLocation id) {
        consumer.accept(id, new InfusionRecipe(this.input, this.inputs, this.result, false), null, this.conditions.toArray(new ICondition[0]));
    }

    public static InfusionRecipeBuilder newSeedRecipe(Crop crop) {
        var builder = new InfusionRecipeBuilder(new ItemStack(crop.getSeedsItem()));

        var essence = CropComponentIngredient.of(crop.getId(), CropComponentIngredient.ComponentType.ESSENCE);
        var seed = CropComponentIngredient.of(crop.getId(), CropComponentIngredient.ComponentType.SEED);
        var material = CropComponentIngredient.of(crop.getId(), CropComponentIngredient.ComponentType.MATERIAL);

        builder.input = seed;

        builder.addIngredient(0, material);
        builder.addIngredient(1, essence);
        builder.addIngredient(2, material);
        builder.addIngredient(3, essence);
        builder.addIngredient(4, material);
        builder.addIngredient(5, essence);
        builder.addIngredient(6, material);
        builder.addIngredient(7, essence);

        builder.addCondition(new CropEnabledCondition(crop.getId()));
        builder.addCondition(new CropHasMaterialCondition(crop.getId()));

        var ingredient = crop.getLazyIngredient();

        if (ingredient.isTag()) {
            builder.addCondition(new NotCondition(new TagEmptyCondition(ingredient.getId())));
        }

        return builder;
    }
}
