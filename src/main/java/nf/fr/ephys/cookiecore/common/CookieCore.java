package nf.fr.ephys.cookiecore.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = CookieCore.MODID, version = CookieCore.VERSION, name = CookieCore.MODNAME)
public class CookieCore {
	public static final String MODNAME = "Cookie Core";
    public static final String MODID = "cookiecore";
    public static final String VERSION = "1.0.0";

	@Mod.Instance(MODID)
	public static CookieCore instance;

	private Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
	}

	public static Logger getLogger() {
		return instance.logger;
	}
}