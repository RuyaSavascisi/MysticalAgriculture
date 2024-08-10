package com.blakebr0.mysticalagriculture.network.payloads;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.util.RecipeIngredientCache;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record ReloadIngredientCachePayload(Map<RecipeType<?>, Map<Item, List<Ingredient>>> caches, Set<Item> validVesselItems) implements CustomPacketPayload {
    public static final Type<ReloadIngredientCachePayload> TYPE = new Type<>(MysticalAgriculture.resource("reload_ingredient_cache"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ReloadIngredientCachePayload> STREAM_CODEC = StreamCodec.of(
            ReloadIngredientCachePayload::toNetwork, ReloadIngredientCachePayload::fromNetwork
    );

    private static ReloadIngredientCachePayload fromNetwork(RegistryFriendlyByteBuf buffer) {
        var caches = new HashMap<RecipeType<?>, Map<Item, List<Ingredient>>>();
        var types = buffer.readVarInt();

        for (var i = 0; i < types; i++) {
            var type = BuiltInRegistries.RECIPE_TYPE.get(buffer.readResourceLocation());
            var items = buffer.readVarInt();

            caches.put(type, new HashMap<>());

            for (var j = 0; j < items; j++) {
                var item = BuiltInRegistries.ITEM.get(buffer.readResourceLocation());
                var ingredients = buffer.readVarInt();

                for (var k = 0; k < ingredients; k++) {
                    var cache = caches.get(type).computeIfAbsent(item, l -> new ArrayList<>());
                    var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);

                    cache.add(ingredient);
                }
            }
        }

        var validVesselItems = new HashSet<Item>();
        var items = buffer.readVarInt();

        for (var i = 0; i < items; i++) {
            var item = BuiltInRegistries.ITEM.get(buffer.readResourceLocation());

            validVesselItems.add(item);
        }

        return new ReloadIngredientCachePayload(caches, validVesselItems);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, ReloadIngredientCachePayload payload) {
        buffer.writeVarInt(payload.caches.size());

        for (var entry : payload.caches.entrySet()) {
            var type = BuiltInRegistries.RECIPE_TYPE.getKey(entry.getKey());
            var caches = entry.getValue();

            assert type != null;

            buffer.writeResourceLocation(type);
            buffer.writeVarInt(caches.size());

            for (var cache : caches.entrySet()) {
                var item = BuiltInRegistries.ITEM.getKey(cache.getKey());
                var ingredients = cache.getValue();

                buffer.writeResourceLocation(item);
                buffer.writeVarInt(ingredients.size());

                for (var ingredient : ingredients) {
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
                }
            }
        }

        buffer.writeVarInt(payload.validVesselItems.size());

        for (var item : payload.validVesselItems) {
            var id = BuiltInRegistries.ITEM.getKey(item);

            buffer.writeResourceLocation(id);
        }
    }

    @Override
    public Type<ReloadIngredientCachePayload> type() {
        return TYPE;
    }

    public static void handleClient(ReloadIngredientCachePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            RecipeIngredientCache.INSTANCE.setCaches(payload.caches);
            RecipeIngredientCache.INSTANCE.setValidVesselItems(payload.validVesselItems);
        });
    }
}
