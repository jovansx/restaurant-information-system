package akatsuki.restaurantsysteminformation.seleniumpages;

import org.openqa.selenium.By;
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

    public static boolean elementDisabledWait(WebDriver driver, WebElement element, int wait) {
        return new WebDriverWait(driver, Duration.ofSeconds(wait)).until((ExpectedCondition<Boolean>) condition -> !element.isEnabled());
    }

    public static boolean elementEnabledWait(WebDriver driver, WebElement element, int wait) {
        return new WebDriverWait(driver, Duration.ofSeconds(wait)).until((ExpectedCondition<Boolean>) condition -> element.isEnabled());
    }

    public static WebElement visibilityWait(WebDriver driver, WebElement element, int wait) {
        return new WebDriverWait(driver, Duration.ofSeconds(wait)).until(ExpectedConditions.visibilityOf(element));
    }

    public static List<WebElement> visibilityAllWait(WebDriver driver, List<WebElement> elements, int wait) {
        return new WebDriverWait(driver, Duration.ofSeconds(wait)).until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public static boolean invisibilityAllWait(WebDriver driver, List<WebElement> elements, int wait) {
        return new WebDriverWait(driver, Duration.ofSeconds(wait)).until(ExpectedConditions.invisibilityOfAllElements(elements));
    }

    public static List<WebElement> numberOfElementsWait(WebDriver driver, By locator, int number, int wait) {
        return new WebDriverWait(driver, Duration.ofSeconds(wait)).until(ExpectedConditions.numberOfElementsToBe(locator, number));
    }

    public static boolean numberOfElementsWait(WebDriver driver, List<WebElement> elements, int number, int wait) {
        return new WebDriverWait(driver, Duration.ofSeconds(wait)).until((ExpectedCondition<Boolean>) condition -> elements.size() == number);
    }

    public static WebElement clickableWait(WebDriver driver, WebElement element, int wait) {
        return new WebDriverWait(driver, Duration.ofSeconds(wait)).until(ExpectedConditions.elementToBeClickable(element));
    }

    public static boolean invisibilityWait(WebDriver driver, WebElement element, int wait) {
        return new WebDriverWait(driver, Duration.ofSeconds(wait)).until(ExpectedConditions.invisibilityOf(element));
    }

    public static void clickButtonUntilItIsClicked(WebElement button) {
        for (int i = 0; i < 50; i++) {
            try {
                button.click();
                return;
            } catch (Exception ignored) {
            }
            try {
                Thread.sleep(100);
            } catch (Exception ignored) {
            }
        }
    }
}
