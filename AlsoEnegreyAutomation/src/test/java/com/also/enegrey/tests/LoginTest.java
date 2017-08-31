package com.also.enegrey.tests;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.also.energy.common.CrossBrowser;
import com.also.energy.common.Reader;
import com.also.energy.pageobjects.LoginPage;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class LoginTest {

	public static Logger logger = Logger.getLogger(LoginTest.class);

	public LoginPage loginPageObject;
	
	 private CrossBrowser browser;
	 public static  RemoteWebDriver driver;
	
	  
	 
	//public WebDriver driver;
	public static String appURL = Reader.getConfigPropertyVal("applicationUrl");
	public static String chromeURL = Reader.getConfigPropertyVal("selenium.server.execution.path");

	@BeforeSuite(alwaysRun = true)
	public void driverInit() throws Exception {
		
		 browser = new CrossBrowser();
		 driver = browser.openBrowser(); 
		 
		/*System.setProperty("webdriver.chrome.driver", chromeURL);
		driver = new ChromeDriver();*/
		loginPageObject = PageFactory.initElements(driver, LoginPage.class);

	}

	/**
	 * Nilesh
	 */
	@Test(groups = { "testExecution" })
	public void loginTest() {
		System.out.println("*** Navigation to Application ***");
		driver.navigate().to(appURL);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		loginPageObject.loginUser();
		
		
		
		
	}

	@AfterSuite(alwaysRun = true)
	public void cleanup() {
		try {
			driver.quit();
			logger.info("Driver quit.");
		} catch (WebDriverException e) {
			logger.error("Exception while closing the driver :: " + e.getMessage());
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
	}
}
