package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutYourInformationPage {

    private WebDriver driver;
    Actions actions;

    WebDriverWait wait;

    private By cancelButton = By.id("cancel");
    private By firstNameField = By.id("first-name");
    private By lastNameField = By.id("last-name");
    private By postalCodeField = By.id("postal-code");
    private By continueButton = By.id("continue"); 
    private By errorMessage = By.xpath("//*[@id=\"checkout_info_container\"]/div/form/div[1]/div[4]/h3");
    private By errorButton = By.className("error-button");




    public CheckoutYourInformationPage(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void enterFirstName(String value){

        driver.findElement(firstNameField).sendKeys(value);
    }
    public void enterLastName(String value){

        driver.findElement(lastNameField).sendKeys(value);
    }
    public void enterPostalCode(String value){

        driver.findElement(postalCodeField).sendKeys(value);
    }
    public void clickContinue(){

        driver.findElement(continueButton).click();
    }
    public void clickCancelButton(){

        driver.findElement(cancelButton).click();
    }

    public String getErrorMessageText(){
        return driver.findElement(errorMessage).getText();
    }

    public void clickErrorButton(){

        driver.findElement(errorButton).click();
    }

    public boolean isErrorMessageDisplayed(){
        try {
            driver.findElement(errorMessage).getText();
            return true;
        } catch (NoSuchElementException e){
            return false;
        }
    }

    public boolean isFirstNameFieldEmpty(){
        try {
            driver.findElement(firstNameField).getText();
            return true;
        } catch (NoSuchElementException e){
            return false;
        }
    }

    public boolean isLastNameFieldEmpty(){
        try {
            driver.findElement(lastNameField).getText();
            return true;
        } catch (NoSuchElementException e){
            return false;
        }
    }

    public boolean isPostalCodeFieldEmpty(){
        try {
            driver.findElement(postalCodeField).getText();
            return true;
        } catch (NoSuchElementException e){
            return false;
        }
    }

}
