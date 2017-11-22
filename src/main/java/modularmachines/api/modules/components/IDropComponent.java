/*
 * Copyright (c) 2017 Nedelosk
 *
 * This work (the MOD) is licensed under the "MIT" License, see LICENSE for details.
 */
package modularmachines.api.modules.components;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface IDropComponent extends IModuleComponent {
	
	List<ItemStack> getDrops(NonNullList<ItemStack> drops);
}
