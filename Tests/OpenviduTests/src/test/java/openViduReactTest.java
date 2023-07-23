import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

/**
 * Test with Java.
 * Test for the openvidu angular 
 * @author Andrea Acuña
 */
class OpenViduReactTest extends Module{

    String testLocation = "test-input/Parameters.xlsx";
    String reportLocation = "OpenViduReactTestTestReport.html";

    private ExtentTest test;
    public static ExtentReports extentReports;

    WebDriver driverChrome;
    WebDriver driverFirefox;

    String URL;

    String NAMESESSION;
    String NAMEPARTICIPANT;
    String TESTNAME;

    String XpathJoinButton;
    String xpathOtherCamera;
    String xpathParticipant;
    
    String idParticipant;
    String idLeaveButton;
    String idNameSession;
    String idHeader;
    String idMainTitle;
    String idSelfCamera;
    String idNameParticipant;

    public OpenViduReactTest() {
        if (extentReports == null){
            extentReports = super.createExtentReports(reportLocation);
        }
    }


    /**
     * Test with Java.
     *
     * @author Andrea Acuña
     * Description: Execute before every single test. Configure the camera an set de url in each browser
     */
    @BeforeEach
    void setup() {
        URL = readVariablesFromExcel(testLocation, "OpenViduReactTest", "URL");
        List<WebDriver> browsers = super.setUpTwoBrowsers();
        driverChrome = browsers.get(0);
        driverFirefox = browsers.get(1);
        driverChrome.get(URL); 
        driverFirefox.get(URL);

        NAMESESSION = readVariablesFromExcel(testLocation, "OpenViduReactTest", "NAMESESSION");
        NAMEPARTICIPANT = readVariablesFromExcel(testLocation, "OpenViduReactTest", "NAMEPARTICIPANT");
        XpathJoinButton = readVariablesFromExcel(testLocation, "OpenViduReactTest", "XpathJoinButton");
        xpathOtherCamera = readVariablesFromExcel(testLocation, "OpenViduReactTest", "xpathOtherCamera");
        xpathParticipant = readVariablesFromExcel(testLocation, "OpenViduReactTest", "xpathParticipant");  
        idParticipant = readVariablesFromExcel(testLocation, "OpenViduReactTest", "idParticipant");
        idLeaveButton = readVariablesFromExcel(testLocation, "OpenViduReactTest", "idLeaveButton");
        idNameSession = readVariablesFromExcel(testLocation, "OpenViduReactTest", "idNameSession");
        idHeader = readVariablesFromExcel(testLocation, "OpenViduReactTest", "idHeader");
        idMainTitle = readVariablesFromExcel(testLocation, "OpenViduReactTest", "idMainTitle");
        idSelfCamera = readVariablesFromExcel(testLocation, "OpenViduReactTest", "idSelfCamera");
        idNameParticipant = readVariablesFromExcel(testLocation, "OpenViduReactTest", "idNameParticipant");
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

        TESTNAME = new Throwable().getStackTrace()[0].getMethodName();
        test = super.startTest(TESTNAME, "Join the session and verifies that the two browsers are inside the session", extentReports);

        addStepWithoutCapture(test, "INFO", "Starting test " + TESTNAME);

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
                addStep(test, "PASS", driverChrome, "The app is correctly inicializate in Chrome");    

            }else{
                addStep(test, "FAIL", driverChrome, "The header is empty in Chrome");    
                fail("General error");

            }
            if (!driverFirefox.findElements(By.id(idHeader)).isEmpty()){
                addStep(test, "PASS", driverFirefox, "The app is correctly inicializate in Firefox");    

            }else{
                addStep(test, "FAIL", driverFirefox, "The header is empty in Firefox");    
                fail("General error");

            }
        }catch (NoSuchElementException n){
            addStepWithoutCapture(test, "FAIL", "General error is occur");
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

        TESTNAME = new Throwable().getStackTrace()[0].getMethodName();
        test = super.startTest(TESTNAME, "Join the session and verifies that the two browsers are inside the session", extentReports);

        addStepWithoutCapture(test, "INFO", "Starting test " + TESTNAME);

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
            // see if the video is playing properly, moreover synchronize both videos
            WebDriverWait waitC = new WebDriverWait(driverChrome, Duration.ofSeconds(30));
            waitC.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOtherCamera)));
            
            WebDriverWait waitF = new WebDriverWait(driverFirefox, Duration.ofSeconds(30));
            waitF.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOtherCamera)));

            String currentTimeChrome = driverChrome.findElement(By.id(idSelfCamera)).getAttribute("currentTime");
            String currentTimeFirefox = driverFirefox.findElement(By.id(idSelfCamera)).getAttribute("currentTime");
          
            if (Float.parseFloat(currentTimeChrome) > 0 && Float.parseFloat(currentTimeFirefox) > 0){

                addStep(test, "INFO", driverChrome, "Session configurated in Chrome with session name: " + NAMESESSION);    
                addStep(test, "INFO", driverFirefox, "Session configurated in Firefox with session name: " + NAMESESSION); 
                
                WebElement leaveButtonC = driverChrome.findElement(By.id(idLeaveButton));
                if (leaveButtonC.isDisplayed()){ 
                    leaveButtonC.click();
                    addStep(test, "INFO", driverChrome, "Leave button was click in Chrome");    
                }else{
                    addStep(test, "FAIL", driverChrome, "Leave button in chrome is not display");    
                    fail("The app is not correctly leave");
                }
                
                WebElement titleC = driverChrome.findElement(By.id(idMainTitle));
                if(titleC.isDisplayed()){
                    addStep(test, "INFO", driverChrome, "Session correctly leave in Chrome");    
                }else{
                    addStep(test, "FAIL", driverChrome, "Session is not leave in Chrome");    
                    fail("Session is not leave in Chrome");
                }

                //Leave the session with Firefox

                WebElement leaveButtonF = driverFirefox.findElement(By.id(idLeaveButton));
                if (leaveButtonF.isDisplayed()){ 
                    leaveButtonF.click();
                    addStep(test, "INFO", driverFirefox, "Leave button was click i Firefox");    
                }else{
                    addStep(test, "FAIL", driverFirefox, "Leave button in Firefox is not display");    
                    fail("The app is not correctly leave");
                }

                WebElement titleF = driverFirefox.findElement(By.id(idMainTitle));
                if(titleF.isDisplayed()){
                    addStep(test, "INFO", driverFirefox, "Session correctly leave in Firefox");    
                }else{
                    addStep(test, "FAIL", driverFirefox, "Session is not leave in Firefox");    
                    fail("Session is not leave in Chrome");
                }

            } else {
                addStepWithoutCapture(test, "FAIL", "Video is not playing");
                fail("Video is not playing in app");
            }

         }catch (NoSuchElementException n){
            addStepWithoutCapture(test, "FAIL", "General error is occur");
            fail("The app is not correctly inicializate");
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

        TESTNAME = new Throwable().getStackTrace()[0].getMethodName();
        test = super.startTest(TESTNAME, "Joins the session and verifies that the session name is correct", extentReports);

        addStepWithoutCapture(test, "INFO", "Starting test " + TESTNAME);
        
        // Configurate the session in chrome
        WebElement textBox = driverChrome.findElement(By.id(idNameSession));
        textBox.clear();
        textBox.sendKeys(NAMESESSION);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();

        try{
            WebDriverWait waitC = new WebDriverWait(driverChrome, Duration.ofSeconds(30));
            waitC.until(ExpectedConditions.elementToBeClickable(By.id(idHeader)));

            if (!driverChrome.findElements(By.id(idHeader)).isEmpty()){
                
                if (NAMESESSION.equals(driverChrome.findElement(By.id(idHeader)).getText())){
                    addStep(test, "INFO", driverChrome, "The header text is correct: " + NAMESESSION);
                }else{
                    addStep(test, "FAIL", driverChrome, "The header it should be: " + NAMESESSION + "but is: " + driverChrome.findElement(By.id(idHeader)).getText());
                    fail("Test fail");
                }
            }else{
                addStep(test, "FAIL", driverChrome, "The header it should be: " + NAMESESSION + "but is blank");
                fail("Test fail");
            }
            addStep(test, "PASS", driverChrome, "TEST: " + TESTNAME +" ok: Session name is: " + NAMESESSION);
                
        }catch (NoSuchElementException n){

            addStepWithoutCapture(test, "FAIL", "General error is occur");
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
        TESTNAME = new Throwable().getStackTrace()[0].getMethodName();
        test = super.startTest(TESTNAME, "Join the session and verifies that the two browsers are inside the session", extentReports);

        addStepWithoutCapture(test, "INFO", "Starting test " + TESTNAME);

        // Configurate the session in chrome
        WebElement nameTextBox = driverChrome.findElement(By.id(idNameParticipant));
        nameTextBox.clear();
        nameTextBox.sendKeys(NAMEPARTICIPANT);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();

        try{
            WebDriverWait waitC = new WebDriverWait(driverChrome, Duration.ofSeconds(30));
            waitC.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathParticipant)));

            if (NAMEPARTICIPANT.equals(driverChrome.findElement(By.xpath(xpathParticipant)).getText())){
                addStep(test, "PASS", driverChrome, "TEST: " + TESTNAME +" ok: Participant name is: " + NAMEPARTICIPANT);
            }else{
                addStep(test, "FAIL", driverChrome, "Participant name is: " + driverChrome.findElement(By.xpath(xpathParticipant)).getText() + " but should be: " + NAMEPARTICIPANT);
            }
            
        }catch (NoSuchElementException n){
            addStepWithoutCapture(test, "FAIL", "General error is occur");
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

    /**
     * AfterAll.
     *
     * @author Andrea Acuña
     * Description: Close and generate the report
     */
    @AfterAll
    public static void tearDown() {
        extentReports.flush();
    }
}

