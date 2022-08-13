/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import entity.Poitem;

/**
 *
 * @author B
 */
public class PoitemDao {
     public static void update(Poitem poitem){
    CommonDao.update(poitem);
    } 
}
