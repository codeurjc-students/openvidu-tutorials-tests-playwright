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
 * Test for the java deployment of open vidu 
 * @author Andrea Acuña
 */
class OpenViduVueTest extends Module{

    String evidencesFolder = "..\\..\\evidence";

    WebDriver driverChrome;
    WebDriver driverFirefox;

    String URL = "http://localhost:8080/";

    String NAMESESSION = "TestSession";
    String NAMEPARTICIPANT = "TestParticipant";

    String XpathJoinButton = "//*[@id='join-dialog']/div/p[3]/button";
    String xpathOtherCamera = "/html/body/div/div/div[3]/div[2]/video";
    String xpathSessionName = "//*[@id='join-dialog']/div/p[2]/input";
    String xpathParticipant = "//*[@id='main-video']/div/div/p";

    String idLeaveButton = "buttonLeaveSession";
    String idHeader = "session-title";
    String idSelfCamera = "local-video-undefined";


/**
 * BeforeEach.
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
    void JoinSession() throws IOException {
        // Configurate the session in chrome
        WebElement textBox = driverChrome.findElement(By.xpath(xpathSessionName));
        textBox.clear();
        textBox.sendKeys(NAMESESSION);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.click();
        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.xpath(xpathSessionName));
        textBoxF.clear();
        textBoxF.sendKeys(NAMESESSION);
        WebElement joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton)); 
        joinButtonF.click();

        try{
            if (!driverChrome.findElements(By.id(idHeader)).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 1");
                super.takePhoto(evidencesFolder + "\\VUE_OK_C.png", "", driverChrome, driverFirefox);
            }
            if (!driverFirefox.findElements(By.id(idHeader)).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 2");
                super.takePhoto("", evidencesFolder + "\\VUE_OK_F.png", driverChrome, driverFirefox);
            }
        }catch (NoSuchElementException n){
            super.takePhoto(evidencesFolder + "\\VUE_ErrorInicializate_C.png", evidencesFolder + "\\VUE_ErrorInicializate_F.png", driverChrome, driverFirefox);
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
    void LeaveSession() throws IOException{

        // Configurate the session in chrome
        WebElement textBox = driverChrome.findElement(By.xpath(xpathSessionName));
        textBox.clear();
        textBox.sendKeys(NAMESESSION);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.click();
        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.xpath(xpathSessionName));
        textBoxF.clear();
        textBoxF.sendKeys(NAMESESSION);
        WebElement joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton)); 
        joinButtonF.click();

        // see if the video is playing properly, moreover synchronize both videos
        WebDriverWait waitC = new WebDriverWait(driverChrome, Duration.ofSeconds(30));
        waitC.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathOtherCamera)));
        
        WebDriverWait waitF = new WebDriverWait(driverFirefox, Duration.ofSeconds(30));
        waitF.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathOtherCamera)));

        // see if the video is playing properly
        String currentTimeChrome = driverChrome.findElement(By.id(idSelfCamera)).getAttribute("currentTime");
        String currentTimeFirefox = driverFirefox.findElement(By.id(idSelfCamera)).getAttribute("currentTime");
        super.takePhoto(evidencesFolder + "\\VUE_VideoPlaying_C.png", evidencesFolder + "\\VUE_VideoPlaying_F.png", driverChrome, driverFirefox);


        if (Float.parseFloat(currentTimeChrome) > 0 && Float.parseFloat(currentTimeFirefox) > 0){
                //Leave the session with chrome
            try{
                WebElement leaveButtonC = driverChrome.findElement(By.id(idLeaveButton));
                if (leaveButtonC.isDisplayed()){ 
                    leaveButtonC.click();
                }
                if(joinButtonC.isDisplayed()){
                    System.out.println("The app leave the session correctly in browser 1");
                    super.takePhoto(evidencesFolder + "\\VUE_LeaveSession_C.png", "", driverChrome, driverFirefox);
                }

                //Leave the session with Firefox

                WebElement leaveButtonF = driverFirefox.findElement(By.id(idLeaveButton));
                if (leaveButtonF.isDisplayed()){ 
                    leaveButtonF.click();
                }
                if(joinButtonF.isDisplayed()){
                    System.out.println("The app leave the session correctly in browser 2");
                    super.takePhoto("", evidencesFolder + "\\VUE_LeaveSession_F.png", driverChrome, driverFirefox);
                }

            }catch (NoSuchElementException n){
                super.takePhoto(evidencesFolder + "\\VUE_Error_C.png", evidencesFolder + "\\VUE_Error_F.png", driverChrome, driverFirefox);
                fail("The app is not correctly working");
            }
        }else{
            super.takePhoto(evidencesFolder + "\\VUE_VideoNotWorking_C.png", evidencesFolder + "\\VUE_VideoNotWorking_F.png", driverChrome, driverFirefox);
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
            WebElement textBox = driverChrome.findElement(By.xpath(xpathSessionName));
            textBox.clear();
            textBox.sendKeys(NAMESESSION);
            WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
            joinButtonC.submit();

            try{
                if (!driverChrome.findElements(By.id(idHeader)).isEmpty()){
                    if (driverChrome.findElement(By.id(idHeader)).getText() == NAMESESSION){
                        System.out.println("The title is correctly set");
                        super.takePhoto(evidencesFolder + "\\VUE_OK_C.png", "", driverChrome, driverFirefox);
                    }
                }
            }catch (NoSuchElementException n){
                super.takePhoto(evidencesFolder + "\\VUE_ErrorInicializate_C.png", evidencesFolder + "", driverChrome, driverFirefox);
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
        WebElement nameTextBox = driverChrome.findElement(By.xpath(xpathSessionName));
        nameTextBox.clear();
        nameTextBox.sendKeys(NAMEPARTICIPANT);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();

        try{
            if (driverChrome.findElement(By.xpath(xpathParticipant)).getText() == NAMEPARTICIPANT){
                    System.out.println("The name of the participant is correctly set");
                    super.takePhoto(evidencesFolder + "\\VUE_OK_C.png", "", driverChrome, driverFirefox);
            }
            
        }catch (NoSuchElementException n){
            super.takePhoto(evidencesFolder + "\\VUE_ErrorInicializate_C.png", evidencesFolder + "", driverChrome, driverFirefox);
            fail("The app is not correctly inicializate");
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
        super.quitTwoBrowsers(driverChrome, driverFirefox);
    }

}
