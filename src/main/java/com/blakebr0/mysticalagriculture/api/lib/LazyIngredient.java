package com.blakebr0.mysticalagriculture.api.lib;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

public class LazyIngredient {
    public static final LazyIngredient EMPTY = new LazyIngredient(null, null, null) {
        @Override
        public Ingredient getIngredient() {
            return Ingredient.EMPTY;
        }
    };

    private final String id;
    private final DataComponentMap components;
    private final Type type;
    private Ingredient ingredient;

    private LazyIngredient(String id, Type type, DataComponentMap components) {
        this.id = id;
        this.type = type;
        this.components = components;
    }

    public static LazyIngredient item(String name) {
        return item(name, DataComponentMap.EMPTY);
    }

    public static LazyIngredient item(String name, DataComponentMap components) {
        return new LazyIngredient(name, Type.ITEM, components);
    }

    public static LazyIngredient tag(String name) {
        return new LazyIngredient(name, Type.TAG, DataComponentMap.EMPTY);
    }
    
    public String getId() {
        return this.id;
    }

    public boolean isItem() {
        return this.type == Type.ITEM;
    }

    public boolean isTag() {
        return this.type == Type.TAG;
    }

    public Ingredient getIngredient() {
        if (this.ingredient == null) {
            if (this.isTag()) {
                var tag = ItemTags.create(ResourceLocation.parse(this.id));
                this.ingredient = Ingredient.of(tag);
            } else if (this.isItem()) {
                var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(this.id));
                if (item != Items.AIR) {
                    if (this.components == null || this.components.isEmpty()) {
                        this.ingredient = Ingredient.of(item);
                    } else {
                        var stack = new ItemStack(item);
                        stack.applyComponents(this.components);
                        this.ingredient = DataComponentIngredient.of(false, stack);
                    }
                }
            }
        }

        return this.ingredient == null ? Ingredient.EMPTY : this.ingredient;
    }

    private enum Type {
        ITEM, TAG
    }
}
