package akatsuki.restaurantsysteminformation.seleniumpages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Utilities {

    public static boolean urlWait(WebDriver driver, String url, int wait) {
        return new WebDriverWait(driver, Duration.ofSeconds(wait)).until(ExpectedConditions.urlToBe(url));
    }

    public static boolean titleWait(WebDriver driver, String title, int wait) {
        return new WebDriverWait(driver, Duration.ofSeconds(wait)).until(ExpectedConditions.titleIs(title));
    }

    public static void elementDisabledWait(WebDriver driver, WebElement element, int wait) {
        new WebDriverWait(driver, Duration.ofSeconds(wait)).until((ExpectedCondition<Boolean>) condition -> !element.isEnabled());
    }

    public static void elementEnabledWait(WebDriver driver, WebElement element, int wait) {
        new WebDriverWait(driver, Duration.ofSeconds(wait)).until((ExpectedCondition<Boolean>) condition -> element.isEnabled());
    }

    public static WebElement visibilityWait(WebDriver driver, WebElement element, int wait) {
        return new WebDriverWait(driver, Duration.ofSeconds(wait)).until(ExpectedConditions.visibilityOf(element));
    }

    public static List<WebElement> visibilityWait(WebDriver driver, By locator, int wait) {
        return new WebDriverWait(driver, Duration.ofSeconds(wait)).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static List<WebElement> numberOfElementsWait(WebDriver driver, By locator, int number, int wait) {
        return new WebDriverWait(driver, Duration.ofSeconds(wait)).until(ExpectedConditions.numberOfElementsToBe(locator, number));
    }

    public static void numberOfElementsWait(WebDriver driver, List<WebElement> elements, int number, int wait) {
        new WebDriverWait(driver, Duration.ofSeconds(wait)).until((ExpectedCondition<Boolean>) condition -> elements.size() == number);
    }

    public static WebElement presenceWait(WebDriver driver, By locator, int wait) {
        return new WebDriverWait(driver, Duration.ofSeconds(wait)).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static WebElement clickableWait(WebDriver driver, WebElement element, int wait) {
        return new WebDriverWait(driver, Duration.ofSeconds(wait)).until(ExpectedConditions.elementToBeClickable(element));
    }

    public static boolean isPresent(WebDriver driver, By locator) {

        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
