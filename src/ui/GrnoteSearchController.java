/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.GrnoteDao;
import dao.GrnoteStatusDao;
import dao.ItemDao;
import dao.POStatusDao;
import dao.POrderDao;
import dao.PoitemDao;
import dao.SupplierDao;
import entity.Grnote;
import entity.Grnotepoitem;
import entity.Grnotestatus;
import entity.Supplier;
import java.net.URL;
import java.util.ArrayList;
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
public class GrnoteSearchController implements Initializable {

    @FXML
    private ComboBox<Supplier> cmbSupplier;
    @FXML
    private ComboBox<Grnotestatus> cmbStatus;
    @FXML
    private Button btnClearSearch;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Grnote> tblGrnote;
    @FXML
    private TableColumn<?, ?> colGrnoteNo;
    @FXML
    private TableColumn<?, ?> colDate;
    @FXML
    private TableColumn<?, ?> colPoNo;
    @FXML
    private TableColumn<?, ?> colStatus;
    @FXML
    private Label lblSupplier;
    @FXML
    private Label lblPorder;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblStatus;
    @FXML
    private Label lblTotal;
    @FXML
    private TableView<Grnotepoitem> tblGrnotepoitems;
    @FXML
    private TableColumn<Grnotepoitem, String> colGrnoteItemName;
    @FXML
    private TableColumn<Grnotepoitem, String> colRecived;
    @FXML
    private TableColumn<Grnotepoitem, String> colPPrice;
    @FXML
    private TableColumn<Grnotepoitem, String> colLineTotal;
    @FXML
    private TableColumn<Grnotepoitem, String> colSPrice;
    @FXML
    private Button btnApprove;
    @FXML
    private Button btnPPreview;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

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

    private void loadForm() {

        this.grnote = new Grnote();
        oldGrnote = null;
        List<Grnotepoitem> grnotepoitems = new ArrayList();
        grnote.setGrnotepoitemList(grnotepoitems);

        cmbSupplier.setItems(SupplierDao.getAll());
        cmbStatus.setItems(GrnoteStatusDao.getAll());
        cmbSupplier.getSelectionModel().select(-1);
        cmbStatus.getSelectionModel().select(-1);

        disableButtons(false, false, true, true);
        btnApprove.setDisable(true);
        tblGrnotepoitems.getItems().clear();
        loadGrnoteItemFormTable();
        btnPPreview.setDisable(true);
    }

    //loadForm(), fillForm(Porder) 
    private void disableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnUpdate.setDisable(update || !privilage.get("Porder_update"));
        btnDelete.setDisable(delete || !privilage.get("Porder_delete"));
        btnApprove.setDisable(delete || !privilage.get("Porder_select"));
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

    //initial() , btnSearchClearAP() 
    private void loadTable() {
        colGrnoteNo.setCellValueFactory(new PropertyValueFactory("no"));
        colDate.setCellValueFactory(new PropertyValueFactory("datetime"));
        colPoNo.setCellValueFactory(new PropertyValueFactory("porderId"));
        colStatus.setCellValueFactory(new PropertyValueFactory("grnotestatusId"));
        tblGrnote.setRowFactory((TableView<Grnote> param) -> {
            TableRow row = new TableRow<Grnote>() {
                @Override
                protected void updateItem(Grnote item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else if (item.getGrnotestatusId().getId() == 1) {
                        setStyle(updated);
                    }
                }
            };
            return row;
        });
        fillTable(GrnoteDao.getAllReverse(), 0);
    }

    //loadTable(), fillTableSearch(),  btnAddAP()
    private void fillTable(ObservableList<Grnote> grnotes, int selectedPage) {
        if (grnotes != null && grnotes.size() != 0) {
            int rowsCount = 13;
            int pageCount = ((grnotes.size() - 1) / rowsCount) + 1;

            page = selectedPage != -1 ? selectedPage : pageCount - 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    if (oldGrnote != null) {
                        loadForm();
                    }
                    page = pageIndex;
                    int start = page * rowsCount;
                    int end = page == pageCount - 1 ? grnotes.size() : page * rowsCount + rowsCount;
                    tblGrnote.getItems().clear();
                    tblGrnote.setItems(FXCollections.observableArrayList(grnotes.subList(start, end)));
                    return tblGrnote;
                }
            });
            pagination.setCurrentPageIndex(selectedPage);
        } else {
            tblGrnote.getItems().clear();
            pagination.setPageCount(1);
            if (oldGrnote != null) {
                loadForm();
            }
        }
    }

    //txtSearchQuantity(), dtpSearchDate(), cmbSearchItem(), btnUpdateAP(), btnDeleteAP(), 
    private void loadTableSearch(int page) {
        Supplier supplier = cmbSupplier.getSelectionModel().getSelectedItem();
        boolean ssupplier = cmbSupplier.getSelectionModel().getSelectedIndex() != -1;
        Grnotestatus status = cmbStatus.getSelectionModel().getSelectedItem();
        boolean sstatus = cmbStatus.getSelectionModel().getSelectedIndex() != -1;
        if (ssupplier) {
            grnotes = GrnoteDao.getBySupplier(supplier);
        }
        if (sstatus) {
            grnotes = GrnoteDao.getByStatus(status);
        }
        if (!sstatus && !ssupplier) {
            grnotes = GrnoteDao.getAll();
        }
        fillTable(grnotes, page);
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
        } else {

        }
    }

    @FXML
    private void btnClearSearchAP(ActionEvent event) {
        loadForm();
        loadTable();
    }

    @FXML
    private void tblPOrderMC(MouseEvent event) {
        if (tblGrnote.getSelectionModel().getSelectedItem() != null) {
            fillForm(tblGrnote.getSelectionModel().getSelectedItem());
            btnPPreview.setDisable(false);
        }
    }

    private void fillForm(Grnote grnote) {

        oldGrnote = GrnoteDao.getById(grnote.getId());
        this.grnote = GrnoteDao.getById(grnote.getId());
        disableButtons(true, true, true, true);
        if (this.grnote.getGrnotestatusId().equals(GrnoteStatusDao.getById(1))) {
            disableButtons(false, true, false, false);
        }
        tblGrnotepoitems.setItems(FXCollections.observableArrayList(this.grnote.getGrnotepoitemList()));
        lblTotal.setText(grnote.getNetamount().toString());
        lblDate.setText(grnote.getDatetime().toString());
        lblPorder.setText(grnote.getPorderId().getNo());
        lblSupplier.setText(grnote.getPorderId().getSupplierId().getCompany().toString());
        lblStatus.setText(grnote.getGrnotestatusId().getName().toString());
    }

    @FXML
    private void tblPOrderKR(KeyEvent event) {
        tblPOrderMC(null);
    }

    @FXML
    private void tblGrnotepoitemsMC(MouseEvent event) {
    }

    @FXML
    private void btnApproveAP(ActionEvent event) {
        String grnoteitems = "";
        for (Grnotepoitem grnotepoitem : tblGrnotepoitems.getItems()) {
            grnoteitems = grnoteitems + "Item :- " + grnotepoitem.getPoitemId().getItemId().getName() + "       Quantity :- " + grnotepoitem.getQtyrecieved() + "\n";
        }
        String confermation = "Ara you sure you need to Approve this Grnote with following details\n "
                + "\nDate :         \t\t" + grnote.getDatetime()
                + "\nSupplier :  \t\t" + grnote.getPorderId().getSupplierId().getCompany()
                + "\n\n---------------------------\n "
                + "Items : \n\n" + grnoteitems
                + "\n----------------------------"
                + "\nTotal :  \t\t" + grnote.getGrossamount().toString();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Approve GRN with following Details");
        alert.setContentText(confermation);

        ButtonType ok = new ButtonType("APPROVE");
        ButtonType no = new ButtonType("DON'T");

        alert.getButtonTypes().setAll(ok, no);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ok) {

            if (grnote != null && grnote.getGrnotestatusId().equals(GrnoteStatusDao.getById(1))) {
                grnote.setGrnotestatusId(GrnoteStatusDao.getById(2));
                GrnoteDao.update(grnote);
                boolean statuschange = true;
                for (Grnotepoitem grnotepoitem : grnote.getGrnotepoitemList()) {
                    grnotepoitem.getPoitemId().setQtyrecieved(grnotepoitem.getPoitemId().getQtyrecieved() + (grnotepoitem.getQtyrecieved()));
                    PoitemDao.update(grnotepoitem.getPoitemId());
                    grnotepoitem.getPoitemId().getItemId().setPprice(grnotepoitem.getPprice());
                    grnotepoitem.getPoitemId().getItemId().setSprice(grnotepoitem.getSprice());
                    grnotepoitem.getPoitemId().getItemId().setQty(Integer.toString(grnotepoitem.getPoitemId().getItemId().getQty() + grnotepoitem.getQtyrecieved()));
                    ItemDao.update(grnotepoitem.getPoitemId().getItemId());

                    if (grnotepoitem.getPoitemId().getQtyrequested() != grnotepoitem.getPoitemId().getQtyrecieved()) {
                        statuschange = false;

                    }
                    if (statuschange) {
                        grnote.getPorderId().setPostatusId(POStatusDao.getById(4));
                        POrderDao.update(grnote.getPorderId());
                    } else {
                        grnote.getPorderId().setPostatusId(POStatusDao.getById(3));
                        POrderDao.update(grnote.getPorderId());
                    }
                }
            }
        } else if (result.get() == no) {
        }
        fillForm(grnote);
        loadTable();
    }

    @FXML
    private void btnPPreviewAP(ActionEvent event) {
        Integer id = tblGrnote.getSelectionModel().getSelectedItem().getId();
        HashMap hmap = new HashMap();
        hmap.put("grnID", id);

        new ReportView("/report/GRNPrint.jasper", hmap);
    }

    @FXML
    private void btnUpdateAP(ActionEvent event) {
        Grnote grnote = (Grnote) rb.getObject("gr");
        grnote.setId(tblGrnote.getSelectionModel().getSelectedItem().getId());

        Alert alert = (Alert) rb.getObject("ui");
        alert.hide();
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("GRN Delete");
        alert.setContentText("Delete GRN " + grnote.getNo());

        ButtonType ok = new ButtonType("DELETE");
        ButtonType no = new ButtonType("DON'T");

        alert.getButtonTypes().setAll(ok, no);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ok) {
            GrnoteDao.delete(grnote);

            //----------- Notification Success -------------
            TrayNotification tray = new TrayNotification();
            Image rightImg = new Image("/image/NotifyIcon/Right.png");
            tray.setTitle("SUCCESS");
            tray.setMessage("Porder " + grnote.getDatetime() + " " + grnote.getPorderId().getSupplierId().getCompany() + " deleted.");
            tray.setRectangleFill(Paint.valueOf("#00b84c"));
            tray.showAndWait();
            tray.setImage(rightImg);
            tray.showAndDismiss(Duration.seconds(4));

            loadForm();
            fillTable(GrnoteDao.getAll(), page);
            tblGrnote.getSelectionModel().select(row);
        } else if (result.get() == no) {
        }
    }

}
