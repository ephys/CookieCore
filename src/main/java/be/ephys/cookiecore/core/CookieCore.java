package be.ephys.cookiecore.core;

import be.ephys.cookiecore.config.Config;
import be.ephys.cookiecore.config.ConfigSynchronizer;
import be.ephys.cookiecore.helpers.DebugHelper;
import be.ephys.cookiecore.registries.FlatPresetRegistry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CookieCore.MODID)
public class CookieCore {
  public static final String MODID = "cookiecore";

  private static final Logger logger = LogManager.getLogger(MODID);

  @Config(description = "Enable the CookieRealm Flat world preset", side = ModConfig.Type.CLIENT)
  @Config.BooleanDefault(true)
  public static ForgeConfigSpec.BooleanValue enableTerracottaWorldPreset;

  public static Logger getLogger() {
    return logger;
  }

  public CookieCore() {
    ConfigSynchronizer.synchronizeConfig();

    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

    modBus.addListener(this::postInit);

    PAINTING_TYPES.register(modBus);
  }

  // TODO Move to Fundamental, as well as banner
  public static DeferredRegister<Motive> PAINTING_TYPES = DeferredRegister.create(ForgeRegistries.PAINTING_TYPES, CookieCore.MODID);
  public static RegistryObject<Motive> PAINTING_ZEN = PAINTING_TYPES.register("zen",()-> new Motive(16, 32));

  public void postInit(FMLClientSetupEvent event) {

    if (DebugHelper.debug) {
      // remove unwanted presets in my test worlds
      FlatPresetRegistry.removeByName("createWorld.customize.preset.classic_flat");
      FlatPresetRegistry.removeByName("createWorld.customize.preset.tunnelers_dream");
      FlatPresetRegistry.removeByName("createWorld.customize.preset.water_world");
      FlatPresetRegistry.removeByName("createWorld.customize.preset.overworld");
      FlatPresetRegistry.removeByName("createWorld.customize.preset.snowy_kingdom");
      FlatPresetRegistry.removeByName("createWorld.customize.preset.bottomless_pit");
      FlatPresetRegistry.removeByName("createWorld.customize.preset.desert");
      FlatPresetRegistry.removeByName("createWorld.customize.preset.the_void");
    }

    // add my own presets
    if (enableTerracottaWorldPreset.get()) {
      FlatPresetRegistry.addPresetAt(0,
        FlatPresetRegistry.buildPreset(
          new TranslatableComponent("cookiecore.superflat.cookie_realmn"),
          Blocks.WHITE_CONCRETE,
          Biomes.MUSHROOM_FIELDS,
          null,
          false,
          false,
          new FlatLayerInfo[]{
            new FlatLayerInfo(63, Blocks.WHITE_CONCRETE),
            new FlatLayerInfo(1, Blocks.BEDROCK)
          }
        )
      );
    }
  }
}
