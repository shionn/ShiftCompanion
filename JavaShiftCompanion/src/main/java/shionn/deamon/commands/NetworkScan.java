package shionn.deamon.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import shionn.deamon.arduino.ArduinoClient;
import shionn.deamon.arduino.Commands;

public class NetworkScan implements Runnable {
	private static final String BASE = "192.168.1.";
	private static final Map<String, Byte> hosts = new HashMap<>();

	private int ip = 1;

	private ArduinoClient client;

	public NetworkScan(ArduinoClient client) {
		this.client = client;
		hosts.put("libreelec", (byte) 0x01);
	}

	@Override
	public void run() {
		try {
			if (InetAddress.getByName(BASE + ip).isReachable(100)) {
				String hostName = InetAddress.getByName(BASE + ip).getHostName().toLowerCase();
				if (hosts.containsKey(hostName)) {
					client.push(new byte[] { Commands.NETWORK.cmd(), hosts.get(hostName), (byte) ip });
				}
			}
		} catch (IOException e) {
			LoggerFactory.getLogger(NetworkScan.class).error("Scan <" + BASE + ip + ">", e);
		}
		ip = ip + 1;
		if (ip >= 255) {
			ip = 1;
		}
	}
	
}
