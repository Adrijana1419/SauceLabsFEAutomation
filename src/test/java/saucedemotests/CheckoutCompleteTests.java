package saucedemotests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CheckoutCompleteTests {

    WebDriver driver;
    WebDriverWait wait;
    ProductsPage productsPage;
    YourCartPage cartPage;
    CheckoutYourInformationPage checkoutPage;
    CheckoutOverviewPage checkoutOverviewPage;
    CheckoutCompletePage checkoutCompletePage;


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
        checkoutOverviewPage.clickFinishButton();
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/checkout-complete.html"));
        checkoutCompletePage = new CheckoutCompletePage(driver);
    }

    @Test
    public void finalMessageVerificationTest() throws InterruptedException {
        System.out.println("ThankYouMessage - " + checkoutCompletePage.getThankYouMessageText() + " " +
                checkoutCompletePage.getDispatchingMessageText());

        assertEquals("Thank you for your order!", checkoutCompletePage.getThankYouMessageText());
        assertEquals("Your order has been dispatched, and will arrive just as fast as the pony can get there!",
                checkoutCompletePage.getDispatchingMessageText());
    }

    @Test
    public void clickingBackHomeButtonRedirectsToProductsPageTest() throws InterruptedException {
        checkoutCompletePage.clickBackHomeButton();
        String expectedUrl = "https://www.saucedemo.com/inventory.html";
        String currentUrl = driver.getCurrentUrl();

        assertEquals(expectedUrl, currentUrl);
        assertTrue(String.valueOf(true), checkoutCompletePage.isShoppingCartBadgeEmpty());

    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
