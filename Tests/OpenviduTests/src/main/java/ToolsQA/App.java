package ToolsQA;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class App 
{
    static WebDriver driverChrome;
    static WebDriver driverFirefox;
    public static void main( String[] args ) throws Exception
    {
        String evidencesFolder = "D:\\TFG\\codigo\\Tests\\DemoMavenProject\\src\\test\\evidence";
        //"..\\..\\evidence";


        WebDriver driverChrome;
        WebDriver driverFirefox;

        String URL = "http://localhost:8080/";

        String NAMESESSION = "TestSession";

        String XpathJoinButton = "//*[@id='join']/form/p[2]/input";
        String xpathLeaveButton = "//*[@id='session']/input";
        String xpathOtherCamera = "/html/body/div[2]/div/div[2]/video";


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
        WebElement textBox = driverChrome.findElement(By.id("sessionId"));
        textBox.clear();
        textBox.sendKeys(NAMESESSION);
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();
        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.id("sessionId"));
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
        String currentTimeChrome = driverChrome.findElement(By.id("local-video-undefined")).getAttribute("currentTime");
        String currentTimeFirefox = driverFirefox.findElement(By.id("local-video-undefined")).getAttribute("currentTime");
        takePhoto(evidencesFolder + "\\vpc.png", evidencesFolder + "\\vpf.png", driverChrome, driverFirefox);


        if (Float.parseFloat(currentTimeChrome) > 0 && Float.parseFloat(currentTimeFirefox) > 0){
                //Leave the session with chrome
            try{
                WebElement leaveButtonC = driverChrome.findElement(By.xpath(xpathLeaveButton));
                if (leaveButtonC.isDisplayed()){ 
                    leaveButtonC.click();
                }
                if(joinButtonC.isDisplayed()){
                    System.out.println("The app leave the session correctly in browser 1");
                    takePhoto(evidencesFolder + "\\HW_LeaveSession_C.png", "", driverChrome, driverFirefox);
                }

                //Leave the session with Firefox

                WebElement leaveButtonF = driverFirefox.findElement(By.xpath(xpathLeaveButton));
                if (leaveButtonF.isDisplayed()){ 
                    leaveButtonF.click();
                }
                if(joinButtonF.isDisplayed()){
                    System.out.println("The app leave the session correctly in browser 2");
                    takePhoto("", evidencesFolder + "\\HW_LeaveSession_F.png", driverChrome, driverFirefox);
                }

            }catch (NoSuchElementException n){
                System.out.println("The app is not correctly working");
                takePhoto(evidencesFolder + "\\HW_Error_C.png", evidencesFolder + "\\HW_Error_F.png", driverChrome, driverFirefox);
            }
        }else{
            System.out.println("The video is not playing properly");
            takePhoto(evidencesFolder + "\\HW_VideoNotWorking_C.png", evidencesFolder + "\\HW_VideoNotWorking_F.png", driverChrome, driverFirefox);
        }

         // --------------------------------------------------------------------------------------------------------------

        driverFirefox.quit();
        driverChrome.quit();
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
