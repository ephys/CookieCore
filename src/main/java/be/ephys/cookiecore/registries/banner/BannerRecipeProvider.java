package be.ephys.cookiecore.registries.banner;

import be.ephys.cookiecore.core.CookieCore;
import net.minecraft.data.CustomRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.function.Consumer;

public class BannerRecipeProvider extends RecipeProvider {
  public BannerRecipeProvider(DataGenerator generatorIn) {
    super(generatorIn);
  }

  @Override
  protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
    CookieCore.getLogger().info("Data Provider: Registering Special Banner Recipe");
    specialRecipe(consumer, BannerRecipe.SERIALIZER);
  }

  private void specialRecipe(Consumer<IFinishedRecipe> consumer, SpecialRecipeSerializer<?> serializer) {
    ResourceLocation name = Registry.RECIPE_SERIALIZER.getKey(serializer);
    CustomRecipeBuilder.customRecipe(serializer)
      .build(consumer, new ResourceLocation(CookieCore.MODID, name.getPath()).toString());
  }

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = CookieCore.MODID)
  public static class GatherDataSubscriber {
    @SubscribeEvent
    public static void registerProvider(GatherDataEvent event) {
      if (event.includeServer()) {
        DataGenerator gen = event.getGenerator();
        gen.addProvider(new BannerRecipeProvider(gen));
      }
    }

    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> evt) {
      evt.getRegistry().register(BannerRecipe.SERIALIZER.setRegistryName(CookieCore.MODID, "banner_pattern_apply"));
    }
  }
}
