package saucedemotests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.LoginPage;
import pages.ProductsPage;
import pages.YourCartPage;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProductPageTests {

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

        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
    }

    @Test
    public void dropDownMenuOptionsTest(){

        assertEquals("Name (A to Z)", productsPage.getAllOptionsFromDropDownMenu().get(0).getText());
        assertEquals("Name (Z to A)", productsPage.getAllOptionsFromDropDownMenu().get(1).getText());
        assertEquals("Price (low to high)", productsPage.getAllOptionsFromDropDownMenu().get(2).getText());
        assertEquals("Price (high to low)", productsPage.getAllOptionsFromDropDownMenu().get(3).getText());

    }
    @Test
    public void sortingProductsByTitleAtoZTest(){

        List<String> initialTitlesList = productsPage.ProductsTitlesList();

        productsPage.selectDropDownMenuOption(1);

        List<String> TitleListReversedZtoA = productsPage.ProductsTitlesList();


        assertEquals(initialTitlesList.reversed(), TitleListReversedZtoA);

    }
    @Test
    public void sortingProductsByTitleZtoATest(){
        productsPage.selectDropDownMenuOption(1);

        assertEquals("Name (Z to A)",productsPage.getTextFromDropDownMenu());

    }
    @Test
    public void sortingProductsByPriceLowToHighTest(){
        productsPage.selectDropDownMenuOption(2);

        assertEquals("Price (low to high)",productsPage.getTextFromDropDownMenu());
        assertTrue(productsPage.areProductsPricesSortedFromLowToHigh());

    }
    @Test
    public void sortingProductsByPriceHighToLowTest(){
        productsPage.selectDropDownMenuOption(3);

        assertEquals("Price (high to low)",productsPage.getTextFromDropDownMenu());
        assertTrue(productsPage.areProductsPricesSortedFromHighToLow());
    }

    @Test
    public void colorChangeHoverOnTitleTest() throws InterruptedException{

        assertEquals("#18583a",productsPage.getColorFromBackPackTitle());

        productsPage.hoverBackPackTitle();



        assertEquals("#3ddc91", productsPage.getColorFromBackPackTitle());
    }





    @After
    public void tearDown(){

    driver.quit();
    }

}
