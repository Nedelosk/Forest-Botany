/*
 * Copyright (c) 2017 Nedelosk
 *
 * This work (the MOD) is licensed under the "MIT" License, see LICENSE for details.
 */
package modularmachines.api.modules.pages;

import net.minecraft.client.gui.inventory.GuiContainer;

import modularmachines.api.modules.IModule;

public class DefaultModuleComponent<P extends IModule> extends PageComponent<P> {
	private final IPageFactory factory;
	
	public DefaultModuleComponent(P parent, IPageFactory factory) {
		super(parent);
		this.factory = factory;
	}
	
	@Override
	public IPage createPage(GuiContainer gui) {
		return factory.createPage(this, gui);
	}
	
	public interface IPageFactory {
		IPage createPage(IPageComponent component, GuiContainer gui);
	}
}
