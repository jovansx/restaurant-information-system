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
public class ChefPage {

    private WebDriver driver;

    @FindBy(xpath = "//*[@id=\"mat-list\"]")
    private List<WebElement> tables;

    @FindBy(xpath = "/html/body/app-root/app-chef-homepage/div/div[2]/app-item-details")
    private WebElement detailsDiv;

    @FindBy(xpath = "/html/body/app-root/app-chef-homepage/div/div[2]/app-item-details/div/div[1]/button")
    private WebElement detailsCloseButton;

    @FindBy(xpath = "/html/body/app-root/app-chef-homepage/div/div[2]/app-item-details/div/div[5]/button")
    private WebElement prepareButton;

    @FindBy(xpath = "/html/body/app-root/app-chef-homepage/div/div[2]/app-item-details/div/div[5]/button")
    private WebElement readyButton;

    @FindBy(xpath = "/html/body/div[1]/div[2]/div/mat-dialog-container/app-pincode-dialog/div[1]/mat-form-field/div/div[1]/div/input")
    private WebElement pinInput;

    @FindBy(xpath = "/html/body/div[1]/div[2]/div/mat-dialog-container/app-pincode-dialog/div[2]/button[2]")
    private WebElement pinButton;

    @FindBy(xpath = "//*[@id=\"mat-list\"]/app-item[2]/mat-list-option")
    private List<WebElement> rowsInOnHold;

    @FindBy(xpath = "/html/body/app-root/app-chef-homepage/div/div/div[3]/app-item-list/div/mat-selection-list/app-item/mat-list-option")
    private List<WebElement> rowsInReady;

    @FindBy(xpath = "//*[@id=\"mat-list\"]/app-item/mat-list-option")
    private List<WebElement> allRows;

    public ChefPage(WebDriver driver) {
        this.driver = driver;
    }

    public List<WebElement> getRowsFromTable(int tableIndex) {
        return tables.get(tableIndex).findElements(By.cssSelector("#mat-list > app-item > mat-list-option"));
    }

    public void enterPin(String pin) {
        pinInput.clear();
        pinInput.sendKeys(pin);
    }


}
