package nf.fr.ephys.cookiecore.helpers;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import nf.fr.ephys.cookiecore.util.NetServerHandlerFake;

import java.util.List;
import java.util.UUID;

public class EntityHelper {
	public static final int ARMORSLOT_HELMET = 3;
	public static final int ARMORSLOT_CHEST = 2;
	public static final int ARMORSLOT_PANTS = 1;
	public static final int ARMORSLOT_BOOT = 0;

	@SideOnly(Side.CLIENT)
	public static EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}

	public static boolean isFakePlayer(EntityPlayer player) {
		if (player == null) return false;

		if (player instanceof FakePlayer) return true;

		if (player.getGameProfile() == null) return true;

		GameProfile profile = player.getGameProfile();

		return profile.getName() == null || profile.getName().contains("[") || player.getClass().toString().toLowerCase().contains("fake");
	}

	public static FakePlayer getFakePlayer(WorldServer world, String username) {
		FakePlayer player = FakePlayerFactory.get(world, new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77"), username));

		player.playerNetServerHandler = new NetServerHandlerFake(MinecraftServer.getServer(), player);

		return player;
	}

	public static FakePlayer getFakePlayer(WorldServer world) {
		FakePlayer player = FakePlayerFactory.getMinecraft(world);

		if (player.playerNetServerHandler == null)
			player.playerNetServerHandler = new NetServerHandlerFake(MinecraftServer.getServer(), player);

		return player;
	}

	public static MovingObjectPosition rayTrace(Entity entity, double range) {
		Vec3 pos = Vec3.createVectorHelper(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
		Vec3 look = entity.getLookVec();
		Vec3 ray = pos.addVector(look.xCoord * range, look.yCoord * range, look.zCoord * range);

		return entity.worldObj.rayTraceBlocks(pos, ray);
	}

	/**
	 * This is an eavy method, don't call it too often. Really.
	 * Finds an entity by it's UUID (returns null if the entity does not exists or is not loaded)
	 *
	 * @param entityUUID the entity UUID
	 * @return Entity 	 the entity
	 */
	public static Entity getEntityByUUID(UUID entityUUID) {
		for (WorldServer world : MinecraftServer.getServer().worldServers) {
			Entity entity = getEntityByUUIDInWorld(entityUUID, world);

			if (entity != null)
				return entity;
		}

		return null;
	}

	/**
	 * This is an heavy method, don't call it too often. Really.
	 * Finds an entity by it's UUID in a specific world (returns null if the entity does not exists or is not loaded)
	 *
	 * @param uuid       the entity UUID
	 * @param world		 the world to search in
	 * @return Entity 	 the entity
	 */
	@SuppressWarnings("unchecked")
	public static Entity getEntityByUUIDInWorld(UUID uuid, World world) {
		List<Entity> entities = world.getLoadedEntityList();

		for (Entity entity : entities) {
			if (entity.getUniqueID().equals(uuid))
				return entity;
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static EntityPlayer getPlayerByUUID(UUID uuid) {
		List<EntityPlayer> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;

		for (EntityPlayer player : players) {
			if (player.getUniqueID().equals(uuid))
				return player;
		}

		return null;
	}
}
