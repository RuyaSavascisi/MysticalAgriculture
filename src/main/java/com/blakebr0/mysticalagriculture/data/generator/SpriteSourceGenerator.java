package com.blakebr0.mysticalagriculture.data.generator;

import com.blakebr0.mysticalagriculture.api.crop.CropTextures;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SpriteSourceGenerator extends SpriteSourceProvider {
    public SpriteSourceGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, String modid, ExistingFileHelper fileHelper) {
        super(output, lookup, modid, fileHelper);
    }

    @Override
    protected void gather() {
        this.atlas(SpriteSourceProvider.BLOCKS_ATLAS)
                .addSource(new SingleFile(CropTextures.FLOWER_DUST_BLANK, Optional.empty()))
                .addSource(new SingleFile(CropTextures.FLOWER_FACE_BLANK, Optional.empty()))
                .addSource(new SingleFile(CropTextures.FLOWER_INGOT_BLANK, Optional.empty()))
                .addSource(new SingleFile(CropTextures.FLOWER_ROCK_BLANK, Optional.empty()))
                .addSource(new SingleFile(CropTextures.ESSENCE_FLAME_BLANK, Optional.empty()))
                .addSource(new SingleFile(CropTextures.ESSENCE_GEM_BLANK, Optional.empty()))
                .addSource(new SingleFile(CropTextures.ESSENCE_DUST_BLANK, Optional.empty()))
                .addSource(new SingleFile(CropTextures.ESSENCE_DIAMOND_BLANK, Optional.empty()))
                .addSource(new SingleFile(CropTextures.ESSENCE_INGOT_BLANK, Optional.empty()))
                .addSource(new SingleFile(CropTextures.ESSENCE_ROD_BLANK, Optional.empty()))
                .addSource(new SingleFile(CropTextures.ESSENCE_TALL_GEM_BLANK, Optional.empty()))
                .addSource(new SingleFile(CropTextures.ESSENCE_QUARTZ_BLANK, Optional.empty()))
                .addSource(new SingleFile(CropTextures.SEED_BLANK, Optional.empty()));
    }
}
