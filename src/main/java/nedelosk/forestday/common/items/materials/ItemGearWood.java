package nedelosk.forestday.common.items.materials;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nedelosk.forestday.api.Tabs;
import nedelosk.forestday.api.crafting.IUnfinished;
import nedelosk.forestday.common.core.TabForestday;
import nedelosk.forestday.common.items.base.ItemForestday;
import nedelosk.forestday.common.registrys.ForestdayRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

public class ItemGearWood extends ItemForestday implements IUnfinished {
	
	@SideOnly(Side.CLIENT)
    public IIcon[] woodIcon;
	
	public ItemGearWood() {
		super(null, Tabs.tabForestdayItems);
		setHasSubtypes(true);
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons (IIconRegister iconRegister)
    {
        this.woodIcon = new IIcon[6];
        
        for(int i = 0; i < 6;i++)
        {
        	this.woodIcon[i] = iconRegister.registerIcon("forestday:gear/wood." + i);
        }
    }
    
    @Override
    public void getSubItems (Item id, CreativeTabs tab, List list)
    {
        for (int i = 0; i < 6; i++)
            list.add(new ItemStack(id, 1, i));
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage (int meta)
    {
        return woodIcon[meta];
    }
    
    @Override
    public String getUnlocalizedName (ItemStack itemstack)
    {
        return ForestdayRegistry.setUnlocalizedItemName("gear.wood." + itemstack.getItemDamage());
    }

	@Override
	public boolean isItemUnfinished(ItemStack stack) {
		return stack.getItemDamage() > 1;
	}

}
