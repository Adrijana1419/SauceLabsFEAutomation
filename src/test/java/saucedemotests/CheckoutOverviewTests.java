package saucedemotests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CheckoutOverviewPage;
import pages.CheckoutYourInformationPage;
import pages.ProductsPage;
import pages.YourCartPage;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CheckoutOverviewTests {

    WebDriver driver;
    WebDriverWait wait;
    ProductsPage productsPage;
    YourCartPage cartPage;
    CheckoutYourInformationPage checkoutPage;
    CheckoutOverviewPage checkoutOverviewPage;


    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);

        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("inventory.html"));

        productsPage = new ProductsPage(driver);

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        productsPage.addProductToCartIfNotAdded("sauce-labs-backpack");
        productsPage.addProductToCartIfNotAdded("sauce-labs-bike-light");
        productsPage.addProductToCartIfNotAdded("sauce-labs-bolt-t-shirt");
        productsPage.goToCart();

        cartPage = new YourCartPage(driver);
        cartPage.clickCheckoutAndRedirectToInformationPage();

        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/checkout-step-one.html"));
        checkoutPage = new CheckoutYourInformationPage(driver);

        checkoutPage.enterFirstName("test first name");
        checkoutPage.enterLastName("test last name");
        checkoutPage.enterPostalCode("1000");
        checkoutPage.clickContinue();
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/checkout-step-two.html"));
        checkoutOverviewPage = new CheckoutOverviewPage(driver);
    }

    @Test
    public void cancelButtonRedirectsToTheProductPageTest() {
        checkoutOverviewPage.clickCancelButton();
        assertEquals("Products", productsPage.productsTextDisplayed());
        assertTrue(productsPage.isProductsTextDisplayed());
    }

    @Test
    public void clickFinishRedirectsToTheCheckoutCompletePageTest() {
        checkoutOverviewPage.clickFinishButton();

        assertEquals("https://www.saucedemo.com/checkout-complete.html", driver.getCurrentUrl());
    }

    @Test
    public void clickingOnProductTitleRedirectsToProductDescriptionPageTest() {
        checkoutOverviewPage.clickProductTitle();
        String expectedUrl = "https://www.saucedemo.com/inventory-item.html?id=4";
        String currentUrl = driver.getCurrentUrl();

        assertEquals(expectedUrl, currentUrl);
    }

    @Test
    public void verifyProductsOnCheckoutOverviewPageTest() {
        List<CheckoutOverviewPage.OverviewPageItem> CheckoutOverviewPageItems = checkoutOverviewPage.getItems();
        assertEquals(3, CheckoutOverviewPageItems.size());

        for (int i = 0; i < CheckoutOverviewPageItems.size(); i++) {
            CheckoutOverviewPage.OverviewPageItem item = CheckoutOverviewPageItems.get(i);
            System.out.printf("%d. %s%n   Description: %s%n   Price: %s%n%n", i + 1, item.title, item.description, item.price);
        }

        assertEquals("Sauce Labs Backpack", CheckoutOverviewPageItems.get(0).title);
        assertTrue(CheckoutOverviewPageItems.get(0).description.contains("carry.allTheThings()"));
        assertEquals("$29.99", CheckoutOverviewPageItems.get(0).price);

        assertEquals("Sauce Labs Bike Light", CheckoutOverviewPageItems.get(1).title);
        assertTrue(CheckoutOverviewPageItems.get(1).description.contains("A red light isn't the desired state in testing"));
        assertEquals("$9.99", CheckoutOverviewPageItems.get(1).price);

        assertEquals("Sauce Labs Bolt T-Shirt", CheckoutOverviewPageItems.get(2).title);
        assertTrue(CheckoutOverviewPageItems.get(2).description.contains("Get your testing superhero on"));
        assertEquals("$15.99", CheckoutOverviewPageItems.get(2).price);
    }

    @Test
    public void verifySummaryInformationOnCheckoutOverviewPageTest() {

        CheckoutOverviewPage.SummaryInformation summary = checkoutOverviewPage.getSummaryInformation();

        System.out.printf(
                "Payment: %s%nShipping: %s%nSubtotal: %s%nTax: %s%nTotal: %s%n",
                summary.payment,
                summary.shipping,
                summary.subtotal,
                summary.tax,
                summary.total
        );

        assertEquals("SauceCard #31337", summary.payment);
        assertEquals("Free Pony Express Delivery!", summary.shipping);
        assertEquals("Item total: $55.97", summary.subtotal);
        assertEquals("Tax: $4.48", summary.tax);
        assertEquals("Total: $60.45", summary.total);
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}