package nf.fr.ephys.cookiecore.util;

import net.minecraft.nbt.NBTTagCompound;
import nf.fr.ephys.cookiecore.helpers.NBTHelper;

/**
 * Unifies the read/write to and from NBT methods for easier automation like with {@link NBTHelper#setWritable(NBTTagCompound, String, IWritable)}
 */
public interface IWritable {
	/**
	 * Write the object to the NBTTagCompound.
	 * @param nbt The nbt compound to write to.
	 */
	void writeToNBT(NBTTagCompound nbt);

	/**
	 * Read the object from the NBTTagCompound.
	 * @param nbt The nbt compound to read from.
	 */
	void readFromNBT(NBTTagCompound nbt);
}
