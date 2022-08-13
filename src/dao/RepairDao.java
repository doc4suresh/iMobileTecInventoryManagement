
package dao;

import entity.Faults1;
import entity.Repair;
import entity.Repairstatus;
import java.util.HashMap;
import java.util.List;
import javafx.collections.ObservableList;

public class RepairDao {
    public static void add(Repair repair) throws DaoException {
        CommonDao.insert(repair);
    }
    
    public static void update(Repair repair) {
        CommonDao.update(repair);
    }
    
    public static void delete(Repair repair) {
        CommonDao.delete(repair);
    }
    
    public static Repair getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Repair) CommonDao.select("Repair.findById", hmap).get(0);
    }
    
    public static ObservableList getAll(){ return CommonDao.select("Repair.findAll"); }
    
    public static ObservableList getAllReverse() {
        return CommonDao.select("Repair.findAllReverse");
    }
    
    public static ObservableList getAllByJobNo(String jobno) {
        HashMap hmap = new HashMap();
        hmap.put("jobno", "%" + jobno + "%");

        return CommonDao.select("Repair.findAllByJobNo", hmap);
    }
    
    
    public static ObservableList getAllByItem(String item) {
        HashMap hmap = new HashMap();
        hmap.put("item", "%" + item + "%");

        return CommonDao.select("Repair.findAllByItem", hmap);
    }
    
    public static ObservableList getAllByStatus(Repairstatus status) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);

        return CommonDao.select("Repair.findAllByStatus", hmap);
    }
    
    public static ObservableList getAllByJobNoStatus(String jobno, Repairstatus status) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);
        hmap.put("jobno", "%" + jobno + "%");
        return CommonDao.select("Repair.findAllByJobNoStatus", hmap);
    }
    
    public static ObservableList getAllByJobNoItem(String jobno, String item) {
        HashMap hmap = new HashMap();
        hmap.put("item", "%" + item + "%");
        hmap.put("jobno", "%" + jobno + "%");
        return CommonDao.select("Repair.findAllByJobNoItem", hmap);
    }
    
    public static ObservableList getAllByItemStatus(String item, Repairstatus status) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);
        hmap.put("item", "%" + item + "%");
        return CommonDao.select("Repair.findAllByItemStatus", hmap);
    }
    
    public static ObservableList getAllByJobNoItemStatus(String jobno, String item, Repairstatus status) {
        HashMap hmap = new HashMap();
        hmap.put("jobno", "%" + jobno + "%");
        hmap.put("item", "%" + item + "%");
        hmap.put("status", status);
        return CommonDao.select("Repair.findAllByJobNoItemStatus", hmap);
    }
    
    public static String getNextJobNo() {
        String no = "";
        List list = CommonDao.select("Repair.findMaxInvoiceNo");
        if (list == null || list.size() == 0 || list.get(0) == null) {
        no = "1000000001";
        } else {
        String maxNo = (String) list.get(0);//.toString().substring(3, 8);
            int nextNo = Integer.parseInt(maxNo) + 1;
            no = String.valueOf(nextNo);
        while (no.length() < 9) {
        no = "0" + no;
            }
        }
        return no;
    }
    
    public static ObservableList getAllImmediat(Repair repair) {
        HashMap hashMap = new HashMap();
        hashMap.put("repair_id", repair);
        return CommonDao.select("Repair.findImmediateHandover", hashMap);
    }
    
    public static ObservableList getAllByFaults1(Faults1 faults1) {
        HashMap hmap = new HashMap();
        hmap.put("faults1", faults1);

        return CommonDao.select("Repair.findAllByFaults1", hmap);
    }
}
