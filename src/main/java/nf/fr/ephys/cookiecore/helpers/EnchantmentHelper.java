package nf.fr.ephys.cookiecore.helpers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Map;

public class EnchantmentHelper {

  @SuppressWarnings("unchecked")
  public static boolean hasEnchant(ItemStack is, Enchantment enchant) {
    if (is == null || enchant == null) {
      return false;
    }

    Map enchantments = net.minecraft.enchantment.EnchantmentHelper.getEnchantments(is);

    for (int id : (Collection<Integer>) enchantments.keySet()) {
      if (id == enchant.effectId) {
        return true;
      }
    }

    return false;
  }
}
