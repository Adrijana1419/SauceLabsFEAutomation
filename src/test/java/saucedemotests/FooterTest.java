package saucedemotests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.Footer;
import pages.LoginPage;
import pages.ProductsPage;

import static org.junit.Assert.*;


public class FooterTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private ProductsPage productsPage;
    Footer footer;

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

        footer = new Footer(driver);
    }

    @Test
    public void twitterFooterRedirectionTest() {
        String redirectedUrl = footer.verifyFooterRedirection(By.className("social_twitter"));
        System.out.println("Twitter redirected to: " + redirectedUrl);

        assertTrue(redirectedUrl.contains("x.com/saucelabs"));
    }

    @Test
    public void facebookFooterRedirectionTest() {
        String redirectedUrl = footer.verifyFooterRedirection(By.className("social_facebook"));
        System.out.println("Facebook redirected to: " + redirectedUrl);

        assertTrue(redirectedUrl.contains("facebook.com"));
    }

    @Test
    public void linkedInFooterRedirectionTest() {
        String redirectedUrl = footer.verifyFooterRedirection(By.className("social_linkedin"));
        System.out.println("LinkedIn redirected to: " + redirectedUrl);

        assertTrue(redirectedUrl.contains("linkedin.com"));
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
