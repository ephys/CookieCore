package nf.fr.ephys.cookiecore.helpers;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Random;

public class BlockHelper {
	private static final int OPPOSITE_SIDES[] = { 1, 0, 3, 2, 5, 4 };

	public static void setBiome(World world, int x, int z, int biomeID) {
		Chunk chunk = world.getChunkFromBlockCoords(x, z);
		byte[] biomes = chunk.getBiomeArray();

		biomes[(z & 15) << 4 | (x & 15)] = (byte) (biomeID & 255);

		chunk.setBiomeArray(biomes);
		chunk.setChunkModified();

		if (world.isRemote)
			world.markBlockRangeForRenderUpdate(x, 0, z, x, 255, z);
	}

	public static boolean isUnbreakable(Block block, World world, int x, int y, int z) {
		return block.getBlockHardness(world, x, y, z) < 0;
	}

	/**
	 * returns the opposite side of side
	 * @param side  the side to oppose ?
	 * @return      the opposite side
	 */
	public static int getOppositeSide(int side) {
		return OPPOSITE_SIDES[side];
	}

	public static int[] getAdjacentBlock(int x, int y, int z, int side) {
		/*switch (side) {
			case 0:
				y--;
				break;
			case 1:
				y++;
				break;
			case 2:
				z--;
				break;
			case 3:
				z++;
				break;
			case 4:
				x--;
				break;
			case 5:
				x++;
				break;
		}*/

		// I'm a mad man

		if ((side & 0b100) == 0b100)
			x += ((side & 0b001) == 0) ? -1 : 1;
		else if ((side & 0b010) == 0b010)
			z += ((side & 0b001) == 0) ? -1 : 1;
		else
			y += ((side & 0b001) == 0) ? -1 : 1;

		return new int[] {x, y, z};
	}

	public static int[] getAdjacentBlock(int[] coords, int side) {
		return getAdjacentBlock(coords[0], coords[1], coords[2], side);
	}

	public static Vec3 relativePos(TileEntity te, Entity e) {
		return Vec3.createVectorHelper(te.xCoord - e.posX, te.yCoord - e.posY, te.zCoord - e.posZ);
	}

	public static Vec3 relativePos(Entity e, TileEntity te) {
		return Vec3.createVectorHelper(e.posX - te.xCoord, e.posY - te.yCoord, e.posZ - te.zCoord);
	}

	public static Vec3 relativePos(TileEntity te1, TileEntity te2) {
		return Vec3.createVectorHelper(te1.xCoord - te2.xCoord, te1.yCoord - te2.yCoord, te1.zCoord - te2.zCoord);
	}

	public static Vec3 relativePos(Entity e1, Entity e2) {
		return Vec3.createVectorHelper(e1.posX - e2.posX, e1.posY - e2.posY, e1.posZ - e2.posZ);
	}

	public static int[] getCoords(TileEntity te) {
		return new int[] { te.xCoord, te.yCoord, te.zCoord };
	}

	public static double[] getCoords(Entity e) {
		return new double[] { e.posX, e.posY, e.posZ };
	}

	public static int orientationToMetadataXZ(double rotationYaw) {
		int l = MathHelper.floor_double((rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		return l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
	}
}