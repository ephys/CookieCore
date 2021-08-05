package be.ephys.cookiecore.transbee;

import be.ephys.cookiecore.config.Config;
import be.ephys.cookiecore.core.CookieCore;
import net.minecraft.client.renderer.entity.BeeRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class TransBeeRenderer extends BeeRenderer {
  private static final ResourceLocation angry = new ResourceLocation(CookieCore.MODID, "textures/entity/transbee/bee_angry.png");
  private static final ResourceLocation angryNectar = new ResourceLocation(CookieCore.MODID, "textures/entity/transbee/bee_angry_nectar.png");
  private static final ResourceLocation base = new ResourceLocation(CookieCore.MODID, "textures/entity/transbee/bee.png");
  private static final ResourceLocation nectar = new ResourceLocation(CookieCore.MODID, "textures/entity/transbee/bee_nectar.png");

  @Config(description = "Set to empty to disable trans bees. Separate names with a space", side = ModConfig.Type.CLIENT)
  @Config.StringDefault("zoe")
  public static ForgeConfigSpec.ConfigValue<String> transBeeNames;

  private static String lastTransBeeNamesStr = null;
  private static Set<String> lastTransBeeNames = null;

  private static Set<String> getNames() {
    if (Objects.equals(transBeeNames.get(), lastTransBeeNamesStr)) {
      return lastTransBeeNames;
    }

    String[] parts = transBeeNames.get().split(" ");

    Set<String> out = new HashSet<>();

    for (String part: parts) {
      part = part.trim();
      if (part.length() == 0) {
        continue;
      }

      out.add(part);
    }

    lastTransBeeNamesStr = transBeeNames.get();
    lastTransBeeNames = out;

    return out;
  }

  public static boolean enabled() {
    return getNames().size() > 0;
  }

  public TransBeeRenderer(EntityRendererManager p_i226033_1_) {
    super(p_i226033_1_);
  }

  public ResourceLocation getEntityTexture(BeeEntity entity) {
    ITextComponent customName = entity.getCustomName();

    if (customName == null || !getNames().contains(customName.getString())) {
      return super.getEntityTexture(entity);
    }

    if (entity.func_233678_J__()) {
      return entity.hasNectar() ? angryNectar : angry;
    } else {
      return entity.hasNectar() ? nectar : base;
    }
  }
}
