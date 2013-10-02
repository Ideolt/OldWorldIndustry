package oldworldindustry.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import oldworldindustry.common.tileentitys.TileEntityAxle;

public class BlockAxle extends BlockContainer
{
	private static int axleLevel;
	
	public BlockAxle(int id,int level)
	{
		super(id, Material.iron);
		this.setHardness(1F);
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setUnlocalizedName("OWIAxle");
		axleLevel = level;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId)
	{
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		switch(axleLevel){
			case 1:
				return new TileEntityAxle(1, 8, 1, 8,axleLevel);
			case 2:
				return new TileEntityAxle(1, 16, 1, 8,axleLevel);
			case 3://should be lvl 5
				return new TileEntityAxle(1, 256, 1, 8,axleLevel);
		}
		
		return null;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving, ItemStack itemStack)
	{
		int blockSet = world.getBlockMetadata(x, y, z) / 4;
		int direction = MathHelper.floor_double((double) (entityliving.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		int newMeta = (blockSet * 4) + direction;
		world.setBlockMetadataWithNotify(x, y, z, newMeta, 0);
		rotateBlock(world,x,y,z,ForgeDirection.UNKNOWN);//direction not used
	}

	@Override
	public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (tile instanceof TileEntityAxle)
		{
			((TileEntityAxle) tile).switchOrientation();
			return true;
		}
		return false;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		TileEntityAxle axle = ((TileEntityAxle) world.getBlockTileEntity(x, y, z));

		if (axle != null)
		{
			axle.delete();
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		switch (axleLevel)
		{
			case 1:
				this.blockIcon = par1IconRegister.registerIcon("OWI:axle_wood");
			case 2:
				this.blockIcon = par1IconRegister.registerIcon("OWI:axle_stone");
			case 3:
				this.blockIcon = par1IconRegister.registerIcon("OWI:axle_iron");
		}
	}

}
