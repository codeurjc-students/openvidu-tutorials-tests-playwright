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
 * Test for the hello word for open vidu 
 * @author Andrea Acuña
 */

class OpenViduHelloWordTest extends Module{

    String testLocation = "test-input/Parameters.xlsx";
    String reportLocation = "OpenViduHelloWordTestTestReport.html";

    private ExtentTest test;
    public static ExtentReports extentReports;
    static ExtentManager e = new ExtentManager();

    WebDriver driverChrome;
    WebDriver driverFirefox;

    String URL;

    String NAMESESSION;
    String TESTNAME;

    String XpathJoinButton;
    String xpathLeaveButton;
    String xpathOtherCamera;

    String idHeader;
    String idNameSession;
    String idSelfCamera;

    public OpenViduHelloWordTest() {
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
        URL = readVariablesFromExcel(testLocation, "OpenViduHelloWordTest", "URL");
        List<WebDriver> browsers = super.setUpTwoBrowsers();
        driverChrome = browsers.get(0);
        driverFirefox = browsers.get(1);
        driverChrome.get(URL); 
        driverFirefox.get(URL);

        NAMESESSION = readVariablesFromExcel(testLocation, "OpenViduHelloWordTest", "NAMESESSION");
        TESTNAME = readVariablesFromExcel(testLocation, "OpenViduHelloWordTest", "TESTNAME");
        XpathJoinButton = readVariablesFromExcel(testLocation, "OpenViduHelloWordTest", "XpathJoinButton");
        xpathLeaveButton = readVariablesFromExcel(testLocation, "OpenViduHelloWordTest", "xpathLeaveButton");
        xpathOtherCamera = readVariablesFromExcel(testLocation, "OpenViduHelloWordTest", "xpathOtherCamera");
        idHeader = readVariablesFromExcel(testLocation, "OpenViduHelloWordTest", "idHeader");
        idNameSession = readVariablesFromExcel(testLocation, "OpenViduHelloWordTest", "idNameSession");
        idSelfCamera = readVariablesFromExcel(testLocation, "OpenViduHelloWordTest", "idSelfCamera");

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
        WebElement textBox = driverChrome.findElement(By.id(idNameSession));
        textBox.clear();
        textBox.sendKeys(NAMESESSION);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();
        e.addStep(test, "INFO", driverChrome, "Session configurated in Chrome with session name: " + NAMESESSION); 

        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.id(idNameSession));
        textBoxF.clear();
        textBoxF.sendKeys(NAMESESSION);
        WebElement joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton)); 
        joinButtonF.submit();
        e.addStep(test, "INFO", driverFirefox, "Session configurated in Firefox with session name: " + NAMESESSION);

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
 * Description: Leave the session, verficate that the video is playing property and leave the session
 * @throws IOException
 */
    @Test
    void T002_LeaveSession() throws IOException{

        TESTNAME = new Throwable().getStackTrace()[0].getMethodName();
        test = e.startTest(TESTNAME, "Join the session, verifies that the video is playing property and leave the session", extentReports);

        e.addStepWithoutCapture(test, "INFO", "Starting test " + TESTNAME);

        // Configurate the session in chrome
        WebElement textBox = driverChrome.findElement(By.id(idNameSession));
        textBox.clear();
        textBox.sendKeys(NAMESESSION);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();

        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.id(idNameSession));
        textBoxF.clear();
        textBoxF.sendKeys(NAMESESSION);
        WebElement joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton)); 
        joinButtonF.submit();

        // see if the video is playing properly, moreover synchronize both videos
        WebDriverWait waitC = new WebDriverWait(driverChrome, Duration.ofSeconds(30));
        waitC.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOtherCamera)));
        
        WebDriverWait waitF = new WebDriverWait(driverFirefox, Duration.ofSeconds(50));
        waitF.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOtherCamera)));

        // see if the video is playing properly
        int repeat = 0;
        String currentTimeChrome = "";
        String currentTimeFirefox = "";
        
        while (repeat <= 5){
            try{
                currentTimeChrome = driverChrome.findElement(By.id(idSelfCamera)).getAttribute("currentTime");
                break;
            }catch(StaleElementReferenceException exc){
                e.addStepWithoutCapture(test, "FAIL", "General error is occur: " + exc.getMessage());
                fail("The app is not correctly inicializate");
            }
            repeat++;
        }
        repeat = 0;
        while (repeat <= 5){
            try{
                currentTimeFirefox = driverFirefox.findElement(By.id(idSelfCamera)).getAttribute("currentTime");
                break;
            }catch(StaleElementReferenceException exc){
                e.addStepWithoutCapture(test, "FAIL", "General error is occur: " + exc.getMessage());
                fail("The app is not correctly inicializate");
            }
            repeat++;
        } 
           
        try{
            if (Float.parseFloat(currentTimeChrome) > 0 && Float.parseFloat(currentTimeFirefox) > 0){
                
                e.addStep(test, "INFO", driverChrome, "Session configurated in Chrome with session name: " + NAMESESSION);    
                e.addStep(test, "INFO", driverFirefox, "Session configurated in Firefox with session name: " + NAMESESSION);    
         
                WebElement leaveButtonC = driverChrome.findElement(By.xpath(xpathLeaveButton));
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
                WebElement leaveButtonF = driverFirefox.findElement(By.xpath(xpathLeaveButton));
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

            }else{
                e.addStep(test, "FAIL", driverChrome, "Video is not playing propertly");    
                fail("Video is not playing propertly");
            }

            e.addStep(test, "PASS", driverChrome, "TEST: " + TESTNAME +" ok: Session correctly leave in both drivers");

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
 * AfterEach.
 *
 * @author Andrea Acuña
 * Description: close both drivers
 */
    @AfterEach
    void quit(){
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