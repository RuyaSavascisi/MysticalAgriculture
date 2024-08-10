package com.blakebr0.mysticalagriculture.init;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.lib.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class ModEnchantments {
    public static final Map<DeferredHolder<Enchantment, Enchantment>, Function<HolderLookup.RegistryLookup<Item>, Enchantment.Builder>> ENTRIES = new HashMap<>();

    public static final DeferredHolder<Enchantment, Enchantment> MYSTICAL_ENLIGHTENMENT = register("mystical_enlightenment", (lookup) -> Enchantment.enchantment(
            Enchantment.definition(
                    lookup.getOrThrow(ModTags.MYSTICAL_ENLIGHTENMENT_ENCHANTABLE),
                    10, 5, Enchantment.dynamicCost(1, 11), Enchantment.dynamicCost(21, 11), 1,
                    EquipmentSlotGroup.MAINHAND
            )
    ));
    public static final DeferredHolder<Enchantment, Enchantment> SOUL_SIPHONER = register("soul_siphoner", (lookup) -> Enchantment.enchantment(
            Enchantment.definition(
                    lookup.getOrThrow(ModTags.SOUL_SIPHONER_ENCHANTABLE),
                    10, 5, Enchantment.dynamicCost(1, 11), Enchantment.dynamicCost(21, 11), 1,
                    EquipmentSlotGroup.MAINHAND
            )
    ));

    @SubscribeEvent
    public void onRegisterEnchantments(RegisterEvent event) {
        event.register(Registries.ENCHANTMENT, registry -> {
            var lookup = BuiltInRegistries.ITEM.asLookup();

            ENTRIES.forEach((holder, builder) -> {
                registry.register(holder.getId(), builder.apply(lookup).build(holder.getId()));
            });
        });
    }

    private static DeferredHolder<Enchantment, Enchantment> register(String name, Function<HolderLookup.RegistryLookup<Item>, Enchantment.Builder> enchantment) {
        var holder = DeferredHolder.create(Registries.ENCHANTMENT, MysticalAgriculture.resource(name));
        ENTRIES.put(holder, enchantment);
        return holder;
    }
}
