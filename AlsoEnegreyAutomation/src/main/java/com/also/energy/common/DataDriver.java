package com.also.energy.common;

import org.testng.annotations.DataProvider;

/**
 * 
 * @author nilesh
 * DataDriver class is used to read input data from .xls file
 */
public class DataDriver 
{
	@DataProvider(name = "DataDriver")
	public static Object[][] getData() {
		Object[][] retObjArr = Reader.getTableArray(
				Reader.getConfigPropertyVal(Constants.testDataFile),
				Reader.getConfigPropertyVal(Constants.tabName),
				Reader.getConfigPropertyVal(Constants.terminateDataEnd));
		return (retObjArr);
	}
}
	

