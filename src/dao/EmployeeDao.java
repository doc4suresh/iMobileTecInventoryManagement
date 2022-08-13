/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Designation;
import entity.Employee;
import entity.Employeestatus;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author USER
 */
public class EmployeeDao {
    public static ObservableList getAll() {
        return CommonDao.select("Employee.findAll");
    }

    public static void add(Employee employee) throws DaoException {
        CommonDao.insert(employee);
    }

    public static Employee getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Employee) CommonDao.select("Employee.findById", hmap).get(0);
    }

    public static void update(Employee employee) {
        CommonDao.update(employee);
    }

    public static void delete(Employee employee) {
        CommonDao.delete(employee);
    }

    public static ObservableList getAllByName(String name) {
        HashMap hmap = new HashMap();
        hmap.put("name", "%" + name + "%");

        return CommonDao.select("Employee.findAllByName", hmap);
    }

    public static ObservableList getAllByDesignation(Designation designation) {
        HashMap hmap = new HashMap();
        hmap.put("designation", designation);

        return CommonDao.select("Employee.findAllByDesignation", hmap);
    }

    public static ObservableList getAllByStatus(Employeestatus status) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);

        return CommonDao.select("Employee.findAllByStatus", hmap);
    }

    public static ObservableList getAllByNameStatus(String name, Employeestatus status) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);
        hmap.put("name", "%" + name + "%");
        return CommonDao.select("Employee.findAllByNameStatus", hmap);
    }

    public static ObservableList getAllByNameDesignation(String name, Designation designation) {
        HashMap hmap = new HashMap();
        hmap.put("designation", designation);
        hmap.put("name", "%" + name + "%");
        return CommonDao.select("Employee.findAllByNameDesignation", hmap);

    }

    public static ObservableList getAllByStatusDesignation(Employeestatus status, Designation designation) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);
        hmap.put("designation", designation);
        return CommonDao.select("Employee.findAllByStatusDesignation", hmap);

    }

    public static ObservableList getAllByNameStatusDesignation(String name, Employeestatus status, Designation designation) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);
        hmap.put("designation", designation);
        hmap.put("name", "%" + name + "%");
        return CommonDao.select("Employee.findAllByNameStatusDesignation", hmap);

    }

    public static ObservableList getAllExeptUsers() {
        return CommonDao.select("Employee.findAllExeptUsers");
    }    
}
