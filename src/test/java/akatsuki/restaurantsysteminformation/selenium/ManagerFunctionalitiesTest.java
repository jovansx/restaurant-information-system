package akatsuki.restaurantsysteminformation.selenium;

import akatsuki.restaurantsysteminformation.seleniumpages.LoginPage;
import akatsuki.restaurantsysteminformation.seleniumpages.ManagerEmployeesPage;
import akatsuki.restaurantsysteminformation.seleniumpages.SystemAdminWorkersPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ManagerFunctionalitiesTest {

    private static WebDriver browser;

    private static LoginPage loginPage;
    private static ManagerEmployeesPage managerPage;

//    private final String MAT_FORM_FIELD_TO_INPUT = "div/div[1]/div/input";

    @BeforeAll
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");

        browser = new ChromeDriver();
        browser.manage().window().maximize();
        browser.navigate().to("http://localhost:4200/login");

        loginPage = PageFactory.initElements(browser, LoginPage.class);
        managerPage = PageFactory.initElements(browser, ManagerEmployeesPage.class);

    }

    @Test
    @Order(1)
    public void badCredentials() {
        loginPage.ensureIsDisplayedLoginBtn();
        Assertions.assertEquals(browser.getTitle(), "RestaurantInformationSystemFrontend");

        loginPage.setUsernameInput("bradpitt");
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

        loginPage.setUsernameInput("bradpitt");
        loginPage.setPasswordInput("bradpitt");
        loginPage.clickLoginButton();

        loginPage.ensureIsNotDisplayedLoginBtn(); // Ensure there's no login btn -> it means url has been changed

        Assertions.assertEquals("http://localhost:4200/home/manager/employees", browser.getCurrentUrl());
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
        Assertions.assertTrue(managerPage.getFirstNameInput().isEnabled());  // Check inputs are enabled
        Assertions.assertTrue(managerPage.getPhoneNumberInput().isEnabled());
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
}
