package be.ephys.cookiecore.core;

import be.ephys.cookiecore.config.Config;
import be.ephys.cookiecore.transbee.TransBeeRenderer;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;

@Mod.EventBusSubscriber(
  value = Dist.CLIENT,
  bus = Mod.EventBusSubscriber.Bus.MOD,
  modid = CookieCore.MODID
)
public class ClientCore {

  @Config(description = "Set to empty to disable trans bees. Separate names with a space", side = ModConfig.Type.CLIENT)
  @Config.StringListDefault({"zoe"})
  public static ForgeConfigSpec.ConfigValue<List<String>> transBeeNames;

  @SubscribeEvent
  public static void onClientSetup(FMLClientSetupEvent event) {
    if (TransBeeRenderer.enabled()) {
      RenderingRegistry.registerEntityRenderingHandler(EntityType.BEE, TransBeeRenderer::new);
    }
  }
}
