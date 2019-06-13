/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.beans;

import cl.HBES.BDD.Conexion;
import cl.HBES.soporte.DEF;
import cl.HBES.soporte.Soporte;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.XML;

/**
 *
 * @author Desarrollador
 */
public class BnDatos {

    public static JSONObject obtenerDatosSoap(String rut, String parametros, String url, String xml, int tipoResponse, String parametrosWeb, boolean web) {
        JSONObject respuesta = new JSONObject();
        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(rut, parametros, xml, parametrosWeb, web), url);
            String resp = soapAstring(soapResponse);
            byte[] ptext = resp.getBytes(ISO_8859_1);
            String value = new String(ptext, UTF_8);
            resp = parsearResp(value);
            respuesta = pasarJSON(resp, tipoResponse);
        } catch (SOAPException | UnsupportedOperationException | JSONException ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        }
        return respuesta;
    }

    private static SOAPMessage createSOAPRequest(String rut, String credenciales, String xml, String parametrosWeb, boolean web) throws JSONException {

        SOAPMessage soapMessage = null;
        try {
            String message = "";
            String[] xmlS = xml.split("\n");
            JSONObject json = new JSONObject(credenciales);
            for (String x : JSONObject.getNames(json)) {
                if (x.equalsIgnoreCase("parametros")) {
                    json.remove(x);
                    break;
                }
            }
            if (web) {
                json = reemplDatConst(json.toString(), parametrosWeb);
            }
            for (int i = 0; i < xmlS.length; i++) {
                String texto = xmlS[i];
                for (String name : JSONObject.getNames(json)) {
                    if (texto.contains(name)) {
                        String valor = json.get(name).toString();
                        if (!web) {
                            if (valor.equalsIgnoreCase("dv") || valor.equalsIgnoreCase("rut") || valor.equalsIgnoreCase("rut-dv") || valor.equalsIgnoreCase("rutdv")) {
                                switch (valor.toUpperCase()) {
                                    case "RUT":
                                        String[] var = rut.split("-");
                                        valor = var[0];
                                        break;
                                    case "DV":
                                        String[] var1 = rut.split("-");
                                        valor = var1[1];
                                        break;
                                    case "RUT-DV":
                                        valor = rut;
                                        break;
                                    case "RUTDV":
                                        String[] var2 = rut.split("-");
                                        valor = var2[0] + "" + var2[1];
                                        break;
                                }
                            }
                        }
                        texto = texto.replace("?", valor);
                        break;
                    }
                }
                if (texto.contains("?")) {
                    texto = texto.replace("?", "");
                }
                message = message + (message == "" ? texto : texto + ((i + 1) == xmlS.length ? "" : "\n"));
            }

            soapMessage = obtenerSoap(message);

            MimeHeaders hd = soapMessage.getMimeHeaders();
            if (json.has("SOAPAction")) {
                hd.addHeader("SOAPAction", json.getString("SOAPAction"));
            }

        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        }
        return soapMessage;
    }

    public static JSONObject obtenerDatosRest(String rut, String credenciales, String url, String xml, int tipoResponse, String parametrosWeb, boolean web) {
        JSONObject respuesta = new JSONObject();
        try {
            int cont = 1;
            String Get = url;
            JSONObject json = new JSONObject(credenciales);
            for (String x : JSONObject.getNames(json)) {
                if (x.equalsIgnoreCase("parametros")) {
                    json.remove(x);
                    break;
                }
            }
            if (web) {
                json = reemplDatConst(json.toString(), parametrosWeb);
            }
            for (String name : JSONObject.getNames(json)) {
                if (cont == 1) {
                    Get = Get + "?" + name + "=";
                    cont++;
                } else {
                    Get = Get + "&" + name + "=";
                }
                String valor = json.get(name).toString();
                if (!web) {
                    if (valor.equalsIgnoreCase("dv") || valor.equalsIgnoreCase("rut") || valor.equalsIgnoreCase("rut-dv") || valor.equalsIgnoreCase("rutdv")) { //RUT SIEMPRE VENDRA CON EN -
                        switch (valor.toUpperCase()) {
                            case "RUT":
                                String[] var = rut.split("-");
                                valor = var[0];
                                break;
                            case "DV":
                                String[] var1 = rut.split("-");
                                valor = var1[1];
                                break;
                            case "RUT-DV":
                                valor = rut;
                                break;
                            case "RUTDV":
                                String[] var2 = rut.split("-");
                                valor = var2[0] + "" + var2[1];
                                break;
                        }
                    }
                }
                Get = Get + valor;
            }

            URL link = new URL(Get);
            HttpURLConnection urlc = (HttpURLConnection) link.openConnection();
            urlc.setAllowUserInteraction(false);
            urlc.setDoInput(true);
            urlc.setDoOutput(false);
            urlc.setUseCaches(true);
            urlc.setRequestMethod("GET");
            urlc.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                xml += line;
            }
            respuesta = pasarJSON(xml, tipoResponse);
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        }
        return respuesta;
    }

    public static JSONObject pasarJSON(String datos, int tipoResponse) {
        JSONObject respuesta = new JSONObject();
        try {
            if (tipoResponse == 1) {//JSON
                if (datos.equalsIgnoreCase("")) {
                    datos = "{}";
                } else if (datos.startsWith("[")) {
                    datos = "{\"datos\":" + datos + "}";
                }
                respuesta = new JSONObject(datos);
            } else if (tipoResponse == 2) {//XML
                respuesta = XML.toJSONObject(datos);
                String aux = "";
                boolean parsear = false;
                if (respuesta.has("RecuperarDatosOrsanXmlResponse")) {
                    aux = Soporte.buscarEnJSONv2(respuesta.toString(), "RecuperarDatosOrsanXmlResult");
                    parsear = true;
                }
                if (parsear) {
                    respuesta = XML.toJSONObject(aux);
                }
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
        }
        return respuesta;
    }

    /**
     *
     * @param message
     * @return
     */
    public static String soapAstring(SOAPMessage message) {
        String result = null;

        if (message != null) {
            ByteArrayOutputStream baos = null;
            try {
                baos = new ByteArrayOutputStream();
                message.writeTo(baos);
                result = baos.toString();

            } catch (SOAPException | IOException ex) {
                Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
                ex.printStackTrace(System.out);
            } finally {
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (IOException ioe) {
                        Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ioe.toString()});
                        ioe.printStackTrace(System.out);
                    }
                }
            }
        }

        return result;
    }

    /**
     *
     * @param xml
     * @return
     * @throws SOAPException
     * @throws IOException
     */
    public static SOAPMessage obtenerSoap(String xml) throws SOAPException, IOException {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))));
        return message;
    }

    /**
     *
     * @param resp
     * @return
     */
    public static String parsearResp(String resp) {
        resp = resp.replace("get:", "");
        resp = resp.replace("soapenv:", "");
        resp = resp.replace("soap:", "");
        resp = resp.replace("-xmlns:", "");
        resp = resp.replace("xmlns:", "");
        resp = resp.replace("tns:", "");
        resp = resp.replace("sch:", "");
        resp = resp.replace("S:B", "B");
        resp = resp.replace("com:", "");
        resp = resp.replace("wsse:", "");
        resp = resp.replace("wsu:", "");
        return resp;
    }

    public static int verificarVigencia(String rut, int vigencia, long idEmpresa, int vigenciaCantDias, int diaVigencia, int isActivo, long idServi, String parametros, boolean web) {
        int cantidad = 1;//NO ESTA REGISTRADO EN CACHE
        Connection con = Conexion.getConn();
        try {
            JSONObject jParam = new JSONObject();
            Date fechaActual = new Date();
            Date fechaUltima = null;
            String query = "";
            if (web) {
                jParam = new JSONObject(parametros);
                JSONObject jaux = new JSONObject();
                query = "SELECT credenciales FROM " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA WHERE ID=?;";
                PreparedStatement pst1 = con.prepareStatement(query);
                pst1.setLong(1, idServi);
                ResultSet rs = pst1.executeQuery();
                while (rs.next()) {
                    JSONObject creden = new JSONObject((rs.getString(1) == null || rs.getString(1).equals("") ? "{}" : rs.getString(1)));
                    if (creden.has("parametros")) {
                        JSONArray param = creden.getJSONArray("parametros");
                        for (String name : JSONObject.getNames(jParam)) {
                            for (int i = 0; i < param.length(); i++) {
                                if (param.getString(i).equals(name)) {
                                    jaux.put(name, jParam.get(name));
                                    break;
                                }
                            }
                        }
                    }
                }
                jParam = jaux;
                String busqueda = "";
                for (String name : JSONObject.getNames(jParam)) {
                    busqueda += (busqueda.equals("") ? "" : " AND ") + "PARAMETROS like '%\"" + name + "\":\"" + jParam.get(name) + "\"%'";
                }
                query = "SELECT UPDATED_DATE FROM " + DEF.ESQUEMA + ".DATA_RESPONSE WHERE " + (busqueda.equals("") ? "" : " AND ") + "ID_EMPRESA = ? AND SERVICIO = ? ORDER BY FECHA DESC LIMIT 1";
                PreparedStatement pst2 = con.prepareStatement(query);
                pst2.setLong(1, idEmpresa);
                pst2.setLong(2, idServi);
                ResultSet rs1 = pst2.executeQuery();
                if (rs1.next()) {
                    fechaUltima = rs1.getDate("UPDATED_DATE");
                }
                if (fechaUltima == null) {
                    busqueda = "";

                    for (String name : JSONObject.getNames(jParam)) {
                        busqueda += (busqueda.equals("") ? "" : " AND ") + "PARAMETROS like '%\"" + name + "\":\"" + jParam.get(name) + "\"%'";
                    }
                    query = "SELECT FECHA FROM " + DEF.ESQUEMA + ".DATA_RESPONSE WHERE " + (busqueda.equals("") ? "" : " AND ") + "ID_EMPRESA = ? AND SERVICIO = ? ORDER BY FECHA DESC LIMIT 1";
                    PreparedStatement pst = con.prepareStatement(query);
                    pst.setLong(1, idEmpresa);
                    pst.setLong(2, idServi);
                    ResultSet rs2 = pst.executeQuery();
                    if (rs2.next()) {
                        fechaUltima = rs2.getDate("FECHA");
                    }
                }
            } else {
                query = "SELECT UPDATED_DATE FROM " + DEF.ESQUEMA + ".DATA_RESPONSE WHERE PARAMETROS like ? AND ID_EMPRESA = ? AND SERVICIO = ? ORDER BY FECHA DESC LIMIT 1";
                PreparedStatement pst2 = con.prepareStatement(query);
                pst2.setString(1, rut);
                pst2.setLong(2, idEmpresa);
                pst2.setLong(3, idServi);
                ResultSet rs1 = pst2.executeQuery();
                if (rs1.next()) {
                    fechaUltima = rs1.getDate("UPDATED_DATE");
                }

                if (fechaUltima == null) {
                    query = "SELECT FECHA FROM " + DEF.ESQUEMA + ".DATA_RESPONSE WHERE PARAMETROS like ? AND ID_EMPRESA = ? AND SERVICIO = ? ORDER BY FECHA DESC LIMIT 1";
                    PreparedStatement pst = con.prepareStatement(query);
                    pst.setString(1, rut);
                    pst.setLong(2, idEmpresa);
                    pst.setLong(3, idServi);
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        fechaUltima = rs.getDate("FECHA");
                    }
                }
            }
            if (fechaUltima != null) {
                long diferencia = (fechaActual.getTime() - fechaUltima.getTime()) / 86400000;
                diferencia++;
                boolean vigente = verificarVigencia(diferencia, fechaUltima, vigencia, vigenciaCantDias, diaVigencia);
                if (vigente) {
                    cantidad = 2;//ESTA VIGENTE
                } else {
                    cantidad = 3;//NO ESTA VIGENTE
                }
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnResponse.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
            cantidad = 4;
        } finally {
            Conexion.desconectar(con);
        }
        if (cantidad == 1 && isActivo == 0) {
            cantidad = 5; //NO ESTA EN CACHE  Y TAMPOCO TIENE ACCESO A BUREAU
        } else if (cantidad == 3 && isActivo == 0) {
            cantidad = 2; //ESTA EN CACHE PERO NO ESTA VIGENTE, DEBERIA SALIR A WS SIN EMBARGO NO ESTA ACTIVO EL BUREAU ASI QUE SE ENVIA A CACHE.
        }
        return cantidad;
    }

    public static boolean verificarVigencia(long diferencia, Date fechaUlt, int vigencia, int vigenciaCantDias, int diaVigencia) {
        Calendar fechaUltima = new GregorianCalendar();
        fechaUltima.setTime(fechaUlt);
        Calendar fechaActual = Calendar.getInstance();
        int diaActual = fechaActual.get(Calendar.DAY_OF_WEEK);
        int diaUlt = fechaUltima.get(Calendar.DAY_OF_WEEK);
        ArrayList<Integer> dias = new ArrayList<Integer>();
        // Si la vigencia se mide de acuerdo a una cantidad de días, se ejecuta tal y como antes
        if (vigencia == 1) {
            if (diferencia < vigenciaCantDias) {
                return true;
            } else {
                return false;
            }
        } else {// Si la vigencia se mide por un día específico de la semana
            if (diferencia >= 7) { // Si la última vez que se consultó el RUT fue hace más de 7 días, esta vencido
                return false;
            } else {
                dias = calcularDias(diaVigencia, diaActual);//RELLENA UN ARRAY LIST CON TODOS LOS DIAS DONDE LA VIGENCIA ESTA OK
                if (dias.contains(diaUlt)) {//SI EL DIA ACTUAL SE ENCUENTRA EN ESA LISTA DEVUELVE TRUE
                    return true;
                } else {
                    return false;
                }

            }
        }
    }

    public static ArrayList<Integer> calcularDias(int diaVigencia, int diaActual) {
        ArrayList<Integer> dias = new ArrayList<Integer>();
        boolean sw = true;
        int i = diaVigencia;
        while (sw) {
            dias.add(i);
            if (i == diaActual) {
                sw = false;
            }
            if (i == 7) {
                i = 1;
            } else {
                i++;
            }
        }
        return dias;

    }

    public static void buscarEnCache(String rut, long idEmpresa, long idServicio, String token, String user, String parametros, boolean web) {
        System.out.println("Buscando en Cache " + idServicio);
        Connection con = Conexion.getConn();
        try {
            JSONObject jParam = new JSONObject();
            if (web) {
                jParam = new JSONObject(parametros);
                JSONObject jaux = new JSONObject();
                String query = "SELECT credenciales FROM " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA WHERE ID=?;";
                PreparedStatement pst1 = con.prepareStatement(query);
                pst1.setLong(1, idServicio);
                ResultSet rs1 = pst1.executeQuery();
                while (rs1.next()) {
                    JSONObject creden = new JSONObject((rs1.getString(1) == null || rs1.getString(1).equals("") ? "{}" : rs1.getString(1)));
                    if (creden.has("parametros")) {
                        JSONArray param = creden.getJSONArray("parametros");
                        for (String name : JSONObject.getNames(jParam)) {
                            for (int i = 0; i < param.length(); i++) {
                                if (param.getString(i).equals(name)) {
                                    jaux.put(name, jParam.get(name));
                                    break;
                                }
                            }
                        }
                    }
                }
                jParam = jaux;
                String busqueda = "";
                for (String name : JSONObject.getNames(jParam)) {
                    busqueda += (busqueda.equals("") ? "" : " AND ") + "PARAMETROS like '%\"" + name + "\":\"" + jParam.get(name) + "\"%'";
                }
                query = "SELECT ID, DATA FROM " + DEF.ESQUEMA + ".DATA_RESPONSE WHERE " + (busqueda.equals("") ? "" : " AND ") + "SERVICIO = ? AND ID_EMPRESA = ? ORDER BY FECHA DESC LIMIT 1";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setLong(1, idServicio);
                pst.setLong(2, idEmpresa);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    BigInteger idConsulta = BigInteger.valueOf(rs.getLong("ID"));
                    guardarRegistroConsulta(idEmpresa, idConsulta, user, token, 2);
                }
            } else {
                String query = "SELECT ID, DATA FROM " + DEF.ESQUEMA + ".DATA_RESPONSE WHERE PARAMETROS like ? AND SERVICIO = ? AND ID_EMPRESA = ? ORDER BY FECHA DESC LIMIT 1";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, rut);
                pst.setLong(2, idServicio);
                pst.setLong(3, idEmpresa);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    BigInteger idConsulta = BigInteger.valueOf(rs.getLong("ID"));
                    guardarRegistroConsulta(idEmpresa, idConsulta, user, token, 2);
                }
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
    }

    public static void actualizarContador(long idConsulta) {
        Connection con = Conexion.getConn();
        try {
            Statement stmt = null;
            stmt = con.createStatement();
            String query = "SELECT contador FROM " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA WHERE ID =" + idConsulta + ";";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int contador = rs.getInt(1) + 1;
                query = "UPDATE " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA SET contador=" + contador + " WHERE ID=" + idConsulta + ";";
                PreparedStatement pst2 = con.prepareStatement(query);
                pst2.executeUpdate();
            }
        } catch (SQLException ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
    }

    public static void guardarRegistroConsulta(long idEmpresa, BigInteger idConsulta, String emailUsuario, String token, int buscar_datos) {
        Connection con = Conexion.getConn();
        try {
            Statement stmt = null;
            stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO " + DEF.ESQUEMA + ".REGISTRO_CONSULTAS\n"
                    + "(ID_EMPRESA, USUARIO, FECHA, ID_DATA_RESPONSE, TOKEN, BUSCAR_DATOS)\n"
                    + "VALUES( " + idEmpresa + ", '" + emailUsuario + "', CURRENT_TIMESTAMP, " + idConsulta + ", '" + token + "', " + buscar_datos + ");");
        } catch (SQLException ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
    }

    public static BigInteger guardarEnCache(String rut, String data, long idEmpresa, String emailUsuario, long idServi, String parametrosWeb, boolean web) {
        System.out.println("Guardar en Cache " + idServi);
        data = data.replace("\\\"", "");
        Connection con = Conexion.getConn();
        try {
            JSONObject jsonParam = new JSONObject();
            if (web) {
                jsonParam = new JSONObject(parametrosWeb);
                JSONObject jaux = new JSONObject();
                String query = "SELECT credenciales FROM " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA WHERE ID=?;";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setLong(1, idServi);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    JSONObject creden = new JSONObject((rs.getString(1) == null || rs.getString(1).equals("") ? "{}" : rs.getString(1)));
                    if (creden.has("parametros")) {
                        JSONArray param = creden.getJSONArray("parametros");
                        for (String name : JSONObject.getNames(jsonParam)) {
                            for (int i = 0; i < param.length(); i++) {
                                if (param.getString(i).equals(name)) {
                                    jaux.put(name, jsonParam.get(name));
                                    break;
                                }
                            }
                        }
                    }
                }
                jsonParam = jaux;
            } else {
                jsonParam = new JSONObject(rut);
            }
            if (data.length() > 2) {
                String sql = "INSERT INTO " + DEF.ESQUEMA + ".DATA_RESPONSE\n"
                        + "(ID_EMPRESA, PARAMETROS, `DATA`, SERVICIO, FECHA, CREATED_BY, CREATED_DATE)\n"
                        + "VALUES( ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP);";
                PreparedStatement pst2 = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pst2.setLong(1, idEmpresa);
                pst2.setString(2, jsonParam.toString());
                pst2.setString(3, data);
                pst2.setLong(4, idServi);
                pst2.setString(5, emailUsuario);
                pst2.executeUpdate();
                ResultSet rs = pst2.getGeneratedKeys();
                if (rs.next()) {
                    java.math.BigInteger idConsulta = rs.getBigDecimal(1).toBigInteger();
                    return idConsulta;
                }
                return new BigInteger("0");
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
        return new BigInteger("0");
    }

    public static BigInteger actualizarEnCache(String rut, String data, String correo_usuario, long idEmpresa, long idServicio, String parametrosWeb, boolean web) {
        System.out.println("Actualizar datos Cache");
        data = data.replace("\\\"", "");
        Connection con = Conexion.getConn();
        try {
            String query = "";
            long idAc = 0;
            JSONObject jsonParam = new JSONObject();
            if (web) {
                jsonParam = new JSONObject(parametrosWeb);
                JSONObject jaux = new JSONObject();
                query = "SELECT credenciales FROM " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA WHERE ID=?;";
                PreparedStatement pst1 = con.prepareStatement(query);
                pst1.setLong(1, idServicio);
                ResultSet rs1 = pst1.executeQuery();
                while (rs1.next()) {
                    JSONObject creden = new JSONObject((rs1.getString(1) == null || rs1.getString(1).equals("") ? "{}" : rs1.getString(1)));
                    if (creden.has("parametros")) {
                        JSONArray param = creden.getJSONArray("parametros");
                        for (String name : JSONObject.getNames(jsonParam)) {
                            for (int i = 0; i < param.length(); i++) {
                                if (param.getString(i).equals(name)) {
                                    jaux.put(name, jsonParam.get(name));
                                    break;
                                }
                            }
                        }
                    }
                }
                jsonParam = jaux;
                if (data.length() > 2) {
                    String busqueda = "";
                    for (String name : JSONObject.getNames(jsonParam)) {
                        busqueda += (busqueda.equals("") ? "" : " AND ") + "PARAMETROS like '%\"" + name + "\":\"" + jsonParam.get(name) + "\"%'";
                    }
                    query = "SELECT ID FROM " + DEF.ESQUEMA + ".DATA_RESPONSE WHERE " + (busqueda.equals("") ? "" : " AND ") + "ID_EMPRESA = ? AND SERVICIO = ? ORDER BY FECHA DESC LIMIT 1;";
                    PreparedStatement pst = con.prepareStatement(query);
                    pst.setLong(1, idEmpresa);
                    pst.setLong(2, idServicio);
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        idAc = rs.getLong(1);
                    }
                }
            } else {
                if (data.length() > 2) {
                    query = "SELECT ID FROM " + DEF.ESQUEMA + ".DATA_RESPONSE WHERE PARAMETROS = ?  AND ID_EMPRESA = ? AND SERVICIO = ? ORDER BY FECHA DESC LIMIT 1;";
                    PreparedStatement pst = con.prepareStatement(query);
                    pst.setString(1, rut);
                    pst.setLong(2, idEmpresa);
                    pst.setLong(3, idServicio);
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        idAc = rs.getLong(1);
                    }
                }
            }
            if (idAc > 0) {
                query = "UPDATE " + DEF.ESQUEMA + ".DATA_RESPONSE SET `DATA`=?, UPDATED_BY= ?, UPDATED_DATE=CURRENT_TIMESTAMP \n"
                        + "WHERE ID=? AND ID_EMPRESA= ? AND  SERVICIO = ?";
                PreparedStatement pst1 = con.prepareStatement(query);
                pst1.setString(1, data);
                pst1.setString(2, correo_usuario);
                pst1.setLong(3, idAc);
                pst1.setLong(4, idEmpresa);
                pst1.setLong(5, idServicio);
                pst1.executeUpdate();
                return new BigInteger(String.valueOf(idAc));
            } else {
                return new BigInteger("0");
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
        return new BigInteger("0");
    }

    public static String buscarDatosServicios(String token, long idEmpresa, long idServicio) {
        String datos = "";
        Connection con = Conexion.getConn();
        try {
            String query = "SELECT RG.ID_DATA_RESPONSE FROM " + DEF.ESQUEMA + ".REGISTRO_CONSULTAS RG\n"
                    + "JOIN " + DEF.ESQUEMA + ".DATA_RESPONSE DT ON (RG.ID_DATA_RESPONSE = DT.ID)\n"
                    + "JOIN " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA SER ON (DT.SERVICIO = SER.ID)\n"
                    + "WHERE RG.TOKEN = ? AND RG.ID_EMPRESA = ? AND SER.ID = ?;";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, token);
            pst.setLong(2, idEmpresa);
            pst.setLong(3, idServicio);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                query = "SELECT `DATA` FROM " + DEF.ESQUEMA + ".DATA_RESPONSE WHERE ID = ?;";
                PreparedStatement pst2 = con.prepareStatement(query);
                pst2.setLong(1, rs.getLong(1));
                ResultSet rs2 = pst2.executeQuery();
                while (rs2.next()) {
                    datos = rs2.getString(1);
                }
            }
            if (datos == "") {
                datos = "SIN DATOS";
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
            datos = "ERROR";
        } finally {
            Conexion.desconectar(con);
        }
        return datos;
    }

    public static boolean validarServicio(long idOrigen, String token, long idEmpresa, String rut) {
        boolean validar = false;
        Connection con = Conexion.getConn();
        try {
            String[] r = rut.split("-");
            int cantidadActivo = 0;
            String query = "SELECT COUNT(*) FROM " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA\n"
                    + "WHERE ID_EMPRESA = ? AND id_origen = ? AND activo = 1 AND tipo_rut IN (" + (r[0].length() > 50000000 ? "2,3" : "1,3") + ");";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setLong(1, idEmpresa);
            pst.setLong(2, idOrigen);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                cantidadActivo = rs.getInt(1);
            }
            int cantidad = 0;
            query = "SELECT COUNT(*) FROM " + DEF.ESQUEMA + ".REGISTRO_CONSULTAS\n"
                    + "WHERE TOKEN = ? AND ORIGEN = ? AND ID_EMPRESA = ?;";
            PreparedStatement pst2 = con.prepareStatement(query);
            pst2.setString(1, token);
            pst2.setLong(2, idOrigen);
            pst2.setLong(3, idEmpresa);
            ResultSet rs2 = pst2.executeQuery();
            while (rs2.next()) {
                cantidad = rs2.getInt(1);
            }
            if (cantidadActivo == cantidad) {
                validar = true;
            }
        } catch (SQLException ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
        return validar;
    }

    public JSONArray buscarOrigen(long idEmpresa) throws JSONException {
        JSONArray resp = new JSONArray();
        Connection con = Conexion.getConn();
        try {
            String query = "SELECT SER.id_origen, ORI.DESCRIPCION FROM " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA SER\n"
                    + "JOIN " + DEF.ESQUEMA + ".ORIGEN ORI ON (SER.id_origen = ORI.ID)\n"
                    + "WHERE SER.id_empresa = ? GROUP BY SER.id_origen ORDER BY SER.id_origen ASC;";//origen
            PreparedStatement pst = con.prepareStatement(query);
            pst.setLong(1, idEmpresa);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject origen = new JSONObject();
                JSONArray servicio = new JSONArray();
                origen.put("ID", rs.getLong(1));
                origen.put("NOMBRE", rs.getString(2));
                query = "SELECT ID, nombre FROM " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA WHERE id_origen = ? AND id_empresa = ? AND activo = 1;";//servicio
                PreparedStatement pst1 = con.prepareStatement(query);
                pst1.setLong(1, rs.getLong(1));
                pst1.setLong(2, idEmpresa);
                ResultSet rs1 = pst1.executeQuery();
                while (rs1.next()) {
                    JSONObject ser = new JSONObject();
                    JSONArray variable = new JSONArray();
                    ser.put("ID", rs1.getLong(1));
                    ser.put("NOMBRE", rs1.getString(2));
                    query = "SELECT ID_VARIABLE, VARIABLE FROM " + DEF.ESQUEMA + ".VARIABLE WHERE TIPO_SERVICIO = ?;";//variable
                    PreparedStatement pst2 = con.prepareStatement(query);
                    pst2.setLong(1, rs1.getLong(1));
                    ResultSet rs2 = pst2.executeQuery();
                    while (rs2.next()) {
                        JSONObject var = new JSONObject();
                        JSONArray hijo = new JSONArray();
                        var.put("ID", rs2.getLong(1));
                        var.put("NOMBRE", rs2.getString(2));
                        query = "SELECT ID, VARIABLE FROM " + DEF.ESQUEMA + ".VARIABLE_HIJO WHERE VARIABLE_PADRE = ?;";//variable hijo
                        PreparedStatement pst3 = con.prepareStatement(query);
                        pst3.setLong(1, rs2.getLong(1));
                        ResultSet rs3 = pst3.executeQuery();
                        while (rs3.next()) {
                            JSONObject varh = new JSONObject();
                            varh.put("ID", rs3.getLong(1));
                            varh.put("NOMBRE", rs3.getString(2));
                            hijo.put(varh);
                        }
                        var.put("VARHIJO", hijo);
                        variable.put(var);
                    }
                    ser.put("VARIABLE", variable);
                    servicio.put(ser);
                }
                origen.put("SERVICIO", servicio);
                resp.put(origen);
            }
        } catch (SQLException ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
        return resp;
    }

    public BigInteger guardarWS(String servicios, String nombre, BigInteger idEmpresa) {
        BigInteger id = new BigInteger("0");
        Connection con = Conexion.getConn();
        try {
            String sql = "INSERT INTO " + DEF.ESQUEMA + ".RESPONSE_EMPRESA (NOMBRE, DATOS, EMPRESA) VALUES( ?, ?, ?);";
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, nombre);
            pst.setString(2, servicios);
            pst.setString(3, idEmpresa.toString());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            while (rs.next()) {
                id = new BigInteger(rs.getString(1));
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
        return id;
    }

    public JSONArray buscarWs(BigInteger idEmpresa) {
        JSONArray resp = new JSONArray();
        Connection con = Conexion.getConn();
        try {
            String sql = "SELECT ID, NOMBRE FROM " + DEF.ESQUEMA + ".RESPONSE_EMPRESA WHERE EMPRESA = ?;";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, idEmpresa.toString());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject json = new JSONObject();
                json.accumulate("ID", rs.getString(1));
                json.accumulate("NOMBRE", rs.getString(2));
                resp.put(json);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
        return resp;
    }

    public JSONObject buscarNom(long idUser) {
        JSONObject rsp = new JSONObject();
        Connection con = Conexion.getConn();
        try {
            String sql = "SELECT US.nombre, CU.historial FROM " + DEF.ESQUEMA + ".CREDENCIALES_USUARIO US\n"
                    + "JOIN " + DEF.ESQUEMA + ".cuentaEmpresa CU ON (US.id_empresa = CU.ID)\n"
                    + "WHERE US.ID = ?;";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setLong(1, idUser);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                rsp.put("user", rs.getString(1));
                rsp.put("hist", rs.getInt(2));
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
        return rsp;
    }

    public static JSONObject reemplDatConst(String credenciales, String parametrosWeb) {
        JSONObject resp = new JSONObject();
        try {
            JSONObject jsonCreden = new JSONObject(credenciales);
            JSONObject jsonParam = new JSONObject(parametrosWeb);
            if (jsonCreden.length() > 0) {
                for (String Creden : JSONObject.getNames(jsonCreden)) {
                    boolean agregar = true;
                    for (String Param : JSONObject.getNames(jsonParam)) {
                        if (Param.equals(Creden)) {
                            resp.put(Param, jsonParam.get(Param));
                            agregar = false;
                            break;
                        }
                    }
                    if (agregar) {
                        resp.put(Creden, jsonCreden.get(Creden));
                    }
                }
                JSONObject aux = resp;
                for (String Param : JSONObject.getNames(jsonParam)) {
                    boolean agregar = true;
                    for (String Aux : JSONObject.getNames(aux)) {
                        if (Aux.equals(Param)) {
                            agregar = false;
                            break;
                        }
                    }
                    if (agregar) {
                        resp.put(Param, jsonParam.get(Param));
                    }
                }
            } else {
                resp = jsonParam;
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        }
        return resp;
    }

    public String obtenerURL(BigInteger id, BigInteger idEmp) {
        Connection con = Conexion.getConn();
        String resp = "";
        try {
            JSONArray json = new JSONArray();
            String sql = "SELECT DATOS FROM " + DEF.ESQUEMA + ".RESPONSE_EMPRESA WHERE ID = ?;";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id.toString());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONArray j = (rs.getString(1) == null || rs.getString(1).equals("") ? new JSONArray() : new JSONArray(rs.getString(1)));
                for (int i = 0; i < j.length(); i++) {
                    JSONObject aux = new JSONObject(j.get(i).toString());
                    boolean existe = false;
                    if (json.length() > 0) {
                        for (int k = 0; k < json.length(); k++) {
                            if (aux.getLong("idServicio") == json.getLong(k)) {
                                existe = true;
                                break;
                            }
                        }
                    }
                    if (!existe) {
                        json.put(aux.getLong("idServicio"));
                    }
                }
            }

            for (int i = 0; i < json.length(); i++) {
                resp = obtenerParam(new BigInteger(json.get(i).toString()), resp);
            }
            if (!resp.equals("")) {
                String uResp = "?id=" + id + "&amp;empresa=" + idEmp + "&amp;user=<em>-</em>&amp;password=<em>-</em>&amp;parametros=" + resp;
                resp = DEF.DOMINIO_WEB_SERV + "" + uResp;
                sql = "UPDATE " + DEF.ESQUEMA + ".RESPONSE_EMPRESA SET URL = ? WHERE ID = ?;";
                PreparedStatement pst2 = con.prepareStatement(sql);
                pst2.setString(1, uResp);
                pst2.setString(2, id.toString());
                pst2.executeUpdate();
            }
            return resp;

        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
        return "";
    }

    public String obtenerParam(BigInteger idServ, String comparar) {
        Connection con = Conexion.getConn();
        String resp = "";
        try {
            JSONObject json = new JSONObject();
            String sql = "SELECT credenciales FROM " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA WHERE ID = ?;";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, idServ.toString());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                json = (rs.getString(1) == null || rs.getString(1).equals("") ? new JSONObject() : new JSONObject(rs.getString(1)));
            }
            if (json.has("parametros")) {
                JSONArray jParam = json.getJSONArray("parametros");
                for (int i = 0; i < jParam.length(); i++) {
                    if (!comparar.contains(jParam.getString(i))) {
                        resp += (!comparar.equals("") ? "," : "") + jParam.getString(i) + ":<em>-</em>" + ((i + 1) == jParam.length() ? "" : ",");
                    }
                }
            }
            return resp;
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
        return "";
    }

    public boolean validarUSer(String user, String pass, BigInteger id, BigInteger idEmp) {
        boolean val = false;
        Connection con = Conexion.getConn();
        try {
            BigInteger bid = new BigInteger("0");
            String sql = "SELECT ID FROM " + DEF.ESQUEMA + ".USUARIO WHERE usuario=? AND password=? AND estado = 1 AND id_empresa = ?;";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, user);
            pst.setString(2, pass);
            pst.setString(3, idEmp.toString());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                bid = new BigInteger(rs.getString(1));
            }
            sql = "SELECT * FROM " + DEF.ESQUEMA + ".USUARIO_has_RESPONSE WHERE USUARIO=? AND RESPONSE=?;";
            PreparedStatement pst1 = con.prepareStatement(sql);
            pst1.setString(1, bid.toString());
            pst1.setString(2, id.toString());
            ResultSet rs2 = pst1.executeQuery();
            while (rs2.next()) {
                val = true;
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
        return val;
    }

    public JSONObject buscarDatUser(String user, String pass, BigInteger IDeMP) {
        JSONObject rsp = new JSONObject();
        Connection con = Conexion.getConn();
        try {
            String sql = "SELECT US.nombre, CU.historial, CU.ID FROM " + DEF.ESQUEMA + ".USUARIO US\n"
                    + "JOIN " + DEF.ESQUEMA + ".cuentaEmpresa CU ON (US.id_empresa = CU.ID)\n"
                    + "WHERE US.usuario = ? AND US.password = ? AND id_empresa = ?;";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, user);
            pst.setString(2, pass);
            pst.setString(3, IDeMP.toString());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                rsp.put("user", rs.getString(1));
                rsp.put("hist", rs.getInt(2));
                rsp.put("idEmp", rs.getLong(3));
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
        return rsp;
    }

    public JSONArray obtenerRuta(long id) {
        Connection con = Conexion.getConn();
        JSONArray resp = new JSONArray();
        try {
            String sql = "SELECT NOMBRE, URL, ID FROM " + DEF.ESQUEMA + ".RESPONSE_EMPRESA WHERE EMPRESA = ?;";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject j = new JSONObject();
                j.put("nombre", rs.getString(1));
                j.put("ruta", (DEF.DOMINIO_WEB_SERV + "" + rs.getString(2)));
                j.put("id", rs.getLong(3));
                JSONArray user = new JSONArray();
                String sql2 = "SELECT US.usuario FROM " + DEF.ESQUEMA + ".USUARIO_has_RESPONSE UR JOIN " + DEF.ESQUEMA + ".USUARIO US ON (UR.USUARIO = US.ID)\n"
                        + "WHERE UR.RESPONSE = ?";
                PreparedStatement pst2 = con.prepareStatement(sql2);
                pst2.setLong(1, rs.getLong(3));
                ResultSet rs2 = pst2.executeQuery();
                while (rs2.next()) {
                    user.put(rs2.getString(1));
                }
                j.put("user", user);
                resp.put(j);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
        return resp;
    }

}
