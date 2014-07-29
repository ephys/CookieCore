package nf.fr.ephys.cookiecore.helpers;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidHelper {
	public static Fluid getFluidForBlock(Block block) {
		// more hotfixing the broken vanilla fluid handling (fluid registry only has blocks for still water & still lava)
		if (block == Blocks.flowing_water)
			return FluidRegistry.WATER;

		if (block == Blocks.flowing_lava)
			return FluidRegistry.LAVA;

		return FluidRegistry.lookupFluidForBlock(block);
	}

	public static Block getBlockForFluid(Fluid fluid) {
		Block fluidBlock = fluid.getBlock();

		if (fluidBlock == Blocks.water)
			fluidBlock = Blocks.flowing_water;
		else if (fluidBlock == Blocks.lava)
			fluidBlock = Blocks.flowing_lava;

		return fluidBlock;
	}
}
