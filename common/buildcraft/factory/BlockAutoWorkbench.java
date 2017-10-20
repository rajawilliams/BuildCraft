/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.factory;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.FMLCommonHandler;
import buildcraft.BuildCraftFactory;
import buildcraft.api.events.BlockInteractionEvent;
import buildcraft.core.BlockBuildCraft;
import buildcraft.core.GuiIds;
import buildcraft.core.IItemPipe;

public class BlockAutoWorkbench extends BlockBuildCraft {

	public BlockAutoWorkbench() {
		super(Material.WOOD);
		setHardness(3.0F);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityplayer, EnumHand activeHand, ItemStack heldItem, EnumFacing par6, float par7, float par8, float par9) {
		super.onBlockActivated(world, pos, state, entityplayer, null, null, par6, par7, par8, par9);
		BlockInteractionEvent event = new BlockInteractionEvent(entityplayer, pos, state);
		FMLCommonHandler.instance().bus().post(event);
		if (event.isCanceled()) {
			return false;
		}

		// Drop through if the player is sneaking
		if (entityplayer.isSneaking()) {
			return false;
		}

		if (heldItem.getItem() != null) { // Welp. That was simple.
			if (heldItem.getItem() instanceof IItemPipe) {
				return false;
			}
		}

		if (!world.isRemote) {
			entityplayer.openGui(BuildCraftFactory.instance, GuiIds.AUTO_CRAFTING_TABLE, world, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileAutoWorkbench();
	}
}
