package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class CheckoutOverviewPage {

    static WebDriver driver;
    WebDriverWait wait;

    private By cancelButton = By.id("cancel");
    private By finishButton = By.id("finish");
    private By inventoryItemNames = By.className("inventory_item_name");
    private By checkoutOverviewTitleText = By.xpath("//*[@id=\"header_container\"]/div[2]/span");


    public CheckoutOverviewPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getCheckoutOverviewTitleText() {
        return driver.findElement(checkoutOverviewTitleText).getText();
    }

    public void clickCancelButton() {
        driver.findElement(cancelButton).click();
    }

    public void clickFinishButton() {
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

    public static class SummaryInformation {
        public String payment;
        public String shipping;
        public String subtotal;
        public String tax;
        public String total;

        public SummaryInformation(String payment, String shipping, String subtotal, String tax, String total) {
            this.payment = payment;
            this.shipping = shipping;
            this.subtotal = subtotal;
            this.tax = tax;
            this.total = total;
        }
    }

    public SummaryInformation getSummaryInformation() {
        WebElement container = driver.findElement(By.className("summary_info"));

        List<WebElement> values = container.findElements(By.className("summary_value_label"));

        String payment = values.get(0).getText().trim();
        String shipping = values.get(1).getText().trim();

        String subtotal = container.findElement(By.className("summary_subtotal_label")).getText().trim();
        String tax = container.findElement(By.className("summary_tax_label")).getText().trim();
        String total = container.findElement(By.className("summary_total_label")).getText().trim();

        return new SummaryInformation(payment, shipping, subtotal, tax, total);
    }







    }
