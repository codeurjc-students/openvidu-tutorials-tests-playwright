import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.TestNG;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.google.common.collect.Table.Cell;

import io.github.bonigarcia.wdm.WebDriverManager;

//import Reporter.ExtentManager;


public class Module extends TestNG{

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

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--ignore-certificate-errors","--disable-extensions","--no-sandbox","--disable-dev-shm-usage");
        options.addArguments("--use-fake-ui-for-media-stream");
        options.addArguments("--use-fake-device-for-media-stream");
        options.addArguments("--remote-allow-origins=*");
        driverChrome = new ChromeDriver(options);
        browsers.add(driverChrome);

        WebDriverManager.firefoxdriver().setup();

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
     * Description: administrate the result
    
    public void tearDownMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
          test.log(Status.FAIL, "Test failed: " + result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
          test.log(Status.SKIP, "Test skipped: " + result.getThrowable());
        } else {
          test.log(Status.PASS, "Test passed");
        }
      } */

    /**
     * method.
     *
     * @author Andrea Acuña
     * Description: create an ExtendReport object and set the report HTML file location and other configurations
     */
    public ExtentReports createExtentReports() {
        
        String filePath = "test-output/Extent.html";

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

    /**
     * method.
     *
     * @author Andrea Acuña
     * Description: open the excel file and read the value specified
     */
    public static String readVariablesFromExcel(String filePath, String testName, String ColValue) {

        try (FileInputStream fileInputStream = new FileInputStream(filePath);
         Workbook workbook = WorkbookFactory.create(fileInputStream)) {
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0); // Obtener la primera hoja del archivo Excel

        int testNameColumnIndex = 0; // Columna de la variable "testName"
        int colValueColumnIndex = -1; // Columna de la variable "ColValue"

        // Buscar la columna cuyo valor sea "ColValue"
        Row firstRow = sheet.getRow(0); // Obtener la primera fila
        int lastCellNum = firstRow.getLastCellNum(); // Obtener el número de celdas en la primera fila

        for (int i = 0; i < lastCellNum; i++) {
            org.apache.poi.ss.usermodel.Cell cell = firstRow.getCell(i);
            if (cell.getStringCellValue().equals(ColValue)) {
                colValueColumnIndex = i;
                break;
            }
        }

        if (colValueColumnIndex == -1) {
            System.out.println("No se encontró la columna: " + ColValue + " en el archivo Excel.");
            return null;
        }

        // Buscar la fila cuyo valor en la columna "testNameColumnIndex" sea "testName"
        int lastRowIndex = sheet.getLastRowNum(); // Obtener el índice de la última fila

        for (int i = 1; i <= lastRowIndex; i++) {
            Row row = sheet.getRow(i);
            org.apache.poi.ss.usermodel.Cell testNameCell = row.getCell(testNameColumnIndex);

            if (testNameCell != null && testNameCell.getStringCellValue().equals(testName)) {
                org.apache.poi.ss.usermodel.Cell valueCell = row.getCell(colValueColumnIndex);

                if (valueCell != null) {
                    return valueCell.getStringCellValue();
                } else {
                    return null;
                }
            }
        }

        System.out.println("No se encontró la fila con el valor de testName: " + testName + " en el archivo Excel.");
    } catch (IOException e) {
        e.printStackTrace();
    }

    return null;
    }

}
