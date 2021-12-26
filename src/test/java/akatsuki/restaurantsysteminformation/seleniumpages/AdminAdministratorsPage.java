package akatsuki.restaurantsysteminformation.seleniumpages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminAdministratorsPage {
    private WebDriver driver;

    @FindBy(xpath = "//table[1]/tbody/tr")
    private List<WebElement> tableRows;

    @FindBy(xpath = "//form/mat-form-field[1]/div/div[1]/div/input")
    private WebElement firstNameInput;

    @FindBy(xpath = "//form/mat-form-field[last()]/div/div[1]/div/input")
    private WebElement phoneNumberInput;

    @FindBy(xpath = "//mat-card/form/div/button")    // Cancel -> Edit -> Save (3)
    private List<WebElement> buttons;

    @FindBy(css = ".add-button")
    private WebElement addBtn;

    @FindBy(xpath = "//*[@id='mat-menu-panel-0']/div/button")     // Manager button, Employee button
    private List<WebElement> addButtons;

    @FindBy(xpath = "//mat-dialog-content/form/mat-form-field")     // List of mat-form-field, 8 when manager dialog is opened and 7 when employee dialog is opened
    private List<WebElement> dialogFields;

    @FindBy(xpath = "//mat-dialog-actions/button")     // Cancel Save
    private List<WebElement> dialogButtons;

    public AdminAdministratorsPage(WebDriver driver) {
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

    public void ensureAddButtonIsDisplayed() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOf(addBtn));
    }

    public void ensureAddButtonsAreDisplayed() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOfAllElements(addButtons));
    }

    public void ensureDialogFieldsAreDisplayed() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOfAllElements(dialogFields));
    }

    public void ensureDialogButtonsAreDisplayed() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOfAllElements(dialogButtons));
    }

    public void ensureRowsAreDisplayed(int number) {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.numberOfElementsToBe(By.xpath("//table[1]/tbody/tr"), number));
    }
}
