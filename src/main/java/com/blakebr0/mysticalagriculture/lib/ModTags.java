package com.blakebr0.mysticalagriculture.lib;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class ModTags {
    public static final TagKey<Block> INCORRECT_FOR_INFERIUM_TOOL = BlockTags.create(MysticalAgriculture.resource("incorrect_for_inferium_tool"));
    public static final TagKey<Block> INCORRECT_FOR_PRUDENTIUM_TOOL = BlockTags.create(MysticalAgriculture.resource("incorrect_for_prudentium_tool"));
    public static final TagKey<Block> INCORRECT_FOR_TERTIUM_TOOL = BlockTags.create(MysticalAgriculture.resource("incorrect_for_tertium_tool"));
    public static final TagKey<Block> INCORRECT_FOR_IMPERIUM_TOOL = BlockTags.create(MysticalAgriculture.resource("incorrect_for_imperium_tool"));
    public static final TagKey<Block> INCORRECT_FOR_SUPREMIUM_TOOL = BlockTags.create(MysticalAgriculture.resource("incorrect_for_supremium_tool"));
    public static final TagKey<Block> INCORRECT_FOR_AWAKENED_SUPREMIUM_TOOL = BlockTags.create(MysticalAgriculture.resource("incorrect_for_awakened_supremium_tool"));
    public static final TagKey<Block> INCORRECT_FOR_SOULIUM_TOOL = BlockTags.create(MysticalAgriculture.resource("incorrect_for_soulium_tool"));

    public static final TagKey<Item> MYSTICAL_ENLIGHTENMENT_ENCHANTABLE = ItemTags.create(MysticalAgriculture.resource("enchantable/mystical_enlightenment"));
    public static final TagKey<Item> SOUL_SIPHONER_ENCHANTABLE = ItemTags.create(MysticalAgriculture.resource("enchantable/soul_siphoner"));
}
