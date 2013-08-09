package oldworldinsdustry.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import oldworldinsdustry.common.tileentitys.TileEntityGearbox;

public class BlockGearbox extends BlockContainer implements IConnectable
{
	private boolean connected;
	private int tear;
	
	protected BlockGearbox(int id)
	{
		super(id, Material.wood);
		this.setHardness(4F);
		this.setResistance(40F);
		this.setStepSound(soundWoodFootstep);
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setUnlocalizedName("OWIGearbox");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityGearbox();
	}

	@Override
	public boolean isConnected()
	{
		return connected;
	}

	@Override
	public boolean canConnect()
	{
		return false;
	}

}
