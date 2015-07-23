package nf.fr.ephys.cookiecore.common;

import com.google.common.eventbus.EventBus;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.FlatLayerInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import nf.fr.ephys.cookiecore.client.registry.FlatPresetRegistry;
import nf.fr.ephys.cookiecore.helpers.DebugHelper;

@Mod(modid = CookieCore.MODID, version = CookieCore.VERSION, name = CookieCore.MODNAME, dependencies = "after:Forge@[10.13.4.1448,)")
public class CookieCore extends DummyModContainer {

  public static final String MODNAME = "Cookie Core";
  public static final String MODID = "cookiecore";
  public static final String VERSION = "1.4.0";

  @Mod.Instance(MODID)
  public static CookieCore instance;

  private Logger logger = LogManager.getLogger(getModId());

  public CookieCore() {
    super(new ModMetadata());

    ModMetadata meta = this.getMetadata();

    meta.authorList.add("EphysPotato");
    meta.description = "Lib for my mods.";
    meta.modId = MODID;
    meta.version = VERSION;
    meta.name = MODNAME;
  }

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
      FlatPresetRegistry.addPresetAt(0, FlatPresetRegistry
          .buildPreset("Cookie Realmn", Item.getItemFromBlock(Blocks.stained_hardened_clay),
                       BiomeGenBase.mushroomIsland, null,
                       new FlatLayerInfo[]{new FlatLayerInfo(63, Blocks.stained_hardened_clay),
                                           new FlatLayerInfo(1, Blocks.bedrock)}));
    }
  }

  public static Logger getLogger() {
    return instance.logger;
  }

  @Override
  public boolean registerBus(EventBus bus, LoadController controller) {
    return true;
  }
}