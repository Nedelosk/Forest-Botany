package nedelosk.modularmachines.common.config;

import java.util.Map.Entry;

import nedelosk.modularmachines.api.modules.IModule;
import nedelosk.modularmachines.api.utils.ModuleRegistry;
import nedelosk.modularmachines.common.ModularMachines;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

public class Config {

	public static void load() {
		ModularMachines.config.load();
	}

	public static void preInit() {
		load();
		Configuration config = ModularMachines.config;
		generateAluminiumOre = config.get("OreGen", "Aluminium", true).getBoolean();
		generateColumbiteOre = config.get("OreGen", "Columbite", true).getBoolean();
		moduleModular = config.get("Modules", "Modular", true, "Add a Modular System, Module and Producers").getBoolean();
		pluginTinkers = config.get("Plugins", "Tinkers Construct", true).getBoolean();
		pluginEnderIO = config.get("Plugins", "EnderIO", true).getBoolean();
		pluginThermalExpansion = config.get("Plugins", "Thermal Expansion", true).getBoolean();
		pluginWaila = config.get("Plugins", "Waila", true).getBoolean();
		pluginMineTweaker3 = config.get("Plugins", "Mine Tweaker 3", true).getBoolean();
		bastFurnaceMaxHeat = config.get("Multiblocks", "Blast Furnace", 1500).getInt();
		cokeOvenMaxHeat = config.get("Multiblocks", "Coke Oven Plant", 1350).getInt();
		airHeatingPlantMaxHeat = config.get("Multiblocks", "Air Heating Plant", 750).getInt();
		save();
	}

	public static void init() {
		load();
		Configuration config = ModularMachines.config;
		for ( Entry<ResourceLocation, IModule> entry : ModuleRegistry.getModuleRegistry().getModules().entrySet() ) {
			if (!config.get("Module Registry", entry.getKey().toString(), true).getBoolean()) {
				ModuleRegistry.getModuleRegistry().getModules().remove(entry.getKey());
			}
		}
		/*
		 * ArrayList<ModuleStack> stacks =
		 * Lists.newArrayList(ModuleRegistry.getProducers().iterator()); for (
		 * ModuleStack module : stacks ) { String[] s =
		 * GameData.getItemRegistry().getNameForObject(module.getItem().getItem(
		 * )).split(":"); if (module.getItem() == null ||
		 * module.getItem().getItem() == null || !config .getBoolean(
		 * module.getModule().getName(module, false) + (module.getModule() !=
		 * null ? " : " + module.getModule().getName(module) : "") + " : " +
		 * module.getMaterial().getLocalName() + " : " +
		 * module.getItem().getUnlocalizedName(), "Modules." + s[0], true, ""))
		 * { ModuleRegistry.getProducers().remove(module);
		 * ItemProducers.getItems().remove(new Pair(module.getMaterial(),
		 * module.getModule())); } }
		 */
		save();
	}

	public static void save() {
		ModularMachines.config.save();
	}

	public static int bastFurnaceMaxHeat;
	public static int cokeOvenMaxHeat;
	public static int airHeatingPlantMaxHeat;
	public static boolean generateColumbiteOre;
	public static boolean generateAluminiumOre;
	public static boolean moduleModular;
	public static boolean pluginTinkers;
	public static boolean pluginEnderIO;
	public static boolean pluginThermalExpansion;
	public static boolean pluginWaila;
	public static boolean pluginMineTweaker3;
}
