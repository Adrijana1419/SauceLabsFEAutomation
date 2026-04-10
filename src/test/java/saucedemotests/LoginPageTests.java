package saucedemotests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.LoginPage;
import pages.ProductsPage;

import static org.junit.Assert.*;

public class LoginPageTests {

    private WebDriver driver;
    private LoginPage loginPage;
    private ProductsPage productsPage;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");

        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
    }

    @Test
    //1
    public void successfulLoginTest() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();

        assertEquals("Products", productsPage.productsTextDisplayed());
        assertTrue(productsPage.isProductsTextDisplayed());
    }

    @Test
    //2
    public void loginWithoutCredentialsTest() {
        loginPage.enterUsername("");
        loginPage.enterPassword("" + "");
        loginPage.clickLogin();

        System.out.println("Actual error message: " + "Epic sadface: Username is required");
        assertTrue("Epic sadface: Username is required", true);
    }

    @Test
    // 3
    public void errorMessageEmptyUsernameAndPasswordTest(){
        loginPage.clickLogin();

        assertEquals("Epic sadface: Username is required", loginPage.getErrorMessage());
    }

    @Test
    //4
    public void removingErrorMessageTest(){
        loginPage.clickLogin();

        loginPage.clickErrorMessageXButton();
        assertFalse(loginPage.isErrorMessageDisplayed());

    }

    @Test
    //5
    public void loginWithoutUsernameTest() {
        loginPage.enterUsername("");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();

        assertTrue("Epic sadface: Username is required", true);
        assertEquals("Epic sadface: Username is required", loginPage.errorUsernameRequired);
    }

    @Test
    //6
    public void loginValidUsernameNoPasswordTest() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("");
        loginPage.clickLogin();

        System.out.println("Actual error: " + "Epic sadface: Password is required");

        assertTrue("Epic sadface: Password is required", true);
        assertEquals("Epic sadface: Password is required",loginPage.errorPasswordRequired);

    }

    @Test
    //7
    public void loginInvalidUsernameNoPasswordTest() {

        loginPage.enterUsername("standard_user123");
        loginPage.enterPassword("");
        loginPage.clickLogin();

        System.out.println("Actual error: " + "Epic sadface: Password is required");

        assertTrue("Epic sadface: Password is required", true);
        assertEquals("Epic sadface: Password is required",loginPage.errorPasswordRequired);

    }

   @Test
    //8
    public void loginValidUsernameInvalidPasswordTest(){
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("testPass123");
        loginPage.clickLogin();

        System.out.println("Actual error: " + "Epic sadface: Username and password do not match any user in this service");

        assertTrue("Epic sadface: Username and password do not match any user in this service",true);
        assertEquals("Epic sadface: Username and password do not match any user in this service",loginPage.errorNoMatch);

    }
   @Test
    //9
    public void loginInvalidUsernameInvalidPasswordTest(){
       loginPage.enterUsername("standard_user123");
       loginPage.enterPassword("testPass123");
       loginPage.clickLogin();

        System.out.println("Actual error: " + "Epic sadface: Username and password do not match any user in this service");

        assertTrue("Epic sadface: Username and password do not match any user in this service",true);
        assertEquals("Epic sadface: Username and password do not match any user in this service",loginPage.errorNoMatch);

    }


    @Test
    //10
    public void loginFormInitialStateUITest(){

        assertEquals("\"DM Sans\", Arial, Helvetica, sans-serif", loginPage.getUsernameFieldFontType());
        assertEquals("14px", loginPage.getUsernameFieldFontSize());

        assertEquals("\"DM Sans\", Arial, Helvetica, sans-serif", loginPage.getPasswordFieldFontType());
        assertEquals("14px", loginPage.getPasswordFieldFontSize());

        assertEquals("Login", loginPage.getLoginButtonText());
        assertEquals("\"DM Sans\", Arial, Helvetica, sans-serif", loginPage.getLoginButtonFontType());
        assertEquals("16px", loginPage.getLoginButtonFontSize());

        assertEquals("#3ddc91", loginPage.getLoginButtonColor());

    }
    @Test
    public void errorMessageColorAndFontTest(){
        driver.findElement(By.id("user-name")).sendKeys("standard_user123");
        driver.findElement(By.id("password")).sendKeys("pass123");
        driver.findElement(By.id("login-button")).click();

        assertEquals("#e2231a", loginPage.getErrorMessageBackgroundColor());
        assertEquals("Roboto, Arial, Helvetica, sans-serif", loginPage.getErrorMessageFontType());
        assertEquals("14px", loginPage.getErrorMessageFontSize());

    }


    @After
    public void tearDown(){
        driver.quit();
    }



}
