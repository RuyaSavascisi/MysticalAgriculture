package com.blakebr0.mysticalagriculture.crafting.condition;

import com.blakebr0.mysticalagriculture.registry.AugmentRegistry;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;

public class AugmentEnabledCondition implements ICondition {
    public static final MapCodec<AugmentEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    ResourceLocation.CODEC.fieldOf("augment").forGetter(condition -> condition.augment)
            ).apply(builder, AugmentEnabledCondition::new)
    );

    private final ResourceLocation augment;

    public AugmentEnabledCondition(ResourceLocation augment) {
        this.augment = augment;
    }

    @Override
    public boolean test(IContext context) {
        var augment = AugmentRegistry.getInstance().getAugmentById(this.augment);
        return augment != null && augment.isEnabled();
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
