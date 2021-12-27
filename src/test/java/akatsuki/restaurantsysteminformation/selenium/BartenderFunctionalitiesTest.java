package akatsuki.restaurantsysteminformation.selenium;

import akatsuki.restaurantsysteminformation.seleniumpages.BartenderPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BartenderFunctionalitiesTest {

    private static WebDriver browser;

    private static BartenderPage bartenderPage;

    @BeforeAll
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");

        browser = new ChromeDriver();
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
        bartenderPage.ensureItemsAreDisplayed();
        Assertions.assertEquals(3, bartenderPage.getRowsFromTable(0).size());
        Assertions.assertThrows(NoSuchElementException.class, () -> bartenderPage.detailsDiv.isDisplayed());
        bartenderPage.getRowsFromTable(0).get(1).click();
        bartenderPage.ensureItemDetailsIsDisplayed();
        Assertions.assertTrue(bartenderPage.detailsDiv.isDisplayed());
        bartenderPage.closeDetailsDiv();
        Assertions.assertThrows(NoSuchElementException.class, () -> bartenderPage.detailsDiv.isDisplayed());
    }

    @Order(2)
    @Test
    public void failPreparing_WrongPin() {
        Assertions.assertThrows(NoSuchElementException.class, () -> bartenderPage.detailsDiv.isDisplayed());
        Assertions.assertEquals("Sex on the beach", bartenderPage.getRowsFromTable(0).get(1).getText());
        bartenderPage.getRowsFromTable(0).get(1).click();
        bartenderPage.ensureItemDetailsIsDisplayed();
        Assertions.assertTrue(bartenderPage.detailsDiv.isDisplayed());
        bartenderPage.prepareButton.click();
        bartenderPage.enterPin("1111");
        bartenderPage.pinButton.click();
        Assertions.assertEquals("Sex on the beach", bartenderPage.getRowsFromTable(0).get(1).getText());
        bartenderPage.closeDetailsDiv();
        Assertions.assertThrows(NoSuchElementException.class, () -> bartenderPage.detailsDiv.isDisplayed());
    }

    @Order(3)
    @Test
    public void succesfullyPreparing_ItemMovedToPreparationTable() {
        Assertions.assertThrows(NoSuchElementException.class, () -> bartenderPage.detailsDiv.isDisplayed());
        Assertions.assertEquals("Sex on the beach", bartenderPage.getRowsFromTable(0).get(1).getText());
        bartenderPage.getRowsFromTable(0).get(1).click();
        bartenderPage.ensureItemDetailsIsDisplayed();
        Assertions.assertTrue(bartenderPage.detailsDiv.isDisplayed());
        bartenderPage.prepareButton.click();
        bartenderPage.enterPin("1112");
        bartenderPage.pinButton.click();
        bartenderPage.ensureItemFromOnHoldIsMoved();
        Assertions.assertTrue(bartenderPage.getRowsFromTable(1).get(1).getText().startsWith("Sex on the beach"));
    }

    @Order(4)
    @Test
    public void succesfullyMaking_ItemMovedToReadyTable() {
        Assertions.assertEquals(1, bartenderPage.getRowsFromTable(2).size());
        Assertions.assertThrows(NoSuchElementException.class, () -> bartenderPage.detailsDiv.isDisplayed());
        Assertions.assertTrue(bartenderPage.getRowsFromTable(1).get(0).getText().startsWith("Apple juice"));
        bartenderPage.getRowsFromTable(1).get(0).click();
        bartenderPage.ensureItemDetailsIsDisplayed();
        Assertions.assertTrue(bartenderPage.detailsDiv.isDisplayed());
        bartenderPage.readyButton.click();
        bartenderPage.enterPin("1112");
        bartenderPage.pinButton.click();
        bartenderPage.ensureItemFromPreparationToReadyIsMoved();
        Assertions.assertEquals(2, bartenderPage.getRowsFromTable(2).size());
        Assertions.assertEquals(1, bartenderPage.getRowsFromTable(1).size());
    }

}
