package be.ephys.cookiecore.core;

import be.ephys.cookiecore.config.Config;
import be.ephys.cookiecore.config.ConfigSynchronizer;
import be.ephys.cookiecore.helpers.DebugHelper;
import be.ephys.cookiecore.registries.banner.BannerRegistry;
import be.ephys.cookiecore.registries.FlatPresetRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.item.Items;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
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

    modBus.addListener(this::commonSetup);
    modBus.addListener(this::postInit);

    PAINTING_TYPES.register(modBus);
  }

  // TODO Move to own feature
  public static DeferredRegister<PaintingType> PAINTING_TYPES = DeferredRegister.create(ForgeRegistries.PAINTING_TYPES, CookieCore.MODID);
  public static RegistryObject<PaintingType> PAINTING_ZEN = PAINTING_TYPES.register("zen",()-> new PaintingType(16, 32));

  public void commonSetup(FMLCommonSetupEvent event) {
    BannerRegistry.addPattern("cc_fox", "cc_fox", Items.IRON_SWORD);
    BannerRegistry.addPattern("cc_dino", "cc_dino", Items.GOLDEN_SWORD);
  }

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
          new TranslationTextComponent("cookiecore.superflat.cookie_realmn"),
          Blocks.WHITE_TERRACOTTA,
          Biomes.MUSHROOM_FIELDS,
          null,
          false,
          false,
          false,
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
