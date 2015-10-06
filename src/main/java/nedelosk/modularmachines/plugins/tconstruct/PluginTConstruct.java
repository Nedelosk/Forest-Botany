package nedelosk.modularmachines.plugins.tconstruct;

import static net.minecraft.util.EnumChatFormatting.DARK_AQUA;
import static net.minecraft.util.EnumChatFormatting.DARK_PURPLE;
import static net.minecraft.util.EnumChatFormatting.DARK_RED;
import static net.minecraft.util.EnumChatFormatting.LIGHT_PURPLE;
import static net.minecraft.util.EnumChatFormatting.RED;

import nedelosk.modularmachines.api.materials.Material;
import nedelosk.modularmachines.api.materials.MaterialType;
import nedelosk.modularmachines.common.config.ModularConfig;
import nedelosk.modularmachines.common.core.MMCore;
import nedelosk.modularmachines.common.core.MMRegistry;
import nedelosk.modularmachines.common.core.manager.MMBlockManager;
import nedelosk.modularmachines.common.core.manager.MMItemManager;
import nedelosk.modularmachines.common.items.ItemMachinePattern;
import nedelosk.modularmachines.common.modular.utils.MaterialManager;
import nedelosk.nedeloskcore.plugins.basic.Plugin;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import tconstruct.TConstruct;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.FluidType;
import tconstruct.library.crafting.LiquidCasting;
import tconstruct.library.crafting.Smeltery;
import tconstruct.smeltery.TinkerSmeltery;

public class PluginTConstruct extends Plugin{

	@Override
	public void preInit() {
		MMRegistry.Cobalt = MMRegistry.addMachineMaterial(MaterialType.METAL_Custom, "Cobalt", 4, 2, DARK_AQUA.toString(), 0x2376DD, "Cobalt", 2, 275);
		MMRegistry.Ardite = MMRegistry.addMachineMaterial(MaterialType.METAL_Custom, "Ardite", 4, 0, DARK_RED.toString(), 0xA53000, "Ardite", 2, 275);
		MMRegistry.Manyullyn = MMRegistry.addMachineMaterial(MaterialType.METAL_Custom, "Manyullyn", 5, 0, DARK_PURPLE.toString(), 0x7338A5, "Manyullyn", 3, 250);
		MMRegistry.Alumite = MMRegistry.addMachineMaterial(MaterialType.METAL_Custom, "Alumite", 4, 2, LIGHT_PURPLE.toString(), 0xffa7e9, "Alumite", 2, 355);
		MMRegistry.Pig_Iron = MMRegistry.addMachineMaterial(MaterialType.METAL_Custom, "Pig Iron", 3, 1, RED.toString(), 0xF0A8A4, "PigIron", 1, 450);
		
		int[] costs = new int[]{ 1, 1, 8 };
		String[] components = new String[]{ "connection_wires", "screws", "saw_blades" };
		
		MMItemManager.MetalPattern.registerItem(new ItemMachinePattern("metal", components, costs).setCreativeTab(TConstructRegistry.materialTab));
	}
	
	@Override
	public void init() {
		Item[] patternOutputs = new Item[] { MMItemManager.Component_Connection_Wires.item(), MMItemManager.Component_Screws.item(), MMItemManager.Component_Saw_Blades.item() };
		int[] liquidDamage = new int[] { 2, 6, 7, 8, 9, 10, 11, 17, 18, 19, 20, 21 };
		Material[] material = new Material[]{ MMRegistry.Iron, MMRegistry.Tin, MMRegistry.Copper, MMRegistry.Bronze, MMRegistry.Steel, MMRegistry.Niobium, MMRegistry.Tantalum, MMRegistry.Cobalt, MMRegistry.Ardite, MMRegistry.Manyullyn, MMRegistry.Alumite, MMRegistry.Pig_Iron };
		
		FluidStack[] liquids = new FluidStack[] { new FluidStack(TinkerSmeltery.moltenIronFluid, 1), new FluidStack(TinkerSmeltery.moltenTinFluid, 1), new FluidStack(TinkerSmeltery.moltenCopperFluid, 1), new FluidStack(TinkerSmeltery.moltenBronzeFluid, 1), new FluidStack(TinkerSmeltery.moltenSteelFluid, 1), new FluidStack(MMCore.Niobium, 1), new FluidStack(MMCore.Tantalum, 1), new FluidStack(TinkerSmeltery.moltenCobaltFluid, 1), new FluidStack(TinkerSmeltery.moltenArditeFluid, 1), new FluidStack(TinkerSmeltery.moltenManyullynFluid, 1), new FluidStack(TinkerSmeltery.moltenAlumiteFluid, 1), new FluidStack(TinkerSmeltery.pigIronFluid, 1) };
		
		FluidType.registerFluidType("niobium", MMBlockManager.Metal_Blocks.block(), 10, 600, MMCore.Niobium, true);
		FluidType.registerFluidType("tantalum", MMBlockManager.Metal_Blocks.block(), 11, 600, MMCore.Tantalum, true);
		
        LiquidCasting tableCasting = TConstructRegistry.getTableCasting();
        for (int i = 0; i < patternOutputs.length; i++) {
            ItemStack cast = new ItemStack(MMItemManager.MetalPattern.item(), 1, i);

            tableCasting.addCastingRecipe(cast, new FluidStack(TinkerSmeltery.moltenAlubrassFluid, TConstruct.ingotLiquidValue), new ItemStack(patternOutputs[i], 1, Short.MAX_VALUE), false, 50);
            tableCasting.addCastingRecipe(cast, new FluidStack(TinkerSmeltery.moltenGoldFluid, TConstruct.ingotLiquidValue * 2), new ItemStack(patternOutputs[i], 1, Short.MAX_VALUE), false, 50);

            for (int iterTwo = 0; iterTwo < liquids.length; iterTwo++) {
                Fluid fs = liquids[iterTwo].getFluid();
                int fluidAmount = ((ItemMachinePattern)MMItemManager.MetalPattern.item()).getPatternCost(cast) * TConstruct.ingotLiquidValue / 2;
                ItemStack metalCast = new ItemStack(patternOutputs[i], 1, liquidDamage[iterTwo]);
                MaterialManager.setMaterial(metalCast, material[iterTwo]);
                tableCasting.addCastingRecipe(metalCast, new FluidStack(fs, fluidAmount), cast, 50);
                Smeltery.addMelting(FluidType.getFluidType(fs), metalCast, liquidDamage[iterTwo], fluidAmount);
            }
        }

        for (int iterTwo = 0; iterTwo < liquids.length; iterTwo++) {
                Fluid fs = liquids[iterTwo].getFluid();
                int fluidAmount = ((ItemMachinePattern)MMItemManager.MetalPattern.item()).getPatternCost(null) * TConstruct.ingotLiquidValue / 2;
                ItemStack metalCast = new ItemStack(MMItemManager.Component_Plates.item(), 1, liquidDamage[iterTwo]);
                MaterialManager.setMaterial(metalCast, material[iterTwo]);
                tableCasting.addCastingRecipe(metalCast, new FluidStack(fs, fluidAmount), null, 50);
                Smeltery.addMelting(FluidType.getFluidType(fs), metalCast, liquidDamage[iterTwo], fluidAmount);
        }
	}
	
	@Override
	public void postInit() {
	}
	
	@Override
	public String getRequiredMod() {
		return "TConstruct";
	}
	
	@Override
	public boolean getConfigOption() {
		return ModularConfig.pluginTinkers;
	}
	
}
