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
public class CuentaEmpresa {
    private long id;
    private String nombre;
    private long rut;
    private long dv;
    private long cod_subsidiary;
    private String fecha_act;
    private boolean historial;

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

    public long getRut() {
        return rut;
    }

    public void setRut(long rut) {
        this.rut = rut;
    }

    public long getDv() {
        return dv;
    }

    public void setDv(long dv) {
        this.dv = dv;
    }

    public long getCod_subsidiary() {
        return cod_subsidiary;
    }

    public void setCod_subsidiary(long cod_subsidiary) {
        this.cod_subsidiary = cod_subsidiary;
    }

    public String getFecha_act() {
        return fecha_act;
    }

    public void setFecha_act(String fecha_act) {
        this.fecha_act = fecha_act;
    }

    public boolean isHistorial() {
        return historial;
    }

    public void setHistorial(boolean historial) {
        this.historial = historial;
    }
    
    
}
