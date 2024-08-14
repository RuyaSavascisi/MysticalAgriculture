package com.blakebr0.mysticalagriculture.lib;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class ModEnchantments {
    public static final DeferredHolder<Enchantment, Enchantment> MYSTICAL_ENLIGHTENMENT = DeferredHolder.create(Registries.ENCHANTMENT, MysticalAgriculture.resource("mystical_enlightenment"));
    public static final DeferredHolder<Enchantment, Enchantment> SOUL_SIPHONER = DeferredHolder.create(Registries.ENCHANTMENT, MysticalAgriculture.resource("soul_siphoner"));
}
