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
import oldworldindustry.common.tileentitys.TileEntityGearbox;
import buildcraft.api.power.IPowerReceptor;

public class BlockGearbox extends BlockContainer 
{
	public static int gearboxLevel;
	
	public BlockGearbox(int id,int level)
	{
		super(id, Material.iron);
		this.setHardness(1F);
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setUnlocalizedName("OWIGearbox");
		gearboxLevel = level;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId)
	{
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		switch(gearboxLevel){
			case 1:
				return new TileEntityGearbox(1, 8, 1, 1000, gearboxLevel);
			case 2:
				return new TileEntityGearbox(1, 16, 1, 10000, gearboxLevel);
			case 3:
				return new TileEntityGearbox(1, 256, 1, 100000, gearboxLevel);
		}
		
		return null;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving, ItemStack itemStack)
	{
		int blockSet = world.getBlockMetadata(x, y, z) / 4;
		int direction = MathHelper.floor_double((double) (entityliving.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		int newMeta = (blockSet * 4) + direction;
		world.setBlockMetadataWithNotify(x, y, z, newMeta, 0);
		rotateBlock(world,x,y,z,ForgeDirection.UNKNOWN);
	}

	@Override
	public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (tile instanceof IPowerReceptor)
		{
			((TileEntityGearbox)tile).switchOrientation();
			return true;
		}
		return false;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		TileEntityGearbox gearbox = ((TileEntityGearbox) world.getBlockTileEntity(x, y, z));

		if (gearbox != null)
		{
			gearbox.delete();
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		switch (gearboxLevel)
		{
			case 1:
				this.blockIcon = par1IconRegister.registerIcon("OWI:gearbox_wd2wd");
			case 2:
				this.blockIcon = par1IconRegister.registerIcon("OWI:gearbox_wd2sn");
			case 3:
				this.blockIcon = par1IconRegister.registerIcon("OWI:gearbox_wd2ir");
		}
	}
}
