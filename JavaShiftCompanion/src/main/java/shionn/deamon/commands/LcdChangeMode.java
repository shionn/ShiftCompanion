package shionn.deamon.commands;

import shionn.deamon.arduino.ArduinoClient;
import shionn.deamon.arduino.Commands;

public class LcdChangeMode implements Runnable {

	private ArduinoClient client;
	private int mode = 0;

	public LcdChangeMode(ArduinoClient client) {
		this.client = client;
	}

	@Override
	public void run() {
		client.push(changeMode(mode));
		mode++;
		if (mode == 3) {
			mode = 0;
		}
	}

	private byte[] changeMode(int mode) {
		return new byte[] { Commands.DISPLAY_MODE.cmd(), (byte) mode };
	}

}
