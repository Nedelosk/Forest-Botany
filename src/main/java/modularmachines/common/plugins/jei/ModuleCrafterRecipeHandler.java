package modularmachines.common.plugins.jei;

import java.util.List;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.util.ErrorUtil;
import mezz.jei.util.Log;
import modularmachines.api.recipes.IModuleCrafterRecipe;

public class ModuleCrafterRecipeHandler implements IRecipeHandler<IModuleCrafterRecipe> {

	@Override
	public Class<IModuleCrafterRecipe> getRecipeClass() {
		return IModuleCrafterRecipe.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Override
	public String getRecipeCategoryUid(IModuleCrafterRecipe recipe) {
		if (recipe.getHolder() != null) {
			return CategoryUIDs.CRAFTING;
		}
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(IModuleCrafterRecipe recipe) {
		if (recipe.getHolder() == null) {
			return new ModuleCrafterWorkbenchRecipeWrapper(recipe);
		}
		return new ModuleCrafterRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(IModuleCrafterRecipe recipe) {
		if (recipe.getRecipeOutput() == null) {
			String recipeInfo = ErrorUtil.getInfoFromRecipe(recipe, this);
			Log.error("Recipe has no output. {}", recipeInfo);
			return false;
		}
		int inputCount = 0;
		for (Object input : recipe.getInput()) {
			if (input instanceof List) {
				if (((List) input).isEmpty()) {
					// missing items for an oreDict name. This is normal
					// behavior, but the recipe is invalid.
					return false;
				}
			}
			if (input != null) {
				inputCount++;
			}
		}
		if (inputCount > 9) {
			String recipeInfo = ErrorUtil.getInfoFromRecipe(recipe, this);
			Log.error("Recipe has too many inputs. {}", recipeInfo);
			return false;
		}
		if (inputCount == 0) {
			String recipeInfo = ErrorUtil.getInfoFromRecipe(recipe, this);
			Log.error("Recipe has no inputs. {}", recipeInfo);
			return false;
		}
		return true;
	}
}
