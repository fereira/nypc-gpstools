package net.nypc.gps;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 

public class ReadExcelPOI {
	 String inputFile = "/cul/src/javadev/data/great_orgs.xlsx";

	public ReadExcelPOI() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		ReadExcelPOI app = new ReadExcelPOI();
		app.run();

	}
	
	public void run() {
		try { 
             
            FileInputStream inputStream = new FileInputStream(new File(this.inputFile));
            Workbook workbook = null;
            if (this.inputFile.endsWith(".xlsx")) {
               workbook = new XSSFWorkbook(inputStream);
            } else if (this.inputFile.endsWith(".xls")) {
               workbook = new HSSFWorkbook(inputStream);
            } else {
                System.err.println("The specified file is not Excel file");
                System.exit(1);
            }
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            
            boolean isfirstRow = true;
            Row firstRow = firstSheet.getRow(0);
            int lastCell = firstRow.getLastCellNum();
            System.out.println("lastCellNum: "+ lastCell);
            
            Iterator<Cell> cellIterator0 = firstRow.cellIterator();
            while (cellIterator0.hasNext()) {
            	Cell cell = cellIterator0.next();
            	Object cellValue = getCellValue(cell);
            	
            }
            // skip first row
            if (rowIterator.hasNext()) {
            	rowIterator.next();
            }
            // read the rest of the rows
            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                 
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    Object cellValue = getCellValue(cell);
                    System.out.println(String.valueOf(cellValue));
                    System.out.print(" - ");
                     
                }
                System.out.println();
            }
             
            workbook.close();
            inputStream.close();
            
            
		} catch (Exception ex) {
		   ex.printStackTrace();	
		}
	}
	
	private Object getCellValue(Cell cell) {
		 
	    switch (cell.getCellTypeEnum()) {
	    case STRING:
	        return cell.getStringCellValue();
	 
	    case BOOLEAN:
	        return cell.getBooleanCellValue();
	 
	    case NUMERIC:
	        return cell.getNumericCellValue();
		case BLANK:
			break;
		case ERROR:
			break;
		case FORMULA:
			break;
		case _NONE:
			break;
		default:
			break;
	    }
	 
	    return null;
	}

}
