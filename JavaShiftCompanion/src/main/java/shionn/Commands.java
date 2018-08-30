package shionn;

import java.lang.management.ManagementFactory;
import java.util.Calendar;

import com.profesorfalken.jsensors.JSensors;

public class Commands {

	private static final byte SYS_STATE_CMD = (byte) 0xA0;
	private static final byte DISPLAY_MODE_CMD = (byte) 0xB0;
	private static final byte TIME_SET_CMD = (byte) 0xC0;

	public byte[] changeMode(int mode) {
		return new byte[] { DISPLAY_MODE_CMD, (byte) mode };
	}

	public byte[] time() {
		Calendar time = Calendar.getInstance();
		return new byte[] { TIME_SET_CMD, //
				(byte) (time.get(Calendar.YEAR) % 100), //
				(byte) (time.get(Calendar.MONTH) + 1), //
				(byte) time.get(Calendar.DAY_OF_MONTH), //
				(byte) time.get(Calendar.HOUR_OF_DAY), //
				(byte) time.get(Calendar.MINUTE), //
				(byte) time.get(Calendar.SECOND) };
	}

	public byte[] systemState() {
		return new byte[] { //
				SYS_STATE_CMD, //
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
