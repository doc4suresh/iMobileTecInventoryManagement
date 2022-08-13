/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Module;
import entity.Privilage;
import entity.Role;
import entity.User;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author USER
 */
public class PrivilageDao {
    public static ObservableList getAll() {
        return CommonDao.select("Privilage.findAll");
    }

    public static void add(Privilage privilage) throws DaoException {
        CommonDao.insert(privilage);
    }

    public static void delete(Privilage privilage) {
        CommonDao.delete(privilage);
    }
    public static Privilage getById(Integer id) {
        Privilage privilage = null;
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        ObservableList list = CommonDao.select("Privilage.findById", hmap);

        if (list != null && list.size() > 0) {
            privilage = (Privilage) list.get(0);
        }

        return privilage;
    }

    public static void update(Privilage privilage) {
        CommonDao.update(privilage);
    }

    public static ObservableList getAllByUser(User user) {
        HashMap hmap = new HashMap();

        hmap.put("user", user);
        return CommonDao.select("Privilage.findAllByUser", hmap);
    }
    
     public static ObservableList getAllByRole(Role role) {
        HashMap hmap = new HashMap();
        hmap.put("role", role);

        return CommonDao.select("Privilage.findAllByRole", hmap);
        
        
    }
     
       public static ObservableList getAllByModule(Module module) {
        HashMap hmap = new HashMap();
        hmap.put("module", module);

        return CommonDao.select("Privilage.findAllByModule", hmap);
    }
        public static ObservableList getAllByRoleModule(Role role, Module module) {
        HashMap hmap = new HashMap();
        hmap.put("role", role);
        hmap.put("module", module);
        return CommonDao.select("Privilage.findAllByRoleModule", hmap);

    }
}
