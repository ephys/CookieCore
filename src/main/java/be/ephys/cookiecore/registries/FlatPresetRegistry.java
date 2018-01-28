package be.ephys.cookiecore.registries;

import net.minecraft.client.gui.GuiFlatPresets;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatLayerInfo;

import java.util.List;

public class FlatPresetRegistry {

    public static int presetCount() {
        return GuiFlatPresets.FLAT_WORLD_PRESETS.size();
    }

    @SuppressWarnings("unchecked")
    public static void addPresetAt(int pos, GuiFlatPresets.LayerItem preset) {
        GuiFlatPresets.field_146431_f.add(pos, preset);
    }

    public static GuiFlatPresets.LayerItem buildPreset(
            String name, Item icon, Biome biome,
            List structures, FlatLayerInfo[] layers
    ) {

        // registerPreset(String name, Item icon, int iconMetadata, Biome biome, List<String> features, FlatLayerInfo... layers)
        GuiFlatPresets.func_146421_a(name, icon, biome, structures, layers);

        return (GuiFlatPresets.LayerItem) GuiFlatPresets.field_146431_f.remove(presetCount() - 1);
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
