package modularmachines.common.modules.components.energy;

public class ProcessFinalComponent extends ProcessComponent {
	private final int processLength;
	
	public ProcessFinalComponent(int processLength) {
		this.processLength = processLength;
	}
	
	@Override
	public int getProcessLength() {
		return this.processLength;
	}
}
