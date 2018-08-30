package shionn;

import java.util.concurrent.TimeUnit;

import com.profesorfalken.jsensors.JSensors;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class ShiftCompanion {

	private String portname;
	private boolean shutdown;

	public ShiftCompanion(String portname) {
		this.portname = portname;
	}

	private void start() throws InterruptedException {
		Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook(this)));
		SerialPort port = new SerialPort(portname);
		try {
			if (port.openPort()) {
				port.setParams(SerialPort.BAUDRATE_9600, //
						SerialPort.DATABITS_8, //
						SerialPort.STOPBITS_1, //
						SerialPort.PARITY_NONE);
				TimeUnit.SECONDS.sleep(5);
				port.writeBytes(new Commands().time());
				TimeUnit.SECONDS.sleep(1);
				port.writeBytes(new Commands().changeMode(0x02));

				TimeUnit.SECONDS.sleep(10);
				while (!shutdown) {
					port.writeBytes(new Commands().systemState());
					TimeUnit.SECONDS.sleep(1);
				}
				port.closePort();
			}
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		if (args.length != 1) {
			System.out.println("need port");
			for (String port : SerialPortList.getPortNames()) {
				System.out.println(port);
			}
			JSensors.main(args);
		} else {
			new ShiftCompanion(args[0]).start();
		}
	}

	public void shutdown() {
		shutdown = true;
	}

}
