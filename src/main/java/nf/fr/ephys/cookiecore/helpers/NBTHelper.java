package nf.fr.ephys.cookiecore.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.UUID;

import nf.fr.ephys.cookiecore.util.IWritable;

public class NBTHelper {

  public static NBTTagCompound getNBT(ItemStack stack) {
    if (!stack.hasTagCompound()) {
      stack.setTagCompound(new NBTTagCompound());
    }

    return stack.getTagCompound();
  }

  // ==========================================================================
  // Setters
  // ==========================================================================
  public static void setIntArray(ItemStack stack, String name, int[] arr) {
    if (stack == null) {
      return;
    }

    setIntArray(getNBT(stack), name, arr);
  }

  public static void setIntArray(NBTTagCompound nbt, String name, int[] arr) {
    if (arr == null) {
      return;
    }

    nbt.setIntArray(name, arr);
  }

  public static void setInt(ItemStack stack, String name, int value) {
    if (stack == null) {
      return;
    }

    getNBT(stack).setInteger(name, value);
  }

  public static void setByte(ItemStack stack, String name, byte value) {
    if (stack == null) {
      return;
    }

    getNBT(stack).setByte(name, value);
  }

  public static void setString(ItemStack stack, String name, String str) {
    if (stack == null) {
      return;
    }

    setString(getNBT(stack), name, str);
  }

  public static void setString(NBTTagCompound nbt, String name, String str) {
    if (str == null) {
      return;
    }

    nbt.setString(name, str);
  }

  public static void setClass(ItemStack stack, String name, Class<?> clazz) {
    if (stack == null) {
      return;
    }

    setString(getNBT(stack), name, clazz.getName());
  }

  public static void setClass(NBTTagCompound nbt, String name, Class<?> clazz) {
    if (clazz == null) {
      return;
    }

    setString(nbt, name, clazz.getName());
  }

  public static void setEntity(NBTTagCompound nbt, String name, Entity entity) {
    setUUID(nbt, name, entity.getUniqueID());
  }

  public static void setUUID(NBTTagCompound nbt, String name, UUID uuid) {
    NBTTagCompound uuidNBT = new NBTTagCompound();

    uuidNBT.setLong("UUIDMost", uuid.getMostSignificantBits());
    uuidNBT.setLong("UUIDLeast", uuid.getLeastSignificantBits());

    nbt.setTag(name, uuidNBT);
  }

  // --------------------------------------------------------------------------
  // implements writeToNBT
  // --------------------------------------------------------------------------
  public static void setWritable(NBTTagCompound nbt, String name, TileEntity input) {
    if (input == null) {
      return;
    }

    NBTTagCompound writableNBT = new NBTTagCompound();
    input.writeToNBT(writableNBT);
    setWritable(nbt, name, writableNBT);
  }

  public static void setWritable(NBTTagCompound nbt, String name, Entity input) {
    if (input == null) {
      return;
    }

    NBTTagCompound writableNBT = new NBTTagCompound();
    input.writeToNBT(writableNBT);
    setWritable(nbt, name, writableNBT);
  }

  public static void setWritable(NBTTagCompound nbt, String name, ItemStack input) {
    if (input == null) {
      return;
    }

    NBTTagCompound writableNBT = new NBTTagCompound();
    input.writeToNBT(writableNBT);
    setWritable(nbt, name, writableNBT);
  }

  public static void setWritable(NBTTagCompound nbt, String name, WorldSavedData input) {
    if (input == null) {
      return;
    }

    NBTTagCompound writableNBT = new NBTTagCompound();
    input.writeToNBT(writableNBT);
    setWritable(nbt, name, writableNBT);
  }

  public static void setWritable(NBTTagCompound nbt, String name, FluidTank input) {
    if (input == null) {
      return;
    }

    NBTTagCompound writableNBT = new NBTTagCompound();
    input.writeToNBT(writableNBT);

    setWritable(nbt, name, writableNBT);
  }

  public static void setWritable(NBTTagCompound nbt, String name, FluidStack input) {
    if (input == null) {
      return;
    }

    NBTTagCompound writableNBT = new NBTTagCompound();
    input.writeToNBT(writableNBT);

    setWritable(nbt, name, writableNBT);
  }

  public static void setWritable(NBTTagCompound nbt, String name, IWritable input) {
    if (input == null) {
      return;
    }

    NBTTagCompound writableNBT = new NBTTagCompound();
    input.writeToNBT(writableNBT);

    setWritable(nbt, name, writableNBT);
  }

  /**
   * Writes input in nbt.name
   *
   * @param nbt   The NBT compound to write to
   * @param name  The key for the input
   * @param input The NBT to write
   */
  public static void setWritable(NBTTagCompound nbt, String name, NBTTagCompound input) {
    if (input == null) {
      return;
    }

    nbt.setTag(name, input);
  }

  // ==========================================================================
  // Getters
  // ==========================================================================

  // --------------------------------------------------------------------------
  // String
  // --------------------------------------------------------------------------
  public static String getString(NBTTagCompound nbt, String name, String def) {
    if (!nbt.hasKey(name)) {
      return def;
    }

    return nbt.getString(name);
  }

  public static String getString(NBTTagCompound nbt, String name) {
    return getString(nbt, name, null);
  }

  public static String getString(ItemStack stack, String name) {
    if (stack == null) {
      return null;
    }

    return getString(getNBT(stack), name, null);
  }

  public static String getString(ItemStack stack, String name, String def) {
    if (stack == null) {
      return def;
    }

    return getString(getNBT(stack), name, def);
  }

  // --------------------------------------------------------------------------
  // int Array
  // --------------------------------------------------------------------------
  public static int[] getIntArray(ItemStack stack, String name) {
    if (stack == null) {
      return null;
    }

    return getIntArray(getNBT(stack), name, null);
  }

  public static int[] getIntArray(ItemStack stack, String name, int[] def) {
    if (stack == null) {
      return def;
    }

    return getIntArray(getNBT(stack), name, def);
  }

  public static int[] getIntArray(NBTTagCompound nbt, String name) {
    return getIntArray(nbt, name, null);
  }

  public static int[] getIntArray(NBTTagCompound nbt, String name, int[] def) {
    if (!nbt.hasKey(name)) {
      return def;
    }

    return nbt.getIntArray(name);
  }

  // --------------------------------------------------------------------------
  // Classes
  // --------------------------------------------------------------------------
  public static Class<?> getClass(NBTTagCompound nbt, String name) {
    return getClass(nbt, name, null);
  }

  public static Class<?> getClass(NBTTagCompound nbt, String name, Class<?> def) {
    String className = getString(nbt, name);

    if (className == null) {
      return def;
    }

    Class<?> clazz;
    try {
      clazz = Class.forName(className);
    } catch (ClassNotFoundException e) {
      clazz = null;
      e.printStackTrace();
    }

    return clazz;
  }


  public static Class<?> getClass(ItemStack stack, String name) {
    if (stack == null) {
      return null;
    }

    return getClass(getNBT(stack), name, null);
  }

  public static Class<?> getClass(ItemStack stack, String name, Class<?> def) {
    if (stack == null) {
      return def;
    }

    return getClass(getNBT(stack), name, def);
  }

  public static Entity getEntity(NBTTagCompound nbt, String name, Entity def) {
    UUID entityUUID = getUUID(nbt, name, null);

    if (entityUUID == null) {
      return def;
    }

    Entity entity = EntityHelper.getEntityByUUID(entityUUID);

    if (entity == null) {
      return def;
    }

    return entity;
  }

  public static UUID getUUID(NBTTagCompound nbt, String name, UUID def) {
    if (!nbt.hasKey(name)) {
      return def;
    }

    NBTTagCompound uuidNBT = nbt.getCompoundTag(name);

    return new UUID(uuidNBT.getLong("UUIDMost"), uuidNBT.getLong("UUIDLeast"));
  }

  // --------------------------------------------------------------------------
  // Boolean
  // --------------------------------------------------------------------------
  public static boolean getBoolean(NBTTagCompound nbt, String name, boolean def) {
    if (!nbt.hasKey(name)) {
      return def;
    }

    return nbt.getBoolean(name);
  }

  public static boolean getBoolean(ItemStack stack, String name, boolean def) {
    if (stack == null) {
      return def;
    }

    return getBoolean(getNBT(stack), name, def);
  }

  // --------------------------------------------------------------------------
  // Double
  // --------------------------------------------------------------------------
  public static double getDouble(NBTTagCompound nbt, String name, double def) {
    if (!nbt.hasKey(name)) {
      return def;
    }

    return nbt.getDouble(name);
  }

  // --------------------------------------------------------------------------
  // Integer
  // --------------------------------------------------------------------------
  public static int getInt(ItemStack stack, String name, int def) {
    if (stack == null) {
      return def;
    }

    return getInt(getNBT(stack), name, def);
  }

  public static int getInt(NBTTagCompound nbt, String name, int def) {
    if (!nbt.hasKey(name)) {
      return def;
    }

    return nbt.getInteger(name);
  }

  // --------------------------------------------------------------------------
  // implements readFromNBT
  // --------------------------------------------------------------------------
  public static NBTTagCompound getWritable(NBTTagCompound nbt, String name) {
    return getWritable(nbt, name, null);
  }

  public static NBTTagCompound getWritable(NBTTagCompound nbt, String name, NBTTagCompound def) {
    if (!nbt.hasKey(name)) {
      return def;
    }

    return nbt.getCompoundTag(name);
  }

  public static ItemStack getItemStack(NBTTagCompound nbt, String name) {
    return getItemStack(nbt, name, null);
  }

  public static ItemStack getItemStack(NBTTagCompound nbt, String name, ItemStack def) {
    if (!nbt.hasKey(name)) {
      return def;
    }

    return ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(name));
  }

  public static FluidStack getFluidStack(NBTTagCompound nbt, String name) {
    return getFluidStack(nbt, name, null);
  }

  public static FluidStack getFluidStack(NBTTagCompound nbt, String name, FluidStack def) {
    if (!nbt.hasKey(name)) {
      return def;
    }

    return FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag(name));
  }
}
