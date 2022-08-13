/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "privilage")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Privilage.findAll", query = "SELECT p FROM Privilage p"),
    @NamedQuery(name = "Privilage.findById", query = "SELECT p FROM Privilage p WHERE p.id = :id"),
    @NamedQuery(name = "Privilage.findByIns", query = "SELECT p FROM Privilage p WHERE p.ins = :ins"),
    @NamedQuery(name = "Privilage.findBySel", query = "SELECT p FROM Privilage p WHERE p.sel = :sel"),
    @NamedQuery(name = "Privilage.findByUpd", query = "SELECT p FROM Privilage p WHERE p.upd = :upd"),
    @NamedQuery(name = "Privilage.findByDel", query = "SELECT p FROM Privilage p WHERE p.del = :del"),

    @NamedQuery(name = "Privilage.findAllByRole", query = "SELECT p FROM Privilage p where p.roleId=:role"),
    @NamedQuery(name = "Privilage.findAllByModule", query = "SELECT p FROM Privilage p where p.moduleId=:module"),
    @NamedQuery(name = "Privilage.findAllByRoleModule", query = "SELECT p FROM Privilage p where p.roleId=:role and p.moduleId=:module"),
    @NamedQuery(name = "Privilage.findAllByUser", query = "SELECT p FROM Privilage p INNER JOIN p.roleId.userList u WHERE u = :user")})
public class Privilage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ins")
    private Integer ins;
    @Column(name = "sel")
    private Integer sel;
    @Column(name = "upd")
    private Integer upd;
    @Column(name = "del")
    private Integer del;
    @JoinColumn(name = "module_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Module moduleId;
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Role roleId;

    public Privilage() {
    }

    public Privilage(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIns() {
        return ins;
    }

    public void setIns(Integer ins) {
        this.ins = ins;
    }

    public Integer getSel() {
        return sel;
    }

    public void setSel(Integer sel) {
        this.sel = sel;
    }

    public Integer getUpd() {
        return upd;
    }

    public void setUpd(Integer upd) {
        this.upd = upd;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public Module getModuleId() {
        return moduleId;
    }

    public void setModuleId(Module moduleId) {
        this.moduleId = moduleId;
    }

    public Role getRoleId() {
        return roleId;
    }

    public void setRoleId(Role roleId) {
        this.roleId = roleId;
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
        if (!(object instanceof Privilage)) {
            return false;
        }
        Privilage other = (Privilage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return roleId.getName() + " Privilages for " + moduleId.getName();
    }
    
}
