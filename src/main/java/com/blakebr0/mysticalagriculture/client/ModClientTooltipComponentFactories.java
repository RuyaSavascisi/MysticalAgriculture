package com.blakebr0.mysticalagriculture.client;

import com.blakebr0.mysticalagriculture.client.handler.AugmentTooltipHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

public final class ModClientTooltipComponentFactories {
    @SubscribeEvent
    public void onRegisterClientTooltipComponentFactoriesEvent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(AugmentTooltipHandler.AugmentToolTypesComponent.class, c -> c);
    }
}
