/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import cl.HBES.beans.BnDatos;
import cl.HBES.beans.BnResponse;
import cl.HBES.soporte.Soporte;
import java.math.BigInteger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Desarrollador
 */
@Path("generic")
public class GenericResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }

    /**
     * Retrieves representation of an instance of WebService.GenericResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getXml(@QueryParam("id") String id, @QueryParam("empresa") String ideMP, @QueryParam("user") String user, @QueryParam("password") String password, @QueryParam("parametros") String parametros) throws JSONException {
        JSONObject jsonResp = new JSONObject();
        try {
            if (id.equals("") || id.equals("0")) {
                jsonResp.put("estado", 201);
                jsonResp.put("descripcion", "ID vacio o incorrecto");
            } else if (ideMP.equals("") || ideMP.equals("0")) {
                jsonResp.put("estado", 202);
                jsonResp.put("descripcion", "Empresa vacio o incorrecto");
            } else {
                boolean validar = ValidarErrores(user, password, parametros);
                if (validar) {
                    jsonResp.put("estado", 203);
                    jsonResp.put("descripcion", "Algunos de los parametros de entrada contiene el signo de Interrogación");
                } else {
                    if (new BnDatos().validarUSer(user, password, new BigInteger(id), new BigInteger(ideMP))) {
                        JSONObject parametrosWeb = new JSONObject();
                        String[] param = parametros.split(",");
                        for (int i = 0; i < param.length; i++) {
                            parametrosWeb.put(param[i].split(":")[0], param[i].split(":")[1]);
                        }
                        String nombre = new BnResponse().buscarNombreResp(new BigInteger(id));
                        JSONObject j = new BnResponse().obtenerDatosWeb(new BigInteger(id), parametrosWeb.toString(), user, password, new BigInteger(ideMP));

                        int estado = 200;
                        if (j.toString().contains("estadoError")) {
                            jsonResp.put("datos", Soporte.buscarEnJSONv2(j.toString(), "txtError"));
                            estado = 500;
                        } else {
                            JSONObject jresp = new JSONObject();
                            jresp.put(nombre, j);
                            jsonResp.put("datos", jresp);
                        }

                        jsonResp.put("estado", estado);
                    } else {
                        jsonResp.put("estado", 300);
                        jsonResp.put("descripcion", "Usuario o Contraseña Incorrecta");
                    }
                }
            }
        } catch (Exception ex) {
            Soporte.severe("{0}:{1}", new Object[]{GenericResource.class.getName(), ex.toString()});
            ex.printStackTrace(System.out);
            jsonResp.put("estado", 400);
        }
        return jsonResp.toString();
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }

    public boolean ValidarErrores(String user, String pass, String param) {
        boolean resp = false;
        if (user.equals("?")) {
            resp = true;
        } else {
            if (pass.equals("?")) {
                resp = true;
            } else {
                if (param.contains("?")) {
                    resp = true;
                }
            }
        }
        return resp;
    }
}
