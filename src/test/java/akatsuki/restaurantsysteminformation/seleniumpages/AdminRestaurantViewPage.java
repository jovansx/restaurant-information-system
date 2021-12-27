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
public class AdminRestaurantViewPage {

    private WebDriver driver;



    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[1]/div/button[1]")
    public WebElement addRoomButton;

    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[1]/div/button[2]")
    public WebElement renameRoomButton;

    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[1]/div/button[3]")
    public WebElement deleteRoomButton;

    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[1]/button[1]")
    public WebElement editRoomButton;

    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[1]/button[2]")
    public WebElement applyLayoutButton;

    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[1]/button[3]")
    public WebElement saveButton;

    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[1]/button[4]")
    public WebElement cancelButton;

    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[1]/form/mat-form-field[1]/div/div[1]/div/input")
    public WebElement rowInput;

    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[1]/form/mat-form-field[2]/div/div[1]/div/input")
    public WebElement colInput;

    @FindBy(xpath = "/html/body/div/div[2]/div/mat-dialog-container/app-roomname-dialog/div[1]/mat-form-field/div/div[1]/div/input")
    public WebElement dialogNameInput;

    @FindBy(xpath = "/html/body/div/div[2]/div/mat-dialog-container/app-roomname-dialog/div[2]/button[2]")
    public WebElement dialogAddButton;

    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[1]/mat-tab-group/mat-tab-header/div[2]/div/div/div")
    public List<WebElement> rooms;

    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[1]/mat-tab-group/mat-tab-header/div[2]/div/div/div[2]")
    public WebElement secondTab;

    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[2]/div[1]/div[1]/app-room-space/button")
    public WebElement buttonWithCordinates00;

    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[2]/div[1]/div[2]/app-room-space/button")
    public WebElement buttonWithCordinates01;

    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[2]/div[1]/div[3]/app-room-space/button")
    public WebElement buttonWithCordinates02;

    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[2]/div[2]/div[1]/app-room-space/button")
    public WebElement buttonWithCordinates10;

    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[2]/div[2]/div[2]/app-room-space/button")
    public WebElement buttonWithCordinates11;

    @FindBy(xpath = "/html/body/div[1]/div[2]/div/mat-dialog-container/app-edit-room-dialog/div[1]/mat-form-field[1]/div/div[1]/div/mat-select")
    public WebElement tableStateInput;

    @FindBy(xpath = "/html/body/div[1]/div[2]/div/mat-dialog-container/app-edit-room-dialog/div[1]/mat-form-field[2]/div/div[1]/div/input")
    public WebElement tableNameInput;

    @FindBy(xpath = "/html/body/div[1]/div[2]/div/mat-dialog-container/app-edit-room-dialog/div[2]/button[2]")
    public WebElement tableDialogOkButton;

    @FindBy(xpath = "/html/body/div[1]/div[4]/div/div/div/mat-option[1]")
    public WebElement emptyButton;
    @FindBy(xpath = "/html/body/div[1]/div[4]/div/div/div/mat-option[2]")
    public WebElement circleButton;
    @FindBy(xpath = "/html/body/div[1]/div[4]/div/div/div/mat-option[3]")
    public WebElement squareButton;

    @FindBy(css = "mat-card")
    public List<WebElement> tables;


    public AdminRestaurantViewPage(WebDriver driver) {
        this.driver = driver;
    }

    public void ensureDialogIsOpen() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.visibilityOf(dialogNameInput));
    }

    public void ensureDialogIsClosed() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.invisibilityOf(dialogNameInput));
    }


    public void ensureRoomIsDeleted() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.numberOfElementsToBe(By.xpath("/html/body/app-root/app-restaurant-view/div/div[1]/mat-tab-group/mat-tab-header/div[2]/div/div/div")
                        , 2));
    }

    public void ensureButtonIsEnabled(WebElement button) {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.elementToBeClickable(button));
    }

    public void ensureTableDialogIsShown() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.visibilityOf(tableDialogOkButton));
    }

    public void ensureTableStateSelectIsShown() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.visibilityOf(circleButton));
    }

    public void ensureNumberOfTablesIs(int i) {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("mat-card"), i));
    }

    public void ensureTableLayoutIsUpdated() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.visibilityOf(buttonWithCordinates02));
    }

    public String getNameOfTable(WebElement table) {
        return table.findElement(By.cssSelector("h3")).getText();
    }

    public void clickButtonUntilItIsClicked(WebElement button) {
        while (true) {
            try {
                button.click();
                return;
            } catch (Exception ignored) {
            }
            try {
                Thread.sleep(50);
            } catch (Exception ignored) {
            }
        }

    }

}
