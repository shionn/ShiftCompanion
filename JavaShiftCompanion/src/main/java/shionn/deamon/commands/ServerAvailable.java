package shionn.deamon.commands;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import shionn.deamon.arduino.ArduinoClient;
import shionn.deamon.arduino.Commands;

public class ServerAvailable implements Runnable {
	private Logger logger = LoggerFactory.getLogger(ServerAvailable.class);
	private String[] servers;
	private int server = 0;
	private ArduinoClient client;
	private boolean alert;

	public ServerAvailable(ArduinoClient client) {
		this.client = client;
		this.servers = new String[] { "http://www.shionn.org", "http://maven.shionn.org" };
	}

	@Override
	public void run() {
		boolean status = available();
		client.push(Commands.serverStatus((byte) server, status));
		if (!status && !alert) {
			client.push(Commands.stripFlash((byte) 255, (byte) 128, (byte) 0));
			alert = true;
		}
		if (status && alert) {
			client.push(Commands.stripRainbow());
			alert = false;
		}
		server = (server + 1) % servers.length;
	}

	private boolean available() {
		try {
			new URL(servers[server]).openConnection();
		} catch (MalformedURLException e) {
			logger.error("on " + servers[server], e);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

}
