/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.BDD;

import cl.HBES.soporte.DEF;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

/**
 *
 * @author Usuario
 */
public class DataSource {

    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private static final String DB_CONNECTION_URL = DEF.DIRECCION + "" + DEF.ESQUEMA;
    private static final String DB_USER = DEF.USER;
    private static final String DB_PWD = DEF.PASSWORD;

    private static DataSource ds;
    private BasicDataSource basicDS = new BasicDataSource();

    //private constructor
    private DataSource() {
        //BasicDataSource basicDS = new BasicDataSource();
        basicDS.setDriverClassName(DRIVER_CLASS);
        basicDS.setUrl(DB_CONNECTION_URL);
        basicDS.setUsername(DB_USER);
        basicDS.setPassword(DB_PWD);

        // Parameters for connection pooling
        basicDS.setInitialSize(1);
        basicDS.setMaxTotal(Integer.parseInt(DEF.MaxTotal));
    }

    /**
     * static method for getting instance.
     */
    public static DataSource getInstance() {
        if (ds == null) {
            ds = new DataSource();
        }
        return ds;
    }

    public BasicDataSource getBasicDS() {
        return basicDS;
    }

    public void setBasicDS(BasicDataSource basicDS) {
        this.basicDS = basicDS;
    }
}
