package be.ephys.cookiecore;

import be.ephys.cookiecore.helpers.DebugHelper;
import be.ephys.cookiecore.registries.FlatPresetRegistry;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = CookieCore.MODID, version = CookieCore.VERSION)
public class CookieCore {
    public static final String MODID = "cookiecore";
    public static final String VERSION = "2.0.0";

    @Mod.Instance(MODID)
    public static CookieCore instance;

    private Logger logger = LogManager.getLogger(MODID);

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (event.getSide().equals(Side.CLIENT)) {
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

    public static Logger getLogger() {
        return instance.logger;
    }
}
