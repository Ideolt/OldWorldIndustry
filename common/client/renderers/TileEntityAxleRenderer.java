package oldworldinsdustry.common.client.renderers;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class TileEntityAxleRenderer extends TileEntitySpecialRenderer
{
	// model

	public TileEntityAxleRenderer()
	{
		// model = new model();
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale)
	{
		int rotation = 180;

		GL11.glPushMatrix();
		int i = te.getBlockMetadata();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(1.0F, -1F, -1F);
		//model.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
		
		//needs rotation to North East West and South 
	}
	
	//model needs to register 6-7 custom icons depending on tear

}
