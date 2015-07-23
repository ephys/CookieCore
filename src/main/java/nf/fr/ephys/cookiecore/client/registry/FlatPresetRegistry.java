package nf.fr.ephys.cookiecore.client.registry;

import net.minecraft.client.gui.GuiFlatPresets;
import net.minecraft.item.Item;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.FlatLayerInfo;

import java.util.List;

public class FlatPresetRegistry {

  public static int nbPresets() {
    return GuiFlatPresets.field_146431_f.size();
  }

  @SuppressWarnings("unchecked")
  public static void addPresetAt(int pos, GuiFlatPresets.LayerItem preset) {
    GuiFlatPresets.field_146431_f.add(pos, preset);
  }

  public static GuiFlatPresets.LayerItem buildPreset(String name, Item icon, BiomeGenBase biome,
                                                     List structures, FlatLayerInfo[] layers) {
    GuiFlatPresets.func_146421_a(name, icon, biome, structures, layers);

    return (GuiFlatPresets.LayerItem) GuiFlatPresets.field_146431_f.remove(nbPresets() - 1);
  }

  public static GuiFlatPresets.LayerItem removeByName(String name) {
    for (int i = 0; i < GuiFlatPresets.field_146431_f.size(); i++) {
      GuiFlatPresets.LayerItem
          preset =
          (GuiFlatPresets.LayerItem) GuiFlatPresets.field_146431_f.get(i);

      if (preset.field_148232_b.equals(name)) {
        return (GuiFlatPresets.LayerItem) GuiFlatPresets.field_146431_f.remove(i);
      }
    }

    return null;
  }

  public static GuiFlatPresets.LayerItem getByName(String name) {
    for (int i = 0; i < GuiFlatPresets.field_146431_f.size(); i++) {
      GuiFlatPresets.LayerItem
          preset =
          (GuiFlatPresets.LayerItem) GuiFlatPresets.field_146431_f.get(i);

      if (preset.field_148232_b.equals(name)) {
        return (GuiFlatPresets.LayerItem) GuiFlatPresets.field_146431_f.get(i);
      }
    }

    return null;
  }
}