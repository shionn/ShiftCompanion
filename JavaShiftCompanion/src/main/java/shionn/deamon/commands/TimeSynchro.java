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
		client.push(time());

	}

	public byte[] time() {
		Calendar time = Calendar.getInstance();
		return new byte[] { Commands.TIME_SET.cmd(), //
				(byte) (time.get(Calendar.YEAR) % 100), //
				(byte) (time.get(Calendar.MONTH) + 1), //
				(byte) time.get(Calendar.DAY_OF_MONTH), //
				(byte) time.get(Calendar.HOUR_OF_DAY), //
				(byte) time.get(Calendar.MINUTE), //
				(byte) time.get(Calendar.SECOND) };
	}

}
