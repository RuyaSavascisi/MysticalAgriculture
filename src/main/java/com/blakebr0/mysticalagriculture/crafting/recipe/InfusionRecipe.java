package com.blakebr0.mysticalagriculture.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.mysticalagriculture.api.crafting.IInfusionRecipe;
import com.blakebr0.mysticalagriculture.init.ModRecipeSerializers;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
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
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.function.BiFunction;

public class InfusionRecipe implements ISpecialRecipe, IInfusionRecipe {
    public static final int RECIPE_SIZE = 9;
    private final Ingredient input;
    private final NonNullList<Ingredient> inputs;
    private final ItemStack result;
    private final boolean transferComponents;
    // for CraftTweaker recipes
    private BiFunction<Integer, ItemStack, ItemStack> transformer;

    // the input is specified separately in JSON but is part of the ingredients list in practice
    public InfusionRecipe(Ingredient input, NonNullList<Ingredient> inputs, ItemStack result, boolean transferComponents) {
        this.input = input;
        this.inputs = inputs;
        this.result = result;
        this.transferComponents = transferComponents;
    }

    @Override
    public ItemStack assemble(RecipeInput inventory, HolderLookup.Provider lookup) {
        var stack = inventory.getItem(0);
        var result = this.result.copy();

        if (this.transferComponents) {
            result.applyComponents(stack.getComponents());
        }

        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider lookup) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.INFUSION.get();
    }

    @Override
    public RecipeType<? extends IInfusionRecipe> getType() {
        return ModRecipeTypes.INFUSION.get();
    }

    @Override
    public boolean matches(RecipeInput inventory, Level level) {
        var altarStack = inventory.getItem(0);
        return !this.inputs.isEmpty() && this.inputs.getFirst().test(altarStack) && ISpecialRecipe.super.matches(inventory, level);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(IItemHandler inventory) {
        var remaining = ISpecialRecipe.super.getRemainingItems(inventory);

        if (this.transformer != null) {
            var used = new boolean[remaining.size()];

            for (int i = 0; i < remaining.size(); i++) {
                var stack = inventory.getStackInSlot(i);

                for (int j = 0; j < this.inputs.size(); j++) {
                    var input = this.inputs.get(j);

                    if (!used[j] && input.test(stack)) {
                        var ingredient = this.transformer.apply(j, stack);

                        used[j] = true;
                        remaining.set(i, ingredient);

                        break;
                    }
                }
            }
        }

        return remaining;
    }

    public void setTransformer(BiFunction<Integer, ItemStack, ItemStack> transformer) {
        this.transformer = transformer;
    }

    public static class Serializer implements RecipeSerializer<InfusionRecipe> {
        public static final MapCodec<InfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
                builder.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(recipe -> recipe.input),
                        Ingredient.CODEC_NONEMPTY
                                .listOf()
                                .fieldOf("ingredients")
                                .flatXmap(
                                        field -> {
                                            var ingredients = field.toArray(Ingredient[]::new);
                                            if (ingredients.length == 0) {
                                                return DataResult.error(() -> "No ingredients for shapeless recipe");
                                            } else {
                                                return ingredients.length > 8
                                                        ? DataResult.error(() -> "Too many ingredients for shapeless recipe. The maximum is: %s".formatted(8))
                                                        : DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients));
                                            }
                                        },
                                        DataResult::success
                                )
                                .forGetter(recipe -> recipe.inputs),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.BOOL.fieldOf("transfer_components").forGetter(recipe -> recipe.transferComponents)
                ).apply(builder, InfusionRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, InfusionRecipe> STREAM_CODEC = StreamCodec.of(
                InfusionRecipe.Serializer::toNetwork, InfusionRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<InfusionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, InfusionRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static InfusionRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            int size = buffer.readVarInt();
            var inputs = NonNullList.withSize(size, Ingredient.EMPTY);

            for (int i = 0; i < size; i++) {
                inputs.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            }

            var result = ItemStack.STREAM_CODEC.decode(buffer);
            var transferNBT = buffer.readBoolean();

            return new InfusionRecipe(inputs.getFirst(), inputs, result, transferNBT);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, InfusionRecipe recipe) {
            buffer.writeVarInt(recipe.inputs.size());

            for (var ingredient : recipe.inputs) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeBoolean(recipe.transferComponents);
        }
    }
}
