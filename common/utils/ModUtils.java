package oldworldindustry.common.utils;

import net.minecraft.item.Item;
import oldworldindustry.common.Config;
import oldworldindustry.common.items.BasicItem;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModUtils
{
	public static Item woodGear;
	public static Item stoneGear;
	public static Item ironGear;
	
	public static boolean buildCraftOn;
	
	private static void checkMods()
	{
		for (ModContainer cont : Loader.instance().getModList())
        {
            if (cont.getModId().equalsIgnoreCase("BuildCraft|Core"))
            {
            	buildCraftOn = true;
            	return;
            }
        }
	}
	
	public static void setUpItems()
	{
		checkMods();
		
		if(buildCraftOn)
		{
			woodGear = GameRegistry.findItem("BuildCraft|Core", "Wooden Gear");
			stoneGear = GameRegistry.findItem("BuildCraft|Core", "Stone Gear");
			ironGear = GameRegistry.findItem("BuildCraft|Core", "Iron Gear");
		}
		else
		{
			woodGear = new BasicItem(Config.LEVEL_RANGE_START+6).setUnlocalizedName("gear_wood");
			stoneGear = new BasicItem(Config.LEVEL_RANGE_START+7).setUnlocalizedName("gear_stone");
			ironGear = new BasicItem(Config.LEVEL_RANGE_START+8).setUnlocalizedName("gear_iron");
		}
		
	}
	
}
