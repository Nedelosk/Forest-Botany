package nedelosk.modularmachines.api.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nedelosk.modularmachines.api.modular.IModular;
import nedelosk.modularmachines.api.modular.basic.IModularInventory;
import nedelosk.modularmachines.api.modular.basic.container.module.IModuleContainer;
import nedelosk.modularmachines.api.modular.basic.container.module.IMultiModuleContainer;
import nedelosk.modularmachines.api.modular.basic.container.module.ISingleModuleContainer;
import nedelosk.modularmachines.api.modules.IModule;
import nedelosk.modularmachines.api.modules.IModuleGui;
import nedelosk.modularmachines.api.modules.fluids.IModuleWithFluid;
import nedelosk.modularmachines.api.modules.inventory.IModuleInventory;
import nedelosk.modularmachines.api.modules.managers.fluids.IModuleTankManager;

public class ModularUtils {

	public static List<ModuleStack<IModuleWithFluid>> getFluidProducers(IModular modular) {
		List<ModuleStack<IModuleWithFluid>> stacks = new ArrayList();
		for ( ModuleStack stack : modular.getModuleStacks() ) {
			if (stack != null && stack.getModule() != null && stack.getModule() instanceof IModuleWithFluid) {
				if (((IModuleWithFluid) stack.getModule()).useFluids(stack)) {
					stacks.add(stack);
				}
			}
		}
		return stacks;
	}

	/* MODULES FROM MODULAR */
	public static ISingleModuleContainer getCasing(IModular modular) {
		return getSingleContainer(modular, ModuleCategoryUIDs.CASING);
	}

	public static ModuleStack<IModuleTankManager> getTankManager(IModular modular) {
		return getModuleStack(modular, ModuleCategoryUIDs.MANAGERS, ModuleCategoryUIDs.MANAGER_TANK);
	}

	public static IMultiModuleContainer getManagers(IModular modular) {
		return getMultiContainer(modular, ModuleCategoryUIDs.MANAGERS);
	}

	public static ISingleModuleContainer getBattery(IModular modular) {
		return getSingleContainer(modular, ModuleCategoryUIDs.BATTERY);
	}

	public static ISingleModuleContainer getEngine(IModular modular) {
		return getSingleContainer(modular, ModuleCategoryUIDs.ENGINE);
	}

	public static ISingleModuleContainer getMachine(IModular modular) {
		return getSingleContainer(modular, ModuleCategoryUIDs.MACHINE);
	}

	public static IMultiModuleContainer<IModule, Collection<ModuleStack<IModule>>> getStorages(IModular modular) {
		return getMultiContainer(modular, ModuleCategoryUIDs.STORAGES);
	}

	public static IMultiModuleContainer<IModule, Collection<ModuleStack<IModule>>> getCapacitors(IModular modular) {
		return getMultiContainer(modular, ModuleCategoryUIDs.CAPACITOR);
	}

	public static IMultiModuleContainer<IModule, Collection<ModuleStack<IModule>>> getMultiContainer(IModular modular, String categoryUID) {
		IModuleContainer container = getModuleStack(modular, categoryUID);
		if (container == null || !(container instanceof IMultiModuleContainer)) {
			return null;
		}
		return (IMultiModuleContainer) container;
	}

	public static ModuleStack getModuleStack(IModular modular, String categoryUID, String moduleUID) {
		return modular.getModuleFromUID(categoryUID + ":" + moduleUID);
	}

	public static ISingleModuleContainer getSingleContainer(IModular modular, String categoryUID) {
		IModuleContainer container = getModuleStack(modular, categoryUID);
		if (container == null || !(container instanceof ISingleModuleContainer)) {
			return null;
		}
		return (ISingleModuleContainer) container;
	}

	public static IModuleContainer getModuleStack(IModular modular, String categoryUID) {
		if (modular == null) {
			return null;
		}
		return modular.getModule(categoryUID);
	}

	public static <M extends IModule> ModuleStack<M> getModuleStackFromGui(IModularInventory modular, IModuleGui<M> gui) {
		return modular.getModuleFromUID(gui.getCategoryUID() + ":" + gui.getModuleUID());
	}

	public static <M extends IModule> ModuleStack<M> getModuleStackFromInventory(IModularInventory modular, IModuleInventory<M> inv) {
		return modular.getModuleFromUID(inv.getCategoryUID() + ":" + inv.getModuleUID());
	}
}
