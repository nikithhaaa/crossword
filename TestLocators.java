package locators;

import org.openqa.selenium.By;

public class TestLocators {
    public static final By SEARCH_TEXT_BOX = By.xpath("//input[@class='main-search__input wizzy-search-input']");
    public static final By SORT_DROPDOWN = By.xpath("//div[@class='wizzy-common-select-selector']");
    public static final By LOW_TO_HIGH = By.xpath("//div[contains(@title, 'Price: Low to High')]");
    public static final By NON_DISCOUNTED_PRICE = By.xpath(".//span[@class='product-item-original-price']");
    public static final By ITEM_PRODUCT_INFO = By.xpath("//div[@class='result-product-item-info']");
    public static final By ORIGINAL_PRICE = By.xpath(".//div[@class='wizzy-product-item-price ']");
    public static final By FILTER_PRICE = By.xpath("//div[@class='wizzy-facet-head facet-head-sellingPrice']");
    public static final By LOWER_HANDLE = By.xpath("//div[@class='noUi-handle noUi-handle-lower']");
    public static final By UPPER_HANDLE = By.xpath("//div[@class='noUi-handle noUi-handle-upper']");

}
