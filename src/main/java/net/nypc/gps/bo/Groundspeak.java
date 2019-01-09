package net.nypc.gps.bo;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("groundspeak:cache")
public class Groundspeak {
	
	@XStreamAlias("groundspeak:name")
	private String name;
	
	@XStreamAlias("groundspeak:placed_by")
	private String placed_by;
	
	@XStreamAlias("groundspeak:owner")
	private String owner;
	
	@XStreamAlias("groundspeak:type")
	private String type;
	
	@XStreamAlias("groundspeak:container")
	private String container;
	
	@XStreamAlias("groundspeak:attributes")
	private List<Attribute> attributes;
	
	@XStreamAlias("groundspeak:difficulty")
	private String difficulty;
	
	@XStreamAlias("groundspeak:terrain")
	private String terrain;
	
	@XStreamAlias("groundspeak:country")
	private String country;
	
	@XStreamAlias("groundspeak:state")
	private String state;
	
	@XStreamAlias("groundspeak:short_description")
	private String shortDescription;
	
	@XStreamAlias("groundspeak:long_description")
	private String longDescription;
	
	@XStreamAlias("groundspeak:encoded_hints")
	private String hint;
	
	@XStreamAlias("groundspeak:logs")
	private List<Log> logs;
	
	@XStreamAlias("groundspeak:travelbugs")
	private String travelbugs;
	
	 

	public Groundspeak() {
		// TODO Auto-generated constructor stub
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlaced_by() {
		return placed_by;
	}

	public void setPlaced_by(String placed_by) {
		this.placed_by = placed_by;
	}

	@Override
	public String toString() {
	    return ToStringBuilder.reflectionToString(this);
	}

}
