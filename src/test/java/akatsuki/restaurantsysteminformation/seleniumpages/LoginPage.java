package akatsuki.restaurantsysteminformation.seleniumpages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    public void setUsernameInput(String username) {
        usernameInput.clear();
        usernameInput.sendKeys(username);
    }

    public void setPasswordInput(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void ensureIsDisplayedLoginBtn() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(loginBtn));
    }

    public void ensureIsNotDisplayedLoginBtn() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.invisibilityOf(loginBtn));
    }

    public void clickLoginButton() {
        WebElement element = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(loginBtn));
        element.click();
    }
}
