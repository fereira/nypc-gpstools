package net.nypc.gps;

import org.apache.commons.lang3.StringUtils;

public class Antipode {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
       Antipode antipode = new Antipode();
       // orig:  S 26 23.344 W 052 07.975
       // dd: -26.38907 -52.13292
       // solution:   N 26 23.344 E 127 52.025
       String latitude = "N 19 40.882";
       String longitude = "W 090 47.007";
       antipode.calculate(latitude, longitude);
	}
	
	public void calculate(String latitude, String longitude) {
		String newLat = new String();
		String newLong = new String();
		String eastwest = new String();
		String antipode_eastwest = new String();
		if (latitude.startsWith("S")) {
			newLat = StringUtils.replace(latitude, "S", "N");
		}
		if (latitude.startsWith("N")) {
			newLat = StringUtils.replace(latitude, "N", "S");
		}
		
		String ddm = new String();
		if (longitude.startsWith("E")) {
			eastwest = "W";
			 
			ddm = StringUtils.replace(longitude, "E", " ").trim();
		}
		if (longitude.startsWith("W")) {
			eastwest = "E";
			 
			ddm = StringUtils.replace(longitude, "W", " ").trim();
		}
		CoordConverter converter = new CoordConverter();
		double dd = converter.DDMToDecimal(eastwest, ddm);
		System.out.format("Decimal: %f\n", dd);
		 
		System.out.println("Original Coordinates");
		System.out.println(latitude +" "+longitude);
		//System.out.println("Longitude: "+ longitude);
		System.out.println("Antipode");
		//System.out.println("Latitude: "+newLat);
		 
		double newdd = 180 - dd;
		int MM = (int) newdd;
		//System.out.format("New Decimal: %f\n", newdd);
		//System.out.println("MM: "+ MM);
		double newMin = (newdd - MM) * 60;
		System.out.format("%s %s %d %6.3f\n", newLat, eastwest, MM, newMin);
	}

}
