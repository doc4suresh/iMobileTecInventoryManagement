/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Brand;
import entity.Item;
import entity.Itemstatus;
import entity.Itemtype;
import entity.Supplier;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author USER
 */
public class ItemDao {
    public static ObservableList getAll() {
        return CommonDao.select("Item.findAll");
    }

    public static void add(Item item) throws DaoException {
        CommonDao.insert(item);
    }

    public static Item getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Item) CommonDao.select("Item.findById", hmap).get(0);
    }

    public static void update(Item item) {
        CommonDao.update(item);
    }

    public static void delete(Item item) {
        CommonDao.delete(item);
    }

    public static ObservableList getAllByName(String name) {
        HashMap hmap = new HashMap();
        hmap.put("name", "%" + name + "%");

        return CommonDao.select("Item.findAllByName", hmap);
    }

    public static ObservableList getAllByType(Itemtype type) {
        HashMap hmap = new HashMap();
        hmap.put("type", type);

        return CommonDao.select("Item.findAllByType", hmap);
    }
    
    public static ObservableList getAllByBrand(Brand brand) {
        HashMap hmap = new HashMap();
        hmap.put("brand", brand);

        return CommonDao.select("Item.findAllByBrand", hmap);
    }

    public static ObservableList getAllByStatus(Itemstatus status) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);

        return CommonDao.select("Item.findAllByStatus", hmap);
    }

    public static ObservableList getAllByNameStatus(String name, Itemstatus status) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);
        hmap.put("name", "%" + name + "%");
        return CommonDao.select("Item.findAllByNameStatus", hmap);
    }
    
    public static ObservableList getAllByNameType(String name, Itemtype type) {
        HashMap hmap = new HashMap();
        hmap.put("type", type);
        hmap.put("name", "%" + name + "%");
        return CommonDao.select("Item.findAllByNameType", hmap);
    }

    public static ObservableList getAllByNameBrand(String name, Brand brand) {
        HashMap hmap = new HashMap();
        hmap.put("brand", brand);
        hmap.put("name", "%" + name + "%");
        return CommonDao.select("Item.findAllByNameBrand", hmap);
    }

    public static ObservableList getAllByStatusBrand(Itemstatus status, Brand brand) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);
        hmap.put("brand", brand);
        return CommonDao.select("Item.findAllByStatusBrand", hmap);
    }
    
    public static ObservableList getAllByStatusType(Itemstatus status, Itemtype type) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);
        hmap.put("type", type);
        return CommonDao.select("Item.findAllByStatusType", hmap);
    }
    
    public static ObservableList getAllByTypeBrand(Itemtype type, Brand brand) {
        HashMap hmap = new HashMap();
        hmap.put("type", type);
        hmap.put("brand", brand);
        return CommonDao.select("Item.findAllByTypeBrand", hmap);
    }
    
    public static ObservableList getAllByNameStatusBrand(String name, Itemstatus status, Brand brand) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);
        hmap.put("brand", brand);
        hmap.put("name", "%" + name + "%");
        return CommonDao.select("Item.findAllByNameStatusBrand", hmap);
    }
    
    public static ObservableList getAllByNameStatusType(String name, Itemstatus status, Itemtype type) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);
        hmap.put("type", type);
        hmap.put("name", "%" + name + "%");
        return CommonDao.select("Item.findAllByNameStatusType", hmap);
    }
    
    public static ObservableList getAllByNameBrandType(String name, Brand brand, Itemtype type) {
        HashMap hmap = new HashMap();
        hmap.put("brand", brand);
        hmap.put("type", type);
        hmap.put("name", "%" + name + "%");
        return CommonDao.select("Item.findAllByNameBrandType", hmap);
    }
    
    public static ObservableList getAllByStatusBrandType(Itemstatus status, Brand brand, Itemtype type) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);
        hmap.put("brand", brand);
        hmap.put("type", type);
        return CommonDao.select("Item.findAllByStatusBrandType", hmap);
    }
    
    public static ObservableList getAllByNameStatusBrandType(String name, Itemstatus status, Brand brand, Itemtype type) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);
        hmap.put("brand", brand);
        hmap.put("name", "%" + name + "%");
        hmap.put("type", type);
        return CommonDao.select("Item.findAllByNameStatusBrandType", hmap);
    }
    
    public static ObservableList<Item> getAllBySupplier(Supplier supplier) {
        HashMap hashMap = new HashMap();
        hashMap.put("supplier", supplier);
        return CommonDao.select("Item.findBySupplier", hashMap);
    }
    
    public static ObservableList getAllBySupplierType(Supplier supplier, Itemtype type) {
        HashMap hashMap = new HashMap();
        hashMap.put("supplier", supplier);
        hashMap.put("type", type);
        return CommonDao.select("Item.findAllBySupplierType", hashMap);
    }
    
    public static List<Item> getBySellingAndItemAndStatus(Item item, BigDecimal Sprice, Itemstatus status) {

        HashMap hashMap = new HashMap();
        hashMap.put("item_id", item);
        hashMap.put("sprice", Sprice);
        hashMap.put("itemstatusId", status);

        List<Item> list = CommonDao.select("Item.findBySellingAndItemAndStatus", hashMap);

        return list;
    }
    
    public static ObservableList getAllByItem(Item item) {
        HashMap hashMap = new HashMap();
        hashMap.put("item_id", item);
        return CommonDao.select("Item.findByItem", hashMap);
    }
    
    public static ObservableList getAllReorder(Item item) {
        HashMap hashMap = new HashMap();
        hashMap.put("item_id", item);
        return CommonDao.select("Item.findReorder", hashMap);
    }
}
