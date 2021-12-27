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
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminFunctionalitiesTest {

    private static WebDriver browser;

    private static LoginPage loginPage;
    private static AdminAdministratorsPage adminPage;
    private static AdminRestaurantViewPage restaurantPage;

    @BeforeAll
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");

        browser = new ChromeDriver();
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
    @Order(4)
    public void successfulEditing() {
        assertEquals(1, adminPage.getTableRows(1).size());
        Utilities.elementDisabledWait(browser, adminPage.getFirstNameInput(), 10);
        assertFalse(adminPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Utilities.elementDisabledWait(browser, adminPage.getPhoneNumberInput(), 10);
        assertFalse(adminPage.getPhoneNumberInput().isEnabled());
        assertEquals("Liam", adminPage.getFirstNameInput().getAttribute("value"));  // Check inputs values set to initially selected row
        assertEquals("0611111116", adminPage.getPhoneNumberInput().getAttribute("value"));

        adminPage.clickButton(0);  // Enable editing
        Utilities.elementEnabledWait(browser, adminPage.getFirstNameInput(), 10);
        assertTrue(adminPage.getFirstNameInput().isEnabled());  // Check inputs are enabled
        Utilities.elementEnabledWait(browser, adminPage.getPhoneNumberInput(), 10);
        assertTrue(adminPage.getPhoneNumberInput().isEnabled());
        adminPage.setFirstNameInput("Liamor"); // Enter new values
        adminPage.setPhoneNumber("0645558888");
        assertEquals("Liamor", adminPage.getFirstNameInput().getAttribute("value"));    // Check if new values are set to inputs
        assertEquals("0645558888", adminPage.getPhoneNumberInput().getAttribute("value"));

        adminPage.clickButton(1);  // Click on save button
        Utilities.elementDisabledWait(browser, adminPage.getFirstNameInput(), 10);
        assertFalse(adminPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Utilities.elementDisabledWait(browser, adminPage.getPhoneNumberInput(), 10);
        assertFalse(adminPage.getPhoneNumberInput().isEnabled());
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
    @Order(3)
    public void failEditing_BadFirstName() {
        assertEquals(1, adminPage.getTableRows(1).size());
        Utilities.elementDisabledWait(browser, adminPage.getFirstNameInput(), 10);
        assertFalse(adminPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Utilities.elementDisabledWait(browser, adminPage.getPhoneNumberInput(), 10);
        assertFalse(adminPage.getPhoneNumberInput().isEnabled());
        assertEquals("Liam", adminPage.getFirstNameInput().getAttribute("value"));  // Check inputs values set to initially selected row
        assertEquals("0611111116", adminPage.getPhoneNumberInput().getAttribute("value"));

        adminPage.getButtons().get(0).click();  // Enable editing
        Utilities.elementEnabledWait(browser, adminPage.getFirstNameInput(), 10);
        assertTrue(adminPage.getFirstNameInput().isEnabled());  // Check inputs are enabled
        Utilities.elementEnabledWait(browser, adminPage.getPhoneNumberInput(), 10);
        assertTrue(adminPage.getPhoneNumberInput().isEnabled());
        adminPage.setPhoneNumber("06933skl"); // Enter new value for phone number
        assertEquals("06933skl", adminPage.getPhoneNumberInput().getAttribute("value"));    // Check if new value is set to input

        adminPage.clickButton(1);  // Click on save button
        Utilities.elementEnabledWait(browser, adminPage.getFirstNameInput(), 10);
        assertTrue(adminPage.getFirstNameInput().isEnabled()); // Check inputs are enabled, save should not be triggered if inputs are not valid
        Utilities.elementEnabledWait(browser, adminPage.getPhoneNumberInput(), 10);
        assertTrue(adminPage.getPhoneNumberInput().isEnabled());
        assertEquals("0611111116", adminPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText()); // Check if new value is not saved

        adminPage.clickButton(0);  // Click on cancel button
        Utilities.elementDisabledWait(browser, adminPage.getFirstNameInput(), 10);
        assertFalse(adminPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Utilities.elementDisabledWait(browser, adminPage.getPhoneNumberInput(), 10);
        assertFalse(adminPage.getPhoneNumberInput().isEnabled());
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
            if (el.findElement(By.xpath("td[2]")).getText().equals("Lucian List")) {
                el.findElement(By.xpath("td[5]/button")).click();
                break;
            }
        }

        assertEquals(1, adminPage.getTableRows(1).size());

    }

    @Test
    @Order(6)
    public void checkingIfButtonsAreDisabled() {
        browser.navigate().to("http://localhost:4200/home/admin/restaurant-view");

        Assertions.assertTrue(restaurantPage.addRoomButton.isEnabled());
        Assertions.assertFalse(restaurantPage.renameRoomButton.isEnabled());
        Assertions.assertFalse(restaurantPage.deleteRoomButton.isEnabled());
        Assertions.assertFalse(restaurantPage.editRoomButton.isEnabled());
        Assertions.assertFalse(restaurantPage.applyLayoutButton.isEnabled());
        Assertions.assertFalse(restaurantPage.saveButton.isEnabled());
        Assertions.assertFalse(restaurantPage.cancelButton.isEnabled());
        Assertions.assertFalse(restaurantPage.rowInput.isEnabled());
        Assertions.assertFalse(restaurantPage.colInput.isEnabled());
    }

    @Test
    @Order(7)
    public void failingAddingRoom_RoomNameIsAlreadyUsed() {
        Assertions.assertEquals(2, restaurantPage.rooms.size());
        restaurantPage.addRoomButton.click();
        restaurantPage.ensureDialogIsOpen();
        restaurantPage.dialogNameInput.sendKeys("Room 1");
        restaurantPage.dialogAddButton.click();
        restaurantPage.ensureDialogIsClosed();
        Assertions.assertEquals(2, restaurantPage.rooms.size());
    }


    @Test
    @Order(8)
    public void successfullyAddingRoom_RoomIsAdded() {
        Assertions.assertEquals(2, restaurantPage.rooms.size());
        restaurantPage.addRoomButton.click();
        restaurantPage.ensureDialogIsOpen();
        restaurantPage.dialogNameInput.sendKeys("Room 3");
        restaurantPage.dialogAddButton.click();
        restaurantPage.ensureDialogIsClosed();
        Assertions.assertEquals(3, restaurantPage.rooms.size());
        Assertions.assertEquals("Room 3", restaurantPage.rooms.get(2).getText());
    }

    @Test
    @Order(9)
    public void successfullyDeletingRoom_RoomDeleted() {
        Assertions.assertEquals(3, restaurantPage.rooms.size());
        restaurantPage.secondTab.click();
        restaurantPage.clickButtonUntilItIsClicked(restaurantPage.deleteRoomButton);
        restaurantPage.ensureRoomIsDeleted();
        Assertions.assertEquals(2, restaurantPage.rooms.size());
    }

    @Test
    @Order(10)
    public void failingRenameRoom_RoomNamAlreadyExist() {
        Assertions.assertEquals(2, restaurantPage.rooms.size());
        restaurantPage.secondTab.click();
        restaurantPage.clickButtonUntilItIsClicked(restaurantPage.renameRoomButton);
        restaurantPage.ensureDialogIsOpen();
        restaurantPage.dialogNameInput.clear();
        restaurantPage.dialogNameInput.sendKeys("Room 1");
        restaurantPage.dialogAddButton.click();
        restaurantPage.ensureDialogIsClosed();
        Assertions.assertEquals(2, restaurantPage.rooms.size());
        Assertions.assertEquals("Room 3", restaurantPage.rooms.get(1).getText());
    }

    @Test
    @Order(11)
    public void successfullyRenameRoom_RoomNameIsUpdated() {
        Assertions.assertEquals(2, restaurantPage.rooms.size());
        restaurantPage.clickButtonUntilItIsClicked(restaurantPage.renameRoomButton);
        restaurantPage.ensureDialogIsOpen();
        restaurantPage.dialogNameInput.clear();
        restaurantPage.dialogNameInput.sendKeys("Room 2");
        restaurantPage.dialogAddButton.click();
        restaurantPage.ensureDialogIsClosed();
        Assertions.assertEquals(2, restaurantPage.rooms.size());
        Assertions.assertEquals("Room 2", restaurantPage.rooms.get(1).getText());
    }

    @Test
    @Order(12)
    public void successfullyEnablingAndCancelingEditMode_EditModeEnabledAndCanceled() {
        Assertions.assertFalse(restaurantPage.applyLayoutButton.isEnabled());
        Assertions.assertFalse(restaurantPage.saveButton.isEnabled());
        Assertions.assertFalse(restaurantPage.cancelButton.isEnabled());
        Assertions.assertFalse(restaurantPage.colInput.isEnabled());
        Assertions.assertFalse(restaurantPage.rowInput.isEnabled());
        Assertions.assertTrue(restaurantPage.editRoomButton.isEnabled());

        restaurantPage.editRoomButton.click();
        restaurantPage.ensureButtonIsEnabled(restaurantPage.saveButton);


        Assertions.assertTrue(restaurantPage.saveButton.isEnabled());
        Assertions.assertTrue(restaurantPage.cancelButton.isEnabled());
        Assertions.assertTrue(restaurantPage.colInput.isEnabled());
        Assertions.assertTrue(restaurantPage.rowInput.isEnabled());
        Assertions.assertFalse(restaurantPage.editRoomButton.isEnabled());

        restaurantPage.cancelButton.click();
        restaurantPage.ensureButtonIsEnabled(restaurantPage.editRoomButton);

        Assertions.assertFalse(restaurantPage.saveButton.isEnabled());
        Assertions.assertFalse(restaurantPage.cancelButton.isEnabled());
        Assertions.assertFalse(restaurantPage.colInput.isEnabled());
        Assertions.assertFalse(restaurantPage.rowInput.isEnabled());
        Assertions.assertTrue(restaurantPage.editRoomButton.isEnabled());
    }

    @Test
    @Order(13)
    public void successfullyEnablingAndAddingTableAndCancelingEditMode_EditModeEnabledAndCanceled() {
        restaurantPage.editRoomButton.click();
        restaurantPage.ensureButtonIsEnabled(restaurantPage.saveButton);

        restaurantPage.buttonWithCordinates00.click();
        restaurantPage.ensureTableDialogIsShown();

        Assertions.assertEquals("Empty", restaurantPage.tableStateInput.getText());

        restaurantPage.tableStateInput.click();
        restaurantPage.ensureTableStateSelectIsShown();

        restaurantPage.circleButton.click();

        restaurantPage.tableNameInput.clear();
        restaurantPage.tableNameInput.sendKeys("T1");

        restaurantPage.tableDialogOkButton.click();
        restaurantPage.ensureNumberOfTablesIs(1);
        Assertions.assertEquals(1, restaurantPage.tables.size());


        restaurantPage.cancelButton.click();
        restaurantPage.ensureButtonIsEnabled(restaurantPage.editRoomButton);
        Assertions.assertEquals(0, restaurantPage.tables.size());
    }

    @Test
    @Order(14)
    public void successfullyAddingTable_TableAddedToRoom() {
        restaurantPage.editRoomButton.click();
        restaurantPage.ensureButtonIsEnabled(restaurantPage.saveButton);

        restaurantPage.buttonWithCordinates00.click();
        restaurantPage.ensureTableDialogIsShown();

        Assertions.assertEquals("Empty", restaurantPage.tableStateInput.getText());

        restaurantPage.tableStateInput.click();
        restaurantPage.ensureTableStateSelectIsShown();

        restaurantPage.circleButton.click();

        restaurantPage.tableNameInput.clear();
        restaurantPage.tableNameInput.sendKeys("T1");

        restaurantPage.tableDialogOkButton.click();
        restaurantPage.ensureNumberOfTablesIs(1);
        Assertions.assertEquals(1, restaurantPage.tables.size());


        restaurantPage.saveButton.click();
        restaurantPage.ensureButtonIsEnabled(restaurantPage.editRoomButton);
        Assertions.assertEquals(1, restaurantPage.tables.size());
        Assertions.assertEquals("T1", restaurantPage.getNameOfTable(restaurantPage.tables.get(0)));
    }

    @Test
    @Order(15)
    public void successfullyEditingTable_TableUpdated() {
        restaurantPage.editRoomButton.click();
        restaurantPage.ensureButtonIsEnabled(restaurantPage.saveButton);

        restaurantPage.buttonWithCordinates00.click();
        restaurantPage.ensureTableDialogIsShown();

        Assertions.assertEquals("Circle", restaurantPage.tableStateInput.getText());

        restaurantPage.tableStateInput.click();
        restaurantPage.ensureTableStateSelectIsShown();

        restaurantPage.squareButton.click();

        restaurantPage.tableNameInput.clear();
        restaurantPage.tableNameInput.sendKeys("T2");

        restaurantPage.tableDialogOkButton.click();
        restaurantPage.ensureNumberOfTablesIs(1);
        Assertions.assertEquals(1, restaurantPage.tables.size());


        restaurantPage.clickButtonUntilItIsClicked(restaurantPage.saveButton);
        Assertions.assertEquals(1, restaurantPage.tables.size());
        Assertions.assertEquals("T2", restaurantPage.getNameOfTable(restaurantPage.tables.get(0)));
    }

    @Test
    @Order(16)
    public void successfullyRemovingTable_TableDeleted() {
        restaurantPage.editRoomButton.click();
        restaurantPage.ensureButtonIsEnabled(restaurantPage.saveButton);

        restaurantPage.buttonWithCordinates00.click();
        restaurantPage.ensureTableDialogIsShown();

        Assertions.assertEquals("Square", restaurantPage.tableStateInput.getText());

        restaurantPage.tableStateInput.click();
        restaurantPage.ensureTableStateSelectIsShown();

        restaurantPage.emptyButton.click();

        restaurantPage.tableDialogOkButton.click();
        restaurantPage.ensureNumberOfTablesIs(0);
        Assertions.assertEquals(0, restaurantPage.tables.size());


        restaurantPage.saveButton.click();
        Assertions.assertEquals(0, restaurantPage.tables.size());
    }

    @Test
    @Order(17)
    public void successfullyChangingLayoutOfTable_TableLayoutUpdated() {
        restaurantPage.editRoomButton.click();
        restaurantPage.ensureButtonIsEnabled(restaurantPage.saveButton);

        restaurantPage.rowInput.clear();
        restaurantPage.rowInput.sendKeys("3");
        restaurantPage.colInput.clear();
        restaurantPage.colInput.sendKeys("3");

        restaurantPage.applyLayoutButton.click();
        restaurantPage.ensureTableLayoutIsUpdated();

        restaurantPage.saveButton.click();

        Assertions.assertTrue(restaurantPage.buttonWithCordinates02.isDisplayed());
    }


}
