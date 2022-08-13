
package ui;

import dao.DaoException;
import dao.SupplierDao;
import entity.Supplier;
import java.io.IOException;
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
import tray.notification.TrayNotification;
import static ui.LoginController.privilage;

public class SupplierController implements Initializable {

    private Main main;

    //<editor-fold defaultstate="collapsed" desc="Module-Data">
    //Color Indication of Input Controls
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    //Binding with the Form
    private Supplier supplier;

    //Update Identification
    private Supplier oldSupplier;

    //Table Row, Page Selected
    private int page;
    private int row;

    //Search Lock
    private boolean lock;

//</editor-fold>  
    @FXML
    private TextField txtCompany;
    @FXML
    private TextField txtContactPerson;
    @FXML
    private TextArea txtAddress;
    @FXML
    private TextField txtLand;
    @FXML
    private TextField txtMobile;
    @FXML
    private TextField txtEmail;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnSearchClear;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Supplier> tblSupplier;
    @FXML
    private TableColumn<Supplier, String> colCompany;
    @FXML
    private TableColumn<Supplier, String> colContactPerson;
    @FXML
    private TableColumn<Supplier, String> colLand;
    @FXML
    private TableColumn<Supplier, String> colMobile;
    @FXML
    private TextField txtSearchCompany;
    @FXML
    private TextField txtSearchContactPerson;

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

        supplier = new Supplier();
        oldSupplier = null;

        txtCompany.setText("");
        txtContactPerson.setText("");
        txtAddress.setText("");
        txtMobile.setText("");
        txtLand.setText("");
        txtEmail.setText("");

        dissableButtons(false, false, true, true);
        setStyle(initial);
    }

    private void loadTable() {
        lock = false;

        txtSearchCompany.setText("");
        txtSearchContactPerson.setText("");

        colCompany.setCellValueFactory(new PropertyValueFactory("company"));
        colContactPerson.setCellValueFactory(new PropertyValueFactory("contactperson"));
        colLand.setCellValueFactory(new PropertyValueFactory("landphone"));
        colMobile.setCellValueFactory(new PropertyValueFactory("mobilephone"));

        fillTable(SupplierDao.getAll());
        pagination.setCurrentPageIndex(0);
    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Supplier_insert"));
        btnUpdate.setDisable(update || !privilage.get("Supplier_update"));
        btnDelete.setDisable(delete || !privilage.get("Supplier_delete"));

        txtSearchCompany.setDisable(select || !privilage.get("Supplier_select"));
        txtSearchContactPerson.setDisable(select || !privilage.get("Supplier_select"));
        btnSearchClear.setDisable(select || !privilage.get("Supplier_select"));

    }

    private void setStyle(String style) {

        txtCompany.setStyle(style);
        txtContactPerson.setStyle(style);
        txtMobile.setStyle(style);
        txtLand.setStyle(style);
        txtEmail.setStyle(style);

        if (!txtAddress.getChildrenUnmodifiable().isEmpty()) {
            ((ScrollPane) txtAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(style);
        }
    }

    private void fillTable(ObservableList<Supplier> suppliers) {

        if (privilage.get("Supplier_select") && suppliers != null && suppliers.size() != 0) {

            int rowsCount = 9;
            int pageCount = ((suppliers.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? suppliers.size() : pageIndex * rowsCount + rowsCount;
                    tblSupplier.getItems().clear();
                    tblSupplier.setItems(FXCollections.observableArrayList(suppliers.subList(start, end)));
                    return tblSupplier;
                }
            });

        } else {

            pagination.setPageCount(1);
            tblSupplier.getItems().clear();
        }
    }

    @FXML
    private void txtCompanyKR(KeyEvent event) {
        if (supplier.setCompany(txtCompany.getText().trim())) {
            if (oldSupplier != null && !supplier.getCompany().equals(oldSupplier.getCompany())) {
                txtCompany.setStyle(updated);
            } else {
                txtCompany.setStyle(valid);
            }
        } else {
            txtCompany.setStyle(invalid);
        }
    }

    @FXML
    private void txtContactPersonKR(KeyEvent event) {
        if (supplier.setContactperson(txtContactPerson.getText().trim())) {
            if (oldSupplier != null && !supplier.getContactperson().equals(oldSupplier.getContactperson())) {
                txtContactPerson.setStyle(updated);
            } else {
                txtContactPerson.setStyle(valid);
            }
        } else {
            txtContactPerson.setStyle(invalid);
        }
    }

    @FXML
    private void txtAddressKR(KeyEvent event) {
        if (supplier.setAddress(txtAddress.getText().trim())) {
            if (oldSupplier != null && !supplier.getAddress().equals(oldSupplier.getAddress())) {
                ((ScrollPane) txtAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(updated);
            } else {
                ((ScrollPane) txtAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(valid);
            }
        } else {
            ((ScrollPane) txtAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(invalid);
        }
    }

    @FXML
    private void txtLandKR(KeyEvent event) {
        if (supplier.setLandphone(txtLand.getText())) {
            if (oldSupplier != null && oldSupplier.getLandphone() != null && supplier.getLandphone() != null && oldSupplier.getLandphone().equals(supplier.getLandphone())) {
                txtLand.setStyle(valid);
            } else if (oldSupplier != null && oldSupplier.getLandphone() != supplier.getLandphone()) {
                txtLand.setStyle(updated);
            } else {
                txtLand.setStyle(valid);
            }
        } else {
            txtLand.setStyle(invalid);
        }
    }

    @FXML
    private void txtMobileKR(KeyEvent event) {
        if (supplier.setMobilephone(txtMobile.getText().trim())) {
            if (oldSupplier != null && !supplier.getMobilephone().equals(oldSupplier.getMobilephone())) {
                txtMobile.setStyle(updated);
            } else {
                txtMobile.setStyle(valid);
            }
        } else {
            txtMobile.setStyle(invalid);
        }
    }

    @FXML
    private void txtEmailKR(KeyEvent event) {
        if (supplier.setEmail(txtEmail.getText().trim())) {
            if (oldSupplier != null && oldSupplier.getEmail() != null && supplier.getEmail() != null && oldSupplier.getEmail().equals(supplier.getEmail())) {
                txtEmail.setStyle(valid);
            } else if (oldSupplier != null && oldSupplier.getEmail() != supplier.getEmail()) {
                txtEmail.setStyle(updated);
            } else {
                txtEmail.setStyle(valid);
            }

        } else {
            txtEmail.setStyle(invalid);
        }
    }

    @FXML
    private void txtSearchCompanyKR(KeyEvent event) {
        txtSearchContactPerson.setText("");
        updateTable();
    }

    @FXML
    private void txtSearchContactPersonKR(KeyEvent event) {
        txtSearchCompany.setText("");
        updateTable();
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
        if (getUpdates().isEmpty() && getErrors().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supplier Delete");
            alert.setContentText("Delete Supplier " + supplier.getCompany());

            ButtonType ok = new ButtonType("DELETE");
            ButtonType no = new ButtonType("DON'T");

            alert.getButtonTypes().setAll(ok, no);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ok) {
                
                try {
                    SupplierDao.delete(supplier);

                //----------- Notification Success -------------
                TrayNotification tray = new TrayNotification();
                Image rightImg = new Image("/image/NotifyIcon/Right.png");
                tray.setTitle("SUCCESS");
                tray.setMessage("Supplier " + supplier.getCompany() + " deleted.");
                tray.setRectangleFill(Paint.valueOf("#00b84c"));
                tray.showAndWait();
                tray.setImage(rightImg);
                tray.showAndDismiss(Duration.seconds(4));

                loadForm();
                updateTable();
                pagination.setCurrentPageIndex(page);
                tblSupplier.getSelectionModel().select(row);
                
                } catch (Exception e) {
                    //----------- Notification Unsuccess-------------
                    TrayNotification tray = new TrayNotification();
                    Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                    tray.setTitle("UN-SUCCESS");
                    tray.setMessage("Supplier " + supplier.getCompany()+ " not Deleted. \n" + e.getClass());
                    tray.setRectangleFill(Paint.valueOf("#dc0000"));
                    tray.showAndWait();
                    tray.setImage(wrongImg);
                    tray.showAndDismiss(Duration.seconds(4));
                }

            } else if (result.get() == no) {
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Supplier Delete");
            alert.setHeaderText(supplier.getCompany() + " Delete ?");
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
                alert.setHeaderText("Supplier Update");
                alert.setContentText("Update Supplier " + supplier.getCompany());

                ButtonType ok = new ButtonType("UPDATE");
                ButtonType no = new ButtonType("DON'T");

                alert.getButtonTypes().setAll(ok, no);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ok) {
                    
                    try {
                        SupplierDao.update(supplier);

                    //----------- Notification Success -------------
                    TrayNotification tray = new TrayNotification();
                    Image rightImg = new Image("/image/NotifyIcon/Right.png");
                    tray.setTitle("SUCCESS");
                    tray.setMessage("Supplier " + supplier.getCompany() + " updated.");
                    tray.setRectangleFill(Paint.valueOf("#00b84c"));
                    tray.showAndWait();
                    tray.setImage(rightImg);
                    tray.showAndDismiss(Duration.seconds(4));

                    updateTable();
                    loadForm();
                    pagination.setCurrentPageIndex(page);
                    tblSupplier.getSelectionModel().select(row);
                    
                    } catch (Exception e) {
                        //----------- Notification Unsuccess-------------
                    TrayNotification tray = new TrayNotification();
                    Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                    tray.setTitle("UN-SUCCESS");
                    tray.setMessage("Supplier " + supplier.getCompany()+ " not Updated. \n" + e.getClass());
                    tray.setRectangleFill(Paint.valueOf("#dc0000"));
                    tray.showAndWait();
                    tray.setImage(wrongImg);
                    tray.showAndDismiss(Duration.seconds(4));
                    }

                    
                } else if (result.get() == no) {
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText("Supplier Update");
                alert.setContentText("Nothing Updated");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("Supplier Update");
            alert.setContentText("Supplier not updated!");
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
        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "Ara you sure you need to add this Supplier with following details\n "
                    + "\nCompany Name :     \t" + supplier.getCompany()
                    + "\nContact Person  :  \t\t" + supplier.getContactperson()
                    + "\nAddress :          \t\t" + supplier.getAddress()
                    + "\nLand :             \t\t\t" + supplier.getLandphone()
                    + "\nMobile No :        \t\t" + supplier.getMobilephone()
                    + "\nEmail :            \t\t\t" + supplier.getEmail();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Add Supplier with following Details");
            alert.setContentText(confermation);

            ButtonType ok = new ButtonType("ADD");
            ButtonType no = new ButtonType("DON'T");

            alert.getButtonTypes().setAll(ok, no);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ok) {

                try {
                    SupplierDao.add(supplier);

                    //----------- Notification Success -------------
                    TrayNotification tray = new TrayNotification();
                    Image rightImg = new Image("/image/NotifyIcon/Right.png");
                    tray.setTitle("SUCCESS");
                    tray.setMessage("Supplier " + supplier.getCompany() + " saved.");
                    tray.setRectangleFill(Paint.valueOf("#00b84c"));
                    tray.showAndWait();
                    tray.setImage(rightImg);
                    tray.showAndDismiss(Duration.seconds(4));

                    loadForm();
                    updateTable();
                    pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                    tblSupplier.getSelectionModel().select(tblSupplier.getItems().size() - 1);

                } catch (DaoException ex) {
                    //----------- Notification Unsuccess-------------
                    TrayNotification tray = new TrayNotification();
                    Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                    tray.setTitle("UN-SUCCESS");
                    tray.setMessage("Supplier " + supplier.getCompany() + " not saved. \n Try Again.");
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
            alert.setContentText("Supplier Detail Error");
            alert.showAndWait();
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
        }
    }

    @FXML
    private void tblSupplierKR(KeyEvent event) {
        fillForm();
    }

    @FXML
    private void tblSupplierMC(MouseEvent event) {
        fillForm();
    }

    private void updateTable() {

        String company = txtSearchCompany.getText().trim();
        boolean scompany = !company.isEmpty();
        String contactperson = txtSearchContactPerson.getText().trim();
        boolean scontactperson = !contactperson.isEmpty();

        if (!scompany && !scontactperson) {
            fillTable(SupplierDao.getAll());
        }
        if (scompany && !scontactperson) {
            fillTable(SupplierDao.getAllByCompany(company));
        }
        if (!scompany && scontactperson) {
            fillTable(SupplierDao.getAllByContactPerson(contactperson));
        }
        if (scompany && scontactperson) {
            fillTable(SupplierDao.getAllByCompanyContactPerson(company, contactperson));
        }
    }

    private String getUpdates() {

        String updates = "";

        if (oldSupplier != null) {

            if (supplier.getCompany() != null && !supplier.getCompany().equals(oldSupplier.getCompany())) {
                updates = updates + oldSupplier.getCompany() + " chnaged to " + supplier.getCompany() + "\n";
            }

            if (supplier.getContactperson() != null && !supplier.getContactperson().equals(oldSupplier.getContactperson())) {
                updates = updates + oldSupplier.getContactperson() + " chnaged to " + supplier.getContactperson() + "\n";
            }

            if (!(oldSupplier.getAddress() != null && supplier.getAddress() != null && oldSupplier.getAddress().equals(supplier.getAddress()))) {
                if (oldSupplier.getAddress() != supplier.getAddress()) {
                    updates = updates + oldSupplier.getAddress() + " chnaged to " + supplier.getAddress() + "\n";
                }
            }

            if (!(oldSupplier.getLandphone() != null
                    && supplier.getLandphone() != null
                    && oldSupplier.getLandphone().equals(supplier.getLandphone()))) {
                if (oldSupplier.getLandphone() != supplier.getLandphone()) {
                    updates = updates + oldSupplier.getLandphone()
                            + " chnaged to " + supplier.getLandphone() + "\n";
                }
            }

            if (!(oldSupplier.getEmail() != null && supplier.getEmail() != null && oldSupplier.getEmail().equals(supplier.getEmail()))) {
                if (oldSupplier.getEmail() != supplier.getEmail()) {
                    updates = updates + oldSupplier.getEmail() + " chnaged to " + supplier.getEmail() + "\n";
                }
            }

            if (!supplier.getMobilephone().equals(oldSupplier.getMobilephone())) {
                updates = updates + oldSupplier.getMobilephone() + " chnaged to " + supplier.getMobilephone() + "\n";
            }
        }
        return updates;
    }

    private String getErrors() {
        String errors = "";

        if (supplier.getCompany() == null) {
            errors = errors + "Company Name \t\t\tis Invalid\n";
        }
        if (supplier.getContactperson() == null) {
            errors = errors + "Contact Person Name \t\tis Invalid\n";
        }
        if (supplier.getMobilephone() == null) {
            errors = errors + "Mobile No. \t\t\tis Invalid\n";
        }

        return errors;
    }

    private void fillForm() {
        if (tblSupplier.getSelectionModel().getSelectedItem() != null) {
            dissableButtons(false, true, false, false);
            setStyle(valid);

            oldSupplier = SupplierDao.getById(tblSupplier.getSelectionModel().getSelectedItem().getId());
            supplier = SupplierDao.getById(tblSupplier.getSelectionModel().getSelectedItem().getId());

            txtCompany.setText(supplier.getCompany());
            txtContactPerson.setText(supplier.getContactperson());
            txtAddress.setText(supplier.getAddress());
            txtMobile.setText(supplier.getMobilephone());
            txtLand.setText(supplier.getLandphone());
            txtEmail.setText(supplier.getEmail());

            page = pagination.getCurrentPageIndex();
            row = tblSupplier.getSelectionModel().getSelectedIndex();
        }
    }

    @FXML
    private void btnHomeAP() throws IOException {
        System.gc();
        Main.showHome();
    }

}
