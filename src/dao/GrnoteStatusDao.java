/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import entity.Grnotestatus;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author PSA
 */
public class GrnoteStatusDao {
     public static ObservableList getAll(){
    return CommonDao.select("Grnotestatus.findAll");
    } 
     
      public static Grnotestatus getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Grnotestatus) CommonDao.select("Grnotestatus.findById", hmap).get(0);
    }
}
