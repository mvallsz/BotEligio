/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.slv;

import cl.HBES.beans.BnDatos;
import cl.HBES.beans.BnUsuario;
import cl.HBES.beans.BnResponse;
import cl.HBES.beans.BnServicios;
import cl.HBES.soporte.Soporte;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Manuel Valls
 */
@WebServlet(name = "Svl_Datos", urlPatterns = {"/Svl_Datos"})
public class Svl_Datos extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String accion = request.getParameter("accion");
            JSONObject json = new JSONObject();
            switch (accion) {
                case "buscarDatos": {
                    BnResponse respo = new BnResponse();
                    BnDatos dat = new BnDatos();
                    String r = request.getParameter("rut").replace(".", "");
                    String dv = request.getParameter("dv");
                    String rut = "{\"rut\": " + r + ", \"dv\": \"" + dv + "\"}";
                    JSONArray servicios = new JSONArray(request.getParameter("servicios"));
                    long idEmp = Long.parseLong(request.getParameter("emp"));
                    JSONObject j = dat.buscarNom(Soporte.getUsuarioSesion(request).getId());
                    JSONObject datos = respo.buscarDatos(servicios, rut, idEmp, j.getString("user"), j.getInt("hist"), "", false);
                    if (datos.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", datos);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "guardarWS": {
                    BigInteger idEmp = new BigInteger(request.getParameter("emp"));
                    JSONArray user = new JSONArray(request.getParameter("usuarios"));
                    String servicios = request.getParameter("servicios");
                    String nombre = request.getParameter("nombre");
                    BigInteger id = new BnDatos().guardarWS(servicios, nombre, idEmp);
                    boolean agre = false;
                    if (user.length() > 0) {
                        for (int i = 0; i < user.length(); i++) {
                            agre = new BnUsuario().insertUser_response(id, new BigInteger(user.get(i).toString()));
                        }
                    }
                    if (agre) {
                        String url = new BnDatos().obtenerURL(id, idEmp);
                        json.put("estado", 200);
                        json.put("url", url);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "listarOrigenes": {
                    long empresa = Long.parseLong(request.getParameter("id_empresa"));
                    JSONArray datos = new BnDatos().buscarOrigen(empresa);
                    if (datos.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", datos);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "listarServWeb": {
                    long empresa = Long.parseLong(request.getParameter("emp"));
                    JSONArray datos = new BnServicios().listarSer(empresa);
                    if (datos.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", datos);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "listarWebServise": {
                    long empresa = Long.parseLong(request.getParameter("idEmp"));
                    JSONObject jsonR = new BnDatos().obtenerRuta(empresa);
                    if (jsonR.getJSONArray("rutas").length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", jsonR.getJSONArray("rutas"));
                        json.put("parametros", jsonR.getJSONObject("parametros"));
                    } else {
                        json.put("estado", 400);
                    }
                    response.getWriter().print(json);
                    break;
                }

            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(Svl_Datos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(Svl_Datos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
