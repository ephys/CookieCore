package be.ephys.cookiecore.helpers;

import be.ephys.cookiecore.core.CookieCore;
import net.minecraft.world.World;

public final class DebugHelper {

  public static final boolean debug = getDebug();

  private static boolean getDebug() {
    try {
      World.class.getMethod("getBlockState", Integer.TYPE, Integer.TYPE, Integer.TYPE);
      CookieCore.getLogger().info("Dev environnement detected");
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static void sidedDebug(World world, String message) {
    CookieCore.getLogger().info("[" + (world.isRemote ? "CLIENT" : "SERVER") + "] " + message);
  }

  public static String arrayToString(Object[] array) {
    StringBuilder str = new StringBuilder();

    str.append('[');

    for (int i = 0; i < array.length; i++) {
      if (array[i] == null) {
        str.append("null");
      } else {
        str.append(array[i].toString());
      }

      if (i != array.length - 1) {
        str.append(", ");
      }
    }

    str.append(']');

    return str.toString();
  }
}
