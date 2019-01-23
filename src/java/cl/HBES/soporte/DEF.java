/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.soporte;

/**
 *
 * @author Desarrollador
 */
public class DEF {

    static BnProperties prop = BnProperties.getInstancia();
    public static final String DIRECCION = prop.getPropiedad("DIRECCION");
    public static final String USER = prop.getPropiedad("USER");
    public static final String PASSWORD = prop.getPropiedad("PASSWORD");
    public static final String ESQUEMA = prop.getPropiedad("ESQUEMA");
    
    public static final String DOMINIO_WEB_SERV = prop.getPropiedad("DOMINIO_WEB_SERV");

    public static final String SESSION_USUARIO = "user_session";
    public static final String TITULO = "HBES";
    public static final String PAGE_LOGIN = "/login.jsp";


}
