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

    public static JSONObject obtenerDatosSoap(String rut, String parametros, String url, String xml, int tipoResponse) {
        JSONObject respuesta = new JSONObject();
        try {
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(rut, parametros, xml), url);
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

    private static SOAPMessage createSOAPRequest(String rut, String credenciales, String xml) throws JSONException {

        SOAPMessage soapMessage = null;
        try {
            String message = "";
            String[] xmlS = xml.split("\n");
            JSONObject json = new JSONObject(credenciales);
            for (int i = 0; i < xmlS.length; i++) {
                String texto = xmlS[i];
                for (String name : JSONObject.getNames(json)) {
                    if (texto.contains(name)) {
                        String valor = json.get(name).toString();
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
                        texto = texto.replace("?", valor);
                        break;
                    }
                }
                if (texto.contains("?")) {
                    texto = texto.replace("?", "");
                }
                message = message + (message == "" ? texto : texto + "\n");
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

    public static JSONObject obtenerDatosRest(String rut, String credenciales, String url, String xml, int tipoResponse) {
        JSONObject respuesta = new JSONObject();
        try {
            int cont = 1;
            String Get = url;
            JSONObject json = new JSONObject(credenciales);
            for (String name : JSONObject.getNames(json)) {
                if (cont == 1) {
                    Get = Get + "?" + name + "=";
                    cont++;
                } else {
                    Get = Get + "&" + name + "=";
                }
                String valor = json.get(name).toString();
                if (valor.equalsIgnoreCase("dv") || valor.equalsIgnoreCase("rut") || valor.equalsIgnoreCase("rut-dv") || valor.equalsIgnoreCase("rutdv")) {                    //RUT SIEMPRE VENDRA CON EN -
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

    public static int verificarVigencia(String rut, int vigencia, long idEmpresa, int vigenciaCantDias, int diaVigencia, int isActivo, long idServi) {
        int cantidad = 1;//NO ESTA REGISTRADO EN CACHE
        Connection con = Conexion.getConn();
        try {

            String[] r = rut.split("-");
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            Date fechaActual = new Date();
            Date fechaUltima = null;

            String query = "SELECT UPDATED_DATE FROM " + DEF.ESQUEMA + ".DATA_RESPONSE WHERE RUT = ? AND ID_EMPRESA = ? AND SERVICIO = ? ORDER BY FECHA DESC LIMIT 1";
            PreparedStatement pst2 = con.prepareStatement(query);
            pst2.setString(1, r[0]);
            pst2.setLong(2, idEmpresa);
            pst2.setLong(3, idServi);
            ResultSet rs1 = pst2.executeQuery();
            if (rs1.next()) {
                fechaUltima = rs1.getDate("UPDATED_DATE");
            }

            if (fechaUltima == null) {
                query = "SELECT FECHA FROM " + DEF.ESQUEMA + ".DATA_RESPONSE WHERE RUT = ? AND ID_EMPRESA = ? AND SERVICIO = ? ORDER BY FECHA DESC LIMIT 1";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, r[0]);
                pst.setLong(2, idEmpresa);
                pst.setLong(3, idServi);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    fechaUltima = rs.getDate("FECHA");
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
        } catch (SQLException ex) {
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

    public static void buscarEnCache(String rut, long idEmpresa, long idServicio, String token, String user) {
        System.out.println("Buscando en Cache " + idServicio);
        String[] r = rut.split("-");
        Connection con = Conexion.getConn();
        try {
            PreparedStatement pst;
            ResultSet rs;
            String query = "SELECT ID, DATA FROM " + DEF.ESQUEMA + ".DATA_RESPONSE WHERE RUT = ? AND SERVICIO = ? AND ID_EMPRESA = ? ORDER BY FECHA DESC LIMIT 1";
            pst = con.prepareStatement(query);
            pst.setString(1, r[0]);
            pst.setLong(2, idServicio);
            pst.setLong(3, idEmpresa);
            rs = pst.executeQuery();
            if (rs.next()) {
                BigInteger idConsulta = BigInteger.valueOf(rs.getLong("ID"));
                guardarRegistroConsulta(idEmpresa, idConsulta, user, token, 2);
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

    public static BigInteger guardarEnCache(String rut, String data, long idEmpresa, String emailUsuario, long idServi) {
        System.out.println("Guardar en Cache " + idServi);
        data = data.replace("\\\"", "");
        Connection con = Conexion.getConn();
        try {
            String[] r = rut.split("-");
            Statement stmt = null;
            ResultSet rs;
            stmt = con.createStatement();
            if (data.length() > 2) {
                stmt.executeUpdate("INSERT INTO " + DEF.ESQUEMA + ".DATA_RESPONSE\n"
                        + "(ID_EMPRESA, RUT, DV, `DATA`, SERVICIO, FECHA, CREATED_BY, CREATED_DATE)\n"
                        + "VALUES( " + idEmpresa + ", '" + r[0] + "', '" + r[1] + "', '" + data + "','" + idServi + "', CURRENT_TIMESTAMP,'" + emailUsuario + "', CURRENT_TIMESTAMP);", Statement.RETURN_GENERATED_KEYS);
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    java.math.BigInteger idConsulta = rs.getBigDecimal(1).toBigInteger();
                    return idConsulta;
                }
                return new BigInteger("0");
            }
        } catch (SQLException ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
        return new BigInteger("0");
    }

    public static BigInteger actualizarEnCache(String rut, String data, String correo_usuario, long idEmpresa, long idServicio) {
        System.out.println("Actualizar datos Cache");
        data = data.replace("\\\"", "");
        Connection con = Conexion.getConn();
        try {
            String[] r = rut.split("-");
            if (data.length() > 2) {
                String query = "SELECT ID FROM " + DEF.ESQUEMA + ".DATA_RESPONSE WHERE RUT = ?  AND ID_EMPRESA = ? AND SERVICIO = ? ORDER BY FECHA DESC LIMIT 1;";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, r[0]);
                pst.setLong(2, idEmpresa);
                pst.setLong(3, idServicio);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    query = "UPDATE " + DEF.ESQUEMA + ".DATA_RESPONSE SET `DATA`=?, UPDATED_BY= ?, UPDATED_DATE=CURRENT_TIMESTAMP \n"
                            + "WHERE ID=? AND ID_EMPRESA= ? AND  SERVICIO = ? AND RUT=?";
                    PreparedStatement pst1 = con.prepareStatement(query);
                    pst1.setString(1, data);
                    pst1.setString(2, correo_usuario);
                    pst1.setLong(3, rs.getLong(1));
                    pst1.setLong(4, idEmpresa);
                    pst1.setLong(5, idServicio);
                    pst1.setString(6, r[0]);
                    pst1.executeUpdate();
                    return new BigInteger(rs.getString("ID"));
                }
                return new BigInteger("0");
            }
        } catch (SQLException ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
        return new BigInteger("0");
    }

    public static String buscarDatosServicios(String token, long idEmpresa, String rut, long idServicio) throws IOException, JSONException {
        String datos = "";
        Connection con = Conexion.getConn();
        try {
            String[] r = rut.split("-");
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
                query = "SELECT `DATA` FROM " + DEF.ESQUEMA + ".DATA_RESPONSE WHERE ID = ? AND RUT = ?;";
                PreparedStatement pst2 = con.prepareStatement(query);
                pst2.setLong(1, rs.getLong(1));
                pst2.setString(2, r[0]);
                ResultSet rs2 = pst2.executeQuery();
                while (rs2.next()) {
                    datos = rs2.getString(1);
                }
            }
            if (datos == "") {
                datos = "SIN DATOS";
            }
        } catch (SQLException ex) {
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
                    + "JOIN HUB_DP.ORIGEN ORI ON (SER.id_origen = ORI.ID)\n"
                    + "WHERE SER.id_empresa = ? GROUP BY SER.id_origen ORDER BY SER.id_origen ASC;";//origen
            PreparedStatement pst = con.prepareStatement(query);
            pst.setLong(1, idEmpresa);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject origen = new JSONObject();
                JSONArray servicio = new JSONArray();
                origen.put("ID", rs.getLong(1));
                origen.put("NOMBRE", rs.getString(2));
                query = "SELECT ID, nombre FROM " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA WHERE id_origen = ? AND id_empresa = ?;";//servicio
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
}
