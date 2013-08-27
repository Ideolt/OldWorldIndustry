package oldworldindustry.common.client.renderers;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import oldworldindustry.common.client.models.ModelAxle;

import org.lwjgl.opengl.GL11;

public class TileEntityAxleRenderer extends TileEntitySpecialRenderer
{
	ModelAxle model = new ModelAxle();

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale)
	{
		GL11.glPushMatrix();
		int i = te.getBlockMetadata();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(1.0F, -1F, -1F);
		bindTextureByName("/mods/OWI/textures/blocks/axleTexture.png");
		model.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}
	
}
