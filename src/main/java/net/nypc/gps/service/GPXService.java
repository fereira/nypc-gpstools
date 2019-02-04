package net.nypc.gps.service;

import java.io.File;
import java.util.Collection;
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
	
	private XStream xstream;
	Class<?>[] classes = new Class[] { GPXService.class  };

	public GPXService() {
		this.xstream = new XStream(new DomDriver());
		XStream.setupDefaultSecurity(xstream);
		xstream.allowTypesByWildcard(new String[] {"net.nypc.gps.**"});
		xstream.allowTypesByRegExp(new String[] { ".*" });
	}
	
	public GPX xmlToGpx(String xml) {
		
		this.xstream.registerConverter(new GPXConverter());
		this.xstream.registerLocalConverter(GPX.class, "waypoints", new NamedCollectionConverter(this.xstream.getMapper(),
	            "waypoint", Object.class));
		
		
		this.xstream.alias("gpx", GPX.class);
		this.xstream.alias("wpt", Waypoint.class);
		this.xstream.alias("groundspeak:cache", Groundspeak.class);
		this.xstream.aliasField("groundspeak:cache", Waypoint.class, "geocache");
		this.xstream.aliasField("groundspeak:name", Groundspeak.class, "name");
		this.xstream.aliasField("groundspeak:placed_by", Groundspeak.class, "placed_by");
		this.xstream.aliasField("groundspeak:owner", Groundspeak.class, "owner");
		
		
		this.xstream.addImplicitCollection(GPX.class, "waypoints");
		this.xstream.processAnnotations(GPX.class);
		this.xstream.processAnnotations(Waypoint.class);
		this.xstream.processAnnotations(Groundspeak.class);
		this.xstream.processAnnotations(Attribute.class);
		this.xstream.processAnnotations(Log.class); 
		
		GPX gpx = new GPX();
		try {
			gpx = (GPX) this.xstream.fromXML(xml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gpx;
		
	}
	
	public String gpxToXml(GPX gpx) {
		String xml = new String(); 
		this.xstream.alias("gpx", GPX.class);
		this.xstream.alias("wpt", Waypoint.class);
		this.xstream.addImplicitCollection(GPX.class, "waypoints");
		this.xstream.useAttributeFor(Waypoint.class, "lat");
		this.xstream.useAttributeFor(Waypoint.class, "lon");
		xml = this.xstream.toXML(gpx);
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
