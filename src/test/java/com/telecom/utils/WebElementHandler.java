package com.telecom.utils;
/**Class that implements singleton design pattern (creational pattern) and the eager initialization concept. It's main use is the handling web pages elements and waits in a multithread safe manner. The class is declared public and final, and should have a private constructor to prevent instantiation if needed. The final keyword prevents sub-classing and can improve efficiency at runtime.
 * @author Marcelo Luna.
 * @version 2.0
 * @since 2.0
 */

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

public final class WebElementHandler {
	private static WebElementHandler instance = new WebElementHandler();
	private ThreadLocal <Wait<WebDriver>> fluentWaitThreadHolder = new ThreadLocal<Wait<WebDriver>>();
	private ThreadLocal <WebPageLoadHelper> pageLoadWaitThreadHolder = new ThreadLocal <WebPageLoadHelper>();
	private final int implicitWaitTimeOut = Integer.valueOf(AutoTool.getSetupValue("implicitWaitTimeOutSeconds"));
	private final int fluentWaitTimeOut = Integer.valueOf(AutoTool.getSetupValue("fluentWaitTimeoutSeconds"));
	private final int fleuntWaitPollingInterval = Integer.valueOf(AutoTool.getSetupValue("fluentWaitPollingEveryMillis"));
		
    private WebElementHandler() {
    }

    public static WebElementHandler getInstance() {
        return instance;
    }
    
    /** Sets Selenium's implicit wait and fluent wait, and a custom web load wait helper.
     * 
     */
	protected void setThreadWaitObjects() {
		AutoTool.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitTimeOut));
		setFluentWait(fluentWaitTimeOut, fleuntWaitPollingInterval);
		pageLoadWaitThreadHolder.set(new WebPageLoadHelper());
	}
	
	/**Initialize a Fluent WebDriver Wait object.
	 * @param timeOut Maximum amount of seconds to wait.
	 * @param pollingInterval Sets the intervals of milliseconds to query again.
	 */
	protected void setFluentWait(int timeOut, int pollingInterval) {
		fluentWaitThreadHolder.set(new FluentWait<WebDriver>(AutoTool.getDriver())
				.withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofMillis(pollingInterval))
				.ignoring(NoSuchElementException.class));
	}
	
	/** Clears ThreadLocal objects related to Selenium's fluent wait and a custom web load wait helper.
	 * 
	 */
	protected void removeThreadWaitObjects() {
		fluentWaitThreadHolder.remove();
		pageLoadWaitThreadHolder.remove();
	}
	
	/** Returns Selenium's fluent wait object from a ThreadLocal object.
	 * 
	 * @return
	 */
	public Wait<WebDriver> getFluentWait() {
		return fluentWaitThreadHolder.get();
	}
	
	/**Uses a fluent wait object to wait until an element of the page is visible.
	 * 
	 * @param webElement Element waited to be visible.
	 */
	public void waitForElement(WebElement webElement) {
		getFluentWait().until(ExpectedConditions.visibilityOf(webElement));
	}
	
	/**Waits for a page to be fully loaded before proceeding. Waiting for this is tricky because it depends on each page technology and the way it's developed so there are a lot of ways of doing this. The class WebPageLoadWaits contains three different examples to solve this, each should work under the right conditions.
	 */
	public void waitWebPageLoad() {
		pageLoadWaitThreadHolder.get().waitHTMLStopChanging(AutoTool.driverAsJSExecutor());
	}

	/**Runs a Javascript code to scroll the browser until a certain WebElement is visible. It waits half a second while the browser ends the scroll task.
	 * 
	 * @param webElement The element we wish to scroll to
	 */
	public void scrollToElement(WebElement webElement) {
		waitForElement(webElement);
		AutoTool.driverAsJSExecutor().executeScript("arguments[0].scrollIntoView({block: 'center'});", webElement);
		waitExternalReasons(500);
	}
	
	/**Switchs to an iframe by it's WebElement
	 *
	 * @param frame WebElement that represents the iframe you want to change to
	 */
	public void switchToFrameByWebElement(WebElement frame) {
		AutoTool.getDriver().switchTo().frame(frame);
	}
	
	/**Switchs to the default document or page Iframe.
	 * 
	 */
	public void switchToDefault() {
		AutoTool.getDriver().switchTo().defaultContent();
	}

	/**This method is used to send characters (keys) with delay to an input.
	 *
	 * @param text Text to be send to the input field
	 * @param element Input field WebElement
	 * @param delay Delay between keys in milliseconds
	 */
	public void sendKeysWithDelay(String text, WebElement element, int delay){
		text.chars().forEach(character->{
			element.sendKeys(String.valueOf((char)character));
			waitExternalReasons(delay);
		});
	}
	
	/**Selects an option from a dropdown.
	 *
	 * @param dropdown The dropdown WebElement
	 * @param option The option to be selected from the dropdown
	 */
	public void selectDropdownOptions(WebElement dropdown, String option) {
		waitForElement(dropdown);
		dropdown.click();
		Select optionDropdown = new Select(dropdown);
		optionDropdown.selectByVisibleText(option);
	}
	
	/**Highlights a WebElement in screen with a red border and a yellow background.
	 * 
	 * @param element The element to be highlighted.
	 */
	public void highLightElement(WebElement element){
		((JavascriptExecutor)AutoTool.getDriver()).executeScript("arguments[0].setAttribute('style','background: yellow; border: 2px solid red;');", element);   
	}
	
	/**Scrolls the screen in a sequence of steps by an amount of pixels
	 * 
	 * @param steps Times to do the scrolling.
	 * @param amountOfPixels Amount of pixels to scroll each time.
	 */
	public void scrollInSequence(int steps, int amountOfPixels) {
		int counter = 0;
		for (int i = 0; i < steps; i++) {
			counter = counter + amountOfPixels;
			AutoTool.driverAsJSExecutor().executeScript("window.scroll(0,"+counter+")");
			waitExternalReasons(400);
		}
	}
	
	/**Puts the thread which call it in pause for the set duration. It should be used under very few and precise circumstances. It shouldn't be used as a delay for Selenium.
	 * 
	 * @param miliseconds
	 */
	private void waitExternalReasons(long miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}
	
    /**Wait for a WebElement to be clickable and click it .
    *
    * @param webElement The element to be Clickable.
    */
   public void waitAndClickWebElement(WebElement webElement) {
	   getFluentWait().until(ExpectedConditions.elementToBeClickable(webElement));
       webElement.click();
   }

   public void waitForElementPresence(String locator) {
	   getFluentWait().until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
   }
   
   /**Used to get an element by text (provided by parameter)
   /**
    * Used to get an element by text (provided by parameter)
    *
    * @param text
    */
   public WebElement getElementByText(String text) {
       return AutoTool.getDriver().findElement(By.xpath("//*[normalize-space(text()) = '" + text + "']"));
   }
   
   /**
    * Used to get an element by xpath (provided by parameter)
    *
    * @param text
    */
   public WebElement getElementByXpath(String text) {
       return AutoTool.getDriver().findElement(By.xpath(text));
   }
   
   public void clickWebElementByJs(WebElement webElement){
	   getFluentWait().until(ExpectedConditions.elementToBeClickable(webElement));
	   AutoTool.driverAsJSExecutor().executeScript("arguments[0].click();", webElement);
   }
}