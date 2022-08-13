package dao;

import entity.Faults1;
import java.util.HashMap;
import javafx.collections.ObservableList;

public class Faults1Dao {

    public static ObservableList getAll() {
        return CommonDao.select("Faults1.findAll");
    }
    
    public static ObservableList getByName(String name) {
        HashMap hmap = new HashMap();
        hmap.put("name", name);

        return CommonDao.select("Faults1.findByName", hmap);
    }
    
    public static Faults1 getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Faults1) CommonDao.select("Faults1.findById", hmap).get(0);
    }
    
    public static void add(Faults1 faults1) throws DaoException {
        CommonDao.insert(faults1);
    }
    
    public static void update(Faults1 faults1) {
        CommonDao.update(faults1);
    }
    
    public static void delete(Faults1 faults1) {
        CommonDao.delete(faults1);
    }
}
