package nf.fr.ephys.cookiecore.helpers;

import com.google.common.collect.BiMap;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.RegistryNamespaced;
import nf.fr.ephys.cookiecore.common.CookieCore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * https://github.com/CoFH/CoFHLib/blob/master/src/main/java/cofh/util/RegistryUtils.java
 */
public class RegistryHelper {
	@SuppressWarnings("unchecked")
	public static Object overwriteEntry(RegistryNamespaced registry, String name, Object newEntry) {
		try {
			Object oldEntry = registry.getObject(name);
			int id = registry.getIDForObject(oldEntry);

			BiMap map = (BiMap) registry.registryObjects;

			registry.underlyingIntegerMap.func_148746_a(newEntry, id);

			map.remove(name);
			map.forcePut(name, newEntry);

			BiMap reverse = (BiMap) registry.field_148758_b;
			reverse.remove(oldEntry);
			reverse.forcePut(newEntry, name);

			CookieCore.getLogger().info("RegistryUtils::overwriteEntry: overwriting '" + name + "' with " + newEntry.toString() + ": Operation success");

			return oldEntry;
		} catch (Exception e) {
			CookieCore.getLogger().error("RegistryUtils::overwriteEntry: overwriting '" + name + ": Operation failure (this is a bug !)");
			e.printStackTrace();

			return null;
		}
	}

	public static Block overwriteBlock(String name, Block newBlock) {
		Block oldBlock = (Block) overwriteEntry(Block.blockRegistry, name, newBlock);

		if (oldBlock == null) return null;

		if (name.startsWith("minecraft:")) {
			Field[] blocks = Blocks.class.getDeclaredFields();

			try {
				for (Field blockField : blocks) {
					if (blockField.get(null) == oldBlock) {
						blockField.setAccessible(true);

						Field modifiersField = Field.class.getDeclaredField("modifiers");
						modifiersField.setAccessible(true);
						modifiersField.setInt(blockField, blockField.getModifiers() & ~Modifier.FINAL);

						blockField.set(null, newBlock);

						CookieCore.getLogger().warn("RegistryUtils::overwriteBlock: overwrote net.minecraft.init.Blocks entry");

						break;
					}
				}
			} catch (Exception e) {
				CookieCore.getLogger().warn("RegistryUtils::overwriteBlock: failed to overwrite net.minecraft.init.Blocks entry");

				e.printStackTrace();
			}
		}

		Object oldItem = Item.itemRegistry.getObject(name);

		if (oldItem instanceof ItemBlock) {
			((ItemBlock) oldItem).field_150939_a = newBlock;
		}

		return oldBlock;
	}

	public static int removeItemRecipe(ItemStack stack) {
		List crafts = CraftingManager.getInstance().getRecipeList();

		int nbRemoved = 0;
		for (int i = 0; i < crafts.size(); i++) {
			if (crafts.get(i) instanceof IRecipe) {
				ItemStack output = ((IRecipe) crafts.get(i)).getRecipeOutput();

				if (output == null) continue;

				if (output.isItemEqual(stack)) {
					crafts.remove(i);
					nbRemoved++;
				}
			}
		}

		return nbRemoved;
	}
}