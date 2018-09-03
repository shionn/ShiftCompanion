package shionn.deamon.commands;

import java.lang.management.ManagementFactory;

import com.profesorfalken.jsensors.JSensors;

import shionn.deamon.arduino.ArduinoClient;
import shionn.deamon.arduino.Commands;

public class SystemInfo implements Runnable {

	private ArduinoClient client;

	public SystemInfo(ArduinoClient client) {
		this.client = client;
	}

	@Override
	public void run() {
		client.push(systemState());
	}

	private byte[] systemState() {
		return new byte[] { //
				Commands.SYS_STATE.cmd(), //
				temperature("ISA adapter", "Temp Package id 0"), //
				temperature("ISA adapter", "Temp temp4"), //
				sysLoad(), //
				fan("ISA adapter", "Fan fan1"), //
				fan("ISA adapter", "Fan fan2") };
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
