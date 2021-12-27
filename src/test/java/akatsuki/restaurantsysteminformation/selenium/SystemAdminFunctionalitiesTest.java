package akatsuki.restaurantsysteminformation.selenium;

import akatsuki.restaurantsysteminformation.seleniumpages.LoginPage;
import akatsuki.restaurantsysteminformation.seleniumpages.SystemAdminMenuPage;
import akatsuki.restaurantsysteminformation.seleniumpages.SystemAdminWorkersPage;
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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SystemAdminFunctionalitiesTest {

    private static WebDriver browser;

    private static LoginPage loginPage;
    private static SystemAdminWorkersPage systemPage;
    private static SystemAdminMenuPage menuPage;

    @BeforeAll
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);

        browser = new ChromeDriver(options);
        browser.manage().window().maximize();
        browser.navigate().to("http://localhost:4200/login");

        loginPage = PageFactory.initElements(browser, LoginPage.class);
        systemPage = PageFactory.initElements(browser, SystemAdminWorkersPage.class);
        menuPage = PageFactory.initElements(browser, SystemAdminMenuPage.class);

    }

    @Test
    @Order(1)
    public void badCredentials() {
        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/login", 10));
        assertEquals(browser.getTitle(), "RestaurantInformationSystemFrontend");

        loginPage.login("liamneeson", "badpassword");

        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/login", 10));
    }

    @Test
    @Order(2)
    public void successfulLogin() {
        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/login", 10));

        loginPage.login("liamneeson", "liamneeson");

        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/home/system-admin/workers", 10));
    }

    @Test
    @Order(3)
    public void successfulEditing() throws InterruptedException {
        Assertions.assertEquals(9, systemPage.getTableRows().size());
        Assertions.assertFalse(systemPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Assertions.assertFalse(systemPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("John", systemPage.getFirstNameInput().getAttribute("value"));  // Check inputs values set to initially selected row
        Assertions.assertEquals("0611111111", systemPage.getPhoneNumberInput().getAttribute("value"));

        systemPage.clickSelectedRow(1); // Select second row
        Assertions.assertEquals("Simon", systemPage.getFirstNameInput().getAttribute("value")); // Check inputs values set to chosen row
        Assertions.assertEquals("0611111112", systemPage.getPhoneNumberInput().getAttribute("value"));

        systemPage.clickButton(0);  // Enable editing
        Assertions.assertTrue(systemPage.getFirstNameInput().isEnabled());  // Check inputs are enabled
        Assertions.assertTrue(systemPage.getPhoneNumberInput().isEnabled());
        systemPage.setFirstNameInput("Pera zdera"); // Enter new values
        systemPage.setPhoneNumber("0622222222");
        Assertions.assertEquals("Pera zdera", systemPage.getFirstNameInput().getAttribute("value"));    // Check if new values are set to inputs
        Assertions.assertEquals("0622222222", systemPage.getPhoneNumberInput().getAttribute("value"));

        systemPage.clickButton(1);  // Click on save button
        Thread.sleep(1000);
        Assertions.assertFalse(systemPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Assertions.assertFalse(systemPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("Pera zdera Baker", systemPage.getTableRows().get(1).findElement(By.xpath("td[2]")).getText()); // Check if new values are updated in the table
        Assertions.assertEquals("0622222222", systemPage.getTableRows().get(1).findElement(By.xpath("td[3]")).getText());

        systemPage.clickSelectedRow(1); // Return old values
        systemPage.clickButton(0);
        systemPage.setFirstNameInput("Simon");
        systemPage.setPhoneNumber("0611111112");
        systemPage.clickButton(1);
        Thread.sleep(1000);
        Assertions.assertEquals("Simon Baker", systemPage.getTableRows().get(1).findElement(By.xpath("td[2]")).getText());
        Assertions.assertEquals("0611111112", systemPage.getTableRows().get(1).findElement(By.xpath("td[3]")).getText());
    }

    @Test
    @Order(4)
    public void failEditing_BadPhoneNumber() {
        Assertions.assertEquals(9, systemPage.getTableRows().size());
        Assertions.assertFalse(systemPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Assertions.assertFalse(systemPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("John", systemPage.getFirstNameInput().getAttribute("value"));  // Check inputs values set to initially selected row
        Assertions.assertEquals("0611111111", systemPage.getPhoneNumberInput().getAttribute("value"));

        systemPage.getButtons().get(0).click();  // Enable editing
        Assertions.assertTrue(systemPage.getFirstNameInput().isEnabled());  // Check inputs are enabled
        Assertions.assertTrue(systemPage.getPhoneNumberInput().isEnabled());
        systemPage.setFirstNameInput("Pera zdera"); // Enter new values
        systemPage.setPhoneNumber("0622sklj");
        Assertions.assertEquals("Pera zdera", systemPage.getFirstNameInput().getAttribute("value"));    // Check if new values are set to inputs
        Assertions.assertEquals("0622sklj", systemPage.getPhoneNumberInput().getAttribute("value"));

        systemPage.clickButton(1);  // Click on save button
        Assertions.assertTrue(systemPage.getFirstNameInput().isEnabled()); // Check inputs are enabled, save should not be triggered if inputs are not valid
        Assertions.assertTrue(systemPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("John Cena", systemPage.getTableRows().get(0).findElement(By.xpath("td[2]")).getText()); // Check if new values are not saved
        Assertions.assertEquals("0611111111", systemPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText());

        systemPage.clickButton(0);  // Click on cancel button
        Assertions.assertFalse(systemPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Assertions.assertFalse(systemPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("John Cena", systemPage.getTableRows().get(0).findElement(By.xpath("td[2]")).getText());
        Assertions.assertEquals("0611111111", systemPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText());
    }

    @Test
    @Order(5)
    public void successfulAddingAndDeletingOfManager() throws InterruptedException {
        systemPage.ensureAddButtonIsDisplayed();
        Assertions.assertTrue(systemPage.getAddBtn().isDisplayed());

        systemPage.getAddBtn().click();

        systemPage.ensureAddButtonsAreDisplayed();
        systemPage.getAddButtons().get(0).click();

        systemPage.ensureDialogFieldsAreDisplayed();
        Thread.sleep(2000);
        systemPage.getDialogFields().get(0).findElement(By.xpath("div/div[1]/div/input")).sendKeys("Leon");
        systemPage.getDialogFields().get(1).findElement(By.xpath("div/div[1]/div/input")).sendKeys("List");
        systemPage.getDialogFields().get(2).findElement(By.xpath("div/div[1]/div/input")).sendKeys("leon@gmail.com");
        systemPage.getDialogFields().get(3).findElement(By.xpath("div/div[1]/div/input")).sendKeys("leon_stronger");
        systemPage.getDialogFields().get(4).findElement(By.xpath("div/div[1]/div/input")).sendKeys("Leon_stronger123");
        systemPage.getDialogFields().get(5).findElement(By.xpath("div/div[1]/div/input")).sendKeys("Leon_stronger123");
        systemPage.getDialogFields().get(6).findElement(By.xpath("div/div[1]/div/input")).sendKeys("40000");
        systemPage.getDialogFields().get(7).findElement(By.xpath("div/div[1]/div/input")).sendKeys("0645533454");

        systemPage.ensureDialogButtonsAreDisplayed();
        systemPage.getDialogButtons().get(1).click();

        systemPage.ensureRowsAreDisplayed(10);
        Assertions.assertEquals(10, systemPage.getTableRows().size());

        for (WebElement el : systemPage.getTableRows()) {
            if (el.findElement(By.xpath("td[2]")).getText().equals("Leon List")) {
                el.findElement(By.xpath("td[5]/button")).click();
                break;
            }
        }

        systemPage.ensureRowsAreDisplayed(9);
        Assertions.assertEquals(9, systemPage.getTableRows().size());

    }

    @Test
    @Order(6)
    public void successfulAddingAndDeletingOfEmployee() {
        systemPage.ensureAddButtonIsDisplayed();
        Assertions.assertTrue(systemPage.getAddBtn().isDisplayed());

        systemPage.getAddBtn().click();

        systemPage.ensureAddButtonsAreDisplayed();
        systemPage.getAddButtons().get(1).click();

        systemPage.ensureDialogFieldsAreDisplayed();
        systemPage.getDialogFields().get(0).findElement(By.xpath("div/div[1]/div/input")).sendKeys("Mia");
        systemPage.getDialogFields().get(1).findElement(By.xpath("div/div[1]/div/input")).sendKeys("List");
        systemPage.getDialogFields().get(2).findElement(By.xpath("div/div[1]/div/input")).sendKeys("mia@gmail.com");
        systemPage.getDialogFields().get(3).findElement(By.xpath("div/div[1]/div/input")).sendKeys("7777");
        systemPage.getDialogFields().get(5).findElement(By.xpath("div/div[1]/div/input")).sendKeys("35000");
        systemPage.getDialogFields().get(6).findElement(By.xpath("div/div[1]/div/input")).sendKeys("0646633454");

        systemPage.ensureDialogButtonsAreDisplayed();
        systemPage.getDialogButtons().get(1).click();

        systemPage.ensureRowsAreDisplayed(10);
        Assertions.assertEquals(10, systemPage.getTableRows().size());

        for (WebElement el : systemPage.getTableRows()) {
            if (el.findElement(By.xpath("td[2]")).getText().equals("Mia List")) {
                el.findElement(By.xpath("td[5]/button")).click();
                break;
            }
        }

        systemPage.ensureRowsAreDisplayed(9);
        Assertions.assertEquals(9, systemPage.getTableRows().size());

    }

    @Test
    @Order(7)
    public void deletionFails() {
        systemPage.ensureRowsAreDisplayed(9);
        Assertions.assertEquals(9, systemPage.getTableRows().size());

        systemPage.getTableRows().get(1).findElement(By.xpath("td[5]/button")).click();

        Assertions.assertEquals(9, systemPage.getTableRows().size());

    }

    @Test
    @Order(8)
    public void successfulChangePassword() {
        systemPage.ensureRowsAreDisplayed(9);
        Assertions.assertEquals(9, systemPage.getTableRows().size());

        systemPage.clickSelectedRow(8);

        systemPage.ensureChangePasswordButtonIsDisplayed();
        systemPage.getChangePasswordBtn().click();

        systemPage.ensurePasswordDialogInputsAreDisplayed();
        systemPage.getPasswordDialogInputs().get(0).findElement(By.xpath("div/div[1]/div/input")).sendKeys("liamneeson");
        systemPage.getPasswordDialogInputs().get(1).findElement(By.xpath("div/div[1]/div/input")).sendKeys("liam123");
        systemPage.getPasswordDialogInputs().get(2).findElement(By.xpath("div/div[1]/div/input")).sendKeys("liam123");

        systemPage.ensureDialogButtonsAreDisplayed();
        systemPage.getDialogButtons().get(1).click();

        systemPage.ensurePasswordDialogInputsAreNotDisplayed();

        systemPage.ensureChangePasswordButtonIsDisplayed();
        systemPage.getChangePasswordBtn().click();

        systemPage.ensurePasswordDialogInputsAreDisplayed();
        systemPage.getPasswordDialogInputs().get(0).findElement(By.xpath("div/div[1]/div/input")).sendKeys("liam123");
        systemPage.getPasswordDialogInputs().get(1).findElement(By.xpath("div/div[1]/div/input")).sendKeys("liamneeson");
        systemPage.getPasswordDialogInputs().get(2).findElement(By.xpath("div/div[1]/div/input")).sendKeys("liamneeson");

        systemPage.ensureDialogButtonsAreDisplayed();
        systemPage.getDialogButtons().get(1).click();

        systemPage.ensurePasswordDialogInputsAreNotDisplayed();

    }

    @Test
    @Order(9)
    public void deleteItemFromMenuAndDiscardChanges() {

        browser.navigate().to("http://localhost:4200/home/system-admin/menu");

        menuPage.ensureCardsAreDisplayed(5, 1); // 5 drink items
        menuPage.ensureCardsAreDisplayed(1, 2); // 1 dish item

        Assertions.assertEquals(5, menuPage.getDrinkCards().size());
        Assertions.assertEquals(1, menuPage.getDishesCards().size());

        menuPage.getDrinkCards().get(0).findElement(By.className("clear-button")).click();    // Click on delete button of card

        menuPage.ensureCardsAreDisplayed(4, 1); // 4 drink items
        Assertions.assertEquals(4, menuPage.getDrinkCards().size());

        menuPage.getDiscardBtn().click();

        menuPage.ensureCardsAreDisplayed(5, 1); // 5 drink items
        menuPage.ensureCardsAreDisplayed(2, 2); // 1 dish item

        Assertions.assertEquals(5, menuPage.getDrinkCards().size());
        Assertions.assertEquals(2, menuPage.getDishesCards().size());

    }

    @Test
    @Order(10)
    public void deleteItemFromMenuAndSaveChanges() {

        menuPage.ensureCardsAreDisplayed(2, 2); // 2 dish items

        Assertions.assertEquals(2, menuPage.getDishesCards().size());

        menuPage.getDishesCards().get(0).findElement(By.xpath("button[2]")).click();    // Click on delete button of card
        menuPage.ensureCardsAreDisplayed(1, 2); // 4 drink items

        Assertions.assertEquals(1, menuPage.getDishesCards().size());

        menuPage.getSaveBtn().click();
        menuPage.ensureCardsAreDisplayed(1, 2); // 1 dish item

        Assertions.assertEquals(1, menuPage.getDishesCards().size());

    }

    @Test
    @Order(11)
    public void addDrinkItem() throws InterruptedException {

        menuPage.ensureCardsAreDisplayed(5, 1); // 5 drink items

        Assertions.assertEquals(5, menuPage.getDrinkCards().size());

        menuPage.ensureAddButtonIsDisplayed();
        menuPage.getAddBtn().click();

        menuPage.ensureItemNameInputIsDisplayed();
        menuPage.setItemName("Cokoladni napitak");
        menuPage.ensureItemCategorySelectIsDisplayed();
        menuPage.setItemCategory("Juices");
        menuPage.ensureTextIsPresentInCategorySelect();
        Thread.sleep(2000);
        menuPage.ensureItemPriceInputIsDisplayed();
        menuPage.setPrice("500");

        menuPage.ensureDialogButtonsAreDisplayed();
        Assertions.assertEquals(2, menuPage.getDialogButtons().size());

        menuPage.ensureSaveButtonIsClickable();
        menuPage.getDialogButtons().get(1).click();

        menuPage.ensureDialogButtonsAreNotDisplayed();
        menuPage.ensureCardsAreDisplayed(6, 1); // 6 drink items
        Assertions.assertEquals(6, menuPage.getDrinkCards().size());

        menuPage.getDiscardBtn().click();

    }

    @Test
    @Order(12)
    public void editDrinkItem() {

        menuPage.ensureCardsAreDisplayed(5, 1); // 5 drink items

        Assertions.assertEquals(5, menuPage.getDrinkCards().size());

        Assertions.assertEquals("Apple juice", menuPage.getDrinkCards().get(0).findElement(By.xpath("mat-card-header/div[1]/mat-card-title")).getText());
        menuPage.getDrinkCards().get(0).findElement(By.xpath("button[1]")).click();    // Click on edit button of card

        menuPage.ensureItemNameInputIsDisplayed();
        menuPage.setItemName("Sok od jabuke");
        menuPage.ensureItemPriceInputIsDisplayed();
        menuPage.setPrice("500");

        menuPage.ensureDialogButtonsAreDisplayed();
        Assertions.assertEquals(2, menuPage.getDialogButtons().size());

        menuPage.ensureSaveButtonIsClickable();
        menuPage.getDialogButtons().get(1).click();

        menuPage.ensureDialogButtonsAreNotDisplayed();
        menuPage.ensureCardsAreDisplayed(5, 1); // 5 drink items
        Assertions.assertEquals(5, menuPage.getDrinkCards().size());
        Assertions.assertEquals("Sok od jabuke", menuPage.getDrinkCards().get(0).findElement(By.xpath("mat-card-header/div[1]/mat-card-title")).getText());

        menuPage.getDiscardBtn().click();

    }

    @AfterAll
    public static void tearDown() {
        browser.quit();
    }
}
