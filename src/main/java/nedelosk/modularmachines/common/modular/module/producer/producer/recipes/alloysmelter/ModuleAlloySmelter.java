package nedelosk.modularmachines.common.modular.module.producer.producer.recipes.alloysmelter;

import java.util.ArrayList;

import nedelosk.modularmachines.api.materials.Material;
import nedelosk.modularmachines.api.materials.Stats;
import nedelosk.modularmachines.api.modular.machines.basic.IModular;
import nedelosk.modularmachines.api.modular.utils.ModuleStack;
import nedelosk.modularmachines.api.parts.IMachinePartProducer;
import nedelosk.modularmachines.api.parts.PartType;
import nedelosk.modularmachines.api.parts.PartType.MachinePartType;
import nedelosk.modularmachines.api.recipes.NeiStack;
import nedelosk.modularmachines.api.recipes.RecipeInput;
import nedelosk.modularmachines.common.core.registry.ItemRegistry;
import nedelosk.modularmachines.common.inventory.slots.SlotModuleMachine;
import nedelosk.modularmachines.common.materials.MachineState;
import nedelosk.modularmachines.common.modular.module.producer.producer.recipes.ModuleProducerRecipe;
import nedelosk.nedeloskcore.api.machines.IContainerBase;
import nedelosk.nedeloskcore.api.machines.IGuiBase;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ModuleAlloySmelter extends ModuleProducerRecipe {

	public ModuleAlloySmelter() {
		super("AlloySmelter", 2, 2);
	}

	@Override
	public ArrayList<Slot> addSlots(IContainerBase container, IModular modular) {
		ArrayList<Slot> list = new ArrayList<Slot>();
		list.add(new SlotModuleMachine(modular.getMachine(), 0, 56, 35, this.getName()));
		list.add(new SlotModuleMachine(modular.getMachine(), 1, 74, 35, this.getName()));
		list.add(new SlotModuleMachine(modular.getMachine(), 2, 116, 35, this.getName()){
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
		});
		list.add(new SlotModuleMachine(modular.getMachine(), 3, 134, 35, this.getName()){
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
		});
		return list;
	}
	
	@Override
	public ArrayList<NeiStack> addNEIStacks() {
		ArrayList<NeiStack> list = new ArrayList<NeiStack>();
		list.add(new NeiStack(56, 24, true));
		list.add(new NeiStack(74, 24, true));
		list.add(new NeiStack(116, 24, false));
		list.add(new NeiStack(134, 24, false));
		return list;
	}

	@Override
	public void addButtons(IGuiBase gui, IModular modular) {
		
	}

	@Override
	public void addWidgets(IGuiBase gui, IModular modular) {
		
	}

	@Override
	public RecipeInput[] getInputs(IModular modular) {
		return getInputItems(modular);
	}

	@Override
	public String getRecipeName() {
		return "AlloySmelter";
	}

	@Override
	public int getSpeedModifier() {
		return 15;
	}

	@Override
	public int getSizeInventory() {
		return 4;
	}

	@Override
	public PartType[] getRequiredComponents() {
		return new PartType[]{new MachinePartType(ItemRegistry.Burning_Chamber),
							  new MachinePartType(ItemRegistry.Module),
							  new MachinePartType(ItemRegistry.Burning_Chamber) };
	}

	@Override
	public ModuleStack creatModule(ItemStack stack) {
		IMachinePartProducer producer = (IMachinePartProducer) stack.getItem();
		Material[] materials = producer.getPartMaterials(stack);
		int size;
		int tiers = 0;
		for(size = 0;size < materials.length;size++){
			if(!materials[size].hasStats(Stats.MACHINE))
				return null;
			tiers += ((MachineState)materials[size].getStats(Stats.MACHINE)).tier();
		}
		int speedModifier = 40/tiers*size;
		return null;
	}

}
