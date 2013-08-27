package oldworldindustry.common.client;

import oldworldindustry.common.CommonProxy;
import oldworldindustry.common.client.renderers.TileEntityAxleRenderer;
import oldworldindustry.common.tileentitys.TileEntityAxle;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenders()
	{
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAxle.class, new TileEntityAxleRenderer());	
    }
}
