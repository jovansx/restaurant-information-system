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
	
	@FindBy(xpath = "//*[@class='product_list grid row']//*[@class='product-name']")
	private List<WebElement> productTitles;
	
	public boolean productContainsText(String text) {
		for (WebElement element : productTitles) {
			if (!element.getText().contains(text))
				return false;
		}

		return true;
	}
	
	@Test
	public void searchTest() {
	// try empty search
	homePage.submitSearchBtnClick();

	assertEquals(
			"http://automationpractice.com/index.php?controller=search&orderby=position&orderway=desc&search_query=&submit_search=",
			driver.getCurrentUrl());

	assertTrue(searchResultsPage.errorMessagePresent("Please enter a search keyword"));
	assertTrue(searchResultsPage.resultMessagePresent("0 results have been found."));

	// try non existing search
	homePage.setSearchInput("Non existing term");
	homePage.submitSearchBtnClick();

	assertEquals(
			"http://automationpractice.com/index.php?controller=search&orderby=position&orderway=desc&search_query=Non+existing+term&submit_search=",
			driver.getCurrentUrl());

	assertTrue(
			searchResultsPage.errorMessagePresent("No results were found for your search \"Non existing term\""));
	assertTrue(searchResultsPage.resultMessagePresent("0 results have been found."));

	// try existing term
	homePage.setSearchInput("dress");
	homePage.submitSearchBtnClick();

	assertEquals(
			"http://automationpractice.com/index.php?controller=search&orderby=position&orderway=desc&search_query=dress&submit_search=",
			driver.getCurrentUrl());

	assertTrue(searchResultsPage.productContainsText("dress"));

	}
	
	public boolean errorMessagePresent(String text) {
		return Utilities.textWait(driver, this.searchAlertDiv, text, 10);
	}
	
	public WebElement getEmailInput() {
		return Utilities.visibilityWait(driver, this.emailInput, 10);
	}

	public void setEmailInput(String value) {
		WebElement el = getEmailInput();
		el.clear();
		el.sendKeys(value);
	}
    
    public static boolean titleWait(WebDriver driver, String title, int wait) {
		return new WebDriverWait(driver, wait).until(ExpectedConditions.titleIs(title));
	}

	public static boolean textWait(WebDriver driver, WebElement element, String text, int wait) {
		return new WebDriverWait(driver, wait).until(ExpectedConditions.textToBePresentInElement(element, text));
	}
    
    public static WebElement presenceWait(WebDriver driver, By locator, int wait) {
		return new WebDriverWait(driver, wait).until(ExpectedConditions.presenceOfElementLocated(locator));
	}
    
    public static boolean isPresent(WebDriver driver, By locator) {

		try {
			return driver.findElement(locator).isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}

    }

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
        for (int i = 0; i < 50; i++){
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
