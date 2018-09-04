package shionn.deamon.commands;

import java.lang.management.ManagementFactory;

import com.profesorfalken.jsensors.JSensors;

import shionn.deamon.arduino.ArduinoClient;
import shionn.deamon.arduino.Commands;

public class SystemInfo implements Runnable {

	private static final int PUMP_LIMIT = 500;
	private static final int MB_LIMIT = 55;
	private static final int CPU_LIMIT = 45;
	private static final String PUMP_FAN_NAME = "Fan fan1";
	private static final String CHASSIS_FAN_NAME = "Fan fan2";
	private ArduinoClient client;

	public SystemInfo(ArduinoClient client) {
		this.client = client;
	}

	@Override
	public void run() {
		byte cpu = temperature("ISA adapter", "Temp Package id 0");
		byte mb = temperature("ISA adapter", "Temp temp4");
		byte pump = fan("ISA adapter", PUMP_FAN_NAME);
		if (cpu > CPU_LIMIT || mb > MB_LIMIT || pump < PUMP_LIMIT) {
			client.push(Commands.stripFlash((byte) 0xFF, (byte) 0x00, (byte) 0x00));
		} else {
			client.push(Commands.stripRainbow());
		}
		client.push(systemState(cpu, mb, pump));
	}

	private byte[] systemState(byte cpu, byte mb, byte pump) {
		return Commands.systemState(cpu, //
				mb, //
				sysLoad(), //
				pump, //
				fan("ISA adapter", CHASSIS_FAN_NAME));
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

}
