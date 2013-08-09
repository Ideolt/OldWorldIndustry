package oldworldinsdustry.common.blocks;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import oldworldinsdustry.common.tileentitys.TileEntityAxle;

public class BlockAxle extends BlockContainer implements IConnectable
{
	private boolean connected;
	private int storedPower = 0;//is transfered to the other block on tick
	
	protected BlockAxle(int id)
	{
		super(id, Material.wood);
		this.setHardness(2F);
		this.setResistance(25F);
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setStepSound(soundWoodFootstep);
		this.setUnlocalizedName("OWIAxle");
		//this.setBlockBounds(par1, par2, par3, par4, par5, par6); needs model to be set
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityAxle();
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,EntityLiving entityliving, ItemStack itemStack) {
		int blockSet = world.getBlockMetadata(x, y, z) / 4;
		int direction = MathHelper
				.floor_double((double) (entityliving.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		int newMeta = (blockSet * 4) + direction;
		world.setBlockMetadataWithNotify(x, y, z, newMeta, 0);
	}

	@Override
	public boolean isConnected()
	{
		return connected;
	}

	@Override
	public boolean canConnect()
	{
		// TODO Auto-generated method stub
		return false;
	}
		
	
}
