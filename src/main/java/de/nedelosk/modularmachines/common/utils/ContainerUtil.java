package de.nedelosk.modularmachines.common.utils;

import java.util.List;

import de.nedelosk.modularmachines.api.ItemUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerUtil {

	private static final int playerInventorySize = 9 * 4;
	private static final int playerHotbarSize = 9;

	public static ItemStack transferStackInSlot(List inventorySlots, EntityPlayer player, int slotIndex) {
		Slot slot = (Slot) inventorySlots.get(slotIndex);
		if (slot == null || !slot.getHasStack()) {
			return null;
		}

		int numSlots = inventorySlots.size();
		ItemStack stackInSlot = slot.getStack();
		ItemStack originalStack = stackInSlot.copy();

		if (!shiftItemStack(inventorySlots, stackInSlot, slotIndex, numSlots)) {
			return null;
		}

		slot.onSlotChange(stackInSlot, originalStack);
		if (stackInSlot.stackSize <= 0) {
			slot.putStack(null);
		} else {
			slot.onSlotChanged();
		}

		if (stackInSlot.stackSize == originalStack.stackSize) {
			return null;
		}

		slot.onPickupFromSlot(player, stackInSlot);
		return originalStack;
	}

	private static boolean shiftItemStack(List inventorySlots, ItemStack stackInSlot, int slotIndex, int numSlots) {
		if (isInPlayerInventory(slotIndex)) {
			if (shiftToInventory(inventorySlots, stackInSlot, numSlots)) {
				return true;
			}

			if (isInPlayerHotbar(slotIndex)) {
				return shiftToPlayerInventoryNoHotbar(inventorySlots, stackInSlot);
			} else {
				return shiftToHotbar(inventorySlots, stackInSlot);
			}
		} else {
			return shiftToPlayerInventory(inventorySlots, stackInSlot);
		}
	}

	private static boolean shiftToPlayerInventory(List inventorySlots, ItemStack stackInSlot) {
		int playerHotbarStart = playerInventorySize - playerHotbarSize;

		// try to merge with existing stacks, hotbar first
		boolean shifted = shiftItemStackToRangeMerge(inventorySlots, stackInSlot, playerHotbarStart, playerHotbarSize);
		shifted |= shiftItemStackToRangeMerge(inventorySlots, stackInSlot, 0, playerHotbarStart);

		// shift to open slots, hotbar first
		shifted |= shiftItemStackToRangeOpenSlots(inventorySlots, stackInSlot, playerHotbarStart, playerHotbarSize);
		shifted |= shiftItemStackToRangeOpenSlots(inventorySlots, stackInSlot, 0, playerHotbarStart);
		return shifted;
	}

	private static boolean shiftToPlayerInventoryNoHotbar(List inventorySlots, ItemStack stackInSlot) {
		return shiftItemStackToRange(inventorySlots, stackInSlot, 0, playerInventorySize - playerHotbarSize);
	}

	private static boolean shiftToHotbar(List inventorySlots, ItemStack stackInSlot) {
		return shiftItemStackToRange(inventorySlots, stackInSlot, playerInventorySize - playerHotbarSize, playerHotbarSize);
	}

	private static boolean shiftToInventory(List inventorySlots, ItemStack stackToShift, int numSlots) {
		boolean success = false;
		if (stackToShift.isStackable()) {
			success = shiftToMachineInventory(inventorySlots, stackToShift, numSlots, true);
		}
		if (stackToShift.stackSize > 0) {
			success |= shiftToMachineInventory(inventorySlots, stackToShift, numSlots, false);
		}
		return success;
	}

	// if mergeOnly = true, don't shift into empty slots.
	private static boolean shiftToMachineInventory(List inventorySlots, ItemStack stackToShift, int numSlots, boolean mergeOnly) {
		for (int machineIndex = playerInventorySize; machineIndex < numSlots; machineIndex++) {
			Slot slot = (Slot) inventorySlots.get(machineIndex);
			if (mergeOnly && slot.getStack() == null) {
				continue;
			}
			if (!slot.isItemValid(stackToShift)) {
				continue;
			}
			if (shiftItemStackToRange(inventorySlots, stackToShift, machineIndex, 1)) {
				return true;
			}
		}
		return false;
	}

	private static boolean shiftItemStackToRange(List inventorySlots, ItemStack stackToShift, int start, int count) {
		boolean changed = shiftItemStackToRangeMerge(inventorySlots, stackToShift, start, count);
		changed |= shiftItemStackToRangeOpenSlots(inventorySlots, stackToShift, start, count);
		return changed;
	}

	private static boolean shiftItemStackToRangeMerge(List inventorySlots, ItemStack stackToShift, int start, int count) {
		if (stackToShift == null || !stackToShift.isStackable() || stackToShift.stackSize <= 0) {
			return false;
		}

		boolean changed = false;
		for (int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < start + count; slotIndex++) {
			Slot slot = (Slot) inventorySlots.get(slotIndex);
			ItemStack stackInSlot = slot.getStack();
			if (stackInSlot != null && ItemUtil.isIdenticalItem(stackInSlot, stackToShift)) {
				int resultingStackSize = stackInSlot.stackSize + stackToShift.stackSize;
				int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
				if (resultingStackSize <= max) {
					stackToShift.stackSize = 0;
					stackInSlot.stackSize = resultingStackSize;
					slot.onSlotChanged();
					changed = true;
				} else if (stackInSlot.stackSize < max) {
					stackToShift.stackSize -= max - stackInSlot.stackSize;
					stackInSlot.stackSize = max;
					slot.onSlotChanged();
					changed = true;
				}
			}
		}
		return changed;
	}


	private static boolean shiftItemStackToRangeOpenSlots(List inventorySlots, ItemStack stackToShift, int start, int count) {
		if (stackToShift == null || stackToShift.stackSize <= 0) {
			return false;
		}

		boolean changed = false;
		for (int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < start + count; slotIndex++) {
			Slot slot = (Slot) inventorySlots.get(slotIndex);
			ItemStack stackInSlot = slot.getStack();
			if (stackInSlot == null) {
				int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
				stackInSlot = stackToShift.copy();
				stackInSlot.stackSize = Math.min(stackToShift.stackSize, max);
				stackToShift.stackSize -= stackInSlot.stackSize;
				slot.putStack(stackInSlot);
				slot.onSlotChanged();
				changed = true;
			}
		}
		return changed;
	}

	private static boolean isInPlayerInventory(int slotIndex) {
		return slotIndex < playerInventorySize;
	}

	public static boolean isSlotInRange(int slotIndex, int start, int count) {
		return slotIndex >= start && slotIndex < start + count;
	}

	private static boolean isInPlayerHotbar(int slotIndex) {
		return isSlotInRange(slotIndex, playerInventorySize - playerHotbarSize, playerInventorySize);
	}
}
