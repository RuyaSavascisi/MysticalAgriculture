package com.blakebr0.mysticalagriculture.api;

import com.blakebr0.mysticalagriculture.api.registry.IAugmentRegistry;
import com.blakebr0.mysticalagriculture.api.registry.ICropRegistry;
import com.blakebr0.mysticalagriculture.api.registry.IMobSoulTypeRegistry;
import net.minecraft.resources.ResourceLocation;

public class MysticalAgricultureAPI {
    public static final String MOD_ID = "mysticalagriculture";

    private static ICropRegistry cropRegistry;
    private static IAugmentRegistry augmentRegistry;
    private static IMobSoulTypeRegistry soulTypeRegistry;

    /**
     * The registry in which all crops, crop tiers, and crop types are stored
     * @return the crop registry
     */
    public static ICropRegistry getCropRegistry() {
        return cropRegistry;
    }

    /**
     * The registry in which all augments are stored
     * @return the augment registry
     */
    public static IAugmentRegistry getAugmentRegistry() {
        return augmentRegistry;
    }

    /**
     * The registry in which all mob soul types are stored
     * @return the mob soul type registry
     */
    public static IMobSoulTypeRegistry getMobSoulTypeRegistry() {
        return soulTypeRegistry;
    }

    /**
     * Creates a {@link ResourceLocation} in the `mysticalagriculture` namespace
     * @param path the path
     * @return the resource location
     */
    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
