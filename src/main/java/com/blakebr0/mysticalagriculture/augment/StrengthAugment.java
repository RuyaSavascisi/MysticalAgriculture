package com.blakebr0.mysticalagriculture.augment;

import com.blakebr0.cucumber.helper.ColorHelper;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.tinkering.Augment;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentAttributeModifier;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.EnumSet;
import java.util.List;

public class StrengthAugment extends Augment {
    private static final ResourceLocation ATTRIBUTE_ID = MysticalAgriculture.resource("strength_augment");
    private final int amplifier;

    public StrengthAugment(ResourceLocation id, int tier, int amplifier) {
        super(id, tier, EnumSet.of(AugmentType.SWORD), getColor(0xFFFD90, tier), getColor(0xCC8E27, tier));
        this.amplifier = amplifier;
    }

    @Override
    public List<AugmentAttributeModifier> getAttributeModifiers() {
        return List.of(
                new AugmentAttributeModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTRIBUTE_ID, 5 * this.amplifier, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.ANY)
        );
    }

    private static int getColor(int color, int tier) {
        return ColorHelper.saturate(color, Math.min((float) tier / 5, 1));
    }
}
