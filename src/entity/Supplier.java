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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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
@Table(name = "supplier")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Supplier.findAll", query = "SELECT s FROM Supplier s"),
    @NamedQuery(name = "Supplier.findById", query = "SELECT s FROM Supplier s WHERE s.id = :id"),
    @NamedQuery(name = "Supplier.findByCompany", query = "SELECT s FROM Supplier s WHERE s.company = :company"),
    @NamedQuery(name = "Supplier.findByContactperson", query = "SELECT s FROM Supplier s WHERE s.contactperson = :contactperson"),
    @NamedQuery(name = "Supplier.findByLandphone", query = "SELECT s FROM Supplier s WHERE s.landphone = :landphone"),
    @NamedQuery(name = "Supplier.findByMobilephone", query = "SELECT s FROM Supplier s WHERE s.mobilephone = :mobilephone"),
    @NamedQuery(name = "Supplier.findByEmail", query = "SELECT s FROM Supplier s WHERE s.email = :email"),

    @NamedQuery(name = "Supplier.findAllByCompany", query = "SELECT s FROM Supplier s WHERE s.company like :company"),
    @NamedQuery(name = "Supplier.findAllByContactPerson", query = "SELECT s FROM Supplier s WHERE s.contactperson like :contactperson"),
    @NamedQuery(name = "Supplier.findAllByCompanyContactPerson", query = "SELECT s FROM Supplier s WHERE s.contactperson = :contactperson and s.company like :company")
    })
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "company")
    private String company;
    @Column(name = "contactperson")
    private String contactperson;
    @Lob
    @Column(name = "address")
    private String address;
    @Column(name = "landphone")
    private String landphone;
    @Column(name = "mobilephone")
    private String mobilephone;
    @Column(name = "email")
    private String email;
    @ManyToMany(mappedBy = "supplierList", fetch = FetchType.EAGER)
    private List<Brand> brandList;
//      REMOVE THIS-------------------------------------------------------------------------------- 
//    @ManyToMany(mappedBy = "supplierList1", fetch = FetchType.EAGER)
//    private List<Brand> brandList1;
//-------------------------------------------------------------------------------------------------
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplierId", fetch = FetchType.EAGER)
    private List<Item> itemList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplierId", fetch = FetchType.EAGER)
    private List<Porder> porderList;

    public Supplier() {
    }

    public Supplier(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public boolean setCompany(String company) {
        boolean validity = company != null && company.matches("^[_A-Za-z0-9-/.:#@,\\s]{1}[_A-Za-z0-9-/.:#@,\\s]*");  
        if(validity){this.company = company; } else { this.company=null; } 
        return validity;
    }

    public String getContactperson() {
        return contactperson;
    }

    public boolean setContactperson(String contactperson) {
        boolean validity = contactperson != null && contactperson.matches("^[_A-Za-z-/.,\\s]{4}[_A-Za-z-/.\\s]*");  
        if(validity){this.contactperson = contactperson; } else { this.contactperson=null; } 
        return validity;
    }

    public String getAddress() {
        return address;
    }

    public boolean setAddress(String address) {
        boolean validity = address != null && address.matches("^[_A-Za-z0-9-/.:,\\s]{5}[_A-Za-z0-9-/.:,\\s]*");  
        if(validity){this.address = address; } else { this.address=null; } 
        return validity;
    }

    public String getLandphone() {
        return landphone;
    }

    public boolean setLandphone(String landphone) {
        boolean validity; 
        if(landphone==null || landphone.isEmpty()){ validity=true; this.landphone=null;}
        else{
           landphone = landphone.trim(); 
           if(landphone.matches("0\\d{9}"))
           {this.landphone=landphone; validity=true;}
           else{ this.landphone=null; validity=false;}           
        }
        return validity;
    }
    
    public String getMobilephone() {
        return mobilephone;
    }

    public boolean setMobilephone(String mobilephone) {
        boolean validity = mobilephone != null && mobilephone.matches("07\\d{8}");  
        if(validity){this.mobilephone = mobilephone; } else { this.mobilephone=null; } 
        return validity;
    }

    public String getEmail() {
        return email;
    }

    public boolean setEmail(String email) {
        boolean validity; 
        if(email==null || email.isEmpty()){ validity=true; this.email=null;}
        else{
           email = email.trim(); 
           if(email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"))
           {this.email=email; validity=true;}
           else{ this.email=null; validity=false;}           
        }
        return validity; 
    }

    @XmlTransient
    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }

//      REMOVE THIS--------------------------------------------------------------------------------    
//    @XmlTransient
//    public List<Brand> getBrandList1() {
//        return brandList1;
//    }
//
//    public void setBrandList1(List<Brand> brandList1) {
//        this.brandList1 = brandList1;
//    }--------------------------------------------------------------------------------------------

    @XmlTransient
    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    @XmlTransient
    public List<Porder> getPorderList() {
        return porderList;
    }

    public void setPorderList(List<Porder> porderList) {
        this.porderList = porderList;
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
        if (!(object instanceof Supplier)) {
            return false;
        }
        Supplier other = (Supplier) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return company;
    }
    
}
