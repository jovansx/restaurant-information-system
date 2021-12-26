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
    private WebElement addBtn;

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

    public void setItemName(String itemName) {
        itemNameInput.clear();
        itemNameInput.sendKeys(itemName);
    }

    public void setItemCategory(String itemCategory) {
        categorySelect.sendKeys(itemCategory);
    }

    public void setPrice(String price) {
        priceInput.clear();
        priceInput.sendKeys(price);
    }

    public void ensureSaveButtonIsClickable() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(dialogButtons.get(1)));
    }

    public void ensureAddButtonIsDisplayed() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.visibilityOf(addBtn));
    }

    public void ensureCardsAreDisplayed(int number, int itemType) { // 1 - drinks, 2 - dishes
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.numberOfElementsToBe(By.xpath("//mat-card[" + itemType + "]/div/mat-card"), number));
    }

    public void ensureItemNameInputIsDisplayed() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.visibilityOf(itemNameInput));
    }

    public void ensureItemCategorySelectIsDisplayed() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.visibilityOf(categorySelect));
    }

    public void ensureItemPriceInputIsDisplayed() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.visibilityOf(priceInput));
    }

    public void ensureDialogButtonsAreDisplayed() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.visibilityOfAllElements(dialogButtons));
    }

    public void ensureDialogButtonsAreNotDisplayed() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.invisibilityOfAllElements(dialogButtons));
    }

    public void ensureTextIsPresentInCategorySelect() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.textToBePresentInElement(categorySelect, "Juices"));
    }
}
