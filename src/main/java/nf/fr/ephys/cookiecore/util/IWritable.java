package nf.fr.ephys.cookiecore.util;

import net.minecraft.nbt.NBTTagCompound;

public interface IWritable {
	/**
	 * Call this with NBTHelper.setWritable !
	 * (or at least store this in a sub tag compound)
	 */
	public void writeToNBT(NBTTagCompound nbt);

	public void readFromNBT(NBTTagCompound nbt);
}
