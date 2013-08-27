package oldworldindustry.common.tileentitys;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.core.Position;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.api.power.PowerProvider;

public class TileEntityGearbox extends TileEntity implements IPowerReceptor
{
	private float energyStored;
	IPowerProvider provider;

	public ForgeDirection orientation = ForgeDirection.UP; // default

	public TileEntityGearbox()
	{
		provider = PowerFramework.currentFramework.createPowerProvider();
		provider.configure(50, 100, 100, 60, 1000);
		provider.configurePowerPerdition(1, 1);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTag)
	{
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTag)
	{
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

	public boolean isPoweredTile(TileEntity tile)
	{
		if (tile instanceof IPowerReceptor)
		{
			IPowerProvider receptor = ((IPowerReceptor) tile).getPowerProvider();

			return receptor != null && receptor.getClass().getSuperclass().equals(PowerProvider.class);
		}

		return false;
	}

	public void delete()
	{
		// drops an item representation
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
	public void updateEntity()
	{
		for (int i = 0; i <= 5; i++)
		{
			IPowerReceptor receiver;
			
			Position pos = new Position(xCoord, yCoord, zCoord, ForgeDirection.getOrientation(i));
			pos.moveForwards(1);

			TileEntity tile = worldObj.getBlockTileEntity((int) pos.x, (int) pos.y, (int) pos.z);
			
			if (isPoweredTile(tile))
			{
				receiver = (IPowerReceptor) tile; 
			}
			else
			{
				continue;
			}
			
			if(receiver.powerRequest(ForgeDirection.getOrientation(i)) == 0)
			{
				float energy = ((IPowerReceptor) tile).getPowerProvider().useEnergy(10, 50, true);
				provider.receiveEnergy(energy, ForgeDirection.getOrientation(i));
			}
			else
			{
				float request = receiver.powerRequest(ForgeDirection.getOrientation(i));
				
				if(request+receiver.getPowerProvider().getEnergyStored() >= receiver.getPowerProvider().getMaxEnergyStored())
					request = receiver.getPowerProvider().getMaxEnergyStored() - request+receiver.getPowerProvider().getEnergyStored();

		        float energy = provider.useEnergy(0.0F, request, true);
		        receiver.getPowerProvider().receiveEnergy(energy, ForgeDirection.getOrientation(i));
			}			
		}
	}

	@Override
	public int powerRequest(ForgeDirection from)
	{
		return provider.getMaxEnergyReceived();
	}
}