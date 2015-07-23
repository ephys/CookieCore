package nf.fr.ephys.cookiecore.helpers;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.List;
import java.util.UUID;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nf.fr.ephys.cookiecore.util.IFilter;
import nf.fr.ephys.cookiecore.util.NetServerHandlerFake;

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
    if (player == null) {
      return false;
    }

    if (player instanceof FakePlayer) {
      return true;
    }

    if (player.getGameProfile() == null) {
      return true;
    }

    GameProfile profile = player.getGameProfile();

    return profile.getName() == null || profile.getName().contains("[") || player.getClass()
        .toString().toLowerCase().contains("fake");
  }

  public static FakePlayer getFakePlayer(WorldServer world, String username) {
    FakePlayer player = FakePlayerFactory.get(world, new GameProfile(
        UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77"), username));

    player.playerNetServerHandler = new NetServerHandlerFake(MinecraftServer.getServer(), player);

    return player;
  }

  public static FakePlayer getFakePlayer(WorldServer world) {
    FakePlayer player = FakePlayerFactory.getMinecraft(world);

    if (player.playerNetServerHandler == null) {
      player.playerNetServerHandler = new NetServerHandlerFake(MinecraftServer.getServer(), player);
    }

    return player;
  }

  public static MovingObjectPosition rayTrace(Entity entity, double range) {
    Vec3
        pos =
        Vec3.createVectorHelper(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
    Vec3 look = entity.getLookVec();
    Vec3 ray = pos.addVector(look.xCoord * range, look.yCoord * range,
                             look.zCoord * range);

    return entity.worldObj.rayTraceBlocks(pos, ray);
  }

  /**
   * This is an eavy method, don't call it too often. Really. Finds an entity by it's UUID (returns
   * null if the entity does not exists or is not loaded)
   *
   * @param entityUUID the entity UUID
   * @return Entity         the entity
   */
  public static Entity getEntityByUUID(UUID entityUUID) {
    for (WorldServer world : MinecraftServer.getServer().worldServers) {
      Entity entity = getEntityByUUIDInWorld(entityUUID, world);

      if (entity != null) {
        return entity;
      }
    }

    return null;
  }

  /**
   * This is an heavy method, don't call it too often. Really. Finds an entity by it's UUID in a
   * specific world (returns null if the entity does not exists or is not loaded)
   *
   * @param uuid  the entity UUID
   * @param world the world to search in
   * @return Entity         the entity
   */
  @SuppressWarnings("unchecked")
  public static Entity getEntityByUUIDInWorld(UUID uuid, World world) {
    List<Entity> entities = world.getLoadedEntityList();

    for (Entity entity : entities) {
      if (entity.getUniqueID().equals(uuid)) {
        return entity;
      }
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  public static EntityPlayer getPlayerByUUID(UUID uuid) {
    List<EntityPlayer>
        players =
        MinecraftServer.getServer().getConfigurationManager().playerEntityList;

    for (EntityPlayer player : players) {
      if (player.getUniqueID().equals(uuid)) {
        return player;
      }
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  // Mostly lifted from NEI, then stolen from TSteelworks
  public static void drawItemsToEntity(EntityLivingBase entity, int distance) {
    float distancexz = distance >> 1;
    double maxspeedxz = 0.6;
    double maxspeedy = 0.6;
    double speedxz = 0.6;
    double speedy = 0.9;

    List<EntityItem>
        items =
        entity.worldObj.getEntitiesWithinAABB(EntityItem.class, entity.boundingBox
            .expand(distancexz, (float) distance, distancexz));
    for (EntityItem item : items) {
      item.delayBeforeCanPickup = 0;

      double dx = entity.posX - item.posX;
      double dy = entity.posY + entity.getEyeHeight() - item.posY;
      double dz = entity.posZ - item.posZ;
      double absxz = Math.sqrt(dx * dx + dz * dz);
      double absy = Math.abs(dy);

      if (absxz > distancexz) {
        continue;
      }
      if (absxz < 1 && entity instanceof EntityPlayerMP) {
        item.onCollideWithPlayer((EntityPlayerMP) entity);
      }

      if (absxz > 1) {
        dx /= absxz;
        dz /= absxz;
      }

      if (absy > 1) {
        dy /= absy;
      }

      double vx = item.motionX + speedxz * dx;
      double vy = item.motionY + speedy * dy;
      double vz = item.motionZ + speedxz * dz;

      double absvxz = Math.sqrt(vx * vx + vz * vz);
      double absvy = Math.abs(vy);

      double rationspeedxz = absvxz / maxspeedxz;
      if (rationspeedxz > 1) {
        vx /= rationspeedxz;
        vz /= rationspeedxz;
      }

      double rationspeedy = absvy / maxspeedy;
      if (rationspeedy > 1) {
        vy /= rationspeedy;
      }

      item.motionX = vx;
      item.motionY = vy;
      item.motionZ = vz;
    }
  }

  /**
   * Returns the closest entity of a given type.
   *
   * @param entityType The type of entity to search.
   * @param world      The world in which to search.
   * @param x          The x coordinate of the point from which the distance is to be calculated.
   * @param y          The y coordinate of the point from which the distance is to be calculated.
   * @param z          The z coordinate of the point from which the distance is to be calculated.
   * @param range      The search range.
   * @param filter     (nullable) A filter to validate the entities in the list.
   * @return the closest entity or null if none is found or matches the filter.
   */
  @SuppressWarnings("unchecked")
  public static <T extends Entity> T getClosestEntity(Class<T> entityType, World world, double x,
                                                      double y, double z, float range,
                                                      IFilter<T> filter) {
    List<T> entities = world.getEntitiesWithinAABB(entityType, AxisAlignedBB.getBoundingBox(
        x - 1, y - 1, z - 1,
        x + 1, y + 1, z + 1)
        .expand(range, range, range));

    T closestEntity = null;
    double closestDistance = Double.MAX_VALUE;
    for (T entity : entities) {
      if (entity.isDead) {
        continue;
      }

      if (filter != null && !filter.isValid(entity)) {
        continue;
      }

      double dist = entity.getDistance(x, y, z);
      if (dist < closestDistance) {
        closestEntity = entity;
        closestDistance = dist;
      }
    }

    return closestEntity;
  }
}
