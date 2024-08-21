package com.blakebr0.mysticalagriculture.client;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.crop.CropTextures;
import com.blakebr0.mysticalagriculture.init.ModItems;
import com.blakebr0.mysticalagriculture.item.ExperienceCapsuleItem;
import com.blakebr0.mysticalagriculture.item.SoulJarItem;
import com.blakebr0.mysticalagriculture.item.tool.EssenceBowItem;
import com.blakebr0.mysticalagriculture.item.tool.EssenceCrossbowItem;
import com.blakebr0.mysticalagriculture.item.tool.EssenceFishingRodItem;
import com.blakebr0.mysticalagriculture.registry.CropRegistry;
import com.google.common.base.Stopwatch;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public final class ModelHandler {
    private static final ResourceLocation MISSING_NO = ResourceLocation.fromNamespaceAndPath("minecraft", "missingno");

    @SubscribeEvent
    public void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
        for (int i = 0; i < 8; i++) {
            event.register(ModelResourceLocation.standalone(MysticalAgriculture.resource("block/mystical_resource_crop_" + i)));
            event.register(ModelResourceLocation.standalone(MysticalAgriculture.resource("block/mystical_mob_crop_" + i)));
        }

        for (var type : CropRegistry.getInstance().getTypes()) {
            event.register(ModelResourceLocation.standalone(CropTextures.FLOWER_INGOT_BLANK.withSuffix("_" + type.getName())));
            event.register(ModelResourceLocation.standalone(CropTextures.FLOWER_ROCK_BLANK.withSuffix("_" + type.getName())));
            event.register(ModelResourceLocation.standalone(CropTextures.FLOWER_DUST_BLANK.withSuffix("_" + type.getName())));
            event.register(ModelResourceLocation.standalone(CropTextures.FLOWER_FACE_BLANK.withSuffix("_" + type.getName())));
        }

        event.register(ModelResourceLocation.standalone(CropTextures.ESSENCE_INGOT_BLANK));
        event.register(ModelResourceLocation.standalone(CropTextures.ESSENCE_ROCK_BLANK));
        event.register(ModelResourceLocation.standalone(CropTextures.ESSENCE_DUST_BLANK));
        event.register(ModelResourceLocation.standalone(CropTextures.ESSENCE_GEM_BLANK));
        event.register(ModelResourceLocation.standalone(CropTextures.ESSENCE_TALL_GEM_BLANK));
        event.register(ModelResourceLocation.standalone(CropTextures.ESSENCE_DIAMOND_BLANK));
        event.register(ModelResourceLocation.standalone(CropTextures.ESSENCE_QUARTZ_BLANK));
        event.register(ModelResourceLocation.standalone(CropTextures.ESSENCE_FLAME_BLANK));
        event.register(ModelResourceLocation.standalone(CropTextures.ESSENCE_ROD_BLANK));

        event.register(ModelResourceLocation.standalone(CropTextures.SEED_BLANK));
    }

    @SubscribeEvent
    public void onModifyBakingResults(ModelEvent.ModifyBakingResult event) {
        var stopwatch = Stopwatch.createStarted();
        var registry = event.getModels();
        var cropModels = new HashMap<ResourceLocation, BakedModel[]>();

        for (var cropType : CropRegistry.getInstance().getTypes()) {
            cropModels.put(cropType.getId(), IntStream.range(0, 7)
                    .mapToObj(i -> registry.get(ModelResourceLocation.standalone(cropType.getStemModel().withSuffix("_" + i))))
                    .toArray(BakedModel[]::new));
        }

        for (var crop : CropRegistry.getInstance().getCrops()) {
            var textures = crop.getTextures();
            var crops = crop.getCropBlock();
            var cropId = BuiltInRegistries.BLOCK.getKey(crops);

            {
                for (int i = 0; i < 7; i++) {
                    var location = new ModelResourceLocation(cropId, "age=" + i);
                    var bakedModel = registry.get(location);

                    if (bakedModel == null || bakedModel.getParticleIcon(ModelData.EMPTY).contents().name().equals(MISSING_NO)) {
                        var type = crop.getType().getId();
                        registry.replace(location, cropModels.get(type)[i]);
                    }
                }

                var location = new ModelResourceLocation(cropId, "age=7");
                var bakedModel = registry.get(location);

                if (bakedModel == null || bakedModel.getParticleIcon(ModelData.EMPTY).contents().name().equals(MISSING_NO)) {
                    var flower = textures.getFlowerTexture();
                    var type = crop.getType().getId();
                    var texture = ResourceLocation.fromNamespaceAndPath(type.getNamespace(), flower.getPath() + "_" + type.getPath());
                    var model = getBakedModel(texture, registry);

                    registry.replace(location, model);
                }
            }

            var essence = crop.getEssenceItem();
            var essenceId = BuiltInRegistries.ITEM.getKey(essence);

            {
                var location = ModelResourceLocation.inventory(essenceId);
                var bakedModel = registry.get(location);

                if (bakedModel == null || bakedModel.getParticleIcon(ModelData.EMPTY).contents().name().equals(MISSING_NO)) {
                    var texture = textures.getEssenceTexture();
                    var model = getBakedModel(texture, registry);

                    registry.replace(location, model);
                }
            }

            var seeds = crop.getSeedsItem();
            var seedsId = BuiltInRegistries.ITEM.getKey(seeds);

            {
                var location = ModelResourceLocation.inventory(seedsId);
                var bakedModel = registry.get(location);

                if (bakedModel == null || bakedModel.getParticleIcon(ModelData.EMPTY).contents().name().equals(MISSING_NO)) {
                    var texture = textures.getSeedTexture();
                    var model = getBakedModel(texture, registry);

                    registry.replace(location, model);
                }
            }
        }

        stopwatch.stop();

        MysticalAgriculture.LOGGER.info("Model replacement took {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ModItems.EXPERIENCE_CAPSULE.get(), ResourceLocation.withDefaultNamespace("fill"), ExperienceCapsuleItem.getFillPropertyGetter());
            ItemProperties.register(ModItems.SOUL_JAR.get(), ResourceLocation.withDefaultNamespace("fill"), SoulJarItem.getFillPropertyGetter());
            ItemProperties.register(ModItems.INFERIUM_BOW.get(), ResourceLocation.withDefaultNamespace("pull"), EssenceBowItem.getPullPropertyGetter());
            ItemProperties.register(ModItems.INFERIUM_BOW.get(), ResourceLocation.withDefaultNamespace("pulling"), EssenceBowItem.getPullingPropertyGetter());
            ItemProperties.register(ModItems.INFERIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("pull"), EssenceCrossbowItem.getPullPropertyGetter());
            ItemProperties.register(ModItems.INFERIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
            ItemProperties.register(ModItems.INFERIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
            ItemProperties.register(ModItems.INFERIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
            ItemProperties.register(ModItems.INFERIUM_FISHING_ROD.get(), ResourceLocation.withDefaultNamespace("cast"), EssenceFishingRodItem.getCastPropertyGetter());
            ItemProperties.register(ModItems.PRUDENTIUM_BOW.get(), ResourceLocation.withDefaultNamespace("pull"), EssenceBowItem.getPullPropertyGetter());
            ItemProperties.register(ModItems.PRUDENTIUM_BOW.get(), ResourceLocation.withDefaultNamespace("pulling"), EssenceBowItem.getPullingPropertyGetter());
            ItemProperties.register(ModItems.PRUDENTIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("pull"), EssenceCrossbowItem.getPullPropertyGetter());
            ItemProperties.register(ModItems.PRUDENTIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
            ItemProperties.register(ModItems.PRUDENTIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
            ItemProperties.register(ModItems.PRUDENTIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
            ItemProperties.register(ModItems.PRUDENTIUM_FISHING_ROD.get(), ResourceLocation.withDefaultNamespace("cast"), EssenceFishingRodItem.getCastPropertyGetter());
            ItemProperties.register(ModItems.TERTIUM_BOW.get(), ResourceLocation.withDefaultNamespace("pull"), EssenceBowItem.getPullPropertyGetter());
            ItemProperties.register(ModItems.TERTIUM_BOW.get(), ResourceLocation.withDefaultNamespace("pulling"), EssenceBowItem.getPullingPropertyGetter());
            ItemProperties.register(ModItems.TERTIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("pull"), EssenceCrossbowItem.getPullPropertyGetter());
            ItemProperties.register(ModItems.TERTIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
            ItemProperties.register(ModItems.TERTIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
            ItemProperties.register(ModItems.TERTIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
            ItemProperties.register(ModItems.TERTIUM_FISHING_ROD.get(), ResourceLocation.withDefaultNamespace("cast"), EssenceFishingRodItem.getCastPropertyGetter());
            ItemProperties.register(ModItems.IMPERIUM_BOW.get(), ResourceLocation.withDefaultNamespace("pull"), EssenceBowItem.getPullPropertyGetter());
            ItemProperties.register(ModItems.IMPERIUM_BOW.get(), ResourceLocation.withDefaultNamespace("pulling"), EssenceBowItem.getPullingPropertyGetter());
            ItemProperties.register(ModItems.IMPERIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("pull"), EssenceCrossbowItem.getPullPropertyGetter());
            ItemProperties.register(ModItems.IMPERIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
            ItemProperties.register(ModItems.IMPERIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
            ItemProperties.register(ModItems.IMPERIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
            ItemProperties.register(ModItems.IMPERIUM_FISHING_ROD.get(), ResourceLocation.withDefaultNamespace("cast"), EssenceFishingRodItem.getCastPropertyGetter());
            ItemProperties.register(ModItems.SUPREMIUM_BOW.get(), ResourceLocation.withDefaultNamespace("pull"), EssenceBowItem.getPullPropertyGetter());
            ItemProperties.register(ModItems.SUPREMIUM_BOW.get(), ResourceLocation.withDefaultNamespace("pulling"), EssenceBowItem.getPullingPropertyGetter());
            ItemProperties.register(ModItems.SUPREMIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("pull"), EssenceCrossbowItem.getPullPropertyGetter());
            ItemProperties.register(ModItems.SUPREMIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
            ItemProperties.register(ModItems.SUPREMIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
            ItemProperties.register(ModItems.SUPREMIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
            ItemProperties.register(ModItems.SUPREMIUM_FISHING_ROD.get(), ResourceLocation.withDefaultNamespace("cast"), EssenceFishingRodItem.getCastPropertyGetter());
            ItemProperties.register(ModItems.AWAKENED_SUPREMIUM_BOW.get(), ResourceLocation.withDefaultNamespace("pull"), EssenceBowItem.getPullPropertyGetter());
            ItemProperties.register(ModItems.AWAKENED_SUPREMIUM_BOW.get(), ResourceLocation.withDefaultNamespace("pulling"), EssenceBowItem.getPullingPropertyGetter());
            ItemProperties.register(ModItems.AWAKENED_SUPREMIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("pull"), EssenceCrossbowItem.getPullPropertyGetter());
            ItemProperties.register(ModItems.AWAKENED_SUPREMIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
            ItemProperties.register(ModItems.AWAKENED_SUPREMIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
            ItemProperties.register(ModItems.AWAKENED_SUPREMIUM_CROSSBOW.get(), ResourceLocation.withDefaultNamespace("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
            ItemProperties.register(ModItems.AWAKENED_SUPREMIUM_FISHING_ROD.get(), ResourceLocation.withDefaultNamespace("cast"), EssenceFishingRodItem.getCastPropertyGetter());
        });
    }

    @Nullable // check for both standalone and inventory variants just in case
    private static BakedModel getBakedModel(ResourceLocation location, Map<ModelResourceLocation, BakedModel> registry) {
        var path = ModelResourceLocation.standalone(location);
        var model = registry.get(path);

        if (model != null)
            return model;

        path = ModelResourceLocation.inventory(location);
        model = registry.get(path);

        return model;
    }
}
