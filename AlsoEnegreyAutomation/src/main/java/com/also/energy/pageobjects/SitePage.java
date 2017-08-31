package com.also.energy.pageobjects;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.also.energy.common.FunctionLibrary;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;

public class SitePage {

	public Logger logger = Logger.getLogger(SitePage.class);
	private FunctionLibrary functionLibrary;

	@FindBy(how = How.XPATH, using = "//img[contains(@src,'https://www.alsoenergy.com/pub/Images/Device')]")
	public List<WebElement> deviceCount = new ArrayList<>();

	@FindBy(how = How.XPATH, using = "//*[contains(text(),'Operating Since')]")
	public WebElement siteStatus;

	@FindBy(how=How.XPATH,using="//div[contains(@style,'flex-basis: 80%')]")
	public List<WebElement> deviceNames=new ArrayList<>();
	
	@FindBy(how = How.XPATH, using = " //*[@id='app']//div[contains(@class,'fixedDataTableRowLayout_rowWrapper')]//div[contains(@class,'public_fixedDataTableCell_cellContent')]/div/div[2]/div")
	public List<WebElement> siteList = new ArrayList<>();
	
	private WebDriver driver;

	public SitePage(WebDriver driver) {
		this.driver = driver;
		functionLibrary = new FunctionLibrary(driver);
	}

	/**
	 *
	 * @param sites
	 *            Function Checks the total number of devices inside each site
	 */
	public void getDeviceCountForSites(List<WebElement> sites) {
		logger.info("Inside site getDeviceCount" + sites.size());
		
		/**
		 * 1. get all the list names.
		 * 2 . construct the XPATH for each List names.	
		 * 3 . Perform click on that element, 
		 * 4 . navigate . back
		 * 5 .  
		 */
		
		for (int i = 0; i < siteList.size(); i++) {
			String siteName=siteList.get(i).getText();
			siteList.get(i).click();
			if (functionLibrary.isElementNotPresent(siteStatus)) {
				logger.error("Site"  +siteName+  "is not Operating");
				driver.navigate().back();
			} else {
				logger.info("There are"+" "+""+deviceCount.size()+" "+ "devices for site::"+siteName+" "+  "are as follows  ");
				for (WebElement webElement : deviceNames) {		
					String str=webElement.getText().split("\n")[0];
				logger.info(str);
				}
				driver.navigate().back();
			}
		}
	}
}
