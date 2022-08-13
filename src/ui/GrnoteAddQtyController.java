/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import entity.Grnote;
import entity.Grnotepoitem;
import entity.Poitem;
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
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class GrnoteAddQtyController implements Initializable {

    @FXML
    private TextField txtQty;
    @FXML
    private TextField txtPPrice;
    @FXML
    private TextField txtSPrice;
    @FXML
    private Label lblItemBrand;
    @FXML
    private Label lblItemName;
    @FXML
    private Label lblQtyAvailable;
    @FXML
    private Label lblQtyRequested;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnCancel;

    // <editor-fold defaultstate="collapsed" desc=" Global-Data ">
    //Color Indication of Input Controls 
    private String valid = Style.valid;
    private String invalid = Style.invalid;
    private String updated = Style.updated;
    private String initial = Style.initial;

    //Binding with the Form, Table
    private Grnotepoitem grnotepoitem;
//    private ObservableList<Grnotepoitem> grnotes;

    //Update Identification 
    private Grnotepoitem oldGrnotepoitem;

    //Table Row, Page Selection 
    private int page;
    private int row;

    ResourceBundle rb;
    Alert alert;
    private Grnote grnote;
    private GRNoteController grnoteUIController;
    private TableView<Grnotepoitem> tblGRNItem;
// </editor-fold> 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        loadForm();
    }

    //initialize(), btnClerAP(), btnAdd(), btnUpdate(), btnDelete(), Pagination.call(), 
    private void loadForm() {
        this.grnotepoitem = new Grnotepoitem();
        oldGrnotepoitem = null;
        grnote = (Grnote) rb.getObject("Grnote");
        tblGRNItem = (TableView<Grnotepoitem>) rb.getObject("tblGrnotepoitems");

        grnoteUIController = (GRNoteController) rb.getObject("controller");

        if ((boolean) rb.getObject("addStatus")) {
            grnotepoitem.setPoitemId((Poitem) rb.getObject("poitem"));
            oldGrnotepoitem = null;
            disableButtons(false, true, false);
        } else {
            grnotepoitem = grnote.getGrnotepoitemList().get(tblGRNItem.getSelectionModel().getSelectedIndex());
            fillForm(grnotepoitem);
            disableButtons(true, false, false);
        }
        lblItemName.setText(grnotepoitem.getPoitemId().getItemId().getName());
        lblItemBrand.setText(grnotepoitem.getPoitemId().getItemId().getBrandId().getName());
        lblQtyRequested.setText(grnotepoitem.getPoitemId().getQtyrequested().toString());
        lblQtyAvailable.setText(grnotepoitem.getPoitemId().getItemId().getQty().toString());
        alert = (Alert) rb.getObject("ui");
        grnotepoitem.setGrnoteId(grnote);
    }

    //loadForm(), fillForm(Porder) 
    private void disableButtons(boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert);
        btnUpdate.setDisable(update);
        btnCancel.setDisable(delete);
    }

    private void fillForm(Grnotepoitem grnotepoitem) {
        setStyle(valid);
        txtQty.setText(grnotepoitem.getQtyrecieved().toString());
        txtPPrice.setText(grnotepoitem.getPprice().toString());
        txtSPrice.setText(grnotepoitem.getSprice().toString());
    }

    // fillForm(Grnotepoitem)
    private void setStyle(String style) {
        txtQty.setStyle(style);
        txtPPrice.setStyle(style);
        txtSPrice.setStyle(style);
    }

    @FXML
    private void txtQtyKR(KeyEvent event) {
        String qty = txtQty.getText().trim();
        if (qty(qty) && Integer.parseInt(qty) <= grnotepoitem.getPoitemId().getQtyrequested() - grnotepoitem.getPoitemId().getQtyrecieved()) {
            grnotepoitem.setQtyrecieved(Integer.parseInt(qty));
            if (oldGrnotepoitem != null && !grnotepoitem.getQtyrecieved().equals(oldGrnotepoitem.getQtyrecieved())) {
                txtQty.setStyle(updated);
            }
            txtQty.setStyle(valid);
            generateLineTotal();
        } else {
            txtQty.setStyle(invalid);
            grnotepoitem.setQtyrecieved(null);
        }
    }

    public static boolean qty(String qty) {
        return qty.matches("\\d{1,7}");
    }

    private void generateLineTotal() {
        if (grnotepoitem.getPprice() != null && grnotepoitem.getQtyrecieved() != null) {
            double lineTotal = grnotepoitem.getPprice().doubleValue() * grnotepoitem.getQtyrecieved();
            grnotepoitem.setLinetotal(BigDecimal.valueOf(lineTotal));
        }
    }

    @FXML
    private void txtPPriceKR(KeyEvent event) {
        if (price(txtPPrice.getText().trim())) {
            grnotepoitem.setPprice(new BigDecimal(txtPPrice.getText()));
            txtPPrice.setStyle(valid);

            priceComparition();
            if (oldGrnotepoitem != null && !oldGrnotepoitem.getPprice().equals(oldGrnotepoitem.getPprice())) {
                txtPPrice.setStyle(updated);
            }
        } else {
            txtPPrice.setStyle(invalid);
            grnotepoitem.setPprice(null);
        }
    }

    public static boolean price(String price) {
        return price.matches("\\d+(.\\d\\d?)?");
    }

    private void priceComparition() {
        if (price(txtSPrice.getText().trim()) && price(txtPPrice.getText().trim())) {
            grnotepoitem.setPprice(new BigDecimal(txtPPrice.getText().trim()));
            grnotepoitem.setSprice(new BigDecimal(txtSPrice.getText().trim()));
        }
        if (grnotepoitem.getSprice() != null && grnotepoitem.getPprice() != null) {

            if (grnotepoitem.getPprice().compareTo(grnotepoitem.getSprice()) > 0) {
                txtPPrice.setStyle(invalid);
                txtSPrice.setStyle(invalid);

            } else {
                txtPPrice.setStyle(valid);
                txtSPrice.setStyle(valid);
            }

        }
    }

    @FXML
    private void txtSPriceKR(KeyEvent event) {
        if (price(txtSPrice.getText().trim())) {
            grnotepoitem.setSprice(new BigDecimal(txtSPrice.getText()));
            txtSPrice.setStyle(valid);
            priceComparition();
            if (oldGrnotepoitem != null && !oldGrnotepoitem.getSprice().equals(oldGrnotepoitem.getSprice())) {
                txtSPrice.setStyle(updated);
            }
//            }
        } else {
            txtSPrice.setStyle(invalid);
            grnotepoitem.setSprice(null);
        }
    }

    @FXML
    private void btnAddAP(ActionEvent event) {
        String errors = getErrors();
        if (errors.isEmpty()) {
            double lineTotal = grnotepoitem.getPprice().doubleValue() * grnotepoitem.getQtyrecieved().doubleValue();
            grnotepoitem.setLinetotal(BigDecimal.valueOf(lineTotal));
            grnote.getGrnotepoitemList().add(grnotepoitem);

            tblGRNItem.setItems(FXCollections.observableArrayList(grnote.getGrnotepoitemList()));
            grnoteUIController.calculateTotal();
            alert.hide();

            //----------- Notification Success -------------
            TrayNotification tray = new TrayNotification();
            Image rightImg = new Image("/image/NotifyIcon/Right.png");
            tray.setTitle("SUCCESS");
            tray.setMessage("Item Added");
            tray.setRectangleFill(Paint.valueOf("#00b84c"));
            tray.showAndWait();
            tray.setImage(rightImg);
            tray.showAndDismiss(Duration.seconds(4));

        } else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Error Message");
            alert1.setHeaderText(null);
            alert1.setContentText("GRN Item Detail Error");
            alert1.showAndWait();
        }
    }

    public String getErrors() {
        String errors = "";
        if (grnotepoitem.getQtyrecieved() == null) {
            errors = errors + "Recieved Quantity \t\tis Invalid\n";
        }
        if (grnotepoitem.getPprice() != null && grnotepoitem.getSprice() != null && grnotepoitem.getPprice().compareTo(grnotepoitem.getSprice()) > 0) {
            grnotepoitem.setPprice(null);
            grnotepoitem.setSprice(null);
        }
        if (grnotepoitem.getPprice() == null) {
            errors = errors + "Purchase Price \t\tis Invalid\n";
        }
        if (grnotepoitem.getSprice() == null) {
            errors = errors + "Sell Price \t\t\t\tis Invalid\n";
        }

        return errors;
    }

    @FXML
    private void btnUpdateAP(ActionEvent event) {
        String errors = getErrors();
        if (errors.isEmpty()) {
            double lineTotal = grnotepoitem.getPprice().doubleValue() * grnotepoitem.getQtyrecieved().doubleValue();
            grnotepoitem.setLinetotal(BigDecimal.valueOf(lineTotal));
            int selectedIndex = tblGRNItem.getSelectionModel().getSelectedIndex();
            grnote.getGrnotepoitemList().set(selectedIndex, grnotepoitem);
            tblGRNItem.getItems().set(selectedIndex, grnotepoitem);
            grnoteUIController.calculateTotal();
            alert.hide();

            //----------- Notification Success -------------
            TrayNotification tray = new TrayNotification();
            Image rightImg = new Image("/image/NotifyIcon/Right.png");
            tray.setTitle("SUCCESS");
            tray.setMessage("Item Updaded");
            tray.setRectangleFill(Paint.valueOf("#00b84c"));
            tray.showAndWait();
            tray.setImage(rightImg);
            tray.showAndDismiss(Duration.seconds(4));

        } else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Error Message");
            alert1.setHeaderText(null);
            alert1.setContentText("GRN Item Detail Error");
            alert1.showAndWait();
        }
    }

    @FXML
    private void btnClose(ActionEvent event) {
        if (getErrors().isEmpty()) {

        }
        alert.hide();
    }

}
