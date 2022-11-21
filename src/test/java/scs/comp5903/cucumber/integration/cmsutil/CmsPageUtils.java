package scs.comp5903.cucumber.integration.cmsutil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author CX无敌
 * @date 2022-06-03
 */
public class CmsPageUtils {

  public static void getToPageAndLogin(WebDriver driver, String url, String username, String password) {
    driver.get(url);
    CmsPageUtils.login(driver, username, password);
  }

  private static void login(WebDriver driver, String username, String password) {
    driver.findElement(By.id("username")).sendKeys(username);
    driver.findElement(By.id("password")).sendKeys(password);
    driver.findElement(By.cssSelector("#login > form > div.submit-container.form-actions > button")).click();
  }
}
