package de.nedelosk.modularmachines.api.modules.tools;

import de.nedelosk.modularmachines.api.modules.IModuleProperties;
import de.nedelosk.modularmachines.api.modules.state.IModuleState;

public interface IModuleMachineProperties extends IModuleProperties {

	double getMaxSpeed(IModuleState state);

	int getWorkTimeModifier(IModuleState state);
}
