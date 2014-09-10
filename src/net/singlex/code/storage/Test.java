package net.singlex.code.storage;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Test {
	static public void main(String[] args) throws Exception { 
		FileOutputStream fos = new FileOutputStream("foo.xls"); 
		HSSFWorkbook wb = new HSSFWorkbook(); 
		HSSFSheet s = wb.createSheet(); 
		wb.setSheetName(0, "Matrix"); 
		for (short i = 0; i < 50; i++) { 
			HSSFRow row = s.createRow(i); 
			for (short j = 0; j < 50; j++) { 
				HSSFCell cell = row.createCell(j); 
				cell = row.createCell(0);
				cell.setCellValue("" + i + "," + j); 
			}
		}
		wb.write(fos); 
		fos.close();
	}
}