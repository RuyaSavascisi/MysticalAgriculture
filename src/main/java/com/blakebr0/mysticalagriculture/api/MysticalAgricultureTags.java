package com.blakebr0.mysticalagriculture.api;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class MysticalAgricultureTags {
    public interface Blocks {
        TagKey<Block> CROPS = BlockTags.create(MysticalAgricultureAPI.resource("crops"));

        TagKey<Block> INCORRECT_FOR_INFERIUM_TOOL = BlockTags.create(MysticalAgricultureAPI.resource("incorrect_for_inferium_tool"));
        TagKey<Block> INCORRECT_FOR_PRUDENTIUM_TOOL = BlockTags.create(MysticalAgricultureAPI.resource("incorrect_for_prudentium_tool"));
        TagKey<Block> INCORRECT_FOR_TERTIUM_TOOL = BlockTags.create(MysticalAgricultureAPI.resource("incorrect_for_tertium_tool"));
        TagKey<Block> INCORRECT_FOR_IMPERIUM_TOOL = BlockTags.create(MysticalAgricultureAPI.resource("incorrect_for_imperium_tool"));
        TagKey<Block> INCORRECT_FOR_SUPREMIUM_TOOL = BlockTags.create(MysticalAgricultureAPI.resource("incorrect_for_supremium_tool"));
        TagKey<Block> INCORRECT_FOR_AWAKENED_SUPREMIUM_TOOL = BlockTags.create(MysticalAgricultureAPI.resource("incorrect_for_awakened_supremium_tool"));
        TagKey<Block> INCORRECT_FOR_SOULIUM_TOOL = BlockTags.create(MysticalAgricultureAPI.resource("incorrect_for_soulium_tool"));
    }

    public interface Items {
        TagKey<Item> ESSENCES = ItemTags.create(MysticalAgricultureAPI.resource("essences"));
        TagKey<Item> SEEDS = ItemTags.create(MysticalAgricultureAPI.resource("seeds"));

        TagKey<Item> MYSTICAL_ENLIGHTENMENT_ENCHANTABLE = ItemTags.create(MysticalAgricultureAPI.resource("enchantable/mystical_enlightenment"));
        TagKey<Item> SOUL_SIPHONER_ENCHANTABLE = ItemTags.create(MysticalAgricultureAPI.resource("enchantable/soul_siphoner"));
    }
}
