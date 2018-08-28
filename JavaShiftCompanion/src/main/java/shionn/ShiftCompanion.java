package shionn;

import java.util.concurrent.TimeUnit;

import com.profesorfalken.jsensors.JSensors;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class ShiftCompanion implements Runnable {

	public static void main(String[] args) throws InterruptedException {
		new ShiftCompanion().start();
	}

	private boolean closed;

	private void start() throws InterruptedException {
		Runtime.getRuntime().addShutdownHook(new Thread(this));
		for (String port : SerialPortList.getPortNames()) {
			System.out.println(port);
		}
		SerialPort port = new SerialPort("/dev/ttyACM0");
		try {
			if (port.openPort()) {
				port.setParams(SerialPort.BAUDRATE_9600, //
						SerialPort.DATABITS_8, //
						SerialPort.STOPBITS_1, //
						SerialPort.PARITY_NONE);
				while (!closed) {
					double temp = JSensors.get.components().cpus.stream()
							.flatMap(cpu -> cpu.sensors.temperatures.stream())
							.filter(t -> t.name.equals("Temp Package id 0")).map(t -> t.value)
							.findFirst().orElse(0.0);
					double fan = JSensors.get.components().cpus.stream()
							.flatMap(cpu -> cpu.sensors.fans.stream())
							.filter(t -> t.name.equals("Temp Package id 0")).map(t -> t.value)
							.findFirst().orElse(0.0);
					port.writeBytes(new byte[] { 'T', (byte) temp });
					TimeUnit.SECONDS.sleep(1);
				}
				port.closePort();
			}
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		closed = true;
	}

}
