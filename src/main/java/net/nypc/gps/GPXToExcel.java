package net.nypc.gps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.nypc.gps.bo.GPX;
import net.nypc.gps.service.GPXService;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GPXToExcel {
	
	private String inputFile;
	private String outputFile;
	private boolean debug;
	
	Workbook workbook = null;
	Sheet sheet = null;
	
	private final int GCCODE = 1;
	private final int GCNAME = 2;
	private final int LATITUDE = 3;
	private final int LONGITUDE = 4;
	private final int CACHETYPE = 5;
	private final int SIZE = 6;
	private final int DIFFICULTY = 7;
	private final int TERRAIN = 8;
	private final int SHORTDESC = 9;
	private final int LONGDESC = 10;
	private final int PLACEDBY = 11;
	private final int OWNER = 12;
	private final int COUNTRY = 13;
	private final int STATE = 14;
	
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
		options.addOption(new Option("o", "outputFile", true, "Output file"));
		options.addOption(new Option("D","debug", false, "turn on debug output"));
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		try {
			CommandLine cmd = parser.parse( options, args);
			if (cmd.hasOption("inputFile")) {
				this.setInputFile(cmd.getOptionValue("inputFile"));
			} else { 
				formatter.printHelp(appName, options );
				System.exit(0);
			}
			
			if (cmd.hasOption("outputFile")) {
				this.setOutputFile(cmd.getOptionValue("outputFile"));
			} else { 
				formatter.printHelp(appName, options );
				System.exit(0);
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
        
        //FileOutputStream outputStream = new FileOutputStream(new File(this.getOutputFile()));
       
		if (this.getOutputFile().endsWith(".xlsx")) {
			workbook = new XSSFWorkbook();
		} else if (this.getOutputFile().endsWith(".xls")) {
			workbook = new HSSFWorkbook();
		} else {
			System.err.println("Use a .xlsx or .xls file extension for the output file.");
			System.exit(1);
		}
		
		CreationHelper createHelper = workbook.getCreationHelper();
		
		
		String gpxXml = new String();
		try {
			gpxXml = FileUtils.readFileToString(new File(this.inputFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GPX gpx = gpxService.xmlToGpx(gpxXml);
		
		// Create a Sheet
        sheet = workbook.createSheet("Geocaches");
		writeHeader(gpx);
		// writeRows(gpx);
		// Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

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

}
