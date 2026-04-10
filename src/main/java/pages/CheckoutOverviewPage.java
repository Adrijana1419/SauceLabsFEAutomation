package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class CheckoutOverviewPage {

    WebDriver driver;
    WebDriverWait wait;

    private By cancelButton = By.id("cancel");
    private By finishButton = By.id("finish");
    private By inventoryItemNames = By.className("inventory_item_name");
    private By checkoutOverviewTitleText  = By.xpath("//*[@id=\"header_container\"]/div[2]/span");

    public CheckoutOverviewPage(WebDriver driver) {
        this.driver = driver;

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getCheckoutOverviewTitleText(){

        return driver.findElement(checkoutOverviewTitleText).getText();
    }

    public void clickCancelButton(){

        driver.findElement(cancelButton).click();
    }
    public void clickFinishButton(){
        driver.findElement(finishButton).click();
    }

    public void clickProductTitle() {
        driver.findElement(inventoryItemNames).click();

    }



    public List<OverviewPageItem> getItems() {
        List<WebElement> InventoryElements = driver.findElements(By.className("cart_item"));
        List<OverviewPageItem> items = new ArrayList<>();

        for (WebElement element : InventoryElements) {
            String title = element.findElement(By.className("inventory_item_name")).getText().trim();
            String description = element.findElement(By.className("inventory_item_desc")).getText().trim();
            String price = element.findElement(By.className("inventory_item_price")).getText().trim();
            items.add(new OverviewPageItem(title, description, price));
        }

        return items;
    }

    public static class OverviewPageItem {
        public String title;
        public String description;
        public String price;

        public OverviewPageItem(String title, String description, String price) {
            this.title = title;
            this.description = description;
            this.price = price;
        }
    }

}
