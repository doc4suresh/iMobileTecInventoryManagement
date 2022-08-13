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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "invoice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invoice.findAll", query = "SELECT i FROM Invoice i")
    ,
    @NamedQuery(name = "Invoice.findById", query = "SELECT i FROM Invoice i WHERE i.id = :id")
    ,
    @NamedQuery(name = "Invoice.findByNo", query = "SELECT i FROM Invoice i WHERE i.no = :no")
    ,
    @NamedQuery(name = "Invoice.findByDatetime", query = "SELECT i FROM Invoice i WHERE i.datetime = :datetime")
    ,
    @NamedQuery(name = "Invoice.findByGrossamount", query = "SELECT i FROM Invoice i WHERE i.grossamount = :grossamount")
    ,
    @NamedQuery(name = "Invoice.findByTotaldiscont", query = "SELECT i FROM Invoice i WHERE i.totaldiscont = :totaldiscont")
    ,
    @NamedQuery(name = "Invoice.findByNetamount", query = "SELECT i FROM Invoice i WHERE i.netamount = :netamount")
    ,
    @NamedQuery(name = "Invoice.findByCustomer", query = "SELECT i FROM Invoice i WHERE i.customer = :customer")
    ,
    @NamedQuery(name = "Invoice.findByContactno", query = "SELECT i FROM Invoice i WHERE i.contactno = :contactno")
    ,

    @NamedQuery(name = "Invoice.findAllByReverse", query = "SELECT i FROM Invoice i ORDER BY i.id DESC")
    ,
    @NamedQuery(name = "Invoice.findAllByNo", query = "SELECT i FROM Invoice i WHERE i.no like :no ORDER BY i.id DESC")
    ,
    @NamedQuery(name = "Invoice.findAllName", query = "SELECT i FROM Invoice i WHERE i.customer like :customer ORDER BY i.id DESC")
    ,
    @NamedQuery(name = "Invoice.findAllContact", query = "SELECT i FROM Invoice i WHERE i.contactno like :contactno ORDER BY i.id DESC")
    ,
    @NamedQuery(name = "Invoice.findAllNoContact", query = "SELECT i FROM Invoice i WHERE i.no like :no and i.contactno like :contactno ORDER BY i.id DESC")
    ,
    @NamedQuery(name = "Invoice.findAllNoName", query = "SELECT i FROM Invoice i WHERE i.no like :no and i.customer like :customer ORDER BY i.id DESC")
    ,
    @NamedQuery(name = "Invoice.findAllContactName", query = "SELECT i FROM Invoice i WHERE i.contactno like :contactno and i.customer like :customer ORDER BY i.id DESC")
    ,
    @NamedQuery(name = "Invoice.findAllNoContactName", query = "SELECT i FROM Invoice i WHERE i.no like :no and i.contactno like :contactno and i.customer like :customer ORDER BY i.id DESC")
    ,
    @NamedQuery(name = "Invoice.findMaxInvoiceNo", query = "SELECT max(no) FROM Invoice")})

public class Invoice implements Serializable {

    @Column(name = "contactno")
    private String contactno;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "no")
    private String no;
    @Column(name = "datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetime;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "grossamount")
    private BigDecimal grossamount;
    @Column(name = "totaldiscont")
    private BigDecimal totaldiscont;
    @Column(name = "netamount")
    private BigDecimal netamount;
    @Column(name = "customer")
    private String customer;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoiceId", fetch = FetchType.EAGER)
    private List<Invoiceitem> invoiceitemList;
    @JoinColumn(name = "user_employee_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User userEmployeeId;

    public Invoice() {
    }

    public Invoice(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public BigDecimal getGrossamount() {
        return grossamount;
    }

    public void setGrossamount(BigDecimal grossamount) {
        this.grossamount = grossamount;
    }

    public BigDecimal getTotaldiscont() {
        return totaldiscont;
    }

    public void setTotaldiscont(BigDecimal totaldiscont) {
        this.totaldiscont = totaldiscont;
    }

    public BigDecimal getNetamount() {
        return netamount;
    }

    public void setNetamount(BigDecimal netamount) {
        this.netamount = netamount;
    }

    public String getCustomer() {
        return customer;
    }

    public boolean setCustomer(String customer) {
        boolean validity = customer != null && customer.matches("^[_A-Z]{1}[_A-Za-z]*([_A-Za-z]*\\s[_A-Za-z]*)*");
        if (validity) {
            this.customer = customer;
        } else {
            this.customer = null;
        }
        return validity;
    }

    @XmlTransient
    public List<Invoiceitem> getInvoiceitemList() {
        return invoiceitemList;
    }

    public void setInvoiceitemList(List<Invoiceitem> invoiceitemList) {
        this.invoiceitemList = invoiceitemList;
    }

    public User getUserEmployeeId() {
        return userEmployeeId;
    }

    public void setUserEmployeeId(User userEmployeeId) {
        this.userEmployeeId = userEmployeeId;
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
        if (!(object instanceof Invoice)) {
            return false;
        }
        Invoice other = (Invoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Invoice[ id=" + id + " ]";
    }

    public String getContactno() {
        return contactno;
    }

    public boolean setContactno(String contactno) {
        boolean validity = contactno != null && contactno.matches("0\\d{9}");
        if (validity) {
            this.contactno = contactno;
        } else {
            this.contactno = null;
        }
        return validity;
    }

}
