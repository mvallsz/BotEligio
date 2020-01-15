/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.soporte;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Singleton Properties 
 */
public class BnProperties {

    private static BnProperties instancia = null;
    private Properties prop;

    private BnProperties() {

        prop = new Properties();
        try {
            prop.load(new FileInputStream(new File("/home/properties/hbes/afa.properties")));
            System.out.println(prop.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BnProperties getInstancia() {

        if (instancia == null) {

            instancia = new BnProperties();
        }
        return instancia;
    }

    public String getPropiedad(String clave) {

        return prop.getProperty(clave);
    }
}
