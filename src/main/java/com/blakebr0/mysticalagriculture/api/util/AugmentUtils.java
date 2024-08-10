package com.blakebr0.mysticalagriculture.api.util;

import com.blakebr0.mysticalagriculture.api.MysticalAgricultureAPI;
import com.blakebr0.mysticalagriculture.api.MysticalAgricultureDataComponentTypes;
import com.blakebr0.mysticalagriculture.api.components.AugmentComponent;
import com.blakebr0.mysticalagriculture.api.tinkering.Augment;
import com.blakebr0.mysticalagriculture.api.tinkering.ITinkerable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AugmentUtils {
    /**
     * Add an augment to the specified tinkerable in the specified slot
     *
     * @param stack   the {@link ITinkerable} item
     * @param augment the augment
     * @param slot    the augment slot
     */
    public static void addAugment(ItemStack stack, Augment augment, int slot) {
        var item = stack.getItem();

        if (item instanceof ITinkerable tinkerable) {
            if (slot < tinkerable.getAugmentSlots() && tinkerable.getTinkerableTier() >= augment.getTier()) {
                var component = stack.getOrDefault(MysticalAgricultureDataComponentTypes.EQUIPPED_AUGMENTS, new HashMap<Integer, AugmentComponent>());
                component.put(slot, new AugmentComponent(augment.getId(), slot));
                stack.set(MysticalAgricultureDataComponentTypes.EQUIPPED_AUGMENTS, component);

                var augmentModifiers = augment.getAttributeModifiers();
                if (!augmentModifiers.isEmpty()) {
                    var itemModifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
                    for (var modifier : augmentModifiers) {
                        itemModifiers = itemModifiers.withModifierAdded(modifier.attribute(), modifier.modifier(), modifier.slot());
                    }
                }
            }
        }
    }

    /**
     * Remove an augment from the specified tinkerable from the specified slot
     *
     * @param stack the {@link ITinkerable} item
     * @param slot  the augment slot
     */
    public static void removeAugment(ItemStack stack, int slot) {
        var component = stack.get(MysticalAgricultureDataComponentTypes.EQUIPPED_AUGMENTS);
        if (component == null)
            return;

        var item = stack.getItem();

        if (item instanceof ITinkerable tinkerable) {
            var augment = getAugment(stack, slot);
            if (slot < tinkerable.getAugmentSlots() && augment != null) {
                component.remove(slot);
                stack.set(MysticalAgricultureDataComponentTypes.EQUIPPED_AUGMENTS, component);

                var augmentModifiers = augment.getAttributeModifiers();
                if (!augmentModifiers.isEmpty()) {
                    var itemModifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
                    for (var modifier : augmentModifiers) {
                        itemModifiers = itemModifiers.withModifierAdded(modifier.attribute(), modifier.modifier(), modifier.slot());
                    }
                }
            }
        }
    }

    /**
     * Get the augment in the specified augment slot
     *
     * @param stack the {@link ITinkerable} item
     * @param slot  the augment slot
     * @return the augment
     */
    public static Augment getAugment(ItemStack stack, int slot) {
        var component = stack.get(MysticalAgricultureDataComponentTypes.EQUIPPED_AUGMENTS);
        if (component == null)
            return null;

        var item = stack.getItem();

        if (item instanceof ITinkerable tinkerable) {
            var augment = component.get(slot);
            if (slot < tinkerable.getAugmentSlots() && augment != null) {
                return MysticalAgricultureAPI.getAugmentRegistry().getAugmentById(augment.id());
            }
        }

        return null;
    }

    /**
     * Gets the augments currently installed on this tinkerable
     *
     * @param stack the {@link ITinkerable}
     * @return the installed augments
     */
    public static List<Augment> getAugments(ItemStack stack) {
        var component = stack.get(MysticalAgricultureDataComponentTypes.EQUIPPED_AUGMENTS);
        List<Augment> augments = new ArrayList<>();

        if (component == null)
            return augments;

        var item = stack.getItem();

        if (item instanceof ITinkerable tinkerable) {
            int slots = tinkerable.getAugmentSlots();

            for (int i = 0; i < slots; i++) {
                var augment = getAugment(stack, i);
                if (augment != null)
                    augments.add(augment);
            }
        }

        return augments;
    }

    /**
     * Helper method to get the augments from the player's armor
     *
     * @param player the player
     * @return the installed augments
     */
    public static List<Augment> getArmorAugments(Player player) {
        var armor = player.getInventory().armor;
        List<Augment> augments = new ArrayList<>();

        for (var stack : armor) {
            augments.addAll(getAugments(stack));
        }

        return augments;
    }

    /**
     * Get the tooltip color for the provided int tier
     *
     * @param tier the tier
     * @return the color
     */
    @Deprecated(forRemoval = true)
    public static ChatFormatting getColorForTier(int tier) {
        return TinkerableUtils.getColorForTier(tier);
    }

    /**
     * Gets the text component variant of the provided tier number for use in tooltips
     *
     * @param tier the tier
     * @return the formatted tier
     */
    @Deprecated(forRemoval = true)
    public static Component getTooltipForTier(int tier) {
        return Component.literal(String.valueOf(tier)).withStyle(getColorForTier(tier));
    }
}
