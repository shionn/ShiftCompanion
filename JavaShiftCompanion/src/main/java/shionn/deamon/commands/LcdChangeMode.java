package shionn.deamon.commands;

import shionn.deamon.arduino.ArduinoClient;
import shionn.deamon.arduino.Commands;

public class LcdChangeMode implements Runnable {

	private ArduinoClient client;
	private byte mode = 0;

	public LcdChangeMode(ArduinoClient client) {
		this.client = client;
	}

	@Override
	public void run() {
		client.push(changeMode(mode));
		mode++;
		if (mode >= 4) {
			mode = 0;
		}
	}

	private byte[] changeMode(byte mode) {
		return Commands.lcdMode(mode);
	}

}
