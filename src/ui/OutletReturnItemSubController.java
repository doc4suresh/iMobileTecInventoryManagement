/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import entity.Invoice;
import entity.Invoiceitem;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class OutletReturnItemSubController implements Initializable {

    @FXML
    private Label lblBrand;
    @FXML
    private Label lblItem;
    @FXML
    private Label lblSprice;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtRtnDisPre;
    @FXML
    private TextField txtRtnDisVal;
    @FXML
    private TextField txtIMEINo;
    @FXML
    private TextField txtQty;


    //Color Indication of Input Controls 
    private String valid = Style.valid;
    private String invalid = Style.invalid;
    private String updated = Style.updated;
    private String initial = Style.initial;
    
    //Binding with the Form, Table
    private Invoiceitem invoiceitem;
    
    //Update Identification 
    private Invoiceitem oldInvoiceitem;
    
    ResourceBundle rb;
    Alert alert;
    private Invoice invoice;
    private OutletReturnController outletReturnUIController;
    private OutletController outletController; 
    private TableView<Invoiceitem> tblCart;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        loadForm();
    }
    
    private void loadForm(){
        this.invoiceitem = new Invoiceitem();
        oldInvoiceitem = null;
        invoice = (Invoice) rb.getObject("Invoice");
        tblCart = (TableView<Invoiceitem>) rb.getObject("tblCart");
        
        outletReturnUIController = (OutletReturnController) rb.getObject("controller");
        
        invoiceitem = tblCart.getSelectionModel().getSelectedItem();
        fillForm(invoiceitem);
        
        lblBrand.setText(invoiceitem.getItemId().getBrandId().getName());
        lblItem.setText(invoiceitem.getItemId().getName());
        lblSprice.setText(invoiceitem.getTotal().toString());
    }
    
    private void fillForm(Invoiceitem invoiceitem){
        if (invoiceitem.getImei() != null) {
            txtIMEINo.setText(invoiceitem.getImei());
            txtQty.setText(invoiceitem.getQty().toString());
            txtQty.setDisable(true);
        } else {
            txtQty.setText(invoiceitem.getQty().toString());
            txtIMEINo.setDisable(true);
        }
        
        if (invoiceitem.getDiscountvalue() != null) {
            txtRtnDisPre.setText(invoiceitem.getDiscountper().toString());
            txtRtnDisVal.setText(invoiceitem.getDiscountvalue().toString());
        }
        
    }

    @FXML
    private void btnUpdateAP(ActionEvent event) {
        invoiceitem.setQty(Integer.parseInt(txtQty.getText()));
        invoiceitem.setImei(txtIMEINo.getText());
        float disPrecent = Float.valueOf(txtRtnDisPre.getText());
        invoiceitem.setDiscountper(BigDecimal.valueOf(disPrecent));
        float discount = Float.valueOf(txtRtnDisVal.getText());
        invoiceitem.setDiscountvalue(BigDecimal.valueOf(discount));
        double linetotal = invoiceitem.getItemId().getSprice().doubleValue() * invoiceitem.getQty().doubleValue();
        invoiceitem.setTotal(BigDecimal.valueOf(linetotal));
        invoice.getInvoiceitemList().add(invoiceitem);
        
        tblCart.getItems().set(invoiceitem.getId(), invoiceitem);
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
    }

    @FXML
    private void btnCancelAP(ActionEvent event) {
    }

    @FXML
    private void txtRtnDisPreKR(KeyEvent event) {
        String thisDisPre = txtRtnDisPre.getText().trim();
        
        if (OutletController.per(thisDisPre)) {
            txtRtnDisPre.setStyle(updated);
        } else {
            txtRtnDisPre.setStyle(invalid);
        }
    }

    @FXML
    private void txtRtnDisValKR(KeyEvent event) {
        String thisDisVal = txtRtnDisVal.getText().trim();
        
        if (OutletController.disValue(thisDisVal)) {
            txtRtnDisVal.setStyle(updated);
        } else {
            txtRtnDisVal.setStyle(invalid);
        }
    }

    @FXML
    private void txtIMEINoKR(KeyEvent event) {
        String thisIMEI = txtIMEINo.getText().trim();
        
        if (OutletController.imei(thisIMEI)) {
            txtIMEINo.setStyle(updated);
        } else {
            txtIMEINo.setStyle(invalid);
        }
    }

    @FXML
    private void txtQtyKR(KeyEvent event) {
        String thisQty = txtQty.getText().trim();
        
        if (OutletController.qty(thisQty)){
            txtQty.setStyle(updated);
        }else{
            txtQty.setStyle(invalid);
        }
    }
    
}
