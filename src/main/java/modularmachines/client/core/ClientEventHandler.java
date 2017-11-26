package modularmachines.client.core;

import org.apache.commons.lang3.text.WordUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import modularmachines.api.EnumIOMode;
import modularmachines.api.IScrewdriver;
import modularmachines.api.modules.IModule;
import modularmachines.api.modules.components.IIOComponent;
import modularmachines.api.modules.container.IModuleContainer;
import modularmachines.client.model.ModelManager;
import modularmachines.common.ModularMachines;
import modularmachines.common.modules.ModuleCapabilities;
import modularmachines.common.utils.Translator;
import modularmachines.common.utils.WorldUtil;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public void tooltipEvent(ItemTooltipEvent event) {
		event.getToolTip().addAll(ModularMachines.proxy.addModuleInfo(event.getItemStack()));
	}
	
	@SubscribeEvent
	public void onTextureStitch(TextureStitchEvent.Pre event) {
		event.getMap().registerSprite(new ResourceLocation("modularmachines:gui/container"));
		event.getMap().registerSprite(new ResourceLocation("modularmachines:gui/liquid"));
	}
	
	@SubscribeEvent
	public void onBakeModel(ModelBakeEvent event) {
		ModelManager.getInstance().onBakeModels(event);
	}
	
	@SubscribeEvent
	public void onPostRenderOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
			return;
		}
		ScaledResolution resolution = event.getResolution();
		int width = resolution.getScaledWidth();
		int height = resolution.getScaledHeight();
		/*int y = height / 8 * 7;
		int x = width / 8 * 7;*/
		Minecraft mc = Minecraft.getMinecraft();
		FontRenderer fontRenderer = mc.fontRenderer;
		int y = height - (int) (fontRenderer.FONT_HEIGHT * 4.5F);
		int x = width - 65;
		EntityPlayer player = mc.player;
		World world = mc.world;
		RayTraceResult posHit = mc.objectMouseOver;
		ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
		if (heldItem.getItem() instanceof IScrewdriver) {
			IScrewdriver screwdriver = (IScrewdriver) heldItem.getItem();
			EnumFacing targetFacing = screwdriver.getSelectedFacing(heldItem);
			String facingText = targetFacing != null ? WordUtils.capitalize(targetFacing.getName()) : "None";
			String text = Translator.translateToLocalFormatted("mm.tooltip.screwdriver.facing", facingText);
			fontRenderer.drawString(text, x - fontRenderer.getStringWidth(text) / 2, y, -1);
			y += fontRenderer.FONT_HEIGHT;
			if (posHit != null && posHit.typeOfHit == RayTraceResult.Type.BLOCK) {
				String currentFacing = Translator.translateToLocalFormatted("mm.tooltip.screwdriver.facing.current", WordUtils.capitalize(posHit.sideHit.getName()));
				fontRenderer.drawString(currentFacing, x - fontRenderer.getStringWidth(currentFacing) / 2, y, -1);
				BlockPos pos = posHit.getBlockPos();
				EnumFacing facing = posHit.sideHit;
				TileEntity tileEntity = WorldUtil.getTile(world, pos, TileEntity.class);
				if (tileEntity != null && tileEntity.hasCapability(ModuleCapabilities.MODULE_CONTAINER, facing.getOpposite())) {
					IModuleContainer container = tileEntity.getCapability(ModuleCapabilities.MODULE_CONTAINER, facing.getOpposite());
					if (container != null) {
						IModule module = container.getModule(posHit.subHit);
						if (module == null) {
							return;
						}
						IIOComponent ioComponent = module.getComponent(IIOComponent.class);
						if (ioComponent == null) {
							return;
						}
						EnumIOMode mode = ioComponent.getMode(targetFacing);
						y += fontRenderer.FONT_HEIGHT;
						String modeText = Translator.translateToLocalFormatted("mm.tooltip.screwdriver.mode", Translator.translateToLocal(mode.getUnlocalizedName()));
						fontRenderer.drawString(modeText, x - fontRenderer.getStringWidth(modeText) / 2, y, -1);
					}
				}
			}
		}
	}
}
