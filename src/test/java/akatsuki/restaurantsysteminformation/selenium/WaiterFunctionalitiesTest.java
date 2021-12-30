package akatsuki.restaurantsysteminformation.selenium;

import akatsuki.restaurantsysteminformation.seleniumpages.Utilities;
import akatsuki.restaurantsysteminformation.seleniumpages.WaiterPage;
import akatsuki.restaurantsysteminformation.seleniumpages.WaiterTableDetailsPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WaiterFunctionalitiesTest {

    private static WebDriver browser;

    private static WaiterPage waiterPage;

    private static WaiterTableDetailsPage waiterTableDetailsPage;

    @BeforeAll
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");
//        ChromeOptions options = new ChromeOptions();
//        options.setHeadless(true);
        browser = new ChromeDriver();
        browser.manage().window().maximize();
        browser.navigate().to("http://localhost:4200/home/waiter");

        waiterPage = PageFactory.initElements(browser, WaiterPage.class);
        waiterTableDetailsPage = PageFactory.initElements(browser, WaiterTableDetailsPage.class);
    }

    @AfterAll
    public static void tearDown() {
//        browser.quit();
    }

//    @Order(1)
//    @Test
//    public void changingTabs() {
//        Utilities.numberOfElementsWait(browser, waiterPage.getTables(), 2, 10);
//        waiterPage.getSecondTab().click();
//        Utilities.numberOfElementsWait(browser, waiterPage.getTables(), 0, 10);
//        waiterPage.getFirstTab().click();
//        Utilities.numberOfElementsWait(browser, waiterPage.getTables(), 2, 10);
//    }
//
//    @Order(2)
//    @Test
//    public void failingToOpenTable_WrongPin() {
//        waiterPage.getButtonWithCordinates01().click();
//        Utilities.visibilityWait(browser, waiterPage.getDialogOkButton(), 10);
//        waiterPage.writeToInput(waiterPage.getPinCodeInput(), "1112");
//        waiterPage.getDialogOkButton().click();
//        Utilities.invisibilityWait(browser, waiterPage.getDialogOkButton(), 10);
//        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/home/waiter", 10));
//    }
//
//    @Order(3)
//    @Test
//    public void succesfullyOpeningEmptyTable_TableIsShown() {
//        waiterPage.getButtonWithCordinates10().click();
//        Utilities.visibilityWait(browser, waiterPage.getDialogOkButton(), 10);
//        waiterPage.writeToInput(waiterPage.getPinCodeInput(), "1111");
//        waiterPage.getDialogOkButton().click();
//        Utilities.invisibilityWait(browser, waiterPage.getDialogOkButton(), 10);
//        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/home/waiter/2", 10));
//        Utilities.numberOfElementsWait(browser, waiterPage.getAllItems(), 0, 10);
//        waiterPage.getBackToRoomButton().click();
//        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/home/waiter", 10));
//    }

    @Order(1)
    @Test
    public void succesfullyOpeningBusyTable_TableIsShown() {
        Utilities.clickButtonUntilItIsClicked(waiterPage.getButtonWithCordinates01());
        Utilities.visibilityWait(browser, waiterPage.getDialogOkButton(), 10);
        waiterPage.writeToInput(waiterPage.getPinCodeInput(), "1111");
        waiterPage.getDialogOkButton().click();
        Utilities.invisibilityWait(browser, waiterPage.getDialogOkButton(), 10);
        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/home/waiter/1", 10));
        Utilities.numberOfElementsWait(browser, waiterPage.getAllItems(), 11, 10);
    }

    @Order(2)
    @Test
    public void successfullyDeliverDrinkItems() throws InterruptedException {
        Utilities.visibilityWait(browser, waiterTableDetailsPage.getDeliverDrinkItemsButton(), 10);
        waiterTableDetailsPage.getDeliverDrinkItemsButton().click();
        Thread.sleep(1000);
//        Utilities.invisibilityWait(browser, waiterTableDetailsPage.getDeliverDrinkItemsButton(), 10);
        assertEquals(0, browser.findElements(By.xpath("/html/body/app-root/app-table-details/div/div[2]/div[1]/div[1]/div[1]/mat-list/mat-list-item[5]/div/div[3]/div/button")).size());
    }

    @Order(3)
    @Test
    public void successfullyDeliverAllDishItems() throws InterruptedException {
        Utilities.visibilityWait(browser, waiterTableDetailsPage.getDeliverAllDishItems(), 10);
        waiterTableDetailsPage.getDeliverAllDishItems().click();
        Thread.sleep(1000);
        assertFalse(waiterTableDetailsPage.getDeliverAllDishItems().isEnabled());
        assertEquals(0, browser.findElements(By.xpath("/html/body/app-root/app-table-details/div/div[2]/div[1]/div[2]/div[1]/mat-list/mat-list-item[4]/div/div[3]/div/button")).size());
    }

    @Order(3)
    @Test
    public void successfullyDiscardOrder_BackToWaiterHomepage() {

        Utilities.clickButtonUntilItIsClicked(waiterTableDetailsPage.getDiscardButton());
        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/home/waiter", 10));
        String classes = waiterPage.getButtonWithCordinates10().getAttribute("class");
        boolean isNoneState = true;
        for(String c: classes.split(" ")) {
            if(c.equals("TAKEN") || c.equals("CHANGED")) {
                isNoneState = false;
            }
        }
        assertTrue(isNoneState);
    }

    @Order(4)
    @Test
    public void successfullyAddDrinkItems() {
        Utilities.clickButtonUntilItIsClicked(waiterPage.getButtonWithCordinates01());
        Utilities.visibilityWait(browser, waiterPage.getDialogOkButton(), 10);
        waiterPage.writeToInput(waiterPage.getPinCodeInput(), "1111");
        waiterPage.getDialogOkButton().click();
        Utilities.invisibilityWait(browser, waiterPage.getDialogOkButton(), 10);
        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/home/waiter/1", 10));
        Utilities.clickButtonUntilItIsClicked(waiterTableDetailsPage.getAddDrinkItemsButton());
        Utilities.clickableWait(browser, waiterTableDetailsPage.getAddDrinkItemToListButton(), 10);
        Utilities.clickButtonUntilItIsClicked(waiterTableDetailsPage.getAddDrinkItemToListButton());
        Utilities.clickableWait(browser, waiterTableDetailsPage.getIncrementDrinkItemButton(), 10);
        waiterTableDetailsPage.getIncrementDrinkItemButton().click();
        Utilities.visibilityWait(browser, waiterTableDetailsPage.getDoneButton(), 10);
        waiterTableDetailsPage.getDoneButton().click();
        Utilities.numberOfElementsWait(browser, waiterPage.getDrinkItems(), 1, 10);
        assertEquals(1, waiterPage.getDrinkItems().size());
    }

    @Order(5)
    @Test
    public void successfullyEditDrinkItems() throws InterruptedException {
        Utilities.clickButtonUntilItIsClicked(waiterTableDetailsPage.getEditDrinkButton());
        Utilities.clickButtonUntilItIsClicked(waiterTableDetailsPage.getIncrementDrinkItemButton());
        Utilities.clickButtonUntilItIsClicked(waiterTableDetailsPage.getDoneButton());
        Utilities.numberOfElementsWait(browser, waiterPage.getDrinkItems(), 1, 10);
        Thread.sleep(1000);
        waiterTableDetailsPage.getEditDrinkButton().click();
        String amount = waiterTableDetailsPage.getDrinkItemAmountInput().getAttribute("value");
        assertEquals("2", amount);
        waiterTableDetailsPage.getDoneButton().click();
    }

    @Order(6)
    @Test
    public void cancelEditDrinkItems() {
        Utilities.clickButtonUntilItIsClicked(waiterTableDetailsPage.getEditDrinkButton());
        Utilities.clickButtonUntilItIsClicked(waiterTableDetailsPage.getIncrementDrinkItemButton());
        Utilities.clickButtonUntilItIsClicked(waiterTableDetailsPage.getCancelButton());
        Utilities.numberOfElementsWait(browser, waiterPage.getDrinkItems(), 1, 10);
        waiterTableDetailsPage.getEditDrinkButton().click();
        String amount = waiterTableDetailsPage.getDrinkItemAmountInput().getAttribute("value");
        assertEquals("2", amount);
        waiterTableDetailsPage.getDoneButton().click();
    }

    @Order(7)
    @Test
    public void successfullyAddDishItem() throws InterruptedException {
        waiterTableDetailsPage.getAddDishItemButton().click();
        Utilities.clickableWait(browser, waiterTableDetailsPage.getAddDrinkItemToListButton(), 10);
        waiterTableDetailsPage.getAddDrinkItemToListButton().click();
        Utilities.clickableWait(browser, waiterTableDetailsPage.getIncrementDrinkItemButton(), 10);
        waiterTableDetailsPage.getIncrementDrinkItemButton().click();
        Utilities.visibilityWait(browser, waiterTableDetailsPage.getDoneButton(), 10);
        waiterTableDetailsPage.getDoneButton().click();

        //again
        waiterTableDetailsPage.getAddDishItemButton().click();
        Utilities.clickableWait(browser, waiterTableDetailsPage.getAddDrinkItemToListButton(), 10);
        waiterTableDetailsPage.getAddDrinkItemToListButton().click();
        Utilities.clickableWait(browser, waiterTableDetailsPage.getIncrementDrinkItemButton(), 10);
        Utilities.clickButtonUntilItIsClicked(waiterTableDetailsPage.getIncrementDrinkItemButton());
        Utilities.visibilityWait(browser, waiterTableDetailsPage.getDoneButton(), 10);
        waiterTableDetailsPage.getDoneButton().click();

        Utilities.numberOfElementsWait(browser, waiterPage.getDishItems(), 2, 10);
        assertEquals(2, waiterPage.getDishItems().size());
        assertEquals(3, waiterPage.getAllItems().size());
    }

    @Order(8)
    @Test
    public void successfullyDeleteDishItem() throws InterruptedException {
        Thread.sleep(1000);
        waiterTableDetailsPage.getDeleteDishItemButton().click();
        Utilities.numberOfElementsWait(browser, waiterPage.getDishItems(), 1, 10);
        assertEquals(1, waiterPage.getDishItems().size());
        assertEquals(2, waiterPage.getAllItems().size());
    }

    @Order(9)
    @Test
    public void successfullyChangeDishItemState() throws InterruptedException {
        Utilities.clickButtonUntilItIsClicked(waiterTableDetailsPage.getPrepareDishItemButton());
        // mozda cekaj na promenu teksta
        Thread.sleep(1000);
        String text = waiterTableDetailsPage.getPrepareDishItemButton().getText();
        assertNotEquals("prepare", text);
    }
}
