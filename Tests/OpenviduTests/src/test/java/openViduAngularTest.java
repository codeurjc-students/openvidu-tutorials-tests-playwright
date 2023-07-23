import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
class OpenViduAngularTest extends Module{

    String testLocation = "test-input/Parameters.xlsx";
    String reportLocation = "OpenViduAngularTestReport.html";

    private ExtentTest test;
    public static ExtentReports extentReports;

    WebDriver driverChrome;
    WebDriver driverFirefox;

    String URL;

    String NAMESESSION;
    String NAMEPARTICIPANT;
    String TESTNAME;

    String XpathJoinButton;
    String xpathHeader;
    String xpathOtherCamera;
    
    String xpathParticipant;

    String idParticipant;
    String idLeaveButton;
    String idNameSession;
    String idHeader;
    String idSelfCamera;
    String idNameParticipant;


    public OpenViduAngularTest() {
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
        URL = readVariablesFromExcel(testLocation, "OpenViduAngularTest", "URL");
        List<WebDriver> browsers = super.setUpTwoBrowsers();
        driverChrome = browsers.get(0);
        driverFirefox = browsers.get(1);
        driverChrome.get(URL); 
        driverFirefox.get(URL);

        NAMESESSION = readVariablesFromExcel(testLocation, "OpenViduAngularTest", "NAMESESSION");
        NAMEPARTICIPANT = readVariablesFromExcel(testLocation, "OpenViduAngularTest", "NAMEPARTICIPANT");
        XpathJoinButton = readVariablesFromExcel(testLocation, "OpenViduAngularTest", "XpathJoinButton");
        xpathHeader = readVariablesFromExcel(testLocation, "OpenViduAngularTest", "xpathHeader");
        xpathOtherCamera = readVariablesFromExcel(testLocation, "OpenViduAngularTest", "xpathOtherCamera");
        xpathParticipant = readVariablesFromExcel(testLocation, "OpenViduAngularTest", "xpathParticipant");
        idParticipant = readVariablesFromExcel(testLocation, "OpenViduAngularTest", "idParticipant");
        idLeaveButton = readVariablesFromExcel(testLocation, "OpenViduAngularTest", "idLeaveButton");
        idNameSession = readVariablesFromExcel(testLocation, "OpenViduAngularTest", "idNameSession");
        idHeader = readVariablesFromExcel(testLocation, "OpenViduAngularTest", "NAMEidHeaderSESSION");
        idSelfCamera = readVariablesFromExcel(testLocation, "OpenViduAngularTest", "idSelfCamera");
        idNameParticipant = readVariablesFromExcel(testLocation, "OpenViduAngularTest", "idNameParticipant");
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
        addStep(test, "INFO", driverChrome, "Session configurated in Chrome with session name: " + NAMESESSION);    


        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.id(idNameSession));
        textBoxF.clear();
        textBoxF.sendKeys(NAMESESSION);
        WebElement participantF = driverFirefox.findElement(By.id(idParticipant));
        participantF.clear();
        participantF.sendKeys("PARTICIPANTFIREFOX");
        WebElement joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton)); 
        joinButtonF.submit();
        addStep(test, "INFO", driverFirefox, "Session configurated in Firefox with session name: " + NAMESESSION);    

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
            waitC.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathOtherCamera)));
            
            WebDriverWait waitF = new WebDriverWait(driverFirefox, Duration.ofSeconds(30));
            waitF.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathOtherCamera)));

            driverChrome.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driverFirefox.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            // see if the video is playing properly
            String currentTimeChrome= driverChrome.findElement(By.id(idSelfCamera)).getAttribute("duration");
            String currentTimeFirefox = driverFirefox.findElement(By.id(idSelfCamera)).getAttribute("duration");
              
            assertNotEquals(currentTimeChrome, "NaN");
            assertNotEquals(currentTimeFirefox, "NaN");

            addStep(test, "INFO", driverChrome, "Session configurated in Chrome with session name: " + NAMESESSION);    
            addStep(test, "INFO", driverFirefox, "Session configurated in Firefox with session name: " + NAMESESSION);    
            
            //Leave the session with chrome
            WebElement leaveButtonC = driverChrome.findElement(By.id(idLeaveButton));
            if (leaveButtonC.isDisplayed()){ 
                leaveButtonC.click();
                addStep(test, "INFO", driverChrome, "Leave button was click");    
            }else{
                addStep(test, "FAIL", driverChrome, "Leave button in chrome is not display");    
                fail("The app is not correctly leave");
            }

            if(joinButtonC.isDisplayed()){
                addStep(test, "INFO", driverChrome, "The app leave the session correctly in Chrome");    
                
            }else{
                addStep(test, "FAIL", driverChrome, "Join button in chrome is not display");    
                fail("The app is not correctly leave");
            }

            //Leave the session with Firefox
            WebElement leaveButtonF = driverFirefox.findElement(By.id(idLeaveButton));
            if (leaveButtonF.isDisplayed()){ 
                leaveButtonF.click();
                addStep(test, "INFO", driverFirefox, "Leave button was click"); 
            }else{
                addStep(test, "FAIL", driverChrome, "Leave button in firefox is not display");    
                fail("The app is not correctly leave");
            }

            if(joinButtonF.isDisplayed()){
                addStep(test, "INFO", driverChrome, "The app leave the session correctly in Firefox");    
            
            }else{
                addStep(test, "FAIL", driverChrome, "Join button in chrome is not display");    
                fail("The app is not correctly leave");
            }
            
             addStep(test, "PASS", driverChrome, "TEST: " + TESTNAME +" ok: Session correctly leave in both drivers");
        
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
        test = super.startTest(TESTNAME, "Join the session and verifies that the two browsers are inside the session", extentReports);

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
