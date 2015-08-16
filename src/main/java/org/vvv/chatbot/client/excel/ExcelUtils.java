package org.vvv.chatbot.client.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	public List<ExcelSheet> readFromExcel(InputStream is)
			throws IOException {
		List<ExcelSheet> data = new ArrayList<ExcelSheet>();
		try (XSSFWorkbook wb = new XSSFWorkbook(is)) {
			Iterator<XSSFSheet> sheets = wb.iterator();
			while (sheets.hasNext()) {
				XSSFSheet sheet = sheets.next();
				ExcelSheet esh = new ExcelSheet();
				esh.setName(sheet.getSheetName());
				List<List<String>> sheetObj = new ArrayList<List<String>>();
				for (Row row : sheet) {
					List<String> rowObj = new ArrayList<String>();
					sheetObj.add(rowObj);
					for (int i = 0; i < row.getLastCellNum(); i++) {
						Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
						String rsdata = "";
						try {
						    rsdata = cell.getStringCellValue();
						} catch (IllegalStateException ex) {
						    rsdata = cell.getNumericCellValue() + "";
						}
						rowObj.add(rsdata);
					}
				}
				esh.setData(sheetObj);
				data.add(esh);
			}
		}
		return data;
	}

}
