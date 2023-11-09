package ToolsQA;

import java.io.File;
import java.io.IOException;
//import java.time.Duration;
import java.util.NoSuchElementException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;

// AUN DEBO PROBARLO BIEN

public class App 
{
    static WebDriver driverChrome;
    static WebDriver driverFirefox;
    public static void main( String[] args ) throws Exception
    {
        String evidencesFolder = "..\\..\\evidence";

        WebDriver driverChrome;
        WebDriver driverFirefox;

        String URL = "http://localhost:8080/";

        String NAMESESSION = "TestSession";

        String XpathJoinButton = "//*[@id='join-dialog']/div/p[3]/button";
        String idSessionLabel = "//*[@id='join-dialog']/div/p[2]/input";
        String idSession = "session-title";
        //String idLeaveButton = "buttonLeaveSession";
        //String xpathOtherCamera = "/html/body/div[2]/div/div[2]/video";


        ChromeOptions options = new ChromeOptions();
        options.addArguments("--use-fake-ui-for-media-stream");
        options.addArguments("--use-fake-device-for-media-stream");
        driverChrome = new ChromeDriver(options);

        FirefoxOptions optionsF = new FirefoxOptions();
        optionsF.addPreference("media.navigator.permission.disabled", true);
        optionsF.addPreference("media.navigator.streams.fake", true);
        driverFirefox = new FirefoxDriver(optionsF);

        driverChrome.get(URL); 
        driverFirefox.get(URL);



         // --------------------------------------------------------------------------------------------------------------

        // Configurate the session in chrome
        WebElement textBox = driverChrome.findElement(By.xpath(idSessionLabel));
        textBox.clear();
        textBox.sendKeys(NAMESESSION);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();
        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.xpath(idSessionLabel));
        textBoxF.clear();
        textBoxF.sendKeys(NAMESESSION);
        WebElement joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton)); 
        joinButtonF.submit();

        try{
            if (!driverChrome.findElements(By.id(idSession)).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 1");
                takePhoto(evidencesFolder + "\\VUE_OK_C.png", "", driverChrome, driverFirefox);
            }
            if (!driverFirefox.findElements(By.id(idSession)).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 2");
                takePhoto("", evidencesFolder + "\\VUE_OK_F.png", driverChrome, driverFirefox);
            }
        }catch (NoSuchElementException n){
            System.out.println("The app is not correctly inicializate");
            takePhoto(evidencesFolder + "\\VUE_ErrorInicializate_C.png", evidencesFolder + "\\VUE_ErrorInicializate_F.png", driverChrome, driverFirefox);
        }
         // --------------------------------------------------------------------------------------------------------------

         if (driverChrome != null){
            driverChrome.quit();
        }
        if (driverFirefox != null){
            driverFirefox.quit();
        }
    }

    public static void takePhoto(String url1, String url2, WebDriver c, WebDriver f) throws IOException{
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
}
