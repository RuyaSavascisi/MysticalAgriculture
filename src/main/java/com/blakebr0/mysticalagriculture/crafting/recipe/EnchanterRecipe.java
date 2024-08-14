package com.blakebr0.mysticalagriculture.crafting.recipe;

import com.blakebr0.cucumber.crafting.ingredient.IngredientWithCount;
import com.blakebr0.mysticalagriculture.api.crafting.IEnchanterRecipe;
import com.blakebr0.mysticalagriculture.init.ModRecipeSerializers;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.stream.Collectors;

public class EnchanterRecipe implements IEnchanterRecipe {
    private final NonNullList<IngredientWithCount> inputs;
    private final Holder<Enchantment> enchantment;

    public EnchanterRecipe(NonNullList<IngredientWithCount> inputs, Holder<Enchantment> enchantment) {
        this.inputs = inputs;
        this.enchantment = enchantment;
    }

    @Override
    public boolean matches(CraftingInput inventory, Level level) {
        var inputs = NonNullList.<ItemStack>create();

        for (var i = 0; i < inventory.size() - 1; i++) {
            var item = inventory.getItem(i);
            if (!item.isEmpty()) {
                inputs.add(item);
            }
        }

        return RecipeMatcher.findMatches(inputs, this.inputs) != null;
    }

    @Override
    public ItemStack assemble(CraftingInput inventory, HolderLookup.Provider provider) {
        return this.getEnchantedOutputItemStack(inventory);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputs.stream()
                .map(ICustomIngredient::toVanilla)
                .collect(Collectors.toCollection(NonNullList::create));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.ENCHANTER.get();
    }

    @Override
    public RecipeType<? extends IEnchanterRecipe> getType() {
        return ModRecipeTypes.ENCHANTER.get();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inventory) {
        var remaining = NonNullList.withSize(inventory.size(), ItemStack.EMPTY);

        for (int i = 0; i < 2; i++) {
            var stack = inventory.getItem(i);
            var count = this.inputs.get(i).getCount() * this.getOutputEnchantmentLevel(inventory);

            remaining.set(i, stack.copyWithCount(stack.getCount() - count));
        }

        var stack = inventory.getItem(2);
        if (stack.getCount() > 1) {
            remaining.set(2, stack.copyWithCount(stack.getCount() - 1));
        }

        return remaining;
    }

    @Override
    public Holder<Enchantment> getEnchantment() {
        return this.enchantment;
    }

    @Override
    public int getCount(int index) {
        if (index < 0 || index >= this.inputs.size())
            return -1;

        return this.inputs.get(index).getCount();
    }

    private ItemStack getEnchantedOutputItemStack(RecipeInput inventory) {
        var stack = inventory.getItem(2);

        if (this.enchantment.value().canEnchant(stack)) {
            var enchantments = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(stack));
            var newLevel = this.getOutputEnchantmentLevel(inventory);

            for (var enchantment : enchantments.keySet()) {
                if (enchantment == this.enchantment && enchantments.getLevel(enchantment) >= newLevel)
                    return ItemStack.EMPTY;

                if (enchantment != this.enchantment && Enchantment.areCompatible(enchantment, this.enchantment))
                    return ItemStack.EMPTY;
            }

            enchantments.set(enchantment, newLevel);

            var result = stack.copyWithCount(1);

            EnchantmentHelper.setEnchantments(result, enchantments.toImmutable());

            return result;
        }

        if (stack.is(Items.BOOK)) {
            return EnchantedBookItem.createForEnchantment(new EnchantmentInstance(this.enchantment, this.getOutputEnchantmentLevel(inventory)));
        }

        return ItemStack.EMPTY;
    }

    private int getOutputEnchantmentLevel(RecipeInput inventory) {
        var level = 0;

        for (var i = 0; i < this.inputs.size(); i++) {
            var stack = inventory.getItem(i);
            var count = this.inputs.get(i).getCount();

            var newLevel = stack.getCount() / count;

            if (level == 0 || newLevel < level) {
                level = Math.min(newLevel, this.enchantment.value().getMaxLevel());
            }
        }

        return level;
    }

    public static class Serializer implements RecipeSerializer<EnchanterRecipe> {
        public static final MapCodec<EnchanterRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
                builder.group(
                        IngredientWithCount.MAP_CODEC
                                .codec()
                                .listOf()
                                .fieldOf("ingredients")
                                .flatXmap(
                                        field -> {
                                            var max = 2;
                                            var ingredients = field.toArray(IngredientWithCount[]::new);
                                            if (ingredients.length == 0) {
                                                return DataResult.error(() -> "No ingredients for enchanter recipe");
                                            } else {
                                                return ingredients.length > max
                                                        ? DataResult.error(() -> "Too many ingredients for enchanter recipe. The maximum is: %s".formatted(max))
                                                        : DataResult.success(NonNullList.of(IngredientWithCount.EMPTY, ingredients));
                                            }
                                        },
                                        DataResult::success
                                )
                                .forGetter(recipe -> recipe.inputs),
                        Enchantment.CODEC.fieldOf("enchantment").forGetter(recipe -> recipe.enchantment)
                ).apply(builder, EnchanterRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, EnchanterRecipe> STREAM_CODEC = StreamCodec.of(
                EnchanterRecipe.Serializer::toNetwork, EnchanterRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<EnchanterRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EnchanterRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static EnchanterRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var size = buffer.readVarInt();
            var inputs = NonNullList.withSize(size, IngredientWithCount.EMPTY);

            for (int i = 0; i < size; i++) {
                inputs.set(i, IngredientWithCount.STREAM_CODEC.decode(buffer));
            }

            var enchantment = Enchantment.STREAM_CODEC.decode(buffer);

            return new EnchanterRecipe(inputs, enchantment);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, EnchanterRecipe recipe) {
            buffer.writeVarInt(recipe.inputs.size());

            for (var i = 0; i < recipe.inputs.size(); i++) {
                IngredientWithCount.STREAM_CODEC.encode(buffer, recipe.inputs.get(i));
            }

            Enchantment.STREAM_CODEC.encode(buffer, recipe.enchantment);
        }
    }
}
