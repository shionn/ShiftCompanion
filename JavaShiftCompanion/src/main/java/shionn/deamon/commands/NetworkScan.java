package shionn.deamon.commands;

import java.io.IOException;
import java.net.InetAddress;

import org.slf4j.LoggerFactory;

public class NetworkScan implements Runnable {
	private static final String BASE = "192.168.1.";

	private int ip = 1;

	public void scan() throws IOException {
		for (int i=1; i<255;i++) {
			if (InetAddress.getByName(BASE+i).isReachable(100)) {
				System.out.println(InetAddress.getByName(BASE+i).getHostName());
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new NetworkScan().scan();
	}

	@Override
	public void run() {
		try {
			if (InetAddress.getByName(BASE + ip).isReachable(100)) {
				System.out.println(InetAddress.getByName(BASE + ip).getHostName());
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
