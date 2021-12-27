package akatsuki.restaurantsysteminformation.selenium;

import akatsuki.restaurantsysteminformation.seleniumpages.ChefPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChefFunctionalitiesTest {

    private static WebDriver browser;

    private static ChefPage chefPage;

    @BeforeAll
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");

        browser = new ChromeDriver();
        browser.manage().window().maximize();
        browser.navigate().to("http://localhost:4200/home/chef");

        chefPage = PageFactory.initElements(browser, ChefPage.class);
    }

    @Order(1)
    @Test
    public void selectAndCloseItem_DetailsOfItemIsDisplayedAndThenClosed() {
        chefPage.ensureItemsAreDisplayed();
        Assertions.assertEquals(1, chefPage.getRowsFromTable(0).size());
        Assertions.assertThrows(NoSuchElementException.class, () -> chefPage.detailsDiv.isDisplayed());
        chefPage.getRowsFromTable(0).get(0).click();
        chefPage.ensureItemDetailsIsDisplayed();
        Assertions.assertTrue(chefPage.detailsDiv.isDisplayed());
        chefPage.closeDetailsDiv();
        Assertions.assertThrows(NoSuchElementException.class, () -> chefPage.detailsDiv.isDisplayed());
    }

    @Order(2)
    @Test
    public void failPreparing_WrongPin() {
        Assertions.assertThrows(NoSuchElementException.class, () -> chefPage.detailsDiv.isDisplayed());
        Assertions.assertEquals("Chicken sandwich", chefPage.getRowsFromTable(0).get(0).getText());
        chefPage.getRowsFromTable(0).get(0).click();
        chefPage.ensureItemDetailsIsDisplayed();
        Assertions.assertTrue(chefPage.detailsDiv.isDisplayed());
        chefPage.prepareButton.click();
        chefPage.enterPin("1112");
        chefPage.pinButton.click();
        Assertions.assertEquals("Chicken sandwich", chefPage.getRowsFromTable(0).get(0).getText());
        chefPage.closeDetailsDiv();
        Assertions.assertThrows(NoSuchElementException.class, () -> chefPage.detailsDiv.isDisplayed());
    }

    @Order(3)
    @Test
    public void succesfullyPreparing_ItemMovedToPreparationTable() {
        Assertions.assertEquals(1, chefPage.getRowsFromTable(0).size());
        Assertions.assertEquals(1, chefPage.getRowsFromTable(1).size());
        Assertions.assertThrows(NoSuchElementException.class, () -> chefPage.detailsDiv.isDisplayed());
        Assertions.assertEquals("Chicken sandwich", chefPage.getRowsFromTable(0).get(0).getText());
        chefPage.getRowsFromTable(0).get(0).click();
        chefPage.ensureItemDetailsIsDisplayed();
        Assertions.assertTrue(chefPage.detailsDiv.isDisplayed());
        chefPage.prepareButton.click();
        chefPage.enterPin("1113");
        chefPage.pinButton.click();
        chefPage.ensureItemFromOnHoldIsMoved();
        Assertions.assertEquals(2, chefPage.getRowsFromTable(1).size());
        Assertions.assertEquals(0, chefPage.getRowsFromTable(0).size());
    }

    @Order(4)
    @Test
    public void succesfullyMaking_ItemMovedToReadyTable() {
        Assertions.assertEquals(2, chefPage.getRowsFromTable(1).size());
        Assertions.assertEquals(1, chefPage.getRowsFromTable(2).size());
        Assertions.assertThrows(NoSuchElementException.class, () -> chefPage.detailsDiv.isDisplayed());
        chefPage.getRowsFromTable(1).get(0).click();
        chefPage.ensureItemDetailsIsDisplayed();
        Assertions.assertTrue(chefPage.detailsDiv.isDisplayed());
        chefPage.readyButton.click();
        chefPage.enterPin("1113");
        chefPage.pinButton.click();
        chefPage.ensureItemFromPreparationToReadyIsMoved();
        Assertions.assertEquals(2, chefPage.getRowsFromTable(2).size());
        Assertions.assertEquals(1, chefPage.getRowsFromTable(1).size());
    }

    @AfterAll
    public static void tearDown() {
        browser.quit();
    }

}
