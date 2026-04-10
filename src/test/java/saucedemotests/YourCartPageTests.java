package saucedemotests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CheckoutYourInformationPage;
import pages.ProductsPage;
import pages.YourCartPage;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.*;

public class YourCartPageTests {

    WebDriver driver;
    YourCartPage cartPage;
    ProductsPage productsPage;
    WebDriverWait wait;



    @Before
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");

        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.className("shopping_cart_link")).click();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        cartPage = new YourCartPage(driver);
        productsPage = new ProductsPage(driver);

        productsPage.goToCart();

    }


    @Test
    public void continueShoppingRedirectsToProductsPageTest() {
        productsPage.addProductToCartIfNotAdded("sauce-labs-backpack");
        productsPage.goToCart();

        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/cart.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("continue-shopping")));

        String actualUrl = cartPage.continueShoppingRedirectToProductPage();
        String expectedUrl = "https://www.saucedemo.com/inventory.html";
        assertEquals("Clicking Continue Shopping should redirect to Products page", expectedUrl, actualUrl);
    }


    @Test
    public void checkoutRedirectsToYourInformationPageTest() {
        productsPage.addProductToCartIfNotAdded("sauce-labs-backpack");

        productsPage.goToCart();

        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/cart.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkout")));

        String actualUrl = cartPage.clickCheckoutAndRedirectToInformationPage();

        String expectedUrl = "https://www.saucedemo.com/checkout-step-one.html";
        assertEquals("Clicking Checkout should redirect to Checkout: Your Information page", expectedUrl, actualUrl);
    }


    @Test
    public void removeSingleProductFromCartTest() {
        productsPage.addBackpackToCartIfNotAdded();

        productsPage.goToCart();

        cartPage.removeBackpackFromCartIfPresent();

        assertFalse("Sauce Labs Backpack should be removed from the cart",
                cartPage.isBackpackInCart());
    }

    @Test
    public void removeMultipleProductsFromCartTest() {
        productsPage.addProductToCartIfNotAdded("sauce-labs-backpack");
        productsPage.addProductToCartIfNotAdded("sauce-labs-bike-light");
        productsPage.addProductToCartIfNotAdded("sauce-labs-bolt-t-shirt");

        String badgeText = driver.findElement(By.className("shopping_cart_badge")).getText();
        System.out.println("Cart badge shows: " + badgeText);

        productsPage.goToCart();

        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/cart.html"));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("cart_item")));

        int initialCartCount = cartPage.getCartItemsCountInList();
        System.out.println("Initial cart count: " + initialCartCount);

        cartPage.removeProductFromCartIfPresent("sauce-labs-backpack");
        cartPage.removeProductFromCartIfPresent("sauce-labs-bike-light");

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("remove-sauce-labs-backpack")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("remove-sauce-labs-bike-light")));

        wait.until(driver -> cartPage.getCartItemsCountInList() == 1);

        assertFalse("Backpack should be removed", cartPage.isProductInCart("sauce-labs-backpack"));
        assertFalse("Bike Light should be removed", cartPage.isProductInCart("sauce-labs-bike-light"));
        assertTrue("Bolt T-shirt should remain", cartPage.isProductInCart("sauce-labs-bolt-t-shirt"));

        int remainingCartCount = cartPage.getCartItemsCountInList();
        assertEquals("Cart count should decrement by 2", initialCartCount - 2, remainingCartCount);
    }

    @Test
    public void verifyCartItemsDetailsAndOrderTest() {
        productsPage.addProductToCartIfNotAdded("sauce-labs-backpack");
        productsPage.addProductToCartIfNotAdded("sauce-labs-bike-light");
        productsPage.addProductToCartIfNotAdded("sauce-labs-bolt-t-shirt");

        productsPage.goToCart();

        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/cart.html"));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("cart_item")));

        List<YourCartPage.CartItem> cartItems = cartPage.getCartItems();

        assertEquals("Cart should contain 3 items", 3, cartItems.size());

        for (int i = 0; i < cartItems.size(); i++) {
            YourCartPage.CartItem item = cartItems.get(i);
            System.out.printf("%d. %s%n   Description: %s%n   Price: %s%n%n", i + 1, item.title, item.description, item.price);
        }


        assertEquals("Sauce Labs Backpack", cartItems.get(0).title);
        assertTrue(cartItems.get(0).description.contains("carry.allTheThings()"));
        assertEquals("$29.99", cartItems.get(0).price);

        assertEquals("Sauce Labs Bike Light", cartItems.get(1).title);
        assertTrue(cartItems.get(1).description.contains("A red light isn't the desired state in testing"));
        assertEquals("$9.99", cartItems.get(1).price);

        assertEquals("Sauce Labs Bolt T-Shirt", cartItems.get(2).title);
        assertTrue(cartItems.get(2).description.contains("Get your testing superhero on"));
        assertEquals("$15.99", cartItems.get(2).price);
    }

    @Test
    public void titleHoverAndClickRedirectsToProductDetailPageTest() {
        productsPage.addProductToCartIfNotAdded("sauce-labs-backpack");

        productsPage.goToCart();

        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/cart.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cart_item")));

        String hoverColor = cartPage.hoverOverProductTitle("Sauce Labs Backpack");
        System.out.println("Title color after hover: " + hoverColor);

        assertNotNull("Title color should change after hover", hoverColor);

        String actualUrl = cartPage.clickProductTitle("Sauce Labs Backpack");

        String expectedUrl = "https://www.saucedemo.com/inventory-item.html?id=4";
        assertEquals("Clicking the title should open the Backpack detail page", expectedUrl, actualUrl);
    }





    @After
    public void tearDown() {
        driver.quit();
    }
}
