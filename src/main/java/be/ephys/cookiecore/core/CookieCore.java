package be.ephys.cookiecore.core;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = CookieCore.MODID, version = CookieCore.VERSION, certificateFingerprint = "@FINGERPRINT@")
public class CookieCore {
  public static final String MODID = "cookiecore";
  public static final String VERSION = "@VERSION@";

  @Mod.Instance(MODID)
  public static CookieCore instance;

  @SidedProxy(
    modId = MODID,
    serverSide = "be.ephys.cookiecore.core.CommonProxy",
    clientSide = "be.ephys.cookiecore.core.ClientProxy"
  )
  public static CommonProxy sidedProxy;

  private Logger logger = LogManager.getLogger(MODID);

  public static Logger getLogger() {
    return instance.logger;
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    sidedProxy.postInit();
  }
}
