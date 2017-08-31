package com.also.energy.common;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * 
 * @author nilesh
 * Cross platform and Browser settings configuration 
 */
public class CrossBrowser 
{
	private Logger logger = Logger.getLogger(CrossBrowser.class);

	private RemoteWebDriver driver;
	/**
	 * TODO : Check with Nilesh why he set the property of chrome/fireFox exe.
	 * 
	 */
	public RemoteWebDriver openBrowser() throws Exception
	{
		DesiredCapabilities capabilities = new DesiredCapabilities();
		String browser = Reader.getConfigPropertyVal("BrowserType");
			if (browser.equalsIgnoreCase("FireFox")) 
			{
				capabilities = DesiredCapabilities.firefox();
			}
		    else if (browser.equalsIgnoreCase("Chrome")) 
			{
		    	capabilities = DesiredCapabilities.chrome();
		    } 
		    else
		    {
		    	capabilities=null;
		    }
		String hubServerUrl=Reader.getConfigPropertyVal("hub.server.url");
		driver = new RemoteWebDriver(new URL(hubServerUrl), capabilities);
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
		logger.debug("openBrowser :: " + hubServerUrl + " " + browser);
		return driver;
	}

	public RemoteWebDriver getDriver() 
	{
		return driver;
	}
	
	
}
