package net.nypc.gps.bo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("groundspeak:log")
public class Log {
	
	@XStreamAsAttribute
	private String id;
	
	@XStreamAlias("groundspeak:date")
	private String date;
	
	@XStreamAlias("groundspeak:type")
	private String type;
	
	@XStreamAlias("groundspeak:finder")
	private String finder;
	
	@XStreamAlias("groundspeak:text")
	private String text;
	
	@XStreamAlias("groundspeak:log_wpt")
	private String wpt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}
    
	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFinder() {
		return finder;
	}

	public void setFinder(String finder) {
		this.finder = finder;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getWpt() {
		return wpt;
	}

	public void setWpt(String wpt) {
		this.wpt = wpt;
	}

	public Log() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
	    return ToStringBuilder.reflectionToString(this);
	}

}
