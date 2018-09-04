package shionn.deamon.arduino;

public enum Commands {

	SYS_STATE((byte) 0xA0), //
	DISPLAY_MODE((byte) 0xB0), //
	STRIP_MODE((byte) 0xB1), //
	TIME_SET((byte) 0xC0), //
	NETWORK((byte) 0xD0);

	private byte cmd;

	private Commands(byte cmd) {
		this.cmd = cmd;
	}

	public byte cmd() {
		return cmd;
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


}
