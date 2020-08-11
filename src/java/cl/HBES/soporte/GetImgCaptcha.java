/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.soporte;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.apache.commons.codec.binary.Base64;
import java.util.logging.Logger;


/**
 *
 * @author Manuel Valls
 */
public class GetImgCaptcha {

    private static final Logger LOG = Logger.getLogger(GetImgCaptcha.class.getName());
    
    /**
     * Método que devuelve el String Base64 del archivo imagen que contiene el Captcha a resolver
     * @param file
     * @return String Base64
     */
    public static String encodeFileToBase64Binary(File file) {
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
        
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            LOG.severe(e.getMessage()); 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            LOG.severe(e.getMessage());
        }
        LOG.log(Level.FINE, "Se genero de forma correcta el String Base64 asociado al Archivo: {0}", file.getName());
        return encodedfile;
    }

    /**
     * Método que devuelve el ID de 2Captcha que indica que inicia el proceso de resolución de la imagen, recibe como parametro el String Base64 del archivo que contiene el captcha a resolver
     * @param file
     * @return String Codigo 2Captcha
     * @throws java.io.IOException
     */
    
    public static String getId2Captcha(String file) throws IOException{
        URL url = new URL("https://2captcha.com/in.php");
        
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("method", "base64");
        params.put("key", "4bb3691437b264f494c6669381a643e9");
        params.put("body", file);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()),
                    "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length",
                String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        BufferedReader in = new BufferedReader(new InputStreamReader(
                conn.getInputStream(), "UTF-8"));
        String respuesta = "";
        respuesta = in.lines().collect(Collectors.joining());

        return respuesta;
    }

     /**
     * Método que devuelve el Captcha de 2Captcha que resuelve el desafio entregado
     * @param llave
     * @return String Captcha resuelto
     * @throws java.io.IOException
     */
    
    public static String getStringCaptcha(String llave) throws IOException {
        URL url = new URL("https://2captcha.com/res.php");
        Map<String, Object> params = new LinkedHashMap<>();

        params.put("action", "get");
        params.put("key", "4bb3691437b264f494c6669381a643e9");
        params.put("id", llave);
        
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()),
                    "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length",
                String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        BufferedReader in = new BufferedReader(new InputStreamReader(
                conn.getInputStream(), "UTF-8"));
        String respuesta = "";
        respuesta = in.lines().collect(Collectors.joining());

        return respuesta;

    }

    public static void main(String[] args) {
        try {
            File f = new File("C:\\Users\\Mabel Gutierrez\\Desktop\\Captcha.png");
            String encodstring = encodeFileToBase64Binary(f);
            String respuesta = String.valueOf(getId2Captcha(encodstring));
            String parts = respuesta.substring(0, 2);

 
            if (parts.equals("OK")) {
                String parts2 = respuesta.substring(3);
                int n = 1;
                while (getStringCaptcha(parts2).equals("CAPCHA_NOT_READY")) {
                    LOG.log(Level.WARNING, "Intento: {0}", n);
                    n++;
                }
                System.out.println(getStringCaptcha(parts2));
            }

        } catch (IOException e) {
            LOG.severe(e.getMessage());
        }
    }
}