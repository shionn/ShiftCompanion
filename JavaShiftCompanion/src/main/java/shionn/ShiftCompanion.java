package shionn;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import com.profesorfalken.jsensors.JSensors;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class ShiftCompanion {

	private static final byte DISPLAY_MODE_CMD = (byte) 0xB0;
	private static final byte SYS_STATE_CMD = (byte) 0xA0;
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
				TimeUnit.SECONDS.sleep(10);
				port.writeBytes(new byte[] { DISPLAY_MODE_CMD, 0x01 });
				TimeUnit.SECONDS.sleep(1);
				while (!shutdown) {
					port.writeBytes(
							new byte[] {
									SYS_STATE_CMD,
									temperature("ISA adapter", "Temp Package id 0"),
									temperature("ISA adapter", "Temp temp4"),
									sysLoad(),
									fan("ISA adapter", "Fan fan1"),
									fan("ISA adapter", "Fan fan2") });
					TimeUnit.SECONDS.sleep(1);
				}
				port.closePort();
			}
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	private byte temperature(String source, String sensor) {
		return JSensors.get.components().cpus.stream() //
				.filter(cpu -> cpu.name.equals(source)) //
				.flatMap(cpu -> cpu.sensors.temperatures.stream()) //
				.filter(temp -> temp.name.equals(sensor)) //
				.map(temp -> (byte) (double) temp.value) //
				.findFirst().orElse((byte) 0);
	}

	private byte fan(String source, String sensor) {
		return JSensors.get.components().cpus.stream() //
				.filter(cpu -> cpu.name.equals(source)) //
				.flatMap(cpu -> cpu.sensors.fans.stream()) //
				.filter(fan -> fan.name.equals(sensor)) //
				.map(fan -> (byte) (double) (fan.value / 50)) //
				.findFirst().orElse((byte) 0);
	}

	private byte sysLoad() {
		return (byte) (ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage() * 10);
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
