package nedelosk.modularmachines.common.modular.module.tool.producer.machine.sawmill;

import nedelosk.modularmachines.api.recipes.Recipe;
import nedelosk.modularmachines.api.recipes.RecipeItem;
import net.minecraft.item.ItemStack;

public class RecipeSawMill extends Recipe {

	public RecipeSawMill(ItemStack input, RecipeItem[] output, int speedModifier, int energy) {
		super(new RecipeItem[] { new RecipeItem(input) }, output, speedModifier, energy, "SawMill");
	}

}
