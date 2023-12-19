package utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
   

    public static List<String> readColumnData(XSSFWorkbook workbook, String sheetName, int columnIndex) {
        List<String> columnData = new ArrayList<>();
        Iterator<Row> rowIterator = workbook.getSheet(sheetName).iterator();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell cell = row.getCell(columnIndex);

            // Assuming all data in the column is of string type
            String cellValue = cell.getStringCellValue();
            columnData.add(cellValue);
        }

        return columnData;
    }
}