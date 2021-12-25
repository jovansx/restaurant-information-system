package akatsuki.restaurantsysteminformation.selenium;

import akatsuki.restaurantsysteminformation.seleniumpages.LoginPage;
import akatsuki.restaurantsysteminformation.seleniumpages.SystemAdminWorkersPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest {

    private static WebDriver browser;
    private static LoginPage loginPage;
    private static SystemAdminWorkersPage systemPage;

    @BeforeAll
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");

        browser = new ChromeDriver();
        browser.manage().window().maximize();
        browser.navigate().to("http://localhost:4200/login");

        loginPage = PageFactory.initElements(browser, LoginPage.class);
        systemPage = PageFactory.initElements(browser, SystemAdminWorkersPage.class);

    }

    @Test
    @Order(1)
    public void badCredentials() {
        loginPage.ensureIsDisplayedLoginBtn();
        Assertions.assertEquals(browser.getTitle(), "RestaurantInformationSystemFrontend");

        loginPage.setUsernameInput("liamneeson");
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

        loginPage.setUsernameInput("liamneeson");
        loginPage.setPasswordInput("liamneeson");
        loginPage.clickLoginButton();

        loginPage.ensureIsNotDisplayedLoginBtn(); // Ensure there's no login btn -> it means url has been changed

        Assertions.assertEquals("http://localhost:4200/home/system-admin/workers", browser.getCurrentUrl());
    }

    @Test
    public void successfulEditing() {
        Assertions.assertEquals(9, systemPage.getTableRows().size());
        Assertions.assertFalse(systemPage.getFirstNameInput().isEnabled());
        Assertions.assertEquals("John", systemPage.getFirstNameInput().getAttribute("value"));

        systemPage.clickSelectedRow(1);
        Assertions.assertEquals("Simon", systemPage.getFirstNameInput().getAttribute("value"));

        systemPage.clickButton(0);
        Assertions.assertTrue(systemPage.getFirstNameInput().isEnabled());
        systemPage.setFirstNameInput("Pera zdera");
        Assertions.assertEquals("Pera zdera", systemPage.getFirstNameInput().getAttribute("value"));

        systemPage.clickButton(1);
        Assertions.assertFalse(systemPage.getFirstNameInput().isEnabled());
    }

    @AfterAll
    public static void tearDown() {
//        browser.quit();
    }
}
