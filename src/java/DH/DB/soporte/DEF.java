/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DH.DB.soporte;

import DH.DB.BDD.DataSource;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

/**
 *
 * @author Manuel Valls
 */
public class DEF {

    static BnProperties prop = BnProperties.getInstancia();
//    public static final String DIRECCION = prop.getPropiedad("DIRECCION");
//    public static final String USER = prop.getPropiedad("USER");
//    public static final String PASSWORD = prop.getPropiedad("PASSWORD");
    public static final String ESQUEMA = prop.getPropiedad("ESQUEMA");

    public static final String DOMINIO_WEB_SERV = prop.getPropiedad("DOMINIO_WEB_SERV");

    public static final String SESSION_USUARIO = "user_session";
    public static final String TITULO = prop.getPropiedad("TITULO");
    public static final String VERSION = prop.getPropiedad("VERSION");
    public static final String PAGE_LOGIN = "/login.jsp";
    public static final String MaxTotal = prop.getPropiedad("MAXTOTAL");
    

    /*Data base*/
    public static final String drivername = prop.getPropiedad("DRIVERNAME");
    public static final String driver = prop.getPropiedad("DRIVER");
    public static final String port = prop.getPropiedad("PORT");
    public static final String host = prop.getPropiedad("SERVER_NAME");
    public static final String user = prop.getPropiedad("DATABASE_USER");
    public static final String pass = prop.getPropiedad("DATABASE_PASSWORD");
    public static final String db = prop.getPropiedad("DATABASE_NAME");
    public static BasicDataSource basicDS = DataSource.getInstance().getBasicDS();
    public static String page2captcha;
    
}

