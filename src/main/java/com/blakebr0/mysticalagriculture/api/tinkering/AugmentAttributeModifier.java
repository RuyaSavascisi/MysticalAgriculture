package com.blakebr0.mysticalagriculture.api.tinkering;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/**
 * Contains the necessary parameters to apply a new AttributeModifier to an item provided by an {@link Augment}
 * @param attribute the entity attribute
 * @param modifier the attribute modifier
 * @param slot the applicable slot group
 */
public record AugmentAttributeModifier(Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot) { }
