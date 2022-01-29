package akatsuki.restaurantsysteminformation.selenium;

import akatsuki.restaurantsysteminformation.seleniumpages.BartenderPage;
import akatsuki.restaurantsysteminformation.seleniumpages.Utilities;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.PageFactory;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BartenderFunctionalitiesTest {

    private static WebDriver browser;

    private static BartenderPage bartenderPage;

    @BeforeAll
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

        browser = new ChromeDriver(options);
        browser.manage().window().maximize();
        browser.navigate().to("http://localhost:4200/home/bartender");

        bartenderPage = PageFactory.initElements(browser, BartenderPage.class);
    }

    @AfterAll
    public static void tearDown() {
        browser.quit();
    }

    @Order(1)
    @Test
    public void selectAndCloseItem_DetailsOfItemIsDisplayedAndThenClosed() {
        Utilities.numberOfElementsWait(browser, bartenderPage.getAllRows(), 5, 10);
        Assertions.assertEquals(3, bartenderPage.getRowsFromTable(0).size());
        Assertions.assertThrows(NoSuchElementException.class, () -> bartenderPage.getDetailsDiv().isDisplayed());
        bartenderPage.getRowsFromTable(0).get(1).click();
        Utilities.visibilityWait(browser, bartenderPage.getPrepareButton(), 10);
        Assertions.assertTrue(bartenderPage.getDetailsDiv().isDisplayed());
        bartenderPage.getDetailsCloseButton().click();
        Assertions.assertThrows(NoSuchElementException.class, () -> bartenderPage.getDetailsDiv().isDisplayed());
    }

    @Order(2)
    @Test
    public void failPreparing_WrongPin() {
        Assertions.assertThrows(NoSuchElementException.class, () -> bartenderPage.getDetailsDiv().isDisplayed());
        Assertions.assertEquals("Sex on the beach", bartenderPage.getRowsFromTable(0).get(1).getText());
        bartenderPage.getRowsFromTable(0).get(1).click();
        Utilities.visibilityWait(browser, bartenderPage.getPrepareButton(), 10);
        Assertions.assertTrue(bartenderPage.getDetailsDiv().isDisplayed());
        bartenderPage.getPrepareButton().click();
        bartenderPage.enterPin("1111");
        bartenderPage.getPinButton().click();
        Assertions.assertEquals("Sex on the beach", bartenderPage.getRowsFromTable(0).get(1).getText());
        bartenderPage.getDetailsCloseButton().click();
        Assertions.assertThrows(NoSuchElementException.class, () -> bartenderPage.getDetailsDiv().isDisplayed());
    }

    @Order(3)
    @Test
    public void succesfullyPreparing_ItemMovedToPreparationTable() {
        Assertions.assertThrows(NoSuchElementException.class, () -> bartenderPage.getDetailsDiv().isDisplayed());
        Assertions.assertEquals("Sex on the beach", bartenderPage.getRowsFromTable(0).get(1).getText());
        bartenderPage.getRowsFromTable(0).get(1).click();
        Utilities.visibilityWait(browser, bartenderPage.getPrepareButton(), 10);
        Assertions.assertTrue(bartenderPage.getDetailsDiv().isDisplayed());
        bartenderPage.getPrepareButton().click();
        bartenderPage.enterPin("1112");
        bartenderPage.getPinButton().click();
        Utilities.numberOfElementsWait(browser, bartenderPage.getRowsInOnHold(), 2, 10);
        Assertions.assertTrue(bartenderPage.getRowsFromTable(1).get(1).getText().startsWith("Sex on the beach"));
    }

    @Order(4)
    @Test
    public void succesfullyMaking_ItemMovedToReadyTable() {
        Assertions.assertEquals(1, bartenderPage.getRowsFromTable(2).size());
        Assertions.assertThrows(NoSuchElementException.class, () -> bartenderPage.getDetailsDiv().isDisplayed());
        Assertions.assertTrue(bartenderPage.getRowsFromTable(1).get(0).getText().startsWith("Apple juice"));
        bartenderPage.getRowsFromTable(1).get(0).click();
        Utilities.visibilityWait(browser, bartenderPage.getPrepareButton(), 10);
        Assertions.assertTrue(bartenderPage.getDetailsDiv().isDisplayed());
        bartenderPage.getReadyButton().click();
        bartenderPage.enterPin("1112");
        bartenderPage.getPinButton().click();
        Utilities.numberOfElementsWait(browser, bartenderPage.getRowsInReady(), 2, 10);
        Assertions.assertEquals(2, bartenderPage.getRowsFromTable(2).size());
        Assertions.assertEquals(1, bartenderPage.getRowsFromTable(1).size());
    }

}
