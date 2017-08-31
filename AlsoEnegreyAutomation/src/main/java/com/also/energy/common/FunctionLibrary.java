package com.also.energy.common;

import java.awt.AWTException;
import java.awt.Robot;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

/**
 * 
 * @author nilesh
 * FunctionLibrary used to maintaned all common function used to selenium webdriver 
 */
public class FunctionLibrary 
{
	public static final int MAXTIMEOUT = 60;
	public  Logger logger= Logger.getLogger(FunctionLibrary.class);
	private WebDriver driver;
	public Long timeout;
	private Actions builder;
	
	public FunctionLibrary(WebDriver driver) {
		this.driver = driver;
		this.builder = new Actions(driver);
	}
	
	public static boolean isElementPresent(WebDriver driver, WebElement slocator) {
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		try {
			slocator.isDisplayed();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			return true;
		} catch (NoSuchElementException e) {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			return false;
		}
	}
	

	public void setDriver(WebDriver driver) {
		this.driver = driver;
		this.builder = new Actions(this.driver);
	}

	public void waitForDriver(long enterSeconds) {
		this.driver.manage().timeouts()
				.implicitlyWait(enterSeconds, TimeUnit.SECONDS);
		logger.info("Driver waiting for: " + enterSeconds);
	}

	static char[] specialCharacters = { '!', '@', '#', '$', '%', '^', '&', '*',
			'(', ')', '?', '/', '"', '|', '{', '[', '<', '>', ';', '`', ',',
			'_', '-' };

	public String getPopupMessage() {
		String message = null;
		try {
			Alert alert = this.driver.switchTo().alert();
			message = alert.getText();
			alert.accept();

		} catch (Exception e) {

			message = null;
		}
		logger.info("message" + message);
		return message;
	}

	public String cancelPopupMessageBox() {
		String message = null;
		try {
			Alert alert = this.driver.switchTo().alert();
			message = alert.getText();
			logger.info("Alert Window: " + message);
			alert.dismiss();

		} catch (Exception e) {

			message = null;
		}

		return message;
	}

	public void acceptPopup() {
		try {
			Alert alert = this.driver.switchTo().alert();

			alert.accept();
			this.driver.switchTo().defaultContent();
			logger.info("Alert Accepted");
		} catch (Exception localException) {
		}
	}

	public String getTitle() {
		String pageTitle = this.driver.getTitle();
		logger.info(pageTitle + " title found");
		return pageTitle;
	}

	public String getText(WebElement slocator) {
		String sText = slocator.getText();
		logger.info(sText + " text found");
		return sText;
	}

	public String getAttributeValue(WebElement slocator, String sValue) {
		String sText = slocator.getAttribute(sValue);
		logger.info(sText + " text found");
		return sText;
	}

	public void cancelPopup() {
		try {
			Alert alert = this.driver.switchTo().alert();
			alert.dismiss();
		} catch (Exception localException) {
		}
	}

	public void mouseOvertoElement(WebElement slocator) {
		logger.info(this.driver.getTitle());
		waitUntilElementPresent(slocator);
		String sElement = slocator.getText();
		this.builder.moveToElement(slocator).build().perform();
		logger.info("Move mouse to \"" + sElement + "\"");
	}

	public boolean waitUntilElementPresent(WebElement slocator) {
		boolean bResult = false;
		Long maxWait = this.timeout;
		int secsWaited = 0;
		try {
			do {
				Thread.sleep(100L);
				secsWaited++;
				if (isElementPresent(slocator)) {
					logger.info("Element found, wait time is = " + secsWaited);
					bResult = true;
					break;
				}
			} while (

			secsWaited < maxWait.longValue() * 10L);
			Thread.sleep(100L);
		} catch (Exception e) {
			logger.info("Exception caught while waiting for the page to load ");
			bResult = false;
		}
		return bResult;
	}

	public void waitForElementPresent(By locator, long timeout) {
		this.driver.manage().timeouts()
				.implicitlyWait(timeout, TimeUnit.SECONDS);
		try {
			this.driver.findElement(locator);
		} catch (NoSuchElementException e) {
			System.err.print(e.getMessage());
		}
	}

	public void populateField(WebDriver driver, By locator, String value) {
		WebElement field = driver.findElement(locator);
		field.clear();
		field.sendKeys(new CharSequence[] { value });
	}

	public String checkHoverMessage(WebElement slocator) {
		String tooltip = slocator.getAttribute("title");
		return tooltip;
	}

	public void selectRadioButton(By locator, String value) {
		List<WebElement> select = this.driver.findElements(locator);

		for (WebElement radio : select) {
			if (radio.getAttribute("value").equalsIgnoreCase(value)) {
				radio.click();
			}
		}
	}

	public void selectDropdown(WebElement slocator, String value) {
		List<WebElement> getDropDownValues = slocator.findElements(By
				.tagName("option"));
		boolean match = false;
		logger.info("Total no. of dropdown values:" + getDropDownValues.size());
		for (int i = 0; i < getDropDownValues.size(); i++) {
			logger.info(((WebElement) getDropDownValues.get(i)).getText());
			if (((WebElement) getDropDownValues.get(i)).getText()
					.equalsIgnoreCase(value)) {
				((WebElement) getDropDownValues.get(i)).click();
				match = true;
				break;
			}
		}
		if (!match) {
			logger.info("No Selection Found");
			Assert.fail(value + "Not found in the dropdown " + slocator);
		}
	}

	public void selectDropdownByPartialText(WebElement slocator, String value) {
		List<WebElement> getDropDownValues = slocator.findElements(By
				.tagName("option"));
		boolean match = false;
		logger.info("Total no. of dropdown values:" + getDropDownValues.size());
		for (int i = 0; i < getDropDownValues.size(); i++) {
			logger.info(((WebElement) getDropDownValues.get(i)).getText());
			if (((WebElement) getDropDownValues.get(i)).getText().contains(
					value)) {
				((WebElement) getDropDownValues.get(i)).click();
				match = true;
				break;
			}
		}
		if (!match) {
			logger.info("No Selection Found");
			Assert.fail(value + "Not found in the dropdown " + slocator);
		}
	}

	public void selectDropDownByValue(WebElement locator, String value) {
		try {
			Select select = new Select(locator);
			select.selectByValue(value);
			logger.info("[" + value + "] selected");
		} catch (Exception e) {
			logger.info("[" + value + "] not found");
			Assert.fail("[" + value + "] not found");
		}
	}

	public void selectDropDownByText(WebElement locator, String text) {
		try {
			Select select = new Select(locator);
			select.selectByVisibleText(text);
			logger.info("[" + text + "] selected");
		} catch (Exception e) {
			logger.info("[" + text + "] not found");
			Assert.fail("[" + text + "] not found");
		}
	}

	public void selectDropdownByIndex(WebElement slocator, int Index) {
		try {
			Select sel = new Select(slocator);
			sel.selectByIndex(Index);
			logger.info(sel.getFirstSelectedOption().getText()
					+ " value selected");
		} catch (Exception e) {
			logger.info("index " + Index + " not found");
			Assert.fail("index " + Index + " not found");
		}
	}

	public void selectSearchDropdown(WebElement slocator, String value) {
		slocator.click();
		slocator.sendKeys(new CharSequence[] { value });
		slocator.sendKeys(new CharSequence[] { Keys.TAB });
	}

	public int getImageCount(WebElement slocator) {
		List<WebElement> getimgCount = slocator.findElements(By.tagName("img"));
		logger.info(Integer.valueOf(getimgCount.size()));
		return getimgCount.size();
	}

	public void handleNewWindow(String windowTitle) {
		boolean found = false;
		String currentWindow = this.driver.getWindowHandle();
		Set<String> handles = this.driver.getWindowHandles();
		Iterator<String> i = handles.iterator();
		while (i.hasNext()) {
			String handle = (String) i.next();
			this.driver.switchTo().window(handle);
			if (this.driver.getTitle().equals(windowTitle)) {
				found = true;
				logger.info("Window with title " + windowTitle
						+ " found and Swiched");
				break;
			}
		}
		if (!found) {
			this.driver.switchTo().window(currentWindow);
			logger.info("Window with title " + windowTitle + " not found");
		}
	}



	public void handleNativeWindow() {
		try {
			Robot robot = new Robot();
			robot.keyPress(10);
			logger.info("Native Window accepted");
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void handleNewTab() {
		for (String winHandle : this.driver.getWindowHandles()) {
			this.driver.switchTo().window(winHandle);
		}

		logger.info("current window handle is: "
				+ this.driver.getWindowHandle());
	}

	public void verifyText(WebElement slocator, String ExpectedText) {
		String ActualText = slocator.getText().trim();
		ExpectedText = ExpectedText.trim();

		if (ExpectedText.equalsIgnoreCase(ActualText)) {
			logger.info("On page " + this.driver.getTitle() + ". Expected Text"
					+ ExpectedText + "Verified");
		} else {
			logger.info("On page " + this.driver.getTitle()
					+ ". Expected Text=" + ExpectedText
					+ "Not Found, instead found=" + ActualText);
			Assert.fail("On page " + this.driver.getTitle()
					+ ". Expected Text=" + ExpectedText
					+ "Not Found, instead found=" + ActualText);
		}
	}

	public void verifyText(String expected) {
		try {
			this.driver.findElement(By.xpath("//*[contains(text(),'"
					+ expected.trim() + "')]"));
			logger.info("On page " + this.driver.getTitle()
					+ ". Expected Text \"" + expected + "\" verified");
		} catch (NoSuchElementException e) {
			logger.info("On page " + this.driver.getTitle()
					+ ". Expected Text \"" + expected + "\" not found");
			Assert.fail("On page " + this.driver.getTitle()
					+ ". Expected Text \"" + expected + "\" not found");
		}
	}

	public boolean verifyTextByBoolean(String expected) {
		try {
			this.driver.findElement(By.xpath("//*[contains(text(),'"
					+ expected.trim() + "')]"));
			logger.info("On page " + this.driver.getTitle()
					+ ". Expected Text \"" + expected + "\" verified");
			return true;
		} catch (NoSuchElementException e) {
			logger.info("On page " + this.driver.getTitle()
					+ ". Expected Text \"" + expected + "\" not found");
		}
		return false;
	}

	public void verifyTitle(String expected) {
		boolean found = false;
		int count = 0;
		while (count < 30) {
			if (this.driver.getTitle().equalsIgnoreCase(expected)) {
				logger.info("On page " + this.driver.getTitle()
						+ ". Expected Text \"" + expected + "\" verified");
				found = true;
				break;
			}
			count++;
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (!found) {
			logger.info("On page " + this.driver.getTitle()
					+ ". Expected Text \"" + expected + "\" not found");
			Assert.fail("On page " + this.driver.getTitle()
					+ ". Expected Text \"" + expected + "\" not found");
		}
	}

	public void verifyDropDownDefaultValue(WebElement locator, String value) {
		Select select = new Select(locator);
		String defaultVal = select.getFirstSelectedOption().getText();
		if (defaultVal.equalsIgnoreCase(value)) {
			logger.info("Default value [" + value + "] verified");
		} else {
			logger.info("On page " + this.driver.getTitle() + ". Expected: ["
					+ value + "]. Actual: [" + defaultVal + "]");
			Assert.fail("On page " + this.driver.getTitle() + ". Expected: ["
					+ value + "]. Actual: [" + defaultVal + "]");
		}
	}

	public void refreshPage() {
		this.driver.navigate().refresh();
	}

	
	public void validatePageURL(String PageName, String ExpectedPageURL) {
		logger.info("Current URL=" + this.driver.getCurrentUrl() + "\n"
				+ "Expected url=" + ExpectedPageURL);
		if (this.driver.getCurrentUrl().contains(ExpectedPageURL)) {
			logger.info(PageName + " Page URL verified - " + ExpectedPageURL);
		} else {
			logger.info("On" + PageName + "\t Page " + ExpectedPageURL
					+ " URL Not Found.");

			Assert.fail("On" + PageName + "\t Page " + ExpectedPageURL
					+ " URL Not Found.");
		}
	}

	public void verifytDropdownValue(WebElement slocator, String[] value) {
		Select select = new Select(slocator);

		List<WebElement> getDropDownValues = select.getOptions();
		logger.info(Integer.valueOf(getDropDownValues.size()));

		for (int i = 0; i < getDropDownValues.size(); i++) {
			if (((WebElement) getDropDownValues.get(i)).getText().trim()
					.equals(value[i].trim())) {
				logger.info(((WebElement) getDropDownValues.get(i)).getText()
						+ "DropDownValue Found");
			} else {
				logger.info(((WebElement) getDropDownValues.get(i)).getText()
						+ "DropDownValue not Found");
				Assert.fail(((WebElement) getDropDownValues.get(i)).getText()
						+ "DropDownValue not Found");
			}
		}
	}

	public void verifyDropdownValueNotExist(WebElement slocator, String[] value) {
		Select select = new Select(slocator);
		List<WebElement> getDropDownValues = select.getOptions();
		logger.info(Integer.valueOf(getDropDownValues.size()));

		for (int i = 0; i < value.length; i++) {
			if (getDropDownValues.contains(value[i].trim())) {
				logger.info(value[i] + " is found in the drop down");
				Assert.fail(value[i] + " is found in the drop down");
			} else {
				logger.info(value[i] + " is not found in the drop down");
			}
		}
	}

	public void verifyLink(WebElement slocator, String sLinkName) {
		boolean match = false;
		List<WebElement> links = slocator.findElements(By.tagName("a"));

		for (int i = 0; i < links.size(); i++) {
			if (((WebElement) links.get(i)).getText().equals(sLinkName)) {
				match = true;
				logger.info(((WebElement) links.get(i)).getText() + " Found");
				break;
			}
		}

		if (!match) {
			logger.info(sLinkName + " not Found");
			Assert.fail(sLinkName + " not Found");
		}
	}

	public void verifyLink(String sLinkName) {
		boolean match = false;
		List<WebElement> links = this.driver.findElements(By.tagName("a"));

		for (int i = 0; i < links.size(); i++) {
			if (((WebElement) links.get(i)).getText().equals(sLinkName)) {
				match = true;
				logger.info(((WebElement) links.get(i)).getText() + " Found");
				break;
			}
		}

		if (!match) {
			logger.info(sLinkName + " not Found");
			Assert.fail(sLinkName + " not Found");
		}
	}


	public boolean isElementPresent(WebElement slocator) {
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		try {
			driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
			slocator.isDisplayed();
			logger.info(slocator + " is displayed");
			return true;
		} catch (NoSuchElementException e) {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			logger.info(slocator + " is not displayed");
		}
		return false;
	}
	@SuppressWarnings("finally")
	public boolean isElementPresentForDevice(WebElement slocator) {
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		try {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			slocator.isDisplayed();
			logger.info(slocator + " is displayed");
			return true;
		}finally{
			logger.info("Element not displayed");
		return false;
		}
	}

	public boolean isElementNotPresent(WebElement slocator) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		try {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			slocator.isDisplayed();
			logger.info(slocator + " is displayed");
			return false;
		} catch (NoSuchElementException e) {
			logger.info(slocator + " is not displayed");
		}
		return true;
	}

	public void verifyWebElement(WebElement slocator) {
		try {
			slocator.isDisplayed();
			logger.info(slocator + "Found ");
		} catch (Exception e) {
			logger.info(e.getMessage());
			logger.info(slocator + "Not Found ");
			Assert.fail(slocator + "Not Found ");
		}
	}
	
	

	public void click(WebElement slocator) {
		try {
			String Element = slocator.getText();
			if ((Element.isEmpty()) || (Element == null)) {
				Element = slocator.getAttribute("value");
			}
			logger.info(Element + " trying to click");
			waitUntilElementPresent(slocator);
			slocator.click();
			logger.info(Element + " clicked ");
			acceptPopup();
		} catch (Exception ex) {
			String sElement = slocator.getText();
			logger.info("Element - " + sElement + " Not Found & not clicked ");
			ex.printStackTrace();
		}
	}

	public void clickAllLinks(WebElement slocator, String NotExpectedText) {
		List<WebElement> linkElements = slocator.findElements(By.tagName("a"));
		String[] linkTexts = new String[linkElements.size()];
		int i = 0;

		for (WebElement e : linkElements) {
			linkTexts[i] = e.getText();
			i++;
		}

		for (String t : linkTexts) {
			this.driver.findElement(By.linkText(t)).click();
			if (this.driver.getTitle().equals(NotExpectedText)) {
				logger.info(NotExpectedText + "\"" + t + "\"" + " found");
				Assert.fail("No link found in the section " + slocator);
			} else {
				logger.info("link Found and clicked");
			}
			this.driver.navigate().back();
		}
	}

	public void doubleClick(WebElement locator) {
		logger.info("Trying to double click on " + locator);
		Actions action = new Actions(this.driver);
		action.doubleClick(locator).build().perform();
		logger.info("Double clicked on " + locator);
	}

	public void check(WebElement locator) {
		try {
			if (!locator.isSelected()) {
				click(locator);
				logger.info("Checkbox checked");
			}
		} catch (Exception ex) {
			String sElement = locator.getText();
			logger.info("Element - " + sElement + " Not Found & not clicked ");
			ex.printStackTrace();
		}
	}

	public void uncheck(WebElement locator) {
		try {
			if (locator.isSelected()) {
				click(locator);
				logger.info("Checkbox unchecked");
			}
		} catch (Exception ex) {
			String sElement = locator.getText();
			logger.info("Element - " + sElement + " Not Found & not clicked ");
			ex.printStackTrace();
		}
	}

	public void check(WebElement locator, String option) {
		if ((!locator.isSelected()) && (option.equalsIgnoreCase("yes"))) {
			click(locator);
			logger.info("Checkbox was unchecked, now checked");
		} else if ((locator.isSelected()) && (option.equalsIgnoreCase("no"))) {
			click(locator);
			logger.info("Checkbox was checked");
		} else {
			logger.info("Invalid option for check/uncheck. Valid options are yes or no");
		}
	}

	public void uncheck(WebElement locator, String option) {
		if ((locator.isSelected()) && (option.equalsIgnoreCase("yes"))) {
			click(locator);
			logger.info("Checkbox was checked, now unchecked");
		} else if ((!locator.isSelected()) && (option.equalsIgnoreCase("no"))) {
			click(locator);
			logger.info("Checkbox was unchecked");
		} else {
			logger.info("Invalid option for check/uncheck. Valid options are yes or no");
		}
	}

	public void setValue(WebElement slocator, String sValue) {
		String Element = slocator.getText();
		try {
			logger.info(Element + "trying to set the value");
			slocator.clear();
			slocator.sendKeys(new CharSequence[] { sValue });
			logger.info(sValue + " entered");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info(Element + "field not found");
		}
	}

	public void setValue(WebElement slocator, Object value) {
		String Element = slocator.getText();
		try {
			logger.info(Element + "trying to set the value");
			slocator.clear();
			slocator.sendKeys(new CharSequence[] { String.valueOf(value) });
			logger.info(String.valueOf(value) + " entered");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info(Element + "field not found");
		}
	}

	public void mouseOverAndClicktoElement(WebElement slocator) {
		waitUntilElementPresent(slocator);
		String sElement = slocator.getText();
		this.builder.moveToElement(slocator).build().perform();
		this.builder.click(slocator).build().perform();
		acceptPopup();
		logger.info("Move Mouse and Click \"" + sElement + "\"");
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getCurrentTimeStamp() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyyHHmmss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public String getCurrentTimeStamp(String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public String getCurrentURL() {
		String url = this.driver.getCurrentUrl();
		logger.info("Current URL is =" + url);
		return url;
	}

	public void scrollElementByID(String id) {
		JavascriptExecutor js = (JavascriptExecutor) this.driver;
		js.executeScript("var obj = document.getElementById(\"" + id + "\");"
				+ "obj.scrollTop = obj.scrollHeight;", new Object[0]);
		logger.info(id + " scrolled");
	}

	public void scrollElementByName(String name) {
		JavascriptExecutor js = (JavascriptExecutor) this.driver;
		js.executeScript("var obj = document.getElementById(\"" + name + "\");"
				+ "obj.scrollTop = obj.scrollHeight;", new Object[0]);
		logger.info(name + " scrolled");
	}

	public void switchToDefaultContent() {
		this.driver.switchTo().defaultContent();
	}

	public void switchFrame(String nameOrID) {
		try {
			int count = 0;
			if (count < 60) {
				this.driver.manage().timeouts()
						.implicitlyWait(0L, TimeUnit.SECONDS);
				try {
					this.driver.switchTo().frame(nameOrID);
				} catch (NoSuchFrameException e) {
					try {
						Thread.sleep(1000L);
					} catch (Exception ex) {
						logger.info("Error for Thread.sleep(");
					}
					count++;
				}
				this.driver.manage().timeouts()
						.implicitlyWait(60L, TimeUnit.SECONDS);
			}

		} catch (Exception e) {
			this.driver.manage().timeouts()
					.implicitlyWait(60L, TimeUnit.SECONDS);
			throw e;
		}
	}

	public void switchFrame(int index) {
		try {
			int count = 0;
			if (count < 60) {
				this.driver.manage().timeouts()
						.implicitlyWait(0L, TimeUnit.SECONDS);
				try {
					this.driver.switchTo().frame(index);
				} catch (NoSuchFrameException e) {
					try {
						Thread.sleep(1000L);
					} catch (Exception ex) {
						logger.info("Error for Thread.sleep(");
					}
					count++;
				}
				this.driver.manage().timeouts()
						.implicitlyWait(60L, TimeUnit.SECONDS);
			}

		} catch (Exception e) {
			this.driver.manage().timeouts()
					.implicitlyWait(60L, TimeUnit.SECONDS);
			throw e;
		}
	}

	public void switchFrame(WebElement element) {
		try {
			int count = 0;
			if (count < 60) {
				this.driver.manage().timeouts()
						.implicitlyWait(0L, TimeUnit.SECONDS);
				try {
					this.driver.switchTo().frame(element);
				} catch (NoSuchFrameException e) {
					try {
						Thread.sleep(1000L);
					} catch (Exception ex) {
						logger.info("Error for Thread.sleep(");
					}
					count++;
				}
				this.driver.manage().timeouts()
						.implicitlyWait(60L, TimeUnit.SECONDS);
			}

		} catch (Exception e) {
			this.driver.manage().timeouts()
					.implicitlyWait(60L, TimeUnit.SECONDS);
			throw e;
		}
	}

	public boolean isAlphabeticalOrder(List<String> list) {
		String previous = "";
		for (String current : list) {
			if (current.compareToIgnoreCase(previous) < 0)
				return false;
			previous = current;
		}
		return true;
	}

	public boolean isAlphabeticalOrder(String[] list) {
		String previous = "";
		for (String current : list) {
			if (current.compareToIgnoreCase(previous) < 0)
				return false;
			previous = current;
		}
		return true;
	}

	public void uploadFile(WebElement browseButton, String filePath) {
		browseButton.sendKeys(new CharSequence[] { filePath });
	}

	public void uploadFile(By browseButton, String filePath) {
		this.driver.findElement(browseButton).sendKeys(
				new CharSequence[] { filePath });
	}
	
	public int getResponseCode(String link) {
		try{
			URL url = new URL(link);
	        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.connect();
	        int code = connection.getResponseCode();
	        return code;
			}
			catch(Exception e){return -1;}
		}
		
		public void checkLink(String link) {
			try{
				URL url = new URL(link);
		        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		        connection.setRequestMethod("GET");
		        connection.connect();
		        int code = connection.getResponseCode();
		        if (code==200){
		        	logger.info("On page " + this.driver.getTitle()+ "verified link " + link);
		        }
		        else
		        {
		        	logger.info("On page " + this.driver.getTitle()+ "verified link " + link + "\" not working. Error code "+ code);
					Assert.fail("On page " + this.driver.getTitle()+ "verified link " + link + "\" not working. Error code "+ code);
		        }
		       
			}
			catch(Exception e){logger.error(e);}
		}
	
}
