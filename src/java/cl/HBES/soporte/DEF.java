/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.soporte;

import cl.HBES.BDD.DataSource;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

/**
 *
 * @author Manuel Valls
 */
public class DEF {

    static BnProperties prop = BnProperties.getInstancia();
    public static final String DIRECCION = prop.getPropiedad("DIRECCION");
    public static final String USER = prop.getPropiedad("USER");
    public static final String PASSWORD = prop.getPropiedad("PASSWORD");
    public static final String ESQUEMA = prop.getPropiedad("ESQUEMA");

    public static final String DOMINIO_WEB_SERV = prop.getPropiedad("DOMINIO_WEB_SERV");

    public static final String SESSION_USUARIO = "user_session";
    public static final String TITULO = prop.getPropiedad("TITULO");
    public static final String VERSION = prop.getPropiedad("VERSION");
    public static final String PAGE_LOGIN = "/login.jsp";
    public static final String MaxTotal = prop.getPropiedad("MaxTotal");
    public static BasicDataSource basicDS = DataSource.getInstance().getBasicDS();

}
