/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Designation;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author USER
 */
public class DesignationDao {

    public static void add(Designation designation) throws DaoException {
        CommonDao.insert(designation);
    }

    public static void update(Designation designation) {
        CommonDao.update(designation);
    }

    public static void delete(Designation designation) {
        CommonDao.delete(designation);
    }

    public static ObservableList getAll() {
        return CommonDao.select("Designation.findAll");
    }

    public static ObservableList getByName(String name) {
        HashMap hmap = new HashMap();
        hmap.put("name", name);

        return CommonDao.select("Designation.findByName", hmap);
    }

    public static Designation getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Designation) CommonDao.select("Designation.findById", hmap).get(0);
    }
}
