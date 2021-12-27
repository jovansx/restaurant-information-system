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

import java.time.Duration;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemAdminMenuPage {

    private WebDriver driver;

    @FindBy(xpath = "//mat-card[1]/mat-card-actions/button")
    private WebElement addDrinkBtn;

    @FindBy(xpath = "//mat-card[2]/mat-card-actions/button")
    private WebElement addDishBtn;

    @FindBy(css = ".main-buttons-container > button:first-child")
    private WebElement discardBtn;

    @FindBy(css = ".main-buttons-container > button:nth-child(2)")
    private WebElement saveBtn;

    @FindBy(xpath = "//mat-card[1]/div/mat-card")
    private List<WebElement> drinkCards;

    @FindBy(xpath = "//mat-card[2]/div/mat-card")
    private List<WebElement> dishesCards;

    @FindBy(xpath = "//mat-dialog-content/form/mat-form-field[1]/div/div[1]/div/input")  // Item name
    private WebElement itemNameInput;

    @FindBy(xpath = "//mat-dialog-content/form/mat-form-field[3]/div/div[1]/div/mat-select")  // Item category
    private WebElement categorySelect;

    @FindBy(xpath = "//mat-dialog-content/form/mat-form-field[4]/div/div[1]/div/input")  // Item price
    private WebElement priceInput;

    @FindBy(xpath = "//mat-dialog-actions/button")     // Cancel Save
    private List<WebElement> dialogButtons;

    public SystemAdminMenuPage(WebDriver driver) {
        this.driver = driver;
    }

    public List<WebElement> getDrinkItemsCards(int number) {
        Utilities.numberOfElementsWait(driver, drinkCards, number, 10);
        return drinkCards;
    }

    public List<WebElement> getDishItemsCards(int number) {
        Utilities.numberOfElementsWait(driver, dishesCards, number, 10);
        return dishesCards;
    }

    public void setItemName(String itemName) {
        Utilities.visibilityWait(driver, itemNameInput, 10);
        itemNameInput.clear();
        itemNameInput.sendKeys(itemName);
    }

    public void setItemCategory(String itemCategory) {
        Utilities.clickableWait(driver, categorySelect, 10);
        categorySelect.sendKeys(itemCategory);
        Utilities.ensureTextIsPresentInElement(driver, categorySelect, itemCategory, 10);
    }

    public void setPrice(String price) {
        Utilities.visibilityWait(driver, priceInput, 10);
        priceInput.clear();
        priceInput.sendKeys(price);
    }

    public void clickAddDrinkButton() {
        Utilities.clickableWait(driver, addDrinkBtn, 10).click();
    }

    public void clickAddDishButton() {
        Utilities.clickableWait(driver, addDishBtn, 10).click();
    }

    public void clickDiscardButton() {
        Utilities.clickableWait(driver, discardBtn, 10).click();
    }

    public void clickSaveButton() {
        Utilities.clickableWait(driver, saveBtn, 10).click();
        Utilities.invisibilityAllWait(driver, dialogButtons, 10);
    }

    public void clickSaveDialogButton() {
        Utilities.clickableWait(driver, dialogButtons.get(1), 10).click();
    }

    public void deleteDrink(int index) {
        Utilities.visibilityWait(driver, drinkCards.get(index), 10);
        Utilities.clickableWait(driver,
                drinkCards.get(index).findElement(By.className("clear-button")), 10).click();
    }

    public void deleteDish(int index) {
        Utilities.visibilityWait(driver, dishesCards.get(index), 10);
        Utilities.clickableWait(driver,
                dishesCards.get(index).findElement(By.className("clear-button")), 10).click();
    }

    public void editDrink(int index) {
        Utilities.visibilityWait(driver, drinkCards.get(index), 10);
        Utilities.clickableWait(driver,
                drinkCards.get(index).findElement(By.xpath("button[1]")), 10).click();
    }

    public void editDish(int index) {
        Utilities.visibilityWait(driver, dishesCards.get(index), 10);
        Utilities.clickableWait(driver,
                dishesCards.get(index).findElement(By.xpath("button[1]")), 10).click();
    }
}
