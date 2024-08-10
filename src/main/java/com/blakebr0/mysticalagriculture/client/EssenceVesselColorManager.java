package com.blakebr0.mysticalagriculture.client;

import com.blakebr0.cucumber.helper.ParsingHelper;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.google.common.base.Stopwatch;
import com.google.gson.JsonParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class EssenceVesselColorManager implements PreparableReloadListener {
    public static final EssenceVesselColorManager INSTANCE = new EssenceVesselColorManager();

    private final HashMap<String, Integer> colors = new HashMap<>();

    @SubscribeEvent
    public void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(this);
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        return CompletableFuture.runAsync(() -> {
            if (!ModLoader.hasErrors()) {
                this.load(manager);
            }
        }, backgroundExecutor).thenCompose(barrier::wait);
    }

    public int getColor(ItemStack stack) {
        var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return this.colors.getOrDefault(id.toString(), 0xFFFFFF);
    }

    private void load(ResourceManager manager) {
        var stopwatch = Stopwatch.createStarted();
        var resources = manager.listResources("essence_vessel_colors.json", s -> s.getPath().endsWith(".json"));

        this.colors.clear();

        for (var resource : resources.entrySet()) {
            try (var reader = resource.getValue().openAsReader()) {
                var json = JsonParser.parseReader(reader).getAsJsonObject();

                for (var entry : json.entrySet()) {
                    var item = entry.getKey();
                    var color = ParsingHelper.parseHex(entry.getValue().getAsString(), item);

                    this.colors.put(item, color);
                }
            } catch (IOException e) {
                MysticalAgriculture.LOGGER.error("Failed to load {}", resource.getKey(), e);
            }
        }

        MysticalAgriculture.LOGGER.info("Loaded {} essence vessel colors in {} ms", this.colors.size(), stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
    }
}
