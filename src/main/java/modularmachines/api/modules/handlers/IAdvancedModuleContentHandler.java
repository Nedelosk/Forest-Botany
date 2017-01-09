package modularmachines.api.modules.handlers;

import java.util.EnumMap;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;

import modularmachines.api.modules.IModule;
import modularmachines.api.modules.handlers.filters.IContentFilter;
import modularmachines.api.recipes.RecipeItem;

public interface IAdvancedModuleContentHandler<C, M extends IModule> extends IModuleContentHandler<M>, INBTSerializable<NBTTagCompound> {

	/**
	 * @return The insert filters of the handler.
	 */
	@Nonnull
	IContentFilter<C, M> getInsertFilter();

	/**
	 * @return The extract filters of the handler.
	 */
	@Nonnull
	IContentFilter<C, M> getExtractFilter();

	@Nonnull
	EnumMap<EnumFacing, boolean[]> getConfigurations();

	@Nonnull
	ContentInfo getInfo(int index);

	@Nonnull
	ContentInfo[] getContentInfos();

	boolean isInput(int index);

	int getInputs();

	int getOutputs();

	/**
	 * @return The content of the handler as a RecipeItem.
	 */
	RecipeItem[] getRecipeItems();

	void removeRecipeInputs(float chance, RecipeItem[] inputs);

	boolean canRemoveRecipeInputs(float chance, RecipeItem[] inputs);

	void addRecipeOutputs(float chance, RecipeItem[] outputs);

	boolean canAddRecipeOutputs(float chance, RecipeItem[] outputs);

	@Nonnull
	List<ItemStack> getDrops();
}
