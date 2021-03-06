/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DH.DB.beans;

import DH.DB.BDD.Conexion;
import DH.DB.soporte.DEF;
import DH.DB.soporte.Correos;
import DH.DB.soporte.Soporte;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Desarrollador
 */
public class BnServicios {

    public void notificaRPA(JSONObject exito, String to) throws JSONException{
        
        Iterator<String> keys = exito.keys();
        String cuerpo = "<html><head></head><body><h1>Se acepto la siguiente solicitud:</h1><br>";
        String subject = "Se ha aceptado una solicitud mediante el DogHound BOT Automatization System";
        while(keys.hasNext()) {
            String key = keys.next();
            //if (exito.get(key) instanceof JSONObject) {
                cuerpo = cuerpo + key + ": " + exito.get(key) + "<br>";
            //}
        }
        cuerpo = cuerpo + "</body></html>";
        Correos.SendMail(cuerpo, subject, to, DEF.CORREOADMIN);
        Soporte.info("Correo enviado con exito");
    }
    
    public BigInteger insertarResRPA(String filtro, String jsonResp, String jsonRespExito, BigInteger idHilo, int idServ) {
        BigInteger id = new BigInteger("0");
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "INSERT INTO "+DEF.ESQUEMA+".bot_resp_servicio (filtros, descripcion_json, descripcion_json_exito, id_bot_servicio, fecha_accion, id_usuario, id_hilo) VALUES(?, ?, ?, ?, CURRENT_TIMESTAMP, 0, ?);";
            PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, filtro);
            pst.setString(2, jsonResp);
            pst.setString(3, jsonRespExito);
            pst.setInt(4, idServ);
            pst.setInt(5, idHilo.intValue());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            while (rs.next()) {
                id = new BigInteger(rs.getString(1));
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return id;
    }
    
    public boolean activarServicio(long idSEr, int activar) {
        boolean act = false;
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "UPDATE " + DEF.ESQUEMA + ".bot_servicio SET estado=? WHERE id=?;";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, activar);
            pst.setLong(2, idSEr);
            pst.executeUpdate();
            act = true;
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return act;
    }

    public boolean apagarHilo(long idHilo) {
        boolean act = false;
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            
            String sql = "UPDATE " + DEF.ESQUEMA + ".bot_threads_info SET estado_hilo=0 WHERE id=?;";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setLong(1, idHilo);
            pst.executeUpdate();
            act = true;
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return act;
    }
    
    public boolean actualizarServicio(long idSEr, String nombre, String ip, int vigencia_tipo, int vigencia_cant, int vigencia_dia, long id_bureau, String credenciales, int limite_contador, String xml, int tipo_rut, int tipo_ws) {
        boolean insert = false;
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "UPDATE " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA SET nombre=?, ip=?, vigencia_tipo=?, vigencia_cant=?, vigencia_dia=?, \n"
                    + "id_origen=?, credenciales=?, limite_contador=?, xml=?, tipo_rut=?, tipo_ws=? WHERE ID=?;";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nombre);
            pst.setString(2, ip);
            pst.setInt(3, vigencia_tipo);
            pst.setInt(4, vigencia_cant);
            pst.setInt(5, vigencia_dia);
            pst.setLong(6, id_bureau);
            pst.setString(7, credenciales);
            pst.setInt(8, limite_contador);
            pst.setString(9, xml);
            pst.setInt(10, tipo_rut);
            pst.setInt(11, tipo_ws);
            pst.setLong(12, idSEr);
            pst.executeUpdate();
            insert = true;
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return insert;
    }

    public boolean eliminarServicio(long idServicio) {
        boolean delete = false;
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "DELETE FROM " + DEF.ESQUEMA + ".bot_servicio WHERE id=?;";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setLong(1, idServicio);
            pst.executeUpdate();
            delete = true;
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return delete;
    }

    public BigInteger insertarServicios(String nombre, String ip, int vigencia_tipo, int vigencia_cant, int vigencia_dia, long id_empresa, long id_bureau, String credenciales, int limite_contador, String xml, int tipo_rut, int tipo_ws,
            int tipoResponse) {
        BigInteger id = new BigInteger("0");
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "INSERT INTO " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA (nombre, ip, vigencia_tipo, vigencia_cant, vigencia_dia, id_empresa, id_origen, activo, credenciales, contador, limite_contador, xml, tipo_rut, tipo_ws, response) \n"
                    + "VALUES(?, ?, ?, ?, ?, ?, ?, 0, ?, 0, ?, ?, ?, ?, ?);";
            PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, nombre);
            pst.setString(2, ip);
            pst.setInt(3, vigencia_tipo);
            pst.setInt(4, vigencia_cant);
            pst.setInt(5, vigencia_dia);
            pst.setLong(6, id_empresa);
            pst.setLong(7, id_bureau);
            pst.setString(8, credenciales);
            pst.setInt(9, limite_contador);
            pst.setString(10, xml);
            pst.setInt(11, tipo_rut);
            pst.setInt(12, tipo_ws);
            pst.setInt(13, tipoResponse);
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            while (rs.next()) {
                id = new BigInteger(rs.getString(1));
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return id;
    }
    
    public BigInteger insertarServiciosRPA(String nombre_rpa, String US_state, String zipCodes, String appliance, String correo_notificacion, String usuario_host, String password_host) {
        BigInteger id = new BigInteger("0");
        Connection conn = null;
        try {
            conn = Conexion.getConn();

            String sql = "INSERT INTO " + DEF.ESQUEMA + ".bot_servicio (nombre, zip_codes, key_words, usuario_host, password_host, email_notificacion, fecha_creacion, id_usu_creacion, estado, us_state) \n"
                    + "VALUES(?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, 0, 0, ?);";
            PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, nombre_rpa);
            pst.setString(2, zipCodes);
            pst.setString(3, appliance);
            pst.setString(4, usuario_host);
            pst.setString(5, password_host);
            pst.setString(6, correo_notificacion);
            pst.setString(7, US_state);
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            while (rs.next()) {
                id = new BigInteger(rs.getString(1));
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return id;
    }

    public JSONArray buscarServiciosRPA() {
        JSONArray json = new JSONArray();
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT id, nombre, zip_codes, key_words, usuario_host, password_host, "
                    + "email_notificacion, fecha_creacion, estado "
                    + "FROM " + DEF.ESQUEMA + ".bot_servicio";

            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject json2 = new JSONObject();
                json2.put("id", rs.getInt(1));
                json2.put("nombre", rs.getString(2));
                json2.put("zip_codes", rs.getString(3));
                json2.put("key_words", rs.getString(4));
                json2.put("usuario_host", rs.getString(5));
                json2.put("password_host", rs.getString(6));
                json2.put("email_notificacion", rs.getString(7));
                json2.put("fecha_creacion", rs.getDate(8));
                json2.put("estado", rs.getInt(9));
                json.put(json2);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return json;
    }

    public JSONArray serviciosActivos(long id_emp) {
        JSONArray json = new JSONArray();
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT SER.id_origen, BU.DESCRIPCION\n"
                    + "FROM " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA SER JOIN " + DEF.ESQUEMA + ".ORIGEN BU ON (SER.id_origen = BU.ID)\n"
                    + "WHERE SER.id_empresa = ? \n"
                    + "AND SER.activo = 1 \n"
                    + "GROUP BY BU.DESCRIPCION\n"
                    + "ORDER BY id_origen ASC;";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setLong(1, id_emp);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject json2 = new JSONObject();
                json2.put("ID_ORIGEN", rs.getInt(1));
                json2.put("NOMBRE_ORIGEN", rs.getString(2));
                json.put(json2);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return json;
    }

    public JSONObject consultaBureauMes(long idEmpresa) {
        JSONObject act = new JSONObject();
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT ID FROM " + DEF.ESQUEMA + ".ORIGEN;";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONArray json = new BnServicios().bureauMes(idEmpresa, rs.getLong(1));
                act.put(rs.getString(1), json);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return act;
    }

    public JSONArray bureauMes(long idEmpresa, long id) {
        JSONArray act = new JSONArray();
        Connection conn = null;
        try {
            int anio = 0;
            int mes = 0;
            conn = Conexion.getConn();
            String sql = "SELECT DATE_FORMAT(CURRENT_DATE,'%m'), DATE_FORMAT(CURRENT_DATE,'%Y');";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                mes = rs.getInt(1);
                anio = rs.getInt(2);
            }
            for (int i = 1; i < 13; i++) {
                JSONObject json = new JSONObject();
                String fecha = anio + "-" + (String.valueOf(mes).length() == 1 ? "0" + mes : "" + mes);
                int cantidad = 0;
                sql = "SELECT COUNT(*) as cantpidad, DATE_FORMAT(RC.UPDATED_DATE,'%m-%Y') as fecha\n"
                        + "FROM " + DEF.ESQUEMA + ".DATA_RESPONSE RC JOIN " + DEF.ESQUEMA + ".cuentaEmpresa EMP ON (RC.ID_EMPRESA = EMP.ID)\n"
                        + "WHERE EMP.ID = ? AND DATE_FORMAT(RC.UPDATED_DATE,'%Y-%m') = ? AND ID_ORIGEN = ?\n"
                        + "GROUP BY DATE_FORMAT(RC.UPDATED_DATE,'%m-%Y');";
                PreparedStatement pst2 = conn.prepareStatement(sql);
                pst2.setLong(1, idEmpresa);
                pst2.setString(2, fecha);
                pst2.setLong(3, id);
                ResultSet rs2 = pst2.executeQuery();
                if (rs2.next()) {
                    cantidad = rs2.getInt(1);
                }
                if (cantidad == 0) {
                    sql = "SELECT COUNT(*) as cantidad\n"
                            + "FROM " + DEF.ESQUEMA + ".DATA_RESPONSE RC JOIN " + DEF.ESQUEMA + ".cuentaEmpresa EMP ON (RC.ID_EMPRESA = EMP.ID)\n"
                            + "WHERE EMP.ID = ? AND DATE_FORMAT(RC.FECHA,'%Y-%m') = ?\n"
                            + "GROUP BY DATE_FORMAT(RC.FECHA,'%m-%Y');";
                    PreparedStatement pst3 = conn.prepareStatement(sql);
                    pst3.setLong(1, idEmpresa);
                    pst3.setString(2, fecha);
                    pst3.setLong(3, id);
                    ResultSet rs3 = pst3.executeQuery();
                    if (rs3.next()) {
                        cantidad = rs3.getInt(1);
                    }
                }

                json.put("fecha", fecha);
                json.put("cantidad", cantidad);
                mes--;
                if (mes == 1) {
                    mes = 12;
                    anio = anio - 1;
                }
                act.put(json);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return act;
    }

    public JSONObject consultaHistorialMes(long idEmpresa) {
        JSONObject act = new JSONObject();
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT ID, NOMBRE FROM " + DEF.ESQUEMA + ".TIPO_DATO_CONSULTA;";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONArray json = new BnServicios().historialMes(idEmpresa, rs.getLong(1));
                act.put(rs.getString(2), json);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return act;
    }

    public JSONArray listarEmpresas() {
        JSONArray empresas = new JSONArray();
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT ID, nom_empresa FROM " + DEF.ESQUEMA + ".cuentaEmpresa WHERE ID != 0 ORDER BY nom_empresa ASC;";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject empresa = new JSONObject();
                empresa.put("ID", rs.getLong(1));
                empresa.put("nom_empresa", rs.getString(2));
                empresas.put(empresa);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return empresas;
    }
    
    public JSONArray listarEstados() {
        JSONArray estados = new JSONArray();
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT id, name FROM " + DEF.ESQUEMA + ".us_states WHERE ID != 0 ORDER BY id ASC;";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject estado = new JSONObject();
                estado.put("id", rs.getLong(1));
                estado.put("name", rs.getString(2));
                estados.put(estado);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return estados;
    }

    public JSONArray consultasServAct() {
        JSONArray servicios = new JSONArray();
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT BT.id, BS.nombre, BS.key_words, BS.zip_codes, BS.email_notificacion, BT.fecha_creacion, COUNT(HP.id) AS ejecuciones FROM "+DEF.ESQUEMA+".bot_threads_info BT \n" +
                        "LEFT JOIN "+DEF.ESQUEMA+".sy_performace_info HP ON BT.id = HP.id_servicio\n" +
                        "INNER JOIN "+DEF.ESQUEMA+".bot_servicio BS ON BT.id_bot_servicio = BS.id \n" +
                        "WHERE BT.estado_hilo = 1 GROUP BY HP.id_servicio, BS.nombre, BS.key_words, BS.zip_codes, BS.email_notificacion, BT.fecha_creacion, BT.id;";
            
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject servicio = new JSONObject();
                servicio.put("id", rs.getLong(1));
                servicio.put("nombre", rs.getString(2));
                servicio.put("key_words", rs.getString(3));
                servicio.put("zip_codes", rs.getString(4));
                servicio.put("email_notification", rs.getString(5));
                servicio.put("fecha_creacion", rs.getString(6));
                servicio.put("ejecuciones", rs.getLong(7));
                servicios.put(servicio);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return servicios;
    }
    
    public JSONArray consultasServHistorico(BigInteger idServ) {
        JSONArray servicios = new JSONArray();
        Connection conn = null;
        String search = "";
        
        if(!idServ.equals(BigInteger.ZERO)){
            search = " AND BS.id = "+idServ;
        }
        
        try {
            conn = Conexion.getConn();
            String sql = "SELECT BT.id, BS.nombre, BS.key_words, BS.zip_codes, BT.fecha_creacion, BT.fecha_fin ,\n" +
                        "(SELECT COUNT(id) FROM "+DEF.ESQUEMA+".bot_resp_servicio BRS WHERE BRS.id_hilo = BT.id AND BRS.descripcion_json_exito <> '[]') AS ejecuciones_exitosas, \n" +
                        "(SELECT COUNT(id) FROM "+DEF.ESQUEMA+".bot_resp_servicio BRS WHERE BRS.id_hilo = BT.id) AS ejecuciones \n" +
                        "FROM "+DEF.ESQUEMA+".bot_threads_info BT \n" +
                        "LEFT JOIN "+DEF.ESQUEMA+".sy_performace_info HP ON BT.id = HP.id_servicio\n" +
                        "INNER JOIN "+DEF.ESQUEMA+".bot_servicio BS ON BT.id_bot_servicio = BS.id\n" +
                        "WHERE BT.estado_hilo <> 1 "+search+" GROUP BY BT.id, BS.nombre, BS.key_words, BS.zip_codes, BT.fecha_creacion;";
            
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject servicio = new JSONObject();
                servicio.put("id", rs.getLong(1));
                servicio.put("nombre", rs.getString(2));
                servicio.put("key_words", rs.getString(3));
                servicio.put("zip_codes", rs.getString(4));
                servicio.put("fecha_creacion", rs.getString(5));
                servicio.put("fecha_fin", rs.getString(6));
                servicio.put("ejecuciones_exitosas", rs.getLong(7));
                servicio.put("ejecuciones", rs.getLong(8));
                servicios.put(servicio);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return servicios;
    }
    
    public JSONArray consultasServDisp() {
        JSONArray servicios = new JSONArray();
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT id, nombre, zip_codes, key_words, usuario_host, password_host, email_notificacion, fecha_creacion, id_usu_creacion, estado FROM "+DEF.ESQUEMA+".bot_servicio WHERE estado = 1";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject servicio = new JSONObject();
                servicio.put("id", rs.getLong(1));
                servicio.put("nombre", rs.getString(2));
                servicio.put("zip_codes", rs.getString(3));
                servicio.put("key_words", rs.getString(4));
                servicio.put("usuario_host", rs.getString(5));
                servicio.put("password_host", rs.getString(6));
                servicio.put("email_notification", rs.getString(7));
                servicio.put("fecha_creacion", rs.getString(8));
                servicio.put("id_usu_creacion", rs.getLong(9));
                servicio.put("estado", rs.getInt(10));
                servicios.put(servicio);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return servicios;
    }
    
    public JSONObject consultasServ(BigInteger idServ) {
        Connection conn = null;
        JSONObject servicio = null;
            
        try {
            conn = Conexion.getConn();
            String sql = "SELECT id, nombre, zip_codes, key_words, usuario_host, password_host, email_notificacion, fecha_creacion, id_usu_creacion, estado, us_state FROM "+DEF.ESQUEMA+".bot_servicio WHERE id = ?";
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setLong(1, idServ.longValue());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                servicio = new JSONObject();
                servicio.put("id", rs.getLong(1));
                servicio.put("nombre", rs.getString(2));
                servicio.put("zip_codes", rs.getString(3));
                servicio.put("key_words", rs.getString(4));
                servicio.put("usuario_host", rs.getString(5));
                servicio.put("password_host", rs.getString(6));
                servicio.put("email_notification", rs.getString(7));
                servicio.put("fecha_creacion", rs.getString(8));
                servicio.put("id_usu_creacion", rs.getLong(9));
                servicio.put("estado", rs.getInt(10));
                servicio.put("us_state", rs.getString(11));
            }
        } catch (SQLException | JSONException ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return servicio;
    }
    
    public BigInteger actualizarEstadosHilo (Thread hiloAct, int estadoAct, int ejecuciones, int tipo, BigInteger id, int idServ){
        
        BigInteger idRegistro = id;
        Connection conn = null;
        
        try {
            conn = Conexion.getConn();
            
            switch(tipo){
                case 1:{
                    String sql = "INSERT INTO " + DEF.ESQUEMA + ".bot_threads_info "
                    + "(nombre_hilo, id_bot_servicio, num_ejecuciones, fecha_creacion, fecha_fin, estado_hilo, id_usuario) "
                    + "VALUES(?, ?, ?, CURRENT_TIMESTAMP, NULL, ?, 0);";
        
                    PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    pst.setString(1, hiloAct.getName());
                    pst.setInt(2, idServ);
                    pst.setInt(3, ejecuciones);
                    pst.setInt(4, estadoAct);
                    pst.executeUpdate();
                    ResultSet rs = pst.getGeneratedKeys();
                    if(rs.next()) {
                        idRegistro = new BigInteger(rs.getString(1));
                    }
                    break;
                }
                case 2:{
                    String sql = "UPDATE " + DEF.ESQUEMA + ".bot_threads_info "
                            + "SET num_ejecuciones = ?,  fecha_fin = CURRENT_TIMESTAMP, estado_hilo = ? WHERE id = ?;";
                    
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setInt(1, ejecuciones);
                    pst.setInt(2, estadoAct);
                    pst.setInt(3, idRegistro.intValue());
                    pst.executeUpdate();
                    
                    break;
                }       
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return idRegistro;
    }
    
    public int checkService(BigInteger idHilo) {
        int resp = 0;
        
        Connection con = Conexion.getConn();
        try {
            String sql = "SELECT estado_hilo FROM " + DEF.ESQUEMA + ".bot_threads_info WHERE id=?;";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setLong(1, idHilo.longValue());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                resp = rs.getInt("estado_hilo"); 
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnDatos.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(con);
        }
        return resp;
    }
    
    public void insertarPerformance(long duration, String proceso, String clase, BigInteger idHilo, int idServicio ){
        
        Connection conn = null;
        
        try {
            conn = Conexion.getConn();
            String sql = "INSERT INTO " + DEF.ESQUEMA + ".sy_performace_info (duration, nombre_proceso, nombre_clase, id_usuario, id_servicio, id_hilo) "
                    + "VALUES(?, ?, ?, 0, ?, ?);";

            PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setLong(1, duration);
            pst.setString(2, proceso);
            pst.setString(3, clase);
            pst.setInt(4, idServicio);
            pst.setInt(5, idHilo.intValue());
            pst.executeUpdate();

        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        
    }
    
    public JSONArray historialMes(long idEmpresa, long buscarDatos) {
        JSONArray act = new JSONArray();
        Connection conn = null;
        try {
            int anio = 0;
            int mes = 0;
            conn = Conexion.getConn();
            String sql = "SELECT DATE_FORMAT(CURRENT_DATE,'%m'), DATE_FORMAT(CURRENT_DATE,'%Y');";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                mes = rs.getInt(1);
                anio = rs.getInt(2);
            }
            for (int i = 1; i < 13; i++) {
                JSONObject json = new JSONObject();
                String fecha = anio + "-" + (String.valueOf(mes).length() == 1 ? "0" + mes : "" + mes);
                int cantidad = 0;
                sql = "SELECT COUNT(*) as cantidad\n"
                        + "FROM " + DEF.ESQUEMA + ".REGISTRO_CONSULTAS RC JOIN " + DEF.ESQUEMA + ".cuentaEmpresa EMP ON (RC.ID_EMPRESA = EMP.ID)\n"
                        + "WHERE EMP.ID = ? AND DATE_FORMAT(RC.FECHA,'%Y-%m') = ? AND RC.BUSCAR_DATOS = ?\n"
                        + "GROUP BY DATE_FORMAT(RC.FECHA,'%m-%Y');";
                PreparedStatement pst2 = conn.prepareStatement(sql);
                pst2.setLong(1, idEmpresa);
                pst2.setString(2, fecha);
                pst2.setLong(3, buscarDatos);
                ResultSet rs2 = pst2.executeQuery();
                if (rs2.next()) {
                    cantidad = rs2.getInt(1);
                }
                json.put("fecha", fecha);
                json.put("cantidad", cantidad);
                mes--;
                if (mes == 1) {
                    mes = 12;
                    anio = anio - 1;
                }
                act.put(json);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return act;
    }

    public JSONArray consultasMes(long idEmpresa, int mes, int anio, int bureau) {
        JSONArray consultas = new JSONArray();
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT RC.ID, RC.ID_EMPRESA, SER.nombre, ORI.DESCRIPCION, RC.FECHA, RC.USUARIO, RC.ID_DATA_RESPONSE, RC.TOKEN, RC.BUSCAR_DATOS\n"
                    + "FROM " + DEF.ESQUEMA + ".REGISTRO_CONSULTAS RC JOIN  " + DEF.ESQUEMA + ".DATA_RESPONSE DATR ON (RC.ID_DATA_RESPONSE = DATR.ID)\n"
                    + "JOIN " + DEF.ESQUEMA + ".DATA_RESPONSE DAT ON (DATR.ID = DAT.ID)\n"
                    + "JOIN " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA SER ON (DAT.SERVICIO = SER.ID)\n"
                    + "JOIN  " + DEF.ESQUEMA + ".ORIGEN ORI ON (SER.id_origen = ORI.ID)\n"
                    + "WHERE MONTH(RC.FECHA) = ? AND YEAR(RC.FECHA) = ? AND RC.ID_EMPRESA = ?";
            if (bureau != 0) {
                sql = sql + " \n AND ORI.ID = ?";
            }
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, mes);
            pst.setLong(2, anio);
            pst.setLong(3, idEmpresa);
            if (bureau != 0) {
//                String nomBureau = "";
//                String sqlB = "SELECT * FROM " + DEF.ESQUEMA + ".ORIGEN \n"
//                        + "WHERE ID = ?";
//                PreparedStatement pstB = conn.prepareStatement(sqlB);
//                pstB.setInt(1, bureau);
//                ResultSet rsB = pstB.executeQuery();
//                while (rsB.next()) {
//                    nomBureau = rsB.getString(2);
//                }
                pst.setInt(4, bureau);
            }
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject consulta = new JSONObject();
                consulta.put("ID", rs.getLong(1));
                consulta.put("ID_EMPRESA", rs.getLong(2));
                consulta.put("SERVICIO", rs.getString(3));
                consulta.put("ORIGEN", rs.getString(4));
                consulta.put("FECHA", rs.getString(5));
                consulta.put("USUARIO", rs.getString(6));
                consulta.put("ID_DATA_RESPONSE", rs.getLong(7));
                consulta.put("TOKEN", rs.getString(8));
                consulta.put("BUSCAR_DATOS", rs.getInt(9));
                consultas.put(consulta);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return consultas;
    }

    public JSONArray listarOrigenes(long idEmp) {
        JSONArray origen = new JSONArray();
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String id = "0," + idEmp;
            String sql = "SELECT ID, DESCRIPCION FROM " + DEF.ESQUEMA + ".ORIGEN WHERE EMPRESA IN (" + id + ");";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject origenn = new JSONObject();
                origenn.put("ID", rs.getLong(1));
                String descripcion = rs.getString(2).substring(0, 1) + "" + rs.getString(2).substring(1, rs.getString(2).length()).toLowerCase();
                origenn.put("nombre_origen", descripcion);
                origen.put(origenn);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return origen;
    }

    public JSONArray listarOrigenes2(long idEmp) {
        JSONArray origen = new JSONArray();
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT SER.id_origen, ORI.DESCRIPCION FROM " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA SER \n"
                    + "JOIN " + DEF.ESQUEMA + ".ORIGEN ORI ON (SER.id_origen = ORI.ID)"
                    + "WHERE SER.id_empresa = ?\n"
                    + "GROUP BY SER.id_origen ORDER BY SER.id_origen ASC;";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setLong(1, idEmp);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject origenn = new JSONObject();
                origenn.put("ID", rs.getLong(1));
                String descripcion = rs.getString(2).substring(0, 1) + "" + rs.getString(2).substring(1, rs.getString(2).length()).toLowerCase();
                origenn.put("nombre_origen", descripcion);
                origen.put(origenn);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return origen;
    }

    public boolean insertarOrigen(String nombre, long empr) {
        boolean insert = false;
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "INSERT INTO " + DEF.ESQUEMA + ".ORIGEN (DESCRIPCION, EMPRESA) \n"
                    + "VALUES(?,?);";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nombre.toUpperCase());
            pst.setLong(2, empr);
            pst.executeUpdate();
            insert = true;
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return insert;
    }

    public JSONArray listarTipoDato() {
        JSONArray tipo = new JSONArray();
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT ID_TIPO, NOMBRE FROM " + DEF.ESQUEMA + ".TIPO_DATO;";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject dato = new JSONObject();
                dato.put("ID", rs.getLong(1));
                dato.put("NOMBRE", rs.getString(2).toUpperCase());
                tipo.put(dato);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return tipo;
    }

    public long guardarVariables(BigInteger id, int tipoRut, JSONObject j) {
        long idvar = 0;

        try {
            if (!j.getString("VARIABLE").equals("")) {
                Connection conn = null;
                try {
                    conn = Conexion.getConn();
                    String sql = "INSERT INTO " + DEF.ESQUEMA + ".VARIABLE (NOMBRE, VARIABLE, TIPO, TIPO_DATO, TIPO_SERVICIO) VALUES(?, ?, ?, ?, ?);";
                    PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    pst.setString(1, j.getString("NOMBRE"));
                    pst.setString(2, j.getString("VARIABLE"));
                    pst.setString(3, (tipoRut == 1 ? "N" : (tipoRut == 2 ? "J" : "A")));
                    pst.setLong(4, j.getLong("TIPODATO"));
                    pst.setString(5, id.toString());
                    pst.executeUpdate();
                    ResultSet rs = pst.getGeneratedKeys();
                    while (rs.next()) {
                        idvar = rs.getLong(1);
                    }
                } catch (Exception ex) {
                    Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
                    ex.printStackTrace(System.out);
                } finally {
                    Conexion.desconectar(conn);
                }
            }
        } catch (Exception e) {
        }
        return idvar;
    }

    public boolean guardarVariablesHijo(long id, JSONObject j) {
        boolean result = false;
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "INSERT INTO " + DEF.ESQUEMA + ".VARIABLE_HIJO (NOMBRE, VARIABLE, TIPO_DATO, VARIABLE_PADRE) VALUES(?, ?, ?, ?);";
            PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, j.getString("NOMBRE"));
            pst.setString(2, j.getString("VARIABLE"));
            pst.setLong(3, j.getLong("TIPODATO"));
            pst.setLong(4, id);
            pst.executeUpdate();
            result = true;
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return result;
    }

    public JSONObject listarServicioDefault() {
        JSONObject json = new JSONObject();
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT DESCRIPCION, ID FROM " + DEF.ESQUEMA + ".ORIGEN WHERE ID IN (1,2,3,4);";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONArray servicios = new JSONArray();
                String sql1 = "SELECT ID, nombre, ip, xml, tipo_rut, tipo_ws, response, credenciales\n"
                        + "FROM " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA WHERE id_empresa = 0 AND id_origen = ?;";
                PreparedStatement pst1 = conn.prepareStatement(sql1);
                pst1.setLong(1, rs.getLong(2));
                ResultSet rs1 = pst1.executeQuery();
                while (rs1.next()) {
                    JSONObject j = new JSONObject();
                    JSONArray var = new JSONArray();
                    j.put("ID", rs1.getLong(1));
                    j.put("NOMBRE", rs1.getString(2));
                    j.put("IP", rs1.getString(3));
                    j.put("XML", rs1.getString(4));
                    j.put("TIPO_RUT", rs1.getInt(5));
                    j.put("TIPO_WS", rs1.getInt(6));
                    j.put("RESPONSE", rs1.getInt(7));
                    String sql2 = "SELECT NOMBRE, VARIABLE, TIPO_DATO, ID_VARIABLE FROM " + DEF.ESQUEMA + ".VARIABLE WHERE TIPO_SERVICIO = ?";
                    PreparedStatement pst2 = conn.prepareStatement(sql2);
                    pst2.setLong(1, rs1.getLong(1));
                    ResultSet rs2 = pst2.executeQuery();
                    while (rs2.next()) {
                        JSONArray varH = new JSONArray();
                        JSONObject k = new JSONObject();
                        k.put("NOMBRE", rs2.getString(1));
                        k.put("VARIABLE", rs2.getString(2));
                        k.put("TIPO_DATO", rs2.getInt(3));
                        String sql3 = "SELECT NOMBRE, VARIABLE, TIPO_DATO FROM " + DEF.ESQUEMA + ".VARIABLE_HIJO WHERE VARIABLE_PADRE = ?";
                        PreparedStatement pst3 = conn.prepareStatement(sql3);
                        pst3.setLong(1, rs2.getLong(4));
                        ResultSet rs3 = pst3.executeQuery();
                        while (rs3.next()) {
                            JSONObject h = new JSONObject();
                            h.put("NOMBRE", rs3.getString(1));
                            h.put("VARIABLE", rs3.getString(2));
                            h.put("TIPO_DATO", rs3.getInt(3));
                            varH.put(h);
                        }
                        k.put("VARIHIJO", varH);
                        var.put(k);
                    }
                    j.put("VARI", var);
                    JSONObject creden = (rs1.getString(8) != null ? (rs1.getString(8).equals("") ? new JSONObject() : new JSONObject(rs1.getString(8))) : new JSONObject());
                    j.put("CREDEN", creden);
                    servicios.put(j);
                }
                json.put(rs.getString(1), servicios);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnServicios.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return json;
    }

    public JSONArray listarSer(long idEmpresa) {
        JSONArray resp = new JSONArray();
        Connection con = Conexion.getConn();
        try {
            String sql = "SELECT SER.ID, SER.nombre, ORI.DESCRIPCION, SER.activo, SER.tipo_rut FROM " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA SER\n"
                    + "JOIN " + DEF.ESQUEMA + ".ORIGEN ORI ON (SER.id_origen = ORI.ID) WHERE ORI.EMPRESA=0 AND SER.id_empresa = ?;";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setLong(1, idEmpresa);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject json = new JSONObject();
                json.put("ID", rs.getString(1));
                json.put("NOMBRE", rs.getString(2));
                json.put("ORIGEN", rs.getString(3));
                json.put("ACTIVO", rs.getInt(4));
                json.put("TIPO", rs.getInt(5));
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

//    public static void main(String[] args) {
////        boolean json = new BnServicios().insertarServicios("prueba", "prueba", 1, 222, 3, 1, 4, "prueba", 0, "xlm", 3, 1);
////        boolean json = new BnServicios().actualizarServicio(35, "pruebaaa", "pruebaaaa", 2, 225, 4, 4, "prueba", 5, "xlm", 3, 1);
////        boolean json = new BnServicios().activarServicio(35, true);
////        boolean json = new BnServicios().eliminarServicio(35);
////        JSONArray json = new BnServicios().serviciosActivos(1);
//        JSONObject json = new BnServicios().consultaHistorialMes(1);
//    }
    
}
