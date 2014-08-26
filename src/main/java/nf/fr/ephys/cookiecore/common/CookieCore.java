package nf.fr.ephys.cookiecore.common;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.gui.GuiFlatPresets;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.FlatLayerInfo;
import nf.fr.ephys.cookiecore.common.registryUtil.FlatPresetRegistry;
import nf.fr.ephys.cookiecore.helpers.DebugHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

@Mod(modid = CookieCore.MODID, version = CookieCore.VERSION, name = CookieCore.MODNAME)
public class CookieCore extends DummyModContainer {
	public static final String MODNAME = "Cookie Core";
    public static final String MODID = "cookiecore";
    public static final String VERSION = "1.3.0";

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
	public void preInit(FMLPreInitializationEvent event) {
		// this is from ExU, yup yup (sorry Tema)

		try {
			World.class.getMethod("getBlock", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE});
			DebugHelper.debug = true;

			logger.info("Dev environnement detected");
		} catch (Exception e) {
			DebugHelper.debug = false;
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
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
		FlatPresetRegistry.addPresetAt(0, FlatPresetRegistry.buildPreset("Cookie Realmn", Item.getItemFromBlock(Blocks.stained_hardened_clay), BiomeGenBase.mushroomIsland, null, new FlatLayerInfo[] {
				new FlatLayerInfo(63, Blocks.stained_hardened_clay),
				new FlatLayerInfo(1, Blocks.bedrock)
		}));
	}

	public static Logger getLogger() {
		return instance.logger;
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		return true;
	}
}