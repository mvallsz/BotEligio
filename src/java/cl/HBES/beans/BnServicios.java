/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.beans;

import cl.HBES.BDD.Conexion;
import cl.HBES.soporte.DEF;
import cl.HBES.soporte.Soporte;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
//        boolean json = new BnServicios().eliminarServicio(35);
//        JSONArray json = new BnServicios().serviciosActivos(1);
        JSONObject json = new BnServicios().consultaHistorialMes(1);
    }

    public boolean activarServicio(long idSEr, int activar) {
        boolean act = false;
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "UPDATE " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA SET activo=? WHERE ID=?;";
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

    public JSONArray buscarServicios(long empresa) {
        JSONArray json = new JSONArray();
        Connection conn = null;
        try {
            conn = Conexion.getConn();
            String sql = "SELECT SER.ID, SER.nombre, SER.ip, SER.vigencia_tipo, SER.vigencia_cant, SER.vigencia_dia, SER.id_origen, BU.DESCRIPCION, SER.activo, \n"
                    + "SER.credenciales, SER.contador, SER.limite_contador, SER.xml, SER.tipo_rut, SER.tipo_ws \n"
                    + "FROM " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA SER JOIN " + DEF.ESQUEMA + ".ORIGEN BU ON (SER.id_origen = BU.ID) \n"
                    + "WHERE SER.id_empresa = ? \n"
                    + "ORDER BY ID ASC;";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setLong(1, empresa);
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
}
