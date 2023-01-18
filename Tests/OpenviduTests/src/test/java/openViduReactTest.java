import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.NoSuchElementException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Test with Java.
 * Test for the openvidu angular 
 * @author Andrea Acuña
 */
class openViduReactTest {

    String evidencesFolder = "..\\..\\evidence";

    WebDriver driverChrome;
    WebDriver driverFirefox;

    String URL = "http://localhost:3000/";

    String NAMESESSION = "TestSession";

    String XpathJoinButton = "//*[@id='join-dialog']/form/p[3]/input";
    String idParticipant = "userName";
    String idLeaveButton = "buttonLeaveSession";
    String idSession = "sessionId";
    String idheader = "session-title";
    String xpathOtherCamera = "/html/body/div/div/div/div[3]/div[2]/div/div/video";


/**
 * Test with Java.
 *
 * @author Andrea Acuña
 * Description: Execute before every single test. Configure the camera an set de url in each browser
 */
    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--ignore-certificate-errors","--disable-extensions","--no-sandbox","--disable-dev-shm-usage");
        options.addArguments("--use-fake-ui-for-media-stream");
        options.addArguments("--use-fake-device-for-media-stream");
        driverChrome = new ChromeDriver(options);

        FirefoxOptions optionsF = new FirefoxOptions();
        optionsF.setHeadless(true);
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
 * @throws IOException
 */
    @Test
    void JoinSession() throws IOException {
        // Configurate the session in chrome
        WebElement sessionC = driverChrome.findElement(By.id(idSession));
        sessionC.clear();
        sessionC.sendKeys(NAMESESSION);
        WebElement participantC = driverChrome.findElement(By.id(idParticipant));
        participantC.clear();
        participantC.sendKeys("PARTICIPANTCHROME");
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();

        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.id(idSession));
        textBoxF.clear();
        textBoxF.sendKeys(NAMESESSION);
        WebElement participantF = driverFirefox.findElement(By.id(idParticipant));
        participantF.clear();
        participantF.sendKeys("PARTICIPANTFIREFOX");
        WebElement joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton)); 
        joinButtonF.submit();

        try{
            if (!driverChrome.findElements(By.id(idheader)).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 1");
                takePhoto(evidencesFolder + "\\OKC.png", "", driverChrome, driverFirefox);
            }
            if (!driverFirefox.findElements(By.id(idheader)).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 2");
                takePhoto("", evidencesFolder + "\\OKF.png", driverChrome, driverFirefox);
            }
        }catch (NoSuchElementException n){
            System.out.println("The app is not correctly inicializate");
            takePhoto(evidencesFolder + "\\ERRORInicializateC.png", evidencesFolder + "\\ERRORInicializateF.png", driverChrome, driverFirefox);
        }
    }

/**
 * Test with Java.
 *
 * @author Andrea Acuña
 * Description: Join the session, verficate that the video is playing property and leave the session
 * @throws IOException
 */
    @Test
    void LeaveSession() throws IOException {

        // Configurate the session in chrome
        WebElement sessionC = driverChrome.findElement(By.id(idSession));
        sessionC.clear();
        sessionC.sendKeys(NAMESESSION);
        WebElement participantC = driverChrome.findElement(By.id(idParticipant));
        participantC.clear();
        participantC.sendKeys("PARTICIPANTCHROME");
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();

        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.id(idSession));
        textBoxF.clear();
        textBoxF.sendKeys(NAMESESSION);
        WebElement participantF = driverFirefox.findElement(By.id(idParticipant));
        participantF.clear();
        participantF.sendKeys("PARTICIPANTFIREFOX");
        WebElement joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton)); 
        joinButtonF.submit();

        // see if the video is playing properly, moreover synchronize both videos
        WebDriverWait waitC = new WebDriverWait(driverChrome, Duration.ofSeconds(30));
        waitC.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOtherCamera)));
        
        WebDriverWait waitF = new WebDriverWait(driverFirefox, Duration.ofSeconds(30));
        waitF.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOtherCamera)));

         String currentTimeChrome = driverChrome.findElement(By.id("local-video-undefined")).getAttribute("currentTime");
         String currentTimeFirefox = driverFirefox.findElement(By.id("local-video-undefined")).getAttribute("currentTime");

         takePhoto(evidencesFolder + "\\videoPlayingC.png", evidencesFolder + "\\videoPlayingF.png", driverChrome, driverFirefox);
 
         if (Float.parseFloat(currentTimeChrome) > 0 && Float.parseFloat(currentTimeFirefox) > 0){
                 //Leave the session with chrome
             try{
                 WebElement leaveButtonC = driverChrome.findElement(By.id(idLeaveButton));
                 if (leaveButtonC.isDisplayed()){ 
                     leaveButtonC.click();
                 }
                 WebElement titleC = driverChrome.findElement(By.id(idheader));
                 if(titleC.isDisplayed()){
                     System.out.println("The app leave the session correctly in browser 1");
                     takePhoto(evidencesFolder + "\\LeaveSessionC.png", "", driverChrome, driverFirefox);
                 }
 
             }catch (NoSuchElementException n){
                 System.out.println("The app is not correctly working in browser 1");
                 takePhoto(evidencesFolder + "\\NOTLeaveSessionC.png", "", driverChrome, driverFirefox);
             }
 
                 //Leave the session with Firefox
             try{
                 WebElement leaveButtonF = driverFirefox.findElement(By.id(idLeaveButton));
                 if (leaveButtonF.isDisplayed()){ 
                     leaveButtonF.click();
                 }
                 WebElement titleF = driverFirefox.findElement(By.id(idheader));
                 if(titleF.isDisplayed()){
                     System.out.println("The app leave the session correctly in browser 2");
                     takePhoto("", evidencesFolder + "\\LeaveSessionF.png", driverChrome, driverFirefox);
                 }
 
             }catch (NoSuchElementException n){
                 System.out.println("The app is not correctly working in browser 2");
                 takePhoto("", evidencesFolder + "\\NOTLeaveSessionF.png", driverChrome, driverFirefox);
             }
         }else{
             System.out.println("The video is not playing properly");
             takePhoto(evidencesFolder + "\\ERRORC.png", evidencesFolder + "\\ERRORF.png", driverChrome, driverFirefox);
         }
    }


    /**
    * AfterEach.
    *
    * @author Andrea Acuña
    * Description: close both drivers
    */
    @AfterEach
    void quit() {

        if (driverChrome != null){
            driverChrome.quit();
        }
        if (driverFirefox != null){
            driverFirefox.quit();
        }
    }

    /**
    * method.
    *
    * @author Andrea Acuña
    * Description: take a screenshot to create an evidence.
    * Parameters: 
    *          - url1: the relative or absolute path to a evidence file of the chrome photo
    *          - url2: the relative or absolute path to a evidence file of the firefox photo
    */
    public void takePhoto(String url1, String url2, WebDriver c, WebDriver f) throws IOException{
        try {
            if(url1 != ""){
                File scrFileC = ((TakesScreenshot)c).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFileC, new File(url1));
            }
            if(url2 != ""){
                File scrFileF = ((TakesScreenshot)f).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFileF, new File(url2));
            }          
        } catch (Exception e) {
            System.out.println("an error has occurred with the screenshot. Please preview the url");
        }

        
    }
}

