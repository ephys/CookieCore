package be.ephys.cookiecore.registries;

import net.minecraft.client.gui.screen.FlatPresetsScreen;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraft.world.gen.feature.structure.Structure;
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
    ITextComponent name,
    IItemProvider icon,
    RegistryKey<Biome> biome,
    List<Structure<?>> structures,
    boolean p_238640_4_,
    boolean p_238640_5_,
    boolean p_238640_6_,
    FlatLayerInfo[] layers
  ) {

    if (structures == null) {
      structures = Collections.emptyList();
    }

    FlatPresetsScreen.func_238640_a_(name, icon, biome, structures, p_238640_4_, p_238640_5_, p_238640_6_, layers);

    return FlatPresetsScreen.FLAT_WORLD_PRESETS.remove(presetCount() - 1);
  }

  public static FlatPresetsScreen.LayerItem removeByName(String name) {
    return removeByName(new TranslationTextComponent(name));
  }

  public static FlatPresetsScreen.LayerItem removeByName(ITextComponent name) {
    for (int i = 0; i < presetCount(); i++) {
      FlatPresetsScreen.LayerItem preset = FlatPresetsScreen.FLAT_WORLD_PRESETS.get(i);

      if (preset.name.equals(name)) {
        return FlatPresetsScreen.FLAT_WORLD_PRESETS.remove(i);
      }
    }

    return null;
  }

  public static FlatPresetsScreen.LayerItem getByName(ITextComponent name) {
    for (int i = 0; i < presetCount(); i++) {
      FlatPresetsScreen.LayerItem preset = FlatPresetsScreen.FLAT_WORLD_PRESETS.get(i);

      if (preset.name.equals(name)) {
        return preset;
      }
    }

    return null;
  }
}
