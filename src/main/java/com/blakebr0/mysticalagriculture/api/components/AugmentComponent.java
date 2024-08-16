package com.blakebr0.mysticalagriculture.api.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record AugmentComponent(ResourceLocation id, int slot) {
    public static final MapCodec<AugmentComponent> MAP_CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(AugmentComponent::id),
                    Codec.INT.fieldOf("slot").forGetter(AugmentComponent::slot)
            ).apply(builder, AugmentComponent::new)
    );
    public static final Codec<AugmentComponent> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<FriendlyByteBuf, AugmentComponent> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            AugmentComponent::id,
            ByteBufCodecs.INT,
            AugmentComponent::slot,
            AugmentComponent::new
    );
    public static final Codec<List<AugmentComponent>> EQUIPPED_CODEC = Codec.list(CODEC);
    public static final StreamCodec<FriendlyByteBuf, List<AugmentComponent>> EQUIPPED_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
}
