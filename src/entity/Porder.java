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
@Table(name = "porder")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Porder.findAll", query = "SELECT p FROM Porder p"),
    @NamedQuery(name = "Porder.findById", query = "SELECT p FROM Porder p WHERE p.id = :id"),
    @NamedQuery(name = "Porder.findByNo", query = "SELECT p FROM Porder p WHERE p.no = :no"),
    @NamedQuery(name = "Porder.findByDatetime", query = "SELECT p FROM Porder p WHERE p.datetime = :datetime"),
    @NamedQuery(name = "Porder.findByTotal", query = "SELECT p FROM Porder p WHERE p.total = :total"),

    @NamedQuery(name = "Porder.findBySupplier", query = "SELECT p FROM Porder p WHERE p.supplierId = :supplier_id"),
    @NamedQuery(name = "Porder.findBySupplierApproved", query = "SELECT p FROM Porder p WHERE p.supplierId = :supplier_id AND (postatusId=2 OR postatusId=3)"),
    @NamedQuery(name = "Porder.findAllReverse", query = "SELECT p FROM Porder p ORDER BY p.id DESC"),
    @NamedQuery(name = "Porder.findMaxNo", query = "SELECT max(no) FROM Porder"),
    @NamedQuery(name = "Porder.findByStatus", query = "SELECT p FROM Porder p WHERE p.postatusId = :postatus_id")})

public class Porder implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "porderId", fetch = FetchType.EAGER)
    private List<Grnote> grnoteList;

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
    @Column(name = "total")
    private BigDecimal total;
    @JoinColumn(name = "postatus_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Postatus postatusId;
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Supplier supplierId;
    @JoinColumn(name = "user_employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User userEmployeeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "porderId", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Poitem> poitemList;

    public Porder() {
    }

    public Porder(Integer id) {
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Postatus getPostatusId() {
        return postatusId;
    }

    public void setPostatusId(Postatus postatusId) {
        this.postatusId = postatusId;
    }

    public Supplier getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Supplier supplierId) {
        this.supplierId = supplierId;
    }

    public User getUserEmployeeId() {
        return userEmployeeId;
    }

    public void setUserEmployeeId(User userEmployeeId) {
        this.userEmployeeId = userEmployeeId;
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
        if (!(object instanceof Porder)) {
            return false;
        }
        Porder other = (Porder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return no;
    }

    @XmlTransient
    public List<Grnote> getGrnoteList() {
        return grnoteList;
    }

    public void setGrnoteList(List<Grnote> grnoteList) {
        this.grnoteList = grnoteList;
    }
    
}
