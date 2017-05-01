package com.brokenlinkfinder.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverFactory {
	
	public static WebDriver getDriver(DriverTypesEnum requestedDriver) {

		String driverPath;
		WebDriver driver;

		switch (requestedDriver) {

			case PHANTOMJS:
				driverPath = "./Driver_Utils/phantomjs.exe";
				System.setProperty("webdriver.phantomjs.driver", driverPath);
				DesiredCapabilities caps = new DesiredCapabilities();
				caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,driverPath);
				driver = new PhantomJSDriver(caps);
				break;

			default:
				driver = null;
				break;
		}

		return driver;
	}
}
