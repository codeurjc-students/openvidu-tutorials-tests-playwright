package ToolsQA;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Test with Java.
 *
 * @author Andrea Acuña
 */
class openViduJsTest {

    String evidencesFolder = "../../../../ScreenShots";

    WebDriver driverChrome;
    WebDriver driverFirefox;

    String URL = "http://localhost:8080/";

    String NAMESESSION = "TestSession";

    String XpathJoinButton = "//*[@id='join']/form/p[2]/input";
    String xpathLeaveButton = "//*[@id='session']/input";


/**
 * Test with Java.
 *
 * @author Andrea Acuña
 * Description: Execute before every single test. Configure the camera an set de url in each browser
 */
    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--use-fake-ui-for-media-stream");
        options.addArguments("--use-fake-device-for-media-stream");
        driverChrome = new ChromeDriver(options);

        FirefoxOptions optionsF = new FirefoxOptions();
        optionsF.addPreference("media.navigator.permission.disabled", true);
        optionsF.addPreference("media.navigator.streams.fake", true);
        driverFirefox = new FirefoxDriver(optionsF);

        driverChrome.get(URL); 
        driverFirefox.get(URL);
    }

/**
 * Test with Java.
 *
 * @author Andrea Acuña
 * Description: Join the session and verificate that the two browsers are inside the session
 */
    @Test
    void JoinSession() {
        // Configurate the session in chrome
        WebElement textBox = driverChrome.findElement(By.id("sessionId"));
        textBox.clear();
        textBox.sendKeys(NAMESESSION);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();
        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.id("sessionId"));
        textBoxF.clear();
        textBoxF.sendKeys(NAMESESSION);
        WebElement joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton)); 
        joinButtonF.submit();

        try{
            if (!driverChrome.findElements(By.id("session-header")).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 1");
            }
            if (!driverFirefox.findElements(By.id("session-header")).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 2");
            }
        }catch (NoSuchElementException n){
            System.out.println("The app is not correctly inicializate");
        }
    }

/**
 * Test with Java.
 *
 * @author Andrea Acuña
 * Description: Join the session, verficate that the video is playing property and leave the session
 */
    @Test
    void LeaveSession() {

        // Configurate the session in chrome
        WebElement textBox = driverChrome.findElement(By.id("sessionId"));
        textBox.clear();
        textBox.sendKeys(NAMESESSION);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();
        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.id("sessionId"));
        textBoxF.clear();
        textBoxF.sendKeys(NAMESESSION);
        WebElement joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton)); 
        joinButtonF.submit();

        // see if the video is playing properly
        String currentTimeChrome = driverChrome.findElement(By.id("local-video-undefined")).getAttribute("currentTime");
        String currentTimeFirefox = driverFirefox.findElement(By.id("local-video-undefined")).getAttribute("currentTime");

        if (Float.parseFloat(currentTimeChrome) > 0 && Float.parseFloat(currentTimeFirefox) > 0){
                //Leave the session with chrome
            try{
                WebElement leaveButtonC = driverChrome.findElement(By.xpath(xpathLeaveButton));
                if (leaveButtonC.isDisplayed()){ 
                    leaveButtonC.click();
                }
                if(joinButtonC.isDisplayed()){
                    System.out.println("The app leave the session correctly in browser 1");
                }

            }catch (NoSuchElementException n){
                System.out.println("The app is not correctly working in browser 1");
            }

                //Leave the session with Firefox
            try{
                WebElement leaveButtonF = driverFirefox.findElement(By.xpath(xpathLeaveButton));
                if (leaveButtonF.isDisplayed()){ 
                    leaveButtonF.click();
                }
                if(joinButtonF.isDisplayed()){
                    System.out.println("The app leave the session correctly in browser 2");
                }

            }catch (NoSuchElementException n){
                System.out.println("The app is not correctly working in browser 2");
            }
        }else{
            System.out.println("The video is not playing properly");
        }
    }


    @AfterEach
    void quit() {
        driverFirefox.quit();
        driverChrome.quit();
    }
}
