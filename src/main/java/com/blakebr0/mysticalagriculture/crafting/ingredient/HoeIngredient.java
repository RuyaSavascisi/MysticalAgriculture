package com.blakebr0.mysticalagriculture.crafting.ingredient;

import com.blakebr0.mysticalagriculture.init.ModIngredientTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class HoeIngredient implements ICustomIngredient {
    public static final MapCodec<HoeIngredient> CODEC = MapCodec.unit(HoeIngredient::new);

    private ItemStack[] stacks;

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack != null) {
            if (this.stacks == null) {
                this.initMatchingStacks();
            }

            for (var itemstack : this.stacks) {
                if (itemstack.getItem() == stack.getItem()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Stream<ItemStack> getItems() {
        if (this.stacks == null) {
            this.initMatchingStacks();
        }

        return Stream.of(this.stacks);
    }

    @Override
    public boolean isSimple() {
        return true;
    }

    @Override
    public IngredientType<?> getType() {
        return ModIngredientTypes.ALL_HOES.get();
    }

    private void initMatchingStacks() {
        this.stacks = BuiltInRegistries.ITEM.entrySet()
                .stream()
                .filter(i -> i.getValue() instanceof HoeItem)
                .map(i -> new ItemStack(i.getValue()))
                .toArray(ItemStack[]::new);
    }
}
