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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import util.Security;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),

    @NamedQuery(name = "User.findAllByRole", query = "SELECT u FROM User u WHERE :role MEMBER OF u.roleList"),
    @NamedQuery(name = "User.findAllByEmployee", query = "SELECT u FROM User u WHERE u.employeeId = :employeeId"),
    @NamedQuery(name = "User.findAllByname", query = "SELECT u FROM User u WHERE u.employeeId.name like :name"),
    @NamedQuery(name = "User.findAllByNameRole", query = "SELECT u FROM User u WHERE :role MEMBER OF u.roleList and u.employeeId.name like :name")})
public class User implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId", fetch = FetchType.EAGER)
    private List<Repair> repairList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
    @ManyToMany(mappedBy = "userList", fetch = FetchType.EAGER)
    private List<Role> roleList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userEmployeeId", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Grnote> grnoteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userEmployeeId", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Porder> porderList;
    @OneToMany(mappedBy = "userEmployeeId", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Invoice> invoiceList;
    @JoinColumn(name = "userstatus_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Userstatus userstatusId;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Employee employeeId;

    public User() {
    }

    public User(Integer id) {
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
        boolean validity = name != null && name.matches("[A-Za-z0-9]{3,15}");
        if (validity) {
            this.name = name;
        } else {
            this.name = null;
        }
        return validity;
    }

    public String getPassword() {
        return password;
    }

    public boolean setPassword(String password) {
        boolean validity = password.matches("[A-Za-z0-9]{5,20}");
        if (validity) {
            this.password = Security.getHash(password);
        } else {
            this.password = null;
        }
        return validity;
    }

    @XmlTransient
    public List<Role> getRoleList() {
        return roleList;
    }

    public boolean setRoleList(List<Role> roleList) {
        boolean validity = roleList != null && !roleList.isEmpty();
        if (validity) {
            this.roleList = roleList;
        } else {
            this.roleList = null;
        }
        return validity;
    }

    @XmlTransient
    public List<Grnote> getGrnoteList() {
        return grnoteList;
    }

    public void setGrnoteList(List<Grnote> grnoteList) {
        this.grnoteList = grnoteList;
    }

    @XmlTransient
    public List<Porder> getPorderList() {
        return porderList;
    }

    public void setPorderList(List<Porder> porderList) {
        this.porderList = porderList;
    }

    @XmlTransient
    public List<Invoice> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
    }

    public Userstatus getUserstatusId() {
        return userstatusId;
    }

    public void setUserstatusId(Userstatus userstatusId) {
        this.userstatusId = userstatusId;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    @XmlTransient
    public List<Repair> getRepairList() {
        return repairList;
    }

    public void setRepairList(List<Repair> repairList) {
        this.repairList = repairList;
    }
    
}
