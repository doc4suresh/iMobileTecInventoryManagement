/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Itemtype;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author USER
 */
public class ItemtypeDao {
    public static void add(Itemtype itemtype) throws DaoException{
    CommonDao.insert(itemtype);
    } 
    
     public static void update(Itemtype itemtype){
    CommonDao.update(itemtype);
    }
    
      public static void delete(Itemtype itemtype){
    CommonDao.delete(itemtype);
    } 
    
     public static ObservableList getAll(){ return CommonDao.select("Itemtype.findAll"); } 
     
      public static ObservableList getByName(String name) { 
        HashMap hmap = new HashMap();
        hmap.put("name", name);
        
        return CommonDao.select("Itemtype.findByName",hmap);  }
      
       public static Itemtype getById(Integer id) { 
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        
        return (Itemtype) CommonDao.select("Itemtype.findById",hmap).get(0);  } 
}
