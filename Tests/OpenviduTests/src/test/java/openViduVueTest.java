import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import Reporter.ExtentManager;
import io.netty.handler.timeout.TimeoutException;

/**
 * Test for the java deployment of open vidu 
 * @author Andrea Acuña
 */
class OpenViduVueTest extends Module{

    String testLocation = "test-input/Parameters.xlsx";
    String reportLocation = "OpenViduVueTestTestReport.html";

    private ExtentTest test;
    public static ExtentReports extentReports;
    static ExtentManager e = new ExtentManager();

    WebDriver driverChrome;
    WebDriver driverFirefox;

    String URL;

    String nameSession;
    String NAMEPARTICIPANT;
    String TESTNAME;

    String XpathJoinButton;
    String xpathOtherCamera;
    String xpathSessionName;
    String XpathParticipant;
    String XpathParticipantName;

    String idLeaveButton;
    String idHeader;
    String idSelfCamera;
    String idHeaderStartPage;


    public OpenViduVueTest() {
        if (extentReports == null){
            extentReports = e.createExtentReports(reportLocation);
        }
    }

/**
 * BeforeEach
 *
 * @author Andrea Acuña
 * Description: Execute before every single test. 
 *              Configure the camera 
 *              Set de url in each browser
 *              Read the variables from excel file
 */
    @BeforeEach
    void setup() {

        URL = readVariablesFromExcel(testLocation, "OpenViduVueTest", "URL");

        List<WebDriver> browsers = super.setUpTwoBrowsers();
        driverChrome = browsers.get(0);
        driverFirefox = browsers.get(1);
        driverChrome.get(URL); 
        driverFirefox.get(URL);

        nameSession = readVariablesFromExcel(testLocation, "OpenViduVueTest", "NAMESESSION");
        NAMEPARTICIPANT = readVariablesFromExcel(testLocation, "OpenViduVueTest", "NAMEPARTICIPANT");
        XpathJoinButton = readVariablesFromExcel(testLocation, "OpenViduVueTest", "XpathJoinButton");
        xpathOtherCamera = readVariablesFromExcel(testLocation, "OpenViduVueTest", "xpathOtherCamera");
        xpathSessionName = readVariablesFromExcel(testLocation, "OpenViduVueTest", "xpathSessionName");
        XpathParticipant = readVariablesFromExcel(testLocation, "OpenViduVueTest", "xpathParticipant");
        XpathParticipantName = readVariablesFromExcel(testLocation, "OpenViduVueTest", "XpathParticipantName");
        idLeaveButton = readVariablesFromExcel(testLocation, "OpenViduVueTest", "idLeaveButton");
        idHeader = readVariablesFromExcel(testLocation, "OpenViduVueTest", "idHeader");
        idSelfCamera = readVariablesFromExcel(testLocation, "OpenViduVueTest", "idSelfCamera");
        idHeaderStartPage = readVariablesFromExcel(testLocation, "OpenViduVueTest", "idHeaderStartPage");
    }

 /**
 * Test with Java -> T001_JoinSession
 *
 * @author Andrea Acuña
 * Description: Join the session and verification that both browsers are inside the session
 */
    @Test
    void T001_JoinSession(){

        TESTNAME = new Throwable().getStackTrace()[0].getMethodName();
        test = e.startTest(TESTNAME, "Join the session and verifies that the two browsers are inside the session", extentReports);

        e.addStepWithoutCapture(test, "INFO", "Starting test " + TESTNAME);

        // Configurate the session in chrome
        WebElement textBox = driverChrome.findElement(By.xpath(xpathSessionName));
        textBox.clear();
        textBox.sendKeys(nameSession);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.click();
        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.xpath(xpathSessionName));
        textBoxF.clear();
        textBoxF.sendKeys(nameSession);
        WebElement joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton)); 
        joinButtonF.click();

        try{
            if (!driverChrome.findElements(By.id(idHeader)).isEmpty()){
                e.addStep(test, "PASS", driverChrome, "The app is correctly inicializate in Chrome");    

            }else{
                e.addStep(test, "FAIL", driverChrome, "The header is empty in Chrome");    
                fail("General error");

            }
            if (!driverFirefox.findElements(By.id(idHeader)).isEmpty()){
                e.addStep(test, "PASS", driverFirefox, "The app is correctly inicializate in Firefox");    

            }else{
                e.addStep(test, "FAIL", driverFirefox, "The header is empty in Firefox");    
                fail("General error");

            }
        }catch (TimeoutException n){
            
            e.addStep(test, "FAIL", driverChrome, "Error in chrome: " + n.getMessage());
            e.addStep(test, "FAIL", driverFirefox, "Error in firefox: " + n.getMessage());
            fail("The app is not correctly inicializate. There are a TimeoutException: " + n.getMessage());

        }catch (Exception ex) {
            
            e.addStep(test, "FAIL", driverChrome, "Error in chrome: " + ex.getMessage());
            e.addStep(test, "FAIL", driverFirefox, "Error in firefox: " + ex.getMessage());
            fail("An unexpected exception occurred: " + ex.getMessage());
        }
    }

/**
 * Test with Java -> T002_LeaveSession
 *
 * @author Andrea Acuña
 * Description: verification that the video is playing property and both browsers leaves the session correctly
 */
    @Test
    void T002_LeaveSession(){

        TESTNAME = new Throwable().getStackTrace()[0].getMethodName();
        test = e.startTest(TESTNAME, "Join the session and verifies that the two browsers are inside the session", extentReports);

        e.addStepWithoutCapture(test, "INFO", "Starting test " + TESTNAME);

        WebDriverWait waitC = new WebDriverWait(driverChrome, Duration.ofSeconds(30));
        WebDriverWait waitF = new WebDriverWait(driverFirefox, Duration.ofSeconds(30));
        int repeat = 0;
        // Configurate the session in chrome
        WebElement textBox = driverChrome.findElement(By.xpath(xpathSessionName));
        textBox.clear();
        textBox.sendKeys(nameSession);
        WebElement joinButtonC = null;
        while(repeat <= 5){
            try{
                joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton));
                joinButtonC.click();
                break;
            }catch(StaleElementReferenceException exc){
                exc.printStackTrace();
            }
            repeat++;
        }

        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.xpath(xpathSessionName));
        textBoxF.clear();
        textBoxF.sendKeys(nameSession);
        repeat = 0;
        WebElement joinButtonF = null;
        while(repeat <= 5){
            try{
                joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton));
                //waitC.until(ExpectedConditions.stalenessOf(joinButtonF));
                joinButtonF.click();
                break;
            }catch(StaleElementReferenceException exc){
                exc.printStackTrace();
            }
            repeat++;
        }
        try{
            // see if the video is playing properly, moreover synchronize both videos
            waitC.until(ExpectedConditions.visibilityOfElementLocated(By.id(idSelfCamera)));
            waitC.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOtherCamera)));
            
            waitF.until(ExpectedConditions.visibilityOfElementLocated(By.id(idSelfCamera)));
            waitF.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOtherCamera)));

            String SelfCurrentTimeChrome = driverChrome.findElement(By.id(idSelfCamera)).getAttribute("currentTime");
            String SelfCurrentTimeFirefox = driverFirefox.findElement(By.id(idSelfCamera)).getAttribute("currentTime");

            String OtherCurrentTimeChrome = driverChrome.findElement(By.xpath(xpathOtherCamera)).getAttribute("currentTime");
            String OtherCurrentTimeFirefox = driverFirefox.findElement(By.xpath(xpathOtherCamera)).getAttribute("currentTime");

            if (Float.parseFloat(SelfCurrentTimeChrome) > 0 && Float.parseFloat(SelfCurrentTimeFirefox) > 0){
                if (Float.parseFloat(OtherCurrentTimeChrome) > 0 && Float.parseFloat(OtherCurrentTimeFirefox) > 0){        
                    
                    e.addStep(test, "INFO", driverChrome, "Videos playing ok in Chrome: Self video is: " + SelfCurrentTimeChrome + " Seconds and Other video is: " + OtherCurrentTimeChrome + " Seconds");    
                    e.addStep(test, "INFO", driverFirefox, "Videos playing ok in Firefox: Self video is: " + SelfCurrentTimeFirefox + " Seconds and Other video is: " + OtherCurrentTimeFirefox + " Seconds"); 
                    
                    //Leave the session with chrome
                    WebElement leaveButtonC = driverChrome.findElement(By.id(idLeaveButton));
                    if (leaveButtonC.isDisplayed()){ 
                        leaveButtonC.click();
                        e.addStep(test, "INFO", driverChrome, "Leave button was click in chrome");    
                    }else{
                        e.addStep(test, "FAIL", driverChrome, "Leave button in chrome is not display");    
                        fail("The app is not correctly leave");
                    }

                    WebElement headerC = driverChrome.findElement(By.id(idHeaderStartPage));
                    if(headerC.isDisplayed()){
                        e.addStep(test, "INFO", driverChrome, "Session correctly leave in Chrome");    
                    }else{
                        e.addStep(test, "FAIL", driverChrome, "Session is not leave in Chrome");    
                        fail("Session is not leave in Chrome");
                    }

                    //Leave the session with Firefox

                    WebElement leaveButtonF = driverFirefox.findElement(By.id(idLeaveButton));
                    if (leaveButtonF.isDisplayed()){ 
                        leaveButtonF.click();
                        e.addStep(test, "INFO", driverFirefox, "Leave button was click i Firefox");    
                    }else{
                        e.addStep(test, "FAIL", driverFirefox, "Leave button in Firefox is not display");    
                        fail("The app is not correctly leave");
                    }

                    WebElement headerF = driverFirefox.findElement(By.id(idHeaderStartPage));
                    if(headerF.isDisplayed()){
                        e.addStep(test, "INFO", driverFirefox, "Session correctly leave in Firefox");    
                    }else{
                        e.addStep(test, "FAIL", driverFirefox, "Session is not leave in Firefox");    
                        fail("Session is not leave in Chrome");
                    }
                }else{
                    if (Float.parseFloat(OtherCurrentTimeChrome) > 0){
                        e.addStep(test, "FAIL", driverFirefox, "Other Video is not playing in Firefox");
                    }else{
                        e.addStep(test, "FAIL", driverChrome, "Other Video is not playing in Chrome");
                    }
                    fail("Self Video is not playing in app");
                }
            }else{
                if (Float.parseFloat(SelfCurrentTimeChrome) > 0){
                    e.addStep(test, "FAIL", driverFirefox, "Self Video is not playing in Firefox");
                }else{
                    e.addStep(test, "FAIL", driverChrome, "Self Video is not playing in Chrome");
                }
                fail("Self Video is not playing in app");
            }

        }catch (TimeoutException n){
            
            e.addStep(test, "FAIL", driverChrome, "Error in chrome: " + n.getMessage());
            e.addStep(test, "FAIL", driverFirefox, "Error in firefox: " + n.getMessage());
            fail("The app is not correctly inicializate. There are a TimeoutException: " + n.getMessage());

        }catch (Exception ex) {
            
            e.addStep(test, "FAIL", driverChrome, "Error in chrome: " + ex.getMessage());
            e.addStep(test, "FAIL", driverFirefox, "Error in firefox: " + ex.getMessage());
            fail("An unexpected exception occurred: " + ex.getMessage());
        }
        
    }

/**
 * Test with Java -> T003_SessionHeader
 *
 * @author Andrea Acuña
 * Description: Joins the session and verifies that the session name is the expected
 */
    @Test
    void T003_SessionHeader(){
        TESTNAME = new Throwable().getStackTrace()[0].getMethodName();
        test = e.startTest(TESTNAME, "Joins the session and verifies that the session name is correct", extentReports);

        e.addStepWithoutCapture(test, "INFO", "Starting test " + TESTNAME);
        
        // Configurate the session in chrome
        WebElement textBox = driverChrome.findElement(By.xpath(xpathSessionName));
        textBox.clear();
        textBox.sendKeys(nameSession);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.click();

        try{
            WebDriverWait waitC = new WebDriverWait(driverChrome, Duration.ofSeconds(30));
            waitC.until(ExpectedConditions.elementToBeClickable(By.id(idHeader)));

            if (!driverChrome.findElements(By.id(idHeader)).isEmpty()){
                
                if (nameSession.equals(driverChrome.findElement(By.id(idHeader)).getText())){
                    e.addStep(test, "INFO", driverChrome, "The header text is correct: " + nameSession);
                }else{
                    e.addStep(test, "FAIL", driverChrome, "The header it should be: " + nameSession + "but is: " + driverChrome.findElement(By.id(idHeader)).getText());
                    fail("Test fail");
                }
            }else{
                e.addStep(test, "FAIL", driverChrome, "The header it should be: " + nameSession + "but is blank");
                fail("Test fail");
            }
            e.addStep(test, "PASS", driverChrome, "TEST: " + TESTNAME +" ok: Session name is: " + nameSession);
                
        }catch (TimeoutException n){
            
            e.addStep(test, "FAIL", driverChrome, "Error in chrome: " + n.getMessage());
            e.addStep(test, "FAIL", driverFirefox, "Error in firefox: " + n.getMessage());
            fail("The app is not correctly inicializate. There are a TimeoutException: " + n.getMessage());

        }catch (Exception ex) {
            
            e.addStep(test, "FAIL", driverChrome, "Error in chrome: " + ex.getMessage());
            e.addStep(test, "FAIL", driverFirefox, "Error in firefox: " + ex.getMessage());
            fail("An unexpected exception occurred: " + ex.getMessage());
        }
    }

/**
 * Test with Java -> T004_ParticipantName
 *
 * @author Andrea Acuña
 * Description: Joins the session and verifies that the chrome participant name is correct
 */
    @Test
        void T004_ParticipantName(){

        TESTNAME = new Throwable().getStackTrace()[0].getMethodName();
        test = e.startTest(TESTNAME, "Join the session and verifies that the two browsers are inside the session", extentReports);

        e.addStepWithoutCapture(test, "INFO", "Starting test " + TESTNAME);
        // Configurate the session in chrome
        WebElement nameTextBox = driverChrome.findElement(By.xpath(XpathParticipantName));
        nameTextBox.clear();
        nameTextBox.sendKeys(NAMEPARTICIPANT);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.click();
        driverChrome.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        try{
            WebDriverWait waitC = new WebDriverWait(driverChrome, Duration.ofSeconds(30));
            int repeat = 0;
            String ParticipantNameText = "";
            waitC.until(ExpectedConditions.visibilityOfElementLocated(By.id(idSelfCamera)));
            while(repeat <= 5){
                try{
                    WebElement ParticipantName = driverChrome.findElement(By.xpath(XpathParticipant));
                    ParticipantNameText = ParticipantName.getText();
                    break;
                }catch(StaleElementReferenceException exc){
                    exc.printStackTrace();
                }
                repeat++;
            }
            assertEquals(ParticipantNameText, NAMEPARTICIPANT);
            e.addStep(test, "PASS", driverChrome, "The header name participant is correct: " + NAMEPARTICIPANT);

        }catch (TimeoutException n){
            
            e.addStep(test, "FAIL", driverChrome, "Error in chrome: " + n.getMessage());
            fail("The app is not correctly inicializate. There are a TimeoutException: " + n.getMessage());

        }catch (Exception ex) {
            
            e.addStep(test, "FAIL", driverChrome, "Error in chrome: " + ex.getMessage());
            fail("An unexpected exception occurred: " + ex.getMessage());
        }
    }

/**
* AfterEach.
*
* @author Andrea Acuña
* Description: close both drivers correctly after every single test
*/
    @AfterEach
    void quit() {
        super.quitTwoBrowsers(driverChrome, driverFirefox);
    }

/**
* AfterAll.
*
* @author Andrea Acuña
* Description: Close and generate the report after execution of all tests
*/
    @AfterAll
    public static void tearDown() {
        e.tearDownExtent(extentReports);
    }

}
