package shionn.deamon.commands;

import org.junit.Test;
import org.jutils.jhardware.HardwareInfo;
import org.jutils.jhardware.model.MemoryInfo;

public class SystemInfoTest {

	@Test
	public void testJHardware() {
		MemoryInfo memoryInfo = HardwareInfo.getMemoryInfo();
		System.out.println(Double.parseDouble(memoryInfo.getAvailableMemory()) / 1024 / 1024);
	}
}
