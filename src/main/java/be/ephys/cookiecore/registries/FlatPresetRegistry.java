package be.ephys.cookiecore.registries;

import net.minecraft.client.gui.screens.PresetFlatWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class FlatPresetRegistry {

  public static int presetCount() {
    return PresetFlatWorldScreen.PRESETS.size();
  }

  public static void addPresetAt(int pos, PresetFlatWorldScreen.PresetInfo preset) {
    PresetFlatWorldScreen.PRESETS.add(pos, preset);
  }

  public static PresetFlatWorldScreen.PresetInfo buildPreset(
    Component name,
    ItemLike icon,
    ResourceKey<Biome> biome,
    Set<ResourceKey<StructureSet>> structures,
    boolean p_238640_4_,
    boolean p_238640_5_,
    FlatLayerInfo[] layers
  ) {

    if (structures == null) {
      structures = Collections.emptySet();
    }

    PresetFlatWorldScreen.preset(name, icon, biome, structures, p_238640_4_, p_238640_5_, layers);

    return PresetFlatWorldScreen.PRESETS.remove(presetCount() - 1);
  }

  public static PresetFlatWorldScreen.PresetInfo removeByName(String name) {
    return removeByName(new TranslatableComponent(name));
  }

  public static PresetFlatWorldScreen.PresetInfo removeByName(Component name) {
    for (int i = 0; i < presetCount(); i++) {
      PresetFlatWorldScreen.PresetInfo preset = PresetFlatWorldScreen.PRESETS.get(i);

      if (preset.name.equals(name)) {
        return PresetFlatWorldScreen.PRESETS.remove(i);
      }
    }

    return null;
  }

  public static PresetFlatWorldScreen.PresetInfo getByName(Component name) {
    for (int i = 0; i < presetCount(); i++) {
      PresetFlatWorldScreen.PresetInfo preset = PresetFlatWorldScreen.PRESETS.get(i);

      if (preset.name.equals(name)) {
        return preset;
      }
    }

    return null;
  }
}
