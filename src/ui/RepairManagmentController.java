/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.DaoException;
import dao.Faults1Dao;
import dao.Faults2Dao;
import dao.RepairDao;
import dao.RepairStatusDao;
import entity.Faults1;
import entity.Faults2;
import entity.Repair;
import entity.Repairstatus;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.util.Duration;
import report.ReportView;
import tray.notification.TrayNotification;
import static ui.LoginController.privilage;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class RepairManagmentController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="Module-Data">
    //Color Indication of Input Controls
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    //Binding with the Form
    private Repair repair;

    //Update Identification
    private Repair oldRepair;

    //Table Row, Page Selected
    private int page;
    private int row;

    //Search Lock
    private boolean lock;

//</editor-fold>   
    @FXML
    private Label lblGotDate;
    @FXML
    private Label lblJobNo;
    @FXML
    private TextField txtCustomerName;
    @FXML
    private TextField txtContactNo;
    @FXML
    private TextField txtItemModel;
    @FXML
    private TextField txtIMEI;
    @FXML
    private TextArea txaRepordedItems;
    @FXML
    private DatePicker dtpAgreed;
    @FXML
    private DatePicker dtpReturned;
    @FXML
    private TextField txtActualCost;
    @FXML
    private TextField txtPrice;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;
    @FXML
    private ComboBox<Repairstatus> cmbStatus;
    @FXML
    private TextField txtSearchJobNo;
    @FXML
    private TextField txtSearchItemModel;
    @FXML
    private ComboBox<Repairstatus> cmbSearchStatus;
    @FXML
    private Button btnSearchLock;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Repair> tblRepair;
    @FXML
    private TableColumn<Repair, String> colJobNo;
    @FXML
    private TableColumn<Repair, String> colCustomerName;
    @FXML
    private TableColumn<Repair, String> colItemModel;
    @FXML
    private TableColumn<Repair, String> colGotDate;
    @FXML
    private TableColumn<Repair, String> colAgreedReturn;
    @FXML
    private TableColumn<Repair, String> colActualCost;
    @FXML
    private TableColumn<Repair, String> colPrice;
    @FXML
    private TableColumn<Repair, Repairstatus> colStatus;
    @FXML
    private Button btnSearchClear;
    @FXML
    private ComboBox<Faults1> cmbFault1;
    @FXML
    private ComboBox<Faults2> cmbFault2;

    /**
     * Initializes the controller class.
     */
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

        repair = new Repair();
        oldRepair = null;

        repair.setUserId(LoginController.user);
        lblJobNo.setText(RepairDao.getNextJobNo());
        repair.setJobno(lblJobNo.getText());
        lblGotDate.setText(LocalDate.now().toString());
        repair.setTookDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        repair.setPrice(BigDecimal.valueOf(Double.valueOf(txtPrice.getText())));
        repair.setActualcost(BigDecimal.valueOf(Double.valueOf(txtActualCost.getText())));

        cmbFault1.setItems(Faults1Dao.getAll());
        cmbFault1.getSelectionModel().select(-1);
        cmbFault2.setItems(Faults2Dao.getAll());
        cmbFault2.getSelectionModel().select(-1);
        cmbStatus.setItems(RepairStatusDao.getAll());
        cmbStatus.getSelectionModel().select(-1);

        txtItemModel.setText("");
        txtCustomerName.setText("");
        txtContactNo.setText("");
        txtIMEI.setText("");
        txtActualCost.setText("00.00");
        txaRepordedItems.setText("");
        txtPrice.setText("00.00");
        dtpAgreed.setValue(null);
        dtpReturned.setValue(null);

        dissableButtons(false, false, true, true);
        setStyle(initial);
    }

    private void loadTable() {
        lock = false;
        btnSearchLock.setText("SEARCH LOCK");

        cmbSearchStatus.setItems(RepairStatusDao.getAll());
        cmbSearchStatus.getSelectionModel().select(-1);

        txtSearchJobNo.setText("");
        txtSearchItemModel.setText("");

        colJobNo.setCellValueFactory(new PropertyValueFactory("jobno"));
        colStatus.setCellValueFactory(new PropertyValueFactory("repairStatusid"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory("customer"));
        colItemModel.setCellValueFactory(new PropertyValueFactory("item"));
        colGotDate.setCellValueFactory(new PropertyValueFactory("tookDate"));
        colAgreedReturn.setCellValueFactory(new PropertyValueFactory("agreeddate"));
        colActualCost.setCellValueFactory(new PropertyValueFactory("actualcost"));
        colPrice.setCellValueFactory(new PropertyValueFactory("price"));

        fillTable(RepairDao.getAllReverse());
        pagination.setCurrentPageIndex(0);
    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Repair_insert"));
        btnUpdate.setDisable(update || !privilage.get("Repair_update"));
        btnDelete.setDisable(delete || !privilage.get("Repair_delete"));

        txtSearchJobNo.setDisable(select || !privilage.get("Repair_select"));
        txtSearchItemModel.setDisable(select || !privilage.get("Repair_select"));
        cmbSearchStatus.setDisable(select || !privilage.get("Repair_select"));

        btnSearchLock.setDisable(select || !privilage.get("Repair_select"));
        btnSearchClear.setDisable(select || !privilage.get("Repair_select"));
    }

    private void setStyle(String style) {
        cmbFault1.setStyle(style);
        cmbFault2.setStyle(style);
        cmbStatus.setStyle(style);

        txtItemModel.setStyle(style);
        txtCustomerName.setStyle(style);
        txtContactNo.setStyle(style);
        txtIMEI.setStyle(style);
        txtActualCost.setStyle(style);
        txtPrice.setStyle(style);

        if (!txaRepordedItems.getChildrenUnmodifiable().isEmpty()) {
            ((ScrollPane) txaRepordedItems.getChildrenUnmodifiable().get(0)).getContent().setStyle(style);
        }

        dtpAgreed.getEditor().setStyle(style);
        dtpReturned.getEditor().setStyle(style);
    }

    private void fillTable(ObservableList<Repair> repairs) {

        if (privilage.get("Repair_select") && repairs != null && repairs.size() != 0) {

            int rowsCount = 11;
            int pageCount = ((repairs.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? repairs.size() : pageIndex * rowsCount + rowsCount;
                    tblRepair.getItems().clear();
                    tblRepair.setItems(FXCollections.observableArrayList(repairs.subList(start, end)));
                    return tblRepair;
                }
            });

        } else {

            pagination.setPageCount(1);
            tblRepair.getItems().clear();
        }
    }

    @FXML
    private void txtCustomerNameKR(KeyEvent event) {
        if (repair.setCustomer(txtCustomerName.getText().trim())) {
            if (oldRepair != null && !repair.getCustomer().equals(oldRepair.getCustomer())) {
                txtCustomerName.setStyle(updated);
            } else {
                txtCustomerName.setStyle(valid);
            }
        } else {
            txtCustomerName.setStyle(invalid);
        }
    }

    @FXML
    private void txtContactNoKR(KeyEvent event) {
        if (repair.setContactno(txtContactNo.getText().trim())) {
            if (oldRepair != null && !repair.getContactno().equals(oldRepair.getContactno())) {
                txtContactNo.setStyle(updated);
            } else {
                txtContactNo.setStyle(valid);
            }
        } else {
            txtContactNo.setStyle(invalid);
        }
    }

    @FXML
    private void txtItemModelKR(KeyEvent event) {
        if (repair.setItem(txtItemModel.getText().trim())) {
            if (oldRepair != null && !repair.getItem().equals(oldRepair.getItem())) {
                txtItemModel.setStyle(updated);
            } else {
                txtItemModel.setStyle(valid);
            }
        } else {
            txtItemModel.setStyle(invalid);
        }
    }

    @FXML
    private void txtIMEIKR(KeyEvent event) {
        if (repair.setImeino(txtIMEI.getText().trim())) {
            if (oldRepair != null && !repair.getImeino().equals(oldRepair.getImeino())) {
                txtIMEI.setStyle(updated);
            } else {
                txtIMEI.setStyle(valid);
            }
        } else {
            txtIMEI.setStyle(invalid);
        }

    }

    @FXML
    private void txaRepordedItemsKR(KeyEvent event) {
        if (repair.setItemsTaken(txaRepordedItems.getText().trim())) {
            if (oldRepair != null && !repair.getItemsTaken().equals(oldRepair.getItemsTaken())) {
                ((ScrollPane) txaRepordedItems.getChildrenUnmodifiable().get(0)).getContent().setStyle(updated);
            } else {
                ((ScrollPane) txaRepordedItems.getChildrenUnmodifiable().get(0)).getContent().setStyle(valid);
            }
        } else {
            ((ScrollPane) txaRepordedItems.getChildrenUnmodifiable().get(0)).getContent().setStyle(invalid);
        }
    }

    @FXML
    private void dtpAgreedAP(ActionEvent event) {
        if (dtpAgreed.getValue() != null) {
            Date today = new Date();
            Date agreed = java.sql.Date.valueOf(dtpAgreed.getValue());
            if (agreed.before(today)) {
                dtpAgreed.getEditor().setStyle(invalid);
            } else {
                repair.setAgreeddate(agreed);
                if (oldRepair != null && !repair.getAgreeddate().equals(oldRepair.getAgreeddate())) {
                    dtpAgreed.getEditor().setStyle(updated);
                } else {
                    dtpAgreed.getEditor().setStyle(valid);
                }
            }
        }
    }

    @FXML
    private void dtpReturnedAP(ActionEvent event) {
        if (dtpReturned.getValue() != null) {
            Date today = new Date();
            Date returned = java.sql.Date.valueOf(dtpReturned.getValue());
            if (returned.before(today)) {
                dtpReturned.getEditor().setStyle(invalid);
            } else {
                repair.setHandedOverDate(returned);
                if (oldRepair != null && !repair.getHandedOverDate().equals(oldRepair.getHandedOverDate())) {
                    dtpReturned.getEditor().setStyle(updated);
                } else {
                    dtpReturned.getEditor().setStyle(valid);
                }
            }
        }
    }

    @FXML
    private void txtActualCostKR(KeyEvent event) {
        if (txtActualCost.getText().matches("\\d+(\\.\\d{0,2})?") && repair.setActualcost(BigDecimal.valueOf(Double.valueOf(txtActualCost.getText().trim())))) {
            if (oldRepair != null && !repair.getActualcost().equals(oldRepair.getActualcost())) {
                txtActualCost.setStyle(updated);
            } else {
                txtActualCost.setStyle(valid);
            }
        } else {
            txtActualCost.setStyle(invalid);
        }
    }

    @FXML
    private void txtPriceKR(KeyEvent event) {
        if (txtPrice.getText().matches("\\d+(\\.\\d{0,2})?") && repair.setPrice(BigDecimal.valueOf(Double.valueOf(txtPrice.getText().trim())))) {
            if (oldRepair != null && !repair.getPrice().equals(oldRepair.getPrice())) {
                txtPrice.setStyle(updated);
            } else {
                txtPrice.setStyle(valid);
            }
        } else {
            txtPrice.setStyle(invalid);
        }
    }

    @FXML
    private void btnAddAP(ActionEvent event) {
        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "Ara you sure you need to add this Employee with following details\n "
                    + "\nJob No :         \t\t" + repair.getJobno()
                    + "\nGot Date :       \t\t" + repair.getTookDate()
                    + "\nCustomer Name :   \t" + repair.getCustomer()
                    + "\nContact No :       \t" + repair.getContactno()
                    + "\nIMEI No. :  \t\t" + repair.getImeino()
                    + "\nFaults1 :      \t\t" + repair.getFaults1Id()
                    + "\nFaults2 :      \t\t" + repair.getFaults2Id()
                    + "\nReported Items :    \t" + repair.getItemsTaken()
                    + "\nAgreed Date :        \t" + repair.getAgreeddate().toString();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Add Job with following Details");
            alert.setContentText(confermation);

            ButtonType ok = new ButtonType("ADD");
            ButtonType no = new ButtonType("DON'T");

            alert.getButtonTypes().setAll(ok, no);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ok) {

                try {
                    repair.setHandedOverDate(repair.getAgreeddate());
                    RepairDao.add(repair);

                    //------------- Print Bill--------------------
                    Integer size = RepairDao.getAll().size();

                    Repair inv = (Repair) RepairDao.getAll().get(size - 1);
                    HashMap hmap = new HashMap();
                    hmap.put("RepID", inv.getId());

                    new ReportView("/report/RepSlip.jasper", hmap);

                    //----------- Notification Success -------------
                    TrayNotification tray = new TrayNotification();
                    Image rightImg = new Image("/image/NotifyIcon/Right.png");
                    tray.setTitle("SUCCESS");
                    tray.setMessage("Repair Item " + repair.getJobno() + " saved.");
                    tray.setRectangleFill(Paint.valueOf("#00b84c"));
                    tray.showAndWait();
                    tray.setImage(rightImg);
                    tray.showAndDismiss(Duration.seconds(4));

                    loadForm();
                    updateTable();
                    loadTable();
                    pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                    tblRepair.getSelectionModel().select(tblRepair.getItems().size() - 1);

                } catch (DaoException ex) {
                    //----------- Notification Unsuccess-------------
                    TrayNotification tray = new TrayNotification();
                    Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                    tray.setTitle("UN-SUCCESS");
                    tray.setMessage("Repair Item " + repair.getJobno() + " not saved. \n Try Again.");
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

    private String getErrors() {
        String errors = "";

        if (repair.getItem() == null) {
            errors = errors + "Item \t\t\tis Invalid\n";
        }
        if (repair.getAgreeddate() == null) {
            errors = errors + "Return Date \t\tis Invalid\n";
        }
        if (repair.getCustomer() == null) {
            errors = errors + "Customer name \t\tis Not Selected\n";
        }
        if (repair.getFaults1Id() == null) {
            errors = errors + "Reported Fault \t\tis Invalid\n";
        }
        if (repair.getRepairStatusid() == null) {
            errors = errors + "Repair Status \t\tis Not Selected\n";
        }
        return errors;
    }

    @FXML
    private void btnUpdateAP(ActionEvent event) {
        String errors = getErrors();

        if (errors.isEmpty()) {

            String updates = getUpdates();

            if (!updates.isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Job Update");
                alert.setContentText("Update " + repair.getJobno());

                ButtonType ok = new ButtonType("UPDATE");
                ButtonType no = new ButtonType("DON'T");

                alert.getButtonTypes().setAll(ok, no);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ok) {

                    RepairDao.update(repair);

                    //------------- Print Bill--------------------
                    Integer inv = tblRepair.getSelectionModel().getSelectedItem().getId();
                    HashMap hmap = new HashMap();
                    hmap.put("RepID", inv);

                    new ReportView("/report/RepSlip.jasper", hmap);

                    //----------- Notification Success -------------
                    TrayNotification tray = new TrayNotification();
                    Image rightImg = new Image("/image/NotifyIcon/Right.png");
                    tray.setTitle("SUCCESS");
                    tray.setMessage("Repair Item " + repair.getJobno() + " updated.");
                    tray.setRectangleFill(Paint.valueOf("#00b84c"));
                    tray.showAndWait();
                    tray.setImage(rightImg);
                    tray.showAndDismiss(Duration.seconds(4));

                    updateTable();
                    loadForm();
                    loadTable();
                    pagination.setCurrentPageIndex(page);
                    tblRepair.getSelectionModel().select(row);
                } else if (result.get() == no) {
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText("Job Update");
                alert.setContentText("Nothing Updated");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("Job Update");
            alert.setContentText("Job not updated!");
            alert.showAndWait();
        }
    }

    private String getUpdates() {

        String updates = "";

        if (oldRepair != null) {

            if (repair.getItem() != null && !repair.getItem().equals(oldRepair.getItem())) {
                updates = updates + oldRepair.getItem() + " chnaged to " + repair.getItem() + "\n";
            }

            if (!repair.getCustomer().equals(oldRepair.getCustomer())) {
                updates = updates + oldRepair.getCustomer() + " chnaged to " + repair.getCustomer() + "\n";
            }

            if (!(oldRepair.getContactno() != null && repair.getContactno() != null && oldRepair.getContactno().equals(repair.getContactno()))) {
                if (oldRepair.getContactno() != repair.getContactno()) {
                    updates = updates + oldRepair.getContactno() + " chnaged to " + repair.getContactno() + "\n";
                }
            }

            if (!(oldRepair.getImeino() != null && repair.getImeino() != null && oldRepair.getImeino().equals(repair.getImeino()))) {
                if (oldRepair.getImeino() != repair.getImeino()) {
                    updates = updates + oldRepair.getImeino() + " chnaged to " + repair.getImeino() + "\n";
                }
            }

            if (!repair.getRepairStatusid().equals(oldRepair.getRepairStatusid())) {
                updates = updates + oldRepair.getRepairStatusid() + " chnaged to " + repair.getRepairStatusid() + "\n";
            }

            if (!repair.getFaults1Id().equals(oldRepair.getFaults1Id())) {
                updates = updates + oldRepair.getFaults1Id() + " chnaged to " + repair.getFaults1Id() + "\n";
            }

            if (!repair.getFaults2Id().equals(oldRepair.getFaults2Id())) {
                updates = updates + oldRepair.getFaults2Id() + " chnaged to " + repair.getFaults2Id() + "\n";
            }

            if (!(oldRepair.getItemsTaken() != null && repair.getItemsTaken() != null && oldRepair.getItemsTaken().equals(repair.getItemsTaken()))) {
                if (oldRepair.getItemsTaken() != repair.getItemsTaken()) {
                    updates = updates + oldRepair.getItemsTaken() + " chnaged to " + repair.getItemsTaken() + "\n";
                }
            }

            if (!repair.getActualcost().equals(oldRepair.getActualcost())) {
                updates = updates + oldRepair.getActualcost() + " chnaged to " + repair.getActualcost() + "\n";
            }

            if (!repair.getPrice().equals(oldRepair.getPrice())) {
                updates = updates + oldRepair.getPrice() + " chnaged to " + repair.getPrice() + "\n";
            }

            if (!repair.getAgreeddate().equals(oldRepair.getAgreeddate())) {
                updates = updates + oldRepair.getAgreeddate().toString() + " Agreed Date" + " chnaged to " + repair.getAgreeddate().toString() + "\n";
            }

            if (!(oldRepair.getHandedOverDate() != null && repair.getHandedOverDate() != null && oldRepair.getHandedOverDate().equals(repair.getHandedOverDate()))) {
                if (oldRepair.getHandedOverDate() != repair.getHandedOverDate()) {
                    updates = updates + oldRepair.getHandedOverDate().toString() + " HandedOverDate" + " chnaged to " + repair.getHandedOverDate().toString() + "\n";
                }
            }

        }
        return updates;
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
        if (getUpdates().isEmpty() && getErrors().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Job Delete");
            alert.setContentText("Delete " + repair.getJobno());

            ButtonType ok = new ButtonType("DELETE");
            ButtonType no = new ButtonType("DON'T");

            alert.getButtonTypes().setAll(ok, no);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ok) {

                RepairDao.delete(repair);

                //----------- Notification Success -------------
                TrayNotification tray = new TrayNotification();
                Image rightImg = new Image("/image/NotifyIcon/Right.png");
                tray.setTitle("SUCCESS");
                tray.setMessage("Repair Item " + repair.getItem() + " deleted.");
                tray.setRectangleFill(Paint.valueOf("#00b84c"));
                tray.showAndWait();
                tray.setImage(rightImg);
                tray.showAndDismiss(Duration.seconds(4));

                loadForm();
                updateTable();
                pagination.setCurrentPageIndex(page);
                tblRepair.getSelectionModel().select(row);

            } else if (result.get() == no) {
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Job Delete");
            alert.setHeaderText(oldRepair.getJobno() + " Delete ?");
            alert.setContentText("You can't delete\nSome of the fields are updated");
            alert.showAndWait();
        }
    }

    @FXML
    private void btnClearAP(ActionEvent event) {
        loadForm();
        loadTable();
    }

    @FXML
    private void cmbFault1AP(ActionEvent event) {
        repair.setFaults1Id(cmbFault1.getSelectionModel().getSelectedItem());
        if (oldRepair != null && !repair.getFaults1Id().equals(oldRepair.getFaults1Id())) {
            cmbFault1.setStyle(updated);
        } else {
            cmbFault1.setStyle(valid);
        }
    }

    @FXML
    private void cmbFault2AP(ActionEvent event) {
        repair.setFaults2Id(cmbFault2.getSelectionModel().getSelectedItem());
        if (oldRepair != null && !repair.getFaults2Id().equals(oldRepair.getFaults2Id())) {
            cmbFault2.setStyle(updated);
        } else {
            cmbFault2.setStyle(valid);
        }
    }

    @FXML
    private void cmbStatusAP(ActionEvent event) {
        repair.setRepairStatusid(cmbStatus.getSelectionModel().getSelectedItem());
        if (oldRepair != null && !repair.getRepairStatusid().equals(oldRepair.getRepairStatusid())) {
            cmbStatus.setStyle(updated);
        } else {
            cmbStatus.setStyle(valid);
        }
    }

    @FXML
    private void txtSearchJobNoKR(KeyEvent event) {
        if (!lock) {
            txtSearchItemModel.setText("");
            cmbSearchStatus.getSelectionModel().select(-1);
        }
        updateTable();
    }

    @FXML
    private void txtSearchItemModelKR(KeyEvent event) {
        if (!lock) {
            txtSearchJobNo.setText("");
            cmbSearchStatus.getSelectionModel().select(-1);
        }
        updateTable();
    }

    @FXML
    private void cmbSearchStatusAP(ActionEvent event) {
        if (!lock) {
            txtSearchJobNo.setText("");
            txtSearchItemModel.setText("");
        }
        updateTable();
    }

    private void updateTable() {
        String jobno = txtSearchJobNo.getText().trim() + lblJobNo.getText().trim();
        boolean sjobno = !jobno.isEmpty();
        String item = txtSearchItemModel.getText().trim() + txtItemModel.getText().trim();
        boolean sitem = !item.isEmpty();
        Repairstatus status = cmbSearchStatus.getSelectionModel().getSelectedItem();
        boolean sstatus = cmbSearchStatus.getSelectionModel().getSelectedIndex() != -1;

        if (!sjobno && !sitem && !sstatus) {
            fillTable(RepairDao.getAll());
        }
        if (sjobno && !sitem && !sstatus) {
            fillTable(RepairDao.getAllByJobNo(jobno));
        }
        if (!sjobno && sitem && !sstatus) {
            fillTable(RepairDao.getAllByItem(item));
        }
        if (!sjobno && !sitem && sstatus) {
            fillTable(RepairDao.getAllByStatus(status));
        }
        if (sjobno && !sitem && sstatus) {
            fillTable(RepairDao.getAllByJobNoStatus(jobno, status));
        }
        if (sjobno && sitem && !sstatus) {
            fillTable(RepairDao.getAllByJobNoItem(jobno, item));
        }
        if (!sjobno && sitem && sstatus) {
            fillTable(RepairDao.getAllByItemStatus(item, status));
        }
        if (sjobno && sitem && sstatus) {
            fillTable(RepairDao.getAllByJobNoItemStatus(jobno, item, status));
        }
    }

    @FXML
    private void btnSearchLockAP(ActionEvent event) {
        if (lock) {
            btnSearchLock.setText("SEARCH LOCK");
            lock = false;
            fillTable(RepairDao.getAll());
        } else {
            btnSearchLock.setText("UNLOCK");
            lock = true;
            updateTable();
        }
    }

    @FXML
    private void tblRepairMC(MouseEvent event) {
        fillForm();
    }

    @FXML
    private void btnSearchClearAP(ActionEvent event) {
        loadTable();
    }

    private void fillForm() {
        if (tblRepair.getSelectionModel().getSelectedItem() != null) {
            dissableButtons(false, true, false, false);
            setStyle(valid);

            oldRepair = RepairDao.getById(tblRepair.getSelectionModel().getSelectedItem().getId());
            repair = RepairDao.getById(tblRepair.getSelectionModel().getSelectedItem().getId());

            cmbFault1.getSelectionModel().select((Faults1) repair.getFaults1Id());
            cmbFault2.getSelectionModel().select((Faults2) repair.getFaults2Id());
            cmbStatus.getSelectionModel().select((Repairstatus) repair.getRepairStatusid());

            txtItemModel.setText(repair.getItem());
            txtCustomerName.setText(repair.getCustomer());
            txtContactNo.setText(repair.getContactno());
            txtIMEI.setText(repair.getImeino());
            txtActualCost.setText(repair.getActualcost().toString());
            txtPrice.setText(repair.getPrice().toString());
            txaRepordedItems.setText(repair.getItemsTaken());

            lblJobNo.setText(repair.getJobno());
            lblGotDate.setText(repair.getTookDate().toString());

            dtpReturned.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(repair.getHandedOverDate())));
            dtpReturned.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(repair.getHandedOverDate()));
            dtpAgreed.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(repair.getAgreeddate())));
            dtpAgreed.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(repair.getAgreeddate()));

            page = pagination.getCurrentPageIndex();
            row = tblRepair.getSelectionModel().getSelectedIndex();
        }
    }
    
    @FXML
    private void lblNewFaultMC() throws IOException {
        Main.showFault();
    }

    @FXML
    private void btnHomeAP() throws IOException {
        System.gc();
        Main.showHome();
    }


}
