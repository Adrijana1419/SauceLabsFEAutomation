package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutCompletePage {
    WebDriver driver;
    WebDriverWait wait;

    private By ThankYouMessageText = By.className("complete-header");
    private By DispatchingMessageText = By.className("complete-text");
    private By BackHomeButton = By.id("back-to-products");
    private By ShoppingCartBadge = By.className("shopping_cart_badge");

    public CheckoutCompletePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getThankYouMessageText() {
        return driver.findElement(ThankYouMessageText).getText();
    }

    public String getDispatchingMessageText() {
        return driver.findElement(DispatchingMessageText).getText();
    }

    public void clickBackHomeButton() {
        driver.findElement(BackHomeButton).click();
    }

    public boolean isShoppingCartBadgeEmpty() {
        return driver.findElements(ShoppingCartBadge).isEmpty();
    }
}
