/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DH.DB.beans;

import DH.DB.BDD.Conexion;
import DH.DB.clases.CredencialesUsuario;
import DH.DB.clases.CuentaEmpresa;
import DH.DB.clases.Usuario;
import DH.DB.soporte.DEF;
import DH.DB.soporte.Soporte;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author mvalls
 */
public class BnUser {

    public CuentaEmpresa getEmpresa(BigInteger id){
        
        CuentaEmpresa empresa = new CuentaEmpresa();
        Connection conn = null;
        
        try {
            conn = Conexion.getConn();
            String sql = "SELECT razon_social, cod_empresa FROM " + DEF.ESQUEMA + ".sy_empresa WHERE id = ?;";
                  
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setLong(1, id.longValue());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                empresa.setId(id);
                empresa.setCod_empresa(rs.getString(2));
                empresa.setNombre(rs.getString(1));
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnUser.class.getName(), ex.toString()});
            ex.printStackTrace();
        } finally {
            Conexion.desconectar(conn);
        }
        return empresa;
    }
    
    public Usuario iniciarSesion(String user, String password) throws SQLException {
        Connection conn = null;
        Usuario credenciales = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT id, nombres, apellidos, email, id_empresa\n" +
                         "FROM " + DEF.ESQUEMA + ".sy_usuario\n" +
                         "WHERE usuario = ? and password = ? LIMIT 1";
                  
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, user);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                credenciales = new Usuario();
                credenciales.setId(Soporte.getBigInteger(rs, 1));
                credenciales.setNombre(rs.getString(2));
                credenciales.setApellido(rs.getString(3));
                credenciales.setEmail(rs.getString(4));
                credenciales.setUsuario(user);
                CuentaEmpresa empresa = getEmpresa(BigInteger.valueOf(rs.getLong(5)));
                credenciales.setEmpresa(empresa);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnUser.class.getName(), ex.toString()});
            ex.printStackTrace();
        } finally {
            Conexion.desconectar(conn);
        }
        return credenciales;
    }

    public long obtenerIDEmpresa() {
        long id = 0;
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT ID FROM " + DEF.ESQUEMA + ".cuentaEmpresa LIMIT 1;";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                id = rs.getLong(1);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnUser.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return id;
    }

    public int agregarUser(String nombre, String user, String password, BigInteger idEmp) {
        int resp = 0;
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT * FROM " + DEF.ESQUEMA + ".USUARIO WHERE usuario = ? AND id_empresa = ?;";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, user);
            pst.setString(2, idEmp.toString());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                resp = 2;
            } else {
                sql = "INSERT INTO " + DEF.ESQUEMA + ".USUARIO (nombre, usuario, password, id_empresa, estado)\n"
                        + "VALUES(?, ?, md5(?), ?, 1);";
                PreparedStatement pst1 = conn.prepareStatement(sql);
                pst1.setString(1, nombre);
                pst1.setString(2, user);
                pst1.setString(3, password);
                pst1.setString(4, idEmp.toString());
                pst1.executeUpdate();
                resp = 1;
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnUser.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return resp;
    }

    public JSONArray listUser(BigInteger idEmp) {
        JSONArray resp = new JSONArray();
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT ID, nombre FROM " + DEF.ESQUEMA + ".USUARIO WHERE id_empresa = ? AND estado = 1;";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, idEmp.toString());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject j = new JSONObject();
                j.put("id", rs.getString(1));
                j.put("nombre", rs.getString(2));
                resp.put(j);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnUser.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return resp;
    }

    public boolean insertUser_response(BigInteger idRespo, BigInteger idUser) {
        boolean resp = false;
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "INSERT INTO " + DEF.ESQUEMA + ".USUARIO_HAS_RESPONSE (USUARIO, RESPONSE)\n"
                    + "VALUES(?, ?);";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, idUser.toString());
            pst.setString(2, idRespo.toString());
            pst.executeUpdate();
            resp = true;
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnUser.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return resp;
    }

    public JSONArray listarUsuarios(BigInteger idEmp) {
        JSONArray resp = new JSONArray();
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT ID, nombre, usuario, estado FROM " + DEF.ESQUEMA + ".USUARIO WHERE id_empresa = ?;";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, idEmp.toString());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject j = new JSONObject();
                j.put("id", rs.getString(1));
                j.put("nombre", rs.getString(2));
                j.put("user", rs.getString(3));
                j.put("estado", rs.getString(4));
                resp.put(j);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnUser.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return resp;
    }

    public boolean editarUsuarios(String nombre, String user, String password, BigInteger idUser, int estado) {
        boolean resp = false;
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "UPDATE " + DEF.ESQUEMA + ".USUARIO SET nombre=?, usuario=?, password=md5(?), estado=? WHERE ID=?;";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nombre);
            pst.setString(2, user);
            pst.setString(3, password);
            pst.setInt(4, estado);
            pst.setString(5, idUser.toString());
            pst.executeUpdate();
            resp = true;
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnUser.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return resp;
    }

    public boolean bloquearUsuarios(BigInteger idUser, int estado) {
        boolean resp = false;
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "UPDATE " + DEF.ESQUEMA + ".USUARIO SET estado = ? WHERE ID = ?;";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, estado);
            pst.setString(2, idUser.toString());
            pst.executeUpdate();
            resp = true;
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnUser.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return resp;
    }

    public JSONObject obtenerSerU(String user, String pass) {
        JSONObject rsp = new JSONObject();
        Connection conn = null;
        try {
            BigInteger idd = new BigInteger("0");
            conn = Conexion.getConn();
            String sql = "SELECT ID FROM " + DEF.ESQUEMA + ".USUARIO WHERE usuario = ? AND password = ?;";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, user);
            pst.setString(2, pass);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                idd = new BigInteger(res.getString(1));
            }
            JSONArray resp = new JSONArray();
            sql = "SELECT RESPONSE FROM " + DEF.ESQUEMA + ".USUARIO_HAS_RESPONSE WHERE USUARIO = ?;";
            PreparedStatement pst1 = conn.prepareStatement(sql);
            pst1.setString(1, idd.toString());
            ResultSet res1 = pst1.executeQuery();
            while (res1.next()) {
                resp.put(res1.getString(1));
            }
            rsp.put("id", idd);
            rsp.put("datos", resp);

        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnUser.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return rsp;
    }
}
