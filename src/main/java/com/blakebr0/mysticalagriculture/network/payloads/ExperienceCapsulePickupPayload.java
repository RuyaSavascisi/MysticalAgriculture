package com.blakebr0.mysticalagriculture.network.payloads;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ExperienceCapsulePickupPayload() implements CustomPacketPayload {
    public static final Type<ExperienceCapsulePickupPayload> TYPE = new Type<>(MysticalAgriculture.resource("experience_pickup"));

    public static final StreamCodec<FriendlyByteBuf, ExperienceCapsulePickupPayload> STREAM_CODEC = StreamCodec.unit(new ExperienceCapsulePickupPayload());

    @Override
    public Type<ExperienceCapsulePickupPayload> type() {
        return TYPE;
    }

    public static void handleClient(ExperienceCapsulePickupPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            context.player().playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.1F, (Utils.RANDOM.nextFloat() - Utils.RANDOM.nextFloat()) * 0.35F + 0.9F);
        });
    }
}
