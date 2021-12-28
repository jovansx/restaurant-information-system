package akatsuki.restaurantsysteminformation.selenium;

import akatsuki.restaurantsysteminformation.seleniumpages.ChefPage;
import akatsuki.restaurantsysteminformation.seleniumpages.Utilities;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChefFunctionalitiesTest {

    private static WebDriver browser;

    private static ChefPage chefPage;

    @BeforeAll
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        browser = new ChromeDriver(options);
        browser.manage().window().maximize();
        browser.navigate().to("http://localhost:4200/home/chef");

        chefPage = PageFactory.initElements(browser, ChefPage.class);
    }

    @AfterAll
    public static void tearDown() {
        browser.quit();
    }

    @Order(1)
    @Test
    public void selectAndCloseItem_DetailsOfItemIsDisplayedAndThenClosed() {
        Utilities.numberOfElementsWait(browser, chefPage.getAllRows(), 3, 10);
        Assertions.assertEquals(1, chefPage.getRowsFromTable(0).size());
        Assertions.assertThrows(NoSuchElementException.class, () -> chefPage.getDetailsDiv().isDisplayed());
        chefPage.getRowsFromTable(0).get(0).click();
        Utilities.visibilityWait(browser, chefPage.getPrepareButton(), 10);
        Assertions.assertTrue(chefPage.getDetailsDiv().isDisplayed());
        chefPage.getDetailsCloseButton().click();
        Assertions.assertThrows(NoSuchElementException.class, () -> chefPage.getDetailsDiv().isDisplayed());
    }

    @Order(2)
    @Test
    public void failPreparing_WrongPin() {
        Assertions.assertThrows(NoSuchElementException.class, () -> chefPage.getDetailsDiv().isDisplayed());
        chefPage.getRowsFromTable(0).get(0).click();
        Utilities.visibilityWait(browser, chefPage.getPrepareButton(), 10);
        Assertions.assertTrue(chefPage.getDetailsDiv().isDisplayed());
        chefPage.getPrepareButton().click();
        chefPage.enterPin("1112");
        chefPage.getPinButton().click();
        chefPage.getDetailsCloseButton().click();
        Assertions.assertThrows(NoSuchElementException.class, () -> chefPage.getDetailsDiv().isDisplayed());
    }

    @Order(3)
    @Test
    public void succesfullyPreparing_ItemMovedToPreparationTable() {
        Assertions.assertEquals(1, chefPage.getRowsFromTable(0).size());
        Assertions.assertEquals(1, chefPage.getRowsFromTable(1).size());
        Assertions.assertThrows(NoSuchElementException.class, () -> chefPage.getDetailsDiv().isDisplayed());
        chefPage.getRowsFromTable(0).get(0).click();
        Utilities.visibilityWait(browser, chefPage.getPrepareButton(), 10);
        Assertions.assertTrue(chefPage.getDetailsDiv().isDisplayed());
        chefPage.getPrepareButton().click();
        chefPage.enterPin("1113");
        chefPage.getPinButton().click();
        Utilities.numberOfElementsWait(browser, chefPage.getRowsInOnHold(), 1, 10);
        Assertions.assertEquals(2, chefPage.getRowsFromTable(1).size());
        Assertions.assertEquals(0, chefPage.getRowsFromTable(0).size());
    }

    @Order(4)
    @Test
    public void succesfullyMaking_ItemMovedToReadyTable() {
        Assertions.assertEquals(2, chefPage.getRowsFromTable(1).size());
        Assertions.assertEquals(1, chefPage.getRowsFromTable(2).size());
        Assertions.assertThrows(NoSuchElementException.class, () -> chefPage.getDetailsDiv().isDisplayed());
        chefPage.getRowsFromTable(1).get(0).click();
        Utilities.visibilityWait(browser, chefPage.getPrepareButton(), 10);
        Assertions.assertTrue(chefPage.getDetailsDiv().isDisplayed());
        chefPage.getReadyButton().click();
        chefPage.enterPin("1113");
        chefPage.getPinButton().click();
        Utilities.numberOfElementsWait(browser, chefPage.getRowsInReady(), 2, 10);
        Assertions.assertEquals(2, chefPage.getRowsFromTable(2).size());
        Assertions.assertEquals(1, chefPage.getRowsFromTable(1).size());
    }

}
