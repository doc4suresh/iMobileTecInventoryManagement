/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Supplier;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author USER
 */
public class SupplierDao {
    public static ObservableList getAll() {
        return CommonDao.select("Supplier.findAll");
    }

    public static void add(Supplier supplier) throws DaoException {
        CommonDao.insert(supplier);
    }

    public static Supplier getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Supplier) CommonDao.select("Supplier.findById", hmap).get(0);
    }

    public static void update(Supplier supplier) {
        CommonDao.update(supplier);
    }

    public static void delete(Supplier supplier) {
        CommonDao.delete(supplier);
    }

    public static ObservableList getAllByCompany(String company) {
        HashMap hmap = new HashMap();
        hmap.put("company", "%" + company + "%");

        return CommonDao.select("Supplier.findAllByCompany", hmap);
    }
    
    public static ObservableList getAllByContactPerson(String contactperson) {
        HashMap hmap = new HashMap();
        hmap.put("contactperson", "%" + contactperson + "%");

        return CommonDao.select("Supplier.findAllByContactPerson", hmap);
    }

    public static ObservableList getAllByCompanyContactPerson(String company, String contactperson) {
        HashMap hmap = new HashMap();
        hmap.put("company", "%" + company + "%");
        hmap.put("contactperson", "%" + contactperson + "%");
        return CommonDao.select("Supplier.findAllByCompanyContactPerson", hmap);

    }
    
}
