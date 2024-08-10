package com.blakebr0.mysticalagriculture.item;

import com.blakebr0.mysticalagriculture.init.ModDataComponentTypes;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class EssenceWateringCanItem extends WateringCanItem {
    private final ChatFormatting textColor;

    public EssenceWateringCanItem(int range, double chance, ChatFormatting textColor) {
        super(range, chance, p -> p.component(ModDataComponentTypes.WATERING_CAN_ACTIVE, false));
        this.textColor = textColor;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        var isActive = isActive(stack);

        if (selected && isActive && entity instanceof Player player) {
            var trace = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

            if (trace.getType() == HitResult.Type.BLOCK) {
                this.doWater(stack, level, player, trace.getBlockPos(), trace.getDirection());
            } else {
                stopPlayingSound(player);
            }
        }

        // we need to actively check if the watering can was playing the sound in any case where it's not actively
        // watering the ground
        if (!selected && isActive && entity instanceof Player player) {
            stopPlayingSound(player);
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return isActive(stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        var trace = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (trace.getType() != HitResult.Type.BLOCK) {
            if (isFilled(stack) && player.isCrouching()) {
                flipActive(stack);
            }

            return new InteractionResultHolder<>(InteractionResult.PASS, stack);
        }

        if (isFilled(stack)) {
            return new InteractionResultHolder<>(InteractionResult.PASS, stack);
        }

        var pos = trace.getBlockPos();
        var direction = trace.getDirection();

        if (level.mayInteract(player, pos) && player.mayUseItemAt(pos.relative(direction), direction, stack)) {
            var fluid = level.getFluidState(pos);

            if (fluid.is(FluidTags.WATER)) {
                setFilled(stack, true);

                player.playSound(SoundEvents.BUCKET_FILL, 1.0F, 1.0F);

                return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
            }
        }

        return new InteractionResultHolder<>(InteractionResult.PASS, stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var player = context.getPlayer();
        if (player == null)
            return InteractionResult.FAIL;

        var hand = context.getHand();
        var stack = player.getItemInHand(hand);

        if (isActive(stack))
            return InteractionResult.PASS;

        return super.useOn(context);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag advanced) {
        super.appendHoverText(stack, context, tooltip, advanced);

        var rangeString = String.valueOf(this.range);
        var rangeNumber = Component.literal(rangeString + "x" + rangeString).withStyle(this.textColor);

        tooltip.add(ModTooltips.TOOL_AREA.args(rangeNumber).build());
    }

    public static boolean isActive(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.WATERING_CAN_ACTIVE, false);
    }

    public static void setActive(ItemStack stack, boolean active) {
        stack.set(ModDataComponentTypes.WATERING_CAN_ACTIVE, active);
    }

    public static void flipActive(ItemStack stack) {
        var current = isActive(stack);
        setActive(stack, !current);
    }
}
