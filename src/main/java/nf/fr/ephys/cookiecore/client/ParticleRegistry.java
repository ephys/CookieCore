package nf.fr.ephys.cookiecore.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;

import java.util.ArrayList;
import java.util.List;

public class ParticleRegistry {

  private static List<IParticleHandler> particleHandlers = new ArrayList<>();

  /**
   * Registers a particle handler and returns it's particle id You can register the same handler
   * multiple times and handle the particle type usung it's id
   *
   * @param handler The handler to call to get the particle to spawn
   * @return The particle id
   */
  public static int registerParticleHandler(IParticleHandler handler) {
    particleHandlers.add(handler);

    return particleHandlers.size() - 1;
  }

  /**
   * Spawns a particle using the particle id
   *
   * @param id   The particle id, register your particle handler with registerParticleHandler()
   * @param x    The x coordinate of the particle
   * @param y    The y coordinate of the particle
   * @param z    The z coordinate of the particle
   * @param velX The x velocity of the particle
   * @param velY The y velocity of the particle
   * @param velZ The z velocity of the particle
   * @return the spawned particle or null if none
   */
  public static EntityFX spawnParticle(int id, double x, double y, double z, double velX,
                                       double velY, double velZ) {
    if (id >= particleHandlers.size()) {
      return null;
    }

    Minecraft mc = Minecraft.getMinecraft();

    int particleSetting = mc.gameSettings.particleSetting;

    if (particleSetting == 1 && mc.theWorld.rand.nextInt(3) == 0) {
      particleSetting = 2;
    }

    IParticleHandler handler = particleHandlers.get(id);

    if (handler.getMaxParticleSetting(id) > particleSetting) {
      return null;
    }

    double renderDistance = handler.getRenderDistance(id);

    double distanceX = mc.renderViewEntity.posX - x;
    double distanceY = mc.renderViewEntity.posY - y;
    double distanceZ = mc.renderViewEntity.posZ - z;

    if (distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ
        > renderDistance * renderDistance) {
      return null;
    }

    EntityFX particle = handler.getParticle(id, mc.theWorld, x, y, z, velX, velY, velZ);

    if (particle != null) {
      Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    return particle;
  }
}
