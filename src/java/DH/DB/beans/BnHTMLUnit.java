/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DH.DB.beans;

import DH.DB.soporte.DEF;
import DH.DB.soporte.InitiateBrowser;
import DH.DB.soporte.Soporte;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import java.math.BigInteger;
import org.openqa.selenium.By;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author mvall
 */
public class BnHTMLUnit extends Thread {

    /**
     * Id del servicio RPA
     */
    public int idServicio = 0;

    /**
     * estado del servicio
     */
    public String estado = "";

    /**
     * usuario del RPA en caso de que haya un login en el page2farm
     */
    public String usuario = "";

    /**
     * password del RPA en caso de que haya un login en el page2farm
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
     * email receptor de los correos enviados por el sistema 
     */
    public String to = "";
    
    /**
     * email receptor de los correos enviados por el sistema 
     */
    public BigInteger idHilo = BigInteger.ZERO;

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
    public BnHTMLUnit(int idServicio, String estado, String usuario, String password, String zipcodes, String keywords, String to) {
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
    public BnHTMLUnit() {
        super();
    }

    public BigInteger getIdHilo() {
        return idHilo;
    }

    public void setIdHilo(BigInteger idHilo) {
        this.idHilo = idHilo;
    }

    @Override
    public void run() {
        try {
            
            long duration = 0;
            int c = 0;
            
            WebDriver driver = InitiateBrowser.InitBrowser("PhantomJS", true, usuario, password, "");
            BnServicios bn = new BnServicios();
            setIdHilo(bn.actualizarEstadosHilo(Thread.currentThread(), 1, c, 1, BigInteger.ZERO, this.idServicio));
            Soporte.info("El ID asignado al hilo es: "+idHilo);
            
            try {
                Thread.sleep(Integer.valueOf(DEF.SLEEPTIME));
            } catch (InterruptedException ex) {
                Soporte.severe("El hilo "+Thread.currentThread().getName()+" ha sido interrrumpido, detalle del error: "+ex.getMessage());
            }           
            
            while(bn.checkService(idHilo) == 1){
                c++;
                Soporte.info("Entrando al ciclo: "+c);
                try {
                    duration = getAppliance(driver, zipcodes, keywords, to);
                } catch (JSONException ex) {
                    Logger.getLogger(BnHTMLUnit.class.getName()).log(Level.SEVERE, null, ex);
                }
                bn.insertarPerformance(duration, "getAppliance", BnHTMLUnit.class.getSimpleName(), idHilo, idServicio);
                Soporte.info("El ciclo duro: "+duration+" milisegundos");

            }
            Soporte.info("Usuario Finalizando hilo: "+Thread.currentThread().getName());
            bn.actualizarEstadosHilo(Thread.currentThread(), 0, c, 2, idHilo, this.idServicio);
            InitiateBrowser.release(driver);
            Thread.currentThread().interrupt();
            
        } catch (FailingHttpStatusCodeException ex) {
            Logger.getLogger(BnHTMLUnit.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    public long getAppliance(WebDriver driver, String zipCodes, String keyWords, String to) throws JSONException {
    
        long startTime;
        long finishTime;
        long duration;
        
        int c = 0;

        BnServicios bn = new BnServicios();
        JSONArray jsonAppliance = new JSONArray();
        JSONArray jsonApplianceTemp;
        JSONArray jsonApplianceOK = new JSONArray();
        JSONArray jsonApplianceOKTemp;
        
        startTime = System.currentTimeMillis();
        
        String[] zipcodesArray = zipCodes.split(",");
        String[] keywordsArray = keyWords.split(",");
        
        driver.navigate().to(DEF.PAGE2FARM+"/artis-tech-home/available-jobs/");
        
        Select pager = null;
        try{
            pager = new Select(driver.findElement(By.xpath("//*[@id=\"pager\"]")));
            List<WebElement> paginas = pager.getOptions();
            if(paginas.isEmpty()){
                Soporte.info("Sin appliance que ejecutar");
            }else{

                for (int i = 1; i <= paginas.size(); i++) {
                    if(i == 1){
                        List<WebElement> textAppliances = driver.findElements(By.xpath("//*[contains(text(),'Job Reference No.')]/parent::div//following-sibling::div"));
                        jsonAppliance = getJsonappliance(textAppliances);
                        jsonApplianceOK = checkFiltro(driver, zipcodesArray, keywordsArray, jsonAppliance);
                    }else{
                        pager = new Select(driver.findElement(By.xpath("//*[@id=\"pager\"]")));
                        pager.selectByVisibleText(String.valueOf(i));
                        List<WebElement> textAppliances = driver.findElements(By.xpath("//*[contains(text(),'Job Reference No.')]/parent::div//following-sibling::div"));
                        jsonApplianceTemp = getJsonappliance(textAppliances);
                        jsonApplianceOKTemp = checkFiltro(driver, zipcodesArray, keywordsArray, jsonApplianceTemp);

                        for (int j = 0; j < jsonApplianceTemp.length(); j++) {
                            JSONObject jsonObject = jsonApplianceTemp.getJSONObject(j);
                            jsonAppliance.put(jsonObject);
                        }

                        for (int k = 0; k < jsonApplianceOKTemp.length(); k++) {
                            JSONObject jsonObject = jsonApplianceOKTemp.getJSONObject(k);
                            jsonApplianceOK.put(jsonObject);
                        }
                    }

                }

            }
            for (int i = 0; i < jsonApplianceOK.length(); i++) {
                bn.notificaRPA(jsonApplianceOK.getJSONObject(i), to);
            }

            bn.insertarResRPA("zipcodes:"+zipcodes+";keywords:"+keywords,jsonAppliance.toString(), jsonApplianceOK.toString(), idHilo, idServicio);
            
        }catch(NoSuchElementException nsee){
            Soporte.severe("El select aun no esta disponible, se establece un timeout de: "+DEF.SLEEPTIME+", y se intentara de nuevo");
            driver.manage().timeouts().implicitlyWait(Integer.valueOf(DEF.SLEEPTIME), TimeUnit.MILLISECONDS);
            try{
                pager = new Select(driver.findElement(By.xpath("//*[@id=\"pager\"]")));
                List<WebElement> paginas = pager.getOptions();
                if(paginas.isEmpty()){
                    Soporte.info("Sin appliance que ejecutar");
                }else{

                    for (int i = 1; i <= paginas.size(); i++) {
                        if(i == 1){
                            List<WebElement> textAppliances = driver.findElements(By.xpath("//*[contains(text(),'Job Reference No.')]/parent::div//following-sibling::div"));
                            jsonAppliance = getJsonappliance(textAppliances);
                            jsonApplianceOK = checkFiltro(driver, zipcodesArray, keywordsArray, jsonAppliance);
                        }else{
                            pager = new Select(driver.findElement(By.xpath("//*[@id=\"pager\"]")));
                            pager.selectByVisibleText(String.valueOf(i));
                            List<WebElement> textAppliances = driver.findElements(By.xpath("//*[contains(text(),'Job Reference No.')]/parent::div//following-sibling::div"));
                            jsonApplianceTemp = getJsonappliance(textAppliances);
                            jsonApplianceOKTemp = checkFiltro(driver, zipcodesArray, keywordsArray, jsonApplianceTemp);

                            for (int j = 0; j < jsonApplianceTemp.length(); j++) {
                                JSONObject jsonObject = jsonApplianceTemp.getJSONObject(j);
                                jsonAppliance.put(jsonObject);
                            }

                            for (int k = 0; k < jsonApplianceOKTemp.length(); k++) {
                                JSONObject jsonObject = jsonApplianceOKTemp.getJSONObject(k);
                                jsonApplianceOK.put(jsonObject);
                            }
                        }

                    }

                }
                for (int i = 0; i < jsonApplianceOK.length(); i++) {
                    bn.notificaRPA(jsonApplianceOK.getJSONObject(i), to);
                }

                bn.insertarResRPA("zipcodes:"+zipcodes+";keywords:"+keywords,jsonAppliance.toString(), jsonApplianceOK.toString(), idHilo, idServicio);

            }catch(NoSuchElementException nsee2){
                Soporte.severe("El select aun no esta disponible en el segundo intento, se procedera a parar el servicio");
                Soporte.info("Finalizando hilo: "+Thread.currentThread().getName());
                bn.actualizarEstadosHilo(Thread.currentThread(), 0, c, 2, idHilo, this.idServicio);
                InitiateBrowser.release(driver);
                Thread.currentThread().interrupt();
            }
        }    
        
        finishTime = System.currentTimeMillis();
        duration = finishTime - startTime;
        return duration;
    }

    public JSONArray getJsonappliance (List<WebElement> tagsAppliance){
        
        /*
        * Bloque para medir el performace del proceso
        *
        */
        long startTime;
        long finishTime;
        long durationTime;
        
        startTime = System.currentTimeMillis();
            
        BnServicios bn = new BnServicios();
        JSONArray arrayJsonAppliance = new JSONArray();
        JSONObject appliance = null;
        
        String label = "";
        boolean isLabel = false;
        boolean isFirst = true;
                
        for (WebElement webElement : tagsAppliance) {
            try {
                if (webElement.getText().equalsIgnoreCase("")) {
                    isLabel = true;
                } else {
                    if (isLabel) {
                        label = webElement.getText();
                        isLabel = false;
                    } else {
                        if (isFirst) {
                            appliance = new JSONObject();
                            appliance.put("orden", webElement.getText());
                            isFirst = false;
                        } else {
                            appliance.put(label, webElement.getText());
                            if (label.equalsIgnoreCase("Symptoms")) {
                                arrayJsonAppliance.put(appliance);
                                isFirst = true;
                                appliance = new JSONObject();
                            }
                        }
                    }
                }
            } catch (JSONException ex) {
                Logger.getLogger(BnSelenium.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        finishTime = System.currentTimeMillis();
        durationTime = finishTime - startTime;
        bn.insertarPerformance(durationTime, "getAppliance:getJsonappliance", BnHTMLUnit.class.getSimpleName(), idHilo, idServicio);
            
        return arrayJsonAppliance;
    }
    
    public JSONArray checkFiltro(WebDriver driver, String[] zipcodesArray, String[] keywordsArray, JSONArray jsonAppliance){
        
        /*
        * Bloque para medir el performace del proceso
        *
        */
        long startTime;
        long finishTime;
        long durationTime;
        
        BnServicios bn = new BnServicios();
        startTime = System.currentTimeMillis();
        
        JSONArray jsonApplianceOK = new JSONArray();
        boolean filtroCumple;
        JavascriptExecutor js = null;
        for (int i = 0; i < jsonAppliance.length(); i++) {
            filtroCumple = true;
            for (String zipcodesArray1 : zipcodesArray) {
                try {
                    if (jsonAppliance.getJSONObject(i).get("Location").toString().contains(zipcodesArray1.trim())) {
                        for (String keywordsArray1 : keywordsArray) {
                            if (jsonAppliance.getJSONObject(i).get("Appliance").toString().toUpperCase().contains(keywordsArray1.trim().toUpperCase())) {
                                
                                filtroCumple = false;
                                jsonAppliance.getJSONObject(i).put("filtro", true);
                                jsonApplianceOK.put(jsonAppliance.getJSONObject(i));

                                if (driver instanceof JavascriptExecutor) {
                                    js = (JavascriptExecutor) driver;
                                }
                                String ejecucion = (String) js.executeScript("return $('#book-" + jsonAppliance.getJSONObject(i).get("orden").toString() + "').submit();");
                                Soporte.info("El Script ejecutado devuelve el siguiente mensaje: "+ejecucion);
                            } else if (filtroCumple) {
                                jsonAppliance.getJSONObject(i).put("filtro", false);
                            }
                        }
                    } else if (filtroCumple) {
                        jsonAppliance.getJSONObject(i).put("filtro", false);
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(BnSelenium.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        finishTime = System.currentTimeMillis();
        durationTime = finishTime - startTime;
        bn.insertarPerformance(durationTime, "getAppliance:checkFiltro", BnHTMLUnit.class.getSimpleName(), idHilo, idServicio);
     
        return jsonApplianceOK;
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

     
}
