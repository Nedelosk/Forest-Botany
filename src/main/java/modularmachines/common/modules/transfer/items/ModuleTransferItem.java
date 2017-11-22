package modularmachines.common.modules.transfer.items;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import modularmachines.api.modules.pages.IPage;
import modularmachines.common.modules.Module;
import modularmachines.common.modules.transfer.ITransferCycle;
import modularmachines.common.modules.transfer.ITransferHandlerWrapper;
import modularmachines.common.modules.transfer.ModuleComponentTransfer;
import modularmachines.common.modules.transfer.ModuleTransfer;
import modularmachines.common.modules.transfer.TransferWrapperModule;
import modularmachines.common.modules.transfer.TransferWrapperTileEntity;

public class ModuleTransferItem extends ModuleTransfer<IItemHandler> {
	
	public ModuleTransferItem() {
		super();
	}
	
	@Override
	public Class<IItemHandler> getHandlerClass() {
		return IItemHandler.class;
	}
	
	@Override
	public ITransferHandlerWrapper<IItemHandler> getWrapper(NBTBase compound) {
		if (compound instanceof NBTTagShort) {
			EnumFacing facing = EnumFacing.VALUES[((NBTTagShort) compound).getShort()];
			ITransferHandlerWrapper wrapper = tileWrappers.get(facing);
			if (wrapper == null) {
				tileWrappers.put(facing, wrapper = createTileWrapper(facing));
			}
			return wrapper;
		} else {
			int index = ((NBTTagInt) compound).getInt();
			ITransferHandlerWrapper wrapper = moduleWrappers.get(index);
			if (wrapper == null) {
				moduleWrappers.put(index, wrapper = createModuleWrapper(index));
			}
			return wrapper;
		}
	}
	
	@Override
	public NBTBase writeWrapper(ITransferHandlerWrapper<IItemHandler> wrapper) {
		if (wrapper instanceof TransferWrapperTileEntity) {
			TransferWrapperTileEntity tileWrapper = (TransferWrapperTileEntity) wrapper;
			return new NBTTagShort((short) tileWrapper.getFacing().ordinal());
		} else if (wrapper instanceof TransferWrapperModule) {
			TransferWrapperModule moduleWrapper = (TransferWrapperModule) wrapper;
			return new NBTTagInt(moduleWrapper.getIndex());
		}
		return new NBTTagInt(0);
	}
	
	@Override
	public ITransferHandlerWrapper<IItemHandler> createModuleWrapper(int moduleIndex) {
		return new TransferWrapperModule<>(this, moduleIndex);
	}
	
	@Override
	public ITransferHandlerWrapper<IItemHandler> createTileWrapper(EnumFacing facing) {
		return new TransferWrapperTileEntity(this, facing);
	}
	
	@Override
	public IItemHandler getHandler(ITransferHandlerWrapper<IItemHandler> wrapper) {
		if (wrapper instanceof TransferWrapperTileEntity) {
			TransferWrapperTileEntity tileWrapper = (TransferWrapperTileEntity) wrapper;
			TileEntity tile = tileWrapper.getTileEntity();
			EnumFacing facing = tileWrapper.getFacing();
			return tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
		} else if (wrapper instanceof TransferWrapperModule) {
			TransferWrapperModule moduleWrapper = (TransferWrapperModule) wrapper;
			Module module = moduleWrapper.getModule();
			return module.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		}
		return null;
	}
	
	@Override
	public boolean isValid(ITransferHandlerWrapper<IItemHandler> wrapper) {
		if (wrapper instanceof TransferWrapperTileEntity) {
			TransferWrapperTileEntity tileWrapper = (TransferWrapperTileEntity) wrapper;
			TileEntity tile = tileWrapper.getTileEntity();
			EnumFacing facing = tileWrapper.getFacing();
			return tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
		} else if (wrapper instanceof TransferWrapperModule) {
			TransferWrapperModule moduleWrapper = (TransferWrapperModule) wrapper;
			Module module = moduleWrapper.getModule();
			return module.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		}
		return false;
	}
	
	@Override
	protected ModuleComponentTransfer createComponent(ModuleTransfer<IItemHandler> parent, int index) {
		return new ModuleComponentTransfer(this, index) {
			@Override
			public IPage createPage(GuiContainer gui) {
				return new PageTransferItem(this, gui);
			}
		};
	}
	
	@Override
	public ITransferCycle<IItemHandler> getCycle(NBTTagCompound compound) {
		return new ItemTransferCycle(this, compound);
	}
	
}
