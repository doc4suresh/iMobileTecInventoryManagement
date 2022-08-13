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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "repair")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Repair.findAll", query = "SELECT r FROM Repair r")
    ,
    @NamedQuery(name = "Repair.findById", query = "SELECT r FROM Repair r WHERE r.id = :id")
    ,
    @NamedQuery(name = "Repair.findByItemsTaken", query = "SELECT r FROM Repair r WHERE r.itemsTaken = :itemsTaken")
    ,
    @NamedQuery(name = "Repair.findByTookDate", query = "SELECT r FROM Repair r WHERE r.tookDate = :tookDate")
    ,
    @NamedQuery(name = "Repair.findByHandedOverDate", query = "SELECT r FROM Repair r WHERE r.handedOverDate = :handedOverDate")
    ,
    @NamedQuery(name = "Repair.findByPrice", query = "SELECT r FROM Repair r WHERE r.price = :price")
    ,
    @NamedQuery(name = "Repair.findAllReverse", query = "SELECT r FROM Repair r ORDER BY r.id DESC")
    ,
    @NamedQuery(name = "Repair.findMaxInvoiceNo", query = "SELECT max(jobno) FROM Repair")
    ,
    @NamedQuery(name = "Repair.findAllByJobNo", query = "SELECT r FROM Repair r WHERE r.jobno = :jobno")
    ,
    @NamedQuery(name = "Repair.findAllByJobNoItem", query = "SELECT r FROM Repair r WHERE r.item = :item and r.jobno = :jobno")
    ,
    @NamedQuery(name = "Repair.findImmediateHandover", query = "SELECT r FROM Repair r WHERE r.agreeddate <= CURDATE() AND r.repairStatusid = 1"),
    
    @NamedQuery(name = "Repair.findAllByFaults1", query = "SELECT r FROM Repair r WHERE r.faults1Id = :faults1"),})

public class Repair implements Serializable {

    @JoinColumn(name = "faults1_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Faults1 faults1Id;
    @JoinColumn(name = "faults2_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Faults2 faults2Id;

    @Column(name = "actualcost")
    private BigDecimal actualcost;
    @Column(name = "item")
    private String item;
    @Column(name = "imeino")
    private String imeino;
    @Column(name = "agreeddate")
    @Temporal(TemporalType.DATE)
    private Date agreeddate;
    @Column(name = "itemsTaken")
    private String itemsTaken;
    @Column(name = "customer")
    private String customer;
    @Column(name = "contactno")
    private String contactno;
    @Column(name = "jobno")
    private String jobno;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "tookDate")
    @Temporal(TemporalType.DATE)
    private Date tookDate;
    @Column(name = "handedOverDate")
    @Temporal(TemporalType.DATE)
    private Date handedOverDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private BigDecimal price;
    @JoinColumn(name = "repairStatus_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Repairstatus repairStatusid;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User userId;

    public Repair() {
    }

    public Repair(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTookDate() {
        return tookDate;
    }

    public void setTookDate(Date tookDate) {
        this.tookDate = tookDate;
    }

    public Date getHandedOverDate() {
        return handedOverDate;
    }

    public void setHandedOverDate(Date handedOverDate) {
        this.handedOverDate = handedOverDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean setPrice(BigDecimal price) {
        this.price = price;
        return price != null;
    }

    public BigDecimal getActualcost() {
        return actualcost;
    }

    public boolean setActualcost(BigDecimal actualcost) {
        this.actualcost = actualcost;
        return actualcost != null;
    }

    public Repairstatus getRepairStatusid() {
        return repairStatusid;
    }

    public void setRepairStatusid(Repairstatus repairStatusid) {
        this.repairStatusid = repairStatusid;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
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
        if (!(object instanceof Repair)) {
            return false;
        }
        Repair other = (Repair) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Repair[ id=" + id + " ]";
    }

    public String getJobno() {
        return jobno;
    }

    public void setJobno(String jobno) {
        this.jobno = jobno;
    }

    public String getCustomer() {
        return customer;
    }

    public boolean setCustomer(String customer) {
        boolean validity = customer != null && customer.matches("^[_A-Za-z]{3}[_A-Za-z]*([_A-Za-z]*\\s[_A-Za-z]*)*");
        if (validity) {
            this.customer = customer;
        } else {
            this.customer = null;
        }
        return validity;
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

    public String getItemsTaken() {
        return itemsTaken;
    }

    public boolean setItemsTaken(String itemsTaken) {
        boolean validity = itemsTaken != null && itemsTaken.matches("^[_A-Za-z0-9]*([_A-Za-z0-9]*\\s[_A-Za-z0-9]*)*");
        if (validity) {
            this.itemsTaken = itemsTaken;
        } else {
            this.itemsTaken = null;
        }
        return validity;
    }

    public String getItem() {
        return item;
    }

    public boolean setItem(String item) {
        boolean validity = item != null && item.matches("^[_A-Za-z0-9-/.:,\\s]{1,20}");
        if (validity) {
            this.item = item;
        } else {
            this.item = null;
        }
        return validity;
    }

    public String getImeino() {
        return imeino;
    }

    public boolean setImeino(String imeino) {
        boolean validity = imeino != null && imeino.matches("\\d{15}");
        if (validity) {
            this.imeino = imeino;
        } else {
            this.imeino = null;
        }
        return validity;
    }

    public Date getAgreeddate() {
        return agreeddate;
    }

    public void setAgreeddate(Date agreeddate) {
        this.agreeddate = agreeddate;
    }

    public Faults1 getFaults1Id() {
        return faults1Id;
    }

    public void setFaults1Id(Faults1 faults1Id) {
        this.faults1Id = faults1Id;
    }

    public Faults2 getFaults2Id() {
        return faults2Id;
    }

    public void setFaults2Id(Faults2 faults2Id) {
        this.faults2Id = faults2Id;
    }

}
