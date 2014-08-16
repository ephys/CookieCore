package nf.fr.ephys.cookiecore.client;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

public interface IParticleHandler {
	public EntityFX getParticle(int id, World world, double x, double y, double z, double velx, double vely, double velz);

	/**
	 * Gets the distance after which the particle will not get rendered
	 *
	 * @param id    The particle id
	 * @return      The render distance, usually 16.
	 */
	public double getRenderDistance(int id);

	public int getMaxParticleSetting(int id);
}
