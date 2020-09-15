/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DH.DB.BDD;

import DH.DB.soporte.DEF;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

/**
 *
 * @author Manuel Valls
 */
public class DataSource {

    private static final String DRIVER_CLASS = DEF.drivername;
    private static final String DB_CONNECTION_URL = DEF.driver+"//"+ DEF.host + ":" + DEF.port +"/"+ DEF.db;
    private static final String DB_USER = DEF.user;
    private static final String DB_PWD = DEF.pass;

    private static DataSource ds;
    private BasicDataSource basicDS = new BasicDataSource();

    //private constructor
    private DataSource() {
        basicDS.setDriverClassName(DRIVER_CLASS);
        //System.out.println(DEF.driver);
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
