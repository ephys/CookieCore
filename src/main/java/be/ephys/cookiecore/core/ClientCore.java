package be.ephys.cookiecore.core;

import be.ephys.cookiecore.config.Config;
import be.ephys.cookiecore.transbee.TransBeeRenderer;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientCore {

  @Config(description = "Set to empty to disable trans bees. Separate names with a space", side = ModConfig.Type.CLIENT)
  @Config.StringListDefault({"zoe"})
  public static ForgeConfigSpec.ConfigValue<List<String>> transBeeNames;

  public static void init() {
    if (TransBeeRenderer.enabled()) {
      RenderingRegistry.registerEntityRenderingHandler(EntityType.BEE, TransBeeRenderer::new);
    }
  }
}
