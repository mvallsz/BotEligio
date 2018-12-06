/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.soporte;

import cl.HBES.clases.CredencialesUsuario;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.security.SecureRandom;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Desarrollador
 */
public class Soporte {

    /**
     *
     */
    public static final String loggerId = "HBES v1.0.0";

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

    public static String buscarEnJSONv2(String str, String busqueda) {
        String val = "";
        if (busqueda.contains("/")) {
            try {
                String[] parts = busqueda.split("/");
                String oldstring2 = str;
                for (int i = 0; i < parts.length; i++) {
                    String part1 = parts[i];
                    oldstring2 = buscarEnJSON(oldstring2, "", part1);
                }
                val = oldstring2;
            } catch (Exception ex) {
                Soporte.severe("{0}:{1}", new Object[]{Soporte.class.getName(), ex.toString()});
            }
        } else {
            val = buscarEnJSON(str, "", busqueda);
        }
        return val;
    }

    protected static SecureRandom random = new SecureRandom();

    public synchronized String generateToken() {
        long longToken = Math.abs(random.nextLong());
        String random = Long.toString(longToken, 50);
        return random;
    }

    public static CredencialesUsuario getUsuarioSesion(HttpServletRequest request) {
        if (isSesionActiva(request)) {
            HttpSession sesion = request.getSession(false);
            CredencialesUsuario usuLogin = (CredencialesUsuario) sesion.getAttribute(DEF.SESSION_USUARIO);
            if (usuLogin == null) {
                return null;
            } else {
                return usuLogin;
            }
        } else {
            return null;
        }
    }

    public static boolean isSesionActiva(HttpServletRequest request) {
        CredencialesUsuario usuLogin = (CredencialesUsuario) request.getSession().getAttribute(DEF.SESSION_USUARIO);
        return usuLogin != null;
    }

    public static String buscarHijos(String datos, JSONArray hijos) {
        String val = "[";
        try {
            if (datos.length() > 0) {
                JSONArray jsonArray = new JSONArray();
                Object json2 = new JSONTokener(datos).nextValue();
                Object intervention = json2;
                if (intervention instanceof JSONArray) {
                    jsonArray = new JSONArray(datos);
                } else if (intervention instanceof JSONObject) {
                    JSONObject jsonObject = new JSONObject(datos);
                    jsonArray.put(jsonObject);
                }
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (i != 0) {
                            val += ",";
                        }
                        val += "{";
                        JSONObject json = new JSONObject(jsonArray.get(i).toString());
                        for (int j = 0; j < hijos.length(); j++) {
                            if (j != 0) {
                                val += ",";
                            }
                            JSONObject jHi = new JSONObject(hijos.get(j).toString());
                            String valor = json.get(jHi.getString("busqueda")).toString();
                            String nombre = jHi.getString("nombre");
                            try {
                                Long.parseLong(valor);
                                val += "\"" + nombre + "\":" + valor;
                            } catch (Exception e) {
                                try {
                                    Float.parseFloat(valor);
                                    val += "\"" + nombre + "\":" + valor;
                                } catch (Exception ex) {
                                    try {
                                        Double.parseDouble(valor);
                                        val += "\"" + nombre + "\":" + valor;
                                    } catch (Exception exs) {
                                        val += "\"" + nombre + "\":\"" + valor + "\"";
                                    }
                                }
                            }
                        }
                        val += "}";
                    }
                }
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{Soporte.class.getName(), ex.toString()});
        }
        val += "]";
        return val;
    }

    public static void toPage(String page, HttpServletRequest request, HttpServletResponse response, HttpServlet ser) throws ServletException {
        try {
            ser.getServletContext().getRequestDispatcher(page).forward(request, response);
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }
    }
}
