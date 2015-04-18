package nf.fr.ephys.cookiecore.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import nf.fr.ephys.cookiecore.helpers.InventoryHelper;
import nf.fr.ephys.cookiecore.helpers.NBTHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the {@link IInventory} interface with a resizeable slot count.
 */
public class SizeableInventory implements IInventory, IWritable {
	private int stackSize;
	private ItemStack[] slots;
	private int slotCount;

	/**
	 * Creates a new inventory with a given number of slots and a maximum stack size of 64.
	 *
	 * @param slotCount The number of slots in the inventory.
	 */
	public SizeableInventory(int slotCount) {
		this(slotCount, 64);
	}

	/**
	 * Creates a new inventory with a given number of slots and a given maximum stack size.
	 *
	 * @param slotCount The number of slots in the inventory.
	 * @param stackSize The maximum size a stack can have in the inventory.
	 */
	public SizeableInventory(int slotCount, int stackSize) {
		this.slotCount = slotCount;
		slots = new ItemStack[slotCount];

		this.stackSize = stackSize;
	}

	/**
	 * Sets the number of slots this inventory has.
	 *
	 * @param size The slot count.
	 */
	public void setInventorySize(int size) {
		this.slotCount = size;

		if (slots.length < size) { // only grow the array, only shrink after the other end fetched the overflow.
			slots = Arrays.copyOf(slots, size);
		}
	}

	/**
	 * Sets the maximum stack size.
	 *
	 * @param size the new stack size.
	 */
	public void setMaxStackSize(int size) {
		stackSize = size;
	}

	/**
	 * Drops any item that stayed in the inventory after a resize to a smaller slot count.
	 * This method is only callable once, the items will be removed from the inventory after that.
	 *
	 * @param world The world to drop the items in.
	 * @param x     The x coordinates of where to drop the items at.
	 * @param y     The y coordinates of where to drop the items at.
	 * @param z     The z coordinates of where to drop the items at.
	 */
	public void dumpOverflow(World world, int x, int y, int z) {
		for (int i = slotCount; i < slots.length; i++) {
			if (slots[i] == null) continue;

			InventoryHelper.dropItem(slots[i], world, x, y, z);

			slots[i] = null;
		}

		removeOverflow();
	}

	/**
	 * Returns the list of items that stayed in the inventory after a resize to a smaller slot count.
	 * This method is only callable once, the items will be removed from the inventory after that.
	 */
	public List<ItemStack> getOverflow() {
		List<ItemStack> items = new ArrayList<>();
		for (int i = slotCount; i < slots.length; i++) {
			if (slots[i] == null) continue;

			items.add(slots[i]);

			slots[i] = null;
		}

		removeOverflow();

		return items;
	}

	private void removeOverflow() {
		if (slotCount != slots.length) {
			slots = Arrays.copyOf(slots, slotCount);
		}
	}

	@Override
	public int getSizeInventory() {
		return slotCount;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return slots[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (slots[slot] == null) return null;

		if (amount > slots[slot].stackSize)
			amount = slots[slot].stackSize;

		ItemStack emptied = slots[slot].splitStack(amount);

		if (slots[slot].stackSize <= 0) {
			setInventorySlotContents(slot, null);
		} else {
			markDirty();
		}

		return emptied;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = slots[slot];

		setInventorySlotContents(slot, null);

		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		slots[slot] = stack;

		markDirty();
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
	public void markDirty() {
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return true;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("nbStacks", slotCount);
		for (int i = 0; i < slots.length; i++) {
			if (slots[i] == null) continue;

			NBTHelper.setWritable(nbt, Integer.toString(i), slots[i]);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.slotCount = nbt.getInteger("nbStacks");
		this.slots = new ItemStack[slotCount];

		for (int i = 0; i < slots.length; i++) {
			slots[i] = NBTHelper.getItemStack(nbt, Integer.toString(i));
		}
	}
}
