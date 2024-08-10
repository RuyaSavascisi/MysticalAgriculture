package com.blakebr0.mysticalagriculture.api.lib;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
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
    private final CompoundTag nbt;
    private final Type type;
    private Ingredient ingredient;

    private LazyIngredient(String id, Type type, CompoundTag nbt) {
        this.id = id;
        this.type = type;
        this.nbt = nbt;
    }

    public static LazyIngredient item(String name) {
        return item(name, null);
    }

    public static LazyIngredient item(String name, CompoundTag nbt) {
        return new LazyIngredient(name, Type.ITEM, nbt);
    }

    public static LazyIngredient tag(String name) {
        return new LazyIngredient(name, Type.TAG, null);
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

    public Ingredient.Value createValue() {
        if (this.isTag()) {
            var tag = ItemTags.create(ResourceLocation.parse(this.id));
            return new Ingredient.TagValue(tag);
        } else if (this.isItem()) {
            var item = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(this.id));
            if (item.isPresent()) {
                var stack = new ItemStack(item.get());

                // TODO: 1.21 change to data components
                if (this.nbt != null && !this.nbt.isEmpty()) {
//                    stack.setTag(this.nbt);
                }

                return new Ingredient.ItemValue(stack);
            }
        }

        return null;
    }

    public Ingredient getIngredient() {
        if (this.ingredient == null) {
            if (this.isTag()) {
                var tag = ItemTags.create(ResourceLocation.parse(this.id));
                this.ingredient = Ingredient.of(tag);
            } else if (this.isItem()) {
                var item = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(this.id));
                if (item.isPresent()) {
                    if (this.nbt == null || this.nbt.isEmpty()) {
                        this.ingredient = Ingredient.of(item.get());
                    } else {
                        var stack = new ItemStack(item.get());

                        // TODO: 1.21 change to data components
//                        stack.setTag(this.nbt);

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
