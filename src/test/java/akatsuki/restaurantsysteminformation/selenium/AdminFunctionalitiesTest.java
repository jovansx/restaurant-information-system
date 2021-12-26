package akatsuki.restaurantsysteminformation.selenium;

import akatsuki.restaurantsysteminformation.seleniumpages.LoginPage;
import akatsuki.restaurantsysteminformation.seleniumpages.ManagerEmployeesPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class AdminFunctionalitiesTest {

    private static WebDriver browser;

    private static LoginPage loginPage;
//    private static ManagerEmployeesPage managerPage;

//    private final String MAT_FORM_FIELD_TO_INPUT = "div/div[1]/div/input";

    @BeforeAll
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");

        browser = new ChromeDriver();
        browser.manage().window().maximize();
        browser.navigate().to("http://localhost:4200/login");

        loginPage = PageFactory.initElements(browser, LoginPage.class);
//        managerPage = PageFactory.initElements(browser, ManagerEmployeesPage.class);

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

    @AfterAll
    public static void tearDown() {
        browser.quit();
    }
}
