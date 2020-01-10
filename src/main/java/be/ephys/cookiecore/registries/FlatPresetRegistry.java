package be.ephys.cookiecore.registries;

import net.minecraft.client.gui.screen.FlatPresetsScreen;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class FlatPresetRegistry {

  public static int presetCount() {
    return FlatPresetsScreen.FLAT_WORLD_PRESETS.size();
  }

  public static void addPresetAt(int pos, FlatPresetsScreen.LayerItem preset) {
    FlatPresetsScreen.FLAT_WORLD_PRESETS.add(pos, preset);
  }

  public static FlatPresetsScreen.LayerItem buildPreset(
    String name, Item icon, int iconMeta, Biome biome,
    List<String> structures, FlatLayerInfo[] layers
  ) {

    if (structures == null) {
      structures = Collections.emptyList();
    }

    FlatPresetsScreen.registerPreset(name, icon, iconMeta, biome, structures, layers);

    return FlatPresetsScreen.FLAT_WORLD_PRESETS.remove(presetCount() - 1);
  }

  public static FlatPresetsScreen.LayerItem removeByName(String name) {
    for (int i = 0; i < presetCount(); i++) {
      FlatPresetsScreen.LayerItem preset = FlatPresetsScreen.FLAT_WORLD_PRESETS.get(i);

      if (preset.name.equals(name)) {
        return FlatPresetsScreen.FLAT_WORLD_PRESETS.remove(i);
      }
    }

    return null;
  }

  public static FlatPresetsScreen.LayerItem getByName(String name) {
    for (int i = 0; i < presetCount(); i++) {
      FlatPresetsScreen.LayerItem preset = FlatPresetsScreen.FLAT_WORLD_PRESETS.get(i);

      if (preset.name.equals(name)) {
        return preset;
      }
    }

    return null;
  }
}
