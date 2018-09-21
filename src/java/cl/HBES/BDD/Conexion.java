/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.BDD;

import cl.HBES.soporte.DEF;
import cl.HBES.soporte.Soporte;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Desarrollador
 */
public class Conexion extends Soporte{

    /**
     *
     * @return
     */
    public static Connection getConn() {
        Connection con = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DEF.DIRECCION, DEF.USER, DEF.PASSWORD);
            con.setAutoCommit(false);

        } catch (ClassNotFoundException | SQLException ex) {
            severe("{0}:{1}", new Object[]{Conexion.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        }

        return con;

    }

    /**
     *
     * @param con
     * @return
     */
    public static boolean desconectar(Connection con) {
        boolean flag = true;
        String conexion = null;
        try {
            if (con == null || con.isClosed()) {
                return flag;
            }
            conexion = con.toString();
            if (con.isReadOnly()) {
                con.rollback();
            } else {
                if (!con.getAutoCommit()) {
                    con.commit();
                }
            }

            con.close();

        } catch (Exception ex) {
            flag = false;
            severe("{0}:{1}", new Object[]{Conexion.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        }
        return flag;
    }
}
