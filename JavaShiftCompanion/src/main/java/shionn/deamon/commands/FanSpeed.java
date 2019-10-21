package shionn.deamon.commands;

import shionn.deamon.arduino.ArduinoClient;
import shionn.deamon.arduino.Commands;

public class FanSpeed implements Runnable {

	private ArduinoClient client;

	public FanSpeed(ArduinoClient client) {
		this.client = client;
	}

	@Override
	public void run() {
		client.push(Commands.fanSpeed((byte) 96, (byte) 96, (byte) 96));
	}

}
