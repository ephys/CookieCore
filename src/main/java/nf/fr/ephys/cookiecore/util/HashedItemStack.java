package nf.fr.ephys.cookiecore.util;

import net.minecraft.item.ItemStack;

/**
 * HashedItemStack is just a wrapper for ItemStacks when you need to use them as keys in hashmaps
 */
public class HashedItemStack {
	private ItemStack itemStack;

	public HashedItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof HashedItemStack && ((HashedItemStack) obj).itemStack.isItemEqual(itemStack);
	}

	@Override
	public int hashCode() {
		return itemStack.getItem().hashCode();
	}

	public HashedItemStack setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;

		return this;
	}
}