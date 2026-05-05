package saucedemotests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.Header;
import pages.LoginPage;
import pages.ProductsPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.By.id;


public class HeaderTests {
    private WebDriver driver;
    private LoginPage loginPage;
    private ProductsPage productsPage;

    Header header;

    @Before
    public void setUp() throws InterruptedException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");

        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);

        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();

        header = new Header(driver);
    }

    @Test
    public void yourCartIconTest() throws InterruptedException {
        driver.findElement(By.className("shopping_cart_link")).click();

        String actualURL = driver.getCurrentUrl();
        System.out.println(actualURL);

        assertEquals("https://www.saucedemo.com/cart.html", actualURL);
    }

    @Test
    public void openSidePanelMenuTest() {
        driver.findElement(id("react-burger-menu-btn")).click();
        WebElement sidePanelMenu = driver.findElement(By.className("bm-menu-wrap"));
        System.out.println(sidePanelMenu);

        assertTrue("bm-menu-wrap", sidePanelMenu.isDisplayed());
    }

    @Test
    public void aboutLinkHrefTest() {
        String href = header.getAboutLinkHref();

        assertEquals("https://saucelabs.com/", href);
    }

    @Test
    public void clickAllItemsRedirectToProductPageTest() {
        driver.findElement(By.className("shopping_cart_link")).click();
        String currentUrl = header.clickAllItems();

        assertEquals("Clicking 'All Items' should redirect to the Products page.",
                "https://www.saucedemo.com/inventory.html",
                currentUrl);
    }

    @Test
    public void resetAppStateTest() {
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        assertTrue(driver.findElement(By.className("shopping_cart_badge")).isDisplayed());

        header.resetAppState();

        assertTrue(driver.findElements(By.className("shopping_cart_badge")).isEmpty());
    }

    @Test
    public void logoutButtonTest() {
        header.clickLogout();

        assertEquals("https://www.saucedemo.com/", driver.getCurrentUrl());
    }

    @After
    public void tearDown() {

        driver.quit();
    }
}
