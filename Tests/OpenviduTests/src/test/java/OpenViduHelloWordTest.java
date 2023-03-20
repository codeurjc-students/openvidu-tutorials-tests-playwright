import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import Listener.TestListener;

/**
 * Test with Java.
 * Test for the hello word for open vidu 
 * @author Andrea Acuña
 */
@Listeners(TestListener.class)
class OpenViduHelloWordTest extends Module{

    String evidencesFolder = "..\\..\\evidence";

    private ExtentTest test;
    public static ExtentReports extentReports = new ExtentReports();

    WebDriver driverChrome;
    WebDriver driverFirefox;

    String URL = "http://localhost:8080/";

    String NAMESESSION = "TestSession";
    String TESTNAME = "";

    String XpathJoinButton = "//*[@id='join']/form/p[2]/input";
    String xpathLeaveButton = "//*[@id='session']/input";
    String xpathOtherCamera = "/html/body/div[2]/div/div[2]/video";

    String idHeader = "session-header";
    String idNameSession = "sessionId";
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

        extentReports = super.createExtentReports();
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
        test.log(Status.INFO, "Session configurated");
        try{
            if (!driverChrome.findElements(By.id(idHeader)).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 1");
                super.takePhoto(evidencesFolder + "\\HW_OK_C.png", "", driverChrome, driverFirefox);
            }
            if (!driverFirefox.findElements(By.id(idHeader)).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 2");
                super.takePhoto("", evidencesFolder + "\\HW_OK_F.png", driverChrome, driverFirefox);
            }
            test.log(Status.PASS, "Join Session Ok");
        }catch (NoSuchElementException n){
            super.takePhoto(evidencesFolder + "\\HW_ErrorInicializate_C.png", evidencesFolder + "\\HW_ErrorInicializate_F.png", driverChrome, driverFirefox);
            test.log(Status.FAIL, "Test Failed");
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
        
        WebDriverWait waitF = new WebDriverWait(driverFirefox, Duration.ofSeconds(30));
        waitF.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathOtherCamera)));

        // see if the video is playing properly
        String currentTimeChrome = driverChrome.findElement(By.id(idSelfCamera)).getAttribute("currentTime");
        String currentTimeFirefox = driverFirefox.findElement(By.id(idSelfCamera)).getAttribute("currentTime");
        super.takePhoto(evidencesFolder + "\\HW_VideoPlaying_C.png", evidencesFolder + "\\HW_VideoPlaying_F.png", driverChrome, driverFirefox);


        if (Float.parseFloat(currentTimeChrome) > 0 && Float.parseFloat(currentTimeFirefox) > 0){
                //Leave the session with chrome
            try{
                WebElement leaveButtonC = driverChrome.findElement(By.xpath(xpathLeaveButton));
                if (leaveButtonC.isDisplayed()){ 
                    leaveButtonC.click();
                }
                if(joinButtonC.isDisplayed()){
                    System.out.println("The app leave the session correctly in browser 1");
                    super.takePhoto(evidencesFolder + "\\HW_LeaveSession_C.png", "", driverChrome, driverFirefox);
                }

                //Leave the session with Firefox

                WebElement leaveButtonF = driverFirefox.findElement(By.xpath(xpathLeaveButton));
                if (leaveButtonF.isDisplayed()){ 
                    leaveButtonF.click();
                }
                if(joinButtonF.isDisplayed()){
                    System.out.println("The app leave the session correctly in browser 2");
                    super.takePhoto("", evidencesFolder + "\\HW_LeaveSession_F.png", driverChrome, driverFirefox);
                }

            }catch (NoSuchElementException n){
                super.takePhoto(evidencesFolder + "\\HW_Error_C.png", evidencesFolder + "\\HW_Error_F.png", driverChrome, driverFirefox);
                fail("The app is not correctly working");
            }
        }else{
            super.takePhoto(evidencesFolder + "\\HW_VideoNotWorking_C.png", evidencesFolder + "\\HW_VideoNotWorking_F.png", driverChrome, driverFirefox);
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
                    super.takePhoto(evidencesFolder + "\\HW_OK_C.png", "", driverChrome, driverFirefox);
                }
            }
        }catch (NoSuchElementException n){
            super.takePhoto(evidencesFolder + "\\HW_ErrorInicializate_C.png", evidencesFolder + "", driverChrome, driverFirefox);
            fail("The app is not correctly inicializate");
        }
    }

/**
 * AfterEach.
 *
 * @author Andrea Acuña
 * Description: close both drivers
 */
    @AfterMethod
    void quit(ITestResult result){
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