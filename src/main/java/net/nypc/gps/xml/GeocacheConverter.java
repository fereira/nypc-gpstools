package net.nypc.gps.xml;

import java.util.ArrayList;
import java.util.List;

import net.nypc.gps.bo.Geocache;

import org.apache.commons.lang3.StringUtils;
 
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
 
public class GeocacheConverter implements Converter {

        public boolean canConvert(Class clazz) {
        	
                return clazz.equals(Geocache.class);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer,
                        MarshallingContext context) {
                Geocache geocache = (Geocache) value;
                writer.startNode("Geocache"); 
                writer.startNode("journal");
                writer.endNode(); // journal
                writer.endNode();
        }

        public Object unmarshal(HierarchicalStreamReader reader,
                        UnmarshallingContext context) {
                Geocache geocache = new Geocache();
                reader.moveDown(); // move down into Geocache element
                reader.moveDown(); // move done into Journal element
                
                reader.moveDown();
                //geocache.setPublisher(reader.getValue()); // publisherName
                reader.moveUp();
                
                reader.moveDown();
                //geocache.setJournalTitle(reader.getValue()); // Journal title
                reader.moveUp();
                
                reader.moveDown();
                //geocache.setIssn(reader.getValue()); // Issn
                reader.moveUp();
                
                reader.moveDown(); reader.getValue(); reader.moveUp(); // skip eissn
                
                reader.moveDown();
                //geocache.setVolume(reader.getValue()); // Volume
                reader.moveUp();
                
                reader.moveDown();
                //geocache.setIssueNumber(reader.getValue()); // Issue
                reader.moveUp();
                
                reader.moveDown(); reader.getValue(); reader.moveUp(); // skip issue Title
                
                reader.moveDown(); // move into pubDate element
                reader.moveDown(); // move into pubDate/Year
                //geocache.setYear(reader.getValue()); // Year
                reader.moveUp();
                reader.moveDown(); reader.getValue(); reader.moveUp(); // skip pubDate/Month
                reader.moveUp();
                
                reader.moveUp(); // move up from Journal element
                                
                reader.moveDown();
                //geocache.setTitle(reader.getValue());
                reader.moveUp();
                
                
                String firstPage = new String();
                String lastPage = new String();
                reader.moveDown();
                firstPage = reader.getValue(); // firstPage
                reader.moveUp();
                reader.moveDown();
                lastPage = reader.getValue(); // lastPage
                reader.moveUp();
                //geocache.setPages(firstPage +"-"+ lastPage);
                //geocache.setFirstPage(firstPage);
                
                reader.moveDown();
                //geocache.setLanguage(reader.getValue()); // language
                reader.moveUp();
                
                
 
                
                //System.out.println("author: "+ geocache.getAuthor());
                reader.moveDown(); 
                String fulltext = reader.getValue();
                reader.moveUp();
                
                reader.moveDown();  reader.moveUp(); // skip publication Type
                reader.moveDown();  reader.moveUp(); // skip geocacheIDList
                
                reader.moveDown();
                //geocache.setGeocacheAbstract(reader.getValue()); // abstract
                reader.moveUp();
                
                return geocache;
        }

}
