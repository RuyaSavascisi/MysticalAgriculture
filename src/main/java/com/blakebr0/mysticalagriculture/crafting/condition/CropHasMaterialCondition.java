package com.blakebr0.mysticalagriculture.crafting.condition;

import com.blakebr0.mysticalagriculture.registry.CropRegistry;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;

public class CropHasMaterialCondition implements ICondition {
    public static final MapCodec<CropHasMaterialCondition> CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    ResourceLocation.CODEC.fieldOf("crop").forGetter(condition -> condition.crop)
            ).apply(builder, CropHasMaterialCondition::new)
    );

    private final ResourceLocation crop;

    public CropHasMaterialCondition(ResourceLocation crop) {
        this.crop = crop;
    }

    @Override
    public boolean test(IContext context) {
        var crop = CropRegistry.getInstance().getCropById(this.crop);
        if (crop == null)
            return false;

        var material = crop.getCraftingMaterial();

        return material != null && !material.isEmpty();
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
