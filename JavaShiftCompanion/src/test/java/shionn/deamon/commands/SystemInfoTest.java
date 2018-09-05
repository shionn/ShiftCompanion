package shionn.deamon.commands;

import org.junit.Test;
import org.jutils.jhardware.HardwareInfo;
import org.jutils.jhardware.model.MemoryInfo;

public class SystemInfoTest {

	@Test
	public void testJHardware() {
		MemoryInfo memoryInfo = HardwareInfo.getMemoryInfo();
		double x = Double.parseDouble(memoryInfo.getAvailableMemory().replaceAll("[^0-9]", ""))
				/ 1024;
		System.out.println(x / 1024);
		System.out.println((byte) (x * 10 / 1024));
	}
}
