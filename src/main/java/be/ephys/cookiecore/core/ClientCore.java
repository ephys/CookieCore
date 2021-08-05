package be.ephys.cookiecore.core;

import be.ephys.cookiecore.transbee.TransBeeRenderer;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientCore {
  public static void init() {
    if (TransBeeRenderer.enabled()) {
      RenderingRegistry.registerEntityRenderingHandler(EntityType.BEE, TransBeeRenderer::new);
    }
  }
}
