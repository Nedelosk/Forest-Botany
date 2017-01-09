package modularmachines.common.plugins.jei;

import java.text.NumberFormat;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.util.Translator;

public class TooltipCallback implements ITooltipCallback<ItemStack> {

	private final ArrayListMultimap<Integer, String> tooltips = ArrayListMultimap.create();

	@Override
	public void onTooltip(int index, boolean input, ItemStack ingredient, List<String> tooltip) {
		List<String> tip = tooltips.get(index);
		if (!tip.isEmpty()) {
			tooltip.addAll(tip);
		}
	}

	public void addChanceTooltip(int index, float chance) {
		if (chance == -1) {
			return;
		} else if (chance < 0) {
			chance = 0;
		} else if (chance > 1.0) {
			chance = 1.0f;
		}
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		percentFormat.setMaximumFractionDigits(2);
		String chanceString = String.valueOf(percentFormat.format(chance));
		tooltips.get(index).add(TextFormatting.GRAY + Translator.translateToLocalFormatted("gui.mm.jei.chance", chanceString));
	}
}
