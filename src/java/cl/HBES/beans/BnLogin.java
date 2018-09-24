/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.beans;

import cl.HBES.BDD.Conexion;
import cl.HBES.clases.CredencialesUsuario;
import cl.HBES.clases.CuentaEmpresa;
import cl.HBES.soporte.DEF;
import cl.HBES.soporte.Soporte;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Desarrollador
 */
public class BnLogin {

    public CredencialesUsuario iniciarSesion(String user, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        CredencialesUsuario credenciales = null;
        try {
            String sql = "SELECT US.ID, US.nombre, US.apellido, US.email, EMP.ID, EMP.cod_subsidiary, EMP.nom_empresa, EMP.rut, EMP.dv, EMP.historial\n"
                    + "FROM " + DEF.ESQUEMA + ".CREDENCIALES_USUARIO US JOIN " + DEF.ESQUEMA + ".cuentaEmpresa EMP ON (US.id_empresa = EMP.ID)\n"
                    + "WHERE US.email = ? AND US.pass = ? LIMIT 1;";

            conn = Conexion.getConn();
            pst = conn.prepareStatement(sql);
            pst.setString(1, user);
            pst.setString(2, password);
            rs = pst.executeQuery();

            while (rs.next()) {
                credenciales = new CredencialesUsuario();
                credenciales.setId(rs.getInt(1));

                CuentaEmpresa empresa = new CuentaEmpresa();

                credenciales.setEmpresa(empresa);

            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnLogin.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
//        SoporteExpertChoice.info("Proceso iniciarSesion, devuelve: "+usuario == null ? "null": usuario.toString());
        return credenciales;
    }

    public long obtenerIDEmpresa() {
        long id = 0;
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT ID FROM " + DEF.ESQUEMA + ".cuentaEmpresa;";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                id = rs.getLong(1);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnLogin.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        } finally {
            Conexion.desconectar(conn);
        }
        return id;
    }
}
