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
	IPowerProvider provider;

	public ForgeDirection orientation = ForgeDirection.UP; // default

	public TileEntityAxle(int minReceived, int maxReceived, int activationEnergy, int storage)
	{
		provider = PowerFramework.currentFramework.createPowerProvider();
		provider.configurePowerPerdition(1, 100);
		provider.configure(1, minReceived, maxReceived, activationEnergy, storage);
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

	private void takeEnergy(IPowerReceptor Eprovider,ForgeDirection from)
	{
		if(provider.getEnergyStored() < provider.getMaxEnergyStored())
			return;
		else
		{
			float energy = Eprovider.getPowerProvider().useEnergy(provider.getMinEnergyReceived(), provider.getMaxEnergyReceived(), true);
			provider.receiveEnergy(energy, from);
		}
		
	}
	
	private void giveEnergy(IPowerReceptor receiver,ForgeDirection to)
	{
		if(provider.getEnergyStored() <= 0)
			return;
		else
		{
			float request = receiver.powerRequest(to.getOpposite());
			
			float energy = provider.useEnergy(0, request, true);
			receiver.getPowerProvider().receiveEnergy(energy, to.getOpposite());
		}
			
	}
	
	@Override
	public void doWork()
	{
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
			
			if(tile instanceof TileEntityAxle)
			{
				if(!(((TileEntityAxle) tile).orientation == orientation) || !(((TileEntityAxle) tile).orientation == orientation.getOpposite()))
					return;
			}
			
			if(receiver.powerRequest(orientation.getOpposite()) == 0)
			{
				takeEnergy(receiver,orientation.getOpposite());
			}
			else
			{
				giveEnergy(receiver,orientation);
			}
		}
		
		pos.moveBackwards(2);
		
		tile = worldObj.getBlockTileEntity((int)pos.x, (int)pos.y, (int)pos.z);
		
		if (isPoweredTile(tile))
		{
			receiver = (IPowerReceptor)tile;
			
			if(tile instanceof TileEntityAxle)
			{
				if(!(((TileEntityAxle) tile).orientation == orientation||((TileEntityAxle) tile).orientation == orientation.getOpposite()))
					return;
			}
			
			if(receiver.powerRequest(orientation.getOpposite()) == 0)
			{
				takeEnergy(receiver,orientation.getOpposite());
			}
			else
			{
				giveEnergy(receiver,orientation);
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
