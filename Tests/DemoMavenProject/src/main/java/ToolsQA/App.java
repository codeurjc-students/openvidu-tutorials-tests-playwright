package ToolsQA;

import java.io.File;
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

import org.slf4j.Logger;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
        WebDriver driverChrome;
        WebDriver driverFirefox;



        ChromeOptions options = new ChromeOptions();
        // This flag avoids to grant the user media
        options.addArguments("--use-fake-ui-for-media-stream");
        // This flag fakes user media with synthetic video (green with spinner
        // and timer)
        options.addArguments("--use-fake-device-for-media-stream");
        driverChrome = new ChromeDriver(options);



        FirefoxOptions optionsF = new FirefoxOptions();
        // This flag avoids granting the access to the camera
        optionsF.addPreference("media.navigator.permission.disabled", true);
        // This flag force to use fake user media (synthetic video of multiple
        // color)
        optionsF.addPreference("media.navigator.streams.fake", true);
        driverFirefox = new FirefoxDriver(optionsF);



        String url = "http://localhost:8080/";
        driverChrome.get(url); 
        driverFirefox.get(url);
    

        // Configurate the session in chrome
        WebElement textBox = driverChrome.findElement(By.id("sessionId"));
        textBox.clear();
        textBox.sendKeys("TestSession");
        WebElement joinButton = driverChrome.findElement(By.xpath("//*[@id='join']/form/p[2]/input")); 
        joinButton.submit();
        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.id("sessionId"));
        textBoxF.clear();
        textBoxF.sendKeys("TestSession");
        WebElement joinButtonF = driverFirefox.findElement(By.xpath("//*[@id='join']/form/p[2]/input")); 
        joinButtonF.submit();




        File scrFile = ((TakesScreenshot)driverChrome).getScreenshotAs(OutputType.FILE);
        // Now you can do whatever you need to do with it, for example copy somewhere
        FileUtils.copyFile(scrFile, new File("D:\\TFG\\codigo\\Tests\\ScreenShots\\photo.png"));




        // see if the video is playing properly
        String currentTimeChrome = driverChrome.findElement(By.id("local-video-undefined")).getAttribute("currentTime");
        String currentTimeFirefox = driverFirefox.findElement(By.id("local-video-undefined")).getAttribute("currentTime");

        if (Float.parseFloat(currentTimeChrome) > 0 && Float.parseFloat(currentTimeFirefox) > 0){
                //Leave the session with chrome
            try{
                WebElement leaveButton = driverChrome.findElement(By.xpath("//*[@id='session']/input"));
                if (leaveButton.isDisplayed()){ 
                    leaveButton.click();
                }
                if(joinButton.isDisplayed()){
                    System.out.println("The app leave the session correctly in browser 1");
                }

            }catch (NoSuchElementException n){
                System.out.println("The app is not correctly working in browser 1");
            }

                //Leave the session with Firefox
            try{
                WebElement leaveButtonF = driverFirefox.findElement(By.xpath("//*[@id='session']/input"));
                if (leaveButtonF.isDisplayed()){ 
                    leaveButtonF.click();
                }
                if(joinButtonF.isDisplayed()){
                    System.out.println("The app leave the session correctly in browser 2");
                }

            }catch (NoSuchElementException n){
                System.out.println("The app is not correctly working in browser 2");
            }
        }else{
            System.out.println("The video is not playing properly");
        }


        driverFirefox.quit();
        driverChrome.quit();
    }

    public static void takeSnapShot(WebDriver webdriver,String fileWithPath) throws Exception{
        //Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot =((TakesScreenshot)webdriver);
        //Call getScreenshotAs method to create image file
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
        //Move image file to new destination
        File DestFile=new File(fileWithPath);
        //Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);
        }
}
