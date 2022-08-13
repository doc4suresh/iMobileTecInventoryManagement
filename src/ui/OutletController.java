package ui;

import dao.BrandDao;
import dao.DaoException;
import dao.InvoiceDao;
import dao.ItemDao;
import dao.ItemtypeDao;
import dao.WarrentyDao;
import entity.Brand;
import entity.Invoice;
import entity.Invoiceitem;
import entity.Item;
import entity.Itemtype;
import entity.Warrenty;
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
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import report.ReportView;
import static ui.LoginController.privilage;

public class OutletController implements Initializable {

    @FXML
    private ComboBox<Itemtype> cmbItemType;
    @FXML
    private ComboBox<Brand> cmbBrand;
    @FXML
    private Button btnSearchClear;
    @FXML
    private TextField txtIMEI;
    @FXML
    private TextField txtBuyQty;
    @FXML
    private TextField txtItemDiscounPercentage;
    @FXML
    private TextField txtItemDiscountValue;
    @FXML
    private Label lblItemPrice;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Item> tblItem;
    @FXML
    private TableColumn<Item, String> colItemName;
    @FXML
    private TableColumn<Item, BigDecimal> colItemSPrice;
    @FXML
    private TableColumn<Item, BigDecimal> colItemPPrice;
    @FXML
    private TableColumn<Item, String> colItemStock;
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
    private Button btnAddToBill;
    @FXML
    private Button btnItemUpdate;
    @FXML
    private Button btnItemClear;
    @FXML
    private Button btnFinish;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnHome;
    @FXML
    private ComboBox<Warrenty> cmbWarranty;
    @FXML
    private Label lblInvoiceNo;
    @FXML
    private Label lblDate;
    @FXML
    private TextField txtCustomer;
    @FXML
    private TextField txtContactNo;
    @FXML
    private Button btnEditInvoice;

    //<editor-fold defaultstate="collapsed" desc="Module-Data">
    //Color Indication of Input Controls
    private String valid = Style.valid;
    private String invalid = Style.invalid;
    private String updated = Style.updated;
    private String initial = Style.initial;

    //Binding with the Form
    private Item item;
    private Invoiceitem invoiceitem;
    private Invoice invoice;

    //Update Identification 
    private Invoiceitem oldInvoiceitem;
    private Invoice oldInvoice;

    //Update Identification
    private Item oldItem;

    //Table Row, Page Selected
    private int page;
    private int row;
//</editor-fold> 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadForm();
        clearCart();
        btnFinish.setDisable(true);
    }

    private void loadForm() {
        this.invoice = new Invoice();
        oldInvoice = null;
        List<Invoiceitem> invoiceItems = new ArrayList();
        invoice.setInvoiceitemList(invoiceItems);

        lblInvoiceNo.setText(InvoiceDao.getNextInvoiceNo());
        invoice.setNo(lblInvoiceNo.getText());
        lblDate.setText(LocalDate.now().toString());
        invoice.setDatetime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        invoice.setUserEmployeeId(LoginController.user);

        cmbItemType.setItems(ItemtypeDao.getAll());
        cmbItemType.getSelectionModel().select(-1);
        cmbBrand.setItems(BrandDao.getAll());
        cmbBrand.getSelectionModel().select(-1);
        cmbWarranty.setItems(WarrentyDao.getAll());
        cmbWarranty.getSelectionModel().select(-1);

        colItemName.setCellValueFactory(new PropertyValueFactory("name"));
        colItemSPrice.setCellValueFactory(new PropertyValueFactory("sprice"));
        colItemPPrice.setCellValueFactory(new PropertyValueFactory("pprice"));
        colItemStock.setCellValueFactory(new PropertyValueFactory("qty"));

        fillTable(ItemDao.getAll());
        pagination.setCurrentPageIndex(0);

        initialLoadForm();
        loadInvoiceitemFormTable();
    }

    private void clearCart() {
        tblCart.getItems().clear();
        txtCustomer.setStyle(initial);
        txtCustomer.setText("Customer");
        txtContactNo.setStyle(initial);
        txtContactNo.setText("");
        lblCartGAmount.setText("00.00");
        lblCartDiscount.setText("00.00");
        lblCartTotal.setText("00.00");
        btnFinish.setDisable(true);
    }

    private void initialLoadForm() {
        txtBuyQty.setText("");
        txtBuyQty.setStyle(initial);
        txtBuyQty.setDisable(true);
        txtItemDiscounPercentage.setText("");
        txtItemDiscounPercentage.setStyle(initial);
        txtItemDiscounPercentage.setDisable(true);
        txtItemDiscountValue.setText("");
        txtItemDiscountValue.setStyle(initial);
        txtItemDiscountValue.setDisable(true);
        txtIMEI.setText("");
        txtIMEI.setStyle(initial);
        txtIMEI.setDisable(true);
        cmbWarranty.setStyle(initial);
        cmbWarranty.setDisable(true);
        txtCustomer.setStyle(initial);
        txtContactNo.setStyle(initial);

        lblItemPrice.setText("00.00");
        btnAddToBill.setDisable(true);
        btnItemUpdate.setDisable(true);
    }

    private void updateItemtable() {
        Brand brand = cmbBrand.getSelectionModel().getSelectedItem();
        boolean sbrand = cmbBrand.getSelectionModel().getSelectedIndex() != -1;
        Itemtype type = cmbItemType.getSelectionModel().getSelectedItem();
        boolean stype = cmbItemType.getSelectionModel().getSelectedIndex() != -1;

        if (!sbrand && !stype) {
            fillTable(ItemDao.getAll());
        }
        if (!sbrand && stype) {
            fillTable(ItemDao.getAllByType(type));
        }
        if (sbrand && !stype) {
            fillTable(ItemDao.getAllByBrand(brand));
        }
        if (sbrand && stype) {
            fillTable(ItemDao.getAllByTypeBrand(type, brand));
        }

    }

    private void fillTable(ObservableList<Item> items) {

        if (privilage.get("Invoice_select") && items != null && items.size() != 0) {

            int rowsCount = 8;
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

    private void loadInvoiceitemFormTable() {
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

    @FXML
    private void cmbItemTypeAP(ActionEvent event) {
        if (cmbItemType.getSelectionModel().getSelectedItem() != null) {
            updateItemtable();
        }
    }

    @FXML
    private void cmbBrandAP(ActionEvent event) {
        if (cmbBrand.getSelectionModel().getSelectedItem() != null) {
            updateItemtable();
        }
    }

    @FXML
    private void btnSearchClearAP() {
        loadForm();
    }

    @FXML
    private void txtIMEIKR(KeyEvent event) {
        String imeiRecieved = txtIMEI.getText().trim();
        if (imei(imeiRecieved)) {
            txtIMEI.setStyle(valid);

            txtItemDiscounPercentage.setDisable(false);
            txtItemDiscounPercentage.setText("0");
            txtItemDiscountValue.setDisable(false);
            txtItemDiscountValue.setText("00");

            claculateItemPrice();
            cmbWarranty.setDisable(false);

        } else {
            txtIMEI.setStyle(invalid);
            cmbWarranty.setDisable(true);
            txtItemDiscounPercentage.setText("");
            txtItemDiscountValue.setText("");
            lblItemPrice.setText("00.00");

            txtItemDiscounPercentage.setDisable(true);
            txtItemDiscountValue.setDisable(true);

        }
    }

    public static boolean imei(String imei) {
        return imei.matches("\\d{14}");
    }

    @FXML
    private void txtBuyQtyKR(KeyEvent event) {
        String recieved = txtBuyQty.getText().trim();
        if (qty(recieved) && tblItem.getSelectionModel().getSelectedItem().getQty() >= Integer.parseInt(txtBuyQty.getText())) {
            txtBuyQty.setStyle(valid);

            txtItemDiscounPercentage.setDisable(false);
            txtItemDiscounPercentage.setText("0");
            txtItemDiscountValue.setDisable(false);
            txtItemDiscountValue.setText("00");

            claculateItemPrice();
            cmbWarranty.setDisable(false);

        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Invalid Quantity");
            alert.setHeaderText("Please Cheak the Stock");
            alert.setContentText("Not that much of \"" + tblItem.getSelectionModel().getSelectedItem().getName() + "\" in stock. Please check the Item Quantity!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                txtBuyQty.setStyle(invalid);
                txtBuyQty.setText("");
                cmbWarranty.setDisable(true);
                txtItemDiscounPercentage.setText("");
                txtItemDiscountValue.setText("");
                lblItemPrice.setText("00.00");

                txtItemDiscounPercentage.setDisable(true);
                txtItemDiscountValue.setDisable(true);

            } else {
                btnSearchClearAP();
            }
        }
    }

    public static boolean qty(String qty) {
        return qty.matches("\\d{0,3}");
    }

    @FXML
    private void txtItemDiscounPercentageKR(KeyEvent event) {
        String recieve = txtItemDiscounPercentage.getText().trim();
        if (per(recieve) && isBigDecimal(recieve)) {
            txtItemDiscounPercentage.setStyle(valid);

            String itemSPrice = tblItem.getSelectionModel().getSelectedItem().getSprice().toString();
            float itemPrice = Float.valueOf(itemSPrice);
            float itemQty = Float.valueOf(txtBuyQty.getText());
            float qtyPrice = itemPrice * itemQty;
            float totalDiscountPercentage = Float.valueOf(txtItemDiscounPercentage.getText());
            float value = qtyPrice / 100 * totalDiscountPercentage;
            String totalDiscountValue = String.format("%.2f", value);
            txtItemDiscountValue.setText(totalDiscountValue);
            txtItemDiscountValue.setStyle(valid);

            claculateItemPrice();

        } else {
            txtItemDiscounPercentage.setStyle(invalid);
            txtItemDiscountValue.setText("");
            lblItemPrice.setText("00.00");
        }

    }

    public static boolean per(String per) {
        return per.matches("\\d{1,2}(\\.\\d{1,2})?");
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

    private void claculateItemPrice() {
        String itemSPrice = tblItem.getSelectionModel().getSelectedItem().getSprice().toString();

        float itemPrice = Float.valueOf(itemSPrice);
        float entQty = Float.valueOf(txtBuyQty.getText());
        float totalDiscount = Float.valueOf(txtItemDiscountValue.getText());

        float value = (itemPrice * entQty) - totalDiscount;

        String totalValue = String.format("%.2f", value);
        lblItemPrice.setText(totalValue);
    }

    @FXML
    private void txtItemDiscountValueKR(KeyEvent event) {
        String recieve = txtItemDiscountValue.getText().trim();
        if (disValue(recieve) && isBigDecimal(recieve)) {
            txtItemDiscountValue.setStyle(valid);

            String itemSPrice = tblItem.getSelectionModel().getSelectedItem().getSprice().toString();
            float itemPrice = Float.valueOf(itemSPrice);
            float itemQty = Float.valueOf(txtBuyQty.getText());
            float qtyPrice = itemPrice * itemQty;
            float totalDiscountvalue = Float.valueOf(txtItemDiscountValue.getText());
            float value = totalDiscountvalue / qtyPrice * 100;
            String totalDiscountPer = String.format("%.2f", value);
            txtItemDiscounPercentage.setText(totalDiscountPer);
            txtItemDiscounPercentage.setStyle(valid);

            claculateItemPrice();

        } else {
            txtItemDiscountValue.setStyle(invalid);
            txtItemDiscounPercentage.setText("");
            lblItemPrice.setText("00.00");
        }
    }

    public static boolean disValue(String disvalue) {
        return disvalue.matches("\\d{1,5}(\\.\\d{1,2})?");
    }

    @FXML
    private void tblItemMC(MouseEvent event) {
        if (tblItem.getSelectionModel().getSelectedItem() != null) {

            int itemcode = tblItem.getSelectionModel().getSelectedItem().getItemtypeId().getId();
            if (itemcode == 1) {
                txtIMEI.setDisable(false);
                txtBuyQty.setText("1");
                txtBuyQty.setDisable(true);

            } else {
                txtBuyQty.setDisable(false);
                txtBuyQty.setText("");
                txtIMEI.setDisable(true);
                txtIMEI.setText("");
            }
        }
    }

    @FXML
    private void tblCartMC(MouseEvent event) {
        if (tblCart.getSelectionModel().getSelectedItem() != null) {
            btnItemUpdate.setDisable(false);
            btnAddToBill.setDisable(true);
            tblItem.setDisable(true);
            txtBuyQty.setDisable(false);
            txtBuyQty.setText(tblCart.getSelectionModel().getSelectedItem().getQty().toString());
            txtIMEI.setDisable(false);
            txtIMEI.setText(tblCart.getSelectionModel().getSelectedItem().getImei());

            tblItem.setItems(FXCollections.observableArrayList(ItemDao.getAllByItem(tblCart.getSelectionModel().getSelectedItem().getItemId())));
        }
    }

    @FXML
    private void btnAddToBillAP(ActionEvent event) {

        if (tblItem.getSelectionModel().getSelectedItem().getQty() >= Integer.parseInt(txtBuyQty.getText())) {
            Invoiceitem invoiceitem = new Invoiceitem();
            invoiceitem.setItemId(tblItem.getSelectionModel().getSelectedItem());
            invoiceitem.setImei(txtIMEI.getText());
            invoiceitem.setQty(Integer.parseInt(txtBuyQty.getText()));
            invoiceitem.setWarrentyId(cmbWarranty.getSelectionModel().getSelectedItem());
            double linetotal = invoiceitem.getItemId().getSprice().doubleValue() * invoiceitem.getQty().doubleValue();
            invoiceitem.setTotal(BigDecimal.valueOf(linetotal));
            float discount = Float.valueOf(txtItemDiscountValue.getText());
            invoiceitem.setDiscountvalue(BigDecimal.valueOf(discount));
            float disPrecent = Float.valueOf(txtItemDiscounPercentage.getText());
            invoiceitem.setDiscountper(BigDecimal.valueOf(disPrecent));
            invoiceitem.setInvoiceId(invoice);
            invoice.getInvoiceitemList().add(invoiceitem);
            tblCart.setItems(FXCollections.observableArrayList(invoice.getInvoiceitemList()));

            calculateTotal();
            initialLoadForm();

            btnFinish.setDisable(false);
        }
    }

    private void calculateTotal() {
        //Calculate Total Bill
        double total = 0.0;
        for (Invoiceitem invoiceitem : invoice.getInvoiceitemList()) {
            if (invoiceitem.getQty() != 0) {
                total = total + invoiceitem.getTotal().doubleValue();
            }
        }
        lblCartTotal.setText(String.valueOf(total));
        invoice.setGrossamount(BigDecimal.valueOf(total));
        lblCartGAmount.setText(String.valueOf(total));

        //Calculate Total Discount Amount
        float discount = 0;
        BigDecimal zero = new BigDecimal("00");
        for (Invoiceitem invoiceitem : invoice.getInvoiceitemList()) {
            if (invoiceitem.getDiscountvalue() != zero) {
                discount = discount + invoiceitem.getDiscountvalue().floatValue();
                double billtotal = total - discount;
                lblCartDiscount.setText(String.valueOf(discount));
                lblCartTotal.setText(String.valueOf(billtotal));
            }
        }

    }

    @FXML
    private void btnItemUpdateAP(ActionEvent event) {
        if (tblItem.getSelectionModel().getSelectedItem() != null && qty(txtBuyQty.getText())) {
            if (tblItem.getSelectionModel().getSelectedItem().getQty() >= Integer.parseInt(txtBuyQty.getText())) {
                Invoiceitem invoiceitem = invoice.getInvoiceitemList().get(tblCart.getSelectionModel().getSelectedIndex());
                invoiceitem.setQty(Integer.parseInt(txtBuyQty.getText()));
                double lineTotal = invoiceitem.getItemId().getSprice().doubleValue() * invoiceitem.getQty().doubleValue();
                invoiceitem.setTotal(BigDecimal.valueOf(lineTotal));

                colCartQty.setVisible(false);
                colCartQty.setVisible(true);
                calculateTotal();

                btnAddToBill.setDisable(false);
                loadForm();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText("Item Add");
                alert.setContentText(tblItem.getSelectionModel().getSelectedItem().getName() + " quantity is grater than to it's stock quantity");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("Item Add");
            alert.setContentText(tblItem.getSelectionModel().getSelectedItem().getName() + " Quantity is invalid");
            alert.showAndWait();
        }
    }

    @FXML
    private void btnItemClearAP(ActionEvent event) {
        loadForm();
    }

    @FXML
    private void btnFinishAP(ActionEvent event) throws DaoException {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Complete the Bill");
        alert.setHeaderText("Customer Name \t: " + txtCustomer.getText() + "\nBill Total \t\t\t: " + lblCartTotal.getText());
        alert.setContentText("You can complete the bill with print or without print.\n.");

        ButtonType btnPrint = new ButtonType("Print & Complete");
        ButtonType btnNotPrint = new ButtonType("Complete without Print");
        ButtonType btnCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnPrint, btnNotPrint, btnCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnPrint) {
            // ... Print the bill ...------------------------------------------------------------------

            completeBill();
            Integer size = InvoiceDao.getAll().size();

            Invoice inv = (Invoice) InvoiceDao.getAll().get(size - 1);
            HashMap hmap = new HashMap();
            hmap.put("abc", inv.getId());

            new ReportView("/report/InvoiceSlip.jasper", hmap);

        } else if (result.get() == btnNotPrint) {
            //... Bill not print-----------------------------------------------------------------------
            completeBill();

        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    private void completeBill() throws DaoException {
        invoice.setGrossamount(new BigDecimal(lblCartGAmount.getText()));
        invoice.setTotaldiscont(new BigDecimal(lblCartDiscount.getText()));
        invoice.setNetamount(new BigDecimal(lblCartTotal.getText()));
        invoice.setCustomer(txtCustomer.getText());
        invoice.setContactno(txtContactNo.getText());

        invoice.setInvoiceitemList(tblCart.getItems());
        String errors = getErrors();

        if (errors.isEmpty()) {
            InvoiceDao.add(invoice);
            updateStock();

            loadForm();
            clearCart();

        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("Invoice Add");
            alert.setContentText(getErrors());
            alert.showAndWait();
        }

    }

    private void updateStock() {

        for (Invoiceitem invoiceitems : invoice.getInvoiceitemList()) {
            int reduce = invoiceitems.getItemId().getQty() - (invoiceitems.getQty());
            invoiceitems.getItemId().setQty(Integer.toString(reduce));
            ItemDao.update(invoiceitems.getItemId());
        }

    }

    public String getErrors() {
        String errors = "";

        if (invoice.getInvoiceitemList() == null || invoice.getInvoiceitemList().size() == 0) {
            errors = errors + "Items.         \t\tNot Invoiced\n";
        }
        if (invoice.getGrossamount() != null) {
            if (invoice.getTotaldiscont() == null) {
                errors = errors + "Total Discount \t\tis Invalid\n";
            }
            if (invoice.getNetamount() == null) {
                errors = errors + "Net Amount     \t\tis Invalid\n";
            }
        } else {
            errors = errors + "Gross Amount   \t\tis Invalid\n";
        }
        return errors;
    }

    @FXML
    private void btnCancelAP(ActionEvent event) {
        clearCart();
        invoice.getInvoiceitemList().clear();

    }

    @FXML
    private void cmbWarrantyAP(ActionEvent event) {

        if (oldInvoiceitem != null && !invoiceitem.getWarrentyId().equals(oldInvoiceitem.getWarrentyId())) {
            cmbWarranty.setStyle(updated);
            btnAddToBill.setDisable(false);
        } else {
            cmbWarranty.setStyle(valid);
            btnAddToBill.setDisable(false);
        }
    }

    @FXML
    private void txtCustomerKR(KeyEvent event) {
        if (invoice.setCustomer(txtCustomer.getText().trim())) {
            if (oldInvoice != null && !invoice.getCustomer().equals(oldInvoice.getCustomer())) {
                txtCustomer.setStyle(updated);
            } else {
                txtCustomer.setStyle(valid);
            }
        } else {
            txtCustomer.setStyle(invalid);
        }
    }

    @FXML
    private void txtContactNoKR(KeyEvent event) {
        if (invoice.setContactno(txtContactNo.getText().trim())) {
            if (oldInvoice != null && !invoice.getContactno().equals(oldInvoice.getContactno())) {
                txtContactNo.setStyle(updated);
            } else {
                txtContactNo.setStyle(valid);
            }
        } else {
            txtContactNo.setStyle(invalid);
        }
    }
    
    @FXML
    private void btnEditInvoiceAP() throws IOException {
        Main.showOutletReturn();
    }

    @FXML
    private void btnHomeAP() throws IOException {
        System.gc();
        Main.showHome();
    }
}
