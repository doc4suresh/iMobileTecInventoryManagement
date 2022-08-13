
package dao;

import entity.Faults2;
import java.util.HashMap;
import javafx.collections.ObservableList;

public class Faults2Dao {
    public static ObservableList getAll(){ return CommonDao.select("Faults2.findAll"); }
    
    public static void add(Faults2 faults2) throws DaoException {
        CommonDao.insert(faults2);
    }
    
    public static void update(Faults2 faults2) {
        CommonDao.update(faults2);
    }
    
    public static void delete(Faults2 faults2) {
        CommonDao.delete(faults2);
    }
    
    public static ObservableList getByName(String name) {
        HashMap hmap = new HashMap();
        hmap.put("name", name);

        return CommonDao.select("Faults2.findByName", hmap);
    }
    
    public static Faults2 getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Faults2) CommonDao.select("Faults2.findById", hmap).get(0);
    }
}
