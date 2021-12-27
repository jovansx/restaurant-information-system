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

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminFunctionalitiesTest {

    private static WebDriver browser;

    private static LoginPage loginPage;
    private static AdminAdministratorsPage adminSystemAdministratorsPage;
    private static AdminRestaurantViewPage adminRestaurantViewPage;

    @BeforeAll
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);

        browser = new ChromeDriver(options);
        browser.manage().window().maximize();
        browser.navigate().to("http://localhost:4200/login");

        loginPage = PageFactory.initElements(browser, LoginPage.class);
        adminSystemAdministratorsPage = PageFactory.initElements(browser, AdminAdministratorsPage.class);
        adminRestaurantViewPage = PageFactory.initElements(browser, AdminRestaurantViewPage.class);
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

    @AfterAll
    public static void tearDown() {
        browser.quit();
    }
}
