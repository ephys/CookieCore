package nf.fr.ephys.cookiecore.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import nf.fr.ephys.cookiecore.helpers.NBTHelper;

public class Inventory implements IInventory, IWritable {
	private int stackSize;
	private ItemStack[] stacks;

	public Inventory(int nbSlots) {
		this(nbSlots, 64);
	}

	public Inventory(int nbSlots, int stackSize) {
		stacks = new ItemStack[nbSlots];

		this.stackSize = stackSize;
	}

	@Override
	public int getSizeInventory() {
		return stacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {

	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return stackSize;
	}

	@Override
	public void markDirty() {}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return true;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		for (int i = 0; i < stacks.length; i++) {
			if (stacks[i] == null) continue;

			NBTHelper.setWritable(nbt, Integer.toString(i), stacks[i]);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		for (int i = 0; i < stacks.length; i++) {

		}
	}
}
