package com.blakebr0.mysticalagriculture.api.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record MobSoulTypeComponent(ResourceLocation id, double souls) {
    public static final MapCodec<MobSoulTypeComponent> MAP_CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(MobSoulTypeComponent::id),
                    Codec.DOUBLE.fieldOf("souls").forGetter(MobSoulTypeComponent::souls)
            ).apply(builder, MobSoulTypeComponent::new)
    );
    public static final Codec<MobSoulTypeComponent> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<FriendlyByteBuf, MobSoulTypeComponent> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            MobSoulTypeComponent::id,
            ByteBufCodecs.DOUBLE,
            MobSoulTypeComponent::souls,
            MobSoulTypeComponent::new
    );
}
