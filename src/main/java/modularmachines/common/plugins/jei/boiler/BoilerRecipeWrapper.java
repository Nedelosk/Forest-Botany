package modularmachines.common.plugins.jei.boiler;

import java.awt.Color;

import net.minecraft.client.Minecraft;

import modularmachines.api.recipes.IRecipe;
import modularmachines.api.recipes.Recipe;
import modularmachines.common.plugins.jei.ModuleRecipeWrapper;
import modularmachines.common.utils.Translator;

public class BoilerRecipeWrapper extends ModuleRecipeWrapper {

	public BoilerRecipeWrapper(IRecipe recipe, String recipeCategoryUid) {
		super(recipe, recipeCategoryUid);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		minecraft.fontRendererObj.drawString(Translator.translateToLocalFormatted("gui.mm.jei.heat", recipe.getValue(Recipe.HEAT)), 58, 14, Color.gray.getRGB());
	}
}
