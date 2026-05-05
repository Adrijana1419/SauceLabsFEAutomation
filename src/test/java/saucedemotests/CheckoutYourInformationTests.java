package saucedemotests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CheckoutOverviewPage;
import pages.ProductsPage;
import pages.YourCartPage;
import pages.CheckoutYourInformationPage;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CheckoutYourInformationTests {

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
        productsPage.goToCart();

        cartPage = new YourCartPage(driver);
        cartPage.clickCheckoutAndRedirectToInformationPage();

        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/checkout-step-one.html"));
        checkoutPage = new CheckoutYourInformationPage(driver);

        checkoutOverviewPage = new CheckoutOverviewPage(driver);
    }

    @Test
    public void clickCancelReturnToYourCartTest() throws InterruptedException {
        Thread.sleep(5000);
        checkoutPage.clickCancelButton();

        assertEquals("Your Cart", cartPage.yourCartTitleText());
    }

    @Test
    public void continueRedirectToCheckoutOverviewPageTest() throws InterruptedException {
        checkoutPage.enterFirstName("test first name");
        checkoutPage.enterLastName("test last name");
        checkoutPage.enterPostalCode("1000");
        checkoutPage.clickContinue();
        Thread.sleep(5000);

        assertEquals("Checkout: Overview", checkoutOverviewPage.getCheckoutOverviewTitleText());

    }

    @Test
    public void missingFirstNameShowErrorTest() throws InterruptedException {
        checkoutPage.enterLastName("test last name");
        checkoutPage.enterPostalCode("1000");
        checkoutPage.clickContinue();

        assertEquals("Error: First Name is required", checkoutPage.getErrorMessageText());
    }

    @Test
    public void missingLastNameShowErrorTest() throws InterruptedException {
        checkoutPage.enterFirstName("test first name");
        checkoutPage.enterPostalCode("1000");
        checkoutPage.clickContinue();

        assertEquals("Error: Last Name is required", checkoutPage.getErrorMessageText());
    }

    @Test
    public void missingPostalCodeShowErrorTest() throws InterruptedException {
        checkoutPage.enterFirstName("test first name");
        checkoutPage.enterLastName("test last name");
        checkoutPage.clickContinue();

        assertEquals("Error: Postal Code is required", checkoutPage.getErrorMessageText());
    }

    @Test
    public void missingFirstNameAndLastNameShowErrorTest() throws InterruptedException {
        checkoutPage.enterPostalCode("1000");
        checkoutPage.clickContinue();

        assertEquals("Error: First Name is required", checkoutPage.getErrorMessageText());
    }

    @Test
    public void dismissErrorMessageRemovesWarningTest() throws InterruptedException {
        checkoutPage.enterLastName("test last name");
        checkoutPage.enterPostalCode("1000");
        checkoutPage.clickContinue();
        Thread.sleep(5000);
        checkoutPage.clickErrorButton();

        assertFalse(checkoutPage.isErrorMessageDisplayed());
    }

    @Test
    public void browserRefreshRemovesInputsTest() throws InterruptedException {
        checkoutPage.enterFirstName("test first name");
        checkoutPage.enterLastName("test last name");
        checkoutPage.enterPostalCode("1000");
        driver.navigate().refresh();
        Thread.sleep(5000);

        assertTrue(checkoutPage.isFirstNameFieldEmpty());
        assertTrue(checkoutPage.isLastNameFieldEmpty());
        assertTrue(checkoutPage.isPostalCodeFieldEmpty());
    }

    @Test
    public void clickOnRefreshButtonMakesErrorMessageDissapearsTest() throws InterruptedException {
        checkoutPage.enterLastName("test last name");
        checkoutPage.enterPostalCode("1000");
        checkoutPage.clickContinue();
        Thread.sleep(5000);
        driver.navigate().refresh();

        assertFalse(checkoutPage.isErrorMessageDisplayed());
    }

    @Test
    public void clickCancelAndBackToPageRemovesInputsTest() throws InterruptedException {
        checkoutPage.enterFirstName("test first name");
        checkoutPage.enterLastName("test last name");
        checkoutPage.enterPostalCode("1000");
        checkoutPage.clickCancelButton();
        Thread.sleep(5000);
        cartPage.clickCheckoutAndRedirectToInformationPage();

        assertTrue(checkoutPage.isFirstNameFieldEmpty());
        assertTrue(checkoutPage.isLastNameFieldEmpty());
        assertTrue(checkoutPage.isPostalCodeFieldEmpty());
    }

    @Test
    public void clickCancelAndBackToPageRemovesErrorMessageTest() throws InterruptedException {
        checkoutPage.clickContinue();
        checkoutPage.clickCancelButton();
        cartPage.clickCheckoutAndRedirectToInformationPage();

        assertFalse(checkoutPage.isErrorMessageDisplayed());
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
