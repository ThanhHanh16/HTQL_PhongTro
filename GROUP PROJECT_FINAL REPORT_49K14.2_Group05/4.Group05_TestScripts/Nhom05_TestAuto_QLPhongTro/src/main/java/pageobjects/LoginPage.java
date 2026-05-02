package pageobjects;

import common.Constant;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private final WebDriver driver;

    // Locator đúng theo file Dang_nhap.html
    private final By txtUsername = By.id("log-username");
    private final By txtPassword = By.id("log-password");
    private final By btnLogin = By.cssSelector("button.btn-login");

    private final By lblUsernameError = By.id("username-error");
    private final By lblPasswordError = By.id("password-error");
    private final By lblFormError = By.id("form-error");

    public LoginPage() {
        this.driver = Constant.WEBDRIVER;
    }

    public void open() {
        driver.get(Constant.LOGIN_URL);
    }

    public void enterUsername(String username) {
        driver.findElement(txtUsername).clear();
        driver.findElement(txtUsername).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(txtPassword).clear();
        driver.findElement(txtPassword).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(btnLogin).click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public String getUsernameErrorMessage() {
        return driver.findElement(lblUsernameError).getText().trim();
    }

    public String getPasswordErrorMessage() {
        return driver.findElement(lblPasswordError).getText().trim();
    }

    public String getFormErrorMessage() {
        return driver.findElement(lblFormError).getText().trim();
    }
}