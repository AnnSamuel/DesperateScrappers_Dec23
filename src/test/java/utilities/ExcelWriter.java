package utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class ExcelWriter {

	public static <T> void writeDataToExcel(List<T> dataList, String filePath) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("DataSheet");

		// Write headers (field names) in the first row
		Row headerRow = sheet.createRow(0);
		Field[] fields = dataList.get(0).getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(fields[i].getName());
		}

		// Write data to Excel
		for (int rowIndex = 0; rowIndex < dataList.size(); rowIndex++) {
			Row dataRow = sheet.createRow(rowIndex + 1); // Start from row 1 to leave space for headers
			T pojoObject = dataList.get(rowIndex);

			for (int colIndex = 0; colIndex < fields.length; colIndex++) {
				Cell cell = dataRow.createCell(colIndex);
				fields[colIndex].setAccessible(true);

				try {
					Object value = fields[colIndex].get(pojoObject);
					if (value != null) {
						if (value instanceof String) {
							cell.setCellValue((String) value);
						} else if (value instanceof Number) {
							cell.setCellValue(((Number) value).doubleValue());
						} else {
							// Handle other data types as needed
							cell.setCellValue(value.toString());
						}
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		// Write the workbook content to a file
		try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
			workbook.write(fileOut);
			System.out.println("Excel file written successfully!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("error -->> "+ e.getMessage());
		} finally {
			// Close the workbook to release resources
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
