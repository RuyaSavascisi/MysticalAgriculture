package com.blakebr0.mysticalagriculture.api;

import com.blakebr0.mysticalagriculture.api.components.AugmentComponent;
import com.blakebr0.mysticalagriculture.api.components.MobSoulTypeComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Map;

public final class MysticalAgricultureDataComponentTypes {
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Map<Integer, AugmentComponent>>> EQUIPPED_AUGMENTS = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, MysticalAgricultureAPI.resource("equipped_augments"));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> EXPERIENCE_CAPSULE = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, MysticalAgricultureAPI.resource("experience_capsule"));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MobSoulTypeComponent>> MOB_SOUL_TYPE = DeferredHolder.create(Registries.DATA_COMPONENT_TYPE, MysticalAgricultureAPI.resource("mob_soul_type"));
}
