package oldworldinsdustry.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "OWI", name = "OldWorldIndustry", version = "0.1")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class OldWorldIndustry
{
	@Instance("OWI")
	public static OldWorldIndustry instance;

	@SidedProxy(clientSide = "oldworldindustry.common.client.ClientProxy", serverSide = "oldworldindustry.common.CommonProxy")
	public static CommonProxy proxy;
	
	@Init
	public void load(FMLInitializationEvent event)
	{
		addBlocks();
		addNames();
		registerBlocks();
	}
	
	public void addBlocks()
	{
		//will register everything when the models will be present
	}
	
	public void addNames()
	{
		
	}
	
	public void registerBlocks()
	{
		
	}
}
