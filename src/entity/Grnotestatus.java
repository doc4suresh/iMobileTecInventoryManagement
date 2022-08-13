/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "grnotestatus")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grnotestatus.findAll", query = "SELECT g FROM Grnotestatus g"),
    @NamedQuery(name = "Grnotestatus.findById", query = "SELECT g FROM Grnotestatus g WHERE g.id = :id"),
    @NamedQuery(name = "Grnotestatus.findByName", query = "SELECT g FROM Grnotestatus g WHERE g.name = :name")})
public class Grnotestatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grnotestatusId", fetch = FetchType.LAZY)
      @Fetch(FetchMode.SELECT)
    private List<Grnote> grnoteList;

    public Grnotestatus() {
    }

    public Grnotestatus(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Grnote> getGrnoteList() {
        return grnoteList;
    }

    public void setGrnoteList(List<Grnote> grnoteList) {
        this.grnoteList = grnoteList;
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
        if (!(object instanceof Grnotestatus)) {
            return false;
        }
        Grnotestatus other = (Grnotestatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
