/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author USER
 */
public class DaoException extends Exception {
    String message; 
 
    public void setMessage(String message){ this.message = message; } 
    
    @Override
    public String getMessage(){ return message; }  
}
