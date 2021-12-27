package akatsuki.restaurantsysteminformation.seleniumpages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerEmployeesPage {
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

    @FindBy(xpath = "//mat-dialog-content/form/mat-form-field")
    // List of mat-form-field, 8 when manager dialog is opened and 7 when employee dialog is opened
    private List<WebElement> dialogFields;

    @FindBy(xpath = "//mat-dialog-actions/button")     // Cancel Save
    private List<WebElement> dialogButtons;

    public ManagerEmployeesPage(WebDriver driver) {
        this.driver = driver;
    }

    public List<WebElement> getTableRows(int number) {
        Utilities.numberOfElementsWait(driver, By.xpath("//table[1]/tbody/tr"), number, 10);
        return tableRows;
    }

    public WebElement getFirstNameInput() {
        Utilities.visibilityWait(driver, firstNameInput, 10);
        return firstNameInput;
    }

    public WebElement getPhoneNumberInput() {
        Utilities.visibilityWait(driver, phoneNumberInput, 10);
        return phoneNumberInput;
    }

    public void setFirstNameInput(String firstName) {
        Utilities.visibilityWait(driver, firstNameInput, 10);
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
    }

    public void setPhoneNumber(String phoneNumber) {
        Utilities.visibilityWait(driver, phoneNumberInput, 10);
        phoneNumberInput.clear();
        phoneNumberInput.sendKeys(phoneNumber);
    }

    public void clickSelectedRow(int index) {
        Utilities.clickableWait(driver, tableRows.get(index), 10).click();
    }

    public void clickButton(int index) {
        Utilities.clickableWait(driver, buttons.get(index), 10).click();
    }

    public void clickAddButton() {
        Utilities.clickableWait(driver, addBtn, 10).click();
    }

    public void clickSaveButton() {
        Utilities.clickableWait(driver, dialogButtons.get(1), 10).click();
    }

    public void setFields(String name, String lastName, String email, String pinCode, String salary, String phoneNumber) {
        Utilities.numberOfElementsWait(driver, dialogFields, 7, 10);

        dialogFields.get(0).findElement(By.xpath("div/div[1]/div/input")).sendKeys(name);
        dialogFields.get(1).findElement(By.xpath("div/div[1]/div/input")).sendKeys(lastName);
        dialogFields.get(2).findElement(By.xpath("div/div[1]/div/input")).sendKeys(email);
        dialogFields.get(3).findElement(By.xpath("div/div[1]/div/input")).sendKeys(pinCode);
        dialogFields.get(5).findElement(By.xpath("div/div[1]/div/input")).sendKeys(salary);
        dialogFields.get(6).findElement(By.xpath("div/div[1]/div/input")).sendKeys(phoneNumber);
    }
}
