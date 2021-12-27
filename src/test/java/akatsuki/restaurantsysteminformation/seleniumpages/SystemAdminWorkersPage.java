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
public class SystemAdminWorkersPage {

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

    @FindBy(xpath = "//mat-dialog-content/form/mat-form-field/div/div[1]/div/input")
    // List of inputs, 8 when manager dialog is opened and 7 when employee dialog is opened
    private List<WebElement> dialogInputs;

    @FindBy(xpath = "//mat-dialog-actions/button")     // Cancel Save
    private List<WebElement> dialogButtons;

    @FindBy(css = ".password-btn")
    private WebElement changePasswordBtn;

    @FindBy(xpath = "//app-change-password-dialog/mat-dialog-content/form/mat-form-field/div/div[1]/div/input")
    private List<WebElement> passwordDialogInputs;  // Old password, New password, Repeat password

    public SystemAdminWorkersPage(WebDriver driver) {
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

    public void setInput(WebElement element, String value) {
        Utilities.visibilityWait(driver, element, 10);
        element.clear();
        element.sendKeys(value);
    }

    public void setManagerFields(String name, String lastName, String email, String username, String password1, String password2, String salary, String phoneNumber) {
        Utilities.numberOfElementsWait(driver, dialogInputs, 8, 10);
        Utilities.visibilityAllWait(driver, dialogInputs, 10);

        setInput(dialogInputs.get(0), name);
        setInput(dialogInputs.get(1), lastName);
        setInput(dialogInputs.get(2), email);
        setInput(dialogInputs.get(3), username);
        setInput(dialogInputs.get(4), password1);
        setInput(dialogInputs.get(5), password2);
        setInput(dialogInputs.get(6), salary);
        setInput(dialogInputs.get(7), phoneNumber);
    }

    public void setEmployeeFields(String name, String lastName, String email, String pinCode, String salary, String phoneNumber) {
        Utilities.numberOfElementsWait(driver, dialogInputs, 6, 10);
        Utilities.visibilityAllWait(driver, dialogInputs, 10);

        setInput(dialogInputs.get(0), name);
        setInput(dialogInputs.get(1), lastName);
        setInput(dialogInputs.get(2), email);
        setInput(dialogInputs.get(3), pinCode);
        setInput(dialogInputs.get(4), salary);
        setInput(dialogInputs.get(5), phoneNumber);
    }

    public void setPasswordFields(String oldPassword, String newPassword, String repeatPassword) {
        Utilities.numberOfElementsWait(driver, passwordDialogInputs, 3, 10);
        Utilities.visibilityAllWait(driver, passwordDialogInputs, 10);

        setInput(passwordDialogInputs.get(0), oldPassword);
        setInput(passwordDialogInputs.get(1), newPassword);
        setInput(passwordDialogInputs.get(2), repeatPassword);
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
        Utilities.invisibilityWait(driver, dialogButtons.get(1), 10);
    }

    public void clickPasswordButton() {
        Utilities.visibilityWait(driver, changePasswordBtn, 10);
        Utilities.clickableWait(driver, changePasswordBtn, 10).click();
    }

    public void clickAddUser(int index) {
        Utilities.clickableWait(driver, addButtons.get(index), 10).click();
    }

    public void deleteRow(int index) {
        Utilities.clickableWait(driver, tableRows.get(index).findElement(By.xpath("td[5]/button")), 10).click();
    }

}
