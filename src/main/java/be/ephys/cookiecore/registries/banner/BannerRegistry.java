package be.ephys.cookiecore.registries.banner;

import net.minecraft.item.Item;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.registries.IRegistryDelegate;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class BannerRegistry {
  public static final Map<IRegistryDelegate<Item>, BannerPattern> PATTERNS = new LinkedHashMap<>();

  public static void addPattern(String name, String id, IItemProvider craftingItem) {
    BannerPattern pattern = BannerPattern.create(name.toUpperCase(Locale.ROOT), name, id, false);
    PATTERNS.put(craftingItem.asItem().delegate, pattern);
  }
}
