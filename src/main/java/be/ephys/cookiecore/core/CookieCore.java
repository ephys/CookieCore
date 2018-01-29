package be.ephys.cookiecore.core;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@Mod(
  modid = CookieCore.MODID,
  version = CookieCore.VERSION,
  certificateFingerprint = "@FINGERPRINT@"
)
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

  @NetworkCheckHandler
  public boolean acceptConnection(Map<String, String> modList, Side side) {

    // Mod can be used on both client & server even if the other party doesn't have it installed
    return true;
  }
}
