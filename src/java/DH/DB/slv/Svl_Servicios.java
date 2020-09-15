/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DH.DB.slv;

import DH.DB.beans.BnHTMLUnit;
import DH.DB.beans.BnSelenium;
import DH.DB.beans.BnServicios;
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
//<editor-fold defaultstate="collapsed" desc="agregarServicio">
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
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="actualizarServicio">                
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
                //</editor-fold>
//<editor-fold defaultstate="collapsed" desc="buscarServicios">
                case "buscarServicios": {
                    JSONArray servicios = bn.buscarServiciosRPA();
                    if (servicios.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", servicios);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="buscarServiciosActivos">
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
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="activarServicio">
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
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="eliminarServicio">
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
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="consultasBureauMes">
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
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="consultasHistorialMes">
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
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="consultasMes">
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
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="listarEstados">
                case "listarEstados": {
                    JSONArray datos = bn.listarEstados();
                    if (datos.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", datos);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="agregarServicioRPA">
                case "agregarServicioRPA": {
                    
                    String nombre_rpa = request.getParameter("nombre_rpa");
                    String US_state = request.getParameter("US_state");
                    String zipCodes = request.getParameter("zipCodes");
                    String appliance = request.getParameter("appliance");
                    String correo_notificacion = request.getParameter("correo_notificacion");
                    String usuario_host = request.getParameter("usuario_host");
                    String password_host = request.getParameter("password_host");
                    
                    BigInteger id = bn.insertarServiciosRPA(nombre_rpa, US_state, zipCodes, appliance, correo_notificacion, usuario_host, password_host);
                    if (!id.toString().equalsIgnoreCase("0")) {
                            json.put("estado", 200);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="iniciarServicio">
                case "iniciarServicio": {
                    JSONObject datos = new JSONObject();
                    BigInteger idServicio = new BigInteger(request.getParameter("idServ"));
                    datos = bn.consultasServ(idServicio);
                    BnHTMLUnit bh = new BnHTMLUnit(idServicio.intValue(), datos.getString("us_state"), datos.getString("usuario_host"), datos.getString("password_host"), datos.getString("zip_codes"), datos.getString("key_words"), datos.getString("email_notification"));
                    bh.setName("Bot Selenium para el servicio Nro:"+idServicio.intValue()+", usuario: "+datos.getString("usuario_host"));
                    bh.setDaemon(true);
                    bh.start();
                    if (bh.isAlive()) {
                        json.put("estado", 200);
                        json.put("datos", datos);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }                
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="consultasServAct">
                case "consultasServAct": {
                    JSONArray datos = new JSONArray();
                    datos = bn.consultasServAct();
                    if (datos.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", datos);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }                
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="consultasServAct">
                case "consultasHistoricas": {
                    JSONArray datos = new JSONArray();
                    BigInteger idServicio = new BigInteger(request.getParameter("idServ"));
                    datos = bn.consultasServHistorico(idServicio);
                    if (datos.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", datos);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }                
//</editor-fold>                
                
//<editor-fold defaultstate="collapsed" desc="consultasServDisp">
                case "consultasServDisp": {
                    JSONArray datos = new JSONArray();
                    datos = bn.consultasServDisp();
                    if (datos.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", datos);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }                
//</editor-fold>        
//<editor-fold defaultstate="collapsed" desc="apagarHilo">
                case "apagarHilo": {
                    long idHilo = Long.parseLong(request.getParameter("idHilo"));

                    if (bn.apagarHilo(idHilo)) {
                        json.put("estado", 200);
                    } else {
                        json.put("estado", 300);
                    }
                    response.getWriter().print(json);
                    break;
                }                
//</editor-fold>        
                
                
//<editor-fold defaultstate="collapsed" desc="listarEmpresas">
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
//</editor-fold>                
//<editor-fold defaultstate="collapsed" desc="listarOrigenes">
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
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="listarOrigenes2">
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
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="agregarOrigen">
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
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="listarTipoDato">
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
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="listarServiciosDefault">
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
//</editor-fold>
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
