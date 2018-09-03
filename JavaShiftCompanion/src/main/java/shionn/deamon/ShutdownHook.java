package shionn.deamon;

public class ShutdownHook implements Runnable {

	private ShiftCompanionDeamon deamon;

	public ShutdownHook(ShiftCompanionDeamon deamon) {
		this.deamon = deamon;
	}

	@Override
	public void run() {
		deamon.shutdown();
	}

}
