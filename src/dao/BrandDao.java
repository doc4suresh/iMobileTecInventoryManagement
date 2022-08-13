/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Brand;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author USER
 */
public class BrandDao {
    public static void add(Brand brand) throws DaoException{
        CommonDao.insert(brand);
    } 
    
    public static void update(Brand brand){
        CommonDao.update(brand);
    }
    
    public static void delete(Brand brand){
        CommonDao.delete(brand);
    } 
    
    public static ObservableList getAll(){ return CommonDao.select("Brand.findAll"); } 
     
    public static ObservableList getByName(String name) { 
        HashMap hmap = new HashMap();
        hmap.put("name", name);
        
        return CommonDao.select("Brand.findByName",hmap);  }
      
    public static Brand getById(Integer id) { 
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        
        return (Brand) CommonDao.select("Brand.findById",hmap).get(0);  } 
}
