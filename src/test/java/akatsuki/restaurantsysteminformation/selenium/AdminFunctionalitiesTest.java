package akatsuki.restaurantsysteminformation.selenium;

import akatsuki.restaurantsysteminformation.seleniumpages.AdminAdministratorsPage;
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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminFunctionalitiesTest {

    private static WebDriver browser;

    private static LoginPage loginPage;
    private static AdminAdministratorsPage adminPage;

    @BeforeAll
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);

        browser = new ChromeDriver(options);
        browser.manage().window().maximize();
        browser.navigate().to("http://localhost:4200/login");

        loginPage = PageFactory.initElements(browser, LoginPage.class);
        adminPage = PageFactory.initElements(browser, AdminAdministratorsPage.class);

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
    }

    @Test
    public void successfulEditing() throws InterruptedException {
        Assertions.assertEquals(1, adminPage.getTableRows().size());
        Assertions.assertFalse(adminPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Assertions.assertFalse(adminPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("Liam", adminPage.getFirstNameInput().getAttribute("value"));  // Check inputs values set to initially selected row
        Assertions.assertEquals("0611111116", adminPage.getPhoneNumberInput().getAttribute("value"));

        adminPage.clickButton(0);  // Enable editing
        Assertions.assertTrue(adminPage.getFirstNameInput().isEnabled());  // Check inputs are enabled
        Assertions.assertTrue(adminPage.getPhoneNumberInput().isEnabled());
        adminPage.setFirstNameInput("Liamor"); // Enter new values
        adminPage.setPhoneNumber("0645558888");
        Assertions.assertEquals("Liamor", adminPage.getFirstNameInput().getAttribute("value"));    // Check if new values are set to inputs
        Assertions.assertEquals("0645558888", adminPage.getPhoneNumberInput().getAttribute("value"));

        adminPage.clickButton(1);  // Click on save button
        Thread.sleep(1000);
        Assertions.assertFalse(adminPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Assertions.assertFalse(adminPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("Liamor Neeson", adminPage.getTableRows().get(0).findElement(By.xpath("td[2]")).getText()); // Check if new values are updated in the table
        Assertions.assertEquals("0645558888", adminPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText());

        adminPage.clickButton(0);
        adminPage.setFirstNameInput("Liam");
        adminPage.setPhoneNumber("0611111116");
        adminPage.clickButton(1);
        Thread.sleep(1000);
        Assertions.assertEquals("Liam Neeson", adminPage.getTableRows().get(0).findElement(By.xpath("td[2]")).getText());
        Assertions.assertEquals("0611111116", adminPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText());
    }

    @Test
    public void failEditing_BadFirstName() {
        Assertions.assertEquals(1, adminPage.getTableRows().size());
        Assertions.assertFalse(adminPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Assertions.assertFalse(adminPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("Liam", adminPage.getFirstNameInput().getAttribute("value"));  // Check inputs values set to initially selected row
        Assertions.assertEquals("0611111116", adminPage.getPhoneNumberInput().getAttribute("value"));

        adminPage.getButtons().get(0).click();  // Enable editing
        Assertions.assertTrue(adminPage.getFirstNameInput().isEnabled());  // Check inputs are enabled
        Assertions.assertTrue(adminPage.getPhoneNumberInput().isEnabled());
        adminPage.setPhoneNumber("06933skl"); // Enter new value for phone number
        Assertions.assertEquals("06933skl", adminPage.getPhoneNumberInput().getAttribute("value"));    // Check if new value is set to input

        adminPage.clickButton(1);  // Click on save button
        Assertions.assertTrue(adminPage.getFirstNameInput().isEnabled()); // Check inputs are enabled, save should not be triggered if inputs are not valid
        Assertions.assertTrue(adminPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("0611111116", adminPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText()); // Check if new value is not saved

        adminPage.clickButton(0);  // Click on cancel button
        Assertions.assertFalse(adminPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Assertions.assertFalse(adminPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("Liam Neeson", adminPage.getTableRows().get(0).findElement(By.xpath("td[2]")).getText());
        Assertions.assertEquals("0611111116", adminPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText());
    }

    @Test
    public void successfulAddingAndDeletingOfSystemAdmin() {
        adminPage.ensureAddButtonIsDisplayed();
        Assertions.assertTrue(adminPage.getAddBtn().isDisplayed());

        adminPage.getAddBtn().click();

        adminPage.ensureDialogFieldsAreDisplayed();
        adminPage.getDialogFields().get(0).findElement(By.xpath("div/div[1]/div/input")).sendKeys("Lucian");
        adminPage.getDialogFields().get(1).findElement(By.xpath("div/div[1]/div/input")).sendKeys("List");
        adminPage.getDialogFields().get(2).findElement(By.xpath("div/div[1]/div/input")).sendKeys("lucian@gmail.com");
        adminPage.getDialogFields().get(3).findElement(By.xpath("div/div[1]/div/input")).sendKeys("lucian_stronger");
        adminPage.getDialogFields().get(4).findElement(By.xpath("div/div[1]/div/input")).sendKeys("lucian_stronger123");
        adminPage.getDialogFields().get(5).findElement(By.xpath("div/div[1]/div/input")).sendKeys("lucian_stronger123");
        adminPage.getDialogFields().get(6).findElement(By.xpath("div/div[1]/div/input")).sendKeys("40000");
        adminPage.getDialogFields().get(7).findElement(By.xpath("div/div[1]/div/input")).sendKeys("0645666666");

        adminPage.ensureDialogButtonsAreDisplayed();
        adminPage.getDialogButtons().get(1).click();

        adminPage.ensureRowsAreDisplayed(2);
        Assertions.assertEquals(2, adminPage.getTableRows().size());

        for (WebElement el : adminPage.getTableRows()) {
            if (el.findElement(By.xpath("td[2]")).getText().equals("Lucian List")) {
                el.findElement(By.xpath("td[5]/button")).click();
                break;
            }
        }

        adminPage.ensureRowsAreDisplayed(1);
        Assertions.assertEquals(1, adminPage.getTableRows().size());

    }

    @AfterAll
    public static void tearDown() {
        browser.quit();
    }
}
