package nf.fr.ephys.cookiecore.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import nf.fr.ephys.cookiecore.helpers.InventoryHelper;
import nf.fr.ephys.cookiecore.helpers.NBTHelper;

import java.util.Arrays;

/**
 * This class is for internal use in anything that requires an inventory
 * Use it to delegate IInventory methods
 */
public class SizeableInventory implements IInventory, IWritable {
	private int stackSize;

	private ItemStack[] stacks;
	private int nbStacks;

	public SizeableInventory(int nbSlots) {
		this(nbSlots, 64);
	}

	public SizeableInventory(int nbSlots, int stackSize) {
		nbStacks = nbSlots;
		stacks = new ItemStack[nbSlots];

		this.stackSize = stackSize;
	}

	/**
	 * Changes the amount of stack this inventory can hold
	 * @param size  the amount of stacks
	 */
	public void setInventorySize(int size) {
		nbStacks = size;

		if (stacks.length < size) {
			stacks = Arrays.copyOf(stacks, size);
		}
	}

	/**
	 * Sets the maximum stack size
	 * @param size  the stack size
	 */
	public void setMaxStackSize(int size) {
		stackSize = size;
	}

	/**
	 * Drops any item that stayed in the inventory after a resize to a smaller inventory
	 *
	 * @param world The world to drop the items in
	 * @param x     The x coords to drop the items at
	 * @param y     The y coords to drop the items at
	 * @param z     The z coords to drop the items at
	 */
	public void dumpOverflow(World world, int x, int y, int z) {
		for (int i = nbStacks; i < stacks.length; i++) {
			if (stacks[i] == null) continue;

			InventoryHelper.dropItem(stacks[i], world, x, y, z);

			stacks[i] = null;
		}
	}

	@Override
	public int getSizeInventory() {
		return nbStacks;
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

		if (stacks[slot].stackSize <= 0) {
			setInventorySlotContents(slot, null);
		}

		markDirty();

		return emptied;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = stacks[slot];

		setInventorySlotContents(slot, null);

		markDirty();

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
		nbt.setInteger("nbStacks", nbStacks);
		for (int i = 0; i < stacks.length; i++) {
			if (stacks[i] == null) continue;

			NBTHelper.setWritable(nbt, Integer.toString(i), stacks[i]);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.nbStacks = nbt.getInteger("nbStacks");
		this.stacks = new ItemStack[nbStacks];

		for (int i = 0; i < stacks.length; i++) {
			stacks[i] = NBTHelper.getItemStack(nbt, Integer.toString(i));
		}
	}
}
