package com.also.enegrey.tests;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.also.energy.common.CrossBrowser;
import com.also.energy.common.Reader;
import com.also.energy.pageobjects.HomePage;
import com.also.energy.pageobjects.LoginPage;

public class HomeTest {
	public static Logger logger = Logger.getLogger(HomeTest.class);
	//public WebDriver driver;
	public static String appURL = Reader.getConfigPropertyVal("applicationUrl");
	public static String chromeURL = Reader.getConfigPropertyVal("selenium.server.execution.path");
	public HomePage homeTestPage;
	public LoginPage loginTestPage;
	private CrossBrowser browser;
	public static  RemoteWebDriver driver;
	
	

	@BeforeTest(alwaysRun = true)
	public void driverInit() throws Exception {
		browser = new CrossBrowser();
		driver = browser.openBrowser(); 
		loginTestPage = PageFactory.initElements(driver, LoginPage.class);
		homeTestPage = PageFactory.initElements(driver, HomePage.class);
	}

	@Test(groups = { "testExecution" })
	public void homePageTest() {
		driver.navigate().to(appURL);
		loginTestPage.loginUser();
		homeTestPage.verifySiteCount();
	}

	@AfterTest(alwaysRun = true)
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
