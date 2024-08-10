package com.blakebr0.mysticalagriculture.crafting.ingredient;

import com.blakebr0.mysticalagriculture.api.util.MobSoulUtils;
import com.blakebr0.mysticalagriculture.init.ModIngredientTypes;
import com.blakebr0.mysticalagriculture.init.ModItems;
import com.blakebr0.mysticalagriculture.item.SoulJarItem;
import com.blakebr0.mysticalagriculture.registry.MobSoulTypeRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class FilledSoulJarIngredient implements ICustomIngredient {
    public static final MapCodec<FilledSoulJarIngredient> CODEC = MapCodec.unit(FilledSoulJarIngredient::new);

    private ItemStack[] stacks;

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack != null) {
            if (this.stacks == null) {
                this.initMatchingStacks();
            }

            return stack.getItem() instanceof SoulJarItem && MobSoulUtils.getSouls(stack) > 0;
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
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return ModIngredientTypes.FILLED_SOUL_JAR.get();
    }

    private void initMatchingStacks() {
        this.stacks = MobSoulTypeRegistry.getInstance().getMobSoulTypes().stream()
                .map(type -> MobSoulUtils.getFilledSoulJar(type, ModItems.SOUL_JAR.get()))
                .toArray(ItemStack[]::new);
    }

    public static Ingredient of() {
        return new FilledSoulJarIngredient().toVanilla();
    }
}
