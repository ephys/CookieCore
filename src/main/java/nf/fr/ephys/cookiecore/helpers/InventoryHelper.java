package nf.fr.ephys.cookiecore.helpers;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class InventoryHelper {
	public static void insertItem(IInventory inventory, ItemStack stack) {
		insertItem(inventory, stack, 0);
	}

	public static boolean insertItem(IInventory inventory, ItemStack toInsert, int side) {
		boolean isSided = inventory instanceof ISidedInventory;

		int[] accessibleSlots = isSided ? ((ISidedInventory) inventory).getAccessibleSlotsFromSide(side) : null;

		if (isSided && accessibleSlots == null) return false;

		int size = isSided ? accessibleSlots.length : inventory.getSizeInventory();

		int[] insertInSlots = new int[size];
		int totalInserted = 0;

		for (int i = 0; i < size && totalInserted < toInsert.stackSize; i++) {
			int slot = isSided ? accessibleSlots[i] : i;

			ItemStack slotStack = inventory.getStackInSlot(slot);

			int inserted = 0;
			if (slotStack == null || slotStack.stackSize == 0) {
				inserted = Math.min(toInsert.stackSize - totalInserted, inventory.getInventoryStackLimit());

			} else if (slotStack.isItemEqual(toInsert)
					&& slotStack.stackSize < inventory.getInventoryStackLimit()
					&& slotStack.stackSize < slotStack.getMaxStackSize()) {
				inserted = Math.min(
						Math.min(toInsert.stackSize - totalInserted,
								inventory.getInventoryStackLimit() - slotStack.stackSize),
								slotStack.getMaxStackSize() - slotStack.stackSize);
			}

			insertInSlots[i] = inserted;
			totalInserted += totalInserted;
		}

		if (totalInserted != toInsert.stackSize) return false;

		for (int i = 0; i < insertInSlots.length; i++) {
			if (insertInSlots[i] == 0) continue;

			int slot = isSided ? accessibleSlots[i] : i;

			ItemStack slotStack = inventory.getStackInSlot(slot);

			if (slotStack == null || slotStack.stackSize == 0) {
				ItemStack stack = toInsert.copy();
				stack.stackSize = insertInSlots[i];

				inventory.setInventorySlotContents(slot, stack);
			} else {
				slotStack.stackSize += insertInSlots[i];
			}
		}

		inventory.markDirty();

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
