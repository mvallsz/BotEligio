/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.beans;

import cl.HBES.BDD.Conexion;
import cl.HBES.soporte.DEF;
import cl.HBES.soporte.Soporte;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Desarrollador
 */
public class BnServicios {

    public static void main(String[] args) {
//        boolean json = new BnServicios().insertarServicios("prueba", "prueba", 1, 222, 3, 1, 4, "prueba", 0, "xlm", 3, 1);
//        boolean json = new BnServicios().actualizarServicio(35, "pruebaaa", "pruebaaaa", 2, 225, 4, 4, "prueba", 5, "xlm", 3, 1);
//        boolean json = new BnServicios().activarServicio(35, true);
        boolean json = new BnServicios().eliminarServicio(35);
//        JSONArray json = new BnServicios().serviciosActivos(1);
    }

    public boolean activarServicio(long idSEr, boolean activar) {
        boolean act = false;
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "UPDATE " + DEF.ESQUEMA + ".SERV_BUREAU_EMPRESA SET activo=? WHERE ID=?;";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, (activar ? 1 : 0));
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

    public boolean actualizarServicio(long idSEr, String nombre, String ip, int vigencia_tipo, int vigencia_cant, int vigencia_dia, long id_bureau, String credenciales, int limite_contador, String xml, int tipo_rut, int tipo_ws) {
        boolean insert = false;
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "UPDATE " + DEF.ESQUEMA + ".SERV_BUREAU_EMPRESA SET nombre=?, ip=?, vigencia_tipo=?, vigencia_cant=?, vigencia_dia=?, \n"
                    + "id_bureau=?, credenciales=?, limite_contador=?, xml=?, tipo_rut=?, tipo_ws=? WHERE ID=?;";
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
            String sql = "DELETE FROM " + DEF.ESQUEMA + ".SERV_BUREAU_EMPRESA WHERE ID=?;";
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

    public boolean insertarServicios(String nombre, String ip, int vigencia_tipo, int vigencia_cant, int vigencia_dia, long id_empresa, long id_bureau, String credenciales, int limite_contador, String xml, int tipo_rut, int tipo_ws) {
        boolean insert = false;
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "INSERT INTO " + DEF.ESQUEMA + ".SERV_BUREAU_EMPRESA (nombre, ip, vigencia_tipo, vigencia_cant, vigencia_dia, id_empresa, id_bureau, activo, credenciales, contador, limite_contador, xml, tipo_rut, tipo_ws) \n"
                    + "VALUES(?, ?, ?, ?, ?, ?, ?, 1, ?, 0, ?, ?, ?, ?);";
            PreparedStatement pst = conn.prepareStatement(sql);
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

    public JSONArray buscarServicios(long id_emp) {
        JSONArray json = new JSONArray();
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT SER.ID, SER.nombre, SER.ip, SER.vigencia_tipo, SER.vigencia_cant, SER.vigencia_dia, SER.id_bureau, BU.DESCRIPCION, SER.activo, \n"
                    + "SER.credenciales, SER.contador, SER.limite_contador, SER.xml, SER.tipo_rut, SER.tipo_ws \n"
                    + "FROM " + DEF.ESQUEMA + ".SERV_BUREAU_EMPRESA SER JOIN " + DEF.ESQUEMA + ".BUREAUS BU ON (SER.id_bureau = BU.ID)\n"
                    + "WHERE id_empresa = ? ORDER BY ID ASC;";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setLong(1, id_emp);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject json2 = new JSONObject();
                json2.put("ID", rs.getInt(1));
                json2.put("NOMBRE", rs.getString(2));
                json2.put("IP", rs.getString(3));
                json2.put("VIGENCIA_TIPO", rs.getInt(4));
                json2.put("VIGENCIA_CANT", rs.getInt(5));
                json2.put("VIGENCIA_DIA", rs.getInt(6));
                json2.put("ID_BUREAU", rs.getInt(7));
                json2.put("NOMBRE_BUREAU", rs.getString(8));
                json2.put("ACTIVO", rs.getInt(9));
                json2.put("CREDENCIALES", rs.getString(10));
                json2.put("CONTADOR", rs.getInt(11));
                json2.put("LIMITE_CONTADOR", rs.getInt(12));
                json2.put("XML", rs.getString(13));
                json2.put("TIPO_RUT", rs.getInt(14));
                json2.put("TIPO_WS", rs.getInt(15));
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
            String sql = "SELECT SER.ID, SER.nombre, SER.ip, SER.vigencia_tipo, SER.vigencia_cant, SER.vigencia_dia, SER.id_bureau, BU.DESCRIPCION, SER.activo, \n"
                    + "SER.credenciales, SER.contador, SER.limite_contador, SER.xml, SER.tipo_rut, SER.tipo_ws \n"
                    + "FROM " + DEF.ESQUEMA + ".SERV_BUREAU_EMPRESA SER JOIN " + DEF.ESQUEMA + ".BUREAUS BU ON (SER.id_bureau = BU.ID)\n"
                    + "WHERE SER.id_empresa = ? AND SER.activo = 1 ORDER BY ID ASC;";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setLong(1, id_emp);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject json2 = new JSONObject();
                json2.put("ID", rs.getInt(1));
                json2.put("NOMBRE", rs.getString(2));
                json2.put("IP", rs.getString(3));
                json2.put("VIGENCIA_TIPO", rs.getInt(4));
                json2.put("VIGENCIA_CANT", rs.getInt(5));
                json2.put("VIGENCIA_DIA", rs.getInt(6));
                json2.put("ID_BUREAU", rs.getInt(7));
                json2.put("NOMBRE_BUREAU", rs.getString(8));
                json2.put("ACTIVO", rs.getInt(9));
                json2.put("CREDENCIALES", rs.getString(10));
                json2.put("CONTADOR", rs.getInt(11));
                json2.put("LIMITE_CONTADOR", rs.getInt(12));
                json2.put("XML", rs.getString(13));
                json2.put("TIPO_RUT", rs.getInt(14));
                json2.put("TIPO_WS", rs.getInt(15));
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

}