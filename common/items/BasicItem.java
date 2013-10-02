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
	private String iconName;

	public BasicItem(int i)
	{
		super(i);
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public Item setUnlocalizedName(String par1Str)
	{
		iconName = par1Str;
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.itemIcon = par1IconRegister.registerIcon("OWI:" + iconName);
	}

}
