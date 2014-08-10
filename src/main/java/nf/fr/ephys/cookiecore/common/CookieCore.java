package nf.fr.ephys.cookiecore.common;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import nf.fr.ephys.cookiecore.common.world.WorldEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = CookieCore.MODID, version = CookieCore.VERSION, name = CookieCore.MODNAME)
public class CookieCore extends DummyModContainer {
	public static final String MODNAME = "Cookie Core";
    public static final String MODID = "cookiecore";
    public static final String VERSION = "1.1.0";

	@Mod.Instance(MODID)
	public static CookieCore instance;

	private Logger logger;

	public CookieCore() {
		super(new ModMetadata());

		ModMetadata meta = this.getMetadata();

		meta.authorList.add("EphysPotato");
		meta.description = "Lib for my mods";
		meta.modId = MODID;
		meta.version = VERSION;
		meta.name = MODNAME;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		MinecraftForge.EVENT_BUS.register(new WorldEvent());
	}

	public static Logger getLogger() {
		return instance.logger;
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		return true;
	}
}