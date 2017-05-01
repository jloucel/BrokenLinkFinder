package com.brokenlinkfinder.logic;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import com.brokenlinkfinder.utils.DriverTypesEnum;
import com.brokenlinkfinder.utils.WebDriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static java.util.stream.Collectors.*;

/**
 * This class instantiates a web driver object to collect web elements
 * from a web page filters the elements for links with valid content
 * and then checks if the links are valid. Valid for this check is
 * defined as not 404 status codes Warning is returned for 401 and 403.
 *
 * @author Jason Loucel
 * @version 1.0
 * @since 2017-04-18
 */

public class LinkTest {

	/**
	 * This method takes a string representing a URL and passes
	 * it to a Selenium webdriver which is then used to capture
	 * all elements from a web page with either an "a" or "img"
	 * tag. The resulting list is then filtered to only include
	 * links containing a href component. The href text is then
	 * stripped out and returned as a list of strings.
	 *
	 * @param testURL {string=} testURL - a URL to test
	 * @return urlList {List<string>=} urlList - list of urls found
	 */
	public List<String> findAllLinks(String testURL){

		WebDriver driver = WebDriverFactory.getDriver(DriverTypesEnum.PHANTOMJS);
		driver.get(testURL);
		try{
		    driver.wait(5000);
        }catch (Exception exp){}

		List<WebElement> pageElements = driver.findElements(By.tagName("a"));
		pageElements.addAll(driver.findElements(By.tagName("img")));

		List<WebElement> filteredList = pageElements.stream()
                .filter(element ->
                    element.getAttribute("href") != null
                    && !element.getAttribute("href").contains("javascript")
                    && !element.getAttribute("href").contains("tel")
					&& !element.getAttribute("href").contains("mailto"))
				.collect(toCollection(ArrayList::new));


		List<String> urlList = new ArrayList<String>();

		for(WebElement e : filteredList){
		    urlList.add(e.getAttribute("href"));
		}

		List<String> uniqueURL = urlList.stream().distinct().collect(Collectors.toList());

        driver.quit();

		return uniqueURL;
	}

	/**
	 * Takes a string representing a URL, creates a connection to the
     * URL and captures the response code and message.
     * Returns a message indicating error
	 * @param testURL {string=} testURL - a URL to test
	 * @return response {string=} response - url validation response
	 * @throws Exception
	 */
	public String isLinkBroken(String testURL) throws Exception {
        URL url = new URL(testURL);
		String response;
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		try{
			conn.connect();
			int code = conn.getResponseCode();
			String rMessage = conn.getResponseMessage();
            conn.disconnect();

			if (code == 404) {
				response = "<font color=\"red\">ERROR: " + testURL + " returned " + rMessage + " " + code + "</font>";
			} else if ((code == 403) || (code == 401)) {
                response = "<font color=\"olive\">Warning: " + testURL + " returned " + rMessage + " " + code + "</font>";
            } else {
				response = "<font color=\"green\">" + testURL + " returned " + rMessage + " " + code + "</font>";
			}

			return response;

		} catch (Exception e){
			return "<font color=\"maroon\">Exception occurred checking: " + testURL + " " + e.getMessage() + "</font>";
		}
	}
}
