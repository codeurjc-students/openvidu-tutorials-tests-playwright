import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Test with Java.
 * Test for the openvidu angular 
 * @author Andrea Acuña
 */
class OpenViduReactTest extends Module{

    String evidencesFolder = "..\\..\\evidence";

    WebDriver driverChrome;
    WebDriver driverFirefox;

    String URL = "http://localhost:3000/";

    String NAMESESSION = "TestSession";
    String NAMEPARTICIPANT = "TestParticipant";

    String XpathJoinButton = "//*[@id='join-dialog']/form/p[3]/input";
    String xpathOtherCamera = "/html/body/div/div/div/div[3]/div[2]/div/div/video";
    String xpathParticipant = "//*[@id='main-video']/div/div/div/p";
    
    String idParticipant = "userName";
    String idLeaveButton = "buttonLeaveSession";
    String idNameSession = "sessionId";
    String idHeader = "session-title";
    String idMainTitle = "join";
    String idSelfCamera = "local-video-undefined";
    String idNameParticipant = "userName";


/**
 * Test with Java.
 *
 * @author Andrea Acuña
 * Description: Execute before every single test. Configure the camera an set de url in each browser
 */
    @BeforeEach
    void setup() {
        List<WebDriver> browsers = super.setUpTwoBrowsers();
        driverChrome = browsers.get(0);
        driverFirefox = browsers.get(1);
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
    void T001_JoinSession() throws IOException {
        // Configurate the session in chrome
        WebElement sessionC = driverChrome.findElement(By.id(idNameSession));
        sessionC.clear();
        sessionC.sendKeys(NAMESESSION);
        WebElement participantC = driverChrome.findElement(By.id(idParticipant));
        participantC.clear();
        participantC.sendKeys("PARTICIPANTCHROME");
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();

        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.id(idNameSession));
        textBoxF.clear();
        textBoxF.sendKeys(NAMESESSION);
        WebElement participantF = driverFirefox.findElement(By.id(idParticipant));
        participantF.clear();
        participantF.sendKeys("PARTICIPANTFIREFOX");
        WebElement joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton)); 
        joinButtonF.submit();

        try{
            if (!driverChrome.findElements(By.id(idHeader)).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 1");
                super.takePhoto(evidencesFolder + "\\R_OK_C.png", "", driverChrome, driverFirefox);
            }
            if (!driverFirefox.findElements(By.id(idHeader)).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 2");
                super.takePhoto("", evidencesFolder + "\\R_OK_F.png", driverChrome, driverFirefox);
            }
        }catch (NoSuchElementException n){
            super.takePhoto(evidencesFolder + "\\R_ERRORInicializate_C.png", evidencesFolder + "\\R_ERRORInicializate_F.png", driverChrome, driverFirefox);
            fail("The app is not correctly inicializate");
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
    void T002_LeaveSession() throws IOException {

        // Configurate the session in chrome
        WebElement sessionC = driverChrome.findElement(By.id(idNameSession));
        sessionC.clear();
        sessionC.sendKeys(NAMESESSION);
        WebElement participantC = driverChrome.findElement(By.id(idParticipant));
        participantC.clear();
        participantC.sendKeys("PARTICIPANTCHROME");
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();

        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.id(idNameSession));
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

         String currentTimeChrome = driverChrome.findElement(By.id(idSelfCamera)).getAttribute("currentTime");
         String currentTimeFirefox = driverFirefox.findElement(By.id(idSelfCamera)).getAttribute("currentTime");

         super.takePhoto(evidencesFolder + "\\R_videoPlaying_C.png", evidencesFolder + "\\R_videoPlaying_F.png", driverChrome, driverFirefox);
 
         if (Float.parseFloat(currentTimeChrome) > 0 && Float.parseFloat(currentTimeFirefox) > 0){
                 //Leave the session with chrome
             try{
                 WebElement leaveButtonC = driverChrome.findElement(By.id(idLeaveButton));
                 if (leaveButtonC.isDisplayed()){ 
                     leaveButtonC.click();
                 }
                 WebElement titleC = driverChrome.findElement(By.id(idMainTitle));
                 if(titleC.isDisplayed()){
                     System.out.println("The app leave the session correctly in browser 1");
                     super.takePhoto(evidencesFolder + "\\R_LeaveSession_C.png", "", driverChrome, driverFirefox);
                 }
 
             }catch (NoSuchElementException n){
                super.takePhoto(evidencesFolder + "\\R_NOTLeaveSession_C.png", "", driverChrome, driverFirefox);
                 fail("The app is not correctly working in browser 1");
             }
 
                 //Leave the session with Firefox
             try{
                 WebElement leaveButtonF = driverFirefox.findElement(By.id(idLeaveButton));
                 if (leaveButtonF.isDisplayed()){ 
                     leaveButtonF.click();
                 }
                 WebElement titleF = driverFirefox.findElement(By.id(idMainTitle));
                 if(titleF.isDisplayed()){
                     System.out.println("The app leave the session correctly in browser 2");
                     super.takePhoto("", evidencesFolder + "\\R_LeaveSession_F.png", driverChrome, driverFirefox);
                 }
 
             }catch (NoSuchElementException n){
                super.takePhoto("", evidencesFolder + "\\R_NOTLeaveSession_F.png", driverChrome, driverFirefox);
                 fail("The app is not correctly working in browser 2");
             }
         }else{
            super.takePhoto(evidencesFolder + "\\R_ERROR_C.png", evidencesFolder + "\\R_ERROR_F.png", driverChrome, driverFirefox);
             fail("The video is not playing properly");
         }
    }

/**
 * Test with Java.
 *
 * @author Andrea Acuña
 * Description: Joins the session and verifies that the session name is correct
 * @throws IOException
 */
    @Test
    void T003_SessionHeader() throws IOException {
        // Configurate the session in chrome
        WebElement textBox = driverChrome.findElement(By.id(idNameSession));
        textBox.clear();
        textBox.sendKeys(NAMESESSION);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();

        try{
            if (!driverChrome.findElements(By.id(idHeader)).isEmpty()){
                if (driverChrome.findElement(By.id(idHeader)).getText() == NAMESESSION){
                    System.out.println("The title is correctly set");
                    super.takePhoto(evidencesFolder + "\\R_OK_C.png", "", driverChrome, driverFirefox);
                }
            }
        }catch (NoSuchElementException n){
            super.takePhoto(evidencesFolder + "\\R_ErrorInicializate_C.png", evidencesFolder + "", driverChrome, driverFirefox);
            fail("The app is not correctly inicializate");
        }
    }

/**
 * Test with Java.
 *
 * @author Andrea Acuña
 * Description: Joins the session and verifies that the participant name is correct
 * @throws IOException
 */
@Test
void T004_ParticipantName() throws IOException {
    // Configurate the session in chrome
    WebElement nameTextBox = driverChrome.findElement(By.id(idNameParticipant));
    nameTextBox.clear();
    nameTextBox.sendKeys(NAMEPARTICIPANT);
    WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
    joinButtonC.submit();

    // wait for the text
    WebDriverWait waitC = new WebDriverWait(driverChrome, Duration.ofSeconds(30));
    waitC.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathParticipant)));

    try{
        if (driverChrome.findElement(By.xpath(xpathParticipant)).getText() == NAMEPARTICIPANT){
                System.out.println("The name of the participant is correctly set");
                super.takePhoto(evidencesFolder + "\\R_OK_C.png", "", driverChrome, driverFirefox);
        }
        
    }catch (NoSuchElementException n){
        super.takePhoto(evidencesFolder + "\\R_ErrorInicializate_C.png", evidencesFolder + "", driverChrome, driverFirefox);
        fail("The app is not correctly inicializate");
    }
}

// ME GUSTARÍA HACER UN TEST PARA EL BOTÓN "SWITCH CAMERA"

    /**
    * AfterEach.
    *
    * @author Andrea Acuña
    * Description: close both drivers
    */
    @AfterEach
    void quit() {
        super.quitTwoBrowsers(driverChrome, driverFirefox);
    }
}

