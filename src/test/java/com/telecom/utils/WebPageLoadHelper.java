package com.telecom.utils;
/**Adds more ways to wait for a page to load than Selenium already have. It's a helper class for AutoTool, implemented because there is a lot of ways to detect if a page already end the load process, and the ones that Selenium brings didn't work with some of Telecom's webpages.
 * @author Marcelo Luna.
 * @version 1.0
 * @since 1.0
 */

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;

final class WebPageLoadHelper {
	
	private final int waitPageLoadTimeOut = Integer.valueOf(AutoTool.getSetupValue("waitPageLoadTimeOut"));

    /** Waits the previous page to be flag as stale by the browser.
     *
     * @param old_page The page that you are leaving.
     * Ex: WebElement old_page = AutoTool.getDriver().findElement(By.tagName("html"));
     *
     * @param wait A Wait<WebDriver> Object
     */
    protected void waitHTMLStaleness(WebElement old_page, Wait<WebDriver> wait) {
        wait.until(ExpectedConditions.stalenessOf(old_page));
    }

    /**Waits until the Document interface returns its readystate as "complete" by queering thru javascript. In theory Selenium already apply this validation, but in practice it doesn't seem so.
     *
     * @param wait A Wait<WebDriver> Object
     */
    protected void waitReadyState(Wait<WebDriver> wait) {
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
    }

	/**Waits that in between a lapse of five seconds the Html code of the page doesn't change anymore or it ends the waiting period after the top value set in 'waitPageLoadTimeOut' in the config file (plus 0 to 30 seconds top), whichever happens first. It's a rustic method, but effective never the less.
	 * 
	 * @param js Selenium driver as javascript executor.
	 */
    protected void waitHTMLStopChanging(JavascriptExecutor js) {
        try {
            waitDOMAvailable(js);
            int referenceValue = getHTMLLenght(js);
            int innerHTMLLenght;
            int earlyExitCondition = 0;
            for (int i = 0; i < waitPageLoadTimeOut; i++) {
                innerHTMLLenght = getHTMLLenght(js);
                if(referenceValue==innerHTMLLenght) {
                    earlyExitCondition++;
                }else {
                    referenceValue=innerHTMLLenght;
                    earlyExitCondition=0;
                }
                if (earlyExitCondition==5) {
                    break;
                }
                Thread.sleep(1000);
            }

        } catch (Exception exp) {
            System.out.println(exp.getCause());
            System.out.println(exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**Gets the web page HTML code length as an int.
     *
     * @param js Selenium driver as javascript executor.
     * Ex: JavascriptExecutor js = (JavascriptExecutor)AutoTool.getDriver();
     *
     * @return Int that represents the actual page HTML code length.
     */
    private int getHTMLLenght(JavascriptExecutor js) {
        return Math.toIntExact((long) js.executeScript("return document.documentElement.innerHTML.length;"));
    }

    /**Waits until the Document interface is available and ready to be query. If it's not ready after 30 seconds, the case should fail as the browser did'nt even begin to load the page in that time.
     *
     * @param js Selenium driver as javascript executor.
     * Ex: JavascriptExecutor js = (JavascriptExecutor)AutoTool.getDriver();
     */
    private void waitDOMAvailable(JavascriptExecutor js) {
        boolean isDOMReady = false;
        try {
            for (int i = 0; i < 30; i++) {
                if (((WebElement)js.executeScript("return document.documentElement;"))!=null) {
                    isDOMReady = true;
                    break;
                }
                Thread.sleep(1000);
            }
            Assertions.assertTrue(isDOMReady, "The browser can't load the request web page.");

        } catch (Exception exp) {
            System.out.println(exp.getCause());
            System.out.println(exp.getMessage());
            exp.printStackTrace();
        }
    }
}