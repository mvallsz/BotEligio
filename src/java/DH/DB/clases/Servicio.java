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
 * @author mvall
 */
public class Servicio {
    
    private BigInteger id;
    private String nombre;
    private String zip_codes;
    private String key_words;
    private String usuario_host;
    private String password_host;
    private String email_notificacion;

    public Servicio() {
    }

    public Servicio(BigInteger id, String nombre, String zip_codes, String key_words, String usuario_host, String password_host, String email_notificacion) {
        this.id = id;
        this.nombre = nombre;
        this.zip_codes = zip_codes;
        this.key_words = key_words;
        this.usuario_host = usuario_host;
        this.password_host = password_host;
        this.email_notificacion = email_notificacion;
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

    public String getZip_codes() {
        return zip_codes;
    }

    public void setZip_codes(String zip_codes) {
        this.zip_codes = zip_codes;
    }

    public String getKey_words() {
        return key_words;
    }

    public void setKey_words(String key_words) {
        this.key_words = key_words;
    }

    public String getUsuario_host() {
        return usuario_host;
    }

    public void setUsuario_host(String usuario_host) {
        this.usuario_host = usuario_host;
    }

    public String getPassword_host() {
        return password_host;
    }

    public void setPassword_host(String password_host) {
        this.password_host = password_host;
    }

    public String getEmail_notificacion() {
        return email_notificacion;
    }

    public void setEmail_notificacion(String email_notificacion) {
        this.email_notificacion = email_notificacion;
    }

    @Override
    public String toString() {
        return "Servicio{" + "id=" + id + ", nombre=" + nombre + ", zip_codes=" + zip_codes + ", key_words=" + key_words + ", usuario_host=" + usuario_host + ", password_host=" + password_host + ", email_notificacion=" + email_notificacion + '}';
    }
    
    public String toJson(){
        Gson gson = new Gson(); 
        String json = gson.toJson(this);
        return json;
    }
    
}
