import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

/**
 * Test with Java.
 * Test for the java deployment of open vidu 
 * @author Andrea Acuña
 */
class OpenViduJsTest extends Module{

    String evidencesFolder = "..\\..\\evidence";

    private ExtentTest test;
    private ExtentReports extentReports;

    WebDriver driverChrome;
    WebDriver driverFirefox;

    String URL = "http://localhost:8080/";

    String NAMESESSION = "TestSession";
    String TESTNAME = "";

    String XpathJoinButton = "//*[@id='join-dialog']/form/p[3]/input";
    String idLeaveButton = "buttonLeaveSession";
    String xpathOtherCamera = "/html/body/div/div[2]/div[3]/video[1]";

    String idHeader = "session-header";
    String idNameSession = "sessionId";
    String idSelfCamera = "local-video-undefined";

/**
 * BeforeTest.
 *
 * @author Andrea Acuña
 * Description: Execute before the grouf of tests. Configure the reporter
 */
@BeforeTest
void setupReporter() {
    extentReports = super.createExtentReports();
}

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

        TESTNAME = Thread.currentThread().getStackTrace()[2].getMethodName();
        test = super.startTest(TESTNAME, "");
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
        test.log(Status.INFO, "Starting test");
        // test.log(Status.PASS, "The title is correctly set");
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
        test.log(Status.PASS, "Session configurated");
        try{
            if (!driverChrome.findElements(By.id(idHeader)).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 1");
                super.takePhoto(evidencesFolder + "\\J_OK_C.png", "", driverChrome, driverFirefox);
            }
            if (!driverFirefox.findElements(By.id(idHeader)).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 2");
                super.takePhoto("", evidencesFolder + "\\J_OK_F.png", driverChrome, driverFirefox);
            }
            test.log(Status.PASS, "Join Session Ok");
        }catch (NoSuchElementException n){
            super.takePhoto(evidencesFolder + "\\J_ErrorInicializate_C.png", evidencesFolder + "\\J_ErrorInicializate_F.png", driverChrome, driverFirefox);
            test.log(Status.FAIL, "Test Failed");
            fail("The app is not correctly inicializate");
        }
    }

/**
 * Test with Java.
 *
 * @author Andrea Acuña
 * Description: Join the session, verficate that the video is playing property and leave the session
 * @throws IOException
 * @throws InterruptedException
 */
    @Test
    void T002_LeaveSession() throws IOException, InterruptedException{

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
        waitC.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathOtherCamera)));
        
        WebDriverWait waitF = new WebDriverWait(driverFirefox, Duration.ofSeconds(30));
        waitF.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathOtherCamera)));

        driverChrome.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driverFirefox.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // see if the video is playing properly
        String currentTimeChrome= driverChrome.findElement(By.id(idSelfCamera)).getAttribute("duration");
        String currentTimeFirefox = driverFirefox.findElement(By.id(idSelfCamera)).getAttribute("duration");
        
        try{
            assertNotEquals(currentTimeChrome, "NaN");
            assertNotEquals(currentTimeFirefox, "NaN");
            super.takePhoto(evidencesFolder + "\\J_VideoPlaying_C.png", evidencesFolder + "\\J_VideoPlaying_F.png", driverChrome, driverFirefox);
                
            //Leave the session with chrome
            WebElement leaveButtonC = driverChrome.findElement(By.id(idLeaveButton));
            if (leaveButtonC.isDisplayed()){ 
                leaveButtonC.click();
            }
            if(joinButtonC.isDisplayed()){
                System.out.println("The app leave the session correctly in browser 1");
                super.takePhoto(evidencesFolder + "\\J_LeaveSession_C.png", "", driverChrome, driverFirefox);
            }

            //Leave the session with Firefox
            WebElement leaveButtonF = driverFirefox.findElement(By.id(idLeaveButton));
            if (leaveButtonF.isDisplayed()){ 
                leaveButtonF.click();
            }
            if(joinButtonF.isDisplayed()){
                System.out.println("The app leave the session correctly in browser 2");
                super.takePhoto("", evidencesFolder + "\\J_LeaveSession_F.png", driverChrome, driverFirefox);
            }

        }catch (NoSuchElementException n){
            super.takePhoto(evidencesFolder + "\\J_Error_C.png", evidencesFolder + "\\J_Error_F.png", driverChrome, driverFirefox);
            fail("The app is not correctly working");
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
                super.takePhoto(evidencesFolder + "\\J_OK_C.png", "", driverChrome, driverFirefox);
            }
        }
    }catch (NoSuchElementException n){
        super.takePhoto(evidencesFolder + "\\J_ErrorInicializate_C.png", evidencesFolder + "", driverChrome, driverFirefox);
        fail("The app is not correctly inicializate");
    }
}

/**
 * AfterMethod.
 *
 * @author Andrea Acuña
 * Description: close both drivers
 */
@AfterMethod
synchronized void afterMethod(ITestResult result){
    if (result.getStatus() == ITestResult.FAILURE) {
        test.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
        test.fail(result.getThrowable());
    } else if (result.getStatus() == ITestResult.SKIP) {
        test.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
    } else {
        test.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " - Test Case Passed", ExtentColor.GREEN));
    }
    extentReports.flush();
    super.quitTwoBrowsers(driverChrome, driverFirefox);
}

}
