package nf.fr.ephys.cookiecore.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import nf.fr.ephys.cookiecore.helpers.NBTHelper;

/**
 * This class is for internal use in anything that requires an inventory
 * Use it to delegate IInventory methods
 */
public class Inventory implements IInventory, IWritable {
	private int stackSize;
	public ItemStack[] stacks;

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
		return stacks[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (stacks[slot] == null) return null;

		if (amount > stacks[slot].stackSize)
			amount = stacks[slot].stackSize;

		ItemStack emptied = stacks[slot].splitStack(amount);

		if (stacks[slot].stackSize <= 0)
			setInventorySlotContents(slot, null);

		return emptied;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = stacks[slot];

		setInventorySlotContents(slot, null);

		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		stacks[slot] = stack;
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
			stacks[i] = NBTHelper.getItemStack(nbt, Integer.toString(i));
		}
	}
}
