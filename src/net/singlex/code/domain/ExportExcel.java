package net.singlex.code.domain;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExportExcel {
	
	public void exportToExcel(ArrayList<String> list) throws IOException{

		String filename = "/home/singlex/HTTP/DomainList-"+System.currentTimeMillis()+".xls";
		FileOutputStream fos = new FileOutputStream(filename);
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(0, "domain");
		// sheet title
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("Domain");
		cell = row.createCell(1);
		cell.setCellValue("Note");
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i+1);
			for (int j = 0; j < 1; j++) {
				cell = row.createCell(j);
				cell.setCellValue(list.get(i));
			}
		}
		workbook.write(fos);
		fos.close();
	}
}