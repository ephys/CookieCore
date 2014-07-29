package nf.fr.ephys.playerproxies.helpers;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderHelper {
	public static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

	public static final ModelBiped MODEL_BIPED = new ModelBiped(0.0F);

	public static void renderSimpleBiped(ResourceLocation skin, float tickTime) {
		RenderManager.instance.renderEngine.bindTexture(skin);

		MODEL_BIPED.bipedHead.render(tickTime);
		MODEL_BIPED.bipedBody.render(tickTime);
		MODEL_BIPED.bipedRightArm.render(tickTime);
		MODEL_BIPED.bipedLeftArm.render(tickTime);
		MODEL_BIPED.bipedRightLeg.render(tickTime);
		MODEL_BIPED.bipedLeftLeg.render(tickTime);
		MODEL_BIPED.bipedHeadwear.render(tickTime);
	}

	public static void loadItemMap() {
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
	}

	public static void loadBlockMap() {
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
	}

	public static void renderItem3D(ItemStack item) {
		int maxRenderPasses = item.getItem().getRenderPasses(item.getItemDamage());

		for (int i = 0; i < maxRenderPasses; i++) {
			IIcon icon = item.getItem().getIcon(item, i);

			if (icon == null)
				continue;

			Color color = new Color(item.getItem().getColorFromItemStack(item, i));
			GL11.glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());

			ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);

			if (item.hasEffect(i)) {
				GL11.glDepthFunc(514);
				GL11.glDisable(2896);
				Minecraft.getMinecraft().getTextureManager().bindTexture(RES_ITEM_GLINT);
				GL11.glEnable(3042);
				OpenGlHelper.glBlendFunc(768, 1, 1, 0);
				float f7 = 0.76F;
				GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
				GL11.glMatrixMode(5890);
				GL11.glPushMatrix();
				float f8 = 0.125F;
				GL11.glScalef(f8, f8, f8);
				float f9 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
				GL11.glTranslatef(f9, 0.0F, 0.0F);
				GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
				ItemRenderer.renderItemIn2D(Tessellator.instance, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
				GL11.glPopMatrix();

				GL11.glPushMatrix();
				GL11.glScalef(f8, f8, f8);
				f9 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
				GL11.glTranslatef(-f9, 0.0F, 0.0F);
				GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
				ItemRenderer.renderItemIn2D(Tessellator.instance, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
				GL11.glPopMatrix();
				GL11.glMatrixMode(5888);
				GL11.glDisable(3042);
				GL11.glEnable(2896);
				GL11.glDepthFunc(515);

				loadItemMap();
			}
		}

		GL11.glColor3f(1F, 1F, 1F);
	}

	public static void renderInventoryBlock(Block block, int metadata, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, -1F, 0F);
		renderer.renderFaceYNeg(block, 0D, 0D, 0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, 1F, 0F);
		renderer.renderFaceYPos(block, 0D, 0D, 0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, 0F, -1F);
		renderer.renderFaceZNeg(block, 0D, 0D, 0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, 0F, 1F);
		renderer.renderFaceZPos(block, 0D, 0D, 0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0F, 0F);
		renderer.renderFaceXNeg(block, 0D, 0D, 0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(1F, 0F, 0F);
		renderer.renderFaceXPos(block, 0D, 0D, 0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
		tessellator.draw();

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	public static IIcon getFluidTexture(FluidStack fluid) {
		IIcon icon = fluid.getFluid().getIcon(fluid);

		if (icon == null && fluid.getFluid().canBePlacedInWorld())
			return fluid.getFluid().getBlock().getIcon(0, 0);

		return icon;
	}

	public static IIcon getFluidTexture(Fluid fluid) {
		IIcon icon = fluid.getIcon();

		if (icon == null && fluid.canBePlacedInWorld())
			return fluid.getBlock().getIcon(0, 0);

		return icon;
	}
}
