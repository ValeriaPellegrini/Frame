package com.telecom.webDriver;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

public class DriverFactory {

	public static WebDriver createDriver(String driverType) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("chrome", "com.telecom.webDriver.ChromeLoader");
		map.put("firefox", "com.telecom.webDriver.FirefoxLoader");
		map.put("edge", "com.telecom.webDriver.EdgeLoader");

		try {
			@SuppressWarnings("unchecked")
			Class<DriverLoaderInterface> theClass = (Class<DriverLoaderInterface>)Class.forName(map.get((driverType.toLowerCase())));
			DriverLoaderInterface driverLoader = theClass.getDeclaredConstructor().newInstance();

			return driverLoader.loadDriver();
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
		return null;
	}
}