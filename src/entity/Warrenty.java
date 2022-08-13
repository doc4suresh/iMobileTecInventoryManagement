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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "warrenty")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Warrenty.findAll", query = "SELECT w FROM Warrenty w"),
    @NamedQuery(name = "Warrenty.findById", query = "SELECT w FROM Warrenty w WHERE w.id = :id"),
    @NamedQuery(name = "Warrenty.findByName", query = "SELECT w FROM Warrenty w WHERE w.name = :name")})
public class Warrenty implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "warrentyId", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private List<Invoiceitem> invoiceitemList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;

    public Warrenty() {
    }

    public Warrenty(Integer id) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Warrenty)) {
            return false;
        }
        Warrenty other = (Warrenty) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    @XmlTransient
    public List<Invoiceitem> getInvoiceitemList() {
        return invoiceitemList;
    }

    public void setInvoiceitemList(List<Invoiceitem> invoiceitemList) {
        this.invoiceitemList = invoiceitemList;
    }

}
