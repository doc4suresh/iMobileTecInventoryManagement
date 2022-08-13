/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import entity.Postatus;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author PSA
 */
public class POStatusDao {
     public static ObservableList getAll(){
    return CommonDao.select("Postatus.findAll");
    } 
     
       public static Postatus getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Postatus) CommonDao.select("Postatus.findById", hmap).get(0);
    }
     
}
