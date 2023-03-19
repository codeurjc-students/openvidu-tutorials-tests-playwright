import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import Reporter.ExtentManager;


public class Module{
    private ExtentTest test;
    static ExtentReports extent = ExtentManager.createExtentReports();

    /**
     * method.
     *
     * @author Andrea Acuña
     * Description: set up two webDrivers with headless mode
     * Return: List with 2 browsers configurated
     */
    public List<WebDriver> setUpTwoBrowsers(){

        WebDriver driverChrome;
        WebDriver driverFirefox;

        List<WebDriver> browsers = new ArrayList<WebDriver>();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--ignore-certificate-errors","--disable-extensions","--no-sandbox","--disable-dev-shm-usage");
        options.addArguments("--use-fake-ui-for-media-stream");
        options.addArguments("--use-fake-device-for-media-stream");
        options.addArguments("--remote-allow-origins=*");
        driverChrome = new ChromeDriver(options);
        browsers.add(driverChrome);

        FirefoxOptions optionsF = new FirefoxOptions();
        optionsF.setHeadless(true);
        optionsF.addPreference("media.navigator.permission.disabled", true);
        optionsF.addPreference("media.navigator.streams.fake", true);
        driverFirefox = new FirefoxDriver(optionsF);
        browsers.add(driverFirefox);

        return browsers;
    }

    /**
    * method.
    *
    * @author Andrea Acuña
    * Description: take a screenshot to create an evidence.
    * Parameters: 
    *          - url1: the relative or absolute path to a evidence file of the chrome photo
    *          - url2: the relative or absolute path to a evidence file of the firefox photo
    */
    public void takePhoto(String url1, String url2, WebDriver c, WebDriver f) throws IOException{
        try {
            if(url1 != ""){
                File scrFileC = ((TakesScreenshot)c).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFileC, new File(url1));
            }
            if(url2 != ""){
                File scrFileF = ((TakesScreenshot)f).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFileF, new File(url2));
            }          
        } catch (Exception e) {
            System.out.println("an error has occurred with the screenshot. Please preview the url");
        }
    }

    /**
     * method.
     *
     * @author Andrea Acuña
     * Description: close both drivers passed by parameter
     */
    public void quitTwoBrowsers(WebDriver driverChrome, WebDriver driverFirefox) {
        if (driverChrome != null){
            driverChrome.quit();
        }
        if (driverFirefox != null){
            driverFirefox.quit();
        }
    }

    /**
     * method.
     *
     * @author Andrea Acuña
     * Description: create a extend report for the test
     */
    public static synchronized ExtentTest startTest(String testName, String desc) {
        ExtentTest test = extent.createTest(testName, desc);
        return test;
    }

    /**
     * method.
     *
     * @author Andrea Acuña
     * Description: administrate the result
     */
    public void tearDownMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
          test.log(Status.FAIL, "Test failed: " + result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
          test.log(Status.SKIP, "Test skipped: " + result.getThrowable());
        } else {
          test.log(Status.PASS, "Test passed");
        }
      }

      /**
     * method.
     *
     * @author Andrea Acuña
     * Description: create an ExtendReport object and set the report HTML file location and other configurations
     */
    public synchronized static ExtentReports createExtentReports() {
        final ExtentReports extentReports = new ExtentReports();
        ExtentSparkReporter reporter = new ExtentSparkReporter("test-output/Extent.html");
        reporter.config().setReportName("test Report");
        reporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        reporter.config().setTheme(Theme.STANDARD);
        extentReports.attachReporter(reporter);
        extentReports.setSystemInfo("Blog Name", "Automation Report");
        extentReports.setSystemInfo("Author", "Andrea P");
        return extentReports;
    }

     /**
     * method.
     *
     * @author Andrea Acuña
     * Description: remove all previous data
     */
    public void tearDown(ExtentReports extentReports){
        extentReports.flush();
    }

}
