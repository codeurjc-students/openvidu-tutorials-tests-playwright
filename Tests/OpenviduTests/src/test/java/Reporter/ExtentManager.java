package Reporter;

import static org.junit.jupiter.api.Assertions.fail;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    
    /**
     * method.
     *
     * @author Andrea Acuña
     * Description: create an ExtendReport object and set the report HTML file location and other configurations
     */
    public ExtentReports createExtentReports(String output) {
        
        String filePath = "test-output/" + output;

        ExtentReports extentReports = new ExtentReports();
        ExtentSparkReporter reporter = new ExtentSparkReporter(filePath);
        reporter.config().setReportName("test Report");
        reporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        reporter.config().setTheme(Theme.DARK);
        extentReports.attachReporter(reporter);
        extentReports.setSystemInfo("Blog Name", "Automation Report");
        extentReports.setSystemInfo("Author", "Andrea P");

        return extentReports;
    }

    /**
     * method.
     *
     * @author Andrea Acuña
     * Description: create a extend report for the test static synchronized
     */
    public ExtentTest startTest(String testName, String desc, ExtentReports e) {
        ExtentTest test = e.createTest(testName, desc);
        return test;
    }

     /**
     * method.
     *
     * @author Andrea Acuña
     * Description: makes a capture of the driver passed by parameter
     */
    public String addInfoStepWithPhoto(WebDriver driver, String description){
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        String base64Screenshot = screenshot.getScreenshotAs(OutputType.BASE64);
        String base64 = "data:image/png;base64," + base64Screenshot;

        return "<div><img src=\"" + base64 + "\"/></div><div>" + description + "</div>"; 
    }

    /**
     * method.
     *
     * @author Andrea Acuña
     * Description: Add a step to the report and attach a capture of driver
     */
    public void addStep(ExtentTest test, String status, WebDriver driver, String description){

        if (status.equals("INFO")){
            test.log(Status.INFO, addInfoStepWithPhoto(driver, description));
        }else if (status.equals("PASS")){
            test.log(Status.PASS, addInfoStepWithPhoto(driver, description));
        }else if(status.equals("FAIL")){
            test.log(Status.FAIL, addInfoStepWithPhoto(driver, description));

        }else{
            test.log(Status.FAIL, "Status in code is not ok");
            fail("The app is not correctly inicializate");
        }
        
    }

    /**
     * method.
     *
     * @author Andrea Acuña
     * Description: Add a step to the report
     */
    public void addStepWithoutCapture(ExtentTest test, String status, String description){

        if (status.equals("INFO")){
            test.log(Status.INFO, description);
        }else if (status.equals("PASS")){
            test.log(Status.PASS, description);
        }else if(status.equals("FAIL")){
            test.log(Status.FAIL, description);

        }else{
            test.log(Status.FAIL, "Status in code is not ok");
            fail("The app is not correctly inicializate");
        }
        
    }

    /**
     * method.
     *
     * @author Andrea Acuña
     * Description: remove all previous data
     */
    public void tearDownExtent(ExtentReports extentReports){
        extentReports.flush();
    }

    public void getResult(ITestResult result, ExtentTest test) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
            test.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
        } else {
            test.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " - Test Case Passed", ExtentColor.GREEN));
        }
    }
}