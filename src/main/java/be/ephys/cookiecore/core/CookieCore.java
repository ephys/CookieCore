package be.ephys.cookiecore.core;

import be.ephys.cookiecore.helpers.DebugHelper;
import be.ephys.cookiecore.registries.FlatPresetRegistry;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = CookieCore.MODID, version = CookieCore.VERSION)
public class CookieCore {
    public static final String MODID = "cookiecore";
    public static final String VERSION = "2.0.0";

    @Mod.Instance(MODID)
    public static CookieCore instance;

    @SidedProxy(
            modId = MODID,
            serverSide = "be.ephys.cookiecore.core.CommonProxy",
            clientSide = "be.ephys.cookiecore.core.ClientProxy"
    )
    public static CommonProxy sidedProxy;

    private Logger logger = LogManager.getLogger(MODID);

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        sidedProxy.postInit();
    }

    public static Logger getLogger() {
        return instance.logger;
    }
}
