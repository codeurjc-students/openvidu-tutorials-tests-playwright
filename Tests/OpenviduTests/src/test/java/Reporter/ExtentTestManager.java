package Reporter;

import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class ExtentTestManager{
    //static Map<Integer, ExtentTest> extentTestMap = new HashMap<>();
    private ExtentTest test;
    static ExtentReports extent = ExtentManager.createExtentReports();

    /**
     * method.
     *
     * @author Andrea Acuña
     * Description: return ExtentTest instance in extentTestMap by using current thread id
     */
    public static synchronized ExtentTest startTest(String testName, String desc) {
        ExtentTest test = extent.createTest(testName, desc);
        return test;
    }

    public void tearDownMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
          test.log(Status.FAIL, "Test failed: " + result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
          test.log(Status.SKIP, "Test skipped: " + result.getThrowable());
        } else {
          test.log(Status.PASS, "Test passed");
        }
      }
}
