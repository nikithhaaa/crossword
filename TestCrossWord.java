import constants.TestConstants;
import locators.TestLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class TestCrossWord {
    ChromeDriver chromeDriver;
    WebDriverWait wait;
    @BeforeTest
    public void setUp(){
        System.setProperty("webdriver.chrome.driver", TestConstants.DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        chromeDriver = new ChromeDriver(options);
    }

    @Test
    public void verifySortingProducts() throws InterruptedException {
        chromeDriver.get(TestConstants.TEST_URL);
        chromeDriver.findElement(TestLocators.SEARCH_TEXT_BOX).sendKeys(TestConstants.SEARCH_KEYWORD);
        chromeDriver.findElement(TestLocators.SEARCH_TEXT_BOX).sendKeys(Keys.ENTER);
        chromeDriver.findElement(TestLocators.SORT_DROPDOWN).click();
        chromeDriver.findElement(TestLocators.LOW_TO_HIGH).click();
        List<WebElement> sortedProducts = chromeDriver.findElements(TestLocators.ITEM_PRODUCT_INFO);
        wait = new WebDriverWait(chromeDriver, 10);
        List<String> discountedBooks = new ArrayList<>();
        int previousPrice = -1;
        int currentPrice;
        int desiredMinValue = 600;
        int desiredMaxValue = 900;
        int originalMinValue = 199;
        int originalMaxValue = 999;
        ((JavascriptExecutor) chromeDriver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        WebElement slider = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("footer-columns row section padless-top")));
        for(WebElement product : sortedProducts ){
            System.out.println("product");
            System.out.println(product.findElement(By.xpath(".//p[@class='product-item-title']")).getText());
            ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(true);", product);
            List<WebElement> nonDiscountedPrice = product.findElements(TestLocators.NON_DISCOUNTED_PRICE);
            if(nonDiscountedPrice.size() == 0){
                System.out.println("discountedPrice");
                System.out.println(product.findElement(TestLocators.ORIGINAL_PRICE).getText());
                currentPrice = Integer.parseInt(product.findElement(TestLocators.ORIGINAL_PRICE).getText().replace("₹", "").trim());
                Assert.assertTrue(currentPrice >= previousPrice);
            } else{
                System.out.println(nonDiscountedPrice.get(0).getText());
                currentPrice = Integer.parseInt(nonDiscountedPrice.get(0).getText().replace("₹", "").trim());
                Assert.assertTrue(currentPrice >= previousPrice);
                discountedBooks.add(nonDiscountedPrice.get(0).getText());
            }
        }
        for(String book: discountedBooks){
            Assert.assertTrue(TestConstants.bookTitles.contains(book));
        }

        List<WebElement> priceRangeProducts = chromeDriver.findElements(TestLocators.ITEM_PRODUCT_INFO);
        WebElement priceFilter = chromeDriver.findElement(TestLocators.FILTER_PRICE);
        ((JavascriptExecutor) chromeDriver).executeScript("arguments[0].scrollIntoView(true);", priceFilter);
        chromeDriver.findElement(TestLocators.FILTER_PRICE).click();
        WebElement slider = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("wizzy-facet-range-slider")));
        WebElement lowerHandle = slider.findElement(TestLocators.LOWER_HANDLE);
        WebElement upperHandle = slider.findElement(TestLocators.UPPER_HANDLE);
        double lowerHandlePercentage = (desiredMinValue - originalMinValue) / (double) (originalMaxValue - originalMinValue) * 100;
        double upperHandlePercentage = (desiredMaxValue - originalMinValue) / (double) (originalMaxValue - originalMinValue) * 100;
        Actions actions = new Actions(chromeDriver);
        actions.clickAndHold(lowerHandle).moveByOffset((int) (slider.getSize().getWidth() * (lowerHandlePercentage / 100) - lowerHandle.getSize().getWidth() / 2), 0).release().perform();
        actions.clickAndHold(upperHandle).moveByOffset((int) (slider.getSize().getWidth() * (upperHandlePercentage / 100) - upperHandle.getSize().getWidth() / 2), 0).release().perform();
        for (WebElement product : priceRangeProducts) {
            List<WebElement> nonDiscountedPrice = product.findElements(TestLocators.NON_DISCOUNTED_PRICE);
            if(nonDiscountedPrice.size() == 0){
                currentPrice = Integer.parseInt(product.findElement(TestLocators.ORIGINAL_PRICE).getText().replace("₹", "").trim());
                Assert.assertTrue(desiredMinValue <= currentPrice && currentPrice <= desiredMaxValue);
            } else{
                System.out.println(nonDiscountedPrice.get(0).getText());
                currentPrice = Integer.parseInt(nonDiscountedPrice.get(0).getText().replace("₹", "").trim());
                Assert.assertTrue(desiredMinValue <= currentPrice && currentPrice <= desiredMaxValue);
            }
        }
    }
}
