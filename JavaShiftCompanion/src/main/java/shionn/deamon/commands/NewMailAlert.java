package shionn.deamon.commands;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import shionn.deamon.arduino.ArduinoClient;
import shionn.deamon.arduino.Commands;

public class NewMailAlert implements Runnable {

	private ArduinoClient client;
	private Properties configuration;

	public NewMailAlert(ArduinoClient client) throws IOException {
		this.client = client;
		configuration = new Properties();
		configuration.load(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("configuration.properties"));
		System.getProperties().setProperty("mail.store.protocol", "imaps");
	}

	@Override
	public void run() {
		Session session = Session.getDefaultInstance(System.getProperties(), null);
		try {
			Store store = session.getStore("imaps");
			store.connect(configuration.getProperty("mail.host"),
					configuration.getProperty("mail.account"),
					configuration.getProperty("mail.pass"));
			Folder folder = store.getFolder("inbox");
			folder.open(Folder.READ_ONLY);
			if (folder.getUnreadMessageCount() > 0) {
				client.push(Commands.stripChenille((byte) 0, (byte) 0, (byte) 255));
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
