package oldworldindustry.common.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BasicItem extends Item
{
	public enum Function
	{
		GOVERNOR,DIFFERENTIAL,REDSTONEBUFFER,
		OILER,DICRIMINATOR,PASSTHROUGH,ITEM
	}
	
	private String iconName;
	private static Function function;
	
	public BasicItem(int id,String name,Function iFunction)
	{
		super(id);
		setCreativeTab(CreativeTabs.tabMisc);
		iconName = name;
		super.setUnlocalizedName(name);
		function = iFunction;		
	}
	
	public static Function getFunction()
	{
		return function;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.itemIcon = par1IconRegister.registerIcon("OWI:" + iconName);
	}

}
