package be.ephys.cookiecore.registries;

import net.minecraft.client.gui.GuiFlatPresets;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class FlatPresetRegistry {

  public static int presetCount() {
    return GuiFlatPresets.FLAT_WORLD_PRESETS.size();
  }

  public static void addPresetAt(int pos, GuiFlatPresets.LayerItem preset) {
    GuiFlatPresets.FLAT_WORLD_PRESETS.add(pos, preset);
  }

  public static GuiFlatPresets.LayerItem buildPreset(
    String name, Item icon, int iconMeta, Biome biome,
    List<String> structures, FlatLayerInfo[] layers
  ) {

    if (structures == null) {
      structures = Collections.emptyList();
    }

    GuiFlatPresets.registerPreset(name, icon, iconMeta, biome, structures, layers);

    return GuiFlatPresets.FLAT_WORLD_PRESETS.remove(presetCount() - 1);
  }

  public static GuiFlatPresets.LayerItem removeByName(String name) {
    for (int i = 0; i < presetCount(); i++) {
      GuiFlatPresets.LayerItem preset = GuiFlatPresets.FLAT_WORLD_PRESETS.get(i);

      if (preset.name.equals(name)) {
        return GuiFlatPresets.FLAT_WORLD_PRESETS.remove(i);
      }
    }

    return null;
  }

  public static GuiFlatPresets.LayerItem getByName(String name) {
    for (int i = 0; i < presetCount(); i++) {
      GuiFlatPresets.LayerItem preset = GuiFlatPresets.FLAT_WORLD_PRESETS.get(i);

      if (preset.name.equals(name)) {
        return preset;
      }
    }

    return null;
  }
}
