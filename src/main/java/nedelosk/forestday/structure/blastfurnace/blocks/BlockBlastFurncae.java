package nedelosk.forestday.structure.blastfurnace.blocks;

import java.util.List;

import nedelosk.forestday.Forestday;
import nedelosk.forestday.api.Tabs;
import nedelosk.forestday.machines.blocks.BlockMachieneBase;
import nedelosk.forestday.structure.alloysmelter.blocks.tile.TileAlloySmelterController;
import nedelosk.forestday.structure.alloysmelter.blocks.tile.TileAlloySmelterRegulator;
import nedelosk.forestday.structure.blastfurnace.blocks.tile.TileBlastFurnaceController;
import nedelosk.forestday.structure.blastfurnace.blocks.tile.TileBlastFurnaceRegulator;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBlastFurncae extends BlockMachieneBase {

	public IIcon[] icons;
	
	public final int meta_controller = 0;
	public final int meta_regulator = 1;
	public final int brick = 2;
	
	
	public BlockBlastFurncae() {
		super(Material.gourd);
		this.setStepSound(soundTypeStone);
		this.setHarvestLevel("pickaxe", 1);
		this.setBlockName("machine.furnace.blast");
		this.setCreativeTab(Tabs.tabForestdayMultiBlocks);
		}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
	
		if(world.getTileEntity(x, y,z) instanceof TileBlastFurnaceRegulator)
		{
		FMLNetworkHandler.openGui(player, Forestday.instance, 7, world, x, y, z);
		}
		if(world.getTileEntity(x, y,z) instanceof TileBlastFurnaceController)
		{
		FMLNetworkHandler.openGui(player, Forestday.instance, 5, world, x, y, z);
		}
		
	  return true;
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon (int side, int meta)
    {
        switch (meta) {
		case meta_controller:
			switch (side) {
			case 0:
				return icons[brick];
			case 1:
				return icons[brick];
			default:
				return icons[meta_controller];
			}
		case meta_regulator:
			switch (side) {
			case 0:
				return icons[meta_regulator];
			case 1:
				return icons[meta_regulator];
			default:
				return icons[brick];
			}
		default:
			return icons[brick];
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons (IIconRegister iconRegister)
    {
    	icons = new IIcon[3];
    	//Brick
        icons[meta_controller] = iconRegister.registerIcon("forestday:controllers/blast_furnace_controller");
        icons[meta_regulator] = iconRegister.registerIcon("forestday:regulators/blast_furnace_regulator");
        icons[brick] = iconRegister.registerIcon("forestday:walls/hardened_brick");
    }
    
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
			switch (meta) {
			case 0:
				return new TileBlastFurnaceController();
			case 1:
				return new TileBlastFurnaceRegulator();
			default:
				return null;
			}
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks (Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < 2; i++)
            par3List.add(new ItemStack(par1, 1, i));
    }

}
