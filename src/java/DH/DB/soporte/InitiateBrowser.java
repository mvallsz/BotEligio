/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DH.DB.soporte;

import java.io.File;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 *
 * @author mvallsz85
 */
public class InitiateBrowser {

   
    public static WebDriver driver = null;
    String workingDir = "";
    
    /**
     *
     * @param browserName
     * @param haveALogin
     * @param user
     * @param password
     * @param extraPath
     */
    public static WebDriver InitBrowser(String browserName, boolean haveALogin, String user, String password, String extraPath){
        
        if(browserName.equalsIgnoreCase("PhantomJS")){
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setJavascriptEnabled(true);
            File file = new File(DEF.PATHPHANTHOM);				
            
            caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, file.getAbsolutePath());
            caps.setCapability("takesScreenshot", true);
            driver = new PhantomJSDriver(caps);
            driver.get(DEF.PAGE2FARM+extraPath);
            
            Soporte.info("Se inicia sesión del navegador: "+browserName+", para la direccion URL: ");
            if(haveALogin){
                loginSession(driver, user, password, true);
            }
            
 
        }else if(browserName.equalsIgnoreCase("Chrome")){
            System.setProperty("webdriver.chrome.driver","");
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        }
        return driver;
    }
    
    /**
     *
     * @param driver
     * @param user
     * @param password
     * @param tryAgain
     */
    public static void loginSession (WebDriver driver, String user, String password, boolean tryAgain){
        
        try {
            Soporte.info("Se inicia proceso de LOGIN, usuario: "+user+", para la direccion URL: "+DEF.PAGE2FARM);
            
            try{
                driver.findElement(By.xpath(DEF.XPATHSUSERLOGIN)).sendKeys(user);
                driver.findElement(By.xpath(DEF.XPATHSPASSLOGIN)).sendKeys(password);
                driver.findElement(By.xpath(DEF.XPATHSBOTONLOGIN)).click();
            }catch(NoSuchElementException nsee){
                Soporte.severe("No ha cargado aun el boton de login: "+nsee.getMessage());
                if(tryAgain){
                    Soporte.info("Se intentara una vez mas el proceso de Login");
                    loginSession(driver, user, password, false);
                }else{
                    Soporte.severe("Proceso de Login no se pudo ejecutar, se procedera a cerrar el driver del Phantom y a matar el hilo: "+Thread.currentThread().getName());
                    release(driver);
                    return;
                }
                
            }
            
        } catch (Exception ex) {
            Logger.getLogger(InitiateBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
              
        
    }
    
    /**
     *
     * @param webdriver
     * @param fileWithPath
     * @throws Exception
     */
    public void takeSnapShot(WebDriver webdriver,String fileWithPath) throws Exception{
        //Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot =((TakesScreenshot)webdriver);
        //Call getScreenshotAs method to create image file
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
        //Move image file to new destination
        File DestFile=new File(fileWithPath);
        //Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);
    }
    
       /**
      * Release those resource of phantomjs, include shutdown phantom process.
      *
      * @param driver close cannot shutdown, should do it with quit()
      */
     public static void release(WebDriver driver) {
         try {
             if (driver != null) driver.close();
         } finally {
             if (driver != null) driver.quit();
         }
     }
}
