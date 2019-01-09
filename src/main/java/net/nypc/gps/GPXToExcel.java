package net.nypc.gps;

import java.io.File;
import java.io.IOException;

import net.nypc.gps.bo.GPX;
import net.nypc.gps.service.GPXService;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

public class GPXToExcel {
	
	private String inputFile;
	private String outputFile;
	private boolean debug;

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public GPXToExcel() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		GPXToExcel app = new GPXToExcel();
		app.getArgs(args);
		app.run();
	}
	
	public void getArgs(String[] args) {
		String appName = this.getClass().getSimpleName();
		Options options = new Options();
		options.addOption(new Option("i", "inputFile", true, "Input file"));
		options.addOption(new Option("o", "outputFile", true, "Output file"));
		options.addOption(new Option("D","debug", false, "turn on debug output"));
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		try {
			CommandLine cmd = parser.parse( options, args);
			if (cmd.hasOption("inputFile")) {
				this.setInputFile(cmd.getOptionValue("inputFile"));
			} else { 
				formatter.printHelp(appName, options );
				System.exit(0);
			}
			
			if (cmd.hasOption("outputFile")) {
				this.setOutputFile(cmd.getOptionValue("outputFile"));
			} else { 
				formatter.printHelp(appName, options );
				System.exit(0);
			}
			 
			if (cmd.hasOption("debug")) {
				this.setDebug(true);
			} else {
				this.setDebug(false);
			}
		} catch (ParseException e) {
			System.out.println("Could not parse arguments");
			formatter.printHelp(appName, options );
			System.exit(0);
		}
	}
	
	public void run() {
		System.out.println("GPXToExcel");
GPXService gpxService = new GPXService();
		
		String gpxXml = new String();
		try {
			gpxXml = FileUtils.readFileToString(new File(this.inputFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GPX gpx = gpxService.xmlToGpx(gpxXml);
		
	}

}
