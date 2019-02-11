package net.nypc.gps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import net.nypc.gps.bo.GPX;
import net.nypc.gps.bo.Groundspeak;
import net.nypc.gps.bo.Waypoint;
import net.nypc.gps.service.GPXService;
import net.nypc.gps.util.ConvertUtil;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont; 
import org.apache.poi.xssf.usermodel.XSSFHyperlink;

public class GPXToExcel {
	
	private String inputFile;
	private String outputFile;
	private String format;
	private boolean debug;
	
	Workbook workbook = null;
	Sheet sheet = null;
	
	CellStyle wrapped_cs;
    XSSFCellStyle hlinkstyle; 
    CreationHelper createHelper;
	
	private final int GCCODE = 0;
	private final int GCNAME = 1;
	private final int LATITUDE = 2;
	private final int LONGITUDE = 3;
	private final int CACHETYPE = 4;
	private final int SIZE = 5;
	private final int DIFFICULTY = 6;
	private final int TERRAIN = 7;
	private final int SHORTDESC = 8;
	private final int LONGDESC = 9;
	private final int PLACEDBY = 10;
	private final int OWNER = 11;
	private final int COUNTRY = 12;
	private final int STATE = 13;
	
	private final int MAXCELLSIZE = 32767;
	
	private static String[] columns = {"GC Code", "Name", "Latitude", "Longitude", "Type", "Size"
		, "Difficulty", "Terrain", "Short Description", "Long Description", "Placed By", "Owner"
		, "Country", "State"};
	

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public GPXToExcel() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		GPXToExcel app = new GPXToExcel();
		app.getArgs(args);
		app.run();
	}
	
	public void getArgs(String[] args) {
		String appName = this.getClass().getSimpleName();
		Options options = new Options();
		options.addOption(new Option("i", "inputFile", true, "Input file"));
		options.addOption(new Option("f", "format", true, "Output format (xlsx, xls, or csv"));
		options.addOption(new Option("D","debug", false, "turn on debug output"));
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		try {
			CommandLine cmd = parser.parse( options, args);
			if (cmd.hasOption("i") || cmd.hasOption("inputFile")) {
				this.setInputFile(cmd.getOptionValue("inputFile"));
			} else { 
				formatter.printHelp(appName, options );
				System.exit(0);
			}
			
			if (cmd.hasOption("format")) {
				this.setFormat(cmd.getOptionValue("format"));
			} else { 
				this.setFormat("xlsx");
			}
			 
			if (cmd.hasOption("debug")) {
				this.setDebug(true);
			} else {
				this.setDebug(false);
			}
		} catch (ParseException e) {
			System.out.println("Could not parse arguments");
			formatter.printHelp(appName, options );
			System.exit(0);
		}
	}
	
	public void run() {
		System.out.println("GPXToExcel");
        GPXService gpxService = new GPXService();  
		
		String gpxXml = new String();
		
		String currentDir = System.getProperty("user.dir");
		 
		String fullPath = currentDir +"/"+ this.getInputFile(); 
		Path path = Paths.get(fullPath);
		File inFile = path.toFile();
		try {
			//gpxXml = FileUtils.readFileToString(inFile);
		    gpxXml = new String (Files.readAllBytes(inFile.toPath()),Charset.forName("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.err.println("Could not open inputFile: "+ fullPath);
			System.exit(1);
		}
		
		initWorkbook();
		GPX gpx = gpxService.xmlToGpx(gpxXml);
		
		// Create a Sheet
        sheet = workbook.createSheet("Geocaches");
		writeHeader(gpx);
		writeRows(gpx); 
        
		//  set column widths 
		
        this.sheet.autoSizeColumn( this.GCCODE);
        this.sheet.autoSizeColumn( this.GCNAME);
        this.sheet.autoSizeColumn( this.LATITUDE);
        this.sheet.autoSizeColumn( this.LONGITUDE);
        this.sheet.autoSizeColumn( this.CACHETYPE);
        this.sheet.autoSizeColumn( this.SIZE );
        this.sheet.autoSizeColumn( this.DIFFICULTY);
        this.sheet.autoSizeColumn( this.TERRAIN);
        this.sheet.setColumnWidth( this.SHORTDESC, 20000 );
        this.sheet.setColumnWidth( this.LONGDESC, 20000 );
        this.sheet.autoSizeColumn( this.PLACEDBY );
        this.sheet.autoSizeColumn( this.OWNER  );
        this.sheet.autoSizeColumn( this.COUNTRY );
        this.sheet.autoSizeColumn( this.STATE );

        // Write the output to a file
        FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(this.getOutputFile());
			workbook.write(fileOut);
	        fileOut.close();

	        // Closing the workbook
	        workbook.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	public void initWorkbook() {
		if (this.getFormat().equals("xlsx")) {
			workbook = new XSSFWorkbook();
			this.outputFile = "geocaches.xlsx";
		} else if (this.getFormat().equals("xls")) {
			workbook = new HSSFWorkbook();
			this.outputFile = "geocaches.xls";
		} else if (this.getFormat().equals("csv")) {
			workbook = new HSSFWorkbook();
			this.outputFile = "geocaches.csv";
		} else {
			System.err.println("Only valid formats are xlsx, xls, or csv.");
			System.exit(1);
		}
		
        this.createHelper = this.workbook.getCreationHelper();
		
	    this.hlinkstyle = (XSSFCellStyle) this.workbook.createCellStyle();
	    XSSFFont hlinkfont = (XSSFFont) this.workbook.createFont();
	    hlinkfont.setUnderline(XSSFFont.U_SINGLE);
	    hlinkfont.setColor(HSSFColor.BLUE.index);
	    hlinkstyle.setFont(hlinkfont);
	    
	    this.wrapped_cs = this.workbook.createCellStyle();
		this.wrapped_cs.setWrapText(true);
		
		
	}
	
	public void writeHeader(GPX gpx) {
		
		// Create a Font for styling header cells
        Font headerFont = this.workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = this.workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        
        // Create a Row
        Row headerRow = this.sheet.createRow(0);

        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
        
		
	}
	
	public void writeRows(GPX gpx) {
		ConvertUtil converter = new ConvertUtil();
	 
		List<Waypoint> waypoints = gpx.getWaypoints();
		int rowNum = 1;
		String shortDesc = new String();
		String longDesc = new String();
		for (Waypoint waypoint: waypoints) {
			Groundspeak geocache = waypoint.getGroundspeak();
			Row row = sheet.createRow(rowNum++);
			
			Cell cell = row.createCell(GCCODE);
			cell.setCellValue(waypoint.getName()); 
			XSSFHyperlink link = (XSSFHyperlink) this.createHelper.createHyperlink(HyperlinkType.URL); 
			link.setAddress(waypoint.getUrl());
			cell.setHyperlink((XSSFHyperlink) link);
		    cell.setCellStyle(hlinkstyle); 
		    
	        row.createCell(GCNAME).setCellValue(geocache.getName());
	        
	        String lat = converter.DecimalToDDM(Double.valueOf(waypoint.getLat()), "lat");
	        String lon = converter.DecimalToDDM(Double.valueOf(waypoint.getLon()), "long");
	        row.createCell(LATITUDE).setCellValue(lat);
	        row.createCell(LONGITUDE).setCellValue(lon);
	        
	        row.createCell(CACHETYPE).setCellValue(geocache.getType());
	        row.createCell(SIZE).setCellValue(geocache.getContainer());
	        row.createCell(DIFFICULTY).setCellValue(geocache.getDifficulty());
	        row.createCell(TERRAIN).setCellValue(geocache.getTerrain());
	        shortDesc = geocache.getShortDescription();
	        longDesc = StringUtils.abbreviate(geocache.getLongDescription(), MAXCELLSIZE);
	        
	        cell = row.createCell(SHORTDESC); 
			cell.setCellValue(shortDesc);
			cell.setCellStyle(this.wrapped_cs);
			
			cell = row.createCell(LONGDESC); 
			cell.setCellValue(longDesc);
			cell.setCellStyle(this.wrapped_cs); 
	        row.createCell(PLACEDBY).setCellValue(geocache.getPlaced_by());
	        row.createCell(OWNER).setCellValue(geocache.getOwner());
	        row.createCell(COUNTRY).setCellValue(geocache.getCountry());
	        row.createCell(STATE).setCellValue(geocache.getState());
		}
	}

}
