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

    public List<WebElement> getTableRows(int numberOfRows) {
        Utilities.numberOfElementsWait(driver, tableRows, numberOfRows, 10);
        return Utilities.visibilityAllWait(driver, tableRows, 10);
    }

    public WebElement getFirstNameInput() {
        return Utilities.visibilityWait(driver, firstNameInput, 10);
    }

    public WebElement getPhoneNumberInput() {
        return Utilities.visibilityWait(driver, phoneNumberInput, 10);
    }

    public void setFirstNameInput(String firstName) {
        WebElement el = Utilities.visibilityWait(driver, firstNameInput, 10);
        el.clear();
        el.sendKeys(firstName);
    }

    public void setPhoneNumber(String phoneNumber) {
        WebElement el = Utilities.visibilityWait(driver, phoneNumberInput, 10);
        el.clear();
        el.sendKeys(phoneNumber);
    }

    public void setManagerFields(String name, String lastName, String email, String username, String password1, String password2, String salary, String phoneNumber) {
        List<WebElement> list = Utilities.visibilityAllWait(driver, dialogInputs, 5);

        list.get(0).clear();
        list.get(0).sendKeys(name);
        list.get(1).clear();
        list.get(1).sendKeys(lastName);
        list.get(2).clear();
        list.get(2).sendKeys(email);
        list.get(3).clear();
        list.get(3).sendKeys(username);
        list.get(4).clear();
        list.get(4).sendKeys(password1);
        list.get(5).clear();
        list.get(5).sendKeys(password2);
        list.get(6).clear();
        list.get(6).sendKeys(salary);
        list.get(7).clear();
        list.get(7).sendKeys(phoneNumber);
    }

    public void setEmployeeFields(String name, String lastName, String email, String pinCode, String salary, String phoneNumber) {
        List<WebElement> list = Utilities.visibilityAllWait(driver, dialogInputs, 5);

        list.get(0).clear();
        list.get(0).sendKeys(name);
        list.get(1).clear();
        list.get(1).sendKeys(lastName);
        list.get(2).clear();
        list.get(2).sendKeys(email);
        list.get(3).clear();
        list.get(3).sendKeys(pinCode);
        list.get(4).clear();
        list.get(4).sendKeys(salary);
        list.get(5).clear();
        list.get(5).sendKeys(phoneNumber);
    }

    public void setPasswordFields(String oldPassword, String newPassword, String repeatPassword) {
        List<WebElement> list = Utilities.visibilityAllWait(driver, passwordDialogInputs, 5);

        list.get(0).sendKeys(oldPassword);
        list.get(1).sendKeys(newPassword);
        list.get(2).sendKeys(repeatPassword);
    }

    public void clickSelectedRow(int index) {
        WebElement el = Utilities.visibilityWait(driver, tableRows.get(index), 10);
        Utilities.clickButtonUntilItIsClicked(el);
    }

    public void clickButton(int index) {
        WebElement el = Utilities.visibilityWait(driver, buttons.get(index), 10);
        Utilities.clickableWait(driver, el, 10).click();
    }

    public void clickAddButton() {
        Utilities.clickableWait(driver, addBtn, 10).click();
    }

    public void clickSaveButton() {
        Utilities.clickableWait(driver, dialogButtons.get(1), 10).click();
        Utilities.invisibilityAllWait(driver, dialogButtons, 10);
    }

    public void clickPasswordButton() {
        Utilities.visibilityWait(driver, changePasswordBtn, 10);
        Utilities.clickableWait(driver, changePasswordBtn, 10).click();
    }

    public void clickAddUser(int index) {
        Utilities.clickableWait(driver, addButtons.get(index), 10).click();
    }

    public void deleteRow(int index) {
        WebElement el = Utilities.visibilityWait(driver, tableRows.get(index).findElement(By.xpath("td[5]/button")), 10);
        Utilities.clickButtonUntilItIsClicked(el);
    }
}
