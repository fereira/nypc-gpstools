package net.nypc.gps.xstream;

import java.util.ArrayList;
import java.util.List;

import net.nypc.gps.bo.GPX;
import net.nypc.gps.bo.Waypoint; 
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter; 

public class GPXConverter implements Converter {

        public boolean canConvert(Class clazz) {
                return clazz.equals(GPX.class);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer,
                        MarshallingContext context) {
                GPX gpx = (GPX) value;
                writer.startNode("gpx"); 
                writer.startNode("wpt");
                writer.endNode(); // gpx/wpt
                writer.endNode(); // gpx
        }

        public Object unmarshal(HierarchicalStreamReader reader,
                        UnmarshallingContext context) {
                GPX gpx = new GPX();
                reader.moveDown(); // move down into name element
                gpx.setName(reader.getValue());
                reader.moveUp(); // 
                
                reader.moveDown(); // desc element
                gpx.setDesc(reader.getValue());
                reader.moveUp(); //
                
                reader.moveDown(); reader.getValue(); reader.moveUp(); // skip author
                reader.moveDown(); reader.getValue(); reader.moveUp(); // skip email
                reader.moveDown(); reader.getValue(); reader.moveUp(); // skip time
                reader.moveDown(); reader.getValue(); reader.moveUp(); // skip keywords
                reader.moveDown(); reader.getValue(); reader.moveUp(); // skip bounds
                // Handle waypoints
                List<Waypoint> waypoints = new ArrayList<Waypoint>();
                while (reader.hasMoreChildren()) {
                    reader.moveDown();
                    if ("wpt".equals(reader.getNodeName())) {
                       //System.out.println("processing wpt node");
                       Waypoint wpt = (Waypoint) context.convertAnother(gpx, Waypoint.class);
                       //System.out.println("WPT: "+ wpt.toString());
                       waypoints.add(wpt);
                    } else {
                    	System.out.println("NODENAME: "+ reader.getNodeName());
                    }
                    reader.moveUp();
                }
                gpx.setWaypoints(waypoints);
                return gpx;
        }

}
