package akatsuki.restaurantsysteminformation.seleniumpages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaiterTableDetailsPage {
    private WebDriver driver;

    @FindBy(id = "discardOrderButton")
    private WebElement discardButton;
    @FindBy(xpath = "/html/body/app-root/app-table-details/div/div[1]/div[2]/button[2]")
    private WebElement chargeOrderButton;
    @FindBy(xpath = "/html/body/app-root/app-table-details/div/div[2]/div[1]/div[1]/div[1]/mat-list/mat-list-item/div/div[3]/div/button[1]")
    private WebElement editDrinkButton;
    // delete drink
    // deliver drink
    // done drink
    // deliver all drink
    // add drink
    @FindBy(xpath = "/html/body/app-root/app-table-details/div/div[2]/div[1]/div[1]/div[2]/button[2]/span[1]/mat-icon")
    private WebElement addDrinkItemsButton;

    @FindBy(xpath = "/html/body/app-root/app-table-details/div/div[2]/div[1]/div[2]/div[2]/button[2]/span[1]/mat-icon")
    private WebElement addDishItemButton;

    @FindBy(xpath = "/html/body/app-root/app-table-details/div/div[2]/div[1]/div[2]/div[1]/mat-list/mat-list-item[2]/div/div[3]/div/button[3]")
    private WebElement deleteDishItemButton;

    @FindBy(xpath = "/html/body/div/div[2]/div/mat-dialog-container/app-order-item-dialog/mat-dialog-content/div/mat-tab-group/div/mat-tab-body[1]/div/mat-card[1]/button")
    private WebElement addDrinkItemToListButton;

    @FindBy(xpath = "/html/body/div/div[2]/div/mat-dialog-container/app-order-item-dialog/mat-dialog-content/div/div[2]/div/mat-list/mat-list-item/div/div[3]/div[2]/button[2]")
    private WebElement incrementDrinkItemButton;

    @FindBy(xpath = "/html/body/div/div[2]/div/mat-dialog-container/app-order-item-dialog/mat-dialog-actions/button[2]")
    private WebElement doneButton;

    @FindBy(xpath = "/html/body/div[1]/div[2]/div/mat-dialog-container/app-order-item-dialog/mat-dialog-actions/button[1]")
    private WebElement cancelButton;

    @FindBy(xpath = "/html/body/div[1]/div[2]/div/mat-dialog-container/app-order-item-dialog/mat-dialog-content/div/div[2]/div/mat-list/mat-list-item/div/div[3]/div[2]/div/input")
    private WebElement drinkItemAmountInput;

    @FindBy(xpath = "/html/body/app-root/app-table-details/div/div[2]/div[1]/div[2]/div[1]/mat-list/mat-list-item/div/div[3]/div/button[1]")
    private WebElement prepareDishItemButton;

    @FindBy(xpath = "/html/body/app-root/app-table-details/div/div[2]/div[1]/div[1]/div[1]/mat-list/mat-list-item[5]/div/div[3]/div/button")
    private WebElement deliverDrinkItemsButton;

    @FindBy(xpath = "/html/body/app-root/app-table-details/div/div[2]/div[1]/div[1]/div[1]/mat-list/mat-list-item[5]/div/div[3]/div/p")
    private WebElement doneParagraph;

    @FindBy(xpath = "/html/body/app-root/app-table-details/div/div[2]/div[1]/div[2]/div[2]/button[1]")
    private WebElement deliverAllDishItems;

    @FindBy(xpath = "/html/body/app-root/app-table-details/div/div[2]/div[2]/button")
    private WebElement backToRoomButton;


    // edit dish
    // delete dish
    // prepare dish
    // deliver dish
    // done dish
    // deliver all dish
    // add dish
    // back to room

    public WaiterTableDetailsPage(WebDriver driver) {
        this.driver = driver;
    }
}
