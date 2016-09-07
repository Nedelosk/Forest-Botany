package de.nedelosk.modularmachines.api.modules.storage;

import java.util.List;

import de.nedelosk.modularmachines.api.modules.IModule;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;

/**
 * A storage to storage module states. It is used in a modular and other thinks that have module states, to storage these.
 */
public interface IModuleStorage {

	/**
	 * @return All module states that are have a module that class is a instance of of moduleClass.
	 */
	<M extends IModule> List<IModuleState<M>> getModules(Class<? extends M> moduleClass);

	/**
	 * @return All modules states in a list.
	 */
	List<IModuleState> getModules();

	/**
	 * @return A module state that have the index.
	 */
	<M extends IModule> IModuleState<M> getModule(int index);

	/**
	 * @return The first module state that have a module that class is a instance of of moduleClass.
	 */
	<M extends IModule> IModuleState<M> getModule(Class<? extends M> moduleClass);

}
