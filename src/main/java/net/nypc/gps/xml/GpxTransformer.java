package net.nypc.gps.xml;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


public class GpxTransformer {

	public GpxTransformer() {
		// TODO Auto-generated constructor stub
	}
	
	public String transform(File f) throws Exception {
		 
		InputStream resourceAsStream = GpxTransformer.class.getResourceAsStream("geocache.xsl");
		StreamSource xsl = new StreamSource(resourceAsStream);
		Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer(xsl);
			StreamSource in = new StreamSource(f); 
			 
			StringWriter sout = new StringWriter();
	        PrintWriter out = new PrintWriter(sout);
			transformer.transform(in, new StreamResult(out));
			return sout.toString();
			
		} catch (TransformerException e) {
			throw e;
		}
		 	
	}

}
