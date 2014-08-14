package nf.fr.ephys.cookiecore.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import nf.fr.ephys.cookiecore.helpers.NBTHelper;

import java.util.ArrayList;

public class MultiFluidTank implements IFluidHandler, IWritable, IFluidTank {
	private ArrayList<FluidStack> stacks = new ArrayList<>();
	private FluidTankInfo[] fluidTankInfos;

	private int capacity;
	private int totalFluidAmount = 0;

	public MultiFluidTank(int capacity) {
		this.capacity = capacity;
		updateFluidTankInfos();
	}

	public void updateFluidTankInfos() {
		fluidTankInfos = new FluidTankInfo[stacks.size() + 1];

		for (int i = 0; i < stacks.size(); i++) {
			fluidTankInfos[i] = new FluidTankInfo(stacks.get(i), stacks.get(i).amount);
		}

		fluidTankInfos[stacks.size()] = new FluidTankInfo(null, capacity - totalFluidAmount);
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

					updateFluidTankInfos();
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
		return fluidTankInfos;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("nbFluids", stacks.size());
		nbt.setInteger("capacity", capacity);

		for (int i = 0; i < stacks.size(); i++) {
			NBTHelper.setWritable(nbt, "f" + i, stacks.get(i));
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		totalFluidAmount = 0;
		capacity = nbt.getInteger("capacity");

		int nbFluids = nbt.getInteger("nbFluids");
		stacks = new ArrayList<>(nbFluids);

		for (int i = 0; i < nbFluids; i++) {
			FluidStack fluid = NBTHelper.getFluidStack(nbt, "f" + i);

			if (fluid != null) {
				totalFluidAmount += fluid.amount;
				stacks.add(i, fluid);
			}
		}

		updateFluidTankInfos();
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
		return fluidTankInfos[0];
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

			updateFluidTankInfos();
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

			updateFluidTankInfos();
		}

		return drained;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public void setStackPos(Fluid fluid, int pos) {
		for (int i = 0; i < stacks.size(); i++) {
			FluidStack stack = stacks.get(i);
			if (stack.getFluid().equals(fluid)) {
				stacks.remove(i);

				if (pos > i)
					pos--;

				stacks.add(pos, stack);

				return;
			}
		}
	}
}
