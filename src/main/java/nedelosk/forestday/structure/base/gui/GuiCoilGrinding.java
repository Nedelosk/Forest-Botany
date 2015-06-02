package nedelosk.forestday.structure.base.gui;

import nedelosk.forestday.structure.base.blocks.tile.TileCoilGrinding;
import nedelosk.nedeloskcore.client.gui.GuiBase;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

public class GuiCoilGrinding extends GuiBase {
	
	public GuiCoilGrinding(TileCoilGrinding tile, InventoryPlayer inventory) {
		super(tile, inventory);
	}

	@Override
	protected void renderStrings(FontRenderer fontRenderer, int x, int y) {
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void renderProgressBar() {
		
	}

	@Override
	protected String getGuiName() {
		return "coil_grinding";
	}
}
