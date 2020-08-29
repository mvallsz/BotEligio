package DH.DB.WSToken;

import DH.DB.BDD.Conexion;
import DH.DB.beans.BnResponse;
import DH.DB.soporte.DEF;
import DH.DB.soporte.Soporte;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("/V01")
public class RestApiController {

    @POST
    @Path("/request")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response consulta(String parametros, @Context HttpHeaders httpHeaders) {
        System.out.println("Authorization:: " + httpHeaders.getRequestHeader("Authorization").get(0));
        JSONObject jsonResult = new JSONObject();
        JSONObject jsonOut = new JSONObject();
        JSONObject jsonValoresLineas = new JSONObject();
        JSONArray jsonOperaciones = new JSONArray();
        try {
            JSONObject parametrosWeb = new JSONObject(parametros).getJSONObject("parametros");
            String id = new JSONObject(parametros).getString("idServicio");
            String ideMP = new JSONObject(parametros).getString("idEmpresa");
            String bearer = httpHeaders.getRequestHeader("Authorization").get(0);
            bearer = bearer.replace("Bearer ", "");
            String sqlToken = "select token,usuario,password from " + DEF.ESQUEMA + ".USUARIO where token ='" + bearer + "'";
            Connection con = Conexion.getConn();
            PreparedStatement ps = con.prepareCall(sqlToken);
            ResultSet rs = ps.executeQuery();
            boolean vali = false;
            String user = "";
            String password = "";
            if (rs.next()) {
                String token = rs.getString("token");
                user = rs.getString("usuario");
                password = rs.getString("password");
                token = token.replace("#a", "");
                token = token.replace("d/by%", "");
                String[] arr = token.split("e\\$c");
                long hasta = Long.valueOf(arr[1]);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                if (hasta < timestamp.getTime()) {
                    JSONObject jsonError = new JSONObject();
                    jsonError.put("error", "409 - Token expirado");
                    return Response.status(Response.Status.CONFLICT).entity(jsonError.toString()).build();
                }
            } else {
                JSONObject jsonError = new JSONObject();
                jsonError.put("error", "406 - Token invalido");
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity(jsonError.toString()).build();
            }

            ////////////////////////////////////////////////////////////////////
//            String[] param = parametros.split(",");
//            for (int i = 0; i < param.length; i++) {
//                parametrosWeb.put(param[i].split(":")[0], param[i].split(":")[1]);
//            }
//          
            String nombre = new BnResponse().buscarNombreResp(new BigInteger(id));
            JSONObject j = new BnResponse().obtenerDatosWeb(new BigInteger(id), parametrosWeb.toString(), user, password, new BigInteger(ideMP), true);

            int estado = 200;
            if (j.toString().contains("estadoError")) {
                jsonOut.put("datos", Soporte.buscarEnJSONv2(j.toString(), "txtError"));
                estado = 500;
            } else {
                JSONObject jresp = new JSONObject();
                jresp.put(nombre, j);
                jsonOut.put("datos", jresp);
            }

            jsonOut.put("estado", estado);
            ////////////////////////////////////////////////////////////////////

        } catch (Exception e) {
            System.out.println("ERROR:: " + e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(jsonOut.toString()).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Usuario usu) {
        System.out.println("usuario: " + usu.getUsuario());
        System.out.println("contraseña: " + usu.getPassword());
        JSONObject json = new JSONObject();
        int idUser = 0;
        try {
            String token = "";
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(usu.getPassword().getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            String md5 = sb.toString();
            String sql = "SELECT *\n"
                    + "FROM " + DEF.ESQUEMA + ".USUARIO where usuario='" + usu.getUsuario() + "' and password = '" + md5 + "'";
            Connection con = Conexion.getConn();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            String contra = "";
            while (rs.next()) {
                contra = rs.getString("password");
                idUser = rs.getInt("ID");
            }
            rs.close();
            st.close();
            con.close();
            if (md5.equalsIgnoreCase(contra)) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                token = "#a" + timestamp.getTime() + "e$c" + (timestamp.getTime() + 300000) + "d/by%";
                String sql2 = "update " + DEF.ESQUEMA + ".USUARIO set token = '" + token + "' where ID=?";
                con = Conexion.getConn();
                PreparedStatement ps = con.prepareCall(sql2);
                ps.setInt(1, idUser);
                ps.executeUpdate();
                json.put("token", token);
            } else {
                json.put("token", "-");
                json.put("error", "401 - Verifique los datos");
                return Response.status(Response.Status.UNAUTHORIZED).entity(json.toString()).build();
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.toString());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(json.toString()).build();
    }
}
