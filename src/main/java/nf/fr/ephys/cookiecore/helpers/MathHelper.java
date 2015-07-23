package nf.fr.ephys.cookiecore.helpers;

import java.awt.Color;
import java.util.List;
import java.util.Random;

public class MathHelper {

  public static Random random = new Random();

  public static Color gradient(Color colorA, Color colorB, float percent) {
    return new Color(
        Math.round((colorB.getRed() - colorA.getRed()) * percent + colorA.getRed()),
        Math.round((colorB.getGreen() - colorA.getGreen()) * percent + colorA.getGreen()),
        Math.round((colorB.getBlue() - colorA.getBlue()) * percent + colorA.getBlue())
    );
  }

  public static int gradientRGB(int color1, int color2, float percent) {
    byte r1 = (byte) (color1 >> 16 & 0xff);
    byte g1 = (byte) (color1 >> 8 & 0xff);
    byte b1 = (byte) (color1 & 0xff);

    byte g2 = (byte) (color2 >> 8 & 0xff);
    byte r2 = (byte) (color2 >> 16 & 0xff);
    byte b2 = (byte) (color2 & 0xff);

    byte r3 = (byte) Math.round((r2 - r1) * percent + r1);
    byte g3 = (byte) Math.round((g2 - g1) * percent + g1);
    byte b3 = (byte) Math.round((b2 - b1) * percent + b1);

    return (r3 << 16) | (g3 << 8) | b3;
  }

  public static byte[] toRGB(int hexColor) {
    return new byte[]{
        (byte) (hexColor >> 16 & 0xff),
        (byte) ((hexColor >> 8) & 0xff),
        (byte) (hexColor & 0xff)
    };
  }

  public static Object getRandom(List list) {
    if (list.size() == 0) {
      return null;
    }

    Object o;
    do {
      o = list.get(net.minecraft.util.MathHelper.getRandomIntegerInRange(random, 0, list.size() - 1));
    } while (o == null);

    return o;
  }

  public static int averageColorFromAint(int[] aint) {

    double r = 0, g = 0, b = 0;
    for (int pixel : aint) {
      r += (-pixel >> 16 & 0xFF);// / 255f;
      g += (-pixel >> 8 & 0xFF); // / 255f;
      b += (-pixel & 0xFF); // / 255f;
    }

    r /= aint.length;
    g /= aint.length;
    b /= aint.length;

    return -((int) r << 16 | (int) g << 8 | (int) b);

    //return -((int) (r*255f) << 16 + (int) (g*255f) << 8 + (int) b*255f);
  }

  public static double round(double value, int digits) {
    double mult = Math.pow(10, digits);

    return Math.round(value * mult) / mult;
  }
}
