package modularmachines.common.network.packets;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

import modularmachines.api.modular.handlers.IModularHandler;
import modularmachines.api.modules.IModulePage;
import modularmachines.api.modules.network.DataInputStreamMM;
import modularmachines.api.modules.network.DataOutputStreamMM;
import modularmachines.api.modules.state.IModuleState;

public abstract class PacketModule extends PacketModularHandler {

	protected int position;
	protected String pageId;

	public PacketModule() {
	}

	public PacketModule(IModuleState module) {
		this(module.getModular().getHandler(), module.getPosition(), null);
		IModulePage currentPage = module.getModular().getCurrentPage();
		if (currentPage.getModuleState().getPosition() == module.getPosition()) {
			pageId = currentPage.getPageID();
		}
	}

	public PacketModule(IModularHandler handler, int position, String pageId) {
		super(handler);
		this.position = position;
		this.pageId = pageId;
	}

	public IModuleState getModule(EntityPlayer player) {
		return getModule(getModularHandler(player));
	}

	public IModuleState getModule(IModularHandler handler) {
		if (handler == null || handler.getModular() == null) {
			return null;
		}
		return handler.getModular().getModule(position);
	}

	@Override
	public void readData(DataInputStreamMM data) throws IOException {
		super.readData(data);
		position = data.readInt();
		if (data.readBoolean()) {
			pageId = DataInputStream.readUTF(data);
		}
	}

	@Override
	protected void writeData(DataOutputStreamMM data) throws IOException {
		super.writeData(data);
		data.writeInt(position);
		data.writeBoolean(pageId != null);
		if (pageId != null) {
			data.writeUTF(pageId);
		}
	}
}
