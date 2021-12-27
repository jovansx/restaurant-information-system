package akatsuki.restaurantsysteminformation.selenium;

import akatsuki.restaurantsysteminformation.seleniumpages.AdminAdministratorsPage;
import akatsuki.restaurantsysteminformation.seleniumpages.AdminRestaurantViewPage;
import akatsuki.restaurantsysteminformation.seleniumpages.LoginPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminFunctionalitiesTest {

    private static WebDriver browser;

    private static LoginPage loginPage;
    private static AdminAdministratorsPage adminSystemAdministratorsPage;
    private static AdminRestaurantViewPage adminRestaurantViewPage;

    @BeforeAll
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");

        browser = new ChromeDriver();
        browser.manage().window().maximize();
        browser.navigate().to("http://localhost:4200/login");

        loginPage = PageFactory.initElements(browser, LoginPage.class);
        adminSystemAdministratorsPage = PageFactory.initElements(browser, AdminAdministratorsPage.class);
        adminRestaurantViewPage = PageFactory.initElements(browser, AdminRestaurantViewPage.class);
    }

    @Test
    @Order(1)
    public void badCredentials() {
        loginPage.ensureIsDisplayedLoginBtn();
        Assertions.assertEquals(browser.getTitle(), "RestaurantInformationSystemFrontend");

        loginPage.setUsernameInput("michaeldouglas");
        loginPage.setPasswordInput("badpassword");
        loginPage.clickLoginButton();

        loginPage.ensureIsDisplayedLoginBtn();

        Assertions.assertEquals("http://localhost:4200/login", browser.getCurrentUrl());
    }

    @Test
    @Order(2)
    public void successfulLogin() {
        loginPage.ensureIsDisplayedLoginBtn();
        Assertions.assertEquals(browser.getTitle(), "RestaurantInformationSystemFrontend");

        loginPage.setUsernameInput("michaeldouglas");
        loginPage.setPasswordInput("michaeldouglas");
        loginPage.clickLoginButton();

        loginPage.ensureIsNotDisplayedLoginBtn(); // Ensure there's no login btn -> it means url has been changed

        Assertions.assertEquals("http://localhost:4200/home/admin/restaurant-view", browser.getCurrentUrl());

        browser.navigate().to("http://localhost:4200/home/admin/administrators");
    }

    @Test
    @Order(4)
    public void successfulEditing() throws InterruptedException {
        Assertions.assertEquals(1, adminSystemAdministratorsPage.getTableRows().size());
        Assertions.assertFalse(adminSystemAdministratorsPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Assertions.assertFalse(adminSystemAdministratorsPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("Liam", adminSystemAdministratorsPage.getFirstNameInput().getAttribute("value"));  // Check inputs values set to initially selected row
        Assertions.assertEquals("0611111116", adminSystemAdministratorsPage.getPhoneNumberInput().getAttribute("value"));

        adminSystemAdministratorsPage.clickButton(0);  // Enable editing
        Assertions.assertTrue(adminSystemAdministratorsPage.getFirstNameInput().isEnabled());  // Check inputs are enabled
        Assertions.assertTrue(adminSystemAdministratorsPage.getPhoneNumberInput().isEnabled());
        adminSystemAdministratorsPage.setFirstNameInput("Liamor"); // Enter new values
        adminSystemAdministratorsPage.setPhoneNumber("0645558888");
        Assertions.assertEquals("Liamor", adminSystemAdministratorsPage.getFirstNameInput().getAttribute("value"));    // Check if new values are set to inputs
        Assertions.assertEquals("0645558888", adminSystemAdministratorsPage.getPhoneNumberInput().getAttribute("value"));

        adminSystemAdministratorsPage.clickButton(1);  // Click on save button
        Thread.sleep(1000);
        Assertions.assertFalse(adminSystemAdministratorsPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Assertions.assertFalse(adminSystemAdministratorsPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("Liamor Neeson", adminSystemAdministratorsPage.getTableRows().get(0).findElement(By.xpath("td[2]")).getText()); // Check if new values are updated in the table
        Assertions.assertEquals("0645558888", adminSystemAdministratorsPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText());

        adminSystemAdministratorsPage.clickButton(0);
        adminSystemAdministratorsPage.setFirstNameInput("Liam");
        adminSystemAdministratorsPage.setPhoneNumber("0611111116");
        adminSystemAdministratorsPage.clickButton(1);
        Thread.sleep(1000);
        Assertions.assertEquals("Liam Neeson", adminSystemAdministratorsPage.getTableRows().get(0).findElement(By.xpath("td[2]")).getText());
        Assertions.assertEquals("0611111116", adminSystemAdministratorsPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText());
    }

    @Test
    @Order(3)
    public void failEditing_BadFirstName() {
        Assertions.assertEquals(1, adminSystemAdministratorsPage.getTableRows().size());
        Assertions.assertFalse(adminSystemAdministratorsPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Assertions.assertFalse(adminSystemAdministratorsPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("Liam", adminSystemAdministratorsPage.getFirstNameInput().getAttribute("value"));  // Check inputs values set to initially selected row
        Assertions.assertEquals("0611111116", adminSystemAdministratorsPage.getPhoneNumberInput().getAttribute("value"));

        adminSystemAdministratorsPage.getButtons().get(0).click();  // Enable editing
        Assertions.assertTrue(adminSystemAdministratorsPage.getFirstNameInput().isEnabled());  // Check inputs are enabled
        Assertions.assertTrue(adminSystemAdministratorsPage.getPhoneNumberInput().isEnabled());
        adminSystemAdministratorsPage.setPhoneNumber("06933skl"); // Enter new value for phone number
        Assertions.assertEquals("06933skl", adminSystemAdministratorsPage.getPhoneNumberInput().getAttribute("value"));    // Check if new value is set to input

        adminSystemAdministratorsPage.clickButton(1);  // Click on save button
        Assertions.assertTrue(adminSystemAdministratorsPage.getFirstNameInput().isEnabled()); // Check inputs are enabled, save should not be triggered if inputs are not valid
        Assertions.assertTrue(adminSystemAdministratorsPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("0611111116", adminSystemAdministratorsPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText()); // Check if new value is not saved

        adminSystemAdministratorsPage.clickButton(0);  // Click on cancel button
        Assertions.assertFalse(adminSystemAdministratorsPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Assertions.assertFalse(adminSystemAdministratorsPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("Liam Neeson", adminSystemAdministratorsPage.getTableRows().get(0).findElement(By.xpath("td[2]")).getText());
        Assertions.assertEquals("0611111116", adminSystemAdministratorsPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText());
    }

    @Test
    @Order(5)
    public void successfulAddingAndDeletingOfSystemAdmin() {
        adminSystemAdministratorsPage.ensureAddButtonIsDisplayed();
        Assertions.assertTrue(adminSystemAdministratorsPage.getAddBtn().isDisplayed());

        adminSystemAdministratorsPage.getAddBtn().click();

        adminSystemAdministratorsPage.ensureDialogFieldsAreDisplayed();
        adminSystemAdministratorsPage.getDialogFields().get(0).findElement(By.xpath("div/div[1]/div/input")).sendKeys("Lucian");
        adminSystemAdministratorsPage.getDialogFields().get(1).findElement(By.xpath("div/div[1]/div/input")).sendKeys("List");
        adminSystemAdministratorsPage.getDialogFields().get(2).findElement(By.xpath("div/div[1]/div/input")).sendKeys("lucian@gmail.com");
        adminSystemAdministratorsPage.getDialogFields().get(3).findElement(By.xpath("div/div[1]/div/input")).sendKeys("lucian_stronger");
        adminSystemAdministratorsPage.getDialogFields().get(4).findElement(By.xpath("div/div[1]/div/input")).sendKeys("lucian_stronger123");
        adminSystemAdministratorsPage.getDialogFields().get(5).findElement(By.xpath("div/div[1]/div/input")).sendKeys("lucian_stronger123");
        adminSystemAdministratorsPage.getDialogFields().get(6).findElement(By.xpath("div/div[1]/div/input")).sendKeys("40000");
        adminSystemAdministratorsPage.getDialogFields().get(7).findElement(By.xpath("div/div[1]/div/input")).sendKeys("0645666666");

        adminSystemAdministratorsPage.ensureDialogButtonsAreDisplayed();
        adminSystemAdministratorsPage.getDialogButtons().get(1).click();

        adminSystemAdministratorsPage.ensureRowsAreDisplayed(2);
        Assertions.assertEquals(2, adminSystemAdministratorsPage.getTableRows().size());

        for (WebElement el : adminSystemAdministratorsPage.getTableRows()) {
            if (el.findElement(By.xpath("td[2]")).getText().equals("Lucian List")) {
                el.findElement(By.xpath("td[5]/button")).click();
                break;
            }
        }

        adminSystemAdministratorsPage.ensureRowsAreDisplayed(1);
        Assertions.assertEquals(1, adminSystemAdministratorsPage.getTableRows().size());
    }

    @Test
    @Order(6)
    public void checkingIfButtonsAreDisabled() {
        browser.navigate().to("http://localhost:4200/home/admin/restaurant-view");

        Assertions.assertTrue(adminRestaurantViewPage.addRoomButton.isEnabled());
        Assertions.assertFalse(adminRestaurantViewPage.renameRoomButton.isEnabled());
        Assertions.assertFalse(adminRestaurantViewPage.deleteRoomButton.isEnabled());
        Assertions.assertFalse(adminRestaurantViewPage.editRoomButton.isEnabled());
        Assertions.assertFalse(adminRestaurantViewPage.applyLayoutButton.isEnabled());
        Assertions.assertFalse(adminRestaurantViewPage.saveButton.isEnabled());
        Assertions.assertFalse(adminRestaurantViewPage.cancelButton.isEnabled());
        Assertions.assertFalse(adminRestaurantViewPage.rowInput.isEnabled());
        Assertions.assertFalse(adminRestaurantViewPage.colInput.isEnabled());
    }

    @Test
    @Order(7)
    public void failingAddingRoom_RoomNameIsAlreadyUsed() {
        Assertions.assertEquals(2, adminRestaurantViewPage.rooms.size());
        adminRestaurantViewPage.addRoomButton.click();
        adminRestaurantViewPage.ensureDialogIsOpen();
        adminRestaurantViewPage.dialogNameInput.sendKeys("Room 1");
        adminRestaurantViewPage.dialogAddButton.click();
        adminRestaurantViewPage.ensureDialogIsClosed();
        Assertions.assertEquals(2, adminRestaurantViewPage.rooms.size());
    }


    @Test
    @Order(8)
    public void successfullyAddingRoom_RoomIsAdded() {
        Assertions.assertEquals(2, adminRestaurantViewPage.rooms.size());
        adminRestaurantViewPage.addRoomButton.click();
        adminRestaurantViewPage.ensureDialogIsOpen();
        adminRestaurantViewPage.dialogNameInput.sendKeys("Room 3");
        adminRestaurantViewPage.dialogAddButton.click();
        adminRestaurantViewPage.ensureDialogIsClosed();
        Assertions.assertEquals(3, adminRestaurantViewPage.rooms.size());
        Assertions.assertEquals("Room 3", adminRestaurantViewPage.rooms.get(2).getText());
    }

    @Test
    @Order(9)
    public void successfullyDeletingRoom_RoomDeleted() {
        Assertions.assertEquals(3, adminRestaurantViewPage.rooms.size());
        adminRestaurantViewPage.secondTab.click();
        adminRestaurantViewPage.clickButtonUntilItIsClicked(adminRestaurantViewPage.deleteRoomButton);
        adminRestaurantViewPage.ensureRoomIsDeleted();
        Assertions.assertEquals(2, adminRestaurantViewPage.rooms.size());
    }

    @Test
    @Order(10)
    public void failingRenameRoom_RoomNamAlreadyExist() {
        Assertions.assertEquals(2, adminRestaurantViewPage.rooms.size());
        adminRestaurantViewPage.secondTab.click();
        adminRestaurantViewPage.clickButtonUntilItIsClicked(adminRestaurantViewPage.renameRoomButton);
        adminRestaurantViewPage.ensureDialogIsOpen();
        adminRestaurantViewPage.dialogNameInput.clear();
        adminRestaurantViewPage.dialogNameInput.sendKeys("Room 1");
        adminRestaurantViewPage.dialogAddButton.click();
        adminRestaurantViewPage.ensureDialogIsClosed();
        Assertions.assertEquals(2, adminRestaurantViewPage.rooms.size());
        Assertions.assertEquals("Room 3", adminRestaurantViewPage.rooms.get(1).getText());
    }

    @Test
    @Order(11)
    public void successfullyRenameRoom_RoomNameIsUpdated() {
        Assertions.assertEquals(2, adminRestaurantViewPage.rooms.size());
        adminRestaurantViewPage.clickButtonUntilItIsClicked(adminRestaurantViewPage.renameRoomButton);
        adminRestaurantViewPage.ensureDialogIsOpen();
        adminRestaurantViewPage.dialogNameInput.clear();
        adminRestaurantViewPage.dialogNameInput.sendKeys("Room 2");
        adminRestaurantViewPage.dialogAddButton.click();
        adminRestaurantViewPage.ensureDialogIsClosed();
        Assertions.assertEquals(2, adminRestaurantViewPage.rooms.size());
        Assertions.assertEquals("Room 2", adminRestaurantViewPage.rooms.get(1).getText());
    }

    @Test
    @Order(12)
    public void successfullyEnablingAndCancelingEditMode_EditModeEnabledAndCanceled() {
        Assertions.assertFalse(adminRestaurantViewPage.applyLayoutButton.isEnabled());
        Assertions.assertFalse(adminRestaurantViewPage.saveButton.isEnabled());
        Assertions.assertFalse(adminRestaurantViewPage.cancelButton.isEnabled());
        Assertions.assertFalse(adminRestaurantViewPage.colInput.isEnabled());
        Assertions.assertFalse(adminRestaurantViewPage.rowInput.isEnabled());
        Assertions.assertTrue(adminRestaurantViewPage.editRoomButton.isEnabled());

        adminRestaurantViewPage.editRoomButton.click();
        adminRestaurantViewPage.ensureButtonIsEnabled(adminRestaurantViewPage.saveButton);


        Assertions.assertTrue(adminRestaurantViewPage.saveButton.isEnabled());
        Assertions.assertTrue(adminRestaurantViewPage.cancelButton.isEnabled());
        Assertions.assertTrue(adminRestaurantViewPage.colInput.isEnabled());
        Assertions.assertTrue(adminRestaurantViewPage.rowInput.isEnabled());
        Assertions.assertFalse(adminRestaurantViewPage.editRoomButton.isEnabled());

        adminRestaurantViewPage.cancelButton.click();
        adminRestaurantViewPage.ensureButtonIsEnabled(adminRestaurantViewPage.editRoomButton);

        Assertions.assertFalse(adminRestaurantViewPage.saveButton.isEnabled());
        Assertions.assertFalse(adminRestaurantViewPage.cancelButton.isEnabled());
        Assertions.assertFalse(adminRestaurantViewPage.colInput.isEnabled());
        Assertions.assertFalse(adminRestaurantViewPage.rowInput.isEnabled());
        Assertions.assertTrue(adminRestaurantViewPage.editRoomButton.isEnabled());
    }

    @Test
    @Order(13)
    public void successfullyEnablingAndAddingTableAndCancelingEditMode_EditModeEnabledAndCanceled() {
        adminRestaurantViewPage.editRoomButton.click();
        adminRestaurantViewPage.ensureButtonIsEnabled(adminRestaurantViewPage.saveButton);

        adminRestaurantViewPage.buttonWithCordinates00.click();
        adminRestaurantViewPage.ensureTableDialogIsShown();

        Assertions.assertEquals("Empty", adminRestaurantViewPage.tableStateInput.getText());

        adminRestaurantViewPage.tableStateInput.click();
        adminRestaurantViewPage.ensureTableStateSelectIsShown();

        adminRestaurantViewPage.circleButton.click();

        adminRestaurantViewPage.tableNameInput.clear();
        adminRestaurantViewPage.tableNameInput.sendKeys("T1");

        adminRestaurantViewPage.tableDialogOkButton.click();
        adminRestaurantViewPage.ensureNumberOfTablesIs(1);
        Assertions.assertEquals(1, adminRestaurantViewPage.tables.size());


        adminRestaurantViewPage.cancelButton.click();
        adminRestaurantViewPage.ensureButtonIsEnabled(adminRestaurantViewPage.editRoomButton);
        Assertions.assertEquals(0, adminRestaurantViewPage.tables.size());
    }

    @Test
    @Order(14)
    public void successfullyAddingTable_TableAddedToRoom() {
        adminRestaurantViewPage.editRoomButton.click();
        adminRestaurantViewPage.ensureButtonIsEnabled(adminRestaurantViewPage.saveButton);

        adminRestaurantViewPage.buttonWithCordinates00.click();
        adminRestaurantViewPage.ensureTableDialogIsShown();

        Assertions.assertEquals("Empty", adminRestaurantViewPage.tableStateInput.getText());

        adminRestaurantViewPage.tableStateInput.click();
        adminRestaurantViewPage.ensureTableStateSelectIsShown();

        adminRestaurantViewPage.circleButton.click();

        adminRestaurantViewPage.tableNameInput.clear();
        adminRestaurantViewPage.tableNameInput.sendKeys("T1");

        adminRestaurantViewPage.tableDialogOkButton.click();
        adminRestaurantViewPage.ensureNumberOfTablesIs(1);
        Assertions.assertEquals(1, adminRestaurantViewPage.tables.size());


        adminRestaurantViewPage.saveButton.click();
        adminRestaurantViewPage.ensureButtonIsEnabled(adminRestaurantViewPage.editRoomButton);
        Assertions.assertEquals(1, adminRestaurantViewPage.tables.size());
        Assertions.assertEquals("T1", adminRestaurantViewPage.getNameOfTable(adminRestaurantViewPage.tables.get(0)));
    }

    @Test
    @Order(15)
    public void successfullyEditingTable_TableUpdated() {
        adminRestaurantViewPage.editRoomButton.click();
        adminRestaurantViewPage.ensureButtonIsEnabled(adminRestaurantViewPage.saveButton);

        adminRestaurantViewPage.buttonWithCordinates00.click();
        adminRestaurantViewPage.ensureTableDialogIsShown();

        Assertions.assertEquals("Circle", adminRestaurantViewPage.tableStateInput.getText());

        adminRestaurantViewPage.tableStateInput.click();
        adminRestaurantViewPage.ensureTableStateSelectIsShown();

        adminRestaurantViewPage.squareButton.click();

        adminRestaurantViewPage.tableNameInput.clear();
        adminRestaurantViewPage.tableNameInput.sendKeys("T2");

        adminRestaurantViewPage.tableDialogOkButton.click();
        adminRestaurantViewPage.ensureNumberOfTablesIs(1);
        Assertions.assertEquals(1, adminRestaurantViewPage.tables.size());


        adminRestaurantViewPage.clickButtonUntilItIsClicked(adminRestaurantViewPage.saveButton);
        Assertions.assertEquals(1, adminRestaurantViewPage.tables.size());
        Assertions.assertEquals("T2", adminRestaurantViewPage.getNameOfTable(adminRestaurantViewPage.tables.get(0)));
    }

    @Test
    @Order(16)
    public void successfullyRemovingTable_TableDeleted() {
        adminRestaurantViewPage.editRoomButton.click();
        adminRestaurantViewPage.ensureButtonIsEnabled(adminRestaurantViewPage.saveButton);

        adminRestaurantViewPage.buttonWithCordinates00.click();
        adminRestaurantViewPage.ensureTableDialogIsShown();

        Assertions.assertEquals("Square", adminRestaurantViewPage.tableStateInput.getText());

        adminRestaurantViewPage.tableStateInput.click();
        adminRestaurantViewPage.ensureTableStateSelectIsShown();

        adminRestaurantViewPage.emptyButton.click();

        adminRestaurantViewPage.tableDialogOkButton.click();
        adminRestaurantViewPage.ensureNumberOfTablesIs(0);
        Assertions.assertEquals(0, adminRestaurantViewPage.tables.size());


        adminRestaurantViewPage.saveButton.click();
        Assertions.assertEquals(0, adminRestaurantViewPage.tables.size());
    }

    @Test
    @Order(17)
    public void successfullyChangingLayoutOfTable_TableLayoutUpdated() {
        adminRestaurantViewPage.editRoomButton.click();
        adminRestaurantViewPage.ensureButtonIsEnabled(adminRestaurantViewPage.saveButton);

        adminRestaurantViewPage.rowInput.clear();
        adminRestaurantViewPage.rowInput.sendKeys("3");
        adminRestaurantViewPage.colInput.clear();
        adminRestaurantViewPage.colInput.sendKeys("3");

        adminRestaurantViewPage.applyLayoutButton.click();
        adminRestaurantViewPage.ensureTableLayoutIsUpdated();

        adminRestaurantViewPage.saveButton.click();

        Assertions.assertTrue(adminRestaurantViewPage.buttonWithCordinates02.isDisplayed());
    }


}
