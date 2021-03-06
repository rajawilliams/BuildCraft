/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * The BuildCraft API is distributed under the terms of the MIT License.
 * Please check the contents of the license, which should be located
 * as "LICENSE.API" in the BuildCraft source code distribution.
 */
package buildcraft.api.blueprints;

import java.util.LinkedList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos; // Util to util.math broke 1.8.9 to 1.10.2 & 1.9.4 porting
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.WorldServer;
import buildcraft.api.core.BuildCraftAPI;

public class SchematicMask extends SchematicBlockBase {

	public boolean isConcrete = true;

	public SchematicMask () {

	}

	public SchematicMask (boolean isConcrete) {
		this.isConcrete = isConcrete;
	}

	@Override
	public void placeInWorld(IBuilderContext context, BlockPos pos, LinkedList<ItemStack> stacks) {
		if (isConcrete) {
			if (stacks.size() == 0 || !BuildCraftAPI.isSoftBlock(context.world(), pos)) {
				return;
			} else {
				ItemStack stack = stacks.getFirst();

				// force the block to be air block, in case it's just a soft
				// block which replacement is not straightforward
				context.world().setBlockToAir(pos);

				stack.onItemUse(
						BuildCraftAPI.proxy.getBuildCraftPlayer((WorldServer) context.world()).get(),
						context.world(), pos, EnumHand.MAIN_HAND, EnumFacing.UP, 0.0f, 0.0f, 0.0f); // Really Mojang.
			}
		} else {
			context.world().setBlockToAir(pos);
		}
	}

	@Override
	public boolean isAlreadyBuilt(IBuilderContext context, BlockPos pos) {
		if (isConcrete) {
			return !BuildCraftAPI.isSoftBlock(context.world(), pos);
		} else {
			return BuildCraftAPI.isSoftBlock(context.world(), pos);
		}
	}

	@Override
	public void writeSchematicToNBT(NBTTagCompound nbt, MappingRegistry registry) {
		nbt.setBoolean("isConcrete", isConcrete);
	}

	@Override
	public void readSchematicFromNBT(NBTTagCompound nbt,	MappingRegistry registry) {
		isConcrete = nbt.getBoolean("isConcrete");
	}
}
