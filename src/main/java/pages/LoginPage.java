package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.time.Duration;


public class LoginPage {

    private WebDriver driver;

    public LoginPage(WebDriver driver) {

        this.driver = driver;
    }

    public static final String errorPasswordRequired = "Epic sadface: Password is required";
    public static String errorUsernameRequired = "Epic sadface: Username is required";
    public static String errorNoMatch = "Epic sadface: Username and password do not match any user in this service";

    private By usernameField = By.id("user-name");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login-button");
    private By errorMessage = By.xpath("//*[@id=\"login_button_container\"]/div/form/div[3]/h3");
    private By errorMessageXButton = By.className("error-button");
    private By errorMessageContainer = By.cssSelector("div.error-message-container.error");


    public void enterUsername(String value) {

        driver.findElement(usernameField).sendKeys(value);
    }

    public void enterPassword(String value) {

        driver.findElement(passwordField).sendKeys(value);
    }

    public void clickLogin() {

        driver.findElement(loginButton).click();
    }

    public String getUsernameFieldFontType() {
        return driver.findElement(usernameField).getCssValue("font-family");
    }

    public String getUsernameFieldFontSize() {
        return driver.findElement(usernameField).getCssValue("font-size");
    }

    public String getPasswordFieldFontType() {
        return driver.findElement(passwordField).getCssValue("font-family");
    }

    public String getPasswordFieldFontSize() {
        return driver.findElement(passwordField).getCssValue("font-size");
    }

    public String getLoginButtonText() {
        return driver.findElement(loginButton).getAttribute("value");
    }

    public String getLoginButtonFontType() {
        return driver.findElement(loginButton).getCssValue("font-family");
    }

    public String getLoginButtonFontSize() {
        return driver.findElement(loginButton).getCssValue("font-size");
    }

    public String getLoginButtonColor() {
        Color loginButtonBackgroundColor = Color.fromString(driver.findElement(loginButton).getCssValue("background-color"));
        return loginButtonBackgroundColor.asHex();
    }

    public String getErrorMessageBackgroundColor() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement errorBox = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageContainer));

        Color bgColor = Color.fromString(errorBox.getCssValue("background-color"));
        return bgColor.asHex();
    }

    public String getErrorMessageFontType() {
        return driver.findElement(errorMessage).getCssValue("font-family");

    }

    public String getErrorMessageFontSize() {
        return driver.findElement(errorMessage).getCssValue("font-size");
    }

    public String getErrorMessage() {

        return driver.findElement(errorMessage).getText();
    }

    public void clickErrorMessageXButton() {

        driver.findElement(errorMessageXButton).click();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            driver.findElement(errorMessage).getText();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }


}







