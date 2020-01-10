package be.ephys.cookiecore.core;

import be.ephys.cookiecore.config.Config;
import be.ephys.cookiecore.config.ConfigSynchronizer;
import be.ephys.cookiecore.helpers.DebugHelper;
import be.ephys.cookiecore.registries.FlatPresetRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CookieCore.MODID)
public class CookieCore {
  public static final String MODID = "cookiecore";

  private static Logger logger = LogManager.getLogger(MODID);

  @Config(description = "Enable the CookieRealm Flat world preset", side = ModConfig.Type.CLIENT)
  @Config.BooleanDefault(true)
  public static ForgeConfigSpec.BooleanValue enableTerracottaWorldPreset;

  public static Logger getLogger() {
    return logger;
  }

  public CookieCore() {
    ConfigSynchronizer.synchronizeConfig();

    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInit);
  }

  public void postInit(FMLClientSetupEvent event) {

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
    if (enableTerracottaWorldPreset.get()) {
      FlatPresetRegistry.addPresetAt(0,
        FlatPresetRegistry.buildPreset(
          "Cookie Realm",
          Blocks.WHITE_TERRACOTTA,
          Biomes.MUSHROOM_FIELDS,
          null,
          new FlatLayerInfo[]{
            new FlatLayerInfo(63, Blocks.WHITE_TERRACOTTA),
            new FlatLayerInfo(1, Blocks.BEDROCK)
          }
        )
      );
    }
  }

  // TODO
//  @NetworkCheckHandler
//  public boolean acceptConnection(Map<String, String> modList, Side side) {
//
//    // Mod can be used on both client & server even if the other party doesn't have it installed
//    return true;
//  }
}
