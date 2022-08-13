package ui;

import Common.MyResourceBundle;
import dao.DaoException;
import dao.GrnoteDao;
import dao.GrnoteStatusDao;
import dao.POrderDao;
import dao.SupplierDao;
import entity.Grnote;
import entity.Grnotepoitem;
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
import javafx.scene.control.Alert.AlertType;
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

public class GRNoteController implements Initializable {

    @FXML
    private Label lblNo;
    @FXML
    private Label lblDate;
    @FXML
    private TableView<Porder> tblPorder;
    @FXML
    private TableColumn<?, ?> colNo;
    @FXML
    private TableColumn<?, ?> colDate;
    @FXML
    private TableView<Poitem> tblpoitems;
    @FXML
    private TableColumn<Poitem, String> colBrand;
    @FXML
    private TableColumn<Poitem, String> colPoItemName;
    @FXML
    private TableColumn<Poitem, String> colPORequeseted;
    @FXML
    private TableColumn<Poitem, String> colPoRecieved;
    @FXML
    private ComboBox<Supplier> cmbSupplier;
    @FXML
    private Button btnItemAdd;
    @FXML
    private Button btnItemUpdate;
    @FXML
    private Button btnItemDelete;
    @FXML
    private TableView<Grnotepoitem> tblGrnotepoitems;
    @FXML
    private TableColumn<Grnotepoitem, String> colGrnoteItemName;
    @FXML
    private TableColumn<Grnotepoitem, String> colRecived;
    @FXML
    private TableColumn<Grnotepoitem, String> colPPrice;
    @FXML
    private TableColumn<Grnotepoitem, String> colSPrice;
    @FXML
    private TableColumn<Grnotepoitem, String> colLineTotal;
    @FXML
    private TextField txtTotalDiscountPercentage;
    @FXML
    private TextField txtTotalDiscountValue;
    @FXML
    private Label lblGrossAmount;
    @FXML
    private Label lblNetAmount;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnSearch;

    // <editor-fold defaultstate="collapsed" desc=" Global-Data ">
    //Color Indication of Input Controls 
    private String valid = Style.valid;
    private String invalid = Style.invalid;
    private String updated = Style.updated;
    private String initial = Style.initial;
    //Binding with the Form, Table
    private Grnote grnote;
    private ObservableList<Grnote> grnotes;

    //Update Identification 
    private Grnote oldGrnote;

// </editor-fold>
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadForm();
        loadTable();
    }

    //initialize(), btnClerAP(), btnAdd(), btnUpdate(), btnDelete(), Pagination.call(), 
    private void loadForm() {
        this.grnote = new Grnote();
        oldGrnote = null;
        List<Grnotepoitem> grnotepoitems = new ArrayList();
        grnote.setGrnotepoitemList(grnotepoitems);

        cmbSupplier.setItems(SupplierDao.getAll());
        cmbSupplier.getSelectionModel().select(-1);
        grnote.setDatetime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        grnote.setUserEmployeeId(LoginController.user);
        grnote.setGrnotestatusId(GrnoteStatusDao.getById(1));
        lblDate.setText(LocalDate.now().toString());
        lblNo.setText(GrnoteDao.getNextNo());
        grnote.setNo(lblNo.getText());
        lblGrossAmount.setText("00.00");
        lblNetAmount.setText("00.00");
        txtTotalDiscountPercentage.setText("");
        txtTotalDiscountValue.setText("");
        disableButtons(false, false, true, true);
        setStyle(initial);
        loadPoItemFormTable();
        loadGrnoteItemFormTable();
        tblPorder.getItems().clear();
        tblpoitems.getItems().clear();
        tblGrnotepoitems.getItems().clear();
        setStyle(initial);
        btnItemAdd.setDisable(true);
        btnItemUpdate.setDisable(true);
        btnItemDelete.setDisable(true);
    }

    //initial()  
    private void loadTable() {
        colNo.setCellValueFactory(new PropertyValueFactory("no"));
        colDate.setCellValueFactory(new PropertyValueFactory("datetime"));
    }

    //loadForm(), fillForm(Grnote) 
    private void disableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Grnote_insert"));
        btnUpdate.setDisable(update || !privilage.get("Grnote_update"));
        btnSearch.setDisable(select || !privilage.get("Grnote_select"));
    }

    //loadForm(), fillForm(Grnote)
    private void setStyle(String style) {
        cmbSupplier.setStyle(style);
        txtTotalDiscountPercentage.setStyle(style);
        txtTotalDiscountValue.setStyle(style);
    }

    //initial
    private void loadPoItemFormTable() {
        colPoItemName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Poitem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Poitem, String> p) {
                return new SimpleStringProperty(p.getValue().getItemId().getName());
            }
        });
        colBrand.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Poitem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Poitem, String> p) {
                return new SimpleStringProperty(p.getValue().getItemId().getBrandId().getName());
            }
        });
        colPORequeseted.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Poitem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Poitem, String> p) {
                return new SimpleStringProperty(p.getValue().getQtyrequested().toString());
            }
        });
        colPoRecieved.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Poitem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Poitem, String> p) {
                return new SimpleStringProperty(p.getValue().getQtyrecieved().toString());
            }
        });
    }

    //initial()
    private void loadGrnoteItemFormTable() {
        colGrnoteItemName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Grnotepoitem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Grnotepoitem, String> g) {
                return new SimpleStringProperty(g.getValue().getPoitemId().getItemId().getName());
            }
        });
        colRecived.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Grnotepoitem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Grnotepoitem, String> g) {
                return new SimpleStringProperty(g.getValue().getQtyrecieved().toString());
            }
        });
        colPPrice.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Grnotepoitem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Grnotepoitem, String> g) {
                return new SimpleStringProperty(g.getValue().getPprice().toString());
            }
        });
        colLineTotal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Grnotepoitem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Grnotepoitem, String> g) {
                return new SimpleStringProperty(g.getValue().getLinetotal().toString());
            }
        });
        colSPrice.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Grnotepoitem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Grnotepoitem, String> g) {
                return new SimpleStringProperty(g.getValue().getSprice().toString());
            }
        });

    }

    @FXML
    private void cmbSupplierAP(ActionEvent event) {
        if (cmbSupplier.getSelectionModel().getSelectedItem() != null) {
            tblPorder.setDisable(false);
            tblPorder.getItems().clear();
            tblPorder.setItems(POrderDao.getBySupplierApproved(cmbSupplier.getSelectionModel().getSelectedItem()));
        }
    }

    @FXML
    private void tblPorderMC(MouseEvent event) {
        if (tblPorder.getSelectionModel().getSelectedItem() != null) {
            grnote.setPorderId(tblPorder.getSelectionModel().getSelectedItem());
            tblpoitems.getItems().clear();
            tblpoitems.setItems(FXCollections.observableArrayList(tblPorder.getSelectionModel().getSelectedItem().getPoitemList()));
            tblpoitems.getSelectionModel().clearSelection();
            tblGrnotepoitems.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void tblPorderKR(KeyEvent event) {
        tblPorderMC(null);
    }

    @FXML
    private void tblpoitemsMC(MouseEvent event) {
        if (tblpoitems.getSelectionModel().getSelectedItem() != null) {
            btnItemAdd.setDisable(false);
            btnItemUpdate.setDisable(true);
            btnItemDelete.setDisable(true);
            tblGrnotepoitems.getSelectionModel().clearSelection();
            if (tblpoitems.getSelectionModel().getSelectedItem().getQtyrequested() == tblpoitems.getSelectionModel().getSelectedItem().getQtyrecieved()) {
                btnItemAdd.setDisable(true);
            }
        }
    }

    @FXML
    private void btnItemAddAP(ActionEvent event) {
        if (tblpoitems.getSelectionModel().getSelectedItem() != null) {
            boolean isExists = false;
            for (Grnotepoitem grnotepoitem : tblGrnotepoitems.getItems()) {
                if (grnotepoitem.getPoitemId().getItemId().getId().
                        equals(tblpoitems.getSelectionModel().getSelectedItem().getItemId().getId())) {
                    isExists = true;
                }
            }
            if (!isExists) {
                Poitem poitem = tblpoitems.getSelectionModel().getSelectedItem();
                try {
                    Alert subUI = new Alert(null, null);
//                    subUI.setResizable(false);

                    MyResourceBundle myResources = new MyResourceBundle();
                    HashMap hm = new HashMap();
                    hm.put("controller", this);
                    hm.put("poitem", poitem);
                    hm.put("ui", subUI);
                    hm.put("addStatus", true);
                    hm.put("tblGrnotepoitems", tblGrnotepoitems);
                    hm.put("Grnote", grnote);
                    myResources.setHashMap(hm);

                    AnchorPane itemSearchUI = FXMLLoader.load(Main.class.getResource("GrnoteAddQty.fxml"), myResources);
                    subUI.getDialogPane().setContent(itemSearchUI);
                    subUI.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                    subUI.show();

                    calculateTotal();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Message");
                alert.setHeaderText("Item is already exists!");
                alert.setContentText("");
                alert.showAndWait();

            }
        }
    }

    void calculateTotal() {
        double total = 0.0;
        for (Grnotepoitem grnotepoitem : grnote.getGrnotepoitemList()) {
            if (grnotepoitem.getQtyrecieved() != null && grnotepoitem.getQtyrecieved() != 0 && grnotepoitem.getPprice() != null) {
                total = total + (grnotepoitem.getQtyrecieved()) * grnotepoitem.getPprice().doubleValue();
            }
        }
        lblGrossAmount.setText(String.valueOf(total));
        grnote.setGrossamount(BigDecimal.valueOf(total));
        calculateNetAmount();
    }

    private void calculateNetAmount() {
        String totalDiscountValue = txtTotalDiscountValue.getText();
        if (!totalDiscountValue.isEmpty()) {
            BigDecimal netAmount = new BigDecimal(lblGrossAmount.getText()).subtract(new BigDecimal(totalDiscountValue));
            lblNetAmount.setText(netAmount.toString());
            boolean setNetamount = grnote.setNetamount(netAmount);
            boolean setdiscount = grnote.setTotaldiscountper(new BigDecimal(txtTotalDiscountPercentage.getText()));
            boolean setdiscountper = grnote.setTotaldiscountvalue(new BigDecimal(txtTotalDiscountValue.getText()));
        } else {
            lblNetAmount.setText("00.00");
        }
    }

    private boolean isBigDecimal(String value) {
        boolean isBigDecimal = false;
        try {
            new BigDecimal(value);
            isBigDecimal = true;
        } catch (Exception e) {
        }
        return isBigDecimal;
    }

    @FXML
    private void btnItemUpdateAP(ActionEvent event) {
        if (tblGrnotepoitems.getSelectionModel().getSelectedItem() != null) {
            Grnotepoitem grnotepoitem = grnote.getGrnotepoitemList().get(tblGrnotepoitems.getSelectionModel().getSelectedIndex());
            try {
                Alert subUI = new Alert(null, null);
                MyResourceBundle myResources = new MyResourceBundle();
                HashMap hm = new HashMap();
                hm.put("controller", this);
                hm.put("gr", grnotepoitem);
                hm.put("ui", subUI);
                hm.put("addStatus", false);
                hm.put("tblGrnotepoitems", tblGrnotepoitems);
                hm.put("Grnote", grnote);
                myResources.setHashMap(hm);
                AnchorPane itemSearchUI = FXMLLoader.load(Main.class.getResource("GrnoteAddQty.fxml"), myResources);
                subUI.getDialogPane().setContent(itemSearchUI);
                subUI.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                subUI.show();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void btnItemDeleteAP(ActionEvent event) {
        if (tblGrnotepoitems.getSelectionModel().getSelectedItem() != null) {
            grnote.getGrnotepoitemList().remove(tblGrnotepoitems.getSelectionModel().getSelectedIndex());
            tblGrnotepoitems.setItems(FXCollections.observableArrayList(grnote.getGrnotepoitemList()));
            calculateTotal();
            btnItemDelete.setDisable(true);
        }
    }

    @FXML
    private void tblGrnotepoitemsMC(MouseEvent event) {
        if (tblGrnotepoitems.getSelectionModel().getSelectedItem() != null) {
            btnItemAdd.setDisable(true);
            btnItemDelete.setDisable(false);
            btnItemUpdate.setDisable(false);
            tblpoitems.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void txtTotalDiscountPercentageKR(KeyEvent event) {
        validateTotalDiscountPercentage();
        if (isBigDecimal(txtTotalDiscountPercentage.getText())) {
            float grossAmount = Float.valueOf(lblGrossAmount.getText());
            float totalDiscountPercentage = Float.valueOf(txtTotalDiscountPercentage.getText());
            float value = grossAmount / 100 * totalDiscountPercentage;
            String totalDiscountValue = String.format("%.2f", value);
            txtTotalDiscountValue.setText(totalDiscountValue);
        } else {
            txtTotalDiscountValue.setText("");
        }
        validateTotalDiscountValue();
        calculateNetAmount();
    }

    private void validateTotalDiscountValue() {
        if (isBigDecimal(txtTotalDiscountValue.getText()) && grnote.setTotaldiscountvalue(new BigDecimal(txtTotalDiscountValue.getText()))) {
            if (oldGrnote != null && !grnote.getTotaldiscountper().equals(oldGrnote.getTotaldiscountper())) {
                txtTotalDiscountValue.setStyle(updated);
            } else {
                txtTotalDiscountValue.setStyle(valid);
            }
        } else {
            txtTotalDiscountValue.setStyle(invalid);
            grnote.setTotaldiscountvalue(null);
        }

    }

    private void validateTotalDiscountPercentage() {
        if (isBigDecimal(txtTotalDiscountPercentage.getText()) && grnote.setTotaldiscountper(new BigDecimal(txtTotalDiscountPercentage.getText()))) {
            if (oldGrnote != null && !grnote.getTotaldiscountper().equals(oldGrnote.getTotaldiscountper())) {
                txtTotalDiscountPercentage.setStyle(updated);
            } else {
                txtTotalDiscountPercentage.setStyle(valid);
            }
        } else {
            txtTotalDiscountPercentage.setStyle(invalid);
            grnote.setTotaldiscountper(null);
        }
    }

    @FXML
    private void txtTotalDiscountValueKR(KeyEvent event) {
        validateTotalDiscountValue();
        if (isBigDecimal(txtTotalDiscountValue.getText())) {
            float grossAmount = Float.valueOf(lblGrossAmount.getText());
            float totalDiscountValue = Float.valueOf(txtTotalDiscountValue.getText());
            float value = totalDiscountValue / grossAmount * 100;
            String totalDiscountPercentage = String.format("%.2f", value);
            txtTotalDiscountPercentage.setText(totalDiscountPercentage);
        } else {
            txtTotalDiscountPercentage.setText("");
        }
        validateTotalDiscountPercentage();
        calculateNetAmount();
    }

    @FXML
    private void btnAddAP(ActionEvent event) {
        grnote.getGrnotepoitemList().clear();
        for (Grnotepoitem grnotepoitem : tblGrnotepoitems.getItems()) {
            if (grnotepoitem.getQtyrecieved() != 0) {
                grnote.getGrnotepoitemList().add(grnotepoitem);
            }
        }
        String errors = getErrors();
        if (errors.isEmpty()) {
            String grnPoitems = "";
            for (Grnotepoitem grnotepoitem : grnote.getGrnotepoitemList()) {
                grnPoitems = grnPoitems + "Item :- " + grnotepoitem.getPoitemId().getItemId().getName() + "       Quantity :- " + grnotepoitem.getQtyrecieved() + "\n";
            }
            String confermation = "Ara you sure you need to add this Grnote with following details\n "
                    + "\nDate :         \t\t" + grnote.getDatetime()
                    + "\nPurchse Order :\t\t" + grnote.getPorderId().getDatetime() + grnote.getPorderId().getSupplierId().getCompany()
                    + "\n\n---------------------------\n "
                    + "Items : \n\n" + grnPoitems
                    + "\n----------------------------"
                    + "\nGross Amount :  \t\t" + grnote.getGrossamount()
                    + "\nNet Amount :    \t\t" + grnote.getNetamount();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Add GRNote with following Details");
            alert.setContentText(confermation);

            ButtonType ok = new ButtonType("ADD");
            ButtonType no = new ButtonType("DON'T");

            alert.getButtonTypes().setAll(ok, no);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ok) {

                try {
                    GrnoteDao.add(grnote);

                    //----------- Notification Success -------------
                    TrayNotification tray = new TrayNotification();
                    Image rightImg = new Image("/image/NotifyIcon/Right.png");
                    tray.setTitle("SUCCESS");
                    tray.setMessage("GRN " + grnote.getNo() + " saved.");
                    tray.setRectangleFill(Paint.valueOf("#00b84c"));
                    tray.showAndWait();
                    tray.setImage(rightImg);
                    tray.showAndDismiss(Duration.seconds(4));

                    loadForm();
                } catch (DaoException ex) {

                    //----------- Notification Unsuccess-------------
                    TrayNotification tray = new TrayNotification();
                    Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                    tray.setTitle("UN-SUCCESS");
                    tray.setMessage("GRN " + grnote.getNo() + " not saved. \n Try Again.");
                    tray.setRectangleFill(Paint.valueOf("#dc0000"));
                    tray.showAndWait();
                    tray.setImage(wrongImg);
                    tray.showAndDismiss(Duration.seconds(4));

                }
            } else if (result.get() == no) {
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("GRN Detail Error");
            alert.showAndWait();
        }
    }

    public String getErrors() {
        String errors = "";
        if (grnote.getUserEmployeeId() == null) {
            errors = errors + "Employee       \t\tis Not Selected\n";
        }
        if (grnote.getPorderId() == null) {
            errors = errors + "Purchase Order. \t\tis Invalid\n";
        }
        if (grnote.getGrnotepoitemList().size() == 0) {
            errors = errors + "Items           \t\t\tis Invalid\n";
        }
        if (grnote.getGrnotestatusId() == null) {
            errors = errors + "Status          \t\tis Not Selected\n";
        }
        if (grnote.getTotaldiscountper() == null) {
            errors = errors + "Discount        \t\t\tis Not Selected\n";
        }
        if (grnote.getTotaldiscountvalue() == null) {
            errors = errors + "Discount value  \t\tis Not Selected\n";
        }
        return errors;
    }

    @FXML
    private void btnUpdateAP(ActionEvent event) {
        String errors = getErrors();
        if (errors.isEmpty()) {
            String updates = getUpdates();
            System.out.println(grnote.getId());
            if (!updates.isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("GRN Update");
                alert.setContentText("Update GRN " + grnote.getNo());

                ButtonType ok = new ButtonType("UPDATE");
                ButtonType no = new ButtonType("DON'T");

                alert.getButtonTypes().setAll(ok, no);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ok) {
                    GrnoteDao.update(grnote);

                    //----------- Notification Success -------------
                    TrayNotification tray = new TrayNotification();
                    Image rightImg = new Image("/image/NotifyIcon/Right.png");
                    tray.setTitle("SUCCESS");
                    tray.setMessage("GRN " + grnote.getDatetime() + " updated.");
                    tray.setRectangleFill(Paint.valueOf("#00b84c"));
                    tray.showAndWait();
                    tray.setImage(rightImg);
                    tray.showAndDismiss(Duration.seconds(4));

                } else if (result.get() == no) {
                    // ... Canceled
                }
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText("GRNote Update");
                alert.setContentText("Nothing Updated");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("GRNote Update");
            alert.setContentText("GRNote not updated!");
            alert.showAndWait();
        }
    }

    public String getUpdates() {

        String updates = "";

        if (oldGrnote != null && getErrors().isEmpty()) {

            if (!grnote.getDatetime().equals(oldGrnote.getDatetime())) {
                updates = updates + oldGrnote.getDatetime() + " chnaged to " + grnote.getDatetime() + "\n";
            }
            if (!grnote.getPorderId().equals(oldGrnote.getPorderId())) {
                updates = updates + oldGrnote.getPorderId() + " chnaged to " + grnote.getPorderId() + "\n";
            }
            if (!grnote.getGrnotestatusId().equals(oldGrnote.getGrnotestatusId())) {
                updates = updates + oldGrnote.getGrnotestatusId() + " chnaged to " + grnote.getGrnotestatusId() + "\n";
            }
            if (!grnote.getGrnotepoitemList().containsAll(oldGrnote.getGrnotepoitemList())) {
                updates = updates + oldGrnote.getGrnotepoitemList() + " chnaged to " + grnote.getGrnotepoitemList() + "\n";
            } else if (!oldGrnote.getGrnotepoitemList().containsAll(grnote.getGrnotepoitemList())) {
                updates = updates + oldGrnote.getGrnotepoitemList() + " chnaged to " + grnote.getGrnotepoitemList() + "\n";
            } else {

                for (int i = 0; i < grnote.getGrnotepoitemList().size(); i++) {
                    if (grnote.getGrnotepoitemList().get(i).getQtyrecieved() != oldGrnote.getGrnotepoitemList().get(i).getQtyrecieved()) {
                        updates = updates + "Quantity:       \t\t" + oldGrnote.getGrnotepoitemList().get(i).getPoitemId().getItemId().getName() + " chnaged from " + oldGrnote.getGrnotepoitemList().get(i).getQtyrecieved() + " to " + grnote.getGrnotepoitemList().get(i).getQtyrecieved() + "\n";
                    }
                }
            }
            if (!grnote.getGrnotestatusId().equals(oldGrnote.getTotaldiscountper())) {
                updates = updates + "Total Discount:      \t\t" + oldGrnote.getTotaldiscountper() + " chnaged to " + grnote.getTotaldiscountper() + "\n";
            }
            if (!grnote.getGrnotestatusId().equals(oldGrnote.getTotaldiscountvalue())) {
                updates = updates + "Total Discount value:\t\t" + oldGrnote.getTotaldiscountvalue() + " chnaged to " + grnote.getTotaldiscountvalue() + "\n";
            }
        }
        return updates;
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
            // ... Canceled
        }
    }

    @FXML
    private void btnSearchAP(ActionEvent event) {
        try {
            loadForm();

            Alert subUI = new Alert(null, null);
            MyResourceBundle myResources = new MyResourceBundle();
            HashMap hm = new HashMap();

            Grnote grnote = new Grnote();

            hm.put("gr", grnote);
            hm.put("ui", subUI);
            myResources.setHashMap(hm);

            AnchorPane itemSearchUI = FXMLLoader.load(Main.class.getResource("GrnoteSearch.fxml"), myResources);
            subUI.getDialogPane().setContent(itemSearchUI);
            subUI.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            subUI.show();

            if (grnote.getId() != null) {
                grnote = GrnoteDao.getById(grnote.getId());
                fillForm(grnote);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void fillForm(Grnote grnote) {
        setStyle(valid);

        oldGrnote = GrnoteDao.getById(grnote.getId());
        this.grnote = GrnoteDao.getById(grnote.getId());

        if (this.grnote.getGrnotestatusId().equals(GrnoteStatusDao.getById(1))) {
            disableButtons(false, true, false, false);
        } else {
            disableButtons(false, true, true, true);
        }
        cmbSupplier.getSelectionModel().select((Supplier) this.grnote.getPorderId().getSupplierId());
        lblGrossAmount.setText(this.grnote.getGrossamount().toString());
        txtTotalDiscountPercentage.setText(grnote.getTotaldiscountper().toString());
        txtTotalDiscountValue.setText(grnote.getTotaldiscountvalue().toString());
        lblNetAmount.setText(grnote.getNetamount().toString());
        tblGrnotepoitems.getItems().clear();
        lblDate.setText(this.grnote.getDatetime().toString());
        lblNo.setText(this.grnote.getNo());
        tblPorder.getSelectionModel().select(this.grnote.getPorderId());
        tblPorderMC(null);
        tblGrnotepoitems.setItems(FXCollections.observableArrayList(this.grnote.getGrnotepoitemList()));
    }

    @FXML
    private void btnHomeAP() throws IOException {
        System.gc();
        Main.showHome();
    }

}
