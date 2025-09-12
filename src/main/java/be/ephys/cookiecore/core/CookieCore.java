package be.ephys.cookiecore.core;

import be.ephys.cookiecore.config.ConfigSynchronizer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod(CookieCore.MODID)
@Mod.EventBusSubscriber(modid = CookieCore.MODID)
public class CookieCore {
  public static final String MODID = "cookiecore";

  private static final Logger logger = LogManager.getLogger(MODID);

  // TODO Move to Fundamental, as well as banner
  public static DeferredRegister<PaintingVariant> PAINTING_TYPES = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, CookieCore.MODID);
  public static RegistryObject<PaintingVariant> PAINTING_ZEN = PAINTING_TYPES.register("zen", () -> new PaintingVariant(16, 32));

  public CookieCore() {
    ConfigSynchronizer.synchronizeConfig();

    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

    PAINTING_TYPES.register(modBus);
  }

  public static Logger getLogger() {
    return logger;
  }

  @SubscribeEvent
  public static void onLevelCreateSpawnPosition(LevelEvent.CreateSpawnPosition event) {
    ServerLevel level = (ServerLevel) event.getLevel();

    ChunkGenerator chunkGenerator = level.getChunkSource().getGenerator();
    if (!(chunkGenerator instanceof FlatLevelSource flatLevelSource)) {
      return;
    }

    FlatLevelGeneratorSettings settings = flatLevelSource.settings();
    if (!settings.getBiome().is(Tags.Biomes.IS_MUSHROOM)) {
      return;
    }

    List<BlockState> layers = settings.getLayers();
    if (layers.size() < 64) {
      return;
    }

    level.getServer().setDefaultGameType(GameType.CREATIVE);

    GameRules gameRules = level.getGameRules();
    gameRules.getRule(GameRules.RULE_DAYLIGHT).set(false, level.getServer());
    gameRules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, level.getServer());
    gameRules.getRule(GameRules.RULE_DOINSOMNIA).set(false, level.getServer());
    gameRules.getRule(GameRules.RULE_DO_IMMEDIATE_RESPAWN).set(true, level.getServer());
    gameRules.getRule(GameRules.RULE_DO_PATROL_SPAWNING).set(false, level.getServer());

    // Set the time to midday
    level.setDayTime(6000);
  }
}
