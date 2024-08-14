package com.blakebr0.mysticalagriculture.api.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record SoulJarComponent(ResourceLocation type, double souls) {
    public static final MapCodec<SoulJarComponent> MAP_CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    ResourceLocation.CODEC.fieldOf("type").forGetter(SoulJarComponent::type),
                    Codec.DOUBLE.fieldOf("souls").forGetter(SoulJarComponent::souls)
            ).apply(builder, SoulJarComponent::new)
    );
    public static final Codec<SoulJarComponent> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<FriendlyByteBuf, SoulJarComponent> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            SoulJarComponent::type,
            ByteBufCodecs.DOUBLE,
            SoulJarComponent::souls,
            SoulJarComponent::new
    );
}
