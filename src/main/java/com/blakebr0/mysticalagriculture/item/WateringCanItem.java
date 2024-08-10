package com.blakebr0.mysticalagriculture.item;

import com.blakebr0.cucumber.item.BaseWateringCanItem;
import com.blakebr0.mysticalagriculture.config.ModConfigs;

import java.util.function.Function;

public class WateringCanItem extends BaseWateringCanItem {
    public WateringCanItem(int range, double chance) {
        super(range, chance);
    }

    public WateringCanItem(int range, double chance, Function<Properties, Properties> properties) {
        super(range, chance, properties);
    }

    @Override
    protected boolean allowFakePlayerWatering() {
        return ModConfigs.FAKE_PLAYER_WATERING.get();
    }
}
