package oldworldindustry.common.tileentitys;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.core.Position;
import buildcraft.api.core.SafeTimeTracker;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.api.power.PowerProvider;

public class TileEntityAxle extends TileEntity implements IPowerReceptor
{
	private int latency;
	private int minEnergyReceived;
	private int maxEnergyReceived;
	private int maxEnergyStored;
	private int activationEnergy;
	private float energyStored;
	private int loss;
	private int lossRegularyti;

	IPowerProvider provider;

	public ForgeDirection orientation = ForgeDirection.UP; // default

	public TileEntityAxle()
	{
		provider = PowerFramework.currentFramework.createPowerProvider();
		provider.configurePowerPerdition(1, 100);
		provider.configure(0, 10, 50, 1, 50);
	}

	public void switchOrientation()
	{
		for (int i = orientation.ordinal() + 1; i <= orientation.ordinal() + 6; ++i)
		{
			ForgeDirection o = ForgeDirection.VALID_DIRECTIONS[i % 6];

			Position pos = new Position(xCoord, yCoord, zCoord, o);
			pos.moveForwards(1);
			TileEntity tile = worldObj.getBlockTileEntity((int) pos.x, (int) pos.y, (int) pos.z);

			if (isPoweredTile(tile))
			{
				orientation = o;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));

				break;
			}
		}
	}
	
	public boolean isPoweredTile(TileEntity tile) {
		if (tile instanceof IPowerReceptor) {
			IPowerProvider receptor = ((IPowerReceptor) tile).getPowerProvider();

			return receptor != null && receptor.getClass().getSuperclass().equals(PowerProvider.class);
		}

		return false;
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

		for (int i = 0; i <= 5; i++)
		{
			Position pos = new Position(xCoord, yCoord, zCoord, ForgeDirection.getOrientation(i));
			pos.moveForwards(1);

			TileEntity tile = worldObj.getBlockTileEntity((int) pos.x, (int) pos.y, (int) pos.z);

			if (isPoweredTile(tile))
			{
				((IPowerReceptor) tile).doWork();// tile should take energy
			}

			if (isPoweredTile(tile) && ((IPowerReceptor)tile).getPowerProvider().isPowerSource(ForgeDirection.getOrientation(i)))
			{
				((IPowerReceptor) tile).getPowerProvider().useEnergy(10, 50, true);
			}
		}

	}
	
	@Override
	public void updateEntity()
	{
		float energy = 0;
		IPowerReceptor receiver;
		
		Position pos = new Position(xCoord, yCoord, zCoord, orientation);
		pos.moveForwards(1);

		TileEntity tile = worldObj.getBlockTileEntity((int) pos.x, (int) pos.y, (int) pos.z);
		
		if (isPoweredTile(tile))
		{
			receiver = (IPowerReceptor)tile;
			
			if(receiver.powerRequest(orientation.getOpposite()) == 0)
			{
				energy = ((IPowerReceptor) tile).getPowerProvider().useEnergy(10, 50, true);
				provider.receiveEnergy(energy, orientation);
			}
			else
			{
				float request = receiver.powerRequest(orientation);
				
				if(request+receiver.getPowerProvider().getEnergyStored() >= receiver.getPowerProvider().getMaxEnergyStored())
					request = receiver.getPowerProvider().getMaxEnergyStored() - request+receiver.getPowerProvider().getEnergyStored();

		        energy = provider.useEnergy(0.0F, request, true);
		        receiver.getPowerProvider().receiveEnergy(energy, orientation.getOpposite());
			}
		}
		
		pos = new Position(xCoord,yCoord,zCoord,orientation.getOpposite());
		pos.moveBackwards(1);
		
		tile = worldObj.getBlockTileEntity((int)pos.x, (int)pos.y, (int)pos.z);
		
		if (isPoweredTile(tile))
		{
			receiver = (IPowerReceptor)tile;
			
			if(receiver.powerRequest(orientation) == 0)
			{
				energy = ((IPowerReceptor) tile).getPowerProvider().useEnergy(10, 50, true);
				provider.receiveEnergy(energy, orientation.getOpposite());
			}
			else
			{
				float request = receiver.powerRequest(orientation.getOpposite());
				
				if(request+receiver.getPowerProvider().getEnergyStored() >= receiver.getPowerProvider().getMaxEnergyStored())
					request = receiver.getPowerProvider().getMaxEnergyStored() - request+receiver.getPowerProvider().getEnergyStored();

		        energy = provider.useEnergy(0.0F, request, true);
		        receiver.getPowerProvider().receiveEnergy(energy, orientation);
			}
		}
		
		
	}

	@Override
	public int powerRequest(ForgeDirection from)
	{
		if((from == orientation||from == orientation.getOpposite()) && provider.getEnergyStored() < provider.getMaxEnergyStored())
			return provider.getMaxEnergyReceived();
		else
			return 0;
		
	}

	public void delete()
	{
		// padropina itema ne bloka
	}

}
