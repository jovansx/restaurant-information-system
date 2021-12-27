package akatsuki.restaurantsysteminformation.selenium;

import akatsuki.restaurantsysteminformation.seleniumpages.LoginPage;
import akatsuki.restaurantsysteminformation.seleniumpages.ManagerEmployeesPage;
import akatsuki.restaurantsysteminformation.seleniumpages.Utilities;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ManagerFunctionalitiesTest {

    private WebDriver browser;

    private LoginPage loginPage;
    private ManagerEmployeesPage managerPage;

    @BeforeAll
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");
//        ChromeOptions options = new ChromeOptions();
//        options.setHeadless(true);

        browser = new ChromeDriver();
        browser.manage().window().maximize();
        browser.navigate().to("http://localhost:4200/login");

        loginPage = PageFactory.initElements(browser, LoginPage.class);
        managerPage = PageFactory.initElements(browser, ManagerEmployeesPage.class);

    }

    @Test
    @Order(1)
    public void badCredentials() {
        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/login", 10));
        assertEquals(browser.getTitle(), "RestaurantInformationSystemFrontend");

        loginPage.login("bradpitt", "badpassword");

        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/login", 10));
    }

    @Test
    @Order(2)
    public void successfulLogin() {
        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/login", 10));

        loginPage.login("bradpitt", "bradpitt");

        assertTrue(Utilities.urlWait(browser, "http://localhost:4200/home/manager/employees", 10));
    }

    @Test
    public void successfulEditing() {
        assertEquals(8, managerPage.getTableRows(8).size());
        Utilities.elementDisabledWait(browser, managerPage.getFirstNameInput(), 10);
        assertFalse(managerPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Utilities.elementDisabledWait(browser, managerPage.getPhoneNumberInput(), 10);
        assertFalse(managerPage.getPhoneNumberInput().isEnabled());
        assertEquals("John", managerPage.getFirstNameInput().getAttribute("value"));  // Check inputs values set to initially selected row
        assertEquals("0611111111", managerPage.getPhoneNumberInput().getAttribute("value"));

        managerPage.clickSelectedRow(5); // Select second row
        assertEquals("Eloner", managerPage.getFirstNameInput().getAttribute("value")); // Check inputs values set to chosen row
        assertEquals("0611111119", managerPage.getPhoneNumberInput().getAttribute("value"));

        managerPage.clickButton(0);  // Enable editing
        Utilities.elementEnabledWait(browser, managerPage.getFirstNameInput(), 10);
        assertTrue(managerPage.getFirstNameInput().isEnabled());  // Check inputs are enabled
        Utilities.elementEnabledWait(browser, managerPage.getFirstNameInput(), 10);
        assertTrue(managerPage.getPhoneNumberInput().isEnabled());
        managerPage.setFirstNameInput("Ela"); // Enter new values
        managerPage.setPhoneNumber("0622222222");
        assertEquals("Ela", managerPage.getFirstNameInput().getAttribute("value"));    // Check if new values are set to inputs
        assertEquals("0622222222", managerPage.getPhoneNumberInput().getAttribute("value"));

        managerPage.clickButton(1);  // Click on save button
        Utilities.elementDisabledWait(browser, managerPage.getFirstNameInput(), 10);
        assertFalse(managerPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Utilities.elementDisabledWait(browser, managerPage.getPhoneNumberInput(), 10);
        assertFalse(managerPage.getPhoneNumberInput().isEnabled());
        assertEquals("Ela Muskila", managerPage.getTableRows().get(5).findElement(By.xpath("td[2]")).getText()); // Check if new values are updated in the table
        assertEquals("0622222222", managerPage.getTableRows().get(5).findElement(By.xpath("td[3]")).getText());

        managerPage.clickSelectedRow(5); // Return old values
        managerPage.clickButton(0);
        managerPage.setFirstNameInput("Eloner");
        managerPage.setPhoneNumber("0611111119");
        managerPage.clickButton(1);
        Utilities.numberOfElementsWait(browser, managerPage.getButtons(), 1, 10);
        assertEquals("Eloner Muskila", managerPage.getTableRows().get(5).findElement(By.xpath("td[2]")).getText());
        assertEquals("0611111119", managerPage.getTableRows().get(5).findElement(By.xpath("td[3]")).getText());
    }

    @Test
    public void failEditing_BadFirstName() {
        assertEquals(8, managerPage.getTableRows(8).size());
        Utilities.elementDisabledWait(browser, managerPage.getFirstNameInput(), 10);
        assertFalse(managerPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Utilities.elementDisabledWait(browser, managerPage.getPhoneNumberInput(), 10);
        assertFalse(managerPage.getPhoneNumberInput().isEnabled());
        assertEquals("John", managerPage.getFirstNameInput().getAttribute("value"));  // Check inputs values set to initially selected row
        assertEquals("0611111111", managerPage.getPhoneNumberInput().getAttribute("value"));

        managerPage.clickButton(0);  // Enable editing
        Utilities.elementEnabledWait(browser, managerPage.getFirstNameInput(), 10);
        assertTrue(managerPage.getFirstNameInput().isEnabled());  // Check inputs are enabled
        Utilities.elementEnabledWait(browser, managerPage.getPhoneNumberInput(), 10);
        assertTrue(managerPage.getPhoneNumberInput().isEnabled());
        managerPage.setFirstNameInput("dasdjakjsdkasdkasdklajskdaksdklasdklaskldlfsdgsdfsdfsd"); // Enter new value for first name
        assertEquals("dasdjakjsdkasdkasdklajskdaksdklasdklaskldlfsdgsdfsdfsd", managerPage.getFirstNameInput().getAttribute("value"));    // Check if new values are set to inputs

        managerPage.clickButton(1);  // Click on save button
        Utilities.elementEnabledWait(browser, managerPage.getFirstNameInput(), 10);
        assertTrue(managerPage.getFirstNameInput().isEnabled()); // Check inputs are enabled, save should not be triggered if inputs are not valid
        Utilities.elementEnabledWait(browser, managerPage.getPhoneNumberInput(), 10);
        assertTrue(managerPage.getPhoneNumberInput().isEnabled());
        assertEquals("John Cena", managerPage.getTableRows().get(0).findElement(By.xpath("td[2]")).getText()); // Check if new value is not saved

        managerPage.clickButton(0);  // Click on cancel button
        Utilities.elementDisabledWait(browser, managerPage.getFirstNameInput(), 10);
        assertFalse(managerPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Utilities.elementDisabledWait(browser, managerPage.getPhoneNumberInput(), 10);
        assertFalse(managerPage.getPhoneNumberInput().isEnabled());
        assertEquals("John Cena", managerPage.getTableRows().get(0).findElement(By.xpath("td[2]")).getText());
        assertEquals("0611111111", managerPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText());
    }

    @Test
    public void successfulAddingAndDeletingOfEmployee() {
        managerPage.clickAddButton();
        managerPage.setFields("Luka", "List", "luka@gmail.com", "8888", "35000", "0648883454");
        managerPage.clickSaveButton();

        assertEquals(9, managerPage.getTableRows(9).size());

        for (WebElement el : managerPage.getTableRows()) {
            if (el.findElement(By.xpath("td[2]")).getText().equals("Luka List")) {
                el.findElement(By.xpath("td[5]/button")).click();
                break;
            }
        }

        assertEquals(8, managerPage.getTableRows(8).size());

    }

    @Test
    public void deletionFails() {
        assertEquals(8, managerPage.getTableRows(8).size());

        managerPage.getTableRows().get(1).findElement(By.xpath("td[5]/button")).click();

        assertEquals(8, managerPage.getTableRows().size());

    }

    @AfterAll
    public void tearDown() {
        browser.quit();
    }
}
