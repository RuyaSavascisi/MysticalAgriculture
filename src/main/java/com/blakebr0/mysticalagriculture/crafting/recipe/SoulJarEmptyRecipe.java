package com.blakebr0.mysticalagriculture.crafting.recipe;

import com.blakebr0.mysticalagriculture.api.util.MobSoulUtils;
import com.blakebr0.mysticalagriculture.crafting.ingredient.FilledSoulJarIngredient;
import com.blakebr0.mysticalagriculture.init.ModItems;
import com.blakebr0.mysticalagriculture.init.ModRecipeSerializers;
import com.blakebr0.mysticalagriculture.item.SoulJarItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

public class SoulJarEmptyRecipe extends ShapelessRecipe {
    public SoulJarEmptyRecipe(ItemStack result, NonNullList<Ingredient> inputs) {
        super("", CraftingBookCategory.MISC, result, inputs);
    }

    @Override
    public boolean matches(CraftingInput inventory, Level level) {
        var hasJar = false;

        for (int i = 0; i < inventory.size(); i++) {
            var stack = inventory.getItem(i);

            if (hasJar && !stack.isEmpty())
                return false;

            var item = stack.getItem();

            if (item instanceof SoulJarItem) {
                double souls = MobSoulUtils.getSouls(stack);
                if (souls > 0) {
                    hasJar = true;
                }
            } else if (!stack.isEmpty()) {
                return false;
            }
        }

        return hasJar;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CRAFTING_SOUL_JAR_EMPTY.get();
    }

    public static class Serializer implements RecipeSerializer<SoulJarEmptyRecipe> {
        public static final MapCodec<SoulJarEmptyRecipe> CODEC = MapCodec.unit(() -> new SoulJarEmptyRecipe(
                new ItemStack(ModItems.SOUL_JAR.get()),
                NonNullList.withSize(1, FilledSoulJarIngredient.of())
        ));
        public static final StreamCodec<RegistryFriendlyByteBuf, SoulJarEmptyRecipe> STREAM_CODEC = StreamCodec.of(
                SoulJarEmptyRecipe.Serializer::toNetwork, SoulJarEmptyRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<SoulJarEmptyRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SoulJarEmptyRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static SoulJarEmptyRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            return new SoulJarEmptyRecipe(new ItemStack(ModItems.SOUL_JAR.get()), NonNullList.withSize(1, FilledSoulJarIngredient.of()));
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, SoulJarEmptyRecipe recipe) { }
    }
}
