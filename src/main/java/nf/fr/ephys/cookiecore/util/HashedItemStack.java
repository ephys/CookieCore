package nf.fr.ephys.cookiecore.util;

import net.minecraft.item.ItemStack;

/**
 * Wrapper for ItemStacks when you need to use them as keys in Maps, overwrites {@link
 * #equals(Object)} and {@link #hashCode()}.
 */
public class HashedItemStack {

  private ItemStack itemStack;

  /**
   * Constructs an <tt>HashedItemStack</tt> with the specified ItemStack. capacity and load factor.
   *
   * @param itemStack The itemstack for which the {@link #hashCode()} and {@link #equals(Object)}
   *                  methods are to be provied.
   * @throws IllegalArgumentException The itemstack is null.
   */
  public HashedItemStack(ItemStack itemStack) {
    if (itemStack == null) {
      throw new IllegalArgumentException(
          "Trying to create an hash wrapper for an itemstack but given itemstack is null.");
    }

    this.itemStack = itemStack;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof HashedItemStack && ((HashedItemStack) obj).itemStack
        .isItemEqual(itemStack);
  }

  @Override
  public int hashCode() {
    return itemStack.getItem().hashCode();
  }

  /**
   * Changes the ItemStack for which the {@link #hashCode()} and {@link #equals(Object)} methods are
   * to be provied.
   *
   * @param itemStack The new ItemStack.
   * @return this.
   */
  public HashedItemStack setItemStack(ItemStack itemStack) {
    this.itemStack = itemStack;

    return this;
  }

  /**
   * Returns the ItemStack wrapped by an instance of this class.
   */
  public ItemStack getItemStack() {
    return itemStack;
  }

  /**
   * Immutable version of the {@link HashedItemStack}.
   */
  public static class ImmutableHashedItemStack extends HashedItemStack {

    public ImmutableHashedItemStack(ItemStack itemStack) {
      super(itemStack);
    }

    @Override
    public HashedItemStack setItemStack(ItemStack itemStack) {
      throw new UnsupportedOperationException();
    }
  }
}