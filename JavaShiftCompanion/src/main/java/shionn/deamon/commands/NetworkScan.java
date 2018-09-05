package shionn.deamon.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;

import shionn.deamon.arduino.ArduinoClient;

public class NetworkScan implements Runnable {
	private static final String BASE = "192.168.1.";
	private static final Map<String, Byte> hosts = new HashMap<>();

	private int ip = 1;

	private ArduinoClient client;

	public NetworkScan(ArduinoClient client) {
		this.client = client;
		hosts.put("shift.home", (byte) 0x01);
		hosts.put("libreelec.home", (byte) 0x02);
		hosts.put("shiftx.home", (byte) 0x01);
	}

	@Override
	public void run() {
		String host = BASE + ip;
		try {
			if (InetAddress.getByName(host).isReachable(100)) {
				String hostName = InetAddress.getByName(host).getHostName().toLowerCase();
				if (hosts.containsKey(hostName)) {
					client.push(null);
				}
			}
		} catch (IOException e) {
			LoggerFactory.getLogger(NetworkScan.class).error("Scan <" + host + ">", e);
		}
		ip = ip + 1;
		if (ip >= 255) {
			ip = 1;
		}
	}

}
