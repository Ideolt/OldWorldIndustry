package oldworldindustry.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import oldworldindustry.common.tileentitys.TileEntityGearbox;
import oldworldindustry.common.tileentitys.TileWoodGearbox;
import buildcraft.api.power.IPowerReceptor;

public class BlockGearbox extends BlockContainer 
{
	public BlockGearbox(int id)
	{
		super(id, Material.iron);
		this.setHardness(1F);
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setUnlocalizedName("OWIGearbox");
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId)
	{
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileWoodGearbox();
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
}
