package com.blakebr0.mysticalagriculture.compat.crafttweaker;

import com.blakebr0.mysticalagriculture.api.crafting.ISoulExtractionRecipe;
import com.blakebr0.mysticalagriculture.crafting.recipe.SoulExtractionRecipe;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.action.recipe.ActionRemoveRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

@ZenCodeType.Name("mods.mysticalagriculture.SoulExtractorCrafting")
@ZenRegister
public final class SoulExtractorCrafting implements IRecipeManager<ISoulExtractionRecipe> {
    private static final SoulExtractorCrafting INSTANCE = new SoulExtractorCrafting();

    @Override
    public RecipeType<ISoulExtractionRecipe> getRecipeType() {
        return ModRecipeTypes.SOUL_EXTRACTION.get();
    }

    @ZenCodeType.Method
    public static void addRecipe(String name, IIngredient input, String type, double souls) {
        var id = CraftTweakerConstants.rl(INSTANCE.fixRecipeName(name));
        var result = new SoulExtractionRecipe.Result(ResourceLocation.parse(type), souls);
        var recipe = new SoulExtractionRecipe(input.asVanillaIngredient(), result);

        CraftTweakerAPI.apply(new ActionAddRecipe<>(INSTANCE, new RecipeHolder<>(id, recipe)));
    }

    @ZenCodeType.Method
    public static void remove(IItemStack stack) {
        CraftTweakerAPI.apply(new ActionRemoveRecipe<>(INSTANCE, recipe -> recipe.value().getIngredients().getFirst().test(stack.getInternal())));
    }

    @ZenCodeType.Method
    public static void remove(String type) {
        CraftTweakerAPI.apply(new ActionRemoveRecipe<>(INSTANCE, recipe -> {
            var mobSoulType = recipe.value().getMobSoulType();
            return mobSoulType != null && mobSoulType.toString().equals(type);
        }));
    }
}
