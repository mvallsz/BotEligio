/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DH.DB.slv;

import DH.DB.soporte.DEF;
import DH.DB.soporte.Soporte;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Desarrollador
 */
@WebServlet(name = "cmd", urlPatterns = {"/cmd"})
public class cmd extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        if (Soporte.isSesionActiva(request)) {
            String code = (request.getParameter("code") == null ? "home" : request.getParameter("code"));
            switch (code) {
                case "home": {
                    Soporte.toPage("/home.jsp", request, response, this);
                    break;
                }
            }
        } else {
            Soporte.info("Parametro: " + request.getParameter("accion") != null ? request.getParameter("accion") : "null" + ", se redireccionara a: " + DEF.PAGE_LOGIN);
            Soporte.toPage(DEF.PAGE_LOGIN, request, response, this);
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

    private void toPage(String page, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            getServletContext().getRequestDispatcher(page).forward(request, response);
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }
    }
}
