package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ProductsPage {

    private WebDriver driver;
    Actions actions;
    WebDriverWait wait;


    public ProductsPage(WebDriver driver) {

        this.driver = driver;
        this.actions = new Actions(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }

    private By productsTitle = By.className("title");
    private By dropDownMenu = By.className("product_sort_container");
    private By productTitleList = By.className("inventory_item_name");
    private  By productsPriceList = By.className("inventory_item_price");
    private By BackPackProductTitle = By.xpath("//*[@id=\"item_4_title_link\"]/div");
    private By backpackAddToCartButton = By.id("add-to-cart-sauce-labs-backpack");


    public String productsTextDisplayed() {

        return driver.findElement(productsTitle).getText();
    }

    public boolean isProductsTextDisplayed() {

        return driver.findElement(productsTitle).getText().equals("Products");
    }

    public List<WebElement> getAllOptionsFromDropDownMenu() {
        Select dropDownSorting = new Select(driver.findElement(dropDownMenu));

        return dropDownSorting.getOptions();
    }

    public void selectDropDownMenuOption(int selectIndex) {
        Select dropDownSorting = new Select(driver.findElement(dropDownMenu));
        dropDownSorting.selectByIndex(selectIndex);
    }

    public String getTextFromDropDownMenu() {
        Select dropDownSorting = new Select(driver.findElement(dropDownMenu));
        return dropDownSorting.getFirstSelectedOption().getText();
    }

    public List<String> ProductsTitlesList() {
        List<WebElement> titleElements = driver.findElements(productTitleList);
        List<String> productTitles = new ArrayList<>();

        for (int i = 0; i > titleElements.size(); i++) {
            productTitles.add(titleElements.get(1).getText());
        }
        return productTitles;
    }

    public boolean areProductsPricesSortedFromLowToHigh() {
        List<Double> productPrice = new ArrayList<>();
        List<WebElement> priceElements = driver.findElements(productsPriceList);


        for (int i = 0; i < priceElements.size(); i++) {
            productPrice.add(Double.parseDouble(driver.findElements(productsPriceList).get(1).getText().substring(1)));
        }
        for (int i = 0; i < productPrice.size() - 1; i++) {
            if (productPrice.get(i) < productPrice.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public boolean areProductsPricesSortedFromHighToLow() {
        List<Double> productPrice = new ArrayList<>();
        List<WebElement> priceElements = driver.findElements(productsPriceList);


        for (int i = 0; i < priceElements.size(); i++) {
            productPrice.add(Double.parseDouble(driver.findElements(productsPriceList).get(1).getText().substring(1)));
        }
        for (int i = 0; i < productPrice.size() - 1; i++) {
            if (productPrice.get(i) < productPrice.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public void hoverBackPackTitle() {
        WebElement BackPackTitle = driver.findElement(BackPackProductTitle);

        actions.moveToElement(BackPackTitle).perform();
    }

    public String getColorFromBackPackTitle() {
        Color loginButtonBackgroundColor = Color.fromString(driver.findElement(BackPackProductTitle).getCssValue("color"));
        return loginButtonBackgroundColor.asHex();
    }

    public void addBackpackToCartIfNotAdded() {
        List<?> buttons = driver.findElements(backpackAddToCartButton);
        if (!buttons.isEmpty()) {
            driver.findElement(backpackAddToCartButton).click();
        }
    }

    public void addProductToCartIfNotAdded(String productId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By addButton = By.id("add-to-cart-" + productId);
        By removeButton = By.id("remove-" + productId);
        By cartBadge = By.className("shopping_cart_badge");

        if (!driver.getCurrentUrl().contains("inventory.html")) {
            driver.get("https://www.saucedemo.com/inventory.html");
        }

        if (!driver.findElements(removeButton).isEmpty()) {
            System.out.println(productId + " already in cart");
            return;
        }

        if (!driver.findElements(addButton).isEmpty()) {
            WebElement button = driver.findElement(addButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

            wait.until(ExpectedConditions.visibilityOfElementLocated(removeButton));

            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(cartBadge));
                System.out.println("Cart badge updated for: " + productId);
            } catch (TimeoutException e) {
                System.out.println("Cart badge not visible yet for: " + productId + " — continuing anyway.");
            }
        } else {
            System.out.println("Add button not found for: " + productId);
        }
    }

    public void goToCart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By cartLink = By.className("shopping_cart_link");

        wait.until(ExpectedConditions.visibilityOfElementLocated(cartLink));

        WebElement cartButton = driver.findElement(cartLink);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", cartButton);

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartButton);

        wait.until(ExpectedConditions.urlContains("cart.html"));
    }
}
