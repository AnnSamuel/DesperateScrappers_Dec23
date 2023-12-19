package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import PojoClass.RecipePojo;

public class CommonUtils {
	public static ResourceBundle endpoints = ResourceBundle.getBundle("properties/endpoint");
	
	public static String getURL(String type) {
		String url = "";
		switch (type) {
		case "DIABETES":
			url = endpoints.getString("diabetesUrl");
			break;
		case "HYPOTHYROIDISM":
			url = endpoints.getString("hypothyroidismUrl");
			break;
		case "HYPERTENSION":
			url = endpoints.getString("hypertensionUrl");
			break;
		case "PCOS":
			url = endpoints.getString("pcosUrl");
			break;
		}
		return url;

	}
	
	public static String getEliminationSheetName(String type) {
		String eliminationSheetName = "";
		switch (type) {
		case "DIABETES":
			eliminationSheetName = "Diabetes Elimination"; 
			break;
		case "HYPOTHYROIDISM":
			eliminationSheetName = "Hypothyroidism Elimination";
			break;
		case "HYPERTENSION":
			eliminationSheetName = "Hypertension Elimination";
			break;
		case "PCOS":
			eliminationSheetName = "PCOS Elimination";
			break;
			
		}
		return eliminationSheetName;

	}
	
	public static String getSheetName(String type) {
		String createSheetName = "";
		switch (type) {
		case "DIABETES":
			createSheetName = "DiabeteSheet";
			break;
		case "HYPOTHYROIDISM":
			createSheetName = "HypothyroidismSheet";
			break;
		case "HYPERTENSION":
			createSheetName = "HypertensionSheet";
			break;
		case "PCOS":
			createSheetName = "PcosSheet";
			break;
		}
		return createSheetName;
	}
	
	public static String getMorbidConditions(String type) {
		String morbidCondition = "";
		switch (type) {
		case "DIABETES":
			morbidCondition = "diabetes";
			break;
		case "HYPOTHYROIDISM":
			morbidCondition = "hypothyroidism";
			break;
		case "HYPERTENSION":
			morbidCondition = "hypertension";
			break;
		case "PCOS":
			morbidCondition = "pcos";
			break;
		}
		return morbidCondition;
	}

	public static List<String> getEliminationList(String filePath, String sheetName, int colIndex) {
		List<String> eliminationList = new ArrayList<String>();
		List<String> splitlEliminationList = new ArrayList<String>();
		try {
			FileInputStream fileInputStream = new FileInputStream(filePath);
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

			eliminationList = ExcelReader.readColumnData(workbook, sheetName, 0); // 0 is the column index

			// split the data
			for (String data : eliminationList) {

				List<String> splittedValue = Arrays.asList(data.split(", "));
				splitlEliminationList.addAll(splittedValue);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return splitlEliminationList;

	}

	public static List<RecipePojo> getFilteredList(List<RecipePojo> pojoList, List<String> eliminationList) {
		List<RecipePojo> filteredPojoList = new ArrayList<RecipePojo>();

		for (RecipePojo pojo : pojoList) {

//			List<String> filteredIngredientList = pojo.getIngredients().stream()
//					.filter(item -> eliminationList.stream().noneMatch(item::contains)).collect(Collectors.toList());
			
			List<String> filteredIngredientList = pojo.getIngredients().stream()
				    .filter(item ->
				        eliminationList.stream().noneMatch(elimination ->
				            item.toLowerCase().contains(elimination.toLowerCase())
				        )
				    )
				    .collect(Collectors.toList());
			
			
			if (filteredIngredientList.size() == pojo.getIngredients().size()) {
				filteredPojoList.add(pojo);
			}

		}

		return filteredPojoList;
	}

	
}
