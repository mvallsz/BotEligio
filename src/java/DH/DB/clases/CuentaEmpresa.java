/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DH.DB.clases;

import java.math.BigInteger;
import com.google.gson.Gson;

/**
 *
 * @author mvalls
 */
public class CuentaEmpresa {
    private BigInteger id;
    private String nombre;
    private String cod_empresa;

    public CuentaEmpresa() {
    }
    
    public CuentaEmpresa(BigInteger id, String nombre, String cod_empresa) {
        this.id = id;
        this.nombre = nombre;
        this.cod_empresa = cod_empresa;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCod_empresa() {
        return cod_empresa;
    }

    public void setCod_empresa(String cod_empresa) {
        this.cod_empresa = cod_empresa;
    }

    @Override
    public String toString() {
        return "CuentaEmpresa{" + "id=" + id + ", nombre=" + nombre + ", cod_empresa=" + cod_empresa + '}';
    }
    
    public String toJson(){
        Gson gson = new Gson(); 
        String json = gson.toJson(this);
        return json;
    }
    
}
