package oldworldindustry.common;

import net.minecraft.block.Block;
import oldworldindustry.common.blocks.BlockAxle;
import oldworldindustry.common.blocks.BlockGearbox;
import oldworldindustry.common.client.renderers.TileEntityAxleRenderer;
import oldworldindustry.common.tileentitys.TileEntityAxle;
import oldworldindustry.common.tileentitys.TileEntityGearbox;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "OWI", name = "OldWorldIndustry", version = "0.1")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class OldWorldIndustry
{
	@Instance("OWI")
	public static OldWorldIndustry instance;

	@SidedProxy(clientSide = "oldworldindustry.common.client.ClientProxy", serverSide = "oldworldindustry.common.CommonProxy")
	public static CommonProxy proxy;
	
	public static Block axle;
	public static Block gearbox;
	public static TileEntityAxle axletile;
	public static TileEntityGearbox gearboxTile;
	
	@Init
	public void load(FMLInitializationEvent event)
	{
		addBlocks();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAxle.class, new TileEntityAxleRenderer());		
		addNames();
		registerBlocks();
	}
	
	public void addBlocks()
	{
		axle = new BlockAxle(501);
		gearbox = new BlockGearbox(502);
	}
	
	public void addNames()
	{
		LanguageRegistry.addName(axle, "Axle");
		LanguageRegistry.addName(gearbox, "Gearbox");
	}
	
	public void registerBlocks()
	{
		GameRegistry.registerBlock(axle, "OWIaxle");
		GameRegistry.registerTileEntity(oldworldindustry.common.tileentitys.TileEntityAxle.class, "OWIaxle");
		GameRegistry.registerBlock(gearbox, "OWIgearbox");
		GameRegistry.registerTileEntity(oldworldindustry.common.tileentitys.TileEntityGearbox.class, "OWIgearbox");
	
	}
}
