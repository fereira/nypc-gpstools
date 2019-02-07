package net.nypc.gps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

import net.nypc.gps.bo.GPX;
import net.nypc.gps.bo.Groundspeak;
import net.nypc.gps.bo.Waypoint;
import net.nypc.gps.bo.Attribute;
import net.nypc.gps.bo.Log;

import net.nypc.gps.service.GPXService;

public class GPX2Text {
	
	private String inputFile;
	private boolean debug;

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public GPX2Text() {
		
		
	}
	
	public static void main(String[] args) {
		GPX2Text app = new GPX2Text();
		app.getArgs(args);
		app.run();
	}
	
	public void getArgs(String[] args) {
		String appName = this.getClass().getSimpleName();
		Options options = new Options();
		options.addOption(new Option("i", "inputFile", true, "Input file"));
		options.addOption(new Option("D","debug", false, "turn on debug output"));
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		try {
			CommandLine cmd = parser.parse( options, args);
			if (cmd.hasOption("i") || cmd.hasOption("inputFile")) {
				this.setInputFile(cmd.getOptionValue("inputFile"));
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
	
	public  void run() {
		GPXService gpxService = new GPXService();
		
		String gpxXml = new String();
		try {
			gpxXml = FileUtils.readFileToString(new File(this.inputFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GPX gpx = gpxService.xmlToGpx(gpxXml);
		gpxService.printGPX(gpx); 
		
		
	}

}
