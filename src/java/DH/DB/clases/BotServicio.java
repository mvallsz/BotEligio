/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DH.DB.clases;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mvall
 */
@Entity
@Table(name = "bot_servicio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BotServicio.findAll", query = "SELECT b FROM BotServicio b"),
    @NamedQuery(name = "BotServicio.findById", query = "SELECT b FROM BotServicio b WHERE b.id = :id"),
    @NamedQuery(name = "BotServicio.findByNombre", query = "SELECT b FROM BotServicio b WHERE b.nombre = :nombre"),
    @NamedQuery(name = "BotServicio.findByZipCodes", query = "SELECT b FROM BotServicio b WHERE b.zipCodes = :zipCodes"),
    @NamedQuery(name = "BotServicio.findByKeyWords", query = "SELECT b FROM BotServicio b WHERE b.keyWords = :keyWords"),
    @NamedQuery(name = "BotServicio.findByUsuarioHost", query = "SELECT b FROM BotServicio b WHERE b.usuarioHost = :usuarioHost"),
    @NamedQuery(name = "BotServicio.findByPasswordHost", query = "SELECT b FROM BotServicio b WHERE b.passwordHost = :passwordHost"),
    @NamedQuery(name = "BotServicio.findByEmailNotificacion", query = "SELECT b FROM BotServicio b WHERE b.emailNotificacion = :emailNotificacion"),
    @NamedQuery(name = "BotServicio.findByFechaCreacion", query = "SELECT b FROM BotServicio b WHERE b.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "BotServicio.findByIdUsuCreacion", query = "SELECT b FROM BotServicio b WHERE b.idUsuCreacion = :idUsuCreacion")})
public class BotServicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 1000)
    @Column(name = "zip_codes")
    private String zipCodes;
    @Size(max = 1000)
    @Column(name = "key_words")
    private String keyWords;
    @Size(max = 255)
    @Column(name = "usuario_host")
    private String usuarioHost;
    @Size(max = 255)
    @Column(name = "password_host")
    private String passwordHost;
    @Size(max = 255)
    @Column(name = "email_notificacion")
    private String emailNotificacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "id_usu_creacion")
    private BigInteger idUsuCreacion;

    public BotServicio() {
    }

    public BotServicio(Long id) {
        this.id = id;
    }

    public BotServicio(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getZipCodes() {
        return zipCodes;
    }

    public void setZipCodes(String zipCodes) {
        this.zipCodes = zipCodes;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getUsuarioHost() {
        return usuarioHost;
    }

    public void setUsuarioHost(String usuarioHost) {
        this.usuarioHost = usuarioHost;
    }

    public String getPasswordHost() {
        return passwordHost;
    }

    public void setPasswordHost(String passwordHost) {
        this.passwordHost = passwordHost;
    }

    public String getEmailNotificacion() {
        return emailNotificacion;
    }

    public void setEmailNotificacion(String emailNotificacion) {
        this.emailNotificacion = emailNotificacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigInteger getIdUsuCreacion() {
        return idUsuCreacion;
    }

    public void setIdUsuCreacion(BigInteger idUsuCreacion) {
        this.idUsuCreacion = idUsuCreacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BotServicio)) {
            return false;
        }
        BotServicio other = (BotServicio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DH.DB.clases.BotServicio[ id=" + id + " ]";
    }
    
}
