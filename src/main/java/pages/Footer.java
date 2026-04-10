package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Footer {

    WebDriver driver;
    WebDriverWait wait;



    public Footer(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }


    public String verifyFooterRedirection(By linkLocator) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");

        WebElement link = driver.findElement(linkLocator);
        link.click();

        String originalWindow = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }

        String redirectedUrl = driver.getCurrentUrl();

        driver.close();
        driver.switchTo().window(originalWindow);

        return redirectedUrl;
    }

}






