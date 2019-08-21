package shionn.deamon.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class NetworkScanTest {

	@Test
	public void testScanByName() throws Exception {
		scanByName("shift.home");
		scanByName("libreelec.home");
		scanByName("ShiftX.home");
	}

	@Test
	public void testRetrieveLocal() throws Exception {
		System.out.println(InetAddress.getLocalHost().getHostAddress());
	}

	@Test
	public void scanByEthernet() throws SocketException {
		NetworkInterface byName = NetworkInterface.getByName("00:e1:8c:78:21:48");
		System.out.println(byName.getDisplayName());
	}

	private void scanByName(String name) throws IOException, UnknownHostException {
		InetAddress host = InetAddress.getByName(name);
		if (host.isReachable(5000)) {
			System.out.println(name + " " + host);
			String adress = host.getHostAddress();
			System.out.println(adress.substring(adress.lastIndexOf('.')));
		}
	}

}
