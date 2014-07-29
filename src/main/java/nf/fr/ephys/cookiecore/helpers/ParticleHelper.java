package nf.fr.ephys.playerproxies.helpers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class ParticleHelper {
	private static ArrayList<String> particles = new ArrayList<>();

	static {
		particles.add("hugeexplosion");
		particles.add("largeexplode");
		particles.add("fireworksSpark");
		particles.add("bubble");
		particles.add("suspended");
		particles.add("depthsuspend");
		particles.add("townaura");
		particles.add("crit");
		particles.add("magicCrit");
		particles.add("smoke");
		particles.add("mobSpell");
		particles.add("mobSpellAmbient");
		particles.add("spell");
		particles.add("instantSpell");
		particles.add("witchMagic");
		particles.add("note");
		particles.add("portal");
		particles.add("enchantmenttable");
		particles.add("explode");
		particles.add("flame");
		particles.add("lava");
		particles.add("footstep");
		particles.add("splash");
		particles.add("largesmoke");
		particles.add("cloud");
		particles.add("reddust");
		particles.add("snowballpoof");
		particles.add("dripWater");
		particles.add("dripLava");
		particles.add("snowshovel");
		particles.add("slime");
		particles.add("heart");
		particles.add("angryVillager");
		particles.add("happyVillager");
	}

	@SideOnly(Side.CLIENT)
	public static void spawnParticle(int id, double x, double y, double z, double velX, double velY, double velZ) {
		Minecraft.getMinecraft().theWorld.spawnParticle(getParticleNameFromID(id), x, y, z, velX, velY, velZ);
	}

	public static String getParticleNameFromID(int id) {
		return particles.get(id);
	}

	public static int getParticleIDFromName(String name) {
		return particles.indexOf(name);
	}
}
