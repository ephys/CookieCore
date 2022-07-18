package be.ephys.cookiecore.helpers;

import be.ephys.cookiecore.core.CookieCore;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public final class DebugHelper {

  public static final boolean debug = getDebug();

  private static boolean getDebug() {
    try {
      Level.class.getMethod("getBlockState", BlockPos.class);
      CookieCore.getLogger().info("Dev environnement detected");
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static void sidedDebug(Level world, String message) {
    CookieCore.getLogger().info("[" + (world.isClientSide() ? "CLIENT" : "SERVER") + "] " + message);
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
