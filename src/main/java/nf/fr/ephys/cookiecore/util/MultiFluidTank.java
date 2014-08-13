package nf.fr.ephys.cookiecore.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import nf.fr.ephys.cookiecore.helpers.NBTHelper;

import java.util.ArrayList;

public class MultiFluidTank implements IFluidHandler, IWritable, IFluidTank {
	private ArrayList<FluidStack> stacks = new ArrayList<>();

	private int capacity;
	private int totalFluidAmount = 0;

	public MultiFluidTank(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		for (int i = 0; i < stacks.size(); i++) {
			FluidStack stack = stacks.get(i);

			if (stack.isFluidEqual(resource)) {
				int toDrain = Math.min(resource.amount, stack.amount);

				FluidStack drained = stack.copy();

				drained.amount = toDrain;

				if (doDrain) {
					stack.amount -= toDrain;
					totalFluidAmount -= toDrain;

					if (stack.amount <= 0)
						stacks.remove(i);
				}

				return drained;
			}
		}

		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		FluidTankInfo[] info = new FluidTankInfo[stacks.size()];

		for (int i = 0; i < info.length; i++) {
			info[i] = new FluidTankInfo(stacks.get(i), capacity);
		}

		return info;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("nbFluids", stacks.size());
		nbt.setInteger("capacity", capacity);

		for (int i = 0; i < stacks.size(); i++) {
			NBTHelper.setWritable(nbt, Integer.toString(i), stacks.get(i));
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		totalFluidAmount = 0;
		capacity = nbt.getInteger("capacity");

		int nbFluids = nbt.getInteger("nbFluids");
		stacks = new ArrayList<>(nbFluids);

		for (int i = 0; i < stacks.size(); i++) {
			FluidStack fluid = NBTHelper.getFluidStack(nbt, Integer.toString(i));
			stacks.set(i, fluid);

			if (fluid != null)
				totalFluidAmount += fluid.amount;
		}
	}

	public int getNbFluids() {
		return stacks.size();
	}

	public FluidStack getFluid(int i) {
		return stacks.get(i);
	}

	@Override
	public FluidStack getFluid() {
		return stacks.size() == 0 ? null : stacks.get(0);
	}

	public int getFluidAmount() {
		return totalFluidAmount;
	}

	public int getCapacity() {
		return capacity;
	}

	@Override
	public FluidTankInfo getInfo() {
		if (stacks.isEmpty()) return null;

		return new FluidTankInfo(stacks.get(0), capacity);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		int canFill = Math.min(capacity - totalFluidAmount, resource.amount);

		if (doFill) {
			FluidStack stackToFill = null;

			for (FluidStack stack : stacks) {
				if (stack.isFluidEqual(resource)) {
					stackToFill = stack;
					break;
				}
			}

			if (stackToFill == null) {
				stacks.add(new FluidStack(resource.fluidID, canFill, resource.tag));
			} else {
				stackToFill.amount += canFill;
			}

			totalFluidAmount += canFill;
		}

		return canFill;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (stacks.isEmpty()) return null;

		FluidStack stack = stacks.get(0);

		int toDrain = Math.min(maxDrain, stack.amount);

		FluidStack drained = stack.copy();

		drained.amount = toDrain;

		if (doDrain) {
			stack.amount -= toDrain;
			totalFluidAmount -= toDrain;

			if (stack.amount <= 0)
				stacks.remove(0);
		}

		return drained;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
