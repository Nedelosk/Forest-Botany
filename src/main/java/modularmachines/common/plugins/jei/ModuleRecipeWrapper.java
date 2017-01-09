package modularmachines.common.plugins.jei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapper;
import modularmachines.api.recipes.IRecipe;
import modularmachines.api.recipes.RecipeItem;
import modularmachines.api.recipes.RecipeRegistry;
import scala.actors.threadpool.Arrays;

public class ModuleRecipeWrapper extends BlankRecipeWrapper implements IRecipeWrapper {

	protected IRecipe recipe;
	protected String recipeCategoryUid;

	public ModuleRecipeWrapper(IRecipe recipe, String recipeCategoryUid) {
		this.recipe = recipe;
		this.recipeCategoryUid = recipeCategoryUid;
	}

	/**
	 * @return The chance of an item. Only work with outputs.
	 */
	public float getChance(int index) {
		RecipeItem recipeItem = recipe.getOutputs()[index];
		float chance = recipeItem.chance;
		if (chance < 0) {
			return -1;
		}
		return chance;
	}

	public List<RecipeItem> getOutputItems() {
		return Arrays.asList(recipe.getOutputs());
	}

	@Override
	public List getInputs() {
		List inputs = new ArrayList<>();
		for (RecipeItem item : recipe.getInputs()) {
			if (item != null && !item.isNull()) {
				if (item.isItem()) {
					inputs.add(item.item);
				} else if (item.isOre()) {
					List oreStacks = new ArrayList<>();
					List<ItemStack> stacks = OreDictionary.getOres(item.ore.oreDict);
					for (ItemStack stack : stacks) {
						ItemStack newStack = stack.copy();
						newStack.stackSize = item.ore.stackSize;
						oreStacks.add(newStack);
					}
					inputs.add(oreStacks);
				}
			}
		}
		return inputs;
	}

	@Override
	public List getOutputs() {
		List inputs = new ArrayList<>();
		for (RecipeItem item : recipe.getOutputs()) {
			if (item != null && !item.isNull()) {
				if (item.isItem()) {
					inputs.add(item.item);
				}
			}
		}
		return inputs;
	}

	@Override
	public List<FluidStack> getFluidInputs() {
		List inputs = new ArrayList<>();
		for (RecipeItem item : recipe.getInputs()) {
			if (item != null && !item.isNull()) {
				if (item.isFluid()) {
					inputs.add(item.fluid);
				}
			}
		}
		return inputs;
	}

	@Override
	public List<FluidStack> getFluidOutputs() {
		List inputs = new ArrayList<>();
		for (RecipeItem item : recipe.getOutputs()) {
			if (item != null && !item.isNull()) {
				if (item.isFluid()) {
					inputs.add(item.fluid);
				}
			}
		}
		return inputs;
	}

	public static List<ModuleRecipeWrapper> getRecipes(String recipeCategory, String recipeCategoryUid, Class<? extends ModuleRecipeWrapper> wrapper) {
		List<ModuleRecipeWrapper> recipes = new ArrayList<>();
		for (IRecipe recipe : RecipeRegistry.getRecipeHandler(recipeCategory).getRecipes()) {
			try {
				recipes.add(wrapper.getConstructor(IRecipe.class, String.class).newInstance(recipe, recipeCategoryUid));
			} catch (Exception e) {
			}
		}
		return recipes;
	}

	public static List<ModuleRecipeWrapper> getRecipes(String recipeCategory, String recipeCategoryUid, Class<? extends ModuleRecipeWrapper> wrapper, IGuiHelper guiHelper) {
		List<ModuleRecipeWrapper> recipes = new ArrayList<>();
		for (IRecipe recipe : RecipeRegistry.getRecipeHandler(recipeCategory).getRecipes()) {
			try {
				recipes.add(wrapper.getConstructor(IRecipe.class, String.class, IGuiHelper.class).newInstance(recipe, recipeCategoryUid, guiHelper));
			} catch (Exception e) {
			}
		}
		return recipes;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		if (!getInputs().isEmpty()) {
			ingredients.setInputs(ItemStack.class, getInputs());
		}
		if (!getOutputs().isEmpty()) {
			ingredients.setOutputs(ItemStack.class, getOutputs());
		}
		if (!getFluidInputs().isEmpty()) {
			ingredients.setInputs(FluidStack.class, getFluidInputs());
		}
		if (!getFluidOutputs().isEmpty()) {
			ingredients.setOutputs(FluidStack.class, getFluidOutputs());
		}
	}
}
