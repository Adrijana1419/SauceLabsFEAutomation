package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class Header {

    private WebDriver driver;
    Actions actions;
    WebDriverWait wait;


    private By yourCartButton = By.className("shopping_cart_link");

    private By burgerMenuButton = By.id("react-burger-menu-btn");
    private By burgerMenu = By.className("bm-menu-wrap");
    private By allItemsLink = By.id("inventory_sidebar_link");

    private By aboutLink = By.id("about_sidebar_link");
    private By logoutLink = By.id("logout_sidebar_link");
    private By resetAppStateLink = By.id("reset_sidebar_link");


    public Header(WebDriver driver) {

        this.driver = driver;
        this.actions = new Actions(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));


    }

    public String redirectingToYourCartPage() {

        driver.findElement(yourCartButton).click();
        return driver.getCurrentUrl();
    }

    public String getAboutLinkHref() {
        WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(burgerMenuButton));
        menuButton.click();

        WebElement about = wait.until(ExpectedConditions.visibilityOfElementLocated(aboutLink));
        return about.getAttribute("href");
    }

    public String clickAllItems() {
        WebElement menuButton =
                wait.until(ExpectedConditions.elementToBeClickable(burgerMenuButton));
        menuButton.click();

        WebElement allItems =
                wait.until(ExpectedConditions.presenceOfElementLocated(allItemsLink));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", allItems);

        wait.until(ExpectedConditions.urlContains("inventory.html"));

        return driver.getCurrentUrl();
    }


    public void resetAppState() {
            WebElement menuButton =
                    wait.until(ExpectedConditions.elementToBeClickable(burgerMenuButton));
            menuButton.click();

            WebElement resetLink =
                    wait.until(ExpectedConditions.presenceOfElementLocated(resetAppStateLink));

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", resetLink);

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    public void clickLogout() {
        WebElement menuButton =
                wait.until(ExpectedConditions.elementToBeClickable(burgerMenuButton));
        menuButton.click();

        WebElement logout =
                wait.until(ExpectedConditions.presenceOfElementLocated(logoutLink));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logout);

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}




















