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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SystemAdminFunctionalitiesTest {

    private WebDriver browser;

    private LoginPage loginPage;
    private SystemAdminWorkersPage systemPage;
    private SystemAdminMenuPage menuPage;

    @BeforeAll
    public void setup() {
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
    public void successfulEditing() {
        assertEquals(9, systemPage.getTableRows(9).size());
        assertTrue(Utilities.elementDisabledWait(browser, systemPage.getFirstNameInput(), 10)); // Check inputs are disabled
        assertTrue(Utilities.elementDisabledWait(browser, systemPage.getPhoneNumberInput(), 10));
        assertEquals("John", systemPage.getFirstNameInput().getAttribute("value"));  // Check inputs values set to initially selected row
        assertEquals("0611111111", systemPage.getPhoneNumberInput().getAttribute("value"));

        systemPage.clickSelectedRow(1); // Select second row
        assertEquals("Simon", systemPage.getFirstNameInput().getAttribute("value")); // Check inputs values set to chosen row
        assertEquals("0611111112", systemPage.getPhoneNumberInput().getAttribute("value"));

        systemPage.clickButton(0);  // Enable editing
        assertTrue(Utilities.elementEnabledWait(browser, systemPage.getFirstNameInput(), 10));  // Check inputs are enabled
        assertTrue(Utilities.elementEnabledWait(browser, systemPage.getPhoneNumberInput(), 10));
        systemPage.setFirstNameInput("Pera zdera"); // Enter new values
        systemPage.setPhoneNumber("0622222222");
        assertEquals("Pera zdera", systemPage.getFirstNameInput().getAttribute("value"));    // Check if new values are set to inputs
        assertEquals("0622222222", systemPage.getPhoneNumberInput().getAttribute("value"));

        systemPage.clickButton(1);  // Click on save button
        assertTrue(Utilities.elementDisabledWait(browser, systemPage.getFirstNameInput(), 10)); // Check inputs are disabled
        assertTrue(Utilities.elementDisabledWait(browser, systemPage.getPhoneNumberInput(), 10));
        assertEquals("Pera zdera Baker", systemPage.getTableRows().get(1).findElement(By.xpath("td[2]")).getText()); // Check if new values are updated in the table
        assertEquals("0622222222", systemPage.getTableRows().get(1).findElement(By.xpath("td[3]")).getText());

        systemPage.clickSelectedRow(1); // Return old values
        systemPage.clickButton(0);
        systemPage.setFirstNameInput("Simon");
        systemPage.setPhoneNumber("0611111112");
        systemPage.clickButton(1);
        Utilities.numberOfElementsWait(browser, systemPage.getButtons(), 1, 10);
        assertEquals("Simon Baker", systemPage.getTableRows().get(1).findElement(By.xpath("td[2]")).getText());
        assertEquals("0611111112", systemPage.getTableRows().get(1).findElement(By.xpath("td[3]")).getText());
    }

    @Test
    @Order(4)
    public void failEditing_BadPhoneNumber() {
        assertEquals(9, systemPage.getTableRows(9).size());
        assertTrue(Utilities.elementDisabledWait(browser, systemPage.getFirstNameInput(), 10)); // Check inputs are disabled
        assertTrue(Utilities.elementDisabledWait(browser, systemPage.getPhoneNumberInput(), 10));
        assertEquals("John", systemPage.getFirstNameInput().getAttribute("value"));  // Check inputs values set to initially selected row
        assertEquals("0611111111", systemPage.getPhoneNumberInput().getAttribute("value"));

        systemPage.clickButton(0);  // Enable editing
        assertTrue(Utilities.elementEnabledWait(browser, systemPage.getFirstNameInput(), 10));  // Check inputs are enabled
        assertTrue(Utilities.elementEnabledWait(browser, systemPage.getPhoneNumberInput(), 10));
        systemPage.setFirstNameInput("Pera zdera"); // Enter new values
        systemPage.setPhoneNumber("0622sklj");
        assertEquals("Pera zdera", systemPage.getFirstNameInput().getAttribute("value"));    // Check if new values are set to inputs
        assertEquals("0622sklj", systemPage.getPhoneNumberInput().getAttribute("value"));

        systemPage.clickButton(1);  // Click on save button
        assertTrue(Utilities.elementEnabledWait(browser, systemPage.getFirstNameInput(), 10));  // Check inputs are enabled
        assertTrue(Utilities.elementEnabledWait(browser, systemPage.getPhoneNumberInput(), 10));
        assertEquals("John Cena", systemPage.getTableRows().get(0).findElement(By.xpath("td[2]")).getText()); // Check if new values are not saved
        assertEquals("0611111111", systemPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText());

        systemPage.clickButton(0);  // Click on cancel button
        assertTrue(Utilities.elementDisabledWait(browser, systemPage.getFirstNameInput(), 10)); // Check inputs are disabled
        assertTrue(Utilities.elementDisabledWait(browser, systemPage.getPhoneNumberInput(), 10));
        assertEquals("John Cena", systemPage.getTableRows().get(0).findElement(By.xpath("td[2]")).getText());
        assertEquals("0611111111", systemPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText());
    }

    @Test
    @Order(5)
    public void successfulAddingAndDeletingOfManager() {
        systemPage.clickAddButton();
        systemPage.clickAddUser(0);     // 0 -> Manager
        systemPage.setManagerFields("Leon", "List", "leon@gmail.com", "leon_stronger",
                "leon_stronger123", "leon_stronger123", "40000", "0645533454");
        systemPage.clickSaveButton();

        assertEquals(10, systemPage.getTableRows(10).size());

        for (WebElement el : systemPage.getTableRows()) {
            WebElement td = Utilities.visibilityWait(browser, el.findElement(By.xpath("td[2]")), 10);
            if (td.getText().equals("Leon List")) {
                Utilities.clickableWait(browser, el.findElement(By.xpath("td[5]/button")), 10).click();
                break;
            }
        }

        assertEquals(9, systemPage.getTableRows(9).size());
    }

    @Test
    @Order(6)
    public void successfulAddingAndDeletingOfEmployee() {
        systemPage.clickAddButton();
        systemPage.clickAddUser(1);     // 1 -> Employee
        systemPage.setEmployeeFields("Mia", "List", "mia@gmail.com", "7777", "35000", "0646633454");
        systemPage.clickSaveButton();

        assertEquals(10, systemPage.getTableRows(10).size());

        for (WebElement el : systemPage.getTableRows()) {
            WebElement td = Utilities.visibilityWait(browser, el.findElement(By.xpath("td[2]")), 10);
            if (td.getText().equals("Mia List")) {
                Utilities.clickableWait(browser, el.findElement(By.xpath("td[5]/button")), 10).click();
                break;
            }
        }

        assertEquals(9, systemPage.getTableRows(9).size());
    }

    @Test
    @Order(7)
    public void deletionFails() {
        assertEquals(9, systemPage.getTableRows(9).size());

        systemPage.deleteRow(1);

        assertEquals(9, systemPage.getTableRows(9).size());

    }

    @Test
    @Order(8)
    public void successfulChangePassword() {
        assertEquals(9, systemPage.getTableRows(9).size());

        systemPage.clickSelectedRow(8);
        systemPage.clickPasswordButton();
        systemPage.setPasswordFields("bradpitt", "brad123", "brad123");
        systemPage.clickSaveButton();
        systemPage.clickPasswordButton();
        systemPage.setPasswordFields("brad123", "bradpitt", "bradpitt");
        systemPage.clickSaveButton();
    }

    @Test
    @Order(9)
    public void deleteItemFromMenuAndDiscardChanges() {
        browser.navigate().to("http://localhost:4200/home/system-admin/menu");
        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/home/system-admin/menu", 10));

        assertEquals(5, menuPage.getDrinkItemsCards(5).size());
        assertEquals(1, menuPage.getDishItemsCards(1).size());

        menuPage.deleteDrink(0);

        assertEquals(4, menuPage.getDrinkItemsCards(4).size());

        menuPage.clickDiscardButton();

        assertEquals(5, menuPage.getDrinkItemsCards(5).size());
        assertEquals(2, menuPage.getDishItemsCards(2).size());
    }

    @Test
    @Order(10)
    public void deleteItemFromMenuAndSaveChanges() {
        assertEquals(2, menuPage.getDishItemsCards(2).size());

        menuPage.deleteDish(0);
        assertEquals(1, menuPage.getDishItemsCards(1).size());

        menuPage.clickSaveButton();
        assertEquals(1, menuPage.getDishItemsCards(1).size());
    }

    @Test
    @Order(11)
    public void addDrinkItem() {
        assertEquals(5, menuPage.getDrinkItemsCards(5).size());

        menuPage.clickAddDrinkButton();

        menuPage.setItemName("Cokoladni napitak");
        menuPage.setDrinkItemCategory();
        menuPage.setPrice("500");

        menuPage.clickSaveDialogButton();
        assertEquals(6, menuPage.getDrinkItemsCards(6).size());

        menuPage.clickDiscardButton();
        assertEquals(5, menuPage.getDrinkItemsCards(5).size());
    }

    @Test
    @Order(12)
    public void editDrinkItem() {
        assertEquals(5, menuPage.getDrinkItemsCards(5).size());

        assertEquals("Apple juice", menuPage.getDrinkCards().get(0).findElement(By.xpath("mat-card-header/div[1]/mat-card-title")).getText());
        menuPage.editDrink(0);    // Click on edit button of card

        menuPage.setItemName("Sok od jabuke");
        menuPage.setPrice("500");

        menuPage.clickSaveDialogButton();

        assertEquals(5, menuPage.getDrinkItemsCards(5).size());
//        Utilities.ensureTextIsPresentInElement(browser, menuPage.getDrinkCards().get(0).findElement(By.xpath("mat-card-header/div[1]/mat-card-title")), "Sok od jabuke", 10);
//        assertEquals("Sok od jabuke", menuPage.getDrinkCards().get(0).findElement(By.xpath("mat-card-header/div[1]/mat-card-title")).getText());

        menuPage.clickDiscardButton();
    }

    @AfterAll
    public void tearDown() {
        browser.quit();
    }
}
