import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.github.bonigarcia.wdm.WebDriverManager;


public class Module{

    /**
     * protected method.
     *
     * @author Andrea Acuña
     * Description: set up two webDrivers (One in Chrome. One in Firefox) with headless mode
     * Parameters: None
     * Return: List with 2 browsers configurated. In the first position is Chrome and in the second position is Firefox
     */
    protected List<WebDriver> setUpTwoBrowsers(){

        WebDriver driverChrome;
        WebDriver driverFirefox;

        List<WebDriver> browsers = new ArrayList<WebDriver>();

        WebDriverManager.chromedriver().clearDriverCache().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--ignore-certificate-errors","--disable-extensions","--no-sandbox","--disable-dev-shm-usage");
        options.addArguments("--use-fake-ui-for-media-stream");
        options.addArguments("--use-fake-device-for-media-stream");
        options.addArguments("--remote-allow-origins=*");
        driverChrome = new ChromeDriver(options);
        browsers.add(driverChrome);

        WebDriverManager.firefoxdriver().clearDriverCache().setup();

        FirefoxOptions optionsF = new FirefoxOptions();
        optionsF.setHeadless(true);
        optionsF.addPreference("media.navigator.permission.disabled", true);
        optionsF.addPreference("media.navigator.streams.fake", true);
        driverFirefox = new FirefoxDriver(optionsF);
        browsers.add(driverFirefox);

        return browsers;
    }

    /**
     * protected method.
     *
     * @author Andrea Acuña
     * Description: close both drivers passed by parameter
     * Parameters: driverChrome: driver of chrome
     *             driverFirefox: driver of firefox
     * Return: None
     */
    protected void quitTwoBrowsers(WebDriver driverChrome, WebDriver driverFirefox) {
        if (driverChrome != null){
            driverChrome.quit();
        }
        if (driverFirefox != null){
            driverFirefox.quit();
        }
    }

    /**
     * protected static method.
     *
     * @author Andrea Acuña
     * Description: open the excel file and read the value specified
     * Parameters: filePath: path where the excel that will be accessed is hosted
     *             testName: Name of the test that is using the method
     *             ColValue: name of the col in the excel
     * Return: Value of the intersection between testName (Row) and ColValue (Col)
     */
    protected static String readVariablesFromExcel(String filePath, String testName, String ColValue) {

        try (FileInputStream fileInputStream = new FileInputStream(filePath);
        Workbook workbook = WorkbookFactory.create(fileInputStream)) {
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);

        int testNameColumnIndex = 0;
        int colValueColumnIndex = -1;

        Row firstRow = sheet.getRow(0);
        int lastCellNum = firstRow.getLastCellNum();

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

        int lastRowIndex = sheet.getLastRowNum();

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
