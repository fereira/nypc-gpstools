package net.nypc.gps.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.nypc.gps.bo.Waypoint;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.nypc.gps.util.XMLUtils;

public class GPXParser {

	public GPXParser() {
		// TODO Auto-generated constructor stub
	}
	
	public List<Waypoint> parse(File f) {
		List<Waypoint> waypoints = new ArrayList<Waypoint>();
		String xml;
		Document doc = null;
		
		try {
			xml = FileUtils.readFileToString(f, "UTF-8");
			doc = XMLUtils.parse(xml);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		xPath.setNamespaceContext(new NamespaceResolver(doc));
		 
		
		try {
			Object result = getExpr(xPath, "/*").evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			Node gpxNode= nodes.item(0);
				
			System.out.println("node name: "+ gpxNode.getLocalName());
				
			NodeList children = gpxNode.getChildNodes(); 
			for (int j=0; j < children.getLength(); j++) {
			    System.out.println("childnode "+j+ " "+ children.item(j).getNodeName());	
			}
			NodeList wptNodes = doc.getElementsByTagName("wpt"); 
			System.out.println("wpt size: "+ wptNodes.getLength());
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return waypoints;
		
	}
	
	protected XPathExpression getExpr(XPath xpath, String s) throws XPathExpressionException {
	   return xpath.compile(s);	
	}
	
	protected String getStringWithXpath(Object obj, XPath xPath, String expr) {
		
    	try {
    		
			return (String) xPath.compile(expr).evaluate(obj, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			System.err.println("Could not evaluate xpath: "+ expr);
			//e.printStackTrace();
			return "";
		}
    }
    
    protected Node getNodeWithXpath(Object obj, XPath xPath, String expr) {
    	try {
			return (Node) xPath.compile(expr).evaluate(obj, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			System.err.println("Could not evaluate xpath: "+ expr);
			//e.printStackTrace();
			return null;
		}
    }
    
    protected NodeList getNodesetWithXpath(Object obj, XPath xPath, String expr) {
    	try {
			return (NodeList) xPath.compile(expr).evaluate(obj, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			System.err.println("Could not evaluate xpath: "+ expr);
			//e.printStackTrace();
			return null;
		}
    }
    
    /**
	 * @param args
	 */
	public static void main(String[] args) {
		 GPXParser app = new GPXParser();
		 app.parse(new File("/usr/local/src/javadev/data/MyFinds.gpx"));
	}

}
