package net.nypc.gps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import net.nypc.gps.bo.GPX;
import net.nypc.gps.bo.Groundspeak;
import net.nypc.gps.bo.Waypoint;
import net.nypc.gps.bo.Attribute;
import net.nypc.gps.bo.Log;

import net.nypc.gps.service.GPXService;

public class GPX2Text {

	public GPX2Text() {
		
		
	}
	
	public static void main(String[] args) {
		GPX2Text app = new GPX2Text();
		if (args.length != 1) {
			System.err.println("You must provide a gpx file name");
			System.exit(-1);
		}
		app.run(args[0]);
	}
	
	public  void run(String filename) {
		GPXService gpxService = new GPXService();
		
		String gpxXml = new String();
		try {
			gpxXml = FileUtils.readFileToString(new File(filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GPX gpx = gpxService.xmlToGpx(gpxXml);
		gpxService.printGPX(gpx); 
		
		
	}

}
