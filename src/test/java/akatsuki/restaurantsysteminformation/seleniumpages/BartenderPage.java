package akatsuki.restaurantsysteminformation.seleniumpages;

import com.zaxxer.hikari.util.ConcurrentBag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
public class BartenderPage {

    private WebDriver driver;

    @FindBy(xpath = "//*[@id=\"mat-list\"]")
    public List<WebElement> tables;

    @FindBy(xpath = "/html/body/app-root/app-bartender-homepage/div/div[2]/app-item-details")
    public WebElement detailsDiv;

    @FindBy(xpath = "/html/body/app-root/app-bartender-homepage/div/div[2]/app-item-details/div/div[1]/button")
    public WebElement detailsCloseButton;

    @FindBy(xpath = "/html/body/app-root/app-bartender-homepage/div/div[2]/app-item-details/div/div[4]/button")
    public WebElement prepareButton;

    @FindBy(xpath = "/html/body/app-root/app-bartender-homepage/div/div[2]/app-item-details/div/div[4]/button")
    public WebElement readyButton;

    @FindBy(xpath = "/html/body/div[1]/div[2]/div/mat-dialog-container/app-pincode-dialog/div[1]/mat-form-field/div/div[1]/div/input")
    public WebElement pinInput;

    @FindBy(xpath = "/html/body/div/div[2]/div/mat-dialog-container/app-pincode-dialog/div[2]/button[2]")
    public WebElement pinButton;

    public BartenderPage(WebDriver driver) {
        this.driver = driver;
    }

    public List<WebElement> getRowsFromTable(int tableIndex) {
        return tables.get(tableIndex).findElements(By.cssSelector("#mat-list > app-item > mat-list-option"));
    }

    public void closeDetailsDiv() {
        detailsCloseButton.click();
    }
    public void enterPin(String pin) {
        pinInput.sendKeys(pin);
    }

    public void ensureItemFromOnHoldIsMoved() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.numberOfElementsToBe(By.xpath("//*[@id=\"mat-list\"]/app-item[2]/mat-list-option"), 2));
    }

    public void ensureItemDetailsIsDisplayed() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.visibilityOf(prepareButton));
    }

    public void ensureItemFromPreparationToReadyIsMoved() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.invisibilityOf(getRowsFromTable(1).get(0)));
    }

    public void ensureItemsAreDisplayed() {
        (new WebDriverWait(driver, Duration.ofSeconds(10)))
                .until(ExpectedConditions.numberOfElementsToBe(By.xpath("//*[@id=\"mat-list\"]/app-item/mat-list-option"), 5));
    }
}
