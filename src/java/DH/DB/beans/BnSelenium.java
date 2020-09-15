/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DH.DB.beans;

import DH.DB.soporte.DEF;
import java.io.File;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.sql.Connection;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;

/**
 *
 * @author mvall
 */
public class BnSelenium extends Thread{
    
    /**
     *
     */
    public int idServicio = 0;

    /**
     *
     */
    public String estado = "";

    /**
     *
     */
    public String usuario = "";

    /**
     *
     */
    public String password = "";

    /**
     *
     */
    public String zipcodes = "";

    /**
     *
     */
    public String keywords = "";
    
    /**
     *
     */
    public String to = "";
    
    /**
     *
     * @param idServicio
     * @param estado
     * @param usuario
     * @param password
     * @param zipcodes
     * @param keywords
     * @param to
     */
    public BnSelenium(int idServicio, String estado, String usuario, String password, String zipcodes, String keywords, String to) {
        this.idServicio = idServicio;
        this.estado = estado;
        this.usuario = usuario;
        this.password = password;
        this.zipcodes = zipcodes;
        this.keywords = keywords;
        this.to = to;
    }

    /**
     *
     */
    public BnSelenium() {
        super();
    }
    
    @Override
    public void run() {
        WebDriver driver;
        Connection con = null;
        
        System.setProperty("webdriver.gecko.driver", DEF.PATHGECKO);
        driver = new FirefoxDriver();
        FirefoxProfile firefoxProfile = new FirefoxProfile();    
        firefoxProfile.setPreference("browser.private.browsing.autostart",true);
        driver.manage().window().maximize();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        //////////////////////////////////////////////////////////////////

        switch (idServicio) {
            case 1:
                  
                String URL = DEF.PAGE2FARM;
                driver.get(URL);
                WebDriverWait wait = new WebDriverWait(driver, 20);
            {
                try {
                    
                   // takeSnapShot(driver, "/home/centos/Documents/test.png");
                   // takeSnapShot(driver, "C:\\Users\\mvall\\OneDrive\\Documentos\\test.png");

                } catch (Exception ex) {
                    Logger.getLogger(BnSelenium.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                //wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class=\"fa fa-sign-in\"]")));
                //driver.findElement(By.xpath("//*[@class=\"fa fa-sign-in\"]")).click();
                Dimension d = new Dimension(420,600);
		//Resize the current window to the given dimension
		driver.manage().window().setSize(d);
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"m_uname\"]")));
                driver.findElement(By.xpath("//*[@id=\"m_uname\"]")).sendKeys(usuario);
                driver.findElement(By.xpath("//*[@id=\"m_upass\"]")).sendKeys(password);
                driver.findElement(By.xpath("//*[@id=\"mobilelogin\"]")).click();

                JSONArray json = new JSONArray();
                if(checkPager(driver)){
                    json = getAppliance(driver, zipcodes, keywords, to);
                }
              
            break;

        }
    }
    
    
    public boolean checkPager(WebDriver driver){
        boolean resp = true;
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[starts-with(@description, 'This is where available jobs appear. All technicians')]")));
        driver.findElement(By.xpath("//*[starts-with(@description, 'This is where available jobs appear. All technicians')]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@name=\"state\"]"))));
        Select state = new Select(driver.findElement(By.xpath("//*[@name=\"state\"]")));
        state.selectByVisibleText(estado);
        Select pager = new Select(driver.findElement(By.xpath("//*[@id=\"pager\"]")));
        
        if(pager.getOptions().isEmpty()){
            System.out.println("Sin appliance que ejecutar");
            checkPager(driver);
        }
        return resp;
    }
    
    public JSONArray getAppliance(WebDriver driver, String zipCodes, String keyWords, String to){
        String[] zipcodesArray = zipCodes.split(",");
        String[] keywordsArray = keyWords.split(",");
        WebDriverWait wait = new WebDriverWait(driver, 20);
        
        List<WebElement>textDemo= driver.findElements(By.xpath("//*[contains(text(),'Job Reference No.')]/parent::div//following-sibling::div"));

        JSONObject json2 = null;
        int c = 0;
        String label = "";
        boolean isLabel = false;
        boolean isFirst = true;
        JSONArray json = new JSONArray();
        BnServicios bn = new BnServicios();
        
        for (WebElement webElement : textDemo) {
            try {
                //if(webElement.getClass())
                if(webElement.getText().equalsIgnoreCase("")){
                    isLabel = true;
                }else{
                    if(isLabel){
                        label = webElement.getText();
                        isLabel = false;
                    }else{
                        if(isFirst){
                            json2 = new JSONObject();
                            json2.put("orden", webElement.getText());
                            isFirst = false;
                        }else{
                            json2.put(label, webElement.getText());
                            if(label.equalsIgnoreCase("Symptoms")){
                                json.put(json2);
                                isFirst = true;
                                json2 = new JSONObject();
                            }
                        } 
                     }
                }
            } catch (JSONException ex) {
                Logger.getLogger(BnSelenium.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        boolean filtroCumple;
        for (int i = 0; i < json.length(); i++) {
            filtroCumple = true;
            for (String zipcodesArray1 : zipcodesArray) {
                try {
                    if (json.getJSONObject(i).get("Location").toString().contains(zipcodesArray1.trim())) {
                        for (String keywordsArray1 : keywordsArray) {
                            if (json.getJSONObject(i).get("Appliance").toString().contains(keywordsArray1.trim())) {
                                json.getJSONObject(i).put("filtro", true);
                                filtroCumple = false;
                                
                                JavascriptExecutor js = null;
                                if (driver instanceof JavascriptExecutor) {
                                    js = (JavascriptExecutor)driver;
                                } 
                                js.executeScript("$('#book-"+json.getJSONObject(i).get("orden").toString()+"').submit();");
                                bn.notificaRPA(json.getJSONObject(i), to);
                                
                            }else if(filtroCumple){
                                json.getJSONObject(i).put("filtro", false);
                            }
                        }
                    }else if(filtroCumple){
                        json.getJSONObject(i).put("filtro", false);
                    }

                }catch (JSONException ex) {
                    Logger.getLogger(BnSelenium.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return json;
    }
    
    /**
     *
     * @param googleKey
     * @return
     */
    public String getRespCaptcha(String googleKey) {
        //API TOKEN DE EXCH
        String keyEXCH2Captcha = "4bb3691437b264f494c6669381a643e9";
        
        // URL del sitio en donde esta alojado el ReCaptcha
        String site = "https://srienlinea.sri.gob.ec/comprobantes-electronicos-internet/publico/validezComprobantes.jsf";
        
        String resp = "";
        //////////////////////////////////////////////////////////////////

        WebDriver driver;
        driver = new FirefoxDriver();

        //////////////////////////////////////////////////////////////////
        try {
            driver.get("https://2captcha.com/in.php?key=" + keyEXCH2Captcha + "&method=userrecaptcha&googlekey=" + googleKey + "&pageurl=" + site + "");
            String response = driver.findElement(By.xpath("/html/body")).getText();

            if (response.contains("OK")) {
                String id = "";
                try {
                    id = response.split("\\|")[1];
                } catch (Exception e) {
                    System.out.println("Excepcion en la respuesta: " + e.getMessage());
                }
                if (!id.equals("")) {
                    int aux = 0;
                    while (aux <= 15) {
                        try {
                            System.out.println("inicia Intento " + (aux + 1) + " de 15");
                            System.out.println("SOLICITANDO CAPTCHA " + "https://2captcha.com/res.php?key=" + keyEXCH2Captcha + "&action=get&taskinfo=1&json=0&id=" + id);
                            driver.get("https://2captcha.com/res.php?key=" + keyEXCH2Captcha + "&action=get&taskinfo=1&json=0&id=" + id);
                            response = driver.findElement(By.xpath("/html/body")).getText();

                            if ("CAPCHA_NOT_READY".equalsIgnoreCase(response)) {

                                System.out.println("CAPTCHA NOT READY " + "https://2captcha.com/res.php?key=" + keyEXCH2Captcha + "&action=get&id=" + id);

                            } else {
                                if (response.contains("OK")) {
                                    try {
                                        System.out.println("El codigo del Captcha es: " + response.split("\\|")[1]);
                                        resp = response.split("\\|")[1];
                                    } catch (Exception e) {
                                        System.out.println("Excepcion en la respuesta: " + e.getMessage());
                                    }
                                    break;
                                }
                            }

                        } catch (Exception e) {
                            System.out.println("Exception Captcha 1: " + e);
                        }
                        System.out.println("Finaliza intento " + (aux + 1) + " de 15");
                        aux++;
                    }
                }
            } else {
                System.out.println("Respuesta del 2Captcha: " + response);
                driver.quit();
                Thread.sleep(2000);
                getRespCaptcha(googleKey);
            }

        } catch (InterruptedException e) {
            System.out.println("Exception Captcha 2: " + e);
        } finally {
            driver.quit();
        }
        return resp;
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

//           System.out.println(driver.findElement(By.xpath("//div[text() = 'Job Reference No.']")).getText());

//                System.out.println(driver.findElement(By.xpath("//*[text() = '']/parent::div//following-sibling::div[@class='span-6']")).getText());
                //Identificacion de los elementos relacionados con el reCaptcha de Google
                // Se obtiene el iFrame para extraer el data Site KEY y el atributo k del SRC del iFrame
                // ambos valores son el mismo, se ejemplifica ambas formas de obtenerlo

//                    WebElement iFrame = driver.findElement(By.xpath("//iframe[starts-with(@src, 'https://www.google.com/recaptcha')]"));
//                    WebElement siteKeyDiv = driver.findElement(By.xpath("//*[@class=\"g-recaptcha\"]"));
//                    String datasitekey = siteKeyDiv.getAttribute("data-sitekey");
//                    
//                    System.out.println(datasitekey);
//                    String src = iFrame.getAttribute("src");
//                    String k = src.substring(src.indexOf("&k=") + 3, src.indexOf("&co"));
//                    System.out.println(src);
//                    System.out.println(k);
//                    Thread.sleep(2000);
//                    String Token = getRespCaptcha(k);
//                    js.executeScript("document.getElementById('g-recaptcha-response').style.display='block';");
//                    js.executeScript("document.getElementById('g-recaptcha-response').innerHTML='" + Token + "';");
//                    js.executeScript("rcBuscar();");