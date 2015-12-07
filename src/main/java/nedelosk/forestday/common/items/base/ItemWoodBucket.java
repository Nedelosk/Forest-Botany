package nedelosk.forestday.common.items.base;

import nedelosk.forestday.common.core.managers.FItemManager;
import nedelosk.forestday.common.core.registry.FRegistry;
import net.minecraft.item.ItemBucket;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemWoodBucket extends ItemBucket {

	public ItemWoodBucket(Block contents, String name) {
		super(contents);
		setUnlocalizedName(FRegistry.setUnlocalizedItemName(name, "fd"));
		setTextureName("forestday:bucket.wood." + contents.getUnlocalizedName().replaceAll("tile.", ""));
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		MovingObjectPosition target = getMovingObjectPositionFromPlayer(world, player, this ==  FItemManager.Bucket_Wood.item());
		ItemStack result = itemStack;
        if (target == null)
        {
            return itemStack;
        }
        else if(target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK){
			if (itemStack.getItem() == FItemManager.Bucket_Wood.item()){
	            int hitX = target.blockX;
	            int hitY = target.blockY;
	            int hitZ = target.blockZ;
	
	            if (player != null && !player.canPlayerEdit(hitX, hitY, hitZ, target.sideHit, itemStack))
	            {
	                return itemStack;
	            }
	
	            Block bID = world.getBlock(hitX, hitY, hitZ);
	
	            // water and lava
	            if(bID == Blocks.water || bID == Blocks.flowing_water)
	            {
	                result = new ItemStack(FItemManager.Bucket_Wood_Water.item());
	                world.setBlockToAir(hitX, hitY, hitZ);
	            }
	            else if(bID == Blocks.lava || bID == Blocks.flowing_lava)
	            {
	            	player.setFire(10);
	            }
	        }else{
	        	
                int i = target.blockX;
                int j = target.blockY;
                int k = target.blockZ;

                if (target.sideHit == 0)
                {
                    --j;
                }

                if (target.sideHit == 1)
                {
                    ++j;
                }

                if (target.sideHit == 2)
                {
                    --k;
                }

                if (target.sideHit == 3)
                {
                    ++k;
                }

                if (target.sideHit == 4)
                {
                    --i;
                }

                if (target.sideHit == 5)
                {
                    ++i;
                }

                if (!player.canPlayerEdit(i, j, k, target.sideHit, itemStack))
                {
                    return itemStack;
                }

                if (this.tryPlaceContainedLiquid(world, i, j, k) && !player.capabilities.isCreativeMode)
                {
                    return new ItemStack(FItemManager.Bucket_Wood.item());
                }
	        }
		}
        return result;
	}
}
