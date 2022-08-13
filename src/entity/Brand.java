/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import dao.BrandDao;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "brand")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Brand.findAll", query = "SELECT b FROM Brand b"),
    @NamedQuery(name = "Brand.findById", query = "SELECT b FROM Brand b WHERE b.id = :id"),
    @NamedQuery(name = "Brand.findByName", query = "SELECT b FROM Brand b WHERE b.name = :name")})
public class Brand implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @JoinTable(name = "supplybrand", joinColumns = {
        @JoinColumn(name = "brand_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "supplier_id", referencedColumnName = "id")})
    @ManyToMany(fetch = FetchType.EAGER)
      @Fetch(FetchMode.SELECT)
    private List<Supplier> supplierList;
//      REMOVE THIS-------------------------------------------------------------------------------- 
//    @JoinTable(name = "brand_has_supplier", joinColumns = {
//        @JoinColumn(name = "brand_id", referencedColumnName = "id")}, inverseJoinColumns = {
//        @JoinColumn(name = "supplier_id", referencedColumnName = "id")})
//    @ManyToMany(fetch = FetchType.EAGER)
//      @Fetch(FetchMode.SELECT)
//    private List<Supplier> supplierList1;
//-------------------------------------------------------------------------------------------------
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "brandId", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private List<Item> itemList;

    public Brand() {
    }

    public Brand(Integer id) {
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
        boolean validity = !name.isEmpty() && BrandDao.getByName(name).isEmpty();
        if (validity) {
            this.name = name;
        } else {
            this.name = null;
        }
        return validity;
    }

    @XmlTransient
    public List<Supplier> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<Supplier> supplierList) {
        this.supplierList = supplierList;
    }

//---------------------------------------------------------------------------------------
//    @XmlTransient
//    public List<Supplier> getSupplierList1() {
//        return supplierList1;
//    }
//
//    public void setSupplierList1(List<Supplier> supplierList1) {
//        this.supplierList1 = supplierList1;
//    }
//----------------------------------------------------------------------------------------

    @XmlTransient
    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
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
        if (!(object instanceof Brand)) {
            return false;
        }
        Brand other = (Brand) object;
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
