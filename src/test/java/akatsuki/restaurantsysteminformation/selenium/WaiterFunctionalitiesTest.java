package akatsuki.restaurantsysteminformation.selenium;

import akatsuki.restaurantsysteminformation.seleniumpages.Utilities;
import akatsuki.restaurantsysteminformation.seleniumpages.WaiterPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WaiterFunctionalitiesTest {

    private static WebDriver browser;

    private static WaiterPage waiterPage;

    @BeforeAll
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        browser = new ChromeDriver(options);
        browser.manage().window().maximize();
        browser.navigate().to("http://localhost:4200/home/waiter");

        waiterPage = PageFactory.initElements(browser, WaiterPage.class);
    }

    @AfterAll
    public static void tearDown() {
        browser.quit();
    }

    @Order(1)
    @Test
    public void changingTabs() {
        Utilities.numberOfElementsWait(browser, waiterPage.getTables(), 2, 10);
        waiterPage.getSecondTab().click();
        Utilities.numberOfElementsWait(browser, waiterPage.getTables(), 0, 10);
        waiterPage.getFirstTab().click();
        Utilities.numberOfElementsWait(browser, waiterPage.getTables(), 2, 10);
    }

    @Order(2)
    @Test
    public void failingToOpenTable_WrongPin() {
        waiterPage.getButtonWithCordinates01().click();
        Utilities.visibilityWait(browser, waiterPage.getDialogOkButton(), 10);
        waiterPage.writeToInput(waiterPage.getPinCodeInput(), "1112");
        waiterPage.getDialogOkButton().click();
        Utilities.invisibilityWait(browser, waiterPage.getDialogOkButton(), 10);
        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/home/waiter", 10));
    }

    @Order(3)
    @Test
    public void succesfullyOpeningEmptyTable_TableIsShown() {
        waiterPage.getButtonWithCordinates10().click();
        Utilities.visibilityWait(browser, waiterPage.getDialogOkButton(), 10);
        waiterPage.writeToInput(waiterPage.getPinCodeInput(), "1111");
        waiterPage.getDialogOkButton().click();
        Utilities.invisibilityWait(browser, waiterPage.getDialogOkButton(), 10);
        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/home/waiter/2", 10));
        Utilities.numberOfElementsWait(browser, waiterPage.getAllItems(), 0, 10);
        waiterPage.getBackToRoomButton().click();
        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/home/waiter", 10));
    }

    @Order(4)
    @Test
    public void succesfullyOpeningBusyTable_TableIsShown() {
        waiterPage.getButtonWithCordinates01().click();
        Utilities.visibilityWait(browser, waiterPage.getDialogOkButton(), 10);
        waiterPage.writeToInput(waiterPage.getPinCodeInput(), "1111");
        waiterPage.getDialogOkButton().click();
        Utilities.invisibilityWait(browser, waiterPage.getDialogOkButton(), 10);
        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/home/waiter/1", 10));
        Utilities.numberOfElementsWait(browser, waiterPage.getAllItems(), 11, 10);
    }

}
