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
@Table(name = "grnote")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grnote.findAll", query = "SELECT g FROM Grnote g"),
    @NamedQuery(name = "Grnote.findById", query = "SELECT g FROM Grnote g WHERE g.id = :id"),
    @NamedQuery(name = "Grnote.findByNo", query = "SELECT g FROM Grnote g WHERE g.no = :no"),
    @NamedQuery(name = "Grnote.findByDatetime", query = "SELECT g FROM Grnote g WHERE g.datetime = :datetime"),
    @NamedQuery(name = "Grnote.findByGrossamount", query = "SELECT g FROM Grnote g WHERE g.grossamount = :grossamount"),
    @NamedQuery(name = "Grnote.findByTotaldiscountper", query = "SELECT g FROM Grnote g WHERE g.totaldiscountper = :totaldiscountper"),
    @NamedQuery(name = "Grnote.findByTotaldiscountvalue", query = "SELECT g FROM Grnote g WHERE g.totaldiscountvalue = :totaldiscountvalue"),
    @NamedQuery(name = "Grnote.findByNetamount", query = "SELECT g FROM Grnote g WHERE g.netamount = :netamount"),

    @NamedQuery(name = "Grnote.findAllReverse", query = "SELECT g FROM Grnote g ORDER BY g.id DESC"),
    @NamedQuery(name = "Grnote.findMaxNo", query = "SELECT max(no) FROM Grnote"),
    @NamedQuery(name = "Grnote.findBySupplier", query = "SELECT g FROM Grnote g WHERE g.porderId.supplierId = :supplier_id"),
    @NamedQuery(name = "Grnote.findBygrnStatus", query = "SELECT g FROM Grnote g WHERE g.grnotestatusId= :grnotestatus_id")
    })
public class Grnote implements Serializable {

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
    @Column(name = "totaldiscountper")
    private BigDecimal totaldiscountper;
    @Column(name = "totaldiscountvalue")
    private BigDecimal totaldiscountvalue;
    @Column(name = "netamount")
    private BigDecimal netamount;
    @JoinColumn(name = "grnotestatus_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Grnotestatus grnotestatusId;
    @JoinColumn(name = "porder_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Porder porderId;
    @JoinColumn(name = "user_employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User userEmployeeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grnoteId", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Grnotepoitem> grnotepoitemList;

    public Grnote() {
    }

    public Grnote(Integer id) {
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

    public BigDecimal getTotaldiscountper() {
        return totaldiscountper;
    }

    public boolean setTotaldiscountper(BigDecimal totaldiscountper) {
        boolean validity = totaldiscountper != null && totaldiscountper.toString().matches("\\d{1,3}(\\.\\d{1,2})?") && totaldiscountper.floatValue() <= 100;
        if (validity) {
            this.totaldiscountper = totaldiscountper;
        } else {
            this.totaldiscountper = null;
        }
        return validity;
    }

    public BigDecimal getTotaldiscountvalue() {
        return totaldiscountvalue;
    }

    public boolean setTotaldiscountvalue(BigDecimal totaldiscountvalue) {
        boolean validity = totaldiscountvalue != null && totaldiscountvalue.toString().matches("\\d{1,8}(\\.\\d{1,2})?") && (grossamount != null && grossamount.compareTo(totaldiscountvalue) >= 0);
        if (validity) {
            this.totaldiscountvalue = totaldiscountvalue;
        } else {
            this.totaldiscountvalue = null;
        }
        return validity;
    }

    public BigDecimal getNetamount() {
        return netamount;
    }

    public boolean setNetamount(BigDecimal netamount) {
        boolean validity = !netamount.toString().isEmpty();
        if (validity) {
            this.netamount = netamount;
        } else {
            this.netamount = null;
        }
        return validity;
    }

    public Grnotestatus getGrnotestatusId() {
        return grnotestatusId;
    }

    public void setGrnotestatusId(Grnotestatus grnotestatusId) {
        this.grnotestatusId = grnotestatusId;
    }

    public Porder getPorderId() {
        return porderId;
    }

    public void setPorderId(Porder porderId) {
        this.porderId = porderId;
    }

    public User getUserEmployeeId() {
        return userEmployeeId;
    }

    public void setUserEmployeeId(User userEmployeeId) {
        this.userEmployeeId = userEmployeeId;
    }

    @XmlTransient
    public List<Grnotepoitem> getGrnotepoitemList() {
        return grnotepoitemList;
    }

    public void setGrnotepoitemList(List<Grnotepoitem> grnotepoitemList) {
        this.grnotepoitemList = grnotepoitemList;
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
        if (!(object instanceof Grnote)) {
            return false;
        }
        Grnote other = (Grnote) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Grnote[ id=" + id + " ]";
    }
    
}
