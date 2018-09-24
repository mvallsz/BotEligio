/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.slv;

import cl.HBES.beans.BnLogin;
import cl.HBES.beans.BnServicios;
import cl.HBES.clases.CredencialesUsuario;
import cl.HBES.soporte.Soporte;
import java.io.IOException;
import java.io.PrintWriter;
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
 * @author Desarrollador
 */
@WebServlet(name = "Svl_Servicios", urlPatterns = {"/Svl_Servicios"})
public class Svl_Servicios extends HttpServlet {

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
            BnServicios bn = new BnServicios();
            long id_empresa = new BnLogin().obtenerIDEmpresa();
//            CredencialesUsuario user = Soporte.getUsuarioSesion(request);
            switch (accion) {
                case "agregarServicio": {
                    String nombre = request.getParameter("nombre");
                    int tipoPersona = Integer.parseInt(request.getParameter("tipoPersona"));
                    long bureau = Long.parseLong(request.getParameter("bureau"));
                    int tipoServicio = Integer.parseInt(request.getParameter("tipoServicio"));
                    String url = request.getParameter("url");
                    int tipoVigencia = Integer.parseInt(request.getParameter("tipoVigencia"));
                    int cantDias = Integer.parseInt(request.getParameter("cantDias"));
                    int diaVigencia = Integer.parseInt(request.getParameter("diaVigencia"));
                    String xml = request.getParameter("xml");
                    int limiteContador = Integer.parseInt(request.getParameter("limiteContador"));
                    String credenciales = request.getParameter("json");

                    if (bn.insertarServicios(nombre, url, tipoVigencia, cantDias, diaVigencia, id_empresa, bureau, credenciales, limiteContador, xml, tipoPersona, tipoServicio)) {
                        json.put("estado", 200);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "actualizarServicio": {
                    String nombre = request.getParameter("nombre");
                    int tipoPersona = Integer.parseInt(request.getParameter("tipoPersona"));
                    long bureau = Long.parseLong(request.getParameter("bureau"));
                    int tipoServicio = Integer.parseInt(request.getParameter("tipoServicio"));
                    String url = request.getParameter("url");
                    int tipoVigencia = Integer.parseInt(request.getParameter("tipoVigencia"));
                    int cantDias = Integer.parseInt(request.getParameter("cantDias"));
                    int diaVigencia = Integer.parseInt(request.getParameter("diaVigencia"));
                    String xml = request.getParameter("xml");
                    int limiteContador = Integer.parseInt(request.getParameter("limiteContador"));
                    String credenciales = request.getParameter("json");
                    long idServicio = Long.parseLong(request.getParameter("idServicio"));

                    if (bn.actualizarServicio(idServicio, nombre, url, tipoVigencia, cantDias, diaVigencia, bureau, credenciales, limiteContador, xml, tipoPersona, tipoServicio)) {
                        json.put("estado", 200);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "buscarServicios": {
                    JSONArray servicios = bn.buscarServicios(id_empresa);
                    if (servicios.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", servicios);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "buscarServiciosActivos": {
                    JSONArray servicios = bn.serviciosActivos(id_empresa);
                    if (servicios.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", servicios);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "activarServicio": {
                    long idServ = Long.parseLong(request.getParameter("idServ"));
                    int activo = Integer.parseInt(request.getParameter("activo"));
                    if (bn.activarServicio(idServ, activo)) {
                        json.put("estado", 200);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "eliminarServicio": {
                    long idServ = Long.parseLong(request.getParameter("idServ"));
                    if (bn.eliminarServicio(idServ)) {
                        json.put("estado", 200);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
            }
        } catch (NumberFormatException ex) {
            response.getWriter().print("{ \"estado\" : " + 300 + ", \"descripcion\" : \"" + ex + "\" }");
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
            Logger.getLogger(Svl_Servicios.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Svl_Servicios.class
                    .getName()).log(Level.SEVERE, null, ex);
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
