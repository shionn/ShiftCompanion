package shionn;

public class ShutdownHook implements Runnable {

	private ShiftCompanion companion;

	public ShutdownHook(ShiftCompanion companion) {
		this.companion = companion;
	}

	@Override
	public void run() {
		companion.shutdown();
	}

}
