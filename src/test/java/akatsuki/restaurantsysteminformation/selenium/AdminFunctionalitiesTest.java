package akatsuki.restaurantsysteminformation.selenium;

import akatsuki.restaurantsysteminformation.seleniumpages.AdminAdministratorsPage;
import akatsuki.restaurantsysteminformation.seleniumpages.AdminRestaurantViewPage;
import akatsuki.restaurantsysteminformation.seleniumpages.LoginPage;
import akatsuki.restaurantsysteminformation.seleniumpages.Utilities;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminFunctionalitiesTest {

    private WebDriver browser;

    private LoginPage loginPage;
    private AdminAdministratorsPage adminPage;
    private AdminRestaurantViewPage restaurantPage;

    @BeforeAll
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);

        browser = new ChromeDriver(options);
        browser.manage().window().maximize();
        browser.navigate().to("http://localhost:4200/login");

        loginPage = PageFactory.initElements(browser, LoginPage.class);
        adminPage = PageFactory.initElements(browser, AdminAdministratorsPage.class);
        restaurantPage = PageFactory.initElements(browser, AdminRestaurantViewPage.class);
    }

    @Test
    @Order(1)
    public void badCredentials() {
        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/login", 10));
        assertEquals(browser.getTitle(), "RestaurantInformationSystemFrontend");

        loginPage.login("michaeldouglas", "badpassword");

        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/login", 10));
    }

    @Test
    @Order(2)
    public void successfulLogin() {
        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/login", 10));

        loginPage.login("michaeldouglas", "michaeldouglas");

        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/home/admin/restaurant-view", 10));
        browser.navigate().to("http://localhost:4200/home/admin/administrators");
        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/home/admin/administrators", 10));
    }

    @Test
    @Order(3)
    public void successfulEditing() {
        assertEquals(1, adminPage.getTableRows(1).size());
        assertTrue(Utilities.elementDisabledWait(browser, adminPage.getFirstNameInput(), 10)); // Check inputs are disabled
        assertTrue(Utilities.elementDisabledWait(browser, adminPage.getPhoneNumberInput(), 10));
        assertEquals("Liam", adminPage.getFirstNameInput().getAttribute("value"));  // Check inputs values set to initially selected row
        assertEquals("0611111116", adminPage.getPhoneNumberInput().getAttribute("value"));

        adminPage.clickButton(0);  // Enable editing
        assertTrue(Utilities.elementEnabledWait(browser, adminPage.getFirstNameInput(), 10));  // Check inputs are enabled
        assertTrue(Utilities.elementEnabledWait(browser, adminPage.getPhoneNumberInput(), 10));
        adminPage.setFirstNameInput("Liamor"); // Enter new values
        adminPage.setPhoneNumber("0645558888");
        assertEquals("Liamor", adminPage.getFirstNameInput().getAttribute("value"));    // Check if new values are set to inputs
        assertEquals("0645558888", adminPage.getPhoneNumberInput().getAttribute("value"));

        adminPage.clickButton(1);  // Click on save button
        assertTrue(Utilities.elementDisabledWait(browser, adminPage.getFirstNameInput(), 10)); // Check inputs are disabled
        assertTrue(Utilities.elementDisabledWait(browser, adminPage.getPhoneNumberInput(), 10));
        assertEquals("Liamor Neeson", adminPage.getTableRows().get(0).findElement(By.xpath("td[2]")).getText()); // Check if new values are updated in the table
        assertEquals("0645558888", adminPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText());

        adminPage.clickButton(0);
        adminPage.setFirstNameInput("Liam");
        adminPage.setPhoneNumber("0611111116");
        adminPage.clickButton(1);
        Utilities.numberOfElementsWait(browser, adminPage.getButtons(), 1, 10);
        assertEquals("Liam Neeson", adminPage.getTableRows().get(0).findElement(By.xpath("td[2]")).getText());
        assertEquals("0611111116", adminPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText());
    }

    @Test
    @Order(4)
    public void failEditing_BadFirstName() {
        assertEquals(1, adminPage.getTableRows(1).size());
        assertTrue(Utilities.elementDisabledWait(browser, adminPage.getFirstNameInput(), 10)); // Check inputs are disabled
        assertTrue(Utilities.elementDisabledWait(browser, adminPage.getPhoneNumberInput(), 10));
        assertEquals("Liam", adminPage.getFirstNameInput().getAttribute("value"));  // Check inputs values set to initially selected row
        assertEquals("0611111116", adminPage.getPhoneNumberInput().getAttribute("value"));

        adminPage.getButtons().get(0).click();  // Enable editing
        assertTrue(Utilities.elementEnabledWait(browser, adminPage.getFirstNameInput(), 10));  // Check inputs are enabled
        assertTrue(Utilities.elementEnabledWait(browser, adminPage.getPhoneNumberInput(), 10));
        adminPage.setPhoneNumber("06933skl"); // Enter new value for phone number
        assertEquals("06933skl", adminPage.getPhoneNumberInput().getAttribute("value"));    // Check if new value is set to input

        adminPage.clickButton(1);  // Click on save button
        assertTrue(Utilities.elementEnabledWait(browser, adminPage.getFirstNameInput(), 10)); // Check inputs are enabled, save should not be triggered if inputs are not valid
        assertTrue(Utilities.elementEnabledWait(browser, adminPage.getPhoneNumberInput(), 10));
        assertEquals("0611111116", adminPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText()); // Check if new value is not saved

        adminPage.clickButton(0);  // Click on cancel button
        assertTrue(Utilities.elementDisabledWait(browser, adminPage.getFirstNameInput(), 10)); // Check inputs are disabled
        assertTrue(Utilities.elementDisabledWait(browser, adminPage.getPhoneNumberInput(), 10));
        assertEquals("Liam Neeson", adminPage.getTableRows().get(0).findElement(By.xpath("td[2]")).getText());
        assertEquals("0611111116", adminPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText());
    }

    @Test
    @Order(5)
    public void successfulAddingAndDeletingOfSystemAdmin() {
        adminPage.clickAddButton();
        adminPage.setFields("Lucian", "List", "lucian@gmail.com", "lucian_stronger",
                "lucian_stronger123", "lucian_stronger123", "40000", "0645666666");
        adminPage.clickSaveButton();

        assertEquals(2, adminPage.getTableRows(2).size());

        for (WebElement el : adminPage.getTableRows()) {
            WebElement td = Utilities.visibilityWait(browser, el.findElement(By.xpath("td[2]")), 10);
            if (td.getText().equals("Lucian List")) {
                Utilities.clickableWait(browser, el.findElement(By.xpath("td[5]/button")), 10).click();
                break;
            }
        }

        assertEquals(1, adminPage.getTableRows(1).size());

    }

    @Test
    @Order(6)
    public void checkingIfButtonsAreDisabled() {
        browser.navigate().to("http://localhost:4200/home/admin/restaurant-view");

        Assertions.assertTrue(restaurantPage.getAddRoomButton().isEnabled());
        Assertions.assertFalse(restaurantPage.getRenameRoomButton().isEnabled());
        Assertions.assertFalse(restaurantPage.getDeleteRoomButton().isEnabled());
        Assertions.assertFalse(restaurantPage.getEditRoomButton().isEnabled());
        Assertions.assertFalse(restaurantPage.getApplyLayoutButton().isEnabled());
        Assertions.assertFalse(restaurantPage.getSaveButton().isEnabled());
        Assertions.assertFalse(restaurantPage.getCancelButton().isEnabled());
        Assertions.assertFalse(restaurantPage.getRowInput().isEnabled());
        Assertions.assertFalse(restaurantPage.getColInput().isEnabled());
    }

    @Test
    @Order(7)
    public void failingAddingRoom_RoomNameIsAlreadyUsed() {
        Assertions.assertEquals(2, restaurantPage.getRooms().size());
        restaurantPage.getAddRoomButton().click();
        Utilities.visibilityWait(browser, restaurantPage.getDialogNameInput(), 10);
        restaurantPage.writeToInput(restaurantPage.getDialogNameInput(), "Room 1");
        restaurantPage.getDialogAddButton().click();
        Assertions.assertTrue(Utilities.invisibilityWait(browser, restaurantPage.getDialogNameInput(), 10));
        Assertions.assertEquals(2, restaurantPage.getRooms().size());
    }


    @Test
    @Order(8)
    public void successfullyAddingRoom_RoomIsAdded() {
        Assertions.assertEquals(2, restaurantPage.getRooms().size());
        restaurantPage.getAddRoomButton().click();
        Utilities.visibilityWait(browser, restaurantPage.getDialogNameInput(), 10);
        restaurantPage.writeToInput(restaurantPage.getDialogNameInput(), "Room 3");
        restaurantPage.getDialogAddButton().click();
        Assertions.assertTrue(Utilities.invisibilityWait(browser, restaurantPage.getDialogNameInput(), 10));
        Assertions.assertEquals(3, restaurantPage.getRooms().size());
        Assertions.assertEquals("Room 3", restaurantPage.getRooms().get(2).getText());
    }

    @Test
    @Order(9)
    public void successfullyDeletingRoom_RoomDeleted() {
        Assertions.assertEquals(3, restaurantPage.getRooms().size());
        restaurantPage.getSecondTab().click();
        Utilities.clickButtonUntilItIsClicked(restaurantPage.getDeleteRoomButton());
        Utilities.numberOfElementsWait(browser, restaurantPage.getRooms(), 2, 10);
        Assertions.assertEquals(2, restaurantPage.getRooms().size());
    }

    @Test
    @Order(10)
    public void failingRenameRoom_RoomNamAlreadyExist() {
        Assertions.assertEquals(2, restaurantPage.getRooms().size());
        restaurantPage.getSecondTab().click();
        Utilities.clickButtonUntilItIsClicked(restaurantPage.getRenameRoomButton());
        Utilities.visibilityWait(browser, restaurantPage.getDialogNameInput(), 10);
        restaurantPage.writeToInput(restaurantPage.getDialogNameInput(), "Room 1");
        restaurantPage.getDialogAddButton().click();
        Assertions.assertTrue(Utilities.invisibilityWait(browser, restaurantPage.getDialogNameInput(), 10));
        Assertions.assertEquals(2, restaurantPage.getRooms().size());
        Assertions.assertEquals("Room 3", restaurantPage.getRooms().get(1).getText());
    }

    @Test
    @Order(11)
    public void successfullyRenameRoom_RoomNameIsUpdated() {
        Assertions.assertEquals(2, restaurantPage.getRooms().size());
        Utilities.clickButtonUntilItIsClicked(restaurantPage.getRenameRoomButton());
        Utilities.visibilityWait(browser, restaurantPage.getDialogNameInput(), 10);
        restaurantPage.writeToInput(restaurantPage.getDialogNameInput(), "Room 2");
        restaurantPage.getDialogAddButton().click();
        Assertions.assertTrue(Utilities.invisibilityWait(browser, restaurantPage.getDialogNameInput(), 10));
        Assertions.assertEquals(2, restaurantPage.getRooms().size());
        Assertions.assertEquals("Room 2", restaurantPage.getRooms().get(1).getText());
    }

    @Test
    @Order(12)
    public void successfullyEnablingAndCancelingEditMode_EditModeEnabledAndCanceled() {
        Assertions.assertFalse(restaurantPage.getApplyLayoutButton().isEnabled());
        Assertions.assertFalse(restaurantPage.getSaveButton().isEnabled());
        Assertions.assertFalse(restaurantPage.getCancelButton().isEnabled());
        Assertions.assertFalse(restaurantPage.getColInput().isEnabled());
        Assertions.assertFalse(restaurantPage.getRowInput().isEnabled());
        Assertions.assertTrue(restaurantPage.getEditRoomButton().isEnabled());
        restaurantPage.getEditRoomButton().click();
        Utilities.elementEnabledWait(browser, restaurantPage.getSaveButton(), 10);
        Assertions.assertTrue(restaurantPage.getSaveButton().isEnabled());
        Assertions.assertTrue(restaurantPage.getCancelButton().isEnabled());
        Assertions.assertTrue(restaurantPage.getColInput().isEnabled());
        Assertions.assertTrue(restaurantPage.getRowInput().isEnabled());
        Assertions.assertFalse(restaurantPage.getEditRoomButton().isEnabled());
        restaurantPage.getCancelButton().click();
        Utilities.elementEnabledWait(browser, restaurantPage.getEditRoomButton(), 10);
        Assertions.assertFalse(restaurantPage.getSaveButton().isEnabled());
        Assertions.assertFalse(restaurantPage.getCancelButton().isEnabled());
        Assertions.assertFalse(restaurantPage.getColInput().isEnabled());
        Assertions.assertFalse(restaurantPage.getRowInput().isEnabled());
        Assertions.assertTrue(restaurantPage.getEditRoomButton().isEnabled());
    }

    @Test
    @Order(13)
    public void successfullyEnablingAndAddingTableAndCancelingEditMode_EditModeEnabledAndCanceled() {
        restaurantPage.getEditRoomButton().click();
        Utilities.elementEnabledWait(browser, restaurantPage.getSaveButton(), 10);
        restaurantPage.getButtonWithCordinates00().click();
        Utilities.visibilityWait(browser, restaurantPage.getTableDialogOkButton(), 10);
        Assertions.assertEquals("Empty", restaurantPage.getTableStateInput().getText());
        restaurantPage.getTableStateInput().click();
        Utilities.visibilityWait(browser, restaurantPage.getCircleButton(), 10);
        restaurantPage.getCircleButton().click();
        restaurantPage.writeToInput(restaurantPage.getTableNameInput(), "T1");
        restaurantPage.getTableDialogOkButton().click();
        Utilities.numberOfElementsWait(browser, restaurantPage.getTables(), 1, 10);
        Assertions.assertEquals(1, restaurantPage.getTables().size());
        restaurantPage.getCancelButton().click();
        Utilities.elementEnabledWait(browser, restaurantPage.getEditRoomButton(), 10);
        Assertions.assertEquals(0, restaurantPage.getTables().size());
    }

    @Test
    @Order(14)
    public void successfullyAddingTable_TableAddedToRoom() {
        restaurantPage.getEditRoomButton().click();
        Utilities.elementEnabledWait(browser, restaurantPage.getSaveButton(), 10);
        restaurantPage.getButtonWithCordinates00().click();
        Utilities.visibilityWait(browser, restaurantPage.getTableDialogOkButton(), 10);
        Assertions.assertEquals("Empty", restaurantPage.getTableStateInput().getText());
        restaurantPage.getTableStateInput().click();
        Utilities.visibilityWait(browser, restaurantPage.getCircleButton(), 10);
        restaurantPage.getCircleButton().click();
        restaurantPage.writeToInput(restaurantPage.getTableNameInput(), "T1");
        restaurantPage.getTableDialogOkButton().click();
        Utilities.numberOfElementsWait(browser, restaurantPage.getTables(), 1, 10);
        Assertions.assertEquals(1, restaurantPage.getTables().size());
        restaurantPage.getSaveButton().click();
        Utilities.elementEnabledWait(browser, restaurantPage.getEditRoomButton(), 10);
        Assertions.assertEquals(1, restaurantPage.getTables().size());
        Assertions.assertEquals("T1", restaurantPage.getNameOfTable(restaurantPage.getTables().get(0)));
    }

    @Test
    @Order(15)
    public void successfullyEditingTable_TableUpdated() {
        Utilities.clickButtonUntilItIsClicked(restaurantPage.getEditRoomButton());
        Utilities.elementEnabledWait(browser, restaurantPage.getSaveButton(), 10);
        restaurantPage.getButtonWithCordinates00().click();
        Utilities.visibilityWait(browser, restaurantPage.getTableDialogOkButton(), 10);
        Assertions.assertEquals("Circle", restaurantPage.getTableStateInput().getText());
        restaurantPage.getTableStateInput().click();
        Utilities.visibilityWait(browser, restaurantPage.getCircleButton(), 10);
        restaurantPage.getSquareButton().click();
        restaurantPage.writeToInput(restaurantPage.getTableNameInput(), "T2");
        restaurantPage.getTableDialogOkButton().click();
        Utilities.invisibilityWait(browser, restaurantPage.getTableDialogOkButton(), 10);
        Assertions.assertEquals(1, restaurantPage.getTables().size());
        Utilities.clickButtonUntilItIsClicked(restaurantPage.getSaveButton());
        Assertions.assertEquals(1, restaurantPage.getTables().size());
        Assertions.assertEquals("T2", restaurantPage.getNameOfTable(restaurantPage.getTables().get(0)));
    }

    @Test
    @Order(16)
    public void successfullyRemovingTable_TableDeleted() {
        restaurantPage.getEditRoomButton().click();
        Utilities.elementEnabledWait(browser, restaurantPage.getSaveButton(), 10);
        restaurantPage.getButtonWithCordinates00().click();
        Utilities.visibilityWait(browser, restaurantPage.getTableDialogOkButton(), 10);
        Assertions.assertEquals("Square", restaurantPage.getTableStateInput().getText());
        restaurantPage.getTableStateInput().click();
        Utilities.visibilityWait(browser, restaurantPage.getCircleButton(), 10);
        restaurantPage.getEmptyButton().click();
        restaurantPage.getTableDialogOkButton().click();
        Utilities.numberOfElementsWait(browser, restaurantPage.getTables(), 0, 10);
        Assertions.assertEquals(0, restaurantPage.getTables().size());
        restaurantPage.getSaveButton().click();
        Assertions.assertEquals(0, restaurantPage.getTables().size());
    }

    @Test
    @Order(17)
    public void successfullyChangingLayoutOfTable_TableLayoutUpdated() {
        Utilities.clickButtonUntilItIsClicked(restaurantPage.getEditRoomButton());
        Utilities.elementEnabledWait(browser, restaurantPage.getSaveButton(), 10);
        restaurantPage.writeToInput(restaurantPage.getRowInput(), "3");
        restaurantPage.writeToInput(restaurantPage.getColInput(), "3");
        restaurantPage.getApplyLayoutButton().click();
        Utilities.visibilityWait(browser, restaurantPage.getButtonWithCordinates02(), 10);
        restaurantPage.getSaveButton().click();
        Assertions.assertTrue(restaurantPage.getButtonWithCordinates02().isDisplayed());
    }

    @AfterAll
    public void tearDown() {
        browser.quit();
    }
}
