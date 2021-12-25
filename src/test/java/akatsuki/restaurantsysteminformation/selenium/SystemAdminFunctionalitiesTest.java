package akatsuki.restaurantsysteminformation.selenium;

import akatsuki.restaurantsysteminformation.seleniumpages.LoginPage;
import akatsuki.restaurantsysteminformation.seleniumpages.SystemAdminWorkersPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SystemAdminFunctionalitiesTest {

    private static WebDriver browser;

    private static LoginPage loginPage;
    private static SystemAdminWorkersPage systemPage;

    private final String MAT_FORM_FIELD_TO_INPUT = "div/div[1]/div/input";

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
    public void successfulAddingAndDeletingOfManager() {
        systemPage.ensureAddButtonIsDisplayed();
        Assertions.assertTrue(systemPage.getAddBtn().isDisplayed());

        systemPage.getAddBtn().click();

        systemPage.ensureAddButtonsAreDisplayed();
        systemPage.getAddButtons().get(0).click();

        systemPage.ensureDialogFieldsAreDisplayed();
        systemPage.getDialogFields().get(0).findElement(By.xpath(MAT_FORM_FIELD_TO_INPUT)).sendKeys("Leon");
        systemPage.getDialogFields().get(1).findElement(By.xpath(MAT_FORM_FIELD_TO_INPUT)).sendKeys("List");
        systemPage.getDialogFields().get(2).findElement(By.xpath(MAT_FORM_FIELD_TO_INPUT)).sendKeys("leon@gmail.com");
        systemPage.getDialogFields().get(3).findElement(By.xpath(MAT_FORM_FIELD_TO_INPUT)).sendKeys("leon_stronger");
        systemPage.getDialogFields().get(4).findElement(By.xpath(MAT_FORM_FIELD_TO_INPUT)).sendKeys("Leon_stronger123");
        systemPage.getDialogFields().get(5).findElement(By.xpath(MAT_FORM_FIELD_TO_INPUT)).sendKeys("Leon_stronger123");
        systemPage.getDialogFields().get(6).findElement(By.xpath(MAT_FORM_FIELD_TO_INPUT)).sendKeys("40000");
        systemPage.getDialogFields().get(7).findElement(By.xpath(MAT_FORM_FIELD_TO_INPUT)).sendKeys("0645533454");

        systemPage.ensureDialogButtonsAreDisplayed();
        systemPage.getDialogButtons().get(1).click();

        systemPage.ensureRowsAreDisplayed(10);
        Assertions.assertEquals(10, systemPage.getTableRows().size());

        for(WebElement el: systemPage.getTableRows()) {
            if (el.findElement(By.xpath("td[2]")).getText().equals("Leon List")) {
                el.findElement(By.xpath("td[5]/button")).click();
                break;
            }
        }

        systemPage.ensureRowsAreDisplayed(9);
        Assertions.assertEquals(9, systemPage.getTableRows().size());

    }

    @Test
    public void successfulAddingAndDeletingOfEmployee() {
        systemPage.ensureAddButtonIsDisplayed();
        Assertions.assertTrue(systemPage.getAddBtn().isDisplayed());

        systemPage.getAddBtn().click();

        systemPage.ensureAddButtonsAreDisplayed();
        systemPage.getAddButtons().get(1).click();

        systemPage.ensureDialogFieldsAreDisplayed();
        systemPage.getDialogFields().get(0).findElement(By.xpath(MAT_FORM_FIELD_TO_INPUT)).sendKeys("Mia");
        systemPage.getDialogFields().get(1).findElement(By.xpath(MAT_FORM_FIELD_TO_INPUT)).sendKeys("List");
        systemPage.getDialogFields().get(2).findElement(By.xpath(MAT_FORM_FIELD_TO_INPUT)).sendKeys("mia@gmail.com");
        systemPage.getDialogFields().get(3).findElement(By.xpath(MAT_FORM_FIELD_TO_INPUT)).sendKeys("7777");
        systemPage.getDialogFields().get(5).findElement(By.xpath(MAT_FORM_FIELD_TO_INPUT)).sendKeys("35000");
        systemPage.getDialogFields().get(6).findElement(By.xpath(MAT_FORM_FIELD_TO_INPUT)).sendKeys("0646633454");

        systemPage.ensureDialogButtonsAreDisplayed();
        systemPage.getDialogButtons().get(1).click();

        systemPage.ensureRowsAreDisplayed(10);
        Assertions.assertEquals(10, systemPage.getTableRows().size());

        for(WebElement el: systemPage.getTableRows()) {
            if (el.findElement(By.xpath("td[2]")).getText().equals("Mia List")) {
                el.findElement(By.xpath("td[5]/button")).click();
                break;
            }
        }

        systemPage.ensureRowsAreDisplayed(9);
        Assertions.assertEquals(9, systemPage.getTableRows().size());

    }

    @AfterAll
    public static void tearDown() {
//        browser.quit();
    }
}
