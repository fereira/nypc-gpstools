package net.nypc.gps;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import net.nypc.gps.xml.GpxTransformer;

public class ConvertGPX {
	
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

	public ConvertGPX() {
		
		
	}
	
	public static void main(String[] args) {
		ConvertGPX app = new ConvertGPX();
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
		
		GpxTransformer transformer = new GpxTransformer();
		String gpxXml = new String();
		try {
			gpxXml = transformer.transform(new File(this.getInputFile()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(gpxXml);
		
		
	}

}
