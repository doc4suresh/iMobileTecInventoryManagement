/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import Common.MyResourceBundle;
import dao.DaoException;
import dao.ItemDao;
import dao.ItemtypeDao;
import dao.POStatusDao;
import dao.POrderDao;
import dao.SupplierDao;
import entity.Item;
import entity.Itemtype;
import entity.Poitem;
import entity.Porder;
import entity.Supplier;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.util.Duration;
import tray.notification.TrayNotification;
import static ui.LoginController.privilage;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class PurchaseOrderUIController implements Initializable {

    @FXML
    private ComboBox<Supplier> cmbSupplier;
    @FXML
    private ComboBox<Itemtype> cmbItemType;
    @FXML
    private TableView<Item> tblItem;
    @FXML
    private TableColumn<Item, String> colBrand;
    @FXML
    private TableColumn<Item, String> colItemName;
    @FXML
    private TableColumn<Item, BigDecimal> colPrice;
    @FXML
    private TableColumn<Item, String> colQtyInHand;
    @FXML
    private Label lblTelephone;
    @FXML
    private Label lblEmail;
    @FXML
    private Button btnAddItem;
    @FXML
    private Button btnItemUpdate;
    @FXML
    private Button btnItemDelete;
    @FXML
    private TextField txtQuantity;
    @FXML
    private Label lblNo;
    @FXML
    private Label lblDate;
    @FXML
    private TableView<Poitem> tblPOItem;
    @FXML
    private TableColumn<Poitem, String> colPOBrand;
    @FXML
    private TableColumn<Poitem, String> colPOItemName;
    @FXML
    private TableColumn<Poitem, Integer> colQuantity;
    @FXML
    private TableColumn<Poitem, BigDecimal> colLineTotal;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnAdd;
    @FXML
    private Label lblTotal;

// <editor-fold defaultstate="collapsed" desc=" Global-Data ">
    //Color Indication of Input Controls 
    private String valid = Style.valid;
    private String invalid = Style.invalid;
    private String updated = Style.updated;
    private String initial = Style.initial;

    //Binding with the Form, Table
    private Porder porder;
    private ObservableList<Porder> porders;

    //Update Identification 
    private Porder oldPorder;

    //Table Row, Page Selection 
    private int page;
    private int row;

// </editor-fold> 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadForm();
    }

    private void loadForm() {

        this.porder = new Porder();
        oldPorder = null;
        List<Poitem> poitems = new ArrayList();
        porder.setPoitemList(poitems);

        cmbSupplier.setItems(SupplierDao.getAll());
        cmbSupplier.getSelectionModel().select(-1);
        cmbSupplier.setDisable(false);
        cmbItemType.setItems(ItemtypeDao.getAll());
        cmbItemType.getSelectionModel().select(-1);
        tblItem.setDisable(true);
        tblItem.getItems().clear();
        lblNo.setText(POrderDao.getNextNo());
        porder.setNo(lblNo.getText());
        txtQuantity.setText("");
        txtQuantity.setDisable(true);
        lblTotal.setText("0.00");
        lblDate.setText(LocalDate.now().toString());
        lblTelephone.setText("--");
        lblEmail.setText("--");
        porder.setDatetime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        porder.setUserEmployeeId(LoginController.user);
        porder.setPostatusId(POStatusDao.getById(1));
        disableButtons(false, false, true, true);
        setStyle(initial);
        loadPOItemFormTable();
        loadItemFormTable();
        tblPOItem.getItems().clear();
        btnAddItem.setDisable(true);
        btnItemDelete.setDisable(true);
        btnItemUpdate.setDisable(true);

        cmbItemType.setDisable(true);
    }

    //loadForm(), fillForm(Porder) 
    private void disableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Porder_insert"));
        btnUpdate.setDisable(update || !privilage.get("Porder_update"));
        btnSearch.setDisable(select || !privilage.get("Porder_select"));
    }

    //loadForm(), fillForm(Porder)
    private void setStyle(String style) {
        cmbSupplier.setStyle(style);
        txtQuantity.setStyle(style);
    }

    private void loadItemFormTable() {
        colBrand.setCellValueFactory(new PropertyValueFactory("brandId"));
        colItemName.setCellValueFactory(new PropertyValueFactory("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory("pprice"));
        colQtyInHand.setCellValueFactory(new PropertyValueFactory("qty"));
    }

    private void loadPOItemFormTable() {
        colPOBrand.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Poitem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Poitem, String> i) {
                return new SimpleStringProperty(i.getValue().getItemId().getBrandId().getName());
            }
        });
        colPOItemName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Poitem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Poitem, String> i) {
                return new SimpleStringProperty(i.getValue().getItemId().getName());
            }
        });
        colQuantity.setCellValueFactory(new PropertyValueFactory("qtyrequested"));
        colLineTotal.setCellValueFactory(new PropertyValueFactory("linetotal"));
    }

    @FXML
    private void cmbSupplierAP(ActionEvent event) {
        if (cmbSupplier.getSelectionModel().getSelectedItem() != null) {
            porder.setSupplierId(cmbSupplier.getSelectionModel().getSelectedItem());
            if (oldPorder != null && !porder.getSupplierId().equals(oldPorder.getSupplierId())) {
                cmbSupplier.setStyle(updated);
            } else {
                cmbSupplier.setStyle(valid);
            }
            lblTelephone.setText(cmbSupplier.getSelectionModel().getSelectedItem().getMobilephone());
            lblEmail.setText(cmbSupplier.getSelectionModel().getSelectedItem().getEmail());
            searchItems();
            cmbItemType.setDisable(false);
        } else {
            lblTelephone.setText("--");
            lblEmail.setText("--");
        }
    }

    private void searchItems() {
        Supplier supplier = cmbSupplier.getSelectionModel().getSelectedItem();
        boolean ssupplier = cmbSupplier.getSelectionModel().getSelectedIndex() != -1;
        Itemtype type = cmbItemType.getSelectionModel().getSelectedItem();
        boolean stype = cmbItemType.getSelectionModel().getSelectedIndex() != -1;

        if (ssupplier && !stype) {
            tblItem.setItems(ItemDao.getAllBySupplier(cmbSupplier.getSelectionModel().getSelectedItem()));
        }
        if (ssupplier && stype) {
            tblItem.setItems(ItemDao.getAllBySupplierType(supplier, type));
        }
        tblItem.setDisable(false);

    }

    @FXML
    private void cmbItemTypeAP(ActionEvent event) {
        if (cmbItemType.getSelectionModel().getSelectedItem() != null) {
            searchItems();
        }
    }

    @FXML
    private void tblItemMC(MouseEvent event) {
        if (tblItem.getSelectionModel().getSelectedItem() != null) {
            tblPOItem.getSelectionModel().clearSelection();
            if (porder.getPostatusId().equals(POStatusDao.getById(1))) {
                txtQuantity.setDisable(false);
                txtQuantity.setText("");
            }
            btnItemUpdate.setDisable(true);
            btnItemDelete.setDisable(true);
        }
    }

    @FXML
    private void btnAddItemAP(ActionEvent event) {
        boolean isexisting = false;
        for (Poitem poitem : porder.getPoitemList()) {
            if (poitem.getItemId().getId() == tblItem.getSelectionModel().getSelectedItem().getId()) {
                isexisting = true;
            }
        }
        if (isexisting) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("Item Add");
            alert.setContentText(tblItem.getSelectionModel().getSelectedItem().getName() + " is Alredy existed \nPlease Update the Quantity");
            alert.showAndWait();

            txtQuantity.setText("");
            txtQuantity.setStyle(initial);
        } else {
            Poitem poitem = new Poitem();
            poitem.setItemId(tblItem.getSelectionModel().getSelectedItem());
            poitem.setQtyrequested(Integer.parseInt(txtQuantity.getText()));
            poitem.setQtyrecieved(0);
            poitem.setPPrice(tblItem.getSelectionModel().getSelectedItem().getPprice());
            double lineTotal = poitem.getItemId().getPprice().doubleValue() * poitem.getQtyrequested().doubleValue();
            poitem.setLinetotal(BigDecimal.valueOf(lineTotal));
            poitem.setPorderId(porder);
            porder.getPoitemList().add(poitem);

            tblPOItem.setItems(FXCollections.observableArrayList(porder.getPoitemList()));
            calculateTotal();
            txtQuantity.setText("");
            cmbSupplier.setDisable(true);
            txtQuantity.setStyle(initial);
            tblItem.requestFocus();
            txtQuantity.setDisable(true);
            btnAddItem.setDisable(true);
        }
    }

    @FXML
    private void btnItemUpdateAP(ActionEvent event) {
        if (tblPOItem.getSelectionModel().getSelectedItem() != null) {
            Poitem poitem = porder.getPoitemList().get(tblPOItem.getSelectionModel().getSelectedIndex());
            poitem.setQtyrequested(Integer.parseInt(txtQuantity.getText()));
            double lineTotal = poitem.getItemId().getPprice().doubleValue() * poitem.getQtyrequested().doubleValue();
            poitem.setLinetotal(BigDecimal.valueOf(lineTotal));
            colQuantity.setVisible(false);
            colQuantity.setVisible(true);
            calculateTotal();
            tblItem.setItems(ItemDao.getAllBySupplier(porder.getSupplierId()));
            txtQuantity.setText("");
            cmbSupplier.setDisable(true);
            btnItemDelete.setDisable(true);
        }
    }

    @FXML
    private void btnItemDeleteAP(ActionEvent event) {
        if (tblPOItem.getSelectionModel().getSelectedItem() != null) {
            porder.getPoitemList().remove(tblPOItem.getSelectionModel().getSelectedIndex());
            tblPOItem.setItems(FXCollections.observableArrayList(porder.getPoitemList()));
            calculateTotal();
            btnItemDelete.setDisable(true);
            tblItem.setItems(ItemDao.getAllBySupplier(porder.getSupplierId()));
            txtQuantity.setText("");
            txtQuantity.setStyle(initial);
        }
    }

    public void calculateTotal() {
        double total = 0.00;
        for (Poitem poitem : porder.getPoitemList()) {
            total = total + poitem.getLinetotal().doubleValue();
        }
        lblTotal.setText(String.valueOf(total));
        porder.setTotal(BigDecimal.valueOf(total));
    }

    @FXML
    private void txtQuantityKR(KeyEvent event) {
        String recieved = txtQuantity.getText().trim();
        if (qty(recieved)) {
            txtQuantity.setStyle(valid);
            if (tblItem.getSelectionModel().getSelectedItem() != null) {
                btnAddItem.setDisable(false);
            }
            if (tblPOItem.getSelectionModel().getSelectedItem() != null) {
                btnAddItem.setDisable(true);
            }
        } else {
            txtQuantity.setStyle(invalid);
            btnAddItem.setDisable(true);
            btnItemUpdate.setDisable(true);
        }
    }

//Quantity validation
    private static boolean qty(String qty) {
        return qty.matches("\\d{0,3}");
    }

    @FXML
    private void tblPOItemMC(MouseEvent event) {
        if (tblPOItem.getSelectionModel().getSelectedItem() != null) {

            if (porder.getPostatusId().equals(POStatusDao.getById(1))) {
                txtQuantity.setDisable(false);
                btnItemDelete.setDisable(false);
                btnItemUpdate.setDisable(false);
                btnAddItem.setDisable(true);
            }
            tblItem.getSelectionModel().clearSelection();
            txtQuantity.setText(tblPOItem.getSelectionModel().getSelectedItem().getQtyrequested().toString());
        }
    }

    @FXML
    private void btnSearchAP(ActionEvent event) {
        try {
            Alert subUI = new Alert(null, null);
            MyResourceBundle myResources = new MyResourceBundle();
            HashMap hm = new HashMap();

            Porder porder = new Porder();

            hm.put("it", porder);
            hm.put("ui", subUI);
            myResources.setHashMap(hm);

            AnchorPane itemSearchUI = FXMLLoader.load(Main.class.getResource("POrderApprove.fxml"), myResources);
            subUI.getDialogPane().setContent(itemSearchUI);
            subUI.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            subUI.show();

            if (porder.getId() != null) {
                porder = POrderDao.getById(porder.getId());
                fillForm(porder);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnUpdateAP(ActionEvent event) {
        String errors = getErrors();
        if (errors.isEmpty()) {
            String updates = getUpdates();
            if (!updates.isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Purchase Order Update");
                alert.setContentText("Update Purchase Order " + porder.getNo());

                ButtonType ok = new ButtonType("UPDATE");
                ButtonType no = new ButtonType("DON'T");

                alert.getButtonTypes().setAll(ok, no);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ok) {

                    POrderDao.update(porder);

                    //----------- Notification Success -------------
                    TrayNotification tray = new TrayNotification();
                    Image rightImg = new Image("/image/NotifyIcon/Right.png");
                    tray.setTitle("SUCCESS");
                    tray.setMessage("Porder " + porder.getNo() + " updated.");
                    tray.setRectangleFill(Paint.valueOf("#00b84c"));
                    tray.showAndWait();
                    tray.setImage(rightImg);
                    tray.showAndDismiss(Duration.seconds(4));

                } else if (result.get() == no) {
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText("Purchase Order Update");
                alert.setContentText("Nothing Updated");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("Purchase Order Update");
            alert.setContentText("Purchase Order  not updated!");
            alert.showAndWait();
        }
    }

    @FXML
    private void btnClearAP(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Clear the Form Fields");
        alert.setContentText("Do you want to clear the form?");

        ButtonType ok = new ButtonType("CLEAR");
        ButtonType no = new ButtonType("DON'T");

        alert.getButtonTypes().setAll(ok, no);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ok) {
            loadForm();
        } else if (result.get() == no) {
        }
    }

    @FXML
    private void btnAddAP(ActionEvent event) {
        porder.setPoitemList(tblPOItem.getItems());
        String errors = getErrors();
        if (errors.isEmpty()) {
            String poitems = "";
            for (Poitem poitem : tblPOItem.getItems()) {
                poitems = poitems + "Item :  " + poitem.getItemId().getName() + "       Quantity :  " + poitem.getQtyrequested() + "\n";
            }
            String confermation = "Ara you sure you need to add this Porder with following details\n"
                    + "\nDate     :     \t\t" + porder.getDatetime()
                    + "\nSupplier :     \t\t" + porder.getSupplierId().getCompany()
                    + "\n\n---------------------------------\n"
                    + "Items : \n\n" + poitems
                    + "\n-----------------------------------"
                    + "\nStatus :       \t\t" + porder.getPostatusId().getName()
                    + "\nTotal :        \t\t" + porder.getTotal().toString();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Add POrder with following Details");
            alert.setContentText(confermation);

            ButtonType ok = new ButtonType("ADD");
            ButtonType no = new ButtonType("DON'T");

            alert.getButtonTypes().setAll(ok, no);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ok) {

                try {
                    POrderDao.add(porder);

                    //----------- Notification Success -------------
                    TrayNotification tray = new TrayNotification();
                    Image rightImg = new Image("/image/NotifyIcon/Right.png");
                    tray.setTitle("SUCCESS");
                    tray.setMessage("Porder " + porder.getNo() + " saved.");
                    tray.setRectangleFill(Paint.valueOf("#00b84c"));
                    tray.showAndWait();
                    tray.setImage(rightImg);
                    tray.showAndDismiss(Duration.seconds(4));

                    loadForm();
                    tblItem.getItems().clear();
                } catch (DaoException ex) {
                    //----------- Notification Unsuccess-------------
                    TrayNotification tray = new TrayNotification();
                    Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                    tray.setTitle("UN-SUCCESS");
                    tray.setMessage("Porder " + porder.getNo() + " not saved. \n Try Again.");
                    tray.setRectangleFill(Paint.valueOf("#dc0000"));
                    tray.showAndWait();
                    tray.setImage(wrongImg);
                    tray.showAndDismiss(Duration.seconds(4));

                }

            } else if (result.get() == no) {
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Purchase Order Detail Error");
            alert.showAndWait();
        }
    }

    private void fillForm(Porder porder) {
        setStyle(valid);
        txtQuantity.setStyle(initial);
        oldPorder = POrderDao.getById(porder.getId());
        this.porder = POrderDao.getById(porder.getId());
        if (this.porder.getPostatusId().equals(POStatusDao.getById(1))) {
            disableButtons(false, true, false, false);
        } else {
            disableButtons(false, true, true, true);
        }
        cmbSupplier.getSelectionModel().select((Supplier) this.porder.getSupplierId());
        lblTotal.setText(this.porder.getTotal().toString());
        tblPOItem.setItems(FXCollections.observableArrayList(this.porder.getPoitemList()));
        lblDate.setText(this.porder.getDatetime().toString());
        lblNo.setText(this.porder.getNo());
    }

    public String getErrors() {

        String errors = "";

        if (porder.getUserEmployeeId() == null) {
            errors = errors + "User     \t\tis Not Selected\n";
        }
        if (porder.getSupplierId() == null) {
            errors = errors + "Supplier \t\tis Not Selected\n";
        }
        if (porder.getPoitemList().size() == 0) {
            errors = errors + "Items.   \t\tare not Selected\n";
        }
        if (porder.getPostatusId() == null) {
            errors = errors + "Status   \t\tis Not Selected\n";
        }

        return errors;

    }

    public String getUpdates() {

        String updates = "";

        if (oldPorder != null && getErrors().isEmpty()) {

            if (!porder.getUserEmployeeId().equals(oldPorder.getUserEmployeeId())) {
                updates = updates + oldPorder.getUserEmployeeId() + " chnaged to " + porder.getUserEmployeeId() + "\n";
            }

            if (!porder.getDatetime().equals(oldPorder.getDatetime())) {
                updates = updates + oldPorder.getDatetime() + " chnaged to " + porder.getDatetime() + "\n";
            }

            if (!porder.getSupplierId().equals(oldPorder.getSupplierId())) {
                updates = updates + oldPorder.getSupplierId() + " chnaged to " + porder.getSupplierId() + "\n";
            }

            if (!porder.getPostatusId().equals(oldPorder.getPostatusId())) {
                updates = updates + oldPorder.getPostatusId() + " chnaged to " + porder.getPostatusId() + "\n";
            }

            if (!porder.getPoitemList().containsAll(oldPorder.getPoitemList())) {
                updates = updates + oldPorder.getPoitemList().toString() + " chnaged to " + porder.getPoitemList().toString() + "\n";
            } else if (!oldPorder.getPoitemList().containsAll(porder.getPoitemList())) {
                updates = updates + oldPorder.getPoitemList().toString() + " chnaged to " + porder.getPoitemList().toString() + "\n";
            } else {

                for (int i = 0; i < porder.getPoitemList().size(); i++) {
                    if (porder.getPoitemList().get(i).getQtyrequested() != oldPorder.getPoitemList().get(i).getQtyrequested()) {
                        updates = updates + oldPorder.getPoitemList().get(i).getItemId().getName() + " chnaged from " + oldPorder.getPoitemList().get(i).getQtyrequested() + " to " + porder.getPoitemList().get(i).getQtyrequested() + "\n";
                    }
                }
            }
        }
        return updates;
    }

    @FXML
    private void btnHomeAP() throws IOException {
        System.gc();
        Main.showHome();
    }

}
