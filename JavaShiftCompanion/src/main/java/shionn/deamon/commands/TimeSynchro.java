package shionn.deamon.commands;

import java.util.Calendar;

import shionn.deamon.arduino.ArduinoClient;
import shionn.deamon.arduino.Commands;

public class TimeSynchro implements Runnable {

	private ArduinoClient client;

	public TimeSynchro(ArduinoClient client) {
		this.client = client;
	}

	@Override
	public void run() {
		this.client.push(Commands.time(Calendar.getInstance()));
	}

}
