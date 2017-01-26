package modularmachines.common.core;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;

@Mod(modid = Constants.MOD_ID, name = Constants.NAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES, guiFactory = "modularmachines.common.config.ConfigFactory")
public class ModularMachines {

	@Instance(Constants.MOD_ID)
	public static ModularMachines instance;
}
