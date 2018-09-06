package shionn.deamon.commands;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import shionn.deamon.arduino.ArduinoClient;
import shionn.deamon.arduino.Commands;

public class NewMailAlert implements Runnable {
	private Logger logger = LoggerFactory.getLogger(NewMailAlert.class);

	private ArduinoClient client;
	private Properties configuration;
	private int last = 0;

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
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			int count = folder.getUnreadMessageCount();
			logger.info("Nb mail : " + count + " : " + folder.getFullName() + " : "
					+ folder.getName() + " : " + folder.getMessageCount());
			if (count > last && last != 0) {
				client.push(Commands.stripFlash((byte) 0, (byte) 0, (byte) 255));
			} else {
				client.push(Commands.stripRainbow());
			}
			last = count;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
