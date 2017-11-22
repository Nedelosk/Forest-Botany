package modularmachines.client.model.module;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import modularmachines.api.modules.IModule;
import modularmachines.api.modules.IModuleHandler;
import modularmachines.api.modules.IModuleProvider;
import modularmachines.api.modules.model.IModelList;
import modularmachines.api.modules.model.IModelProperty;
import modularmachines.api.modules.model.IModuleModelState;
import modularmachines.api.modules.model.ModelLocationBuilder;
import modularmachines.api.modules.positions.EnumCasingPositions;
import modularmachines.common.modules.components.RackComponent;

@SideOnly(Side.CLIENT)
public class ModelDataCasing extends ModelData {
	
	private enum Property implements IModelProperty {
		BASE, LEFT, RIGHT
	}
	
	public static void initModelData(ModelLocationBuilder location) {
		ModelDataCasing casing = new ModelDataCasing();
		casing.add(Property.BASE, location.copy().setPreFix("casing"));
		casing.add(Property.LEFT, location.copy().setPreFix("side_left"));
		casing.add(Property.RIGHT, location.copy().setPreFix("side_right"));
		location.data().setModel(casing);
	}
	
	@Override
	public IModuleModelState createState(IModule module) {
		IModuleHandler moduleHandler = module.getParent();
		if (module instanceof IModuleProvider) {
			moduleHandler = ((IModuleProvider) module).getHandler();
		}
		ModuleModelState modelState = new ModuleModelState();
		IModule left = moduleHandler.getModule(EnumCasingPositions.LEFT);
		IModule right = moduleHandler.getModule(EnumCasingPositions.RIGHT);
		modelState.set(Property.LEFT, left == null || !left.hasComponent(RackComponent.class));
		modelState.set(Property.RIGHT, right == null || !right.hasComponent(RackComponent.class));
		return modelState;
	}
	
	@Override
	public void addModel(IModelList modelList, IModule module, IModuleModelState modelState) {
		if (modelState.get(Property.LEFT)) {
			modelList.add(Property.LEFT);
		}
		if (modelState.get(Property.RIGHT)) {
			modelList.add(Property.RIGHT);
		}
		modelList.add(Property.BASE);
		/*for (Module otherModule : storage.getModules().getModules()) {
			if(otherModule != module){
				models.add(ModelLoader.getModel(otherModule, storage, modelState, format));
			}
		}*/
	}
}
