/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * The BuildCraft API is distributed under the terms of the MIT License.
 * Please check the contents of the license, which should be located
 * as "LICENSE.API" in the BuildCraft source code distribution.
 */
package buildcraft.api.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class BlockPlacedDownEvent extends Event {
	public EntityPlayer player;
	public IBlockState state;
	public BlockPos pos;

	public BlockPlacedDownEvent(EntityPlayer player, IBlockState state, BlockPos pos) {
		this.player = player;
		this.state = state;
		this.pos = pos;
	}
}
