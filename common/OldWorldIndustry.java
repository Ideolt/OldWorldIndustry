package oldworldindustry.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import oldworldindustry.common.blocks.BlockAxle;
import oldworldindustry.common.blocks.BlockGearbox;
import oldworldindustry.common.blocks.BlockWindmill;
import oldworldindustry.common.client.renderers.TileEntityAxleRenderer;
import oldworldindustry.common.items.BasicItem;
import oldworldindustry.common.tileentitys.TileEntityAxle;
import oldworldindustry.common.tileentitys.TileEntityGearbox;
import oldworldindustry.common.utils.ModUtils;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "OWI", name = "OldWorldIndustry", version = "0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class OldWorldIndustry
{
	@Instance("OWI")
	public static OldWorldIndustry instance;

	@SidedProxy(clientSide = "oldworldindustry.common.client.ClientProxy", serverSide = "oldworldindustry.common.CommonProxy")
	public static CommonProxy proxy;
	
	public static Block woodAxle;
	public static Block stoneAxle;
	public static Block ironAxle;
	
	public static Block woodGearbox;
	public static Block stoneGearbox;
	public static Block ironGearbox;
	
	public static Block woodWindmill;
	
	public static Item windmillHub;
	public static Item windmillBlade;
	
	public static Item governor;
	public static Item differential;
	public static Item redstoneBuffer;
	public static Item oiler;
	public static Item discriminator;
	public static Item passThrough;
	
	
	@PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
     Config.loadConfigs();
    }
	
	@Init
	public void load(FMLInitializationEvent event)
	{
		addBlocks();
		addItems();	
		addRecipes();
		addNames();
		registerBlocks();
		registerItems();
		bindRenderers();
	}
	
	@PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
     System.out.println("[OWI Online] Have Fun!");
    }
	
	public void addBlocks()
	{
		woodAxle = new BlockAxle(Config.LEVEL_RANGE_START,1);
		stoneAxle = new BlockAxle(Config.LEVEL_RANGE_START+1,2);
		ironAxle = new BlockAxle(Config.LEVEL_RANGE_START+2,3);
		
		woodGearbox = new BlockGearbox(Config.LEVEL_RANGE_START+3,1);
		stoneGearbox = new BlockGearbox(Config.LEVEL_RANGE_START+4,2);
		ironGearbox = new BlockGearbox(Config.LEVEL_RANGE_START+5,3);
		
		woodWindmill = new BlockWindmill(Config.LEVEL_RANGE_START+11,1);
	}
	
	// !!!            last used ID levelRange + 17			!!!
		
	public void addItems()
	{
		windmillHub = new BasicItem(Config.LEVEL_RANGE_START+9,"windmill_hub",BasicItem.Function.ITEM);
		windmillBlade = new BasicItem(Config.LEVEL_RANGE_START+10,"windmill_blade",BasicItem.Function.ITEM);
		
		governor = new BasicItem(Config.LEVEL_RANGE_START+12,"governor",BasicItem.Function.GOVERNOR);
		differential = new BasicItem(Config.LEVEL_RANGE_START+13,"differential",BasicItem.Function.DIFFERENTIAL);
		redstoneBuffer = new BasicItem(Config.LEVEL_RANGE_START+14,"gear_redstone",BasicItem.Function.REDSTONEBUFFER);
		oiler = new BasicItem(Config.LEVEL_RANGE_START+15,"governor",BasicItem.Function.OILER);//needs icon
		discriminator = new BasicItem(Config.LEVEL_RANGE_START+16,"governor",BasicItem.Function.DICRIMINATOR);//needs icon
		passThrough = new BasicItem(Config.LEVEL_RANGE_START+17,"governor",BasicItem.Function.PASSTHROUGH);//needs icon

	}
	
	public void addRecipes()
	{	
		ModUtils.setUpItems();
		
		ModLoader.addRecipe(new ItemStack(woodAxle,1) , new Object[] { "   ", "GSG", "   ", 'G', new ItemStack(ModUtils.woodGear), 'S', Item.stick });
		ModLoader.addRecipe(new ItemStack(stoneAxle,1) , new Object[] { "   ", "GSG", "   ", 'G', new ItemStack(ModUtils.stoneGear), 'S', Item.stick });
		ModLoader.addRecipe(new ItemStack(ironAxle,1) , new Object[] { "   ", "GSG", "   ", 'G', new ItemStack(ModUtils.ironGear), 'S', Item.stick });
		
		ModLoader.addRecipe(new ItemStack(woodGearbox,1) , new Object[] { "WGW", "GGG", "WGW ", 'G', new ItemStack(ModUtils.woodGear), 'W', Block.wood });
		ModLoader.addRecipe(new ItemStack(stoneGearbox,1) , new Object[] { "WGW", "GGG", "WGW", 'G', new ItemStack(ModUtils.stoneGear), 'W', Block.wood });
		ModLoader.addRecipe(new ItemStack(ironGearbox,1) , new Object[] { "WGW", "GGG", "WGW", 'G', new ItemStack(ModUtils.ironGear), 'W', Block.wood });
		
		ModLoader.addRecipe(new ItemStack(windmillBlade,1), new Object[] {"WS ","wS ","WS ", 'W',Block.wood,'w',Block.cloth,'S',Item.stick });
		ModLoader.addRecipe(new ItemStack(windmillHub,1), new Object[] {"sSs","SGS","sSs" ,'s',Item.silk,'S',Item.stick,'G', new ItemStack(ModUtils.woodGear)});
		
		ModLoader.addRecipe(new ItemStack(woodWindmill,1), new Object[] {" B ","BHB"," B ", 'B' ,new ItemStack(windmillBlade), 'H' ,new ItemStack(windmillHub) });
		
		
		if(!ModUtils.buildCraftOn)
		{
			ModLoader.addRecipe(new ItemStack(ModUtils.woodGear,1) , new Object[] { " S ", "S S", " S ", 'S', Item.stick });
			ModLoader.addRecipe(new ItemStack(ModUtils.stoneGear,1) , new Object[] { " C ", "CGC", " C ", 'G', new ItemStack(ModUtils.woodGear), 'C', Block.cobblestone });
			ModLoader.addRecipe(new ItemStack(ModUtils.ironGear,1) , new Object[] { " I ", "IGI", " I ", 'G', new ItemStack(ModUtils.stoneGear), 'I', Item.ingotIron });
		}
	}
	
	
	public void addNames()
	{
		LanguageRegistry.addName(woodAxle, "Wooden Axle");
		LanguageRegistry.addName(stoneAxle, "Stone Axle");
		LanguageRegistry.addName(ironAxle, "Iron Axle");
		
		LanguageRegistry.addName(woodGearbox, "Wooden Gearbox");
		LanguageRegistry.addName(stoneGearbox, "Stone Gearbox");
		LanguageRegistry.addName(ironGearbox, "Iron Gearbox");
		
		LanguageRegistry.addName(windmillBlade, "Windmill Blade");
		LanguageRegistry.addName(windmillHub, "Windmill Hub");
		
		LanguageRegistry.addName(woodWindmill, "Wood Windmill");
		
		if(!ModUtils.buildCraftOn)
		{
			LanguageRegistry.addName(ModUtils.woodGear, "Wooden Gear");
			LanguageRegistry.addName(ModUtils.stoneGear, "Stone Gear");
			LanguageRegistry.addName(ModUtils.ironGear, "Iron Gear");
		}
	}
	
	public void registerBlocks()
	{
		GameRegistry.registerBlock(woodAxle, "Wooden Axle");
		GameRegistry.registerBlock(stoneAxle, "Stone Axle");
		GameRegistry.registerBlock(ironAxle, "Iron Axle");
		
		GameRegistry.registerBlock(woodGearbox, "Wooden Gearbox");
		GameRegistry.registerBlock(stoneGearbox, "Stone Gearbox");
		GameRegistry.registerBlock(ironGearbox, "Iron Gearbox");
		
		GameRegistry.registerBlock(woodWindmill, "Wood Windmill");
		
		GameRegistry.registerTileEntity(oldworldindustry.common.tileentitys.TileEntityGearbox.class, "Gearbox Tile");
		GameRegistry.registerTileEntity(oldworldindustry.common.tileentitys.TileEntityAxle.class, "Axle Tile");
		GameRegistry.registerTileEntity(oldworldindustry.common.tileentitys.TileEntityWindmill.class, "Windmill Tile");
	}
	
	public void registerItems()
	{
		GameRegistry.registerItem(windmillBlade, "Windmill Blade");
		GameRegistry.registerItem(windmillHub,"Windmill HuB");
		
		if(!ModUtils.buildCraftOn)
		{
			GameRegistry.registerItem(ModUtils.woodGear, "Wooden Gear");
			GameRegistry.registerItem(ModUtils.stoneGear, "Stone Gear");
			GameRegistry.registerItem(ModUtils.ironGear, "Iron Gear");
		}		
	}
	
	public void bindRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAxle.class, new TileEntityAxleRenderer());	
	}
}
