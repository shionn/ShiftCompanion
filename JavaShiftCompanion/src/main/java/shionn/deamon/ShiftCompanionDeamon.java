package shionn.deamon;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import shionn.deamon.arduino.ArduinoClient;
import shionn.deamon.commands.FanSpeed;
import shionn.deamon.commands.LcdChangeMode;
import shionn.deamon.commands.NewMailAlert;
import shionn.deamon.commands.ServerAvailable;
import shionn.deamon.commands.SystemInfo;
import shionn.deamon.commands.TimeSynchro;

public class ShiftCompanionDeamon implements Remote {

	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private ArduinoClient client;

	public ShiftCompanionDeamon(String port) {
		this.client = new ArduinoClient(port);
	}

	public void start() throws IOException {
		Registry registry = LocateRegistry.createRegistry(10000);
		registry.rebind("rmi://127.0.0.1/ShiftCompanion", this);
		Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook(this)));
		executor.scheduleAtFixedRate(client, 0, 1, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(new LcdChangeMode(client), 1, 5, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(new TimeSynchro(client), 2, 180, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(new SystemInfo(client), 3, 15, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(new ServerAvailable(client), 4, 60, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(new NewMailAlert(client), 5, 30, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(new FanSpeed(client), 6, 60, TimeUnit.SECONDS);
		// executor.scheduleAtFixedRate(new NetworkScan(client), 4, 60, TimeUnit.SECONDS);
	}

	public void shutdown() {
		this.executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					Registry registry = LocateRegistry.getRegistry(10000);
					registry.unbind("rmi://localhost/ShiftCompanion");
				} catch (IOException | NotBoundException e) {
					e.printStackTrace();
				}
			}
		});
		this.executor.shutdown();
	}

}
