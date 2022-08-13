package ui;

import Common.MyResourceBundle;
import dao.InvoiceDao;
import entity.Invoice;
import entity.Invoiceitem;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import report.ReportView;

public class OutletReturnController implements Initializable {

    @FXML
    private Button btnSearchClear;
    @FXML
    private TableView<Invoiceitem> tblCart;
    @FXML
    private TableColumn<Invoiceitem, String> colCartBrand;
    @FXML
    private TableColumn<Invoiceitem, String> colCartItem;
    @FXML
    private TableColumn<Invoiceitem, String> colCartQty;
    @FXML
    private TableColumn<Invoiceitem, String> colCartPrice;
    @FXML
    private TableColumn<Invoiceitem, Button> colCartDelete;
    @FXML
    private Label lblCartGAmount;
    @FXML
    private Label lblCartDiscount;
    @FXML
    private Label lblCartTotal;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnPrint;
    @FXML
    private TextField txtCustomer;
    @FXML
    private TextField txtContactNo;
    @FXML
    private Label lblInvoiceNo;
    @FXML
    private Label lblDate;
    @FXML
    private TextField txtSearchInvoiceNo;
    @FXML
    private TextField txtSearchContactNo;
    @FXML
    private TableView<Invoice> tblSearchInvoice;
    @FXML
    private TableColumn<Invoice, String> colSearchInvoiceNo;
    @FXML
    private TableColumn<Invoice, Date> colSearchDate;
    @FXML
    private TableColumn<Invoice, String> colSearchCustomer;
    @FXML
    private TableColumn<Invoice, Integer> colSearchContact;
    @FXML
    private Button btnHome;
    @FXML
    private Pagination pagination;
    @FXML
    private Button btnSearchLock;
    @FXML
    private TextField txtSearchCusName;

// <editor-fold defaultstate="collapsed" desc=" Global-Data ">    
    //Color Indication of Input Controls 
    private String valid = Style.valid;
    private String invalid = Style.invalid;
    private String updated = Style.updated;
    private String initial = Style.initial;

    //Binding with the Form, Table
    private Invoice invoice;
    private ObservableList<Invoice> invoices;

    //Update Identification 
    private Invoice oldInvoice;

    //Table Row, Page Selection 
    private int page;
    private int row;

    ResourceBundle rb;

    private boolean lock;
// </editor-fold>
    @FXML
    private Button btnDelete;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        loadForm();
        loadTable();
    }

    private void loadForm() {
        invoice = new Invoice();
        oldInvoice = null;
        List<Invoiceitem> invoiceitems = new ArrayList();
        invoice.setInvoiceitemList(invoiceitems);
        btnUpdate.setDisable(true);
        btnPrint.setDisable(true);
        tblCart.getItems().clear();
        loadInvoiceItemFromTable();
    }

    private void loadTable() {
        lock = false;
        btnSearchLock.setText("Searck Lock");

        colSearchInvoiceNo.setCellValueFactory(new PropertyValueFactory("no"));
        colSearchDate.setCellValueFactory(new PropertyValueFactory("datetime"));
        colSearchCustomer.setCellValueFactory(new PropertyValueFactory("customer"));
        colSearchContact.setCellValueFactory(new PropertyValueFactory("contactno"));

        txtSearchInvoiceNo.setText("");
        txtSearchCusName.setText("");
        txtSearchContactNo.setText("");

        fillTable(InvoiceDao.getAllByReverse());
        pagination.setCurrentPageIndex(0);
    }

    private void loadInvoiceItemFromTable() {
        colCartBrand.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Invoiceitem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Invoiceitem, String> i) {
                return new SimpleStringProperty(i.getValue().getItemId().getBrandId().getName());
            }
        });
        colCartItem.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Invoiceitem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Invoiceitem, String> i) {
                return new SimpleStringProperty(i.getValue().getItemId().getName());
            }
        });
        colCartQty.setCellValueFactory(new PropertyValueFactory("qty"));

        colCartPrice.setCellValueFactory(new PropertyValueFactory("total"));

        colCartDelete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Invoiceitem, Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Invoiceitem, Button> row) {
                Button btnImageDelete = new Button("Remove");

                btnImageDelete.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        invoice.getInvoiceitemList().remove(row.getValue());
                        tblCart.setItems(FXCollections.observableArrayList(invoice.getInvoiceitemList()));
                    }
                });
                return new SimpleObjectProperty(btnImageDelete);
            }
        });
    }

    private void fillTable(ObservableList<Invoice> invoices) {
        if (invoices != null && invoices.size() != 0) {
            int rowsCount = 10;
            int pageCount = ((invoices.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? invoices.size() : pageIndex * rowsCount + rowsCount;
                    tblSearchInvoice.getItems().clear();
                    tblSearchInvoice.setItems(FXCollections.observableArrayList(invoices.subList(start, end)));
                    return tblSearchInvoice;
                }
            });

        } else {
            pagination.setPageCount(1);
            tblSearchInvoice.getItems().clear();
        }
    }

    @FXML
    private void txtSearchInvoiceNoKR(KeyEvent event) {
        if (!lock) {
            txtSearchCusName.setText("");
            txtSearchContactNo.setText("");
        }
        updateTable();
    }

    @FXML
    private void txtSearchCusNameKR(KeyEvent event) {
        if (!lock) {
            txtSearchInvoiceNo.setText("");
            txtSearchContactNo.setText("");
        }
        updateTable();
    }

    @FXML
    private void txtSearchContactNoKR(KeyEvent event) {
        if (!lock) {
            txtSearchInvoiceNo.setText("");
            txtSearchCusName.setText("");
        }
        updateTable();
    }

    @FXML
    private void tblSearchInvoiceMC(MouseEvent event) {
        if (tblSearchInvoice.getSelectionModel().getSelectedItem() != null) {
            invoice.setId(tblSearchInvoice.getSelectionModel().getSelectedItem().getId());
            tblCart.getItems().clear();
            tblCart.setItems(FXCollections.observableArrayList(tblSearchInvoice.getSelectionModel().getSelectedItem().getInvoiceitemList()));
            tblCart.getSelectionModel().clearSelection();
            
            txtCustomer.setText(tblSearchInvoice.getSelectionModel().getSelectedItem().getCustomer());
            txtContactNo.setText(tblSearchInvoice.getSelectionModel().getSelectedItem().getContactno());
            lblInvoiceNo.setText(tblSearchInvoice.getSelectionModel().getSelectedItem().getNo());
            lblDate.setText(tblSearchInvoice.getSelectionModel().getSelectedItem().getDatetime().toString());
            
            lblCartGAmount.setText(tblSearchInvoice.getSelectionModel().getSelectedItem().getGrossamount().toString());
            lblCartDiscount.setText(tblSearchInvoice.getSelectionModel().getSelectedItem().getTotaldiscont().toString());
            lblCartTotal.setText(tblSearchInvoice.getSelectionModel().getSelectedItem().getNetamount().toString());
            
            btnPrint.setDisable(false);
        }
    }

    @FXML
    private void btnSearchClearAP(ActionEvent event) {
    }

    @FXML
    private void tblCartMC(MouseEvent event) {
        if (tblCart.getSelectionModel().getSelectedItem() != null) {
            Invoiceitem invoiceitem = tblCart.getSelectionModel().getSelectedItem();

            try {
                Alert SubUI = new Alert(null, null);
                MyResourceBundle myResources = new MyResourceBundle();
                HashMap hm = new HashMap();
                hm.put("controller", this);
                hm.put("item", invoiceitem);
                hm.put("ui", SubUI);
                hm.put("addStatus", false);
                hm.put("tblCart", tblCart);
                hm.put("Invoice", invoice);
                myResources.setHashMap(hm);
                AnchorPane itemReturnUI = FXMLLoader.load(Main.class.getResource("OutletReturnItemSub.fxml"), myResources);
                SubUI.getDialogPane().setContent(itemReturnUI);
                SubUI.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                SubUI.show();
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void btnUpdateAP(ActionEvent event) {
    }

    @FXML
    private void btnPrintAP(ActionEvent event) {
                
        Invoice inv = invoice;
        HashMap hmap = new HashMap();
        hmap.put("abc", inv.getId());

        new ReportView("/report/InvoiceSlip.jasper", hmap);
    }

    @FXML
    private void txtCustomerKR(KeyEvent event) {

    }

    @FXML
    private void txtContactNoKR(KeyEvent event) {

    }

    @FXML
    private void btnSearchLockAP(ActionEvent event) {
        if (lock) {
            btnSearchLock.setText("Search Lock");
            lock = false;
            fillTable(InvoiceDao.getAllByReverse());
        } else {
            btnSearchLock.setText("Unlock");
            lock = true;
            updateTable();
        }
    }

    private void updateTable() {
        String no = txtSearchInvoiceNo.getText().trim();
        boolean sno = !no.isEmpty();
        String contactno = txtSearchContactNo.getText().trim();
        boolean scontactno = !contactno.isEmpty();
        String customer = txtSearchCusName.getText().trim();
        boolean scustomer = !customer.isEmpty();

        if (!sno && !scontactno && !scustomer) {
            fillTable(InvoiceDao.getAllByReverse());
        }
        if (sno && !scontactno && !scustomer) {
            fillTable(InvoiceDao.getAllByNo(no));
        }
        if (!sno && !scontactno && scustomer) {
            fillTable(InvoiceDao.getAllByName(customer));
        }
        if (!sno && scontactno && !scustomer) {
            fillTable(InvoiceDao.getAllByContact(contactno));
        }
        if (sno && scontactno && !scustomer) {
            fillTable(InvoiceDao.getAllByNoContact(no, contactno));
        }
        if (sno && !scontactno && scustomer) {
            fillTable(InvoiceDao.getAllByNoName(no, customer));
        }
        if (!sno && scontactno && scustomer) {
            fillTable(InvoiceDao.getAllByContactName(contactno, customer));
        }
        if (sno && scontactno && scustomer) {
            fillTable(InvoiceDao.getAllByNoContactName(no, contactno, customer));
        }
    }

    @FXML
    private void btnHomeAP() throws IOException {
        System.gc();
        Main.showHome();
    }

    @FXML
    private void btnDelete(ActionEvent event) {
    }

}
