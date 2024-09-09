package com.telecom.webDriver;

import java.util.logging.Level;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;

import com.telecom.utils.AutoTool;
import com.telecom.utils.LogHandler;

public class ChromeLoader implements DriverLoaderInterface {

    public WebDriver loadDriver() {

        String driverPath = AutoTool.getFACRunningPath()+"//Drivers//chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", driverPath);

        ChromeOptions options = new ChromeOptions();

        if (AutoTool.getSetupValue("writeTxtLogs").equals("true")&&AutoTool.getSetupValue("writeDriverLogs").equals("true")) {
            System.setProperty("webdriver.chrome.logfile", LogHandler.getDriverLogAbsolutePath());

            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.DRIVER, Level.INFO);

            options.setCapability("goog:loggingPrefs", logPrefs);
        }
        options.addArguments("start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("ignore-certificate-errors");
        //options.addArguments("user-data-dir=\\selenium");
        options.addArguments("--remote-allow-origins=*"); //Para que funcione las conexiones remotas, sino se bloquea y recibe 403


        String userName = System.getProperty("user.name");
		/*if (userName.startsWith("x")) {
			String proxyValues = AutoTool.getSetupValue("virtualProxy");
			options.addArguments("--proxy-server="+proxyValues);
		}*/
        
        if (AutoTool.getSetupValue("driverHeadless").equals("true")) {
            options.addArguments("window-size=1366,768");
            options.addArguments("--headless");
        }

        return new ChromeDriver(options);
    }
}