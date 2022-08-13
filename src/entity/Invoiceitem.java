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
@Table(name = "invoiceitem")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invoiceitem.findAll", query = "SELECT i FROM Invoiceitem i"),
    @NamedQuery(name = "Invoiceitem.findById", query = "SELECT i FROM Invoiceitem i WHERE i.id = :id"),
    @NamedQuery(name = "Invoiceitem.findByQty", query = "SELECT i FROM Invoiceitem i WHERE i.qty = :qty"),
    @NamedQuery(name = "Invoiceitem.findByDiscountper", query = "SELECT i FROM Invoiceitem i WHERE i.discountper = :discountper"),
    @NamedQuery(name = "Invoiceitem.findByDiscountvalue", query = "SELECT i FROM Invoiceitem i WHERE i.discountvalue = :discountvalue"),
    @NamedQuery(name = "Invoiceitem.findByTotal", query = "SELECT i FROM Invoiceitem i WHERE i.total = :total"),
    @NamedQuery(name = "Invoiceitem.findByImei", query = "SELECT i FROM Invoiceitem i WHERE i.imei = :imei")})
public class Invoiceitem implements Serializable {

    @Column(name = "IMEI")
    private String imei;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "qty")
    private Integer qty;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "discountper")
    private BigDecimal discountper;
    @Column(name = "discountvalue")
    private BigDecimal discountvalue;
    @Column(name = "total")
    private BigDecimal total;
    @JoinColumn(name = "warrenty_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Warrenty warrentyId;
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Invoice invoiceId;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Item itemId;

    public Invoiceitem() {
    }

    public Invoiceitem(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public BigDecimal getDiscountper() {
        return discountper;
    }

    public void setDiscountper(BigDecimal discountper) {
        this.discountper = discountper;
    }

    public BigDecimal getDiscountvalue() {
        return discountvalue;
    }

    public void setDiscountvalue(BigDecimal discountvalue) {
        this.discountvalue = discountvalue;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }


    public Warrenty getWarrentyId() {
        return warrentyId;
    }

    public void setWarrentyId(Warrenty warrentyId) {
        this.warrentyId = warrentyId;
    }

    public Invoice getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Invoice invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Item getItemId() {
        return itemId;
    }

    public void setItemId(Item itemId) {
        this.itemId = itemId;
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
        if (!(object instanceof Invoiceitem)) {
            return false;
        }
        Invoiceitem other = (Invoiceitem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Invoiceitem[ id=" + id + " ]";
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
    
}
