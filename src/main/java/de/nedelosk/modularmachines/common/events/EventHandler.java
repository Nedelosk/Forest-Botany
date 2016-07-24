package de.nedelosk.modularmachines.common.events;

import com.mojang.realmsclient.gui.ChatFormatting;

import de.nedelosk.modularmachines.api.modular.ModularManager;
import de.nedelosk.modularmachines.api.modules.IModuleContainer;
import de.nedelosk.modularmachines.api.modules.ModuleEvents;
import de.nedelosk.modularmachines.api.modules.items.IModuleProvider;
import de.nedelosk.modularmachines.api.modules.items.ModuleProvider;
import de.nedelosk.modularmachines.client.model.ModelModular;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventHandler {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void tooltipEvent(ItemTooltipEvent event) {
		IModuleContainer container = ModularManager.getContainerFromItem(event.getItemStack());
		if (container != null) {
			container.addTooltip(event.getToolTip());
		}
		ItemStack stack = event.getItemStack();
		event.getToolTip().add(ChatFormatting.YELLOW.toString() + stack.getItem().getRegistryName() + ":" + stack.getItemDamage());
	}


	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onModuleModelInit(ModuleEvents.ModuleModelInitEvent event) {
		IModuleContainer conatiner = event.getContainer();
		ResourceLocation windowLocation = conatiner.getModule().getWindowLocation(conatiner);
		if(windowLocation != null){
			ModelLoaderRegistry.getModelOrMissing(windowLocation);
		}
	}

	@SubscribeEvent
	public void onInitCapabilities(AttachCapabilitiesEvent.Item event) {
		IModuleContainer container = ModularManager.getContainerFromItem(event.getItemStack());
		if(container != null){
			IModuleProvider provider = new ModuleProvider();
			provider.setState(ModularManager.loadModuleState(null, event.getItemStack(), container));
			event.addCapability(new ResourceLocation("modularmachines:modules"), provider);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onTextureStitch(TextureStitchEvent event) {
		event.getMap().registerSprite(new ResourceLocation("modularmachines:gui/container"));
		event.getMap().registerSprite(new ResourceLocation("modularmachines:gui/liquid"));
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onBakeModel(ModelBakeEvent event) {
		IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
		registry.putObject(new ModelResourceLocation("modularmachines:modular"), new ModelModular());
		registry.putObject(new ModelResourceLocation("modularmachines:modular", "inventory"), new ModelModular());
	}
}
