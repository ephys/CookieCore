package nf.fr.ephys.cookiecore.helpers;

import java.awt.*;
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

	public static int gradientRGB(int colorA, int colorB, float percent) {
		return Math.round((colorB - colorA) * percent + colorA);
	}

	public static int[] toRGB(int hexColor) {
		// 0xRRGGBB

		return new int[] {
			hexColor >> 16,         // RR [>> GGBB]
			(hexColor >> 8) & 0xFF, // RRGG [>> BB], 00GG
			hexColor & 0xFF         // 0000BB
		};
	}

	public static Object getRandom(List list) {
		if (list.size() == 0) return null;

		Object o = null;
		do {
			o = list.get(net.minecraft.util.MathHelper.getRandomIntegerInRange(random, 0, list.size() - 1));
		} while(o == null);

		return o;
	}

	public static int averageColorFromAint(int[] aint) {
		float r = 0;
		float g = 0;
		float b = 0;

		for (int pixel : aint) {
			/*r += (pixel >> 16 & 0xFF) / aint.length;
			g += (pixel >> 8 & 0xFF) / aint.length;
			b += (pixel & 0xFF) / aint.length;*/

			r += (-pixel >> 16 & 0xFF) / 255.0F;
			g += (-pixel >> 8 & 0xFF) / 255.0F;
			b += (-pixel & 0xFF) / 255.0F;
		}

		r /= aint.length;
		g /= aint.length;
		b /= aint.length;

		return -((int)(r * 255.0F) << 16 | (int)(g * 255.0F) << 8 | (int)(b * 255.0F));

		//return (int) r << 16 + (int) g << 8 + (int) b;
	}
}
