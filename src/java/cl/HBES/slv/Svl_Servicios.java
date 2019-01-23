/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.slv;

import cl.HBES.beans.BnUsuario;
import cl.HBES.beans.BnResponse;
import cl.HBES.beans.BnServicios;
import cl.HBES.clases.CredencialesUsuario;
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
                    String credenciales = request.getParameter("json");
                    int tipoResponse = Integer.parseInt(request.getParameter("response"));
                    String varResponse = request.getParameter("varResponse");
                    long id_empresa = Long.parseLong(request.getParameter("idEmpresa"));
                    int limiteContador = 0;
                    try {
                        limiteContador = Integer.parseInt(request.getParameter("limiteContador"));
                    } catch (Exception ex) {
                    }
                    BigInteger id = bn.insertarServicios(nombre, url, tipoVigencia, cantDias, diaVigencia, id_empresa, bureau, credenciales, limiteContador, xml, tipoPersona, tipoServicio, tipoResponse);
                    if (!id.toString().equalsIgnoreCase("0")) {
                        boolean result = false;
                        JSONArray jsonn = new JSONArray(varResponse);
                        for (int i = 0; i < jsonn.length(); i++) {
                            JSONObject j = new JSONObject(jsonn.get(i).toString());
                            long idr = bn.guardarVariables(id, tipoPersona, j);
                            JSONArray jr = new JSONArray(j.get("VARHIJO").toString());
                            if (jr.length() > 0) {
                                for (int k = 0; k < jr.length(); k++) {
                                    JSONObject j2 = new JSONObject(jr.get(k).toString());
                                    result = bn.guardarVariablesHijo(idr, j2);
                                }
                            } else {
                                result = true;
                            }
                        }
                        if (result) {
                            json.put("estado", 200);
                        } else {
                            json.put("estado", 301);
                        }
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
                    String credenciales = request.getParameter("json");
                    long idServicio = Long.parseLong(request.getParameter("idServicio"));
                    int limiteContador = 0;
                    try {
                        limiteContador = Integer.parseInt(request.getParameter("limiteContador"));
                    } catch (Exception ex) {
                    }
                    if (bn.actualizarServicio(idServicio, nombre, url, tipoVigencia, cantDias, diaVigencia, bureau, credenciales, limiteContador, xml, tipoPersona, tipoServicio)) {
                        json.put("estado", 200);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "buscarServicios": {
                    long empresa = 1;
                    empresa = Long.parseLong(request.getParameter("empresa"));
                    JSONArray servicios = bn.buscarServicios(empresa);
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
                    long id_empresa = Long.parseLong(request.getParameter("idEmpresa"));
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
                case "consultasBureauMes": {
                    long id_empresa = Long.parseLong(request.getParameter("idEmpresa"));
                    JSONObject datos = bn.consultaBureauMes(id_empresa);
                    if (datos.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", datos);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "consultasHistorialMes": {
                    long id_empresa = Long.parseLong(request.getParameter("idEmpresa"));
                    JSONObject datos = bn.consultaHistorialMes(id_empresa);
                    if (datos.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", datos);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "consultasMes": {
                    int mes = 0;
                    int anio = 0;
                    int bureau = 0;
                    JSONArray datos = new JSONArray();
                    long id_empresa = Long.parseLong(request.getParameter("empresa"));
                    mes = Integer.parseInt(request.getParameter("mes"));
                    anio = Integer.parseInt(request.getParameter("anio"));
                    bureau = Integer.parseInt(request.getParameter("bureau"));
                    datos = bn.consultasMes(id_empresa, mes, anio, bureau);
                    if (datos.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", datos);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "listarEmpresas": {
                    JSONArray datos = bn.listarEmpresas();
                    if (datos.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", datos);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "listarOrigenes": {
                    long id_empresa = Long.parseLong(request.getParameter("idEmp"));
                    JSONArray datos = bn.listarOrigenes(id_empresa);
                    if (datos.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", datos);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "listarOrigenes2": {
                    long id_empresa = Long.parseLong(request.getParameter("idEmp"));
                    JSONArray datos = bn.listarOrigenes2(id_empresa);
                    if (datos.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", datos);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "agregarOrigen": {
                    String origen = request.getParameter("origen");
                    long idEmp = Long.parseLong(request.getParameter("idEmp"));
                    if (bn.insertarOrigen(origen, idEmp)) {
                        json.put("estado", 200);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "listarTipoDato": {
                    JSONArray datos = bn.listarTipoDato();
                    if (datos.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", datos);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
                case "listarServiciosDefault": {
                    JSONObject datos = bn.listarServicioDefault();
                    if (datos.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", datos);
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
