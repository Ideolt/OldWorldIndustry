package oldworldindustry.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import oldworldindustry.common.tileentitys.TileEntityGearbox;
import oldworldindustry.common.tileentitys.TileEntityWindmill;
import buildcraft.api.core.Position;

public class BlockWindmill extends BlockContainer
{
	TileEntityWindmill windmill;
	
	public BlockWindmill(int Id,int level)
	{
		super(Id, Material.wood);
		this.setCreativeTab(CreativeTabs.tabMisc);
		windmill = new TileEntityWindmill(Id,level);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving, ItemStack itemStack)
	{
			if(!canBePlaced())
			this.breakBlock(world, x, y, z, 0, 0);
	}
	
	
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return windmill;
	}

	public boolean canBePlaced()
	{
		if(windmill.orientation != ForgeDirection.UNKNOWN)			
			return true;
		
		return false;
	}

}
