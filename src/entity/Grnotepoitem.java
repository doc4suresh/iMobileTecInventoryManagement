/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "grnotepoitem")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grnotepoitem.findAll", query = "SELECT g FROM Grnotepoitem g"),
    @NamedQuery(name = "Grnotepoitem.findById", query = "SELECT g FROM Grnotepoitem g WHERE g.id = :id"),
    @NamedQuery(name = "Grnotepoitem.findByQtyrecieved", query = "SELECT g FROM Grnotepoitem g WHERE g.qtyrecieved = :qtyrecieved"),
    @NamedQuery(name = "Grnotepoitem.findByPprice", query = "SELECT g FROM Grnotepoitem g WHERE g.pprice = :pprice"),
    @NamedQuery(name = "Grnotepoitem.findBySprice", query = "SELECT g FROM Grnotepoitem g WHERE g.sprice = :sprice"),
    @NamedQuery(name = "Grnotepoitem.findByLinetotal", query = "SELECT g FROM Grnotepoitem g WHERE g.linetotal = :linetotal")})
public class Grnotepoitem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "qtyrecieved")
    private Integer qtyrecieved;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pprice")
    private BigDecimal pprice;
    @Column(name = "sprice")
    private BigDecimal sprice;
    @Column(name = "linetotal")
    private BigDecimal linetotal;
    @JoinColumn(name = "grnote_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Grnote grnoteId;
    @JoinColumn(name = "poitem_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Poitem poitemId;

    public Grnotepoitem() {
    }

    public Grnotepoitem(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQtyrecieved() {
        return qtyrecieved;
    }

    public void setQtyrecieved(Integer qtyrecieved) {
        this.qtyrecieved = qtyrecieved;
    }

    public BigDecimal getPprice() {
        return pprice;
    }

    public void setPprice(BigDecimal pprice) {
        this.pprice = pprice;
    }

    public BigDecimal getSprice() {
        return sprice;
    }

    public void setSprice(BigDecimal sprice) {
        this.sprice = sprice;
    }

    public BigDecimal getLinetotal() {
        return linetotal;
    }

    public void setLinetotal(BigDecimal linetotal) {
        this.linetotal = linetotal;
    }

    public Grnote getGrnoteId() {
        return grnoteId;
    }

    public void setGrnoteId(Grnote grnoteId) {
        this.grnoteId = grnoteId;
    }

    public Poitem getPoitemId() {
        return poitemId;
    }

    public void setPoitemId(Poitem poitemId) {
        this.poitemId = poitemId;
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
        if (!(object instanceof Grnotepoitem)) {
            return false;
        }
        Grnotepoitem other = (Grnotepoitem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Grnotepoitem[ id=" + id + " ]";
    }
    
}
