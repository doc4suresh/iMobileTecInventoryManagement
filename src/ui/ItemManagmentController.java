
package ui;

import dao.ItemstatusDao;
import dao.ItemtypeDao;
import dao.BrandDao;
import dao.DaoException;
import dao.ItemDao;
import dao.SupplierDao;
import entity.Brand;
import entity.Item;
import entity.Itemstatus;
import entity.Itemtype;
import entity.Supplier;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.util.Duration;
import tray.notification.TrayNotification;
import static ui.LoginController.privilage;

public class ItemManagmentController implements Initializable {

    private Main main;
    private static final String btnActL = "-fx-background-color: #424343;" + "-fx-text-fill: #FFF;";
    private static final String btnActU = "-fx-background-color: #FFF;" + "-fx-text-fill: #424343;";

    //<editor-fold defaultstate="collapsed" desc="Module-Data">
    //Color Indication of Input Controls
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    //Binding with the Form
    private Item item;

    //Update Identification
    private Item oldItem;

    //Table Row, Page Selected
    private int page;
    private int row;

    //Search Lock
    private boolean lock;

//</editor-fold>  
    @FXML
    private ComboBox<Supplier> cmbSupplier;
    @FXML
    private ComboBox<Itemtype> cmbType;
    @FXML
    private ComboBox<Brand> cmbBrand;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPPrice;
    @FXML
    private TextField txtSPrice;
    @FXML
    private TextField txtQty;
    @FXML
    private TextField txtRop;
    @FXML
    private ComboBox<Itemstatus> cmbStatus;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private ComboBox<Itemtype> cmbSearchItemType;
    @FXML
    private ComboBox<Brand> cmbSearchItemBrand;
    @FXML
    private TextField txtSearchName;
    @FXML
    private ComboBox<Itemstatus> cmbSearchItemStatus;
    @FXML
    private Button btnSearchClear;
    @FXML
    private Button btnSearchLock;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Item> tblItem;
    @FXML
    private TableColumn<Item, Itemtype> colType;
    @FXML
    private TableColumn<Item, Brand> colBrand;
    @FXML
    private TableColumn<Item, String> colName;
    @FXML
    private TableColumn<Item, Itemstatus> colStatus;
    @FXML
    private Label lblNewType;
    @FXML
    private Label lblNewBrand;
    @FXML
    private Label lblSpriceInvalid;
    @FXML
    private Label lblSpriceInvalidEg;
    @FXML
    private Label lblPpriceInvalid;
    @FXML
    private Label lblPpriceInvalidEg;
    @FXML
    private Label lblNewSupplier;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadForm();
        loadTable();
    }

    private void loadForm() {

        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;

        item = new Item();
        oldItem = null;

        cmbSupplier.setItems(SupplierDao.getAll());
        cmbSupplier.getSelectionModel().select(-1);
        cmbType.setItems(ItemtypeDao.getAll());
        cmbType.getSelectionModel().select(-1);
        cmbStatus.setItems(ItemstatusDao.getAll());
        cmbStatus.getSelectionModel().select(-1);
        cmbBrand.setItems(BrandDao.getAll());
        cmbBrand.getSelectionModel().select(-1);

        txtName.setText("");
        txtPPrice.setText("");
        txtSPrice.setText("");
        txtQty.setText("");
        txtRop.setText("");

        dissableButtons(false, false, true, true);
        setStyle(initial);
    }

    private void loadTable() {
        lock = false;
        btnSearchLock.setText("Lock");

        cmbSearchItemType.setItems(ItemtypeDao.getAll());
        cmbSearchItemType.getSelectionModel().select(-1);
        cmbSearchItemBrand.setItems(BrandDao.getAll());
        cmbSearchItemBrand.getSelectionModel().select(-1);
        cmbSearchItemStatus.setItems(ItemstatusDao.getAll());
        cmbSearchItemStatus.getSelectionModel().select(-1);
        txtSearchName.setText("");

        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colStatus.setCellValueFactory(new PropertyValueFactory("itemstatusId"));
        colType.setCellValueFactory(new PropertyValueFactory("itemtypeId"));
        colBrand.setCellValueFactory(new PropertyValueFactory("brandId"));

        fillTable(ItemDao.getAll());
        pagination.setCurrentPageIndex(0);

    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Item_insert"));
        btnUpdate.setDisable(update || !privilage.get("Item_update"));
        btnDelete.setDisable(delete || !privilage.get("Item_delete"));

        txtSearchName.setDisable(select || !privilage.get("Item_select"));
        cmbSearchItemType.setDisable(select || !privilage.get("Item_select"));
        cmbSearchItemBrand.setDisable(select || !privilage.get("Item_select"));
        cmbSearchItemStatus.setDisable(select || !privilage.get("Item_select"));
        btnSearchLock.setDisable(select || !privilage.get("Item_select"));
        btnSearchClear.setDisable(select || !privilage.get("Item_select"));

        lblNewType.setDisable(select || !privilage.get("Itemtype_select"));
        lblNewBrand.setDisable(select || !privilage.get("Brand_select"));
    }

    private void setStyle(String style) {
        cmbSupplier.setStyle(style);
        cmbType.setStyle(style);
        cmbBrand.setStyle(style);
        cmbStatus.setStyle(style);

        txtName.setStyle(style);
        txtPPrice.setStyle(style);
        txtSPrice.setStyle(style);
        txtQty.setStyle(style);
        txtRop.setStyle(style);
    }

    private void fillTable(ObservableList<Item> items) {

        if (privilage.get("Item_select") && items != null && items.size() != 0) {

            int rowsCount = 9;
            int pageCount = ((items.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? items.size() : pageIndex * rowsCount + rowsCount;
                    tblItem.getItems().clear();
                    tblItem.setItems(FXCollections.observableArrayList(items.subList(start, end)));
                    return tblItem;
                }
            });
        } else {

            pagination.setPageCount(1);
            tblItem.getItems().clear();
        }
    }

    @FXML
    private void cmbSupplierAP(ActionEvent event) {
        item.setSupplierId(cmbSupplier.getSelectionModel().getSelectedItem());
        if (cmbSupplier.getSelectionModel().getSelectedItem() != null) {
            if (oldItem != null && !item.getSupplierId().equals(oldItem.getSupplierId())) {
                cmbSupplier.setStyle(updated);
            } else {
                cmbSupplier.setStyle(valid);
            }
        }
    }

    @FXML
    private void cmbTypeAP(ActionEvent event) {
        item.setItemtypeId(cmbType.getSelectionModel().getSelectedItem());
        if (oldItem != null && !item.getItemtypeId().equals(oldItem.getItemtypeId())) {
            cmbType.setStyle(updated);
        } else {
            cmbType.setStyle(valid);
        }
    }

    @FXML
    private void cmbBrandAP(ActionEvent event) {
        item.setBrandId(cmbBrand.getSelectionModel().getSelectedItem());
        if (oldItem != null && !item.getBrandId().equals(oldItem.getBrandId())) {
            cmbBrand.setStyle(updated);
        } else {
            cmbBrand.setStyle(valid);
        }
    }

    @FXML
    private void txtNameKR(KeyEvent event) {
        if (item.setName(txtName.getText().trim())) {
            if (oldItem != null && !item.getName().equals(oldItem.getName())) {
                txtName.setStyle(updated);
            } else {
                txtName.setStyle(valid);
                updateTable();
            }
        } else {
            txtName.setStyle(invalid);
        }
    }

    @FXML
    private void txtPpriceKR(KeyEvent event) {
        if (txtPPrice.getText().matches("\\d+(\\.\\d{0,2})?") && item.setPprice(BigDecimal.valueOf(Double.valueOf(txtPPrice.getText().trim())))) {
            if (oldItem != null && !item.getPprice().equals(oldItem.getPprice())) {
                txtPPrice.setStyle(updated);
                lblPpriceInvalid.setText(" ");
                lblPpriceInvalidEg.setText(" ");
            } else {
                txtPPrice.setStyle(valid);
                lblPpriceInvalid.setText(" ");
                lblPpriceInvalidEg.setText(" ");
            }
        } else {
            txtPPrice.setStyle(invalid);
            lblPpriceInvalid.setText("Only can enter Numbers");
            lblPpriceInvalidEg.setText("Eg: 1000.00");
        }
    }

    @FXML
    private void txtSpriceKR(KeyEvent event) {
        if (txtSPrice.getText().matches("\\d+(\\.\\d{0,2})?") && item.setSprice(BigDecimal.valueOf(Double.valueOf(txtSPrice.getText().trim())))) {
            if (oldItem != null && !item.getSprice().equals(oldItem.getSprice())) {
                txtSPrice.setStyle(updated);
                lblSpriceInvalid.setText(" ");
                lblSpriceInvalidEg.setText(" ");
            } else {
                txtSPrice.setStyle(valid);
                lblSpriceInvalid.setText(" ");
                lblSpriceInvalidEg.setText(" ");
            }
        } else {
            txtSPrice.setStyle(invalid);
            lblSpriceInvalid.setText("Only can enter Numbers");
            lblSpriceInvalidEg.setText("Eg: 1000.00");
        }
    }

    @FXML
    private void txtQtyKR(KeyEvent event) {
        if (item.setQty(txtQty.getText())) {
            if (oldItem != null && !item.getQty().equals(oldItem.getQty())) {
                txtQty.setStyle(updated);
            } else {
                txtQty.setStyle(valid);
            }
        } else {
            txtQty.setStyle(invalid);
        }
    }

    @FXML
    private void txtRopKR(KeyEvent event) {
        if (item.setRop(txtRop.getText())) {
            if (oldItem != null && !item.getRop().equals(oldItem.getRop())) {
                txtRop.setStyle(updated);
            } else {
                txtRop.setStyle(valid);
            }
        } else {
            txtRop.setStyle(invalid);
        }
    }

    @FXML
    private void cmbStatusAP(ActionEvent event) {
        item.setItemstatusId(cmbStatus.getSelectionModel().getSelectedItem());
        if (oldItem != null && !item.getItemstatusId().equals(oldItem.getItemstatusId())) {
            cmbStatus.setStyle(updated);
        } else {
            cmbStatus.setStyle(valid);
        }
    }

    @FXML
    private void btnAddAP(ActionEvent event) {
        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "Ara you sure you need to add this Item with following details\n "
                    + "\nSupplier :         \t\t" + item.getSupplierId().getCompany()
                    + "\nItem Type :        \t\t" + item.getItemtypeId().getName()
                    + "\nBrand :            \t\t\t" + item.getBrandId().getName()
                    + "\nItem Name :        \t\t" + item.getName()
                    + "\nPurchase Price :   \t\t" + item.getPprice()
                    + "\nSale Price :       \t\t" + item.getSprice()
                    + "\nQuantity :         \t\t" + item.getQty()
                    + "\nReorder Point :    \t\t" + item.getRop()
                    + "\nItem Status :      \t\t" + item.getItemstatusId().getName();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Add Item with following Details");
            alert.setContentText(confermation);

            ButtonType ok = new ButtonType("ADD");
            ButtonType no = new ButtonType("DON'T");

            alert.getButtonTypes().setAll(ok, no);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ok) {

                try {
                    ItemDao.add(item);

                    //----------- Notification Success -------------
                    TrayNotification tray = new TrayNotification();
                    Image rightImg = new Image("/image/NotifyIcon/Right.png");
                    tray.setTitle("SUCCESS");
                    tray.setMessage("Item " + item.getName() + " saved.");
                    tray.setRectangleFill(Paint.valueOf("#00b84c"));
                    tray.showAndWait();
                    tray.setImage(rightImg);
                    tray.showAndDismiss(Duration.seconds(4));

                    loadForm();
                    updateTable();
                    pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                    tblItem.getSelectionModel().select(tblItem.getItems().size() - 1);

                } catch (DaoException ex) {

                    //----------- Notification Unsuccess-------------
                    TrayNotification tray = new TrayNotification();
                    Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                    tray.setTitle("UN-SUCCESS");
                    tray.setMessage("Item " + item.getName() + " not saved. \n Try Again.");
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
            alert.setContentText("Item Detail Error");
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
            loadTable();
        } else if (result.get() == no) {
            // ... Canceled
        }
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
        if (getUpdates().isEmpty() && getErrors().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Item Delete");
            alert.setContentText("Delete Item " + item.getName());

            ButtonType ok = new ButtonType("DELETE");
            ButtonType no = new ButtonType("DON'T");

            alert.getButtonTypes().setAll(ok, no);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ok) {

                try {
                    ItemDao.delete(item);

                    //----------- Notification Success -------------
                    TrayNotification tray = new TrayNotification();
                    Image rightImg = new Image("/image/NotifyIcon/Right.png");
                    tray.setTitle("SUCCESS");
                    tray.setMessage("Item " + item.getName() + " deleted.");
                    tray.setRectangleFill(Paint.valueOf("#00b84c"));
                    tray.showAndWait();
                    tray.setImage(rightImg);
                    tray.showAndDismiss(Duration.seconds(4));

                    loadForm();
                    updateTable();
                    pagination.setCurrentPageIndex(page);
                    tblItem.getSelectionModel().select(row);

                } catch (Exception ex) {
                    //----------- Notification Unsuccess-------------
                    TrayNotification tray = new TrayNotification();
                    Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                    tray.setTitle("UN-SUCCESS");
                    tray.setMessage("Item " + item.getName() + " not Deleted!\n" + ex);
                    tray.setRectangleFill(Paint.valueOf("#dc0000"));
                    tray.showAndWait();
                    tray.setImage(wrongImg);
                    tray.showAndDismiss(Duration.seconds(4));
                }

            } else if (result.get() == no) {
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Item Delete");
            alert.setHeaderText(oldItem.getName() + " Delete ?");
            alert.setContentText("You can't delete\nSome of the fields are updated");
            alert.showAndWait();
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
                alert.setHeaderText("Item Update");
                alert.setContentText("Update Item " + item.getName());

                ButtonType ok = new ButtonType("UPDATE");
                ButtonType no = new ButtonType("DON'T");

                alert.getButtonTypes().setAll(ok, no);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ok) {

                    try {
                        ItemDao.update(item);

                        //----------- Notification Success -------------
                        TrayNotification tray = new TrayNotification();
                        Image rightImg = new Image("/image/NotifyIcon/Right.png");
                        tray.setTitle("SUCCESS");
                        tray.setMessage("Item " + item.getName() + " updated.");
                        tray.setRectangleFill(Paint.valueOf("#00b84c"));
                        tray.showAndWait();
                        tray.setImage(rightImg);
                        tray.showAndDismiss(Duration.seconds(4));

                        updateTable();
                        loadForm();
                        pagination.setCurrentPageIndex(page);
                        tblItem.getSelectionModel().select(row);

                    } catch (Exception ex) {
                        //----------- Notification Unsuccess-------------
                        TrayNotification tray = new TrayNotification();
                        Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                        tray.setTitle("UN-SUCCESS");
                        tray.setMessage("Item " + item.getName() + " not Updated!\n" + ex);
                        tray.setRectangleFill(Paint.valueOf("#dc0000"));
                        tray.showAndWait();
                        tray.setImage(wrongImg);
                        tray.showAndDismiss(Duration.seconds(4));
                    }

                } else if (result.get() == no) {
                    // ... Canceled
                }
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText("Item Update");
                alert.setContentText("Nothing Updated");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("Item Update");
            alert.setContentText("Item not updated!");
            alert.showAndWait();
        }
    }

    @FXML
    private void txtSearchNameKR(KeyEvent event) {
        if (!lock) {
            cmbSearchItemType.getSelectionModel().select(-1);
            cmbSearchItemStatus.getSelectionModel().select(-1);
            cmbSearchItemBrand.getSelectionModel().select(-1);
        }
        updateTable();
    }

    @FXML
    private void cmbSearchItemTypeAP(ActionEvent event) {
        if (cmbSearchItemType.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                txtSearchName.setText("");
                cmbSearchItemBrand.getSelectionModel().select(-1);
                cmbSearchItemStatus.getSelectionModel().select(-1);
            }
            updateTable();
        }
    }

    @FXML
    private void cmbSearchItemBrandAP(ActionEvent event) {
        if (cmbSearchItemBrand.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                txtSearchName.setText("");
                cmbSearchItemType.getSelectionModel().select(-1);
                cmbSearchItemStatus.getSelectionModel().select(-1);
            }
            updateTable();
        }
    }

    @FXML
    private void cmbSearchItemStatusAP(ActionEvent event) {
        if (cmbSearchItemStatus.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                txtSearchName.setText("");
                cmbSearchItemType.getSelectionModel().select(-1);
                cmbSearchItemBrand.getSelectionModel().select(-1);

            }
            updateTable();
        }
    }

    @FXML
    private void btnSearchClearAP(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Clear Form");
        alert.setContentText("Are you sure you need to clear the Table Search and the Form?");

        ButtonType ok = new ButtonType("CLEAR");
        ButtonType no = new ButtonType("DON'T");

        alert.getButtonTypes().setAll(ok, no);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ok) {
            loadTable();
        } else if (result.get() == no) {
            // ... Canceled
        }
    }

    @FXML
    private void btnSearchLockAP(ActionEvent event) {
        if (lock) {
            btnSearchLock.setText("Lock");
            lock = false;
            fillTable(ItemDao.getAll());
            btnSearchLock.setStyle(btnActU);
        } else {
            btnSearchLock.setText("Unlock");
            lock = true;
            updateTable();
            btnSearchLock.setStyle(btnActL);
        }
    }

    @FXML
    private void tblItemMC(MouseEvent event) {
        fillForm();
    }

    @FXML
    private void tblItemKR(KeyEvent event) {
        fillForm();
    }

    @FXML
    private void lblNewSupplierMC() throws IOException {
        Main.showNewSupplier();
    }

    @FXML
    private void lblNewTypeMC() throws IOException {
        Main.showItemType();
    }

    @FXML
    private void lblNewBrandMC() throws IOException {
        Main.showItemBrand();
    }

    private String getErrors() {
        String errors = "";

        if (item.getSupplierId() == null) {
            errors = errors + "Supplier \t\t\tis Not Selected\n";
        }
        if (item.getItemtypeId() == null) {
            errors = errors + "Item Type \t\tis Not Selected\n";
        }
        if (item.getBrandId() == null) {
            errors = errors + "Brand \t\t\tis Not Selected\n";
        }
        if (item.getName() == null) {
            errors = errors + "Item Name \t\tis Invalid\n";
        }
        if (item.getPprice() == null) {
            errors = errors + "Purchase Price \tis Not Entered\n";
        }
        if (item.getSprice() == null) {
            errors = errors + "Sale Price \t\tis ot Entered\n";
        }
        if (item.getQty() == null) {
            errors = errors + "Quantity \t\t\tis ot Entered\n";
        }
        if (item.getRop() == null) {
            errors = errors + "Reorder Point \t\tis ot Entered\n";
        }
        if (item.getItemstatusId() == null) {
            errors = errors + "Item Status \t\tis Not Selected\n";
        }
        return errors;
    }

    private String getUpdates() {

        String updates = "";

        if (oldItem != null) {

            if (!item.getSupplierId().equals(oldItem.getSupplierId())) {
                updates = updates + oldItem.getSupplierId() + " chnaged to " + item.getSupplierId() + "\n";
            }

            if (!item.getItemtypeId().equals(oldItem.getItemtypeId())) {
                updates = updates + oldItem.getItemtypeId() + " chnaged to " + item.getItemtypeId() + "\n";
            }

            if (!item.getBrandId().equals(oldItem.getBrandId())) {
                updates = updates + oldItem.getBrandId() + " chnaged to " + item.getBrandId() + "\n";
            }

            if (item.getName() != null && !item.getName().equals(oldItem.getName())) {
                updates = updates + oldItem.getName() + " chnaged to " + item.getName() + "\n";
            }

            if (!item.getPprice().equals(oldItem.getPprice())) {
                updates = updates + oldItem.getPprice() + " chnaged to " + item.getPprice() + "\n";
            }

            if (!item.getSprice().equals(oldItem.getSprice())) {
                updates = updates + oldItem.getSprice() + " chnaged to " + item.getSprice() + "\n";
            }

            if (!item.getQty().equals(oldItem.getQty())) {
                updates = updates + oldItem.getQty() + " chnaged to " + item.getQty() + "\n";
            }

            if (!item.getRop().equals(oldItem.getRop())) {
                updates = updates + oldItem.getRop() + " chnaged to " + item.getRop() + "\n";
            }

            if (!item.getItemstatusId().equals(oldItem.getItemstatusId())) {
                updates = updates + oldItem.getItemstatusId() + " chnaged to " + item.getItemstatusId() + "\n";
            }
        }
        return updates;
    }

    private void updateTable() {
        String name = txtSearchName.getText().trim() + txtName.getText().trim();
        boolean sname = !name.isEmpty();
        Itemstatus status = cmbSearchItemStatus.getSelectionModel().getSelectedItem();
        boolean sstatus = cmbSearchItemStatus.getSelectionModel().getSelectedIndex() != -1;
        Brand brand = cmbSearchItemBrand.getSelectionModel().getSelectedItem();
        boolean sbrand = cmbSearchItemBrand.getSelectionModel().getSelectedIndex() != -1;
        Itemtype type = cmbSearchItemType.getSelectionModel().getSelectedItem();
        boolean stype = cmbSearchItemType.getSelectionModel().getSelectedIndex() != -1;

        if (!sname && !sstatus && !sbrand && !stype) {
            fillTable(ItemDao.getAll());
        }
        if (sname && !sstatus && !sbrand && !stype) {
            fillTable(ItemDao.getAllByName(name));
        }
        if (!sname && !sstatus && sbrand && !stype) {
            fillTable(ItemDao.getAllByBrand(brand));
        }
        if (!sname && sstatus && !sbrand && !stype) {
            fillTable(ItemDao.getAllByStatus(status));
        }
        if (!sname && !sstatus && !sbrand && stype) {
            fillTable(ItemDao.getAllByType(type));
        }
        if (sname && sstatus && !sbrand && !stype) {
            fillTable(ItemDao.getAllByNameStatus(name, status));
        }
        if (sname && !sstatus && sbrand && !stype) {
            fillTable(ItemDao.getAllByNameBrand(name, brand));
        }
        if (sname && !sstatus && !sbrand && stype) {
            fillTable(ItemDao.getAllByNameType(name, type));
        }
        if (!sname && sstatus && sbrand && !stype) {
            fillTable(ItemDao.getAllByStatusBrand(status, brand));
        }
        if (!sname && sstatus && !sbrand && stype) {
            fillTable(ItemDao.getAllByStatusType(status, type));
        }
        if (!sname && !sstatus && sbrand && stype) {
            fillTable(ItemDao.getAllByTypeBrand(type, brand));
        }
        if (sname && sstatus && sbrand && !stype) {
            fillTable(ItemDao.getAllByNameStatusBrand(name, status, brand));
        }
        if (sname && sstatus && !sbrand && stype) {
            fillTable(ItemDao.getAllByNameStatusType(name, status, type));
        }
        if (sname && !sstatus && sbrand && stype) {
            fillTable(ItemDao.getAllByNameBrandType(name, brand, type));
        }
        if (!sname && sstatus && sbrand && stype) {
            fillTable(ItemDao.getAllByStatusBrandType(status, brand, type));
        }
        if (sname && sstatus && sbrand && stype) {
            fillTable(ItemDao.getAllByNameStatusBrandType(name, status, brand, type));
        }
    }

    private void fillForm() {
        if (tblItem.getSelectionModel().getSelectedItem() != null) {
            dissableButtons(false, true, false, false);
            setStyle(valid);

            oldItem = ItemDao.getById(tblItem.getSelectionModel().getSelectedItem().getId());
            item = ItemDao.getById(tblItem.getSelectionModel().getSelectedItem().getId());

            cmbSupplier.getSelectionModel().select((Supplier) item.getSupplierId());
            cmbType.getSelectionModel().select((Itemtype) item.getItemtypeId());
            cmbBrand.getSelectionModel().select((Brand) item.getBrandId());
            cmbStatus.getSelectionModel().select((Itemstatus) item.getItemstatusId());

            txtName.setText(item.getName());
            txtPPrice.setText(item.getPprice().toString());
            txtSPrice.setText(item.getSprice().toString());
            txtQty.setText(item.getQty().toString());
            txtRop.setText(item.getRop().toString());

            page = pagination.getCurrentPageIndex();
            row = tblItem.getSelectionModel().getSelectedIndex();
        }
    }

    @FXML
    private void btnHomeAP() throws IOException {
        System.gc();
        Main.showHome();
    }

}
