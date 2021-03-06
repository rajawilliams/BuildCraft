/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport.utils;

import io.netty.buffer.ByteBuf;

import net.minecraft.block.Block;

import net.minecraft.util.EnumFacing;

public class FacadeMatrix {

	private final Block[] blocks = new Block[EnumFacing.values().length];
	private final int[] blockMetas = new int[EnumFacing.values().length];
	private final boolean[] transparent = new boolean[EnumFacing.values().length];
	private boolean dirty = false;

	public FacadeMatrix() {
	}

	public void setFacade(EnumFacing direction, Block block, int blockMeta, boolean trans) {
		if (blocks[direction.ordinal()] != block || blockMetas[direction.ordinal()] != blockMeta || transparent[direction.ordinal()] != trans) {
			blocks[direction.ordinal()] = block;
			blockMetas[direction.ordinal()] = blockMeta;
			transparent[direction.ordinal()] = trans;
			dirty = true;
		}
	}

	public Block getFacadeBlock(EnumFacing direction) {
		return blocks[direction.ordinal()];
	}

	public int getFacadeMetaId(EnumFacing direction) {
		return blockMetas[direction.ordinal()];
	}

	public boolean getFacadeTransparent(EnumFacing direction) {
		return transparent[direction.ordinal()];
	}

	public boolean isDirty() {
		return dirty;
	}

	public void clean() {
		dirty = false;
	}

	public void writeData(ByteBuf data) {
		for (int i = 0; i < EnumFacing.values().length; i++) {
			if (blocks [i] == null) {
				data.writeShort(0);
			} else {
				data.writeShort(Block.blockRegistry.getIDForObject(blocks[i]));
			}
			data.writeBoolean(transparent[i]);
			data.writeByte(blockMetas[i]);
		}
	}

	public void readData(ByteBuf data) {
		for (int i = 0; i < EnumFacing.values().length; i++) {
			short id = data.readShort();
			
			Block block;
			
			if (id == 0) {
				block = null;
			} else {
				block = (Block) Block.blockRegistry.getObjectById(id);
			}
			
			if (blocks[i] != block) {
				blocks[i] = block;
				dirty = true;
			}
			boolean trans = data.readBoolean();
			if (transparent[i] != trans) {
				transparent[i] = trans;
				dirty = true;
			}
			byte meta = data.readByte();
			if (blockMetas[i] != meta) {
				blockMetas[i] = meta;
				dirty = true;
			}
		}
	}
}
