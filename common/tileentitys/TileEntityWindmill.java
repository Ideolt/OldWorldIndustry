package oldworldindustry.common.tileentitys;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.core.Position;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;

public class TileEntityWindmill extends TileEntity implements IPowerReceptor
{
	
	public ForgeDirection orientation; //the side that connects to a gearbox
	public boolean canWork;
	
	IPowerProvider provider;
	private static int windmilLevel;
	private static int minAirX;
	private static int minAirY;
	private static int minAirZ;
	private int timer;//to air recalculation
	private static Random random;
	
	public TileEntityWindmill(int Id,int level)
	{
		provider = PowerFramework.currentFramework.createPowerProvider();
		provider.configure(1, 1, 1, 0, 1000);
		provider.configurePowerPerdition(1, 1);
		solveOrientation(worldObj,xCoord,yCoord,zCoord);
		windmilLevel = level;
		random = new Random();
		
		switch (level)
		{
			case 1:
			{
				minAirX = 5;
				minAirY = 9;
				minAirZ = 5;
				break;
			}
			case 2:
			{
				minAirX = 5;
				minAirY = 9;
				minAirZ = 5;
				break;
			}
		}
			
	}
	
	@Override
	public void updateEntity()
	{
		generatePower();
		transferPower(orientation);
	}
	
	public void generatePower()
	{
		/*generates power from statistics:
		1. Biome
		2. Level
		3. AirBlocks around
		 */
		if(timer == 100)
		{
			canWork = solveAirAround();//recalculates every 5 seconds
			timer = 0;
		}
		else
			timer++;
		
		if(!canWork)
			return;
		
		int energy = random.nextInt(5) + 1;
		
		provider.receiveEnergy(energy, orientation.getOpposite());
		
	}
	
	public void transferPower(ForgeDirection to)
	{
		float energy = provider.useEnergy(0, 12, true);
		
		Position pos = new Position(xCoord,yCoord,zCoord,to);
		
		pos.moveForwards(1);
		
		TileEntity tile = worldObj.getBlockTileEntity((int) pos.x,(int) pos.y,(int) pos.z);
		
		if((tile instanceof IPowerReceptor)&&(tile != null))
			((IPowerReceptor)tile).getPowerProvider().receiveEnergy(energy, to.getOpposite());
		
	}
	
	public void solveOrientation(World world,int x,int y,int z)
	{
		for (int i = orientation.ordinal() + 1; i <= orientation.ordinal() + 6; ++i)
		{
			ForgeDirection o = ForgeDirection.VALID_DIRECTIONS[i % 6];

			Position pos = new Position(x, y, z, o);
			pos.moveForwards(1);
			TileEntity tile = world.getBlockTileEntity((int) pos.x, (int) pos.y, (int) pos.z);
			
			if(tile instanceof TileEntityGearbox)
			{
				orientation = pos.orientation;
				break;
			}
			else
				orientation = ForgeDirection.UNKNOWN;
		}
		
	}
	
	public boolean solveAirAround()
	{
		//For coord manipulation [needs half of maxAir to be in the center] 
		int X = 0 - (minAirX / 2);
		int Y = 0 - (minAirY / 2);
		int Z = 0 - (minAirZ / 2);
		
		while((X != (minAirX/2))&&(Y != (minAirY/2))&&(Z != (minAirZ/2)))
		{
			if((X == 0)&&(Y == 0)&&(Z == 0))
				continue;
			
			if(worldObj.getBlockMaterial(xCoord + X, yCoord + Y, zCoord +Z) != Material.air)
				return false;
			
			if(X != (minAirX/2))
				X++;
			else if(Z != (minAirZ / 2))
				Z++;
			else if(Y != (minAirY / 2))
				Y++;
		}
		return true;
	}

	@Override
	public void setPowerProvider(IPowerProvider provider)
	{
		this.provider = provider;
	}

	@Override
	public IPowerProvider getPowerProvider()
	{
		return provider;
	}

	@Override
	public void doWork()
	{			
	}

	@Override
	public int powerRequest(ForgeDirection from)
	{
		return 0;
	}

	
}