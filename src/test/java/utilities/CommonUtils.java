package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import PojoClass.RecipePojo;

public class CommonUtils {
	public static List<String> getEliminationAddList(String filePath, String sheetName, int colIndex) {
		List<String> eliminationList = new ArrayList<String>();
		try {
			FileInputStream fileInputStream = new FileInputStream(filePath);
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

			eliminationList = ExcelReader.readColumnData(workbook, sheetName, 0); // 0 is the column index

			// Display the data
			for (String data : eliminationList) {
				System.out.println(data);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return eliminationList;

	}

	public static List<RecipePojo> getFilteredList(List<RecipePojo> pojoList, List<String> eliminationList) {
		List<RecipePojo> filteredPojoList = new ArrayList<RecipePojo>();

		for (RecipePojo pojo : pojoList) {

			List<String> filteredIngredientList = pojo.getIngredients().stream()
					.filter(item -> eliminationList.stream().noneMatch(item::contains)).collect(Collectors.toList());
			if (filteredIngredientList.size() == pojo.getIngredients().size()) {
				filteredPojoList.add(pojo);
			}

		}

		return filteredPojoList;
	}

}
