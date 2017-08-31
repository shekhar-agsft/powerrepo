package com.also.energy.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import jxl.Cell;

/**
 * 
 * @author nilesh
 * Reader class is used to read properties files and input data files
 */
public class Reader {

	private static String configPathLocation = "Config.properties";
	
	public static String readFile(String File, String pName) {
		String pValue = "";
		try {
			Properties pro = new Properties();
			FileInputStream in = new FileInputStream(File);
			pro.load(in);

			pValue = pro.getProperty(pName);
			//logger.info(pName + " value from config.properties:" + pValue);
			return pValue;
		} catch (IOException e) {
			System.out.println("Error from config.properties for getting "
					+ pName + ":" + e.getMessage());
			e.printStackTrace();
		}
		return pValue;
	}
	
	public static String getConfigPropertyVal(String key) {
		String configPropertyVal = readFile(configPathLocation, key);
		if (key.contains("Application"))
			return configPropertyVal != null ? configPropertyVal.trim()
					.replaceAll("[^a-zA-Z0-9]+", "_") : configPropertyVal;
		if (key.contains("Cycle")) {
			return configPropertyVal != null ? configPropertyVal.trim()
					.replaceAll("[^a-zA-Z0-9 .]+", "_") : configPropertyVal;
		}
		return configPropertyVal != null ? configPropertyVal.trim()
				: configPropertyVal;
	}
	/**
	 * GetTable Array is used to read input data from xls
	 * @param xlFilePath
	 * @param sheetName
	 * @param tableName
	 * @return
	 */
	public static String[][] getTableArray(String xlFilePath, String sheetName, String tableName)
	   {
	     String[][] tabArray = null;
	     try {
	     System.out.println("Work Book created");
	     java.io.FileInputStream fis = new java.io.FileInputStream(xlFilePath);
	       jxl.Workbook workbook = jxl.Workbook.getWorkbook(fis);
	       jxl.Sheet sheet = workbook.getSheet(sheetName);
	      System.out.println("Work sheet created");
	       
	       Cell tableStart = sheet.findCell(tableName);
	       int startRow = tableStart.getRow();
	       int startCol = tableStart.getColumn();
	      System.out.println("startRow=" + startRow + ",startCol=" + startCol);
	       Cell tableEnd = sheet.findCell(tableName, startCol + 1, startRow + 1, 2000, 1000, false);
	      int endRow = tableEnd.getRow();
	      int endCol = tableEnd.getColumn();
	      System.out.println("startRow=" + startRow + ", endRow=" + endRow + ", " + "startCol=" + startCol + ", endCol=" + endCol);
	      tabArray = new String[endRow - startRow - 1][endCol - startCol - 1];
	       int ci = 0;
	       for (int i = startRow + 1; i < endRow; ci++) {
	         int cj = 0;
	        for (int j = startCol + 1; j < endCol; cj++) {
	           tabArray[ci][cj] = sheet.getCell(j, i).getContents();j++;
	         }
	         i++;
	       }
	
	     }
	     catch (Exception e)
	     {
	       System.out.println("error in getTableArray()"+e);
	       e.printStackTrace();
	     }
	     return tabArray;
	   }

}
