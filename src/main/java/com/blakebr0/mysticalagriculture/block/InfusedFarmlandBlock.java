package com.blakebr0.mysticalagriculture.block;

import com.blakebr0.cucumber.iface.IColored;
import com.blakebr0.mysticalagriculture.api.crop.CropTier;
import com.blakebr0.mysticalagriculture.api.farmland.IEssenceFarmland;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.ArrayList;
import java.util.List;

public class InfusedFarmlandBlock extends FarmBlock implements IColored, IEssenceFarmland {
    public static final List<InfusedFarmlandBlock> FARMLANDS = new ArrayList<>();
    private final CropTier tier;

    public InfusedFarmlandBlock(CropTier tier) {
        super(Properties.ofFullCopy(Blocks.FARMLAND));
        this.tier = tier;

        FARMLANDS.add(this);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.causeFallDamage(fallDistance, 1.0F, level.damageSources().fall());
    }

    @Override
    public boolean isFertile(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(MOISTURE) > 0;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int moisture = state.getValue(MOISTURE);

        if (!isNearWater(level, pos) && !level.isRainingAt(pos.above())) {
            if (moisture > 0) {
                level.setBlock(pos, state.setValue(MOISTURE, moisture - 1), 2);
            }
        } else if (moisture < 7) {
            level.setBlock(pos, state.setValue(MOISTURE, 7), 2);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        var stack = builder.getOptionalParameter(LootContextParams.TOOL);
        var registry = builder.getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        var silkTouch = registry.getHolderOrThrow(Enchantments.SILK_TOUCH);

        if (stack != null && EnchantmentHelper.getTagEnchantmentLevel(silkTouch, stack) > 0) {
            drops.add(new ItemStack(this));
        } else {
            drops.add(new ItemStack(Blocks.DIRT));

            var random = builder.getLevel().getRandom();
            if (random.nextInt(100) < 25)
                drops.add(new ItemStack(this.tier.getEssence(), 1));
        }

        return drops;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(ModTooltips.TIER.args(this.tier.getDisplayName()).build());
    }

    @Override
    public int getColor(int index) {
        return this.tier.getColor();
    }

    @Override
    public CropTier getTier() {
        return this.tier;
    }
}
