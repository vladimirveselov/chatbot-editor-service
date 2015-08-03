package org.vvv.chatbotdb.utils;

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

	public List<List<List<String>>> readFromExcel(InputStream is)
			throws IOException {
		List<List<List<String>>> data = new ArrayList<List<List<String>>>();
		try (XSSFWorkbook wb = new XSSFWorkbook(is)) {
			Iterator<XSSFSheet> sheets = wb.iterator();
			while (sheets.hasNext()) {
				XSSFSheet sheet = sheets.next();
				List<List<String>> sheetObj = new ArrayList<List<String>>();
				data.add(sheetObj);
				for (Row row : sheet) {
					List<String> rowObj = new ArrayList<String>();
					sheetObj.add(rowObj);
					for (int i = 0; i < row.getLastCellNum(); i++) {
						Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
						rowObj.add(cell.getStringCellValue());
					}
				}
			}
		}
		return data;
	}

}
