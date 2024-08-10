package com.blakebr0.mysticalagriculture.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.mysticalagriculture.api.crafting.IReprocessorRecipe;
import com.blakebr0.mysticalagriculture.init.ModRecipeSerializers;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ReprocessorRecipe implements ISpecialRecipe, IReprocessorRecipe {
    private final NonNullList<Ingredient> inputs;
    private final ItemStack result;

    public ReprocessorRecipe(Ingredient input, ItemStack result) {
        this.inputs = NonNullList.of(Ingredient.EMPTY, input);
        this.result = result;
    }

    @Override
    public ItemStack assemble(RecipeInput inventory, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.REPROCESSOR.get();
    }

    @Override
    public RecipeType<? extends IReprocessorRecipe> getType() {
        return ModRecipeTypes.REPROCESSOR.get();
    }

    public static class Serializer implements RecipeSerializer<ReprocessorRecipe> {
        public static final MapCodec<ReprocessorRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
               builder.group(
                       Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(recipe -> recipe.inputs.getFirst()),
                       ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
               ).apply(builder, ReprocessorRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, ReprocessorRecipe> STREAM_CODEC = StreamCodec.of(
                ReprocessorRecipe.Serializer::toNetwork, ReprocessorRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<ReprocessorRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ReprocessorRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static ReprocessorRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var input = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            var result = ItemStack.STREAM_CODEC.decode(buffer);

            return new ReprocessorRecipe(input, result);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, ReprocessorRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.inputs.getFirst());
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        }
    }
}
