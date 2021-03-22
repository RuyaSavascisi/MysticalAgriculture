package com.blakebr0.mysticalagriculture.block;

import com.blakebr0.cucumber.block.BaseTileEntityBlock;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.util.VoxelShapeBuilder;
import com.blakebr0.mysticalagriculture.tileentity.InfusionPedestalTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class InfusionPedestalBlock extends BaseTileEntityBlock {
    public static final VoxelShape PEDESTAL_SHAPE = new VoxelShapeBuilder()
            .cuboid(2.0, 0.0, 2.0, 5.0, 2.0, 5.0).cuboid(11.0, 0.0, 2.0, 14.0, 2.0, 5.0)
            .cuboid(2.0, 0.0, 11.0, 5.0, 2.0, 14.0).cuboid(11.0, 0.0, 11.0, 14.0, 2.0, 14.0)
            .cuboid(3.0, 2.0, 3.0, 13.0, 4.0, 13.0).cuboid(4.0, 4.0, 4.0, 12.0, 14.0, 12.0)
            .cuboid(3.0, 14.0, 3.0, 13.0, 16.0, 5.0).cuboid(3.0, 14.0, 11.0, 13.0, 16.0, 13.0)
            .cuboid(3.0, 14.0, 5.0, 5.0, 16.0, 11.0).cuboid(11.0, 14.0, 5.0, 13.0, 16.0, 11.0).build();

    public InfusionPedestalBlock() {
        super(Material.ROCK, SoundType.STONE, 10.0F, 12.0F, ToolType.PICKAXE);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new InfusionPedestalTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof InfusionPedestalTileEntity) {
            InfusionPedestalTileEntity pedestal = (InfusionPedestalTileEntity) tile;
            BaseItemStackHandler inventory = pedestal.getInventory();
            ItemStack input = inventory.getStackInSlot(0);
            ItemStack held = player.getHeldItem(hand);

            if (input.isEmpty() && !held.isEmpty()) {
                inventory.setStackInSlot(0, StackHelper.withSize(held, 1, false));
                player.setHeldItem(hand, StackHelper.shrink(held, 1, false));
                world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            } else if (!input.isEmpty()) {
                inventory.setStackInSlot(0, ItemStack.EMPTY);
                ItemEntity item = new ItemEntity(world, player.getPosX(), player.getPosY(), player.getPosZ(), input);
                item.setNoPickupDelay();
                world.addEntity(item);
            }
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof InfusionPedestalTileEntity) {
                InfusionPedestalTileEntity altar = (InfusionPedestalTileEntity) tile;
                InventoryHelper.dropItems(world, pos, altar.getInventory().getStacks());
            }
        }

        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return PEDESTAL_SHAPE;
    }
}
