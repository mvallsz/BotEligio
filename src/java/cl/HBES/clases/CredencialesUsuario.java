/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.HBES.clases;

/**
 *
 * @author Desarrollador
 */
public class CredencialesUsuario {
    private long id;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private CuentaEmpresa empresa;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CuentaEmpresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(CuentaEmpresa empresa) {
        this.empresa = empresa;
    }
    
    
}
