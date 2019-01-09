package net.nypc.gps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.nypc.gps.bo.GPX;
import net.nypc.gps.bo.Waypoint;
import net.nypc.gps.util.XMLUtils;
import net.nypc.gps.service.GPXService;

public class Waypoints2Xml {

	public Waypoints2Xml() {
		
		
	}
	
	public static void main(String[] args) {
		Waypoints2Xml app = new Waypoints2Xml();
		 
		app.run();
	}
	
	public  void run() {
		GPXService gpxService = new GPXService();
		GPX gpx = new GPX();
		gpx.setName("Test GPX");
		gpx.setDesc("Test Desc");
		List<Waypoint> waypoints = new ArrayList<Waypoint>();
		
		Waypoint wpt1 = new Waypoint();
		wpt1.setLat("42.510767");
		wpt1.setLon("-76.19445");
		wpt1.setName("GCZ9HR");
		wpt1.setDesc("Mary, Mary Quite Contrary by Woodland Clan, Traditional Cache (1/1.5)");
		wpt1.setUrl("http://www.geocaching.com/seek/cache_details.aspx?guid=540c783b-ab0d-48ef-bab2-404e385242a2");
		wpt1.setUrlname("Mary, Mary, Quite Contrary");
		wpt1.setSym("Geocache Found");
		wpt1.setType("Geocache|Traditional Cache");
		waypoints.add(wpt1);
		
		Waypoint wpt2 = new Waypoint();
		wpt2.setLat("42.554117");
		wpt2.setLon("-76.14815");
		wpt2.setName("GCZFV1");
		wpt2.setDesc("The Dark Tower by gregger and wacky, Traditional Cache (1.5/1.5)");
		wpt2.setUrl("http://www.geocaching.com/seek/cache_details.aspx?guid=3ee734ab-afca-48b5-a9c9-b177f38a19b1");
		wpt2.setUrlname("The Dark Tower");
		wpt2.setSym("Geocache Found");
		wpt2.setType("Geocache|Traditional Cache");
		waypoints.add(wpt2);
		
		Waypoint wpt3 = new Waypoint();
		wpt3.setLat("37.885767");
		wpt3.setLon("-75.344633");
		wpt3.setName("GCZNVY");
		wpt3.setDesc("Assateague Island On The Move by climbstuff, Earthcache (2/2)");
		wpt3.setUrl("http://www.geocaching.com/seek/cache_details.aspx?guid=fcad3b5c-ab4b-4ee9-a592-4a28dc3047a0");
		wpt3.setUrlname("Assateague Island On The Move");
		wpt3.setSym("Geocache Found");
		wpt3.setType("Geocache|Earth Cache");
		waypoints.add(wpt3);
		
		gpx.setWaypoints(waypoints);
		
		String xml = gpxService.gpxToXml(gpx);
		XMLUtils.prettyPrint(xml);
		
		
		
		 
		
		
	}

}
