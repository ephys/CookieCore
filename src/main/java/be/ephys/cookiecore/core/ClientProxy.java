package be.ephys.cookiecore.core;

import be.ephys.cookiecore.helpers.DebugHelper;
import be.ephys.cookiecore.registries.FlatPresetRegistry;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.gen.FlatLayerInfo;

public class ClientProxy extends CommonProxy {

    @Override
    void postInit() {
        if (DebugHelper.debug) {
            // remove unwanted presets in my test worlds
            FlatPresetRegistry.removeByName("Classic Flat");
            FlatPresetRegistry.removeByName("Tunnelers' Dream");
            FlatPresetRegistry.removeByName("Water World");
            FlatPresetRegistry.removeByName("Overworld");
            FlatPresetRegistry.removeByName("Snowy Kingdom");
            FlatPresetRegistry.removeByName("Bottomless Pit");
            FlatPresetRegistry.removeByName("Desert");
        }

        // add my own presets
        FlatPresetRegistry.addPresetAt(0,
                FlatPresetRegistry.buildPreset(
                        "Cookie Realmn",
                        Item.getItemFromBlock(Blocks.STAINED_HARDENED_CLAY),
                        0,
                        Biomes.MUSHROOM_ISLAND,
                        null,
                        new FlatLayerInfo[]{
                                new FlatLayerInfo(63, Blocks.STAINED_HARDENED_CLAY),
                                new FlatLayerInfo(1, Blocks.BEDROCK)
                        }
                )
        );
    }
}
