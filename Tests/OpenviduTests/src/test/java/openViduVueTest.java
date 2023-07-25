import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import Reporter.ExtentManager;

/**
 * Test with Java.
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

    String NAMESESSION;
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
     * BeforeEach.
     *
     * @author Andrea Acuña
     * Description: Execute before every single test. Configure the camera an set de url in each browser
     */
    @BeforeEach
    void setup() {

        URL = readVariablesFromExcel(testLocation, "OpenViduVueTest", "URL");

        List<WebDriver> browsers = super.setUpTwoBrowsers();
        driverChrome = browsers.get(0);
        driverFirefox = browsers.get(1);
        driverChrome.get(URL); 
        driverFirefox.get(URL);

        NAMESESSION = readVariablesFromExcel(testLocation, "OpenViduVueTest", "NAMESESSION");
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
 * Test with Java.
 *
 * @author Andrea Acuña
 * Description: Join the session and verificate that the two browsers are inside the session
 * @throws IOException
 */
    @Test
    void T001_JoinSession() throws IOException {

        TESTNAME = new Throwable().getStackTrace()[0].getMethodName();
        test = e.startTest(TESTNAME, "Join the session and verifies that the two browsers are inside the session", extentReports);

        e.addStepWithoutCapture(test, "INFO", "Starting test " + TESTNAME);

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
        }catch (NoSuchElementException n){
            
            e.addStepWithoutCapture(test, "FAIL", "General error is occur");
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
    void T002_LeaveSession() throws IOException{

        TESTNAME = new Throwable().getStackTrace()[0].getMethodName();
        test = e.startTest(TESTNAME, "Join the session and verifies that the two browsers are inside the session", extentReports);

        e.addStepWithoutCapture(test, "INFO", "Starting test " + TESTNAME);

        WebDriverWait waitC = new WebDriverWait(driverChrome, Duration.ofSeconds(30));
        WebDriverWait waitF = new WebDriverWait(driverFirefox, Duration.ofSeconds(30));
        int repeat = 0;
        // Configurate the session in chrome
        WebElement textBox = driverChrome.findElement(By.xpath(xpathSessionName));
        textBox.clear();
        textBox.sendKeys(NAMESESSION);
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
        textBoxF.sendKeys(NAMESESSION);
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
            waitC.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathOtherCamera)));
            waitF.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathOtherCamera)));

            // see if the video is playing properly
            String currentTimeChrome = driverChrome.findElement(By.id(idSelfCamera)).getAttribute("currentTime");
            String currentTimeFirefox = driverFirefox.findElement(By.id(idSelfCamera)).getAttribute("currentTime");

            if (Float.parseFloat(currentTimeChrome) > 0 && Float.parseFloat(currentTimeFirefox) > 0){
                    //Leave the session with chrome

                e.addStep(test, "INFO", driverChrome, "Session configurated in Chrome with session name: " + NAMESESSION);    
                e.addStep(test, "INFO", driverFirefox, "Session configurated in Firefox with session name: " + NAMESESSION);    
                
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
                e.addStepWithoutCapture(test, "FAIL", "Video is not playing correctly");
                fail("Video is not playing correctly");
            }

        }catch (NoSuchElementException n){
            e.addStepWithoutCapture(test, "FAIL", "General error is occur");
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
        test = e.startTest(TESTNAME, "Joins the session and verifies that the session name is correct", extentReports);

        e.addStepWithoutCapture(test, "INFO", "Starting test " + TESTNAME);
        
        // Configurate the session in chrome
        WebElement textBox = driverChrome.findElement(By.xpath(xpathSessionName));
        textBox.clear();
        textBox.sendKeys(NAMESESSION);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.click();

        try{
            WebDriverWait waitC = new WebDriverWait(driverChrome, Duration.ofSeconds(30));
            waitC.until(ExpectedConditions.elementToBeClickable(By.id(idHeader)));

            if (!driverChrome.findElements(By.id(idHeader)).isEmpty()){
                
                if (NAMESESSION.equals(driverChrome.findElement(By.id(idHeader)).getText())){
                    e.addStep(test, "INFO", driverChrome, "The header text is correct: " + NAMESESSION);
                }else{
                    e.addStep(test, "FAIL", driverChrome, "The header it should be: " + NAMESESSION + "but is: " + driverChrome.findElement(By.id(idHeader)).getText());
                    fail("Test fail");
                }
            }else{
                e.addStep(test, "FAIL", driverChrome, "The header it should be: " + NAMESESSION + "but is blank");
                fail("Test fail");
            }
            e.addStep(test, "PASS", driverChrome, "TEST: " + TESTNAME +" ok: Session name is: " + NAMESESSION);
                
        }catch (NoSuchElementException n){

            e.addStepWithoutCapture(test, "FAIL", "General error is occur");
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
        test = e.startTest(TESTNAME, "Join the session and verifies that the two browsers are inside the session", extentReports);

        e.addStepWithoutCapture(test, "INFO", "Starting test " + TESTNAME);
        // Configurate the session in chrome
        WebElement nameTextBox = driverChrome.findElement(By.xpath(XpathParticipantName));
        nameTextBox.clear();
        nameTextBox.sendKeys(NAMEPARTICIPANT);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.click();

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

        }catch (NoSuchElementException n){
            e.addStepWithoutCapture(test, "FAIL", "General error is occur");
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
        e.tearDownExtent(extentReports);
    }

}
