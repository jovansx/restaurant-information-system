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
public class AdminRestaurantViewPage {

    private WebDriver driver;
    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[1]/div/button[1]")
    private WebElement addRoomButton;
    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[1]/div/button[2]")
    private WebElement renameRoomButton;
    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[1]/div/button[3]")
    private WebElement deleteRoomButton;
    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[1]/button[1]")
    private WebElement editRoomButton;
    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[1]/button[2]")
    private WebElement applyLayoutButton;
    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[1]/button[3]")
    private WebElement saveButton;
    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[1]/button[4]")
    private WebElement cancelButton;
    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[1]/form/mat-form-field[1]/div/div[1]/div/input")
    private WebElement rowInput;
    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[1]/form/mat-form-field[2]/div/div[1]/div/input")
    private WebElement colInput;
    @FindBy(xpath = "/html/body/div/div[2]/div/mat-dialog-container/app-roomname-dialog/div[1]/mat-form-field/div/div[1]/div/input")
    private WebElement dialogNameInput;
    @FindBy(xpath = "/html/body/div/div[2]/div/mat-dialog-container/app-roomname-dialog/div[2]/button[2]")
    private WebElement dialogAddButton;
    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[1]/mat-tab-group/mat-tab-header/div[2]/div/div/div")
    private List<WebElement> rooms;
    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[1]/mat-tab-group/mat-tab-header/div[2]/div/div/div[2]")
    private WebElement secondTab;
    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[2]/div[1]/div[1]/app-room-space/button")
    private WebElement buttonWithCordinates00;
    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[2]/div[1]/div[2]/app-room-space/button")
    private WebElement buttonWithCordinates01;
    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[2]/div[1]/div[3]/app-room-space/button")
    private WebElement buttonWithCordinates02;
    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[2]/div[2]/div[1]/app-room-space/button")
    private WebElement buttonWithCordinates10;
    @FindBy(xpath = "/html/body/app-root/app-restaurant-view/div/div[2]/div[2]/div[2]/div[2]/app-room-space/button")
    private WebElement buttonWithCordinates11;
    @FindBy(xpath = "/html/body/div[1]/div[2]/div/mat-dialog-container/app-edit-room-dialog/div[1]/mat-form-field[1]/div/div[1]/div/mat-select")
    private WebElement tableStateInput;
    @FindBy(xpath = "/html/body/div[1]/div[2]/div/mat-dialog-container/app-edit-room-dialog/div[1]/mat-form-field[2]/div/div[1]/div/input")
    private WebElement tableNameInput;
    @FindBy(xpath = "/html/body/div[1]/div[2]/div/mat-dialog-container/app-edit-room-dialog/div[2]/button[2]")
    private WebElement tableDialogOkButton;
    @FindBy(xpath = "/html/body/div[1]/div[4]/div/div/div/mat-option[1]")
    private WebElement emptyButton;
    @FindBy(xpath = "/html/body/div[1]/div[4]/div/div/div/mat-option[2]")
    private WebElement circleButton;
    @FindBy(xpath = "/html/body/div[1]/div[4]/div/div/div/mat-option[3]")
    private WebElement squareButton;
    @FindBy(css = "mat-card")
    private List<WebElement> tables;

    public AdminRestaurantViewPage(WebDriver driver) {
        this.driver = driver;
    }

    public void writeToInput(WebElement input, String text) {
        input.clear();
        input.sendKeys(text);
    }

    public String getNameOfTable(WebElement table) {
        return table.findElement(By.cssSelector("h3")).getText();
    }
}
