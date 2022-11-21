package scs.comp5903.cucumber.integration.cmsutil;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

/**
 * @author CX无敌
 * @date 2022-06-01
 */
public class SeleniumFactory {

  static {
    WebDriverManager.chromedriver().setup();
  }

  public static WebDriver getDriver() {
    var chromeDriver = new ChromeDriver();
    chromeDriver.manage().timeouts().implicitlyWait(Duration.ofMinutes(10));
    return chromeDriver;
  }
}
