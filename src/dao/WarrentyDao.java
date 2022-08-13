/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Warrenty;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author USER
 */
public class WarrentyDao {
    public static void add(Warrenty warrenty) throws DaoException{
        CommonDao.insert(warrenty);
    } 
    
    public static void update(Warrenty warrenty){
        CommonDao.update(warrenty);
    }
    
    public static void delete(Warrenty warrenty){
        CommonDao.delete(warrenty);
    } 
    
    public static ObservableList getAll(){ return CommonDao.select("Warrenty.findAll"); } 
     
    public static ObservableList getByName(String name) { 
        HashMap hmap = new HashMap();
        hmap.put("name", name);
        
        return CommonDao.select("Warrenty.findByName",hmap);  }
      
    public static Warrenty getById(Integer id) { 
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        
        return (Warrenty) CommonDao.select("Warrenty.findById",hmap).get(0);  } 
    
}
