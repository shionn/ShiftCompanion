package shionn.deamon.arduino;

import java.util.Arrays;

public enum Commands {

	SYS_STATE((byte) 0xA0), //
	DISPLAY_MODE((byte) 0xB0), //
	STRIP_MODE((byte) 0xB1), //
	TIME_SET((byte) 0xC0), //
	SERVER_STATUS((byte) 0xD0), //
	NETWORK((byte) 0xE0);

	private byte cmd;

	private Commands(byte cmd) {
		this.cmd = cmd;
	}

	public byte cmd() {
		return cmd;
	}

	public static byte[] lcdMode(byte mode) {
		return new byte[] { Commands.DISPLAY_MODE.cmd(), mode };
	}

	public static byte[] stripRainbow() {
		return new byte[] { Commands.STRIP_MODE.cmd(), (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
	}

	public static byte[] stripTheatre() {
		return new byte[] { Commands.STRIP_MODE.cmd(), (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
	}

	public static byte[] stripChenille(byte r, byte g, byte b) {
		return new byte[] { Commands.STRIP_MODE.cmd(), (byte) 0x02, r, g, b };
	}

	public static byte[] stripFlash(byte r, byte g, byte b) {
		return new byte[] { Commands.STRIP_MODE.cmd(), (byte) 0x03, r, g, b };
	}

	public static byte[] systemState(byte cpu, byte mb, byte load, byte fan1, byte fan2) {
		return new byte[] { Commands.SYS_STATE.cmd(), cpu, mb, load, fan1, fan2 };
	}

	public static byte[] serverStatus(byte server, boolean available) {
		return new byte[] {
				Commands.SERVER_STATUS.cmd(),
				server,
				(byte) (available ? 0x01 : 0x00) };
	}

	public static Commands from(byte[] cmd) {
		return from(cmd[0]);
	}

	public static Commands from(byte cmd) {
		return Arrays.stream(Commands.values()).filter(c -> c.cmd == cmd).findFirst().orElse(null);
	}


}
