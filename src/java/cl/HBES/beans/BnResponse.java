/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.beans;

import cl.HBES.BDD.Conexion;
import static cl.HBES.beans.BnDatos.buscarEnCache;
import cl.HBES.soporte.DEF;
import cl.HBES.soporte.Soporte;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Desarrollador
 */
public class BnResponse {

    public static void main(String[] args) throws JSONException {
        String rut = "10454144-5";
        String datos = "[{\"idOrigen\":30,\"idServicio\":2206,\"idVariable\":0,\"idHijo\":0},{\"idOrigen\":1,\"idServicio\":2171,\"idVariable\":0,\"idHijo\":0}]";
        JSONArray json = new JSONArray(datos);
        JSONObject serv = obtenerServicios(json);
//        new BnResponse().datos(serv, "464654654", 1);
    }
    public static ExecutorService executor;

    public JSONObject buscarDatos(JSONArray servicios, String rut, long idEmpresa, String user, int historial, String parametrosWeb, boolean web) {
        JSONObject json = new JSONObject();
        try {
            String token = new Soporte().generateToken();
            JSONObject origen = new JSONObject();
            if (web) {
                origen = obtenerServicios(servicios);
            } else {
                origen = obtenerServicios2(servicios);
            }
            int cont = 0;
            for (String name : JSONObject.getNames(origen)) {
                for (String name1 : JSONObject.getNames(origen.getJSONObject(name))) {
                    cont++;
                }
            }
            executor = Executors.newFixedThreadPool(cont);
            for (String name : JSONObject.getNames(origen)) {
                JSONObject serv = origen.getJSONObject(name);
                for (String name1 : JSONObject.getNames(serv)) {
                    JSONObject jsonServicios = serv.getJSONObject(name1);
                    String credenciales = jsonServicios.getString("credenciales");
                    String url = jsonServicios.getString("ip");
                    String xml = jsonServicios.getString("xml");
                    int vigencia = jsonServicios.getInt("vigencia");
                    int vigenciaCantDias = jsonServicios.getInt("vigenciaCantDias");
                    int diaVigencia = jsonServicios.getInt("diaVigencia");
                    long idServicio = jsonServicios.getLong("idServicio");
                    int tipoWS = jsonServicios.getInt("tipo_ws");
                    int activo = jsonServicios.getInt("activo");
                    int tipoResponse = jsonServicios.getInt("tipoResponse");
                    process(rut, vigencia, idEmpresa, vigenciaCantDias, diaVigencia, activo, credenciales, url, xml, user, idServicio, token, tipoWS, historial, tipoResponse, parametrosWeb, web);
                }
            }
            executor.shutdown();
            Thread.sleep(1000);
            json = datos(origen, token, idEmpresa, web);
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnResponse.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
        }
        return json;
    }

    public static JSONObject obtenerServicios(JSONArray servicios) {
        Connection con = Conexion.getConn();
        try {
            JSONObject json2 = new JSONObject();
            for (int i = 0; i < servicios.length(); i++) {
                JSONObject j = new JSONObject(servicios.get(i).toString());
                String query = "SELECT SER.ip, SER.vigencia_tipo, SER.vigencia_cant, SER.vigencia_dia, SER.credenciales, SER.xml, SER.contador, SER.limite_contador, SER.nombre, \n"
                        + "ORI.DESCRIPCION, SER.tipo_ws, SER.response\n"
                        + "FROM " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA SER JOIN " + DEF.ESQUEMA + ".ORIGEN ORI ON (SER.id_origen = ORI.ID)\n"
                        + "WHERE SER.ID = ? AND SER.activo = 1;";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setLong(1, j.getLong("idServicio"));
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    JSONObject json = new JSONObject();
                    if (json2.length() > 0) {
                        for (String name : JSONObject.getNames(json2)) {
                            if (rs.getString(10).equalsIgnoreCase(name)) {
                                json = json2.getJSONObject(name);
                                json2.remove(name);
                            }
                        }
                    }
                    JSONObject var = new JSONObject();
                    JSONObject varHij = new JSONObject();
                    JSONArray vari = new JSONArray();
                    JSONArray variHijo = new JSONArray();
                    query = "SELECT NOMBRE, VARIABLE FROM " + DEF.ESQUEMA + ".VARIABLE WHERE ID_VARIABLE = ?";
                    PreparedStatement pst2 = con.prepareStatement(query);
                    pst2.setLong(1, j.getLong("idVariable"));
                    ResultSet rs2 = pst2.executeQuery();
                    while (rs2.next()) {
                        var.put("busqueda", rs2.getString(1));
                        var.put("nombre", rs2.getString(2));
                    }
                    if (j.getLong("idHijo") != 0) {
                        query = "SELECT NOMBRE, VARIABLE FROM " + DEF.ESQUEMA + ".VARIABLE_HIJO WHERE ID = ?";
                        PreparedStatement pst3 = con.prepareStatement(query);
                        pst3.setLong(1, j.getLong("idHijo"));
                        ResultSet rs3 = pst3.executeQuery();
                        while (rs3.next()) {
                            varHij.put("busqueda", rs3.getString(1));
                            varHij.put("nombre", rs3.getString(2));
                        }
                    }
                    boolean contiene = false;
                    if (json.length() > 0) {
                        for (String name : JSONObject.getNames(json)) {
                            JSONObject js = new JSONObject(json.get(name).toString());
                            if (j.getLong("idServicio") == js.getLong("idServicio")) {
                                contiene = true;
                                JSONArray v = js.getJSONArray("variable");
                                if (var.length() > 0) {
//                                var.put("busqueda", "-");
//                                var.put("nombre", "-");
                                    boolean contieneVar = false;
                                    for (int l = 0; l < v.length(); l++) {
                                        JSONObject jv = new JSONObject(v.get(l).toString());
                                        if (jv.getString("busqueda").equalsIgnoreCase(var.getString("busqueda"))) {
                                            contieneVar = true;
                                            variHijo = jv.getJSONArray("varHijo");
                                            if (varHij.length() > 0) {
                                                variHijo.put(varHij);
                                            }
                                            jv.remove("varHijo");
                                            jv.put("varHijo", variHijo);
                                            v.remove(l);
                                            v.put(jv);
                                            break;
                                        }
                                    }
                                    if (varHij.length() > 0 && !contieneVar) {
                                        variHijo.put(varHij);
                                    }
                                    if (!contieneVar) {
                                        var.put("varHijo", variHijo);
                                        v.put(var);
                                    }
                                }
                                js.remove("variable");
                                js.put("variable", v);
                                json.remove(name);
                                json.put(name, js);
                                json2.put(rs.getString(10), json);
                                break;
                            }
                        }
                    }

                    if (!contiene) {
                        JSONObject jsonTipo = new JSONObject();
                        jsonTipo.put("ip", rs.getString(1));
                        jsonTipo.put("vigencia", rs.getInt(2));
                        jsonTipo.put("vigenciaCantDias", rs.getInt(3));
                        jsonTipo.put("diaVigencia", rs.getInt(4));
                        jsonTipo.put("credenciales", rs.getString(5));
                        jsonTipo.put("xml", rs.getString(6));
                        if (rs.getInt(8) != 0) {
                            if (rs.getInt(7) == rs.getInt(8)) {
                                jsonTipo.put("activo", 0);
                            } else {
                                jsonTipo.put("activo", 1);
                            }
                        } else {
                            jsonTipo.put("activo", 1);
                        }
//                        jsonTipo.put("nombre", rs.getString(9));
                        jsonTipo.put("tipo_ws", rs.getString(11));
                        jsonTipo.put("tipoResponse", rs.getInt(12));
                        jsonTipo.put("idServicio", j.getLong("idServicio"));
                        if (var.length() == 0) {
                            var.put("busqueda", "-");
                            var.put("nombre", "-");
                        }
                        if (varHij.length() > 0) {
                            variHijo.put(varHij);
                        }
                        var.put("varHijo", variHijo);
                        vari.put(var);
                        jsonTipo.put("variable", vari);
                        json.put(rs.getString(9), jsonTipo);
                        json2.put(rs.getString(10), json);
                    }
                }
            }
            return json2;
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnResponse.class.getName(), ex.toString()});
        } finally {
            Conexion.desconectar(con);
        }
        return new JSONObject();
    }

    public Future<Boolean> process(String rut, int vigencia, long idEmpresa, int vigenciaCantDias, int diaVigencia, int activo, String credenciales, String url, String xml, String emailUsuario, long idServicio, String token, int tipoWS, int historial, int tipoResponse, String parametrosWeb, boolean web) {
        return executor.submit(() -> {
            try {
                switch (new BnDatos().verificarVigencia(rut, vigencia, idEmpresa, vigenciaCantDias, diaVigencia, activo, idServicio, parametrosWeb, web)) {
                    case 1: {
                        buscarEnServicios(rut, idEmpresa, credenciales, url, xml, emailUsuario, idServicio, token, tipoWS, tipoResponse, parametrosWeb, web);
                        break;
                    }
                    case 2: {
                        //REGISTRADO EN CACHE VIGENTE
                        buscarEnCache(rut, idEmpresa, idServicio, token, emailUsuario, parametrosWeb, web);
                        break;
                    }
                    case 3: {
                        //REGISTRADO EN CACHE NO VIGENTE
                        buscarEnServiciosYActualizarCache(rut, idEmpresa, credenciales, url, xml, emailUsuario, historial, idServicio, token, tipoWS, tipoResponse, parametrosWeb, web);
                        break;
                    }
                }
            } catch (Exception ex) {
                Soporte.severe("{0}:{1}", new Object[]{BnResponse.class.getName(), ex.toString()});
            }
            return true;
        });
    }

    public static void buscarEnServicios(String rut, long idEmpresa, String credenciales, String url, String xml, String emailUsuario, long idServicio, String token, int tipoWS, int tipoResponse, String parametrosWeb, boolean web) {
        JSONObject json = new JSONObject();
//CONSULTA SERVICIOS           
        if (tipoWS == 1) {
            json = BnDatos.obtenerDatosSoap(rut, credenciales, url, xml, tipoResponse, parametrosWeb, web);
        } else {
            json = BnDatos.obtenerDatosRest(rut, credenciales, url, xml, tipoResponse, parametrosWeb, web);
        }
//GUARDA EN CACHE
        BigInteger idConsulta = BnDatos.guardarEnCache(rut, json.toString(), idEmpresa, emailUsuario, idServicio, parametrosWeb, web);
        if (!idConsulta.equals(new BigInteger("0"))) {
            BnDatos.guardarRegistroConsulta(idEmpresa, idConsulta, emailUsuario, token, 1);
            BnDatos.actualizarContador(idServicio);
        }
    }

    public static void buscarEnServiciosYActualizarCache(String rut, long idEmpresa, String credenciales, String url, String xml, String emailUsuario, int historia, long idServicio, String token, int tipoWS, int tipoResponse, String parametrosWeb, boolean web) {
        try {
            JSONObject respuesta = new JSONObject();
            //CONSULTA SERVICIOS                
            if (tipoWS == 1) {
                respuesta = BnDatos.obtenerDatosSoap(rut, credenciales, url, xml, tipoResponse, parametrosWeb, web);
            } else {
                respuesta = BnDatos.obtenerDatosRest(rut, credenciales, url, xml, tipoResponse, parametrosWeb, web);
            }
            //GUARDA EN CACHE     
            BigInteger idConsulta;
            if (historia == 1) {
                idConsulta = BnDatos.guardarEnCache(rut, respuesta.toString(), idEmpresa, emailUsuario, idServicio, parametrosWeb, web);
            } else {
                idConsulta = BnDatos.actualizarEnCache(rut, respuesta.toString(), emailUsuario, idEmpresa, idServicio, parametrosWeb, web);
            }
            if (!idConsulta.equals(new BigInteger("0"))) {
                BnDatos.guardarRegistroConsulta(idEmpresa, idConsulta, emailUsuario, token, 1);
                BnDatos.actualizarContador(idServicio);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnResponse.class.getName(), ex.toString()});
        }
    }

    public static JSONObject datos(JSONObject origen, String token, long idEmpresa, boolean web) {
        JSONObject json = new JSONObject();
        int limit = 10;
        try {
            JSONObject respVari = new JSONObject();
            for (String name : JSONObject.getNames(origen)) {
                JSONObject serv = origen.getJSONObject(name);
                JSONObject resp = new JSONObject();
                for (String name2 : JSONObject.getNames(serv)) {
                    JSONObject js = serv.getJSONObject(name2);
                    int cont = 0;
                    String datos = BnDatos.buscarDatosServicios(token, idEmpresa, js.getLong("idServicio"));
                    if (datos.equalsIgnoreCase("SIN DATOS")) {
                        while (cont < limit) {
                            Thread.sleep(5000);
                            datos = BnDatos.buscarDatosServicios(token, idEmpresa, js.getLong("idServicio"));
                            if (datos.equalsIgnoreCase("SIN DATOS")) {
                                cont++;
                            } else if (datos.equalsIgnoreCase("ERROR")) {
                                datos = "{}";
                                cont = 10;
                            } else {
                                cont = 10;
                            }
                        }
                    } else if (datos.equalsIgnoreCase("ERROR")) {
                        datos = "{}";
                    }
                    if (js.has("variable")) {
                        JSONArray variables = js.getJSONArray("variable");
                        respVari = new BnResponse().datosResponse(variables, datos, respVari, js.getLong("idServicio"));
                    } else {
                        resp.put(name2, datos);
                    }
                }
                if (!web) {
                    json.put(name, resp);
                }
            }
            if (web) {
                json = respVari;
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnResponse.class.getName(), ex.toString()});
        }
        return json;
    }

    public JSONObject datosResponse(JSONArray vari, String datos, JSONObject respVari, long idServicio) {
        JSONObject respuesta = new JSONObject();
        try {
            if (vari.length() > 0) {
                if (vari.toString().contains("-")) {
                    vari = buscarTodasVaria(idServicio);
                }
                for (int k = 0; k < vari.length(); k++) {
                    JSONObject js = new JSONObject(vari.get(k).toString());
                    String variable = js.getString("busqueda");
                    String nombre = js.getString("nombre");
                    JSONArray varHijos = js.getJSONArray("varHijo");
                    String da = Soporte.buscarEnJSONv2(datos, variable);
                    if (varHijos.length() > 0) {
                        da = Soporte.buscarHijos(da, varHijos);
                        respVari.put(variable, new JSONArray(da));
                    } else {
                        if (da.equals("")) {
                            respVari.put(variable, da);
                        } else {
                            Object objeto = new JSONTokener(da).nextValue();
                            if (objeto instanceof JSONArray) {
                                JSONArray variab = new JSONArray();
                                JSONArray aux = new JSONArray(da);
                                for (int i = 0; i < aux.length(); i++) {
                                    Object objeto2 = new JSONTokener(aux.get(i).toString()).nextValue();
                                    if (objeto2 instanceof JSONObject) {
                                        JSONObject vari2 = new JSONObject();
                                        for (String c : JSONObject.getNames(new JSONObject(aux.get(i).toString()))) {
                                            vari2.put(c, new JSONObject(aux.get(i).toString()).get(c));
                                        }
                                        variab.put(vari2);
                                    } else {
                                        variab.put(aux.get(i));
                                    }
                                }
                                respVari.put(variable, variab);
                            } else if (objeto instanceof JSONObject) {
                                JSONObject vari3 = new JSONObject();
                                for (String c : JSONObject.getNames(new JSONObject(da))) {
                                    vari3.put(c, new JSONObject(da).get(c));
                                }
                                respVari.put(variable, vari3);
                            } else {
                                respVari.put(variable, da);
                            }
                        }
                    }
                }
                respuesta = respVari;
            } else {
                respuesta = new JSONObject(datos);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnResponse.class.getName(), ex.toString()});
        }
        return respuesta;
    }

    public static JSONObject obtenerServicios2(JSONArray servicios) {
        Connection con = Conexion.getConn();
        try {
            JSONObject json2 = new JSONObject();
            for (int i = 0; i < servicios.length(); i++) {
                JSONObject json = new JSONObject();
                String query = "SELECT SER.ip, SER.vigencia_tipo, SER.vigencia_cant, SER.vigencia_dia, SER.credenciales, SER.xml, SER.contador, SER.limite_contador, SER.nombre, \n"
                        + "ORI.DESCRIPCION, SER.tipo_ws, SER.response\n"
                        + "FROM " + DEF.ESQUEMA + ".SERV_ORIGEN_EMPRESA SER JOIN " + DEF.ESQUEMA + ".ORIGEN ORI ON (SER.id_origen = ORI.ID)\n"
                        + "WHERE SER.ID = ? AND SER.activo = 1;";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setLong(1, servicios.getLong(i));
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    JSONObject jsonTipo = new JSONObject();
                    jsonTipo.put("ip", rs.getString(1));
                    jsonTipo.put("vigencia", rs.getInt(2));
                    jsonTipo.put("vigenciaCantDias", rs.getInt(3));
                    jsonTipo.put("diaVigencia", rs.getInt(4));
                    jsonTipo.put("credenciales", rs.getString(5));
                    jsonTipo.put("xml", rs.getString(6));
                    if (rs.getInt(8) != 0) {
                        if (rs.getInt(7) == rs.getInt(8)) {
                            jsonTipo.put("activo", 0);
                        } else {
                            jsonTipo.put("activo", 1);
                        }
                    } else {
                        jsonTipo.put("activo", 1);
                    }
                    jsonTipo.put("tipo_ws", rs.getString(11));
                    jsonTipo.put("tipoResponse", rs.getInt(12));
                    jsonTipo.put("idServicio", servicios.getLong(i));
                    if (json2.length() == 0) {
                        json.put(rs.getString(9), jsonTipo);
                        json2.put(rs.getString(10), json);
                    } else {
                        boolean entro = false;
                        for (String x : JSONObject.getNames(json2)) {
                            if (rs.getString(10).equalsIgnoreCase(x)) {
                                JSONObject j = json2.getJSONObject(x);
                                j.put(rs.getString(9), jsonTipo);
                                json2.remove(x);
                                json2.put(x, j);
                                entro = true;
                                break;
                            }
                        }
                        if (!entro) {
                            json.put(rs.getString(9), jsonTipo);
                            json2.put(rs.getString(10), json);
                        }
                    }
                }
            }
            return json2;
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnResponse.class.getName(), ex.toString()});
        } finally {
            Conexion.desconectar(con);
        }
        return new JSONObject();
    }

    public JSONObject obtenerDatosWeb(BigInteger idRespon, String parametrosWeb, String user, String pass, BigInteger IDeMP) {
        JSONObject resp = new JSONObject();
        Connection con = Conexion.getConn();
        try {
            JSONArray servicios = new JSONArray();
            String query = "SELECT DATOS FROM  " + DEF.ESQUEMA + ".RESPONSE_EMPRESA WHERE ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, idRespon.toString());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                servicios = new JSONArray(rs.getString(1));
            }
            JSONObject j = new BnDatos().buscarDatUser(user, pass, IDeMP);
            resp = buscarDatos(servicios, "", j.getLong("idEmp"), j.getString("user"), j.getInt("hist"), parametrosWeb, true);
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnResponse.class.getName(), ex.toString()});
        } finally {
            Conexion.desconectar(con);
        }
        return resp;
    }

    public String buscarNombreResp(BigInteger id) {
        Connection con = Conexion.getConn();
        String nom = "";
        try {
            String query = "SELECT NOMBRE FROM  " + DEF.ESQUEMA + ".RESPONSE_EMPRESA WHERE ID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, id.toString());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                nom = rs.getString(1);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnResponse.class.getName(), ex.toString()});
        } finally {
            Conexion.desconectar(con);
        }
        return nom;
    }

    public JSONArray buscarTodasVaria(long id) {
        Connection con = Conexion.getConn();
        JSONArray resp = new JSONArray();
        try {
            String query = "";
            JSONArray respAux = new JSONArray();
            query = "SELECT NOMBRE, VARIABLE, ID_VARIABLE FROM  " + DEF.ESQUEMA + ".VARIABLE WHERE TIPO_SERVICIO = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                JSONObject j = new JSONObject();
                j.put("busqueda", rs.getString(1));
                j.put("nombre", rs.getString(2));
                j.put("id", rs.getLong(3));
                respAux.put(j);
            }
            for (int i = 0; i < respAux.length(); i++) {
                JSONObject jresp = new JSONObject();
                JSONArray respAuxH = new JSONArray();
                JSONObject j = new JSONObject(respAux.get(i).toString());
                query = "SELECT NOMBRE, VARIABLE FROM  " + DEF.ESQUEMA + ".VARIABLE_HIJO WHERE VARIABLE_PADRE = ?";
                PreparedStatement pst2 = con.prepareStatement(query);
                pst2.setLong(1, j.getLong("id"));
                ResultSet rs2 = pst2.executeQuery();
                while (rs2.next()) {
                    JSONObject jH = new JSONObject();
                    jH.put("busqueda", rs2.getString(1));
                    jH.put("nombre", rs2.getString(2));
                    respAuxH.put(jH);
                }
                jresp.put("busqueda", j.getString("busqueda"));
                jresp.put("nombre", j.getString("nombre"));
                jresp.put("varHijo", respAuxH);
                resp.put(jresp);
            }

        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnResponse.class.getName(), ex.toString()});
        } finally {
            Conexion.desconectar(con);
        }
        return resp;
    }

}
