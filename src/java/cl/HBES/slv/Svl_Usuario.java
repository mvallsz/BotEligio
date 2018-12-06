/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.slv;

import cl.HBES.beans.BnUsuario;
import cl.HBES.clases.CredencialesUsuario;
import cl.HBES.clases.Encriptar;
import cl.HBES.soporte.DEF;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Desarrollador
 */
@WebServlet(name = "Svl_Usuario", urlPatterns = {"/Svl_Usuario"})
public class Svl_Usuario extends HttpServlet {

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String code = request.getParameter("accion");
            BnUsuario bn = new BnUsuario();
            HttpSession session = request.getSession();
            switch (code) {
                case "login": {
                    JSONObject json = new JSONObject();
                    String user = request.getParameter("username");
                    String clave = request.getParameter("password");
                    CredencialesUsuario credenciales = bn.iniciarSesion(user, Encriptar.toMD5(clave));
                    if (credenciales != null) {
                        credenciales.setPassword(clave);
                        session.setAttribute(DEF.SESSION_USUARIO, credenciales);
                        json.put("estado", 200);
                    } else {
                        json.put("estado", 405);
                        json.put("descripcion", "Error de usuario o contraseña");
                    }
                    out.print(json);
                    break;
                }
                case "agregarUsuario": {
                    JSONObject json = new JSONObject();
                    BigInteger empresa = new BigInteger(request.getParameter("empresa"));
                    String nombre = request.getParameter("nombre");
                    String user = request.getParameter("user");
                    String pass = request.getParameter("pass1");
                    int creado = bn.agregarUser(nombre, user, pass, empresa);
                    if (creado == 1) {
                        json.put("estado", 200);
                    } else if (creado == 2) {
                        json.put("estado", 300);
                    } else {
                        json.put("estado", 400);
                    }
                    out.print(json);
                    break;
                }
                case "listarUsuario": {
                    JSONObject json = new JSONObject();
                    BigInteger idEmp = new BigInteger(request.getParameter("idEmp"));
                    JSONArray user = bn.listUser(idEmp);
                    if (user.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", user);
                    } else {
                        json.put("estado", 400);
                    }
                    out.print(json);
                    break;
                }
                case "listarUsuarios": {
                    JSONObject json = new JSONObject();
                    BigInteger idEmp = new BigInteger(request.getParameter("idEmp"));
                    JSONArray user = bn.listarUsuarios(idEmp);
                    if (user.length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", user);
                    } else {
                        json.put("estado", 400);
                    }
                    out.print(json);
                    break;
                }
                case "bloquearUser": {
                    JSONObject json = new JSONObject();
                    BigInteger idUser = new BigInteger(request.getParameter("idUser"));
                    int estado = Integer.parseInt(request.getParameter("estado"));
                    if (bn.bloquearUsuarios(idUser, estado)) {
                        json.put("estado", 200);
                    } else {
                        json.put("estado", 400);
                    }
                    out.print(json);
                    break;
                }
                case "actualizarUsuario": {
                    JSONObject json = new JSONObject();
                    BigInteger idUser = new BigInteger(request.getParameter("idUser"));
                    String nombre = request.getParameter("nombre");
                    String password = request.getParameter("pass");
                    String usuario = request.getParameter("user");
                    int estado = Integer.parseInt(request.getParameter("estado"));
                    if (bn.editarUsuarios(nombre, usuario, password, idUser, estado)) {
                        json.put("estado", 200);
                    } else {
                        json.put("estado", 400);
                    }
                    out.print(json);
                    break;
                }
                case "CerrarSession": {
                    session.invalidate();
                    response.sendRedirect("cmd");
                    break;
                }
                case "buscarServU": {
                    JSONObject json = new JSONObject();
                    String user = request.getParameter("user");
                    String pass = request.getParameter("pass");
                    JSONObject j = bn.obtenerSerU(user, pass);
                    if (j.getJSONArray("datos").length() > 0) {
                        json.put("estado", 200);
                        json.put("datos", j.getJSONArray("datos"));
                        json.put("id", j.getString("id"));
                    } else {
                        json.put("estado", 400);
                    }
                    out.print(json);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
        processRequest(request, response);
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
        processRequest(request, response);
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
