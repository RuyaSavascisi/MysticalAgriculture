package com.blakebr0.mysticalagriculture.handler;

import com.blakebr0.mysticalagriculture.init.ModTileEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class RegisterCapabilityHandler {
    @SubscribeEvent
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.AWAKENING_ALTAR.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.AWAKENING_PEDESTAL.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.FURNACE.get(), (block, direction) -> block.getSidedInventory(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.HARVESTER.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.INFUSION_ALTAR.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.INFUSION_PEDESTAL.get(), (block, direction) -> block.getInventory());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.REPROCESSOR.get(), (block, direction) -> block.getSidedInventory(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.SOUL_EXTRACTOR.get(), (block, direction) -> block.getSidedInventory(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.SOULIUM_SPAWNER.get(), (block, direction) -> block.getSidedInventory(direction)); // TODO: 1.21 sided

        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModTileEntities.FURNACE.get(), (block, direction) -> block.getEnergy());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModTileEntities.HARVESTER.get(), (block, direction) -> block.getEnergy());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModTileEntities.REPROCESSOR.get(), (block, direction) -> block.getEnergy());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModTileEntities.SOUL_EXTRACTOR.get(), (block, direction) -> block.getEnergy());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModTileEntities.SOULIUM_SPAWNER.get(), (block, direction) -> block.getEnergy());
    }
}
