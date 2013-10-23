package oldworldindustry.common.tileentitys;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import oldworldindustry.common.items.BasicItem;
import buildcraft.api.core.Position;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.api.power.PowerProvider;

public class TileEntityGearbox extends TileEntity implements IPowerReceptor,
		IInventory
{
	IPowerProvider provider;

	private ItemStack inv[];

	private List providers = new ArrayList();
	private List receivers = new ArrayList();

	private int[] out = { 50, 50, 50, 50, 50, 50, 0 };
	private int[] in =	{ 50, 50, 50, 50, 50, 50, 0 };
	private int[] transmit = { 0, 1, 2, 3, 4, 5, 6 };// used as counters

	private float maxOut;

	private static int gearboxLevel;

	public ForgeDirection orientation = ForgeDirection.UP; // default

	public TileEntityGearbox(int minReceived, int maxReceived,
			int activationEnergy, int storage, int level)
	{
		provider = PowerFramework.currentFramework.createPowerProvider();
		provider.configure(1, minReceived, maxReceived, activationEnergy,
				storage);
		provider.configurePowerPerdition(1, 1);
		gearboxLevel = level;

		inv = new ItemStack[level];
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTag)
	{
		provider.readFromNBT(nbtTag);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTag)
	{
		provider.writeToNBT(nbtTag);
	}

	public void switchOrientation()
	{
		for (int i = orientation.ordinal() + 1; i <= orientation.ordinal() + 6; ++i)
		{
			ForgeDirection o = ForgeDirection.VALID_DIRECTIONS[i % 6];

			Position pos = new Position(xCoord, yCoord, zCoord, o);
			pos.moveForwards(1);
			TileEntity tile = worldObj.getBlockTileEntity((int) pos.x,
					(int) pos.y, (int) pos.z);

			if (isPoweredTile(tile))
			{
				orientation = o;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord,
						worldObj.getBlockId(xCoord, yCoord, zCoord));

				break;
			}
		}
	}

	private void takeEnergy(IPowerReceptor Eprovider, ForgeDirection from)
	{
		if (provider.getEnergyStored() < provider.getMaxEnergyStored()
				|| transmit[from.ordinal()] < 8)
		{
			transmit[from.ordinal()]++;
			return;
		}
		else
		{
			float energy = Eprovider.getPowerProvider().useEnergy(1,
					in[from.ordinal()], true);
			provider.receiveEnergy(energy, from);

			transmit[from.ordinal()] = 0;
		}

	}

	private void giveEnergy(IPowerReceptor receiver, ForgeDirection to)
	{
		if (provider.getEnergyStored() <= 0 || transmit[to.ordinal()] < 8)
		{
			transmit[to.ordinal()]++;
			return;
		}
		else
		{
			float request = receiver.powerRequest(to.getOpposite());

			if (request > out[to.ordinal()])
				request = out[to.ordinal()];
			else if (request > maxOut)
				request = maxOut;

			float energy = provider.useEnergy(0, request, true);
			receiver.getPowerProvider().receiveEnergy(energy, to.getOpposite());

			transmit[to.ordinal()] = 0;
		}

	}

	public boolean isPoweredTile(TileEntity tile)
	{
		if (tile instanceof IPowerReceptor)
		{
			IPowerProvider receptor = ((IPowerReceptor) tile)
					.getPowerProvider();

			return receptor != null
					&& receptor.getClass().getSuperclass()
							.equals(PowerProvider.class);
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
		int i;
		TileEntity tile;
		Position pos;

		for (i = 0; i <= 5; i++)
		{
			pos = new Position(xCoord, yCoord, zCoord,
					ForgeDirection.getOrientation(i));
			pos.moveForwards(1);

			tile = worldObj.getBlockTileEntity((int) pos.x, (int) pos.y,
					(int) pos.z);

			if (isPoweredTile(tile))
			{
				if (((IPowerReceptor) tile).powerRequest(ForgeDirection
						.getOrientation(i).getOpposite()) == 0)
					providers.add(ForgeDirection.getOrientation(i));
				else
					receivers.add(ForgeDirection.getOrientation(i));
			}
			else
			{
				continue;
			}
		}

		if (providers.size() + receivers.size() == 0)
			return;

		for (i = 0; i <= providers.size() - 1; i++)
		{
			pos = new Position(xCoord, yCoord, zCoord,
					(ForgeDirection) providers.get(i));
			pos.moveForwards(1);

			tile = worldObj.getBlockTileEntity((int) pos.x, (int) pos.y,
					(int) pos.z);

			takeEnergy((IPowerReceptor) tile, (ForgeDirection) providers.get(i));

		}

		if (receivers.size() > 0)
			maxOut = provider.getEnergyStored() / receivers.size();
		else
			return;

		for (i = 0; i <= providers.size() - 1; i++)
		{
			pos = new Position(xCoord, yCoord, zCoord,
					(ForgeDirection) providers.get(i));
			pos.moveForwards(1);
			tile = worldObj.getBlockTileEntity((int) pos.x, (int) pos.y,
					(int) pos.z);

			giveEnergy((IPowerReceptor) tile, (ForgeDirection) providers.get(i));

		}

	}

	@Override
	public int powerRequest(ForgeDirection from)
	{
		if (provider.getEnergyStored() < provider.getMaxEnergyStored())
			return provider.getMaxEnergyReceived();
		else
			return 0;
	}

	@Override
	public int getSizeInventory()
	{
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return inv[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		ItemStack stack = getStackInSlot(slot);
		if (stack != null)
		{
			if (stack.stackSize <= amount)
			{
				setInventorySlotContents(slot, null);
			}
			else
			{
				stack = stack.splitStack(amount);
				if (stack.stackSize == 0)
				{
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		ItemStack stack = getStackInSlot(slot);
		
		if (stack != null)
		{
			setInventorySlotContents(slot, null);
		}
		
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack)
	{
		inv[slot] = itemStack;

		if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
		{
			itemStack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName()
	{
		return "owi.tileentitygearbox";
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
                entityplayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public void openChest()
	{
	}

	@Override
	public void closeChest()
	{
	}

	@Override
	public boolean isStackValidForSlot(int slot, ItemStack itemStack)
	{
		if(itemStack.getItem() instanceof BasicItem)
			if(((BasicItem)itemStack.getItem()).getFunction() != BasicItem.Function.ITEM)
				return true;
		
		return false;
	}
}