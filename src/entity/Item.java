/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i"),
    @NamedQuery(name = "Item.findById", query = "SELECT i FROM Item i WHERE i.id = :id"),
    @NamedQuery(name = "Item.findByName", query = "SELECT i FROM Item i WHERE i.name = :name"),
    @NamedQuery(name = "Item.findByPprice", query = "SELECT i FROM Item i WHERE i.pprice = :pprice"),
    @NamedQuery(name = "Item.findBySprice", query = "SELECT i FROM Item i WHERE i.sprice = :sprice"),
    @NamedQuery(name = "Item.findByQty", query = "SELECT i FROM Item i WHERE i.qty = :qty"),
    @NamedQuery(name = "Item.findByRop", query = "SELECT i FROM Item i WHERE i.rop = :rop"),

    @NamedQuery(name = "Item.findBySupplier", query = "SELECT i FROM Item i WHERE i.supplierId = :supplier"),
    @NamedQuery(name = "Item.findAllBySupplierType", query = "SELECT i FROM Item i WHERE i.supplierId=:supplier and i.itemtypeId = :type "),
    
    @NamedQuery(name = "Item.findAllByType", query = "SELECT i FROM Item i WHERE i.itemtypeId = :type"),
    @NamedQuery(name = "Item.findAllByBrand", query = "SELECT i FROM Item i WHERE i.brandId = :brand"),
    @NamedQuery(name = "Item.findAllByStatus", query = "SELECT i FROM Item i WHERE i.itemstatusId = :status"),
    @NamedQuery(name = "Item.findAllByName", query = "SELECT i FROM Item i WHERE i.name like :name"),
    @NamedQuery(name = "Item.findAllByNameStatus", query = "SELECT i FROM Item i WHERE i.itemstatusId = :status and i.name like :name"),
    @NamedQuery(name = "Item.findAllByNameBrand", query = "SELECT i FROM Item i WHERE i.brandId = :brand and i.name like :name"),
    @NamedQuery(name = "Item.findAllByNameType", query = "SELECT i FROM Item i WHERE i.itemtypeId = :type and i.name like :name"),
    @NamedQuery(name = "Item.findAllByStatusBrand", query = "SELECT i FROM Item i WHERE i.itemstatusId = :status and i.brandId = :brand"),
    @NamedQuery(name = "Item.findAllByStatusType", query = "SELECT i FROM Item i WHERE i.itemstatusId = :status and i.itemtypeId = :type"),
    @NamedQuery(name = "Item.findAllByTypeBrand", query = "SELECT i FROM Item i WHERE i.itemtypeId = :type and i.brandId = :brand"),
    @NamedQuery(name = "Item.findAllByNameStatusBrand", query = "SELECT i FROM Item i WHERE i.itemstatusId = :status and i.brandId = :brand and i.name like :name"),
    @NamedQuery(name = "Item.findAllByNameStatusType", query = "SELECT i FROM Item i WHERE i.itemstatusId = :status and i.itemtypeId = :type and i.name like :name"),
    @NamedQuery(name = "Item.findAllByNameBrandType", query = "SELECT i FROM Item i WHERE i.brandId = :brand and i.itemtypeId = :type and i.name like :name"),
    @NamedQuery(name = "Item.findAllByStatusBrandType", query = "SELECT i FROM Item i WHERE i.brandId = :brand and i.itemtypeId = :type and i.itemstatusId = :status"),
    @NamedQuery(name = "Item.findAllByNameStatusBrandType", query = "SELECT i FROM Item i WHERE i.itemstatusId = :status and i.brandId = :brand and i.name like :name and i.itemtypeId = :type"),

    @NamedQuery(name = "Item.findBySellingAndItemAndStatus", query = "SELECT i FROM Item i WHERE i.id = :id AND i.sprice = :sprice AND i.itemstatusId = :itemstatusId"),
    @NamedQuery(name = "Item.findByItem", query = "SELECT i FROM Item i WHERE i.itemstatusId = 1"),
    @NamedQuery(name = "Item.findReorder", query = "SELECT i FROM Item i WHERE i.qty <= i.rop AND i.itemstatusId = 1"),
    })
public class Item implements Serializable {

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "itemCode")
    private String itemCode;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pprice")
    private BigDecimal pprice;
    @Column(name = "sprice")
    private BigDecimal sprice;
    @Column(name = "qty")
    private Integer qty;
    @Column(name = "rop")
    private Integer rop;
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Brand brandId;
    @JoinColumn(name = "itemstatus_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Itemstatus itemstatusId;
    @JoinColumn(name = "itemtype_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Itemtype itemtypeId;
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Supplier supplierId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemId")
    private List<Invoiceitem> invoiceitemList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemId")
    private List<Poitem> poitemList;

    public Item() {
    }

    public Item(Integer id) {
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

    public boolean setName(String name) {
        boolean validity = name != null && name.matches("^[_A-Za-z0-9-/.:,\\s]{1,20}");
        if(validity){this.name = name; } else { this.name=null; } 
        return validity;
    }

    public BigDecimal getPprice() {
        return pprice;
    }

    public boolean setPprice(BigDecimal pprice) {
        this.pprice = pprice;
        return pprice != null;
    }

    public BigDecimal getSprice() {
        return sprice;
    }

    public boolean setSprice(BigDecimal sprice) {
        this.sprice = sprice;
        return sprice != null;
    }

    public Integer getQty() {
        return qty;
    }

    public boolean setQty(String qty) {
        boolean validity = qty.matches("\\d{1,4}");
        if (validity) {
            this.qty = Integer.parseInt(qty);
        } else {
            this.qty = null;
        }
        return validity;
    }

    public Integer getRop() {
        return rop;
    }

    public boolean setRop(String rop) {
        boolean validity = rop.matches("\\d{1,3}");
        if (validity) {
            this.rop = Integer.parseInt(rop);
        } else {
            this.rop = null;
        }
        return validity;
    }

    public Brand getBrandId() {
        return brandId;
    }

    public void setBrandId(Brand brandId) {
        this.brandId = brandId;
    }

    public Itemstatus getItemstatusId() {
        return itemstatusId;
    }

    public void setItemstatusId(Itemstatus itemstatusId) {
        this.itemstatusId = itemstatusId;
    }

    public Itemtype getItemtypeId() {
        return itemtypeId;
    }

    public void setItemtypeId(Itemtype itemtypeId) {
        this.itemtypeId = itemtypeId;
    }

    public Supplier getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Supplier supplierId) {
        this.supplierId = supplierId;
    }

    @XmlTransient
    public List<Invoiceitem> getInvoiceitemList() {
        return invoiceitemList;
    }

    public void setInvoiceitemList(List<Invoiceitem> invoiceitemList) {
        this.invoiceitemList = invoiceitemList;
    }

    @XmlTransient
    public List<Poitem> getPoitemList() {
        return poitemList;
    }

    public void setPoitemList(List<Poitem> poitemList) {
        this.poitemList = poitemList;
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
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    
}
