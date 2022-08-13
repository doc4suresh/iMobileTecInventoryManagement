/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Employee;
import entity.Role;
import entity.User;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author USER
 */
public class UserDao {
    public static ObservableList getAll() {
        return CommonDao.select("User.findAll");
    }

    public static User getByName(String name) {
        User user = null;
        HashMap hmap = new HashMap();
        hmap.put("name", name );
        ObservableList list = CommonDao.select("User.findByName", hmap);

        if (list != null && list.size() > 0) {
            user = (User) list.get(0);
        }

        return user;

    }

    public static void add(User user) throws DaoException {
        CommonDao.insert(user);
    }

    public static User getById(Integer id) {
        User user = null;
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        ObservableList list = CommonDao.select("User.findById", hmap);

        if (list != null && list.size() > 0) {
            user = (User) list.get(0);
        }

        return user;
    }

    public static void update(User user) {
        CommonDao.update(user);
    }

    public static void delete(User user) {
        CommonDao.delete(user);
    }

    public static ObservableList getAllByName(String name) {
        HashMap hmap = new HashMap();
        hmap.put("name", "%" + name + "%");

        return CommonDao.select("User.findAllByname", hmap);
    }

    public static ObservableList getAllByEmployee(Employee employeeId) {
        HashMap hmap = new HashMap();
        hmap.put("employeeId", employeeId);

        return CommonDao.select("User.findAllByEmployee", hmap);
    }

    public static ObservableList getAllByRole(Role role) { 
        HashMap hmap = new HashMap();
        hmap.put("role", role); 
        
        return CommonDao.select("User.findAllByRole",hmap);  }
    
    
      public static ObservableList getAllByNameRole(String name, Role role) {
        HashMap hmap = new HashMap();
        hmap.put("role", role);
        hmap.put("name", "%" + name + "%");
        return CommonDao.select("User.findAllByNameRole", hmap);

    }
}
