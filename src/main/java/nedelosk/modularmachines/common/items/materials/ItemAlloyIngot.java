package nedelosk.modularmachines.common.items.materials;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nedelosk.forestday.common.core.registry.FRegistry;
import nedelosk.modularmachines.common.items.ModularItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemAlloyIngot extends ModularItem {

	public String[] ingot = new String[] { "Bronze", "Invar" };
	@SideOnly(Side.CLIENT)
	public IIcon[] itemIcon;

	public ItemAlloyIngot() {
		super("ingotAlloy");
		setHasSubtypes(true);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = new IIcon[ingot.length];

		for (int i = 0; i < this.itemIcon.length; ++i) {
			this.itemIcon[i] = iconRegister.registerIcon("modularmachines:ingots/ingot" + ingot[i]);
		}
	}

	@Override
	public void getSubItems(Item id, CreativeTabs tab, List list) {
		for (int i = 0; i < ingot.length; i++)
			list.add(new ItemStack(id, 1, i));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamage(int meta) {
		return itemIcon[meta];
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return FRegistry.setUnlocalizedItemName("ingot.alloy." + itemstack.getItemDamage(), "mm");
	}

}
