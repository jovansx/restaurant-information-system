package akatsuki.restaurantsysteminformation.seleniumpages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemAdminWorkersPage {

    private WebDriver driver;

    @FindBy(xpath = "//table[1]/tbody/tr")
    private List<WebElement> tableRows;

    @FindBy(xpath = "//form/mat-form-field[1]/div/div[1]/div/input")
    private WebElement firstNameInput;

    @FindBy(xpath = "//form/mat-form-field[last()]/div/div[1]/div/input")
    private WebElement phoneNumberInput;

    @FindBy(xpath = "/html/body/app-root/app-workers/div/mat-card/form/div/button")    // Cancel -> Edit -> Save (3)
    private List<WebElement> buttons;

    public SystemAdminWorkersPage(WebDriver driver) {
        this.driver = driver;
    }

    public void setFirstNameInput(String firstName) {
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
    }

    public void setPhoneNumber(String phoneNumber) {
        phoneNumberInput.clear();
        phoneNumberInput.sendKeys(phoneNumber);
    }

    public void clickSelectedRow(int index) {
        WebElement row = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(tableRows.get(index)));
        row.click();
    }

    public void clickButton(int index) {
        WebElement button = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(buttons.get(index)));
        button.click();
    }
}
