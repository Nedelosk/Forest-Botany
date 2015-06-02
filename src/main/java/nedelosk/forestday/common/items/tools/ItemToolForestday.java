package nedelosk.forestday.common.items.tools;

import java.util.List;

import nedelosk.forestday.common.core.TabForestday;
import nedelosk.forestday.common.registrys.ForestdayRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.util.StatCollector;

public class ItemToolForestday extends Item {

	private String name;
	
	public ItemToolForestday(String name, int maxDamage, int tier, Material material)
	{
		this.setMaxDamage(maxDamage);
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setFull3D();
		this.tier = tier;
		this.material = material;
		this.maxStackSize = 1;
		this.setMaxStackSize(1);
		this.name = name;
		this.setTextureName("forestday:tools/" + name);
	}
	
	public ItemToolForestday(String name, int maxDamage)
	{
		this.setMaxDamage(maxDamage);
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setFull3D();
		this.maxStackSize = 1;
		this.name = name;
	}
	
	protected int tier;
	protected Material material;
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return  ForestdayRegistry.setUnlocalizedItemName( "tool." + name);
	}
	
	public int getTier()
	{
		return this.tier;
	}
	
	@Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
    aList.add(StatCollector.translateToLocal("forestday.tooltip.damage") + (aStack.getMaxDamage() - getDamage(aStack)) + "/" + aStack.getMaxDamage());
    aList.add(StatCollector.translateToLocal("forestday.tooltip.tier") + (getTier()));
    aList.add(StatCollector.translateToLocal("forestday.tooltip.material") + (material.getMaterial()));
	}
	
	public static enum Material 
	{
		Wood,
		Stone,
		Iron,
		Steel,
		Dark_Steel,
		Copper,
		Tin,
		Bronze,
		Gold,
		Diamond;
		
		public String material;
		
		public String getMaterial()
		{
			return material = name();
		}
	}
}
