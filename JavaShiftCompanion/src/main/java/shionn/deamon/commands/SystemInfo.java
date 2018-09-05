package shionn.deamon.commands;

import java.lang.management.ManagementFactory;

import org.jutils.jhardware.HardwareInfo;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;

import shionn.deamon.arduino.ArduinoClient;
import shionn.deamon.arduino.Commands;

public class SystemInfo implements Runnable {

	private static final int MEMORY_FACTOR = 10; // adapté à 16Go
	private static final int PUMP_LIMIT = 20;
	private static final int MB_LIMIT = 55;
	private static final int CPU_LIMIT = 45;
	private static final String PUMP_FAN_NAME = "Fan fan1";
	private static final String CHASSIS_FAN_NAME = "Fan fan2";
	private ArduinoClient client;

	private boolean alert = false;

	public SystemInfo(ArduinoClient client) {
		this.client = client;
	}

	@Override
	public void run() {
		Components components = JSensors.get.components();

		byte cpu = temperature(components, "ISA adapter", "Temp Package id 0");
		byte mb = temperature(components, "ISA adapter", "Temp temp4");
		byte pump = fan(components, "ISA adapter", PUMP_FAN_NAME);
		if (isOverheat(cpu, mb, pump) && !alert) {
			alert = true;
			client.push(Commands.stripFlash((byte) 0xFF, (byte) 0x00, (byte) 0x00));
		} else if (!isOverheat(cpu, mb, pump) && alert) {
			alert = false;
			client.push(Commands.stripRainbow());
		}
		client.push(Commands.systemState(cpu, //
				mb, //
				sysLoad(), //
				pump, //
				fan(components, "ISA adapter", CHASSIS_FAN_NAME), //
				memory()));
	}

	private byte memory() {
		return (byte) (Double.parseDouble(
				HardwareInfo.getMemoryInfo().getAvailableMemory().replaceAll("[^0-9]", ""))
				* MEMORY_FACTOR / 1024 / 1024);
	}

	private boolean isOverheat(byte cpu, byte mb, byte pump) {
		return cpu > CPU_LIMIT || mb > MB_LIMIT || pump < PUMP_LIMIT;
	}

	private byte temperature(Components components, String source, String sensor) {
		return components.cpus.stream() //
				.filter(cpu -> cpu.name.equals(source)) //
				.flatMap(cpu -> cpu.sensors.temperatures.stream()) //
				.filter(temp -> temp.name.equals(sensor)) //
				.map(temp -> (byte) (double) temp.value) //
				.findFirst().orElse((byte) 0);
	}

	private byte fan(Components components, String source, String sensor) {
		return components.cpus.stream() //
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
