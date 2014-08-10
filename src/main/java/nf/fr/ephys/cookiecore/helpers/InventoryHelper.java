package nf.fr.ephys.cookiecore.helpers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.swing.text.html.parser.Entity;
import java.util.List;

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

	public static IInventory getInventoryAt(World world, double x, double y, double z) {
		IInventory inventory = getBlockInventoryAt(world, (int) x, (int) y, (int) z);

		if (inventory == null)
			inventory = getEntityInventoryAt(world, x, y, z);

		return inventory;
	}

	public static IInventory getBlockInventoryAt(World world, int x, int y, int z) {
		// special mojang bad code hotfix yay
		Block block = world.getBlock(x, y, z);
		if (world.getBlock(x, y, z) instanceof BlockChest) {
			IInventory chestInventory = ((BlockChest) block).func_149951_m(world, x, y, z);

			if (chestInventory != null) return chestInventory;
		}

		TileEntity te = world.getTileEntity(x, y, z);

		if (te instanceof IInventory) {
			return (IInventory) te;
		}

		return null;
	}

	// todo: check if the AABB is valid
	@SuppressWarnings("unchecked")
	public static IInventory getEntityInventoryAt(World world, double x, double y, double z) {
		List<IInventory> entities = world.selectEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(x, y, z, x, y, z), IEntitySelector.selectInventories);

		return (IInventory) MathHelper.getRandom(entities);
	}

	public static boolean isBlockEqual(ItemStack stack, World world, int x, int y, int z) {
		return world.getBlock(x, y, z).equals(Block.getBlockFromItem(stack.getItem())) && world.getBlockMetadata(x, y, z) == stack.getItemDamage();
	}

	public static boolean isBlockEqual(String oredictName, World world, int x, int y, int z) {
		int needle = OreDictionary.getOreID(oredictName);

		ItemStack stack = new ItemStack(world.getBlock(x, y, z), 1, world.getBlockMetadata(x, y, z));

		int[] haystack = OreDictionary.getOreIDs(stack);

		for (int id : haystack) {
			if (id == needle) return true;
		}

		return false;
	}

	public static boolean itemIsOre(ItemStack stack) {
		int[] ids = OreDictionary.getOreIDs(stack);

		for (int id : ids) {
			if (OreDictionary.getOreName(id).startsWith("ore"))
				return true;
		}

		return false;
	}
}
