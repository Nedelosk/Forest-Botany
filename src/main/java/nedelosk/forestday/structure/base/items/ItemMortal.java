package nedelosk.forestday.structure.base.items;

import java.util.List;

import nedelosk.forestday.api.Tabs;
import nedelosk.forestday.common.items.base.ItemForestday;
import nedelosk.forestday.common.registrys.ForestdayRegistry;
import nedelosk.forestday.structure.base.blocks.BlockCoilGrinding;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMortal extends ItemForestday {

	public String[] textures = new String[] { "mortar_soft", "mortar_medium", "mortar_hard", "mortar_enderium", "mortar_magic" };
	@SideOnly(Side.CLIENT)
    public IIcon[] itemIcon;
	
	public ItemMortal() {
		super(null, Tabs.tabForestdayItems);
		setHasSubtypes(true);
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons (IIconRegister iconRegister)
    {
        this.itemIcon = new IIcon[textures.length];

        for (int i = 0; i < this.itemIcon.length; ++i)
        {
            this.itemIcon[i] = iconRegister.registerIcon("forestday:building/" + textures[i]);
        }
    }
    
    @Override
    public void getSubItems (Item id, CreativeTabs tab, List list)
    {
        for (int i = 0; i < textures.length; i++)
            list.add(new ItemStack(id, 1, i));
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage (int meta)
    {
        return itemIcon[meta];
    }
    
    @Override
    public String getUnlocalizedName (ItemStack itemstack)
    {
        return ForestdayRegistry.setUnlocalizedItemName("build.material.mortal." + itemstack.getItemDamage());
    }

}
