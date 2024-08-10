package com.blakebr0.mysticalagriculture.client.handler;

import com.blakebr0.mysticalagriculture.api.crafting.IAwakeningRecipe;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import com.blakebr0.mysticalagriculture.tileentity.AwakeningAltarTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.EssenceVesselTileEntity;
import com.blakebr0.mysticalagriculture.tileentity.InfusionAltarTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public final class GuiOverlayHandler {
    @SubscribeEvent
    public void onPostRenderGui(RenderGuiEvent.Post event) {
        var mc = Minecraft.getInstance();
        var level = mc.level;

        if (level == null)
            return;

        var gfx = event.getGuiGraphics();

        if (mc.hitResult instanceof BlockHitResult result) {
            var pos = result.getBlockPos();
            var tile = level.getBlockEntity(pos);
            var stack = ItemStack.EMPTY;

            if (tile instanceof InfusionAltarTileEntity altar) {
                var recipe = altar.getActiveRecipe();
                if (recipe != null) {
                    stack = recipe.getResultItem(level.registryAccess());
                }
            }

            if (tile instanceof AwakeningAltarTileEntity altar) {
                var recipe = altar.getActiveRecipe();
                if (recipe != null) {
                    stack = recipe.getResultItem(level.registryAccess());

                    drawEssenceRequirements(gfx, recipe, altar);
                }
            }

            if (!stack.isEmpty()) {
                int x = mc.getWindow().getGuiScaledWidth() / 2 - 11;
                int y = mc.getWindow().getGuiScaledHeight() / 2 - 8;

                gfx.renderItem(stack, x + 26, y);
                gfx.renderItemDecorations(mc.font, stack, x + 26, y);
                gfx.drawString(mc.font, stack.getHoverName(), x + 48, y + 5, 16383998);
            }
        }

        if (mc.hitResult instanceof BlockHitResult result) {
            var pos = result.getBlockPos();
            var tile = mc.level.getBlockEntity(pos);

            if (tile instanceof EssenceVesselTileEntity vessel) {
                var stack = vessel.getInventory().getStackInSlot(0);

                if (!stack.isEmpty()) {
                    int x = mc.getWindow().getGuiScaledWidth() / 2 - 11;
                    int y = mc.getWindow().getGuiScaledHeight() / 2 - 8;

                    gfx.renderItem(stack, x + 26, y);
                    gfx.renderItemDecorations(mc.font, stack, x + 26, y);
                    gfx.drawString(mc.font, stack.getHoverName(), x + 48, y + 5, 16383998);
                }
            }
        }
    }

    private static void drawEssenceRequirements(GuiGraphics gfx, IAwakeningRecipe recipe, AwakeningAltarTileEntity altar) {
        var mc = Minecraft.getInstance();

        int x = mc.getWindow().getGuiScaledWidth() / 2 - 11;
        int y = mc.getWindow().getGuiScaledHeight() / 2 - 4;
        var lineHeight = mc.font.lineHeight + 6;

        var hasMissingEssences = false;
        var xOffset = 0;

        var missingEssences = recipe.getMissingEssences(altar.getEssenceItems());

        for (var essence : missingEssences.entrySet()) {
            gfx.renderItem(essence.getKey(), x + 26 + xOffset, y + 2 * lineHeight);
            gfx.drawString(mc.font, getEssenceDisplayName(essence.getKey(), essence.getValue()), x + 48 + xOffset, y + 5 + 2 * lineHeight, 16383998);
            xOffset += 56;
            hasMissingEssences = true;
        }

        if (hasMissingEssences) {
            gfx.drawString(mc.font, ModTooltips.MISSING_ESSENCES.build(), x + 28, y + 5 + lineHeight, 16383998);
        }
    }

    private static String getEssenceDisplayName(ItemStack stack, int missing) {
        var required = stack.getCount();
        return required - missing + "/" + required;
    }
}
