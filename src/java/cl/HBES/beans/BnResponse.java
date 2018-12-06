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

/**
 *
 * @author Desarrollador
 */
public class BnResponse {

    public static void main(String[] args) throws JSONException {
        String rut = "10454144-5";
        String datos = "[{\"idServicio\":1863,\"idVariable\":1,\"idHijo\":1,\"idOrigen\":4},{\"idServicio\":1863,\"idVariable\":1,\"idHijo\":2,\"idOrigen\":4},{\"idServicio\":1863,\"idVariable\":1,\"idHijo\":3,\"idOrigen\":4},{\"idServicio\":1867,\"idVariable\":2,\"idHijo\":0,\"idOrigen\":2},{\"idServicio\":1867,\"idVariable\":3,\"idHijo\":0,\"idOrigen\":2},{\"idServicio\":1868,\"idVariable\":4,\"idHijo\":0,\"idOrigen\":3},{\"idServicio\":1868,\"idVariable\":5,\"idHijo\":0,\"idOrigen\":3},{\"idServicio\":1872,\"idVariable\":6,\"idHijo\":0,\"idOrigen\":1},{\"idServicio\":1872,\"idVariable\":7,\"idHijo\":4,\"idOrigen\":1},{\"idServicio\":1872,\"idVariable\":7,\"idHijo\":5,\"idOrigen\":1},{\"idServicio\":1872,\"idVariable\":7,\"idHijo\":6,\"idOrigen\":1}]";
        JSONArray json = new JSONArray(datos);
        JSONObject serv = obtenerServicios(json);
        new BnResponse().datos(serv, "464654654", 1, rut);
    }
    public static ExecutorService executor;

    public JSONObject buscarDatos(JSONArray servicios, String rut, long idEmpresa, String user, int historial) {
        JSONObject json = new JSONObject();
        try {
            String token = new Soporte().generateToken();
            JSONObject origen = obtenerServicios2(servicios);
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
                    process(rut, vigencia, idEmpresa, vigenciaCantDias, diaVigencia, activo, credenciales, url, xml, user, idServicio, token, tipoWS, historial, tipoResponse);
                }
            }
            executor.shutdown();
            Thread.sleep(1000);
            json = datos(origen, token, idEmpresa, rut);
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
                    JSONArray json = new JSONArray();
                    if (json2.length() > 0) {
                        for (String name : JSONObject.getNames(json2)) {
                            if (rs.getString(10).equalsIgnoreCase(name)) {
                                json = json2.getJSONArray(name);
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
                    for (int k = 0; k < json.length(); k++) {
                        JSONObject js = new JSONObject(json.get(k).toString());
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
                            json.remove(k);
                            json.put(js);
                            json2.put(rs.getString(10), json);
                            break;
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
                        jsonTipo.put("nombre", rs.getString(9));
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
                        json.put(jsonTipo);
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

    public Future<Boolean> process(String rut, int vigencia, long idEmpresa, int vigenciaCantDias, int diaVigencia, int activo, String credenciales, String url, String xml, String emailUsuario, long idServicio, String token, int tipoWS, int historial, int tipoResponse) {
        return executor.submit(() -> {
            try {
                switch (new BnDatos().verificarVigencia(rut, vigencia, idEmpresa, vigenciaCantDias, diaVigencia, activo, idServicio)) {
                    case 1: {
                        buscarEnServicios(rut, idEmpresa, credenciales, url, xml, emailUsuario, idServicio, token, tipoWS, tipoResponse);
                        break;
                    }
                    case 2: {
                        //REGISTRADO EN CACHE VIGENTE
                        buscarEnCache(rut, idEmpresa, idServicio, token, emailUsuario);
                        break;
                    }
                    case 3: {
                        //REGISTRADO EN CACHE NO VIGENTE
                        buscarEnServiciosYActualizarCache(rut, idEmpresa, credenciales, url, xml, emailUsuario, historial, idServicio, token, tipoWS, tipoResponse);
                        break;
                    }
                }
            } catch (Exception ex) {
                Soporte.severe("{0}:{1}", new Object[]{BnResponse.class.getName(), ex.toString()});
            }
            return true;
        });
    }

    public static void buscarEnServicios(String rut, long idEmpresa, String credenciales, String url, String xml, String emailUsuario, long idServicio, String token, int tipoWS, int tipoResponse) {
        JSONObject json = new JSONObject();
//CONSULTA SERVICIOS           
        if (tipoWS == 1) {
            json = BnDatos.obtenerDatosSoap(rut, credenciales, url, xml, tipoResponse);
        } else {
            json = BnDatos.obtenerDatosRest(rut, credenciales, url, xml, tipoResponse);
        }
//GUARDA EN CACHE
        BigInteger idConsulta = BnDatos.guardarEnCache(rut, json.toString(), idEmpresa, emailUsuario, idServicio);
        if (!idConsulta.equals(new BigInteger("0"))) {
            BnDatos.guardarRegistroConsulta(idEmpresa, idConsulta, emailUsuario, token, 1);
            BnDatos.actualizarContador(idServicio);
        }
    }

    public static void buscarEnServiciosYActualizarCache(String rut, long idEmpresa, String credenciales, String url, String xml, String emailUsuario, int historia, long idServicio, String token, int tipoWS, int tipoResponse) {
        try {
            JSONObject respuesta = new JSONObject();
            //CONSULTA SERVICIOS                
            if (tipoWS == 1) {
                respuesta = BnDatos.obtenerDatosSoap(rut, credenciales, url, xml, tipoResponse);
            } else {
                respuesta = BnDatos.obtenerDatosRest(rut, credenciales, url, xml, tipoResponse);
            }
            //GUARDA EN CACHE     
            BigInteger idConsulta;
            if (historia == 1) {
                idConsulta = BnDatos.guardarEnCache(rut, respuesta.toString(), idEmpresa, emailUsuario, idServicio);
            } else {
                idConsulta = BnDatos.actualizarEnCache(rut, respuesta.toString(), emailUsuario, idEmpresa, idServicio);
            }
            if (!idConsulta.equals(new BigInteger("0"))) {
                BnDatos.guardarRegistroConsulta(idEmpresa, idConsulta, emailUsuario, token, 1);
                BnDatos.actualizarContador(idServicio);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnResponse.class.getName(), ex.toString()});
        }
    }

    public static JSONObject datos(JSONObject origen, String token, long idEmpresa, String rut) {
        JSONObject json = new JSONObject();
        int limit = 10;
        try {
            for (String name : JSONObject.getNames(origen)) {
                JSONObject serv = origen.getJSONObject(name);
                JSONObject resp = new JSONObject();
                for (String name2 : JSONObject.getNames(serv)) {
                    JSONObject js = serv.getJSONObject(name2);
                    int cont = 0;
                    String datos = BnDatos.buscarDatosServicios(token, idEmpresa, rut, js.getLong("idServicio"));
                    if (datos.equalsIgnoreCase("SIN DATOS")) {
                        while (cont < limit) {
                            Thread.sleep(5000);
                            datos = BnDatos.buscarDatosServicios(token, idEmpresa, rut, js.getLong("idServicio"));
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
//                    JSONArray variables = js.getJSONArray("variable");
//                    JSONObject d = new BnResponse().datosResponse(variables, datos);
                    resp.put(name2, datos);
                }
                json.put(name, resp);
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{BnResponse.class.getName(), ex.toString()});
        }
        return json;
    }

    public JSONObject datosResponse(JSONArray vari, String datos) {
        JSONObject respuesta = new JSONObject();
        try {
            String response = "{";
            if (vari.length() > 0) {
                for (int k = 0; k < vari.length(); k++) {
                    JSONObject js = new JSONObject(vari.get(k).toString());
                    String variable = js.getString("busqueda");
                    String nombre = js.getString("nombre");
                    JSONArray varHijos = js.getJSONArray("varHijo");
                    String da = Soporte.buscarEnJSONv2(datos, variable);
                    if (varHijos.length() > 0) {
                        da = Soporte.buscarHijos(da, varHijos);
                    }
                    response += (k != 0 ? "," : "") + "\"" + nombre + "\":" + da;
                }
                response += "}";
                respuesta = new JSONObject(response);
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

}
