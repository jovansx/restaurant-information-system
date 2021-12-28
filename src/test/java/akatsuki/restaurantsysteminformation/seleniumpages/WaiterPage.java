package akatsuki.restaurantsysteminformation.seleniumpages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaiterPage {

    private WebDriver driver;

    @FindBy(css = "mat-card")
    private List<WebElement> tables;
    @FindBy(xpath = "/html/body/app-root/app-waiter-homepage/div/div[2]/div/div[1]/div[1]/app-room-space/button")
    private WebElement buttonWithCordinates00;
    @FindBy(xpath = "/html/body/app-root/app-waiter-homepage/div/div[2]/div/div[1]/div[2]/app-room-space/button")
    private WebElement buttonWithCordinates01;
    @FindBy(xpath = "/html/body/app-root/app-waiter-homepage/div/div[2]/div/div[2]/div[1]/app-room-space/button")
    private WebElement buttonWithCordinates10;
    @FindBy(xpath = "/html/body/app-root/app-waiter-homepage/div/div[2]/div/div[2]/div[2]/app-room-space/button")
    private WebElement buttonWithCordinates11;
    @FindBy(xpath = "/html/body/app-root/app-waiter-homepage/div/div[1]/mat-tab-group/mat-tab-header/div[2]/div/div/div[1]")
    private WebElement firstTab;
    @FindBy(xpath = "/html/body/app-root/app-waiter-homepage/div/div[1]/mat-tab-group/mat-tab-header/div[2]/div/div/div[2]")
    private WebElement secondTab;
    @FindBy(xpath = "/html/body/div/div[2]/div/mat-dialog-container/app-pincode-dialog/div[1]/mat-form-field/div/div[1]/div/input")
    private WebElement pinCodeInput;
    @FindBy(xpath = "/html/body/div/div[2]/div/mat-dialog-container/app-pincode-dialog/div[2]/button[2]")
    private WebElement dialogOkButton;
    @FindBy(xpath = "/html/body/app-root/app-table-details/div/div[2]/div[1]/div/div[1]/mat-list/mat-list-item")
    private List<WebElement> allItems;
    @FindBy(xpath = "/html/body/app-root/app-table-details/div/div[2]/div[1]/div[1]/div[1]/mat-list/mat-list-item")
    private List<WebElement> drinkItems;
    @FindBy(xpath = "/html/body/app-root/app-table-details/div/div[2]/div[1]/div[2]/div[1]/mat-list/mat-list-item")
    private List<WebElement> dishItems;
    @FindBy(xpath = "/html/body/app-root/app-table-details/div/div[2]/div[2]/button")
    private WebElement backToRoomButton;

    public WaiterPage(WebDriver driver) {
        this.driver = driver;
    }

    public void writeToInput(WebElement input, String text) {
        input.clear();
        input.sendKeys(text);
    }
}
