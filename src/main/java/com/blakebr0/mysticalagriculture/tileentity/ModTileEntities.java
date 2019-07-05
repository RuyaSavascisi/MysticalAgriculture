package com.blakebr0.mysticalagriculture.tileentity;

import com.blakebr0.mysticalagriculture.block.ModBlocks;
import com.blakebr0.mysticalagriculture.client.tesr.InfusionAltarRenderer;
import com.blakebr0.mysticalagriculture.client.tesr.InfusionPedestalRenderer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModTileEntities {
    public static final TileEntityType<InfusionPedestalTileEntity> INFUSION_PEDESTAL = TileEntityType.Builder.create(InfusionPedestalTileEntity::new, ModBlocks.INFUSION_PEDESTAL).build(null);
    public static final TileEntityType<InfusionAltarTileEntity> INFUSION_ALTAR = TileEntityType.Builder.create(InfusionAltarTileEntity::new, ModBlocks.INFUSION_ALTAR).build(null);

    public static void onRegisterTypes(RegistryEvent.Register<TileEntityType<?>> event) {
        IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();

        registry.register(INFUSION_PEDESTAL.setRegistryName("infusion_pedestal"));
        registry.register(INFUSION_ALTAR.setRegistryName("infusion_altar"));
    }

    public static void onClientSetup() {
        ClientRegistry.bindTileEntitySpecialRenderer(InfusionPedestalTileEntity.class, new InfusionPedestalRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(InfusionAltarTileEntity.class, new InfusionAltarRenderer());
    }
}