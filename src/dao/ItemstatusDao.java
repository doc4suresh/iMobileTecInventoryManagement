/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Itemstatus;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author USER
 */
public class ItemstatusDao {
    public static ObservableList getAll(){ return CommonDao.select("Itemstatus.findAll"); }
    
    public static Itemstatus getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        return (Itemstatus) CommonDao.select("Itemstatus.findById", hmap).get(0);
    }
}
