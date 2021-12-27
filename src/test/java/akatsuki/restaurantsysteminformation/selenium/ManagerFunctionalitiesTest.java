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
    public void successfulEditing() throws InterruptedException {
        Assertions.assertEquals(8, managerPage.getTableRows().size());
        Assertions.assertFalse(managerPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Assertions.assertFalse(managerPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("John", managerPage.getFirstNameInput().getAttribute("value"));  // Check inputs values set to initially selected row
        Assertions.assertEquals("0611111111", managerPage.getPhoneNumberInput().getAttribute("value"));

        managerPage.clickSelectedRow(5); // Select second row
        Assertions.assertEquals("Eloner", managerPage.getFirstNameInput().getAttribute("value")); // Check inputs values set to chosen row
        Assertions.assertEquals("0611111119", managerPage.getPhoneNumberInput().getAttribute("value"));

        managerPage.clickButton(0);  // Enable editing
        assertTrue(managerPage.getFirstNameInput().isEnabled());  // Check inputs are enabled
        assertTrue(managerPage.getPhoneNumberInput().isEnabled());
        managerPage.setFirstNameInput("Ela"); // Enter new values
        managerPage.setPhoneNumber("0622222222");
        Assertions.assertEquals("Ela", managerPage.getFirstNameInput().getAttribute("value"));    // Check if new values are set to inputs
        Assertions.assertEquals("0622222222", managerPage.getPhoneNumberInput().getAttribute("value"));

        managerPage.clickButton(1);  // Click on save button
        Thread.sleep(1000);
        Assertions.assertFalse(managerPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Assertions.assertFalse(managerPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("Ela Muskila", managerPage.getTableRows().get(5).findElement(By.xpath("td[2]")).getText()); // Check if new values are updated in the table
        Assertions.assertEquals("0622222222", managerPage.getTableRows().get(5).findElement(By.xpath("td[3]")).getText());

        managerPage.clickSelectedRow(5); // Return old values
        managerPage.clickButton(0);
        managerPage.setFirstNameInput("Eloner");
        managerPage.setPhoneNumber("0611111119");
        managerPage.clickButton(1);
        Thread.sleep(1000);
        Assertions.assertEquals("Eloner Muskila", managerPage.getTableRows().get(5).findElement(By.xpath("td[2]")).getText());
        Assertions.assertEquals("0611111119", managerPage.getTableRows().get(5).findElement(By.xpath("td[3]")).getText());
    }

    @Test
    public void failEditing_BadFirstName() {
        Assertions.assertEquals(8, managerPage.getTableRows().size());
        Assertions.assertFalse(managerPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Assertions.assertFalse(managerPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("John", managerPage.getFirstNameInput().getAttribute("value"));  // Check inputs values set to initially selected row
        Assertions.assertEquals("0611111111", managerPage.getPhoneNumberInput().getAttribute("value"));

        managerPage.getButtons().get(0).click();  // Enable editing
        assertTrue(managerPage.getFirstNameInput().isEnabled());  // Check inputs are enabled
        assertTrue(managerPage.getPhoneNumberInput().isEnabled());
        managerPage.setFirstNameInput("dasdjakjsdkasdkasdklajskdaksdklasdklaskldlfsdgsdfsdfsd"); // Enter new value for first name
        Assertions.assertEquals("dasdjakjsdkasdkasdklajskdaksdklasdklaskldlfsdgsdfsdfsd", managerPage.getFirstNameInput().getAttribute("value"));    // Check if new values are set to inputs

        managerPage.clickButton(1);  // Click on save button
        assertTrue(managerPage.getFirstNameInput().isEnabled()); // Check inputs are enabled, save should not be triggered if inputs are not valid
        assertTrue(managerPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("John Cena", managerPage.getTableRows().get(0).findElement(By.xpath("td[2]")).getText()); // Check if new value is not saved

        managerPage.clickButton(0);  // Click on cancel button
        Assertions.assertFalse(managerPage.getFirstNameInput().isEnabled()); // Check inputs are disabled
        Assertions.assertFalse(managerPage.getPhoneNumberInput().isEnabled());
        Assertions.assertEquals("John Cena", managerPage.getTableRows().get(0).findElement(By.xpath("td[2]")).getText());
        Assertions.assertEquals("0611111111", managerPage.getTableRows().get(0).findElement(By.xpath("td[3]")).getText());
    }

    @Test
    public void successfulAddingAndDeletingOfEmployee() {
        managerPage.ensureAddButtonIsDisplayed();
        assertTrue(managerPage.getAddBtn().isDisplayed());

        managerPage.getAddBtn().click();

        managerPage.ensureDialogFieldsAreDisplayed();
        managerPage.getDialogFields().get(0).findElement(By.xpath("div/div[1]/div/input")).sendKeys("Luka");
        managerPage.getDialogFields().get(1).findElement(By.xpath("div/div[1]/div/input")).sendKeys("List");
        managerPage.getDialogFields().get(2).findElement(By.xpath("div/div[1]/div/input")).sendKeys("luka@gmail.com");
        managerPage.getDialogFields().get(3).findElement(By.xpath("div/div[1]/div/input")).sendKeys("8888");
        managerPage.getDialogFields().get(5).findElement(By.xpath("div/div[1]/div/input")).sendKeys("35000");
        managerPage.getDialogFields().get(6).findElement(By.xpath("div/div[1]/div/input")).sendKeys("0648883454");

        managerPage.ensureDialogButtonsAreDisplayed();
        managerPage.getDialogButtons().get(1).click();

        managerPage.ensureRowsAreDisplayed(9);
        Assertions.assertEquals(9, managerPage.getTableRows().size());

        for (WebElement el : managerPage.getTableRows()) {
            if (el.findElement(By.xpath("td[2]")).getText().equals("Luka List")) {
                el.findElement(By.xpath("td[5]/button")).click();
                break;
            }
        }

        managerPage.ensureRowsAreDisplayed(8);
        Assertions.assertEquals(8, managerPage.getTableRows().size());

    }

    @Test
    public void deletionFails() {
        managerPage.ensureRowsAreDisplayed(8);
        Assertions.assertEquals(8, managerPage.getTableRows().size());

        managerPage.getTableRows().get(1).findElement(By.xpath("td[5]/button")).click();

        Assertions.assertEquals(8, managerPage.getTableRows().size());

    }

    @AfterAll
    public void tearDown() {
        browser.quit();
    }
}
