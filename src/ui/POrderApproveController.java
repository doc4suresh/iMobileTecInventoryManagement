/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.POStatusDao;
import dao.POrderDao;
import dao.SupplierDao;
import entity.Poitem;
import entity.Porder;
import entity.Postatus;
import entity.Supplier;
import java.math.BigDecimal;
import java.net.URL;
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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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
public class POrderApproveController implements Initializable {

    @FXML
    private TableView<Porder> tblPOrder;
    @FXML
    private TableColumn<Porder, String> colPONo;
    @FXML
    private TableColumn<Porder, Date> colDate;
    @FXML
    private TableColumn<Porder, Supplier> colSupplier;
    @FXML
    private TableColumn<Porder, Postatus> colStatus;
    @FXML
    private Pagination pagination;
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
    private Button btnApprove;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblSupplier;
    @FXML
    private Label lblStatus;
    @FXML
    private ComboBox<Supplier> cmbSupplier;
    @FXML
    private ComboBox<Postatus> cmbStatus;
    @FXML
    private Button btnClearSearch;
    @FXML
    private Button btnPrint;

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

    ResourceBundle rb;

// </editor-fold>
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        loadForm();
        loadTable();
    }

    //loadForm(), fillForm(Porder) 
    private void disableButtons(boolean select, boolean insert, boolean update, boolean delete) {

        btnUpdate.setDisable(update || !privilage.get("Porder_update"));
        btnDelete.setDisable(delete || !privilage.get("Porder_delete"));
        btnApprove.setDisable(delete || !privilage.get("Porder_select"));
        btnPrint.setDisable(delete || !privilage.get("Porder_select"));
    }

    //initialize(), btnClerAP(), btnAdd(), btnUpdate(), btnDelete(), Pagination.call(), 
    private void loadForm() {

        porder = new Porder();
        oldPorder = null;
        List<Poitem> poitems = new ArrayList();
        porder.setPoitemList(poitems);
        disableButtons(false, false, true, true);
        btnApprove.setDisable(true);
        btnPrint.setDisable(true);
        tblPOItem.getItems().clear();
        loadPOItemFormTable();
        cmbSupplier.setItems(SupplierDao.getAll());
        cmbStatus.setItems(POStatusDao.getAll());
        cmbSupplier.getSelectionModel().select(-1);
        cmbStatus.getSelectionModel().select(-1);
    }

    //initial() , btnSearchClearAP() 
    private void loadTable() {

        colPONo.setCellValueFactory(new PropertyValueFactory("no"));
        colDate.setCellValueFactory(new PropertyValueFactory("datetime"));
        colSupplier.setCellValueFactory(new PropertyValueFactory("supplierId"));
        colStatus.setCellValueFactory(new PropertyValueFactory("postatusId"));

        tblPOrder.setRowFactory((TableView<Porder> param) -> {
            TableRow row = new TableRow<Porder>() {
                @Override
                protected void updateItem(Porder item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        if (item.getPostatusId().getId() == 1) {
                            setStyle(updated);
                        }
                        if (item.getPostatusId().getId() == 2) {
                            setStyle(valid);
                        }
                        if (item.getPostatusId().getId() == 3) {
                            setStyle(invalid);
                        }
                    }
                }
            };
            return row;
        });
        fillTable(POrderDao.getAllReverse(), 0);
    }

    //loadTable(), fillTableSearch(),  btnAddAP()
    private void fillTable(ObservableList<Porder> porders, int selectedPage) {
        if (porders != null && porders.size() != 0) {
            int rowsCount = 8;
            int pageCount = ((porders.size() - 1) / rowsCount) + 1;
            page = selectedPage != -1 ? selectedPage : pageCount - 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    if (oldPorder != null) {
                        loadForm();
                    }
                    page = pageIndex;
                    int start = page * rowsCount;
                    int end = page == pageCount - 1 ? porders.size() : page * rowsCount + rowsCount;
                    tblPOrder.getItems().clear();
                    tblPOrder.setItems(FXCollections.observableArrayList(porders.subList(start, end)));
                    return tblPOrder;
                }
            });
            pagination.setCurrentPageIndex(selectedPage);
        } else {
            tblPOrder.getItems().clear();
            pagination.setPageCount(1);
            if (oldPorder != null) {
                loadForm();
            }
        }
    }

    //loadForm()
    private void loadPOItemFormTable() {
        colPOItemName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Poitem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Poitem, String> i) {
                return new SimpleStringProperty(i.getValue().getItemId().getName());
            }
        });
        colQuantity.setCellValueFactory(new PropertyValueFactory("qtyrequested"));
        colPOBrand.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Poitem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Poitem, String> i) {
                return new SimpleStringProperty(i.getValue().getItemId().getBrandId().getName());
            }
        });
        colLineTotal.setCellValueFactory(new PropertyValueFactory("linetotal"));
    }

    @FXML
    private void tblPOrderMC(MouseEvent event) {
        fillForm(tblPOrder.getSelectionModel().getSelectedItem());
        btnPrint.setDisable(false);
    }

    @FXML
    private void tblPOrderKR(KeyEvent event) {
        fillForm(tblPOrder.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void btnApproveAP(ActionEvent event) {
        String poitems = "";
        for (Poitem poitem : tblPOItem.getItems()) {
            poitems = poitems + "Item :- " + poitem.getItemId().getName() + "       Quantity :- " + poitem.getQtyrequested() + "\n";
        }
        String confermation = "Ara you sure you need to Approve this Porder with following details\n "
                + "\nDate :         \t\t" + porder.getDatetime()
                + "\nSupplier :     \t\t" + porder.getSupplierId().getCompany()
                + "\n\n---------------------------\n "
                + "Items : \n\n" + poitems
                + "\n----------------------------"
                + "\nTotal :        \t\t" + porder.getTotal().toString();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Approve POrder with following Details");
        alert.setContentText(confermation);

        ButtonType ok = new ButtonType("APPROVE");
        ButtonType no = new ButtonType("DON'T");

        alert.getButtonTypes().setAll(ok, no);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ok) {

            if (porder != null && porder.getPostatusId().equals(POStatusDao.getById(1))) {
                porder.setPostatusId(POStatusDao.getById(2));
                POrderDao.update(porder);
            }

            //----------- Notification Success -------------
            TrayNotification tray = new TrayNotification();
            Image rightImg = new Image("/image/NotifyIcon/Right.png");
            tray.setTitle("SUCCESS");
            tray.setMessage("Porder " + porder.getNo() + " Approved.");
            tray.setRectangleFill(Paint.valueOf("#00b84c"));
            tray.showAndWait();
            tray.setImage(rightImg);
            tray.showAndDismiss(Duration.seconds(4));

        } else if (result.get() == no) {
        }
        fillForm(porder);
        loadTable();
    }

    @FXML
    private void btnUpdateAP(ActionEvent event) {
        Porder porder = (Porder) rb.getObject("it");
        porder.setId(tblPOrder.getSelectionModel().getSelectedItem().getId());

        Alert alert = (Alert) rb.getObject("ui");
        alert.hide();
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Purchase Order Delete");
        alert.setContentText("Delete Purchase Order " + porder.getNo());

        ButtonType ok = new ButtonType("DELETE");
        ButtonType no = new ButtonType("DON'T");

        alert.getButtonTypes().setAll(ok, no);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ok) {

            POrderDao.delete(porder);

            //----------- Notification Success -------------
            TrayNotification tray = new TrayNotification();
            Image rightImg = new Image("/image/NotifyIcon/Right.png");
            tray.setTitle("SUCCESS");
            tray.setMessage("Porder " + porder.getNo() + " Deleted.");
            tray.setRectangleFill(Paint.valueOf("#00b84c"));
            tray.showAndWait();
            tray.setImage(rightImg);
            tray.showAndDismiss(Duration.seconds(4));

            loadForm();
            fillTable(POrderDao.getAll(), page);
            tblPOrder.getSelectionModel().select(row);

        } else if (result.get() == no) {
        }
    }

    //navigateForm()
    private void fillForm(Porder porder) {

        oldPorder = POrderDao.getById(porder.getId());
        this.porder = POrderDao.getById(porder.getId());
        disableButtons(true, true, true, true);
        if (this.porder.getPostatusId().equals(POStatusDao.getById(1))) {
            disableButtons(false, true, false, false);
        }

        tblPOItem.setItems(FXCollections.observableArrayList(this.porder.getPoitemList()));
        lblTotal.setText(porder.getTotal().toString());
        lblDate.setText(porder.getDatetime().toString());
        lblSupplier.setText(porder.getSupplierId().getCompany());
        lblStatus.setText(porder.getPostatusId().getName());
    }

    @FXML
    private void cmbSupplierAP(ActionEvent event) {
        if (cmbSupplier.getSelectionModel().getSelectedItem() != null) {
            cmbStatus.getSelectionModel().select(-1);
            loadTableSearch(0);
        }
    }

    @FXML
    private void cmbStatusAP(ActionEvent event) {
        if (cmbStatus.getSelectionModel().getSelectedItem() != null) {
            cmbSupplier.getSelectionModel().select(-1);
            loadTableSearch(0);
        }
    }

    @FXML
    private void btnClearSearchAP(ActionEvent event) {
        loadForm();
        loadTable();
    }

    //txtSearchQuantity(), dtpSearchDate(), cmbSearchItem(), btnUpdateAP(), btnDeleteAP(), 
    private void loadTableSearch(int page) {
//        LocalDate date = dtpSearchDate.getValue();
//        boolean sdate = date.toString().isEmpty();
        Supplier supplier = cmbSupplier.getSelectionModel().getSelectedItem();
        boolean ssupplier = cmbSupplier.getSelectionModel().getSelectedIndex() != -1;
        Postatus status = cmbStatus.getSelectionModel().getSelectedItem();
        boolean sstatus = cmbStatus.getSelectionModel().getSelectedIndex() != -1;
        if (ssupplier) {
            porders = POrderDao.getBySupplier(supplier);
        }
        if (sstatus) {
            porders = POrderDao.getByStatus(status);
        }
        if (!sstatus && !ssupplier) {
            porders = POrderDao.getAll();
        }
        fillTable(porders, page);
    }

    @FXML
    private void btnPrintAP(ActionEvent event) {
        Integer id = tblPOrder.getSelectionModel().getSelectedItem().getId();
        HashMap hmap = new HashMap();
        hmap.put("poID", id);

        new ReportView("/report/POPrint.jasper", hmap);
    }
}
