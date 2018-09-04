package shionn.deamon.arduino;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jssc.SerialPort;
import jssc.SerialPortException;

public class ArduinoClient implements Runnable {

	private Logger logger = LoggerFactory.getLogger(ArduinoClient.class);

	private boolean initialized;
	private SerialPort port;
	private Queue<byte[]> commands;

	public ArduinoClient(String portname) {
		this.port = new SerialPort(portname);
		this.initialized = false;
		this.commands = new LinkedBlockingQueue<>();
	}

	@Override
	public void run() {
		try {
			if (!initialized) {
				port.openPort();
				logger.info("Init <" + port.getPortName() + ">");
				port.setParams(SerialPort.BAUDRATE_9600, //
						SerialPort.DATABITS_8, //
						SerialPort.STOPBITS_1, //
						SerialPort.PARITY_NONE);
				initialized = true;
			} else {
				byte[] cmd = commands.poll();
				if (cmd != null) {
					logger.info("Send " + Commands.from(cmd));
					port.writeBytes(cmd);
				}
			}
		} catch (SerialPortException e) {
			logger.error("Connexion avec l'arduino", e);
			try {
				port.closePort();
				initialized = false;
			} catch (SerialPortException ex) {
				logger.error("Fermeture du port", e);
			}
		}
	}

	public void push(byte[] cmd) {
		if (initialized) {
			commands.add(cmd);
		}
	}

}
