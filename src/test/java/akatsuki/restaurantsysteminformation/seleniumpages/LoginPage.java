package akatsuki.restaurantsysteminformation.seleniumpages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginPage {

    private WebDriver driver;

    @FindBy(xpath = "//*[@id='mat-input-0']")
    private WebElement usernameInput;

    @FindBy(xpath = "//*[@id='mat-input-1']")
    private WebElement passwordInput;

    @FindBy(id = "login")
    private WebElement loginBtn;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void login(String username, String password) {
        setUsernameInput(username);
        setPasswordInput(password);
        clickLoginButton();
    }

    private void setUsernameInput(String username) {
        Utilities.visibilityWait(driver, usernameInput, 10);
        usernameInput.clear();
        usernameInput.sendKeys(username);
    }

    private void setPasswordInput(String password) {
        Utilities.visibilityWait(driver, passwordInput, 10);
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    private void clickLoginButton() {
        Utilities.clickableWait(driver, this.loginBtn, 10).click();
    }
}
