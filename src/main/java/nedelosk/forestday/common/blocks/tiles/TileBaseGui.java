package nedelosk.forestday.common.blocks.tiles;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public abstract class TileBaseGui extends TileBase {

	public abstract Container getContainer(InventoryPlayer inventory);

	public abstract Object getGUIContainer(InventoryPlayer inventory);

}
