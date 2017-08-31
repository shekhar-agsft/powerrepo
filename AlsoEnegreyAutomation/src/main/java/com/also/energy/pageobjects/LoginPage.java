package com.also.energy.pageobjects;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.also.energy.common.FunctionLibrary;
import com.also.energy.common.Reader;

/**
 *
 * @author nilesh
 * LoginTest Page object used to maintane WebElements of that login page 
 * and also contains login page methods which perform operations on those WebElements.
 */
public class LoginPage 
{
	public  Logger logger= Logger.getLogger(LoginPage.class);
	private  WebDriver driver;
	private FunctionLibrary functionLibrary;
	
		 
	@FindBy(how=How.XPATH, using=".//*[@id='Username']")
	public WebElement uNameTbox;
	
	@FindBy(how=How.XPATH, using=".//*[@id='Password']")
	public WebElement pwdTbox;
	
	@FindBy(how=How.XPATH, using=".//*[@id='loginForm']/form//input[@type='submit']")
	public WebElement loginButton;
	
	@FindBy(how=How.XPATH, using=".//div[@id='app']//img[@src='/Content/images/ui/userprofile.png']")
	public WebElement loggedInUserIcon;
	
	
	
	String powerTrackuser = Reader.getConfigPropertyVal("username");
	String powerTrackUserpassword=Reader.getConfigPropertyVal("password");
	 
	public LoginPage(WebDriver driver) 
	{
		this.driver = driver;	
		functionLibrary=new FunctionLibrary(driver);
    }
	
	/**
	 * loginUser() used to test login functionality
	 * @param extentTestlogger2 
	 * @param report2 
	 */
	public void loginUser()
	{
		try{
			if(functionLibrary.isElementPresent(uNameTbox) && functionLibrary.isElementPresent(pwdTbox))
			{
				uNameTbox.sendKeys(powerTrackuser);
				pwdTbox.sendKeys(powerTrackUserpassword);
				if(functionLibrary.isElementPresent(loginButton))
				{
					loginButton.submit();
					Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)							
							.withTimeout(30, TimeUnit.SECONDS) 			
							.pollingEvery(5, TimeUnit.SECONDS) 			
							.ignoring(NoSuchElementException.class);
					
					wait.until(ExpectedConditions.visibilityOf(loggedInUserIcon));
					if(functionLibrary.isElementPresent(loggedInUserIcon))
					{
						logger.info("User login successful :"+ driver.getCurrentUrl());
						
					}
					else
					{
						Assert.fail("User login failed:"+driver.getCurrentUrl());
						
					}
					
				}
				else
				{
					Assert.fail("User login button not present on page:"+driver.getCurrentUrl());
				}
			}
			else
			{
				Assert.fail("User name and password input field not present on page :"+driver.getCurrentUrl());
			}
	    
	}
	catch(Exception e)
	{
		e.printStackTrace();
		Assert.fail("Unable to login. Exception - "+e);
	}
	}
	
	
}
