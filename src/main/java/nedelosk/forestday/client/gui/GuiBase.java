package nedelosk.forestday.client.gui;

import org.lwjgl.opengl.GL11;

import nedelosk.forestday.api.guis.Button;
import nedelosk.forestday.api.guis.IButtonManager;
import nedelosk.forestday.api.guis.IGuiBase;
import nedelosk.forestday.api.guis.IWidgetManager;
import nedelosk.forestday.common.blocks.tiles.TileBaseInventory;
import nedelosk.forestday.utils.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public abstract class GuiBase<T extends TileBaseInventory> extends GuiContainer implements IGuiBase<T> {

	protected ResourceLocation guiTexture;
	protected T tile;
	protected ButtonManager buttonManager;
	protected WidgetManager widgetManager;

	public GuiBase(T tile, InventoryPlayer inventory) {
		super(tile.getContainer(inventory));
		this.tile = tile;
		widgetManager = new WidgetManager(this);
		buttonManager = new ButtonManager(this);
		guiTexture = RenderUtils.getResourceLocation(getModName(), getGuiName(), "gui");
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonManager.clear();
		addButtons();
		buttonList.addAll(buttonManager.getButtons());
	}

	public void addButtons() {

	}

	@Override
	public IButtonManager getButtonManager() {
		return buttonManager;
	}

	@Override
	public IWidgetManager getWidgetManager() {
		return widgetManager;
	}

	@Override
	public T getTile() {
		return tile;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		widgetManager.handleMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {

		renderStrings(fontRendererObj, param1, param2);
		widgetManager.drawTooltip(param1, param2);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderUtils.bindTexture(guiTexture);
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		renderProgressBar();

		widgetManager.drawWidgets();
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button instanceof Button)
			((Button) button).onButtonClick(this);
	}

	protected abstract void renderStrings(FontRenderer fontRenderer, int x, int y);

	protected abstract void renderProgressBar();

	protected abstract String getGuiName();

	protected abstract String getModName();

	public static RenderItem getItemRenderer() {
		return itemRender;
	}

	@Override
	public void setZLevel(float zLevel) {
		this.zLevel = zLevel;
	}

	@Override
	public int getGuiLeft() {
		return this.guiLeft;
	}

	@Override
	public int getGuiTop() {
		return this.guiTop;
	}

}
