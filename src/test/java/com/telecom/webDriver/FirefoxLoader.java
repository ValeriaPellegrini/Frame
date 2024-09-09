package com.telecom.webDriver;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.telecom.utils.AutoTool;
import com.telecom.utils.LogHandler;

public class FirefoxLoader implements DriverLoaderInterface {

    public WebDriver loadDriver() {

        String driverPath = AutoTool.getFACRunningPath()+"//Drivers//geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", driverPath);

        FirefoxOptions options = new FirefoxOptions();

        if (AutoTool.getSetupValue("writeTxtLogs").equals("true")&&AutoTool.getSetupValue("writeDriverLogs").equals("true")) {
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, LogHandler.getDriverLogAbsolutePath());
            options.setLogLevel(FirefoxDriverLogLevel.DEBUG);
        }
        
		String userName = System.getProperty("user.name");
		if (userName.startsWith("x")) {
			String proxyValues = AutoTool.getSetupValue("virtualProxy");
			Proxy proxyCFG = new Proxy();
			//Adding the desired host and port for the http, ssl, and ftp Proxy Servers respectively
			proxyCFG.setHttpProxy("<"+proxyValues+">");
			proxyCFG.setSslProxy("<"+proxyValues+">");
			proxyCFG.setSslProxy("<"+proxyValues+">");
			proxyCFG.setFtpProxy("<"+proxyValues+">");
			options.setCapability("proxy", proxyCFG);
		}
		
        //options.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        if (AutoTool.getSetupValue("driverHeadless").equals("true")) {
            options.addArguments("--headless");
        }

        return new FirefoxDriver(options);
    }
}