package net.nypc.gps;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CoordConverter {
	
	private String input; 
	private boolean debug;

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public CoordConverter() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		CoordConverter converter = new CoordConverter();
		double dd = converter.DDMToDecimal("S", "26 23.344");
		System.out.format("Decimal: %f%n", dd);
	}
	
	public void getArgs(String[] args) {
		String appName = this.getClass().getSimpleName();
		Options options = new Options();
		options.addOption(new Option("i", "input", true, "Input")); 
		options.addOption(new Option("D","debug", false, "turn on debug output"));
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		try {
			CommandLine cmd = parser.parse( options, args);
			if (cmd.hasOption("input")) {
				this.setInput(cmd.getOptionValue("input"));
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

	// Input a double latitude or longitude in the decimal format
	// e.g. -79.982195
	String decimalToDMS(double coord) {
		String output, degrees, minutes, seconds;

		// gets the modulus the coordinate divided by one (MOD1).
		// in other words gets all the numbers after the decimal point.
		// e.g. mod := -79.982195 % 1 == 0.982195
		//
		// next get the integer part of the coord. On other words the whole
		// number part.
		// e.g. intPart := -79

		double mod = coord % 1;
		int intPart = (int) coord;

		// set degrees to the value of intPart
		// e.g. degrees := "-79"

		degrees = String.valueOf(intPart);

		// next times the MOD1 of degrees by 60 so we can find the integer part
		// for minutes.
		// get the MOD1 of the new coord to find the numbers after the decimal
		// point.
		// e.g. coord := 0.982195 * 60 == 58.9317
		// mod := 58.9317 % 1 == 0.9317
		//
		// next get the value of the integer part of the coord.
		// e.g. intPart := 58

		coord = mod * 60;
		mod = coord % 1;
		intPart = (int) coord;
		if (intPart < 0) {
			// Convert number to positive if it's negative.
			intPart *= -1;
		}

		// set minutes to the value of intPart.
		// e.g. minutes = "58"
		minutes = String.valueOf(intPart);

		// do the same again for minutes
		// e.g. coord := 0.9317 * 60 == 55.902
		// e.g. intPart := 55
		coord = mod * 60;
		intPart = (int) coord;
		if (intPart < 0) {
			// Convert number to positive if it's negative.
			intPart *= -1;
		}

		// set seconds to the value of intPart.
		// e.g. seconds = "55"
		seconds = String.valueOf(intPart);

		// I used this format for android but you can change it
		// to return in whatever format you like
		// e.g. output = "-79/1,58/1,56/1"
		output = degrees + "/1," + minutes + "/1," + seconds + "/1";

		// Standard output of D°M′S″
		// output = degrees + "°" + minutes + "'" + seconds + "\"";

		return output;
	}

	/*
	 * Conversion DMS to decimal
	 * 
	 * Input: latitude or longitude in the DMS format ( example: W 79° 58'
	 * 55.903") Return: latitude or longitude in decimal format
	 * hemisphereOUmeridien => {W,E,S,N}
	 */
	public double DMSToDecimal(String hemisphereOUmeridien, double degres,
			double minutes, double secondes) {
		double LatOrLon = 0;
		double signe = 1.0;

		if ((hemisphereOUmeridien.equals("W"))
				|| (hemisphereOUmeridien.equals("S"))) {
			signe = -1.0;
		}
		LatOrLon = signe
				* (Math.floor(degres) + Math.floor(minutes) / 60.0 + secondes / 3600.0);

		return (LatOrLon);
	}
	
	/*Conversion from MinDec to Decimal Degree[edit]
			Given a MinDec (Degrees, Minutes, Decimal Minutes) coordinate such as 79°58.93172W, convert it to a number of decimal degrees using the following method:

			The integer number of degrees is the same (79)
			The decimal degrees is the decimal minutes divided by 60 (58.93172/60 = ~0.982195)
			Add the two together (79 + 0.982195= 79.982195)
			For coordinates ind the western (or southern) hemisphere, negate the result.
			The final result is -79.982195*/
	
	
	public double DDMToDecimal(String hemisphereOUmeridien, String ddm) {
		double latOrLon = 0;
		double signe = 1.0;
		if ((hemisphereOUmeridien.equals("W")) || (hemisphereOUmeridien.equals("S"))) {
			signe = -1.0;
		}
		String[] parts = ddm.split(" ");
		int degrees = Integer.parseInt(parts[0]);
		int M = (int) Double.parseDouble(parts[1]);
		double minutes = (double) Double.parseDouble(parts[1]);
		//System.out.println("degrees: "+ degrees);
		//System.out.format("minutes: %5.3f\n", minutes);
		 
		double newMin =  minutes / 60.0;
		 
		latOrLon = signe * (Math.floor(degrees) + newMin);
		return (latOrLon);
	}
	
	public String DecimalToDDM(Double decimal, String latOrLong) {
		String ddm = new String();
		String hemisphere = new String();
		if (latOrLong.equals("lat")) {
			if (decimal < 0.0) {
				hemisphere = "S";
			} else {
				hemisphere = "N";
			}
		} else if (latOrLong.equals("long")) {
			if (decimal < 0.0) {
				hemisphere = "W";
			} else {
				hemisphere = "E";
			}
		}
		int iDegrees = (int) Math.round(decimal);
		int minutes = (int) Math.rint(decimal)/60;
		ddm = hemisphere + " "+ String.valueOf(iDegrees)+"."+String.valueOf(minutes);
		return ddm;
		
	}
	
	

}
