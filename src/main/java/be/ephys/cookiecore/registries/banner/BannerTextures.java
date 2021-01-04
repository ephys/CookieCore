package be.ephys.cookiecore.registries.banner;

import be.ephys.cookiecore.core.CookieCore;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.tileentity.BannerPattern;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = CookieCore.MODID, value = Dist.CLIENT)
public class BannerTextures {

  @SubscribeEvent
  public static void onModelRegister(ModelRegistryEvent evt) {
    Set<RenderMaterial> materials = ModelBakery.LOCATIONS_BUILTIN_TEXTURES;

    for (BannerPattern pattern : BannerPattern.values()) {
      if (pattern.getFileName().startsWith("cc_")) {
        // func_226957_a_ = getTextureLocation
        materials.add(new RenderMaterial(Atlases.SHIELD_ATLAS, pattern.func_226957_a_(false)));
        materials.add(new RenderMaterial(Atlases.BANNER_ATLAS, pattern.func_226957_a_(true)));
      }
    }
  }
}
