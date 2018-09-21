/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.soporte;

import java.security.SecureRandom;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Desarrollador
 */
public class Soporte {
    
    /**
     *
     */
    public static final String loggerId = "Cache Siisa v1.0.0";

    /**
     *
     */
    public static final Logger LOG = Logger.getLogger(loggerId);

    //<editor-fold  desc="METODOS LOG">    
    /**
     * Logger general de la aplicación.
     */
    /**
     * Entrega la referencia al log de la aplicación.
     *
     * @return una istancia de {@link Logger}
     */
    /**
     * Metodo que imprime en log LEVEL INFO
     *
     * @param mensaje String
     */
    public static void info(String mensaje) {
        LOG.log(Level.INFO, loggerId + " - {0}", mensaje);
    }

    /**
     * Metodo que imprime en log LEVEL INFO
     *
     * @param mensaje String
     * @param obj Objetc
     */
    public static void info(String mensaje, Object obj) {
        LOG.log(Level.INFO, loggerId + " - " + mensaje, obj);
    }

    /**
     * Metodo que imprime en log LEVEL INFO
     *
     * @param mensaje String
     * @param obj Objet[]
     */
    public static void info(String mensaje, Object[] obj) {
        LOG.log(Level.INFO, loggerId + " - " + mensaje, obj);
    }

    /**
     * Metodo que imprime en log LEVEL INFO
     *
     * @param mensaje String
     * @param obj Object type {@link Throwable}
     */
    public static void info(String mensaje, Throwable obj) {
        LOG.log(Level.INFO, loggerId + " - " + mensaje, obj);
    }

    /**
     * Metodo que imprime en log LEVEL SEVERE
     *
     * @param mensaje String
     */
    public static void severe(String mensaje) {
        LOG.log(Level.SEVERE, loggerId + " - {0}", mensaje);
    }

    /**
     * Metodo que imprime en log LEVEL SEVERE
     *
     * @param mensaje String
     * @param obj Object
     */
    public static void severe(String mensaje, Object obj) {
        LOG.log(Level.SEVERE, loggerId + " - " + mensaje, obj);
    }

    /**
     * Metodo que imprime en log LEVEL SEVERE
     *
     * @param mensaje String
     * @param obj Object[]
     */
    public static void severe(String mensaje, Object[] obj) {
        LOG.log(Level.SEVERE, loggerId + " - " + mensaje, obj);
    }

    /**
     * Metodo que imprime en log LEVEL SEVERE
     *
     * @param mensaje String
     * @param obj Obtect type {@link Throwable}
     */
    public static void severe(String mensaje, Throwable obj) {
        LOG.log(Level.SEVERE, loggerId + " - " + mensaje, obj);
    }

//</editor-fold>
    public static String buscarEnJSON(String str, String keyA, String busqueda) {
        try {
            JSONObject json;
            Object intervention;
            JSONArray interventionJsonArray;
            Object json2 = new JSONTokener(str).nextValue();
            intervention = json2;
            if (intervention instanceof JSONArray) {
                if (keyA.equals(busqueda)) {
                    return str;
                }
                interventionJsonArray = (JSONArray) intervention;
                int i = 0;
                while (i < interventionJsonArray.length()) {
                    String aux = buscarEnJSON(interventionJsonArray.get(i).toString(), keyA, busqueda);
                    if (!aux.equals("")) {
                        return aux;
                    }
                    i++;
                }
            } else if (intervention instanceof JSONObject) {
                if (keyA.equals(busqueda)) {
                    return str;
                }
                json = new JSONObject(str);
                Iterator<?> permisos = json.keys();
                while (permisos.hasNext()) {
                    String key = (String) permisos.next();
                    String aux = buscarEnJSON(json.get(key).toString(), key, busqueda);
                    if (!aux.equals("")) {
                        return aux;
                    }
                }
            } else {
                if (keyA.equals(busqueda)) {
                    return str;
                } else {
                    return "";
                }
            }
        } catch (JSONException ex) {
            if (keyA.equals(busqueda)) {
                return str;
            } else {
                return "";
            }
        }
        return "";
    }

    protected static SecureRandom random = new SecureRandom();

    public synchronized String generateToken() {
        long longToken = Math.abs(random.nextLong());
        String random = Long.toString(longToken, 50);
        return random;
    }
}
