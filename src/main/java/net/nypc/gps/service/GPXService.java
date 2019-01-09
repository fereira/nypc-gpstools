package net.nypc.gps.service;

import java.io.File;
import java.util.List;

import net.nypc.gps.bo.Attribute;
import net.nypc.gps.bo.GPX;
import net.nypc.gps.bo.Groundspeak;
import net.nypc.gps.bo.Log;
import net.nypc.gps.bo.Waypoint;
import net.nypc.gps.xstream.GPXConverter;

import org.apache.commons.io.FileUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.extended.NamedCollectionConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class GPXService {

	public GPXService() {
		// TODO Auto-generated constructor stub
	}
	
	public GPX xmlToGpx(String xml) {
		XStream xstream = new XStream(new DomDriver());
		xstream.registerConverter(new GPXConverter());
		
		xstream.registerLocalConverter(GPX.class, "waypoints", new NamedCollectionConverter(xstream.getMapper(),
	            "waypoint", Object.class));
		
		
		xstream.alias("gpx", GPX.class);
		xstream.alias("wpt", Waypoint.class);
		xstream.alias("groundspeak:cache", Groundspeak.class);
		xstream.aliasField("groundspeak:cache", Waypoint.class, "geocache");
		xstream.aliasField("groundspeak:name", Groundspeak.class, "name");
		xstream.aliasField("groundspeak:placed_by", Groundspeak.class, "placed_by");
		xstream.aliasField("groundspeak:owner", Groundspeak.class, "owner");
		
		
		xstream.addImplicitCollection(GPX.class, "waypoints");
		xstream.processAnnotations(GPX.class);
		xstream.processAnnotations(Waypoint.class);
		xstream.processAnnotations(Groundspeak.class);
		xstream.processAnnotations(Attribute.class);
		xstream.processAnnotations(Log.class); 
		
		GPX gpx = new GPX();
		try {
			gpx = (GPX) xstream.fromXML(xml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gpx;
		
	}
	
	public String gpxToXml(GPX gpx) {
		String xml = new String();
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("gpx", GPX.class);
		xstream.alias("wpt", Waypoint.class);
		xstream.addImplicitCollection(GPX.class, "waypoints");
		xstream.useAttributeFor(Waypoint.class, "lat");
		xstream.useAttributeFor(Waypoint.class, "lon");
		xml = xstream.toXML(gpx);
		return xml;
	}
	
	public void printGPX(GPX gpx) {
		System.out.println("name: "+ gpx.getName());
		System.out.println("desc: "+ gpx.getDesc());
		List<Waypoint> waypoints = gpx.getWaypoints();
		for (Waypoint wpt: waypoints) {
			System.out.println("wpt/name: "+wpt.getName());
			System.out.println("wpt/desc: "+wpt.getDesc());
			System.out.println("wpt/lat: "+wpt.getLat());
			System.out.println("wpt/lon: "+wpt.getLon());
			System.out.println("wpt/url: "+wpt.getUrl());
			System.out.println("wpt/urlname: "+wpt.getUrlname());
			System.out.println("wpt/sym: "+wpt.getSym());
			System.out.println("wpt/type: "+wpt.getType());
			Groundspeak groundspeak = wpt.getGroundspeak();
			System.out.println("wpt/groundspeak:name : "+groundspeak.getName());
			System.out.println("wpt/groundspeak:placed_by : "+groundspeak.getPlaced_by());
			System.out.println();		
		}
		
		System.out.println();
	}

}
