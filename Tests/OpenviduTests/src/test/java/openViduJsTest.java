import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.List;

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

import Reporter.ExtentManager;
import io.netty.handler.timeout.TimeoutException;

/**
 * Test with Java.
 * Test for the java deployment of open vidu 
 * @author Andrea Acuña
 */
class OpenViduJsTest extends Module{

    String testLocation = "test-input/Parameters.xlsx";
    String reportLocation = "OpenViduJsTestTestReport.html";

    String[] readyStateValues = {
        "HAVE_NOTHING",
        "HAVE_METADATA",
        "HAVE_CURRENT_DATA",
        "HAVE_FUTURE_DATA",
        "HAVE_ENOUGH_DATA"
    };

    private ExtentTest test;
    public static ExtentReports extentReports;
    static ExtentManager e = new ExtentManager();

    WebDriver driverChrome;
    WebDriver driverFirefox;

    String URL;

    String nameSession;
    String TESTNAME;

    String XpathJoinButton;
    String idLeaveButton;
    String xpathOtherCamera;

    String idHeader;
    String idNameSession;
    String idSelfCamera;

    public OpenViduJsTest() {
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
        URL = readVariablesFromExcel(testLocation, "OpenViduJsTest", "URL");
        List<WebDriver> browsers = super.setUpTwoBrowsers();
        driverChrome = browsers.get(0);
        driverFirefox = browsers.get(1);
        driverChrome.get(URL); 
        driverFirefox.get(URL);

        nameSession = readVariablesFromExcel(testLocation, "OpenViduJsTest", "NAMESESSION");
        XpathJoinButton = readVariablesFromExcel(testLocation, "OpenViduJsTest", "XpathJoinButton");
        idLeaveButton = readVariablesFromExcel(testLocation, "OpenViduJsTest", "idLeaveButton");
        xpathOtherCamera = readVariablesFromExcel(testLocation, "OpenViduJsTest", "xpathOtherCamera");
        idHeader = readVariablesFromExcel(testLocation, "OpenViduJsTest", "idHeader");
        idNameSession = readVariablesFromExcel(testLocation, "OpenViduJsTest", "idNameSession");
        idSelfCamera = readVariablesFromExcel(testLocation, "OpenViduJsTest", "idSelfCamera");
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
        WebElement textBox = driverChrome.findElement(By.id(idNameSession));
        textBox.clear();
        textBox.sendKeys(nameSession);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();
        e.addStep(test, "INFO", driverChrome, "Session configurated in Chrome with session name: " + nameSession);    

        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.id(idNameSession));
        textBoxF.clear();
        textBoxF.sendKeys(nameSession);
        WebElement joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton)); 
        joinButtonF.submit();
        e.addStep(test, "INFO", driverFirefox, "Session configurated in Firefox with session name: " + nameSession);    

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
        test = e.startTest(TESTNAME, "Join the session, verifies that the video is playing property and leave the session", extentReports);

        e.addStepWithoutCapture(test, "INFO", "Starting test " + TESTNAME);

        // Configurate the session in chrome
        WebElement textBox = driverChrome.findElement(By.id(idNameSession));
        textBox.clear();
        textBox.sendKeys(nameSession);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.click();
        
        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.id(idNameSession));
        textBoxF.clear();
        textBoxF.sendKeys(nameSession);
        WebElement joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton)); 
        joinButtonF.click();
        
        try{
            // see if the video is playing properly, moreover synchronize both videos
            WebDriverWait waitC = new WebDriverWait(driverChrome, Duration.ofSeconds(30));
            waitC.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOtherCamera)));
            
            WebDriverWait waitF = new WebDriverWait(driverFirefox, Duration.ofSeconds(30));
            waitF.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOtherCamera)));

            driverChrome.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driverFirefox.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            // see if the video is playing properly
            String SelfCurrentTimeChrome = driverChrome.findElement(By.id(idSelfCamera)).getAttribute("readyState");
            String SelfCurrentTimeFirefox = driverFirefox.findElement(By.id(idSelfCamera)).getAttribute("readyState");

            if (Integer.parseInt(SelfCurrentTimeChrome) >= 3 && Integer.parseInt(SelfCurrentTimeChrome) < readyStateValues.length) {
                e.addStep(test, "INFO", driverChrome, "Self video is correctly playing in Chrome: " + readyStateValues[Integer.parseInt(SelfCurrentTimeChrome)]);    
            } else {
                e.addStep(test, "FAIL", driverChrome, "Self video is NOT correctly playing in Chrome: " + readyStateValues[Integer.parseInt(SelfCurrentTimeChrome)]);    
            }

            if (Integer.parseInt(SelfCurrentTimeFirefox) >= 3 && Integer.parseInt(SelfCurrentTimeFirefox) < readyStateValues.length) {
                e.addStep(test, "INFO", driverFirefox, "Self video is correctly playing in Firefox: " + readyStateValues[Integer.parseInt(SelfCurrentTimeFirefox)]);    
             } else {
                e.addStep(test, "FAIL", driverFirefox, "Self video is NOT correctly playing in Firefox: " + readyStateValues[Integer.parseInt(SelfCurrentTimeFirefox)]);    
            }
            
            waitC.until(ExpectedConditions.visibilityOfElementLocated(By.id(idSelfCamera)));
            waitF.until(ExpectedConditions.visibilityOfElementLocated(By.id(idSelfCamera)));

            String OtherCurrentTimeChrome = driverChrome.findElement(By.xpath(xpathOtherCamera)).getAttribute("readyState");
            String OtherCurrentTimeFirefox = driverFirefox.findElement(By.xpath(xpathOtherCamera)).getAttribute("readyState");

            if (Integer.parseInt(OtherCurrentTimeChrome) >= 3 && Integer.parseInt(OtherCurrentTimeChrome) < readyStateValues.length) {
                e.addStep(test, "INFO", driverChrome, "Other video is correctly playing in Chrome: " + readyStateValues[Integer.parseInt(OtherCurrentTimeChrome)]);    
            } else {
                e.addStep(test, "FAIL", driverChrome, "Other video is NOT correctly playing in Chrome: " + readyStateValues[Integer.parseInt(SelfCurrentTimeChrome)]);    
            }

            if (Integer.parseInt(OtherCurrentTimeFirefox) >= 3 && Integer.parseInt(OtherCurrentTimeFirefox) < readyStateValues.length) {
                e.addStep(test, "INFO", driverFirefox, "Other video is correctly playing in Firefox: " + readyStateValues[Integer.parseInt(OtherCurrentTimeFirefox)]);    
             } else {
                e.addStep(test, "FAIL", driverFirefox, "Other video is NOT correctly playing in Firefox: " + readyStateValues[Integer.parseInt(OtherCurrentTimeFirefox)]);    
            }

            //Leave the session with chrome
            WebElement leaveButtonC = driverChrome.findElement(By.id(idLeaveButton));
            if (leaveButtonC.isDisplayed()){ 
                leaveButtonC.click();
                e.addStep(test, "INFO", driverChrome, "Leave button was click");    
            }else{
                e.addStep(test, "FAIL", driverChrome, "Leave button in chrome is not display");    
                fail("The app is not correctly leave");
            }

            if(joinButtonC.isDisplayed()){
                e.addStep(test, "INFO", driverChrome, "The app leave the session correctly in Chrome");    
                
            }else{
                e.addStep(test, "FAIL", driverChrome, "Join button in chrome is not display");    
                fail("The app is not correctly leave");
            }

            //Leave the session with Firefox
            WebElement leaveButtonF = driverFirefox.findElement(By.id(idLeaveButton));
            if (leaveButtonF.isDisplayed()){ 
                leaveButtonF.click();
                e.addStep(test, "INFO", driverFirefox, "Leave button was click"); 
            }else{
                e.addStep(test, "FAIL", driverFirefox, "Leave button in firefox is not display");    
                fail("The app is not correctly leave");
            }

            if(joinButtonF.isDisplayed()){
                e.addStep(test, "INFO", driverFirefox, "The app leave the session correctly in Firefox");    
            
            }else{
                e.addStep(test, "FAIL", driverFirefox, "Join button in chrome is not display");    
                fail("The app is not correctly leave");
            }
            
             e.addStep(test, "PASS", driverChrome, "TEST: " + TESTNAME +" ok: Session correctly leave in both drivers");
        
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
        WebElement textBox = driverChrome.findElement(By.id(idNameSession));
        textBox.clear();
        textBox.sendKeys(nameSession);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();

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
* AfterEach.
*
* @author Andrea Acuña
* Description: close both drivers correctly after every single test
*/
    @AfterEach
    void quit(){
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
