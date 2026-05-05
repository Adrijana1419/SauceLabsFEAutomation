package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class YourCartPage {

    private WebDriver driver;
    Actions actions;
    WebDriverWait wait;

    private By backpackRemoveButton = By.id("remove-sauce-labs-backpack");
    private By yourCartTitle = By.className("title");

    public YourCartPage(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String continueShoppingRedirectToProductPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By continueShoppingButton = By.id("continue-shopping");

        wait.until(ExpectedConditions.visibilityOfElementLocated(continueShoppingButton));

        WebElement button = driver.findElement(continueShoppingButton);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

        wait.until(ExpectedConditions.urlContains("inventory"));

        return driver.getCurrentUrl();
    }

    public String clickCheckoutAndRedirectToInformationPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By checkoutButton = By.id("checkout");

        wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutButton));

        WebElement button = driver.findElement(checkoutButton);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

        wait.until(ExpectedConditions.urlContains("checkout-step-one"));

        return driver.getCurrentUrl();
    }

    public void removeBackpackFromCartIfPresent() {
        List<?> removeButtons = driver.findElements(backpackRemoveButton);
        if (!removeButtons.isEmpty()) {
            driver.findElement(backpackRemoveButton).click();
        }
    }

    public boolean isBackpackInCart() {
        return !driver.findElements(backpackRemoveButton).isEmpty();
    }

    public void removeProductFromCartIfPresent(String productId) {
        By removeButton = By.id("remove-" + productId);
        List<WebElement> buttons = driver.findElements(removeButton);
        if (!buttons.isEmpty()) {
            WebElement button = buttons.get(0);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
    }

    public boolean isProductInCart(String productId) {
        By removeButton = By.id("remove-" + productId);
        return !driver.findElements(removeButton).isEmpty();
    }

    public int getCartItemsCountInList() {

        return driver.findElements(By.cssSelector("button[id^='remove-']")).size();
    }

    public List<CartItem> getCartItems() {
        List<WebElement> cartElements = driver.findElements(By.className("cart_item"));
        List<CartItem> items = new ArrayList<>();

        for (WebElement element : cartElements) {
            String title = element.findElement(By.className("inventory_item_name")).getText().trim();
            String description = element.findElement(By.className("inventory_item_desc")).getText().trim();
            String price = element.findElement(By.className("inventory_item_price")).getText().trim();
            items.add(new CartItem(title, description, price));
        }

        return items;
    }

    public static class CartItem {
        public String title;
        public String description;
        public String price;

        public CartItem(String title, String description, String price) {
            this.title = title;
            this.description = description;
            this.price = price;
        }
    }

    public String hoverOverProductTitle(String productName) {
        WebElement titleElement = getProductTitleElement(productName);

        String beforeHoverColor = titleElement.getCssValue("color");

        actions.moveToElement(titleElement).perform();

        wait.until(driver -> {
            String newColor = titleElement.getCssValue("color");
            return !newColor.equals(beforeHoverColor);
        });

        return titleElement.getCssValue("color");
    }

    public String clickProductTitle(String productName) {
        WebElement titleElement = getProductTitleElement(productName);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", titleElement);
        titleElement.click();

        wait.until(ExpectedConditions.urlContains("inventory-item.html"));
        return driver.getCurrentUrl();
    }

    private WebElement getProductTitleElement(String productName) {
        return driver.findElement(By.xpath("//div[@class='inventory_item_name' and text()='" + productName + "']"));
    }

    public String yourCartTitleText() {

        return driver.findElement(yourCartTitle).getText();
    }

}
