
package Common; 

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class MyResourceBundle extends ResourceBundle{ 

    HashMap<String,Object> hashMap ;  
  
    public void setHashMap (HashMap hashMap){ 

    this.hashMap = hashMap;   

    }

    

    @Override

    protected Object handleGetObject(String key) {

        return hashMap.get(key);  

    }

    @Override

    public Enumeration<String> getKeys() {

        throw new UnsupportedOperationException("Not supported yet."); 

    }

   

    

}
