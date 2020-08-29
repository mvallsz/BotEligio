/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DH.DB.clases;

import com.google.gson.Gson;
import java.math.BigInteger;

/**
 *
 * @author mvalls
 */
public class Usuario {
    private BigInteger id;
    private String usuario;
    private String password;
    private String email;
    private String nombre;
    private String apellido;
    private CuentaEmpresa empresa;

    public Usuario() {
    }

    public Usuario(BigInteger id, String usuario, String apellido, String password, String email, String nombre, CuentaEmpresa empresa) {
        this.id = id;
        this.usuario = usuario;
        this.password = password;
        this.email = email;
        this.nombre = nombre;                
        this.apellido = apellido;
        this.empresa = empresa;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public CuentaEmpresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(CuentaEmpresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", usuario=" + usuario + ", password=" + password + ", email=" + email + ", nombre=" + nombre + ", empresa=" + empresa + '}';
    }

     public String toJson(){
        Gson gson = new Gson(); 
        String json = gson.toJson(this);
        return json;
    }
    
}
