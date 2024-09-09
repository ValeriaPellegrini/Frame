package com.telecom.utils;
/**Completely stateless utility class in Java. The class is declared public and final, and should have a private constructor to prevent instantiation if needed. The final keyword prevents sub-classing and can improve efficiency at runtime. It's main use is the handling and setup of a WebDriver object in a multithread safe manner.
 * @author Marcelo Luna.
 * @version 1.0
 * @since 1.0
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.telecom.webDriver.DriverFactory;

public final class AutoTool {
	private static ThreadLocal <WebDriver> webDriverThreadHolder = new ThreadLocal <WebDriver>();
	private static String appLocation = System.getProperty("user.dir").replace("\\", "//");
	private static Properties properties=null;

    /**Returns the HDD path in where the main application is running.
     *
     * @return FAC application HDD execution path.
     */
    public static String getFACRunningPath() {
        return appLocation;
    }

	/**Sets the kind of WebDriver to use, initialize a web's element handler object and a pageObject's handler object.
	 * 
	 * @param selectedDriver Kind of web driver to be used.
	 */
	public synchronized static void setupDriver(String selectedDriver) {
		webDriverThreadHolder.set(DriverFactory.createDriver(selectedDriver));
		webHandler().setThreadWaitObjects();
		pageObject().setThreadPagesContainer();
	}
	
	/**Calls Selenium's methods to properly clear a driver's instance, clears the ThreadLocal container for the driver, web's element handler and pageObject's handler, and releases the Semaphore object needed to control the amount of driver's instances open in parallel.
	 */
	public static void clearDriver() {
		if (getDriver()!=null) {
			//Driver ending process should be done in this order: close then quit. This clears temporary files the proper way in parallel executions.
			webDriverThreadHolder.get().close();
			webDriverThreadHolder.get().quit();
			webDriverThreadHolder.remove();
			webHandler().removeThreadWaitObjects();
			pageObject().removeThreadPagesContainer();
		}
	}
	
	/**Returns the WebDriver object as a Javascript code executor.
	 * 
	 * @return WebDriver as a JavascriptExecutor.
	 */
	public static JavascriptExecutor driverAsJSExecutor() {
		return (JavascriptExecutor)webDriverThreadHolder.get();
	}
	
	/**Returns the WebDriver object stored in the ThreadLocal object.
	 * 
	 * @return A WebDriver object.
	 */
	public static WebDriver getDriver() {
		return webDriverThreadHolder.get();
	}
	
	/**Loads the contents of a file into a Properties object, allowing easier later access to it's contents. The file could be used to store configuration parameters for the tests. The file's path in this case is: "src/test/resources/config.properties" and its contents are arranged in a "Key-Value" manner.
	 */
	public static void loadSetupValues() {
		try {
			InputStream configFileInput = new FileInputStream("src/test/resources/config.properties");
			properties = new Properties();
			properties.load(configFileInput);
			configFileInput.close();
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}
	
	/**Returns a value contained in a Properties object by key.
	 * 
	 * @param value The Key parameter of the requested Value.
	 * @return The Value stored for the requested Key.
	 */
	public static String getSetupValue(String value) {
		return properties.getProperty(value);
	}

	/**Adds a string to Cucumber's execution report in the stage where it was called. Could be used to add logs to the report in all its stages like Before Test, Test Steps, After Test, etc. In order to work properly this class should be able to access Cucumber's Scenario.
	 * 
	 * @param textToLog String to log in the execution report.
	 */
	public static void addLogToCucumberExecutionReport(String textToLog) {
		AutoTool.scenarioHandler().getScenario().log(textToLog);
	}
	
	/**Adds a screenshot to Cucumber's execution report in the stage where it was called. Should be used to add screenshots to the test steps when required. In order to work properly this class should be able to access Cucumber's Scenario. This method will take the screenshot when called, and will not wait for the browser to fully load the page. If needed, it should be used in conjunction with the method waitWebPageLoad.
	 * 
	 * @param imageFileName The name that the image file will have in the report.
	 */
	public static void addScreenshotToCucumberExecutionReport(String imageFileName) {
		final byte[] screenshot = ((TakesScreenshot)AutoTool.getDriver()).getScreenshotAs(OutputType.BYTES);
		// addLogToCucumberExecutionReport("Attached Image Name: "+imageFileName);
		AutoTool.scenarioHandler().getScenario().attach(screenshot, "image/png", imageFileName);
	}
	
    /**
     * Used to upload document or image.
     *
     * @param filename
     * @param input
     */
    public static void fileUpload(String filename, WebElement input){
        File directory = new File(filename);
        input.sendKeys(directory.getAbsolutePath());
    }
	
	/** Allows access to the Cucumber's scenario handler that will control all scenario related tasks.
	 * 
	 * @return Cucumber's scenario handler.
	 */
	public static ScenarioHandler scenarioHandler() {
		return ScenarioHandler.getInstance();
	}

	/** Allows access to a web's elements handler that controls all page's element related tasks. Waits for elements, page loads, and webElement's interaction should be use from this method.
	 * 
	 * @return Web elements handler.
	 */
	public static WebElementHandler webHandler() {
		return WebElementHandler.getInstance();
	}
	
	/** Allows access to a pageObjects handler. All pageObject's interactions should be done thru this method.
	 * 
	 * @return Page Object handler.
	 */
	public static PageObjectHandler pageObject() {
		return PageObjectHandler.getInstance();
	}
	
	/** Allows access to a test values handler that controls secret and inside a test values. All test values interactions should happen thru this method.
	 * 
	 * @return Test Values handler.
	 */
	public static TestValuesHandler testValues() {
		return TestValuesHandler.getInstance();
	}
}