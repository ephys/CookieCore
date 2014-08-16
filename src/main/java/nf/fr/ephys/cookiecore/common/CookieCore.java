package nf.fr.ephys.cookiecore.common;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

	}

	public static Logger getLogger() {
		return instance.logger;
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		return true;
	}
}