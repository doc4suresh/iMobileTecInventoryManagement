
package dao;

import entity.Grnote;
import entity.Grnotestatus;
import entity.Supplier;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javafx.collections.ObservableList;


public class GrnoteDao {

    public static String getNextNo() {
        String no = "";
        List list = CommonDao.select("Grnote.findMaxNo");
        if (list == null || list.size() == 0 || list.get(0) == null) {
            no = "00001";
        } else {
            String maxNo = list.get(0).toString().substring(3, 8);
            int nextNo = Integer.parseInt(maxNo) + 1;
            no = String.valueOf(nextNo);
            while (no.length() < 5) {
                no = "0" + no;
            }
        }

        return "GRN" + no;
    }

    public static void add(Grnote grnote) throws DaoException {
        CommonDao.insert(grnote);
    }

    public static void update(Grnote grnote) {
        CommonDao.update(grnote);
    }

    public static void delete(Grnote grnote) {
        CommonDao.delete(grnote);
    }

    public static ObservableList getAll() {
        return CommonDao.select("Grnote.findAll");
    }

    public static Grnote getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        return (Grnote) CommonDao.select("Grnote.findById", hmap).get(0);
    }

    public static ObservableList getByDate(Date date) {
        HashMap hashMap = new HashMap();
        hashMap.put("date", date);
        return CommonDao.select("Grnote.findByDate", hashMap);

    }

    public static ObservableList getAllReverse() {
        return CommonDao.select("Grnote.findAllReverse");
    }

    public static ObservableList getBySupplier(Supplier supplier) {
        HashMap hashMap = new HashMap();
        hashMap.put("supplier_id", supplier);
        return CommonDao.select("Grnote.findBySupplier", hashMap);
    }

    public static ObservableList getByStatus(Grnotestatus grnotestatus) {
        HashMap hashMap = new HashMap();
        hashMap.put("grnotestatus_id", grnotestatus);
        return CommonDao.select("Grnote.findBygrnStatus", hashMap);

    }
}
