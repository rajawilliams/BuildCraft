/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport.pipes;

import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.util.EnumFacing;
import buildcraft.BuildCraftTransport;
import buildcraft.transport.IPipeConnectionForced;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeIconProvider;
import buildcraft.transport.PipeTransportPower;
import buildcraft.transport.TileGenericPipe;

public class PipePowerSandstone extends Pipe<PipeTransportPower> implements IPipeConnectionForced {

	public PipePowerSandstone(Item item) {
		super(new PipeTransportPower(), item);
		transport.initFromPipe(getClass());
	}

	/*@Override
	@SideOnly(Side.CLIENT)
	public IIconProvider getIconProvider() {
		return BuildCraftTransport.instance.pipeIconProvider;
	}*/

	@Override
	public int getIconIndex(EnumFacing direction) {
		return PipeIconProvider.TYPE.PipePowerSandstone.ordinal();
	}

    @Override
    public boolean canPipeConnect(TileEntity tile, EnumFacing side) {
        return (tile instanceof TileGenericPipe) && super.canPipeConnect(tile, side);
    }

    @Override
    public boolean ignoreConnectionOverrides(EnumFacing with) {
        return true;
    }
}
