package com.blakebr0.mysticalagriculture.crafting.recipe;

import com.blakebr0.mysticalagriculture.init.ModRecipeSerializers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.ShapelessRecipe;

public class FarmlandTillRecipe extends ShapelessRecipe {
    private final ItemStack result;

    public FarmlandTillRecipe(String group, ItemStack result, NonNullList<Ingredient> inputs) {
        super(group, CraftingBookCategory.MISC, result, inputs);
        this.result = result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inventory) {
        var remaining = super.getRemainingItems(inventory);

        for (int i = 0; i < inventory.size(); i++) {
            var stack = inventory.getItem(i);

            if (stack.getItem() instanceof HoeItem) {
                var hoe = stack.copy();

                if (hoe.isDamageableItem()) {
                    var damage = hoe.getDamageValue() + 1;
                    hoe.setDamageValue(damage);
                    if (damage < hoe.getMaxDamage()) {
                        remaining.set(i, hoe);
                    }
                } else {
                    remaining.set(i, hoe);
                }
            }
        }

        return remaining;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CRAFTING_FARMLAND_TILL.get();
    }

    public static class Serializer implements RecipeSerializer<FarmlandTillRecipe> {
        public static final MapCodec<FarmlandTillRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
                builder.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(ShapelessRecipe::getGroup),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Ingredient.CODEC_NONEMPTY
                                .listOf()
                                .fieldOf("ingredients")
                                .flatXmap(
                                        field -> {
                                            Ingredient[] ingredients = field.toArray(Ingredient[]::new); // Neo skip the empty check and immediately create the array.
                                            if (ingredients.length == 0) {
                                                return DataResult.error(() -> "No ingredients for shapeless recipe");
                                            } else {
                                                return ingredients.length > ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth()
                                                        ? DataResult.error(() -> "Too many ingredients for shapeless recipe. The maximum is: %s".formatted(ShapedRecipePattern.getMaxHeight() * ShapedRecipePattern.getMaxWidth()))
                                                        : DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients));
                                            }
                                        },
                                        DataResult::success
                                )
                                .forGetter(ShapelessRecipe::getIngredients)
                ).apply(builder, FarmlandTillRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, FarmlandTillRecipe> STREAM_CODEC = StreamCodec.of(
                FarmlandTillRecipe.Serializer::toNetwork, FarmlandTillRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<FarmlandTillRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FarmlandTillRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static FarmlandTillRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var group = buffer.readUtf(32767);
            int size = buffer.readVarInt();
            var inputs = NonNullList.withSize(size, Ingredient.EMPTY);

            inputs.replaceAll(ignored -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));

            var result = ItemStack.STREAM_CODEC.decode(buffer);

            return new FarmlandTillRecipe(group, result, inputs);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, FarmlandTillRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeVarInt(recipe.getIngredients().size());

            for (var ingredient : recipe.getIngredients()) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        }
    }
}
