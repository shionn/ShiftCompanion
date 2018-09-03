package shionn.deamon.arduino;

public enum Commands {

	SYS_STATE((byte) 0xA0), DISPLAY_MODE((byte) 0xB0), TIME_SET((byte) 0xC0);

	private byte cmd;

	private Commands(byte cmd) {
		this.cmd = cmd;
	}

	public byte cmd() {
		return cmd;
	}


}
