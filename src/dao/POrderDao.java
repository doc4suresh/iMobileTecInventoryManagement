/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Porder;
import entity.Postatus;
import entity.Supplier;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javafx.collections.ObservableList;


public class POrderDao {
 public static String getNextNo() {
 String no = "";
 List list = CommonDao.select("Porder.findMaxNo");
  if (list == null || list.size() == 0 || list.get(0) == null) {
 no = "00001";
        } else {
 String maxNo = list.get(0).toString().substring(3,8);
            int nextNo = Integer.parseInt(maxNo) + 1;
            no = String.valueOf(nextNo);

            while (no.length() < 5) {

                no = "0" + no;
            }
        }

        return "PUR" + no;
    }
    public static void add(Porder porder)throws DaoException{
    CommonDao.insert(porder);
    } 
    
    public static void update(Porder porder){
    CommonDao.update(porder);
    } 
    
    public static void delete(Porder porder){
    CommonDao.delete(porder);
    } 
    
    public static ObservableList getAll() {
        return CommonDao.select("Porder.findAll");
    }
    
    public static ObservableList getAllReverse() {
        return CommonDao.select("Porder.findAllReverse");
    }
    
    public static Porder getById(Integer id) { 
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        return (Porder) CommonDao.select("Porder.findById",hmap).get(0);  }
 
    public static ObservableList getAllByNo(String no) {
        HashMap hmap = new HashMap();
        hmap.put("no", "%" + no + "%");

        return CommonDao.select("Porder.findAllByNo", hmap);
    }
    
    
    public static ObservableList getByDate(Date date) {
        HashMap hashMap = new HashMap();
        hashMap.put("date", date);
        return CommonDao.select("Porder.findByDate", hashMap);

    }

    public static ObservableList getBySupplier(Supplier supplier) {
        HashMap hashMap = new HashMap();
        hashMap.put("supplier_id", supplier);
        return CommonDao.select("Porder.findBySupplier", hashMap);

    }
      public static ObservableList getByStatus(Postatus postatus) {
        HashMap hashMap = new HashMap();
        hashMap.put("postatus_id", postatus);
        return CommonDao.select("Porder.findByStatus", hashMap);

    }
    public static ObservableList getBySupplierApproved(Supplier supplier) {
        HashMap hashMap = new HashMap();
        hashMap.put("supplier_id", supplier);
        return CommonDao.select("Porder.findBySupplierApproved", hashMap);

    }
}
