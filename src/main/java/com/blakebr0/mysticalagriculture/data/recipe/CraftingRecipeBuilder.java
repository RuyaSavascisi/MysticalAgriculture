package com.blakebr0.mysticalagriculture.data.recipe;

import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.blakebr0.mysticalagriculture.crafting.condition.CropEnabledCondition;
import com.blakebr0.mysticalagriculture.crafting.condition.CropHasMaterialCondition;
import com.blakebr0.mysticalagriculture.crafting.condition.SeedCraftingRecipesEnabledCondition;
import com.blakebr0.mysticalagriculture.crafting.ingredient.CropComponentIngredient;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CraftingRecipeBuilder {
    private final ItemStack result;
    private String group = "";
    private ShapedRecipePattern pattern;
    private CraftingBookCategory category;
    private final List<ICondition> conditions;

    public CraftingRecipeBuilder(ItemStack result) {
        this.result = result;
        this.conditions = new ArrayList<>();
    }

    public void addCondition(ICondition condition) {
        this.conditions.add(condition);
    }

    public void build(RecipeOutput consumer, ResourceLocation id) {
        consumer.accept(id, new ShapedRecipe(this.group, this.category, this.pattern, this.result), null, this.conditions.toArray(new ICondition[0]));
    }

    public static CraftingRecipeBuilder newSeedRecipe(Crop crop) {
        var builder = new CraftingRecipeBuilder(new ItemStack(crop.getSeedsItem()));

        var essence = CropComponentIngredient.of(crop.getId(), CropComponentIngredient.ComponentType.ESSENCE);
        var seed = CropComponentIngredient.of(crop.getId(), CropComponentIngredient.ComponentType.SEED);
        var material = CropComponentIngredient.of(crop.getId(), CropComponentIngredient.ComponentType.MATERIAL);

        builder.group = "mysticalagriculture:seeds";
        builder.category = CraftingBookCategory.MISC;
        builder.pattern = ShapedRecipePattern.of(
                Map.of('E', essence, 'S', seed, 'M', material),
                "MEM",
                "ESE",
                "MEM"
        );

        builder.addCondition(new SeedCraftingRecipesEnabledCondition());
        builder.addCondition(new CropEnabledCondition(crop.getId()));
        builder.addCondition(new CropHasMaterialCondition(crop.getId()));

        var ingredient = crop.getLazyIngredient();

        if (ingredient.isTag()) {
            builder.addCondition(new NotCondition(new TagEmptyCondition(ingredient.getId())));
        }

        return builder;
    }
}
