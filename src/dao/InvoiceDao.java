/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Invoice;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author PSA
 */
public class InvoiceDao {

    public static void add(Invoice invoice) throws DaoException{
    CommonDao.insert(invoice);
    } 
      public static boolean add(Invoice invoice, List list) {

        boolean status = CommonDao.insert(invoice, list);

        return status;

    }
    public static void update(Invoice invoice){
    CommonDao.update(invoice);
    } 
    
    public static void delete(Invoice invoice){
    CommonDao.delete(invoice);
    } 
    
    public static ObservableList getAll() {
        return CommonDao.select("Invoice.findAll");
    }
    
    public static ObservableList getAllByReverse(){
        return CommonDao.select("Invoice.findAllByReverse");
    }
    
    public static String getNextInvoiceNo() {
        String no = "";
        List list = CommonDao.select("Invoice.findMaxInvoiceNo");
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
    
     public static Invoice getById(Integer id) { 
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        return (Invoice) CommonDao.select("Invoice.findById",hmap).get(0);  }     
    
    
    public static ObservableList getByDate(Date date) {
        HashMap hashMap = new HashMap();
        hashMap.put("date", date);
        return CommonDao.select("Invoice.findByDate", hashMap);
    }
    
    public static ObservableList getAllByNo(String no){
        HashMap hmap = new HashMap();
        hmap.put("no", "%" + no + "%");
        return CommonDao.select("Invoice.findAllByNo", hmap);
    }

    public static ObservableList<Invoice> getAllByName(String customer) {
        HashMap hmap = new HashMap();
        hmap.put ("customer", "%" + customer + "%");
        return CommonDao.select("Invoice.findAllName", hmap);
    }
    
    public static ObservableList getAllByContact(String contactno){
        HashMap hmap = new HashMap();
        hmap.put ("contactno", "%" + contactno +"%");
        return CommonDao.select("Invoice.findAllContact");
    }
    
    public static ObservableList getAllByNoContact(String no, String contactno){
        HashMap hmap = new HashMap();
        hmap.put ("no", "%" + no + "%");
        hmap.put ("contactno", "%" + contactno +"%");
        return CommonDao.select("Invoice.findAllNoContact", hmap);
    }
    
    public static ObservableList getAllByNoName(String no, String customer){
        HashMap hmap = new HashMap();
        hmap.put("no", "%" + no + "%");
        hmap.put ("customer", "%" + customer + "%");
        return CommonDao.select("Invoice.findAllNoName", hmap);
    }
    
    public static ObservableList getAllByContactName(String contactno, String customer){
        HashMap hmap = new HashMap();
        hmap.put ("contactno", "%" + contactno +"%");
        hmap.put ("customer", "%" + customer + "%");
        return CommonDao.select("Invoice.findAllContactName", hmap);
    }
    
    public static ObservableList getAllByNoContactName(String no, String contactno, String customer){
        HashMap hmap = new HashMap();
        hmap.put("no", "%" + no + "%");
        hmap.put ("contactno", "%" + contactno +"%");
        hmap.put ("customer", "%" + customer + "%");
        return CommonDao.select("Invoice.findAllNoContactName", hmap);
    }
}
