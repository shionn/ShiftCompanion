package shionn.deamon.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import shionn.deamon.arduino.ArduinoClient;

public class NewMailAlertTest {

	private NewMailAlert alert;
	private List<byte[]> cmds = new ArrayList<>();

	@Before
	public void setup() throws IOException {
		alert = new NewMailAlert(new ArduinoClient(null) {
			@Override
			public void push(byte[] cmd) {
				NewMailAlertTest.this.cmds.add(cmd);
			}
		});
	}

	@Test
	public void testRun() throws Exception {
		alert.run();
	}

}
