package ToolsQA;

import java.io.File;
import java.io.IOException;
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

public class App 
{
    static WebDriver driverChrome;
    static WebDriver driverFirefox;
    public static void main( String[] args ) throws Exception
    {
        String evidencesFolder = "..\\..\\evidence";
    
        String URL = "http://localhost:4200/";
    
        String NAMESESSION = "TestSession";
    
        String XpathJoinButton = "//*[@id='join-dialog']/form/p[3]/input";
        String idParticipant = "userName";
        //String idLeaveButton = "buttonLeaveSession";
        String idSession = "sessionId";
        String idheader = "session-title";





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



        // Configurate the session in chrome
        WebElement sessionC = driverChrome.findElement(By.id(idSession));
        sessionC.clear();
        sessionC.sendKeys(NAMESESSION);
        WebElement participantC = driverChrome.findElement(By.id(idParticipant));
        participantC.clear();
        participantC.sendKeys("PARTICIPANTCHROME");
        WebElement joinButtonC = driverChrome.findElement(By.xpath(XpathJoinButton)); 
        joinButtonC.submit();

        //Configurate de session in firefox
        WebElement textBoxF = driverFirefox.findElement(By.id(idSession));
        textBoxF.clear();
        textBoxF.sendKeys(NAMESESSION);
        WebElement participantF = driverFirefox.findElement(By.id(idParticipant));
        participantF.clear();
        participantF.sendKeys("PARTICIPANTFIREFOX");
        WebElement joinButtonF = driverFirefox.findElement(By.xpath(XpathJoinButton)); 
        joinButtonF.submit();

        try{
            if (!driverChrome.findElements(By.id(idheader)).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 1");
                takePhoto(evidencesFolder + "\\COK.png");
            }
            if (!driverFirefox.findElements(By.id(idheader)).isEmpty()){
                System.out.println("The app is correctly inicializate in browser 2");
                takePhoto(evidencesFolder + "\\FOK.png");
            }
        }catch (NoSuchElementException n){
            System.out.println("The app is not correctly inicializate");
            takePhoto(evidencesFolder + "\\ErrorInicializate.png");
        }


        driverFirefox.quit();
        driverChrome.quit();
    }

    public static void takePhoto(String url) throws IOException{
        try {
            File scrFile = ((TakesScreenshot)driverChrome).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(url));
        } catch (Exception e) {
            System.out.println("an error has occurred with the screenshot. Please preview the url");
        }
        
    }
}
