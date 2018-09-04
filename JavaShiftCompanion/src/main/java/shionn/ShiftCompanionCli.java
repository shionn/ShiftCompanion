package shionn;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.profesorfalken.jsensors.JSensors;

import jssc.SerialPortList;
import shionn.deamon.ShiftCompanionDeamon;

public class ShiftCompanionCli {

	private Options options;

	public ShiftCompanionCli() {
		options = new Options();
		options.addOption(
				Option.builder("d").longOpt("deamon").desc("Mode deamon (need port)").required(false).build());
		options.addOption(Option.builder("p").longOpt("port").required(false).hasArg(true)
				.desc("port serie vers l'arduino").build());
		options.addOption(Option.builder("s").longOpt("scan").desc("Scan de Jsensor et Jssc").required(false).build());

	}

	public void parse(String[] args) throws IOException {
		try {
			DefaultParser parser = new DefaultParser();
			CommandLine commandLine = parser.parse(options, args);
			if (commandLine.hasOption("d") && commandLine.hasOption("p")) {
				new ShiftCompanionDeamon(commandLine.getOptionValue("p")).start();
			} else if (commandLine.hasOption("s")) {
				for (String port : SerialPortList.getPortNames()) {
					System.out.println("Port : " + port);
				}
				JSensors.main(new String[0]);
			} else {
				new HelpFormatter().printHelp("shiftcompanion", options);
			}
		} catch (ParseException e) {
			new HelpFormatter().printHelp("shiftcompanion", options);
		}
	}

}
