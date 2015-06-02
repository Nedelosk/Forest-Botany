package nedelosk.forestday.common.machines.brick.furnace.coke;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nedelosk.nedeloskcore.common.inventory.ContainerBase;
import nedelosk.nedeloskcore.common.inventory.slots.SlotOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCokeFurnace extends ContainerBase {

	private int heat;
	
	public ContainerCokeFurnace(InventoryPlayer inventory, TileCokeFurnace tile) {
		super(tile, inventory);
		
	}
	
	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) 
    {
        return null;
    }

	@Override
	protected void addSlots(InventoryPlayer inventory) {
		
		//input
		addSlotToContainer(new Slot(tile, 0, 40, 34));
		
		//outnput
		addSlotToContainer(new SlotOutput(tile, 1, 111, 16));
		addSlotToContainer(new SlotOutput(tile, 2, 111, 34));
		addSlotToContainer(new SlotOutput(tile, 3, 111, 52));
	}

}
