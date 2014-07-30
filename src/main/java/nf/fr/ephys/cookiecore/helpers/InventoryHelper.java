package nf.fr.ephys.cookiecore.helpers;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class InventoryHelper {
	public static int[] getUnSidedInventorySlots(IInventory inventory) {
		int[] slots = new int[inventory.getSizeInventory()];

		for (int i = 0; i < slots.length; i++) {
			slots[i] = i;
		}

		return slots;
	}

	public static boolean insertItem(IInventory inventory, ItemStack toInsert) {
		return insertItem(inventory, getUnSidedInventorySlots(inventory), toInsert);
	}

	public static boolean insertItem(IInventory inventory, ItemStack toInsert, int side) {
		if (inventory instanceof ISidedInventory) {
			return insertItem(inventory, ((ISidedInventory) inventory).getAccessibleSlotsFromSide(side), toInsert);
		}

		return insertItem(inventory, toInsert);
	}

	public static boolean insertItem(IInventory inventory, int[] slots, ItemStack toInsert) {
		int[] insertBySlot = new int[slots.length];
		int totalInserted = 0;

		int i;
		for (i = 0; i < slots.length && totalInserted < toInsert.stackSize; i++) {
			if (!inventory.isItemValidForSlot(slots[i], toInsert)) continue;

			ItemStack stack = inventory.getStackInSlot(slots[i]);

			if (stack != null && !stack.isItemEqual(toInsert)) continue;

			int emptyness = stack == null ? inventory.getInventoryStackLimit() : inventory.getInventoryStackLimit() - stack.stackSize;

			if (emptyness > toInsert.getMaxStackSize())
				emptyness = toInsert.getMaxStackSize();

			int inserted = Math.min(emptyness, toInsert.stackSize - totalInserted);

			insertBySlot[i] = inserted;
			totalInserted += inserted;
		}

		if (totalInserted != toInsert.stackSize) return false;

		for (int j = 0; j < i; j++) {
			ItemStack stack = inventory.getStackInSlot(slots[j]);

			if (stack == null) {
				stack = toInsert.copy();
			}

			stack.stackSize = insertBySlot[j];

			inventory.setInventorySlotContents(slots[j], stack);
		}

		return true;
	}

	public static void dropContents(IInventory te, World world, int x, int y, int z) {
		for (int i = 0; i < te.getSizeInventory(); ++i) {
			ItemStack itemstack = te.getStackInSlotOnClosing(i);

			dropItem(itemstack, world, x, y, z);
		}
	}

	public static void dropItem(ItemStack itemstack, World world, double x, double y, double z) {
		if (itemstack != null) {
			float randX = MathHelper.random.nextFloat() * 0.8F + 0.1F;
			float randY = MathHelper.random.nextFloat() * 0.8F + 0.1F;
			float randZ = MathHelper.random.nextFloat() * 0.8F + 0.1F;

			while (itemstack.stackSize > 0) {
				int k1 = MathHelper.random.nextInt(21) + 10;

				if (k1 > itemstack.stackSize) {
					k1 = itemstack.stackSize;
				}

				itemstack.stackSize -= k1;
				EntityItem entityitem = new EntityItem(world,
						(double) ((float) x + randX),
						(double) ((float) y + randY),
						(double) ((float) z + randZ),
						new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage())
				);

				float f3 = 0.05F;
				entityitem.motionX = (double) ((float) MathHelper.random.nextGaussian() * f3);
				entityitem.motionY = (double) ((float) MathHelper.random.nextGaussian() * f3 + 0.2F);
				entityitem.motionZ = (double) ((float) MathHelper.random.nextGaussian() * f3);

				if (itemstack.hasTagCompound()) {
					entityitem.getEntityItem().setTagCompound(
							(NBTTagCompound) itemstack.getTagCompound()
									.copy());
				}

				world.spawnEntityInWorld(entityitem);
			}
		}
	}

	public static void dropItem(ItemStack itemstack, EntityPlayer player) {
		dropItem(itemstack, player.worldObj, player.posX, player.posY, player.posZ);
	}
}
