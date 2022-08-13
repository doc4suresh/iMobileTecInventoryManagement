/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Role;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author USER
 */
public class ModuleDao {
   public static ObservableList getAll(){ return CommonDao.select("Module.findAll"); } 
       
       
       
   public static ObservableList getUnassignedModuleByRole(Role role) { 
        HashMap hmap = new HashMap();
        hmap.put("role", role); 

        return CommonDao.select("Module.findUnAssignedModuleByRole",hmap);  }
}
