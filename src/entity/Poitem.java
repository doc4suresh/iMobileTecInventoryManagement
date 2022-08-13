/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "poitem")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Poitem.findAll", query = "SELECT p FROM Poitem p"),
    @NamedQuery(name = "Poitem.findById", query = "SELECT p FROM Poitem p WHERE p.id = :id"),
    @NamedQuery(name = "Poitem.findByQtyrequested", query = "SELECT p FROM Poitem p WHERE p.qtyrequested = :qtyrequested"),
    @NamedQuery(name = "Poitem.findByQtyrecieved", query = "SELECT p FROM Poitem p WHERE p.qtyrecieved = :qtyrecieved"),
    @NamedQuery(name = "Poitem.findByLinetotal", query = "SELECT p FROM Poitem p WHERE p.linetotal = :linetotal"),
    @NamedQuery(name = "Poitem.findByPPrice", query = "SELECT p FROM Poitem p WHERE p.pPrice = :pPrice")})
public class Poitem implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poitemId", fetch = FetchType.EAGER)
    private List<Grnotepoitem> grnotepoitemList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "qtyrequested")
    private Integer qtyrequested;
    @Column(name = "qtyrecieved")
    private Integer qtyrecieved;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "linetotal")
    private BigDecimal linetotal;
    @Column(name = "pPrice")
    private BigDecimal pPrice;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Item itemId;
    @JoinColumn(name = "porder_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Porder porderId;

    public Poitem() {
    }

    public Poitem(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQtyrequested() {
        return qtyrequested;
    }

    public void setQtyrequested(Integer qtyrequested) {
        this.qtyrequested = qtyrequested;
    }

    public Integer getQtyrecieved() {
        return qtyrecieved;
    }

    public void setQtyrecieved(Integer qtyrecieved) {
        this.qtyrecieved = qtyrecieved;
    }

    public BigDecimal getLinetotal() {
        return linetotal;
    }

    public void setLinetotal(BigDecimal linetotal) {
        this.linetotal = linetotal;
    }

    public BigDecimal getPPrice() {
        return pPrice;
    }

    public void setPPrice(BigDecimal pPrice) {
        this.pPrice = pPrice;
    }

    public Item getItemId() {
        return itemId;
    }

    public void setItemId(Item itemId) {
        this.itemId = itemId;
    }

    public Porder getPorderId() {
        return porderId;
    }

    public void setPorderId(Porder porderId) {
        this.porderId = porderId;
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
        if (!(object instanceof Poitem)) {
            return false;
        }
        Poitem other = (Poitem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Poitem[ id=" + id + " ]";
    }

    @XmlTransient
    public List<Grnotepoitem> getGrnotepoitemList() {
        return grnotepoitemList;
    }

    public void setGrnotepoitemList(List<Grnotepoitem> grnotepoitemList) {
        this.grnotepoitemList = grnotepoitemList;
    }
    
}
