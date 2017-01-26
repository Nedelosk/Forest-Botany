package modularmachines.client.gui.widgets;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import modularmachines.common.utils.RenderUtil;

/*@SideOnly(Side.CLIENT)
public class WidgetWashing extends Widget {

	public int workTime;
	public int worktTimeTotal;

	public WidgetWashing(int posX, int posY, int workTime, int workTimeTotal) {
		super(posX, posY, 20, 19);
		this.workTime = workTime;
		this.worktTimeTotal = workTimeTotal;
	}

	@Override
	public List<String> getTooltip(IGuiBase gui) {
		ArrayList<String> list = new ArrayList<>();
		if (worktTimeTotal != 0) {
			list.add(workTime + " / " + worktTimeTotal);
		}
		return list;
	}

	@Override
	public void draw(IGuiBase gui) {
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.enableAlpha();
		RenderUtil.bindTexture(widgetTexture);
		int process = (worktTimeTotal == 0) ? 0 : workTime * positon.width / worktTimeTotal;
		int sx = gui.getGuiLeft();
		int sy = gui.getGuiTop();
		gui.getGui().drawTexturedModalRect(sx + positon.x, sy + positon.y, 28, 171, positon.width, positon.height);
		if (workTime > 0) {
			gui.getGui().drawTexturedModalRect(sx + positon.x, sy + positon.y, 48, 171, process, positon.height);
		}
		GlStateManager.disableAlpha();
	}
}*/
