package Reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    public static final ExtentReports extentReports = new ExtentReports();

    /**
     * method.
     *
     * @author Andrea Acuña
     * Description: create an ExtendReport object and set the report HTML file location and other configurations
     */
    public synchronized static ExtentReports createExtentReports() {
        ExtentSparkReporter reporter = new ExtentSparkReporter("./extent-reports/extent-report.html");
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
    public void tearDown(){
        extentReports.flush();
    }
}