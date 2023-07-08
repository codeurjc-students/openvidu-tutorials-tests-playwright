
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public class BasicFunctions{

    public void clickElement(WebElement element, WebDriver driver) throws IOException{

        WebDriverWait waitC = new WebDriverWait(driver, Duration.ofSeconds(30));
        waitC.until(ExpectedConditions.elementToBeClickable(element));

        if (element.isDisplayed()){
            element.click();
        }

    }


    public void sendKeysById(WebElement textBox, String text) throws IOException{

        textBox.clear();
        textBox.sendKeys(text);

    }

    public void submitElement(WebElement element, WebDriver driver) throws IOException{

        WebDriverWait waitC = new WebDriverWait(driver, Duration.ofSeconds(30));
        waitC.until(ExpectedConditions.elementToBeClickable(element));

        if (element.isDisplayed()){
            element.submit();
        }

    }


    public String getAttributeById(WebElement element, String attribute) throws IOException{

        String currentTimeChrome = element.getAttribute(attribute);
        return currentTimeChrome;
    }


}
