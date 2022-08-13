
package ui;

import dao.CivilstatusDao;
import dao.DaoException;
import dao.DesignationDao;
import dao.EmployeeDao;
import dao.EmployeestatusDao;
import dao.GenderDao;
import entity.Civilstatus;
import entity.Designation;
import entity.Employee;
import entity.Employeestatus;
import entity.Gender;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.swing.ImageIcon;
import tray.notification.TrayNotification;
import static ui.IMobileTec_MainWindowController.lastDirectory;
import static ui.LoginController.privilage;
import static ui.Main.stage;

public class EmployeeController implements Initializable {

    private Main main;

    //<editor-fold defaultstate="collapsed" desc="Module-Data">
    //Color Indication of Input Controls
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    //Binding with the Form
    private Employee employee;

    //Update Identification
    private Employee oldEmployee;

    //Table Row, Page Selected
    private int page;
    private int row;

    //Photo Selection
    private boolean photoSelected;

    //Search Lock
    private boolean lock;

//</editor-fold>   
    @FXML
    private TextField txtName;
    @FXML
    private ComboBox<Civilstatus> cmbCivilstatus;
    @FXML
    private DatePicker dtpDob;
    @FXML
    private TextField txtNic;
    @FXML
    private ComboBox<Gender> cmbGender;
    @FXML
    private TextArea txtAddress;
    @FXML
    private TextField txtMobile;
    @FXML
    private TextField txtLand;
    @FXML
    private TextField txtEmail;
    @FXML
    private ImageView imgPhoto;
    @FXML
    private ComboBox<Designation> cmbDesignation;
    @FXML
    private DatePicker dtpAssign;
    @FXML
    private ComboBox<Employeestatus> cmbEmployeestatus;
    @FXML
    private TextField txtSearchName;
    @FXML
    private ComboBox<Employeestatus> cmbSearchEmployeestatus;
    @FXML
    private ComboBox<Designation> cmbSearchDesignation;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnPhotoSelect;
    @FXML
    private Button btnPhotoClear;
    @FXML
    private Button btnSearchClear;
    @FXML
    private Button btnSearchLock;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Employee> tblEmployee;
    @FXML
    private TableColumn<Employee, String> colName;
    @FXML
    private TableColumn<Employee, Employeestatus> colStatus;
    @FXML
    private TableColumn<Employee, Designation> colDesignation;
    @FXML
    private TableColumn<Employee, String> colMobile;
    @FXML
    private TableColumn<Employee, String> colEmail;
    @FXML
    private Label lblNewDesignation;

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

        employee = new Employee();
        oldEmployee = null;

        cmbGender.setItems(GenderDao.getAll());
        cmbGender.getSelectionModel().select(-1);
        cmbCivilstatus.setItems(CivilstatusDao.getAll());
        cmbCivilstatus.getSelectionModel().select(-1);
        cmbDesignation.setItems(DesignationDao.getAll());
        cmbDesignation.getSelectionModel().select(-1);
        cmbEmployeestatus.setItems(EmployeestatusDao.getAll());
        cmbEmployeestatus.getSelectionModel().select(-1);

        txtName.setText("");
        txtAddress.setText("");
        txtNic.setText("");
        txtMobile.setText("");
        txtLand.setText("");
        txtEmail.setText("");
        dtpDob.setValue(null);
        dtpAssign.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));

        imgPhoto.setImage(new Image("/image/p1.png"));
        photoSelected = false;
        dissableButtons(false, false, true, true);
        setStyle(initial);
    }

    private void loadTable() {
        lock = false;
        btnSearchLock.setText("Lock");

        cmbSearchEmployeestatus.setItems(EmployeestatusDao.getAll());
        cmbSearchEmployeestatus.getSelectionModel().select(-1);
        cmbSearchDesignation.setItems(DesignationDao.getAll());
        cmbSearchDesignation.getSelectionModel().select(-1);
        txtSearchName.setText("");

        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colStatus.setCellValueFactory(new PropertyValueFactory("employeestatusId"));
        colMobile.setCellValueFactory(new PropertyValueFactory("mobile"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colDesignation.setCellValueFactory(new PropertyValueFactory("designationId"));

        fillTable(EmployeeDao.getAll());
        pagination.setCurrentPageIndex(0);
    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Employee_insert"));
        btnUpdate.setDisable(update || !privilage.get("Employee_update"));
        btnDelete.setDisable(delete || !privilage.get("Employee_delete"));

        txtSearchName.setDisable(select || !privilage.get("Employee_select"));
        cmbSearchDesignation.setDisable(select || !privilage.get("Employee_select"));
        cmbSearchEmployeestatus.setDisable(select || !privilage.get("Employee_select"));
        btnSearchLock.setDisable(select || !privilage.get("Employee_select"));
        btnSearchClear.setDisable(select || !privilage.get("Employee_select"));

        lblNewDesignation.setDisable(select || !privilage.get("Designation_select"));
    }

    private void setStyle(String style) {
        cmbGender.setStyle(style);
        cmbCivilstatus.setStyle(style);
        cmbDesignation.setStyle(style);
        cmbEmployeestatus.setStyle(style);

        txtName.setStyle(style);
        txtNic.setStyle(style);
        txtMobile.setStyle(style);
        txtLand.setStyle(style);
        txtEmail.setStyle(style);

        if (!txtAddress.getChildrenUnmodifiable().isEmpty()) {
            ((ScrollPane) txtAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(style);
        }

        dtpDob.getEditor().setStyle(style);
        dtpAssign.getEditor().setStyle(style);
    }

    private void fillTable(ObservableList<Employee> employees) {

        if (privilage.get("Employee_select") && employees != null && employees.size() != 0) {

            int rowsCount = 14;
            int pageCount = ((employees.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? employees.size() : pageIndex * rowsCount + rowsCount;
                    tblEmployee.getItems().clear();
                    tblEmployee.setItems(FXCollections.observableArrayList(employees.subList(start, end)));
                    return tblEmployee;
                }
            });

        } else {

            pagination.setPageCount(1);
            tblEmployee.getItems().clear();
        }
    }

    @FXML
    private void txtNameKR(KeyEvent event) {

        if (employee.setName(txtName.getText().trim())) {
            if (oldEmployee != null && !employee.getName().equals(oldEmployee.getName())) {
                txtName.setStyle(updated);
            } else {
                txtName.setStyle(valid);
            }
        } else {
            txtName.setStyle(invalid);
        }
    }

    @FXML
    private void cmbCivilstatusAP(ActionEvent event) {
        employee.setCivilstatusId(cmbCivilstatus.getSelectionModel().getSelectedItem());
        if (oldEmployee != null && !employee.getCivilstatusId().equals(oldEmployee.getCivilstatusId())) {
            cmbCivilstatus.setStyle(updated);
        } else {
            cmbCivilstatus.setStyle(valid);
        }
    }

    @FXML
    private void dtpDobAP(ActionEvent event) {
        if (dtpDob.getValue() != null) {
            Date today = new Date();
            today.setYear(today.getYear() - 18);
            Date dob = java.sql.Date.valueOf(dtpDob.getValue());
            if (dob.before(today)) {
                employee.setDob(dob);
                if (oldEmployee != null && !employee.getDob().equals(oldEmployee.getDob())) {
                    dtpDob.getEditor().setStyle(updated);
                } else {
                    dtpDob.getEditor().setStyle(valid);
                }
            } else {
                dtpDob.getEditor().setStyle(invalid);
            }
        }
    }

    @FXML
    private void txtNicKR(KeyEvent event) {
        if (employee.setNic(txtNic.getText().trim())) {
            if (oldEmployee != null && !employee.getNic().equals(oldEmployee.getNic())) {
                txtNic.setStyle(updated);
            } else {
                txtNic.setStyle(valid);
            }
        } else {
            txtNic.setStyle(invalid);
        }
    }

    @FXML
    private void cmbGenderAP(ActionEvent event) {
        employee.setGenderId(cmbGender.getSelectionModel().getSelectedItem());
        if (oldEmployee != null && !employee.getGenderId().equals(oldEmployee.getGenderId())) {
            cmbGender.setStyle(updated);
        } else {
            cmbGender.setStyle(valid);
        }
    }

    @FXML
    private void txtAddressKR(KeyEvent event) {
        if (employee.setAddress(txtAddress.getText().trim())) {
            if (oldEmployee != null && !employee.getAddress().equals(oldEmployee.getAddress())) {
                ((ScrollPane) txtAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(updated);
            } else {
                ((ScrollPane) txtAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(valid);
            }
        } else {
            ((ScrollPane) txtAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(invalid);
        }
    }

    @FXML
    private void txtMobileKR(KeyEvent event) {

        if (employee.setMobile(txtMobile.getText().trim())) {
            if (oldEmployee != null && !employee.getMobile().equals(oldEmployee.getMobile())) {
                txtMobile.setStyle(updated);
            } else {
                txtMobile.setStyle(valid);
            }
        } else {
            txtMobile.setStyle(invalid);
        }
    }

    @FXML
    private void txtLandKR(KeyEvent event) {

        if (employee.setLand(txtLand.getText())) {
            if (oldEmployee != null && oldEmployee.getLand() != null && employee.getLand() != null && oldEmployee.getLand().equals(employee.getLand())) {
                txtLand.setStyle(valid);
            } else if (oldEmployee != null && oldEmployee.getLand() != employee.getLand()) {
                txtLand.setStyle(updated);
            } else {
                txtLand.setStyle(valid);
            }
        } else {
            txtLand.setStyle(invalid);
        }
    }

    @FXML
    private void txtEmailKR(KeyEvent event) {

        if (employee.setEmail(txtEmail.getText().trim())) {
            if (oldEmployee != null && oldEmployee.getEmail() != null && employee.getEmail() != null && oldEmployee.getEmail().equals(employee.getEmail())) {
                txtEmail.setStyle(valid);
            } else if (oldEmployee != null && oldEmployee.getEmail() != employee.getEmail()) {
                txtEmail.setStyle(updated);
            } else {
                txtEmail.setStyle(valid);
            }

        } else {
            txtEmail.setStyle(invalid);
        }
    }

    @FXML
    private void cmbDesignationAP(ActionEvent event) {
        employee.setDesignationId(cmbDesignation.getSelectionModel().getSelectedItem());
        if (oldEmployee != null && !employee.getDesignationId().equals(oldEmployee.getDesignationId())) {
            cmbDesignation.setStyle(updated);
        } else {
            cmbDesignation.setStyle(valid);
        }
    }

    @FXML
    private void dtpAssignAP(ActionEvent event) {
        if (dtpDob.getValue() != null) {
            Date today = new Date();
            Date assign = java.sql.Date.valueOf(dtpAssign.getValue());
            if (assign.before(today)) {
                employee.setAssigned(assign);
                if (oldEmployee != null && !employee.getAssigned().equals(oldEmployee.getAssigned())) {
                    dtpAssign.getEditor().setStyle(updated);
                } else {
                    dtpAssign.getEditor().setStyle(valid);
                }
            } else {
                dtpAssign.getEditor().setStyle(invalid);
            }
        }
    }

    @FXML
    private void btnPhotoSelectAP(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        if (lastDirectory != null) {
            fileChooser.setInitialDirectory(lastDirectory);
        }
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            lastDirectory = file.getParentFile();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                byte[] image = new byte[(int) file.length()];
                DataInputStream dataIs = new DataInputStream(new FileInputStream(file));
                dataIs.readFully(image);

                ImageIcon img = new ImageIcon(image);
                String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('.'));
                if (extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".gif")) {

                    if (img.getIconHeight() <= 78 && img.getIconWidth() <= 56) {
                        Image photo = new Image(fis);
                        imgPhoto.setImage(photo);
                        employee.setImage(image);
                        photoSelected = true;

                    } else {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("The Image Size should smaller than '78 x 56' Pixel");
                        alert.showAndWait();
                        photoSelected = false;
                    }
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("The File should be JPG, JPEG, GIF or PNG");
                    alert.showAndWait();
                    photoSelected = false;
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            photoSelected = false;
        }
    }

    @FXML
    private void btnPhotoClearAP(ActionEvent event) {

        if (employee.getImage() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Clear the photo");
            alert.setContentText("Do you want to clear the photo?");

            ButtonType ok = new ButtonType("CLEAR");
            ButtonType no = new ButtonType("DON'T");

            alert.getButtonTypes().setAll(ok, no);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ok) {

                imgPhoto.setImage(new Image("/image/p1.png"));
                if (oldEmployee != null && oldEmployee.getImage() != null) {
                    photoSelected = true;
                } else {
                    photoSelected = false;
                }
                employee.setImage(null);
            } else if (result.get() == no) {
                // ... Canceled
            }
        }
    }

    @FXML
    private void cmbEmployeestatusAP(ActionEvent event) {

        employee.setEmployeestatusId(cmbEmployeestatus.getSelectionModel().getSelectedItem());
        if (oldEmployee != null && !employee.getEmployeestatusId().equals(oldEmployee.getEmployeestatusId())) {
            cmbEmployeestatus.setStyle(updated);
        } else {
            cmbEmployeestatus.setStyle(valid);
        }
    }

    @FXML
    private void lblNewDesignationMC() throws IOException {
        Main.showDesignation();
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Form-Operation-Methods">
    private String getErrors() {
        String errors = "";

        if (employee.getName() == null) {
            errors = errors + "Name \t\tis Invalid\n";
        }
        if (employee.getGenderId() == null) {
            errors = errors + "Gender \t\tis Not Selected\n";
        }
        if (employee.getDob() == null) {
            errors = errors + "Birth Date \tis Invalid\n";
        }
        if (employee.getNic() == null) {
            errors = errors + "NIC No. \t\tis Invalid\n";
        }
        if (employee.getCivilstatusId() == null) {
            errors = errors + "Civilstatus \tis Not Selected\n";
        }
        if (employee.getAddress() == null) {
            errors = errors + "Address \t\tis Invalid\n";
        }
        if (employee.getMobile() == null) {
            errors = errors + "Mobile No. \tis Invalid\n";
        }
        if (txtLand.getText() != null && !employee.setLand(txtLand.getText().trim())) {
            errors = errors + "Land No. \t\tis Invalid\n";
        }
        if (txtEmail.getText() != null && !employee.setEmail(txtEmail.getText().trim())) {
            errors = errors + "Email \t\tis Invalid\n";
        }

        if (employee.getDesignationId() == null) {
            errors = errors + "Designation \tis Not Selected\n";
        }

        if (employee.getAssigned() == null) {
            errors = errors + "Assign Date \tis Invalid\n";
        }

        if (employee.getEmployeestatusId() == null) {
            errors = errors + "Status \t\tis Not Selected\n";
        }
        return errors;
    }

    private String getUpdates() {

        String updates = "";

        if (oldEmployee != null) {

            if (employee.getName() != null && !employee.getName().equals(oldEmployee.getName())) {
                updates = updates + oldEmployee.getName() + " chnaged to " + employee.getName() + "\n";
            }

            if (!employee.getAddress().equals(oldEmployee.getAddress())) {
                updates = updates + oldEmployee.getAddress() + " chnaged to " + employee.getAddress() + "\n";
            }

            if (!employee.getNic().equals(oldEmployee.getNic())) {
                updates = updates + oldEmployee.getNic() + " chnaged to " + employee.getNic() + "\n";
            }

            if (!(oldEmployee.getLand() != null
                    && employee.getLand() != null
                    && oldEmployee.getLand().equals(employee.getLand()))) {
                if (oldEmployee.getLand() != employee.getLand()) {
                    updates = updates + oldEmployee.getLand()
                            + " chnaged to " + employee.getLand() + "\n";
                }
            }

            if (!(oldEmployee.getEmail() != null && employee.getEmail() != null && oldEmployee.getEmail().equals(employee.getEmail()))) {
                if (oldEmployee.getEmail() != employee.getEmail()) {
                    updates = updates + oldEmployee.getEmail() + " chnaged to " + employee.getEmail() + "\n";
                }
            }

            if (!employee.getMobile().equals(oldEmployee.getMobile())) {
                updates = updates + oldEmployee.getMobile() + " chnaged to " + employee.getMobile() + "\n";
            }

            if (!employee.getGenderId().equals(oldEmployee.getGenderId())) {
                updates = updates + oldEmployee.getGenderId() + " chnaged to " + employee.getGenderId() + "\n";
            }

            if (!employee.getCivilstatusId().equals(oldEmployee.getCivilstatusId())) {
                updates = updates + oldEmployee.getCivilstatusId() + " chnaged to " + employee.getCivilstatusId() + "\n";
            }

            if (!employee.getDesignationId().equals(oldEmployee.getDesignationId())) {
                updates = updates + oldEmployee.getDesignationId() + " chnaged to " + employee.getDesignationId() + "\n";
            }

            if (!employee.getDob().equals(oldEmployee.getDob())) {
                updates = updates + oldEmployee.getDob().toString() + "(dob)" + " chnaged to " + employee.getDob().toString() + "\n";
            }

            if (!employee.getAssigned().equals(oldEmployee.getAssigned())) {
                updates = updates + oldEmployee.getAssigned().toString() + " chnaged to " + employee.getAssigned().toString() + "\n";
            }

            if (photoSelected) {
                updates = updates + "Photo Changed\n";
            }

            if (!employee.getEmployeestatusId().equals(oldEmployee.getEmployeestatusId())) {
                updates = updates + oldEmployee.getEmployeestatusId() + " chnaged to " + employee.getEmployeestatusId() + "\n";
            }
        }
        return updates;
    }

    private void fillForm() {
        if (tblEmployee.getSelectionModel().getSelectedItem() != null) {
            dissableButtons(false, true, false, false);
            setStyle(valid);

            oldEmployee = EmployeeDao.getById(tblEmployee.getSelectionModel().getSelectedItem().getId());
            employee = EmployeeDao.getById(tblEmployee.getSelectionModel().getSelectedItem().getId());

            cmbGender.getSelectionModel().select((Gender) employee.getGenderId());
            cmbCivilstatus.getSelectionModel().select((Civilstatus) employee.getCivilstatusId());
            cmbDesignation.getSelectionModel().select((Designation) employee.getDesignationId());
            cmbEmployeestatus.getSelectionModel().select((Employeestatus) employee.getEmployeestatusId());

            txtName.setText(employee.getName());
            txtAddress.setText(employee.getAddress());
            txtNic.setText(employee.getNic());
            txtMobile.setText(employee.getMobile());
            txtLand.setText(employee.getLand());
            txtEmail.setText(employee.getEmail());

            dtpDob.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(employee.getDob())));
            dtpDob.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(employee.getDob()));
            dtpAssign.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(employee.getAssigned())));
            dtpAssign.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(employee.getAssigned()));

            if (employee.getImage() != null) {
                imgPhoto.setImage(new Image(new ByteArrayInputStream(employee.getImage())));
            } else {
                imgPhoto.setImage(new Image("/image/p1.png"));
            }

            page = pagination.getCurrentPageIndex();
            row = tblEmployee.getSelectionModel().getSelectedIndex();
        }
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {

        if (getUpdates().isEmpty() && getErrors().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Employee Delete");
            alert.setContentText("Delete Employee " + employee.getName());

            ButtonType ok = new ButtonType("DELETE");
            ButtonType no = new ButtonType("DON'T");

            alert.getButtonTypes().setAll(ok, no);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ok) {
                
                try {
                    EmployeeDao.delete(employee);

                //----------- Notification Success -------------
                TrayNotification tray = new TrayNotification();
                Image rightImg = new Image("/image/NotifyIcon/Right.png");
                tray.setTitle("SUCCESS");
                tray.setMessage("Employee " + employee.getName() + " deleted.");
                tray.setRectangleFill(Paint.valueOf("#00b84c"));
                tray.showAndWait();
                tray.setImage(rightImg);
                tray.showAndDismiss(Duration.seconds(4));

                loadForm();
                updateTable();
                pagination.setCurrentPageIndex(page);
                tblEmployee.getSelectionModel().select(row);
                
                } catch (Exception ex) {
                    //----------- Notification Unsuccess-------------
                    TrayNotification tray = new TrayNotification();
                    Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                    tray.setTitle("UN-SUCCESS");
                    tray.setMessage("Employee " + employee.getName() + " not Deleted!\n" + ex);
                    tray.setRectangleFill(Paint.valueOf("#dc0000"));
                    tray.showAndWait();
                    tray.setImage(wrongImg);
                    tray.showAndDismiss(Duration.seconds(4));
                }
            } else if (result.get() == no) {}
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Employee Delete");
            alert.setHeaderText(oldEmployee.getName() + " Delete ?");
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
                alert.setHeaderText("Employee Update");
                alert.setContentText("Update Employee " + employee.getName());

                ButtonType ok = new ButtonType("UPDATE");
                ButtonType no = new ButtonType("DON'T");

                alert.getButtonTypes().setAll(ok, no);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ok) {
                    
                    try {
                        
                        EmployeeDao.update(employee);
                        
                            //----------- Notification Success -------------
                            TrayNotification tray = new TrayNotification();
                            Image rightImg = new Image("/image/NotifyIcon/Right.png");
                            tray.setTitle("SUCCESS");
                            tray.setMessage("Employee " + employee.getName() + " updated.");
                            tray.setRectangleFill(Paint.valueOf("#00b84c"));
                            tray.showAndWait();
                            tray.setImage(rightImg);
                            tray.showAndDismiss(Duration.seconds(4));

                        updateTable();
                        loadForm();
                        pagination.setCurrentPageIndex(page);
                        tblEmployee.getSelectionModel().select(row);
                    
                    } catch (Exception ex) {
                            //----------- Notification Unsuccess-------------
                            TrayNotification tray = new TrayNotification();
                            Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                            tray.setTitle("UN-SUCCESS");
                            tray.setMessage("Employee " + employee.getName() + " not Update!\n" + ex);
                            tray.setRectangleFill(Paint.valueOf("#dc0000"));
                            tray.showAndWait();
                            tray.setImage(wrongImg);
                            tray.showAndDismiss(Duration.seconds(4));
                    }

                } else if (result.get() == no) {
                }
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText("Employee Update");
                alert.setContentText("Nothing Updated");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("Employee Update");
            alert.setContentText("Employee not updated!");
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

            String confermation = "Ara you sure you need to add this Employee with following details\n "
                    + "\nName :         \t\t" + employee.getName()
                    + "\nGender :       \t\t" + employee.getGenderId().getName()
                    + "\nBirth Date :   \t\t" + employee.getDob().toString()
                    + "\nNIC No :       \t\t" + employee.getNic()
                    + "\nCivilstatus :  \t\t" + employee.getCivilstatusId().getName()
                    + "\nAddress :      \t\t" + employee.getAddress()
                    + "\nMobile No :    \t\t" + employee.getMobile()
                    + "\nLand :         \t\t" + employee.getLand()
                    + "\nEmail :        \t\t" + employee.getEmail()
                    + "\nPhoto :  \t\t\t" + (employee.getImage() == null ? "Not Selected" : "Selected")
                    + "\nDesignation :  \t\t" + employee.getDesignationId().getName()
                    + "\nAssigned Date :  \t" + employee.getAssigned().toString()
                    + "\nStatus :  \t\t\t" + employee.getEmployeestatusId().getName();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Add Employee with following Details");
            alert.setContentText(confermation);

            ButtonType ok = new ButtonType("ADD");
            ButtonType no = new ButtonType("DON'T");

            alert.getButtonTypes().setAll(ok, no);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ok) {

                try {
                    EmployeeDao.add(employee);

                    //----------- Notification Success -------------
                    TrayNotification tray = new TrayNotification();
                    Image rightImg = new Image("/image/NotifyIcon/Right.png");
                    tray.setTitle("SUCCESS");
                    tray.setMessage("Employee " + employee.getName() + " saved.");
                    tray.setRectangleFill(Paint.valueOf("#00b84c"));
                    tray.showAndWait();
                    tray.setImage(rightImg);
                    tray.showAndDismiss(Duration.seconds(4));

                    loadForm();
                    updateTable();
                    pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                    tblEmployee.getSelectionModel().select(tblEmployee.getItems().size() - 1);

                } catch (DaoException ex) {
                    //----------- Notification Unsuccess-------------
                    TrayNotification tray = new TrayNotification();
                    Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                    tray.setTitle("UN-SUCCESS");
                    tray.setMessage("Employee " + employee.getName() + " not saved. \n Try Again.");
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
            alert.setContentText("Employee Detail Error");
            alert.showAndWait();
        }
    }

    @FXML
    private void tblEmployeeMC(MouseEvent event) {
        fillForm();
    }

    @FXML
    private void tblEmployeeKR(KeyEvent event) {
        fillForm();
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Search-Methods">
    private void updateTable() {

        String name = txtSearchName.getText().trim();
        boolean sname = !name.isEmpty();
        Employeestatus status = cmbSearchEmployeestatus.getSelectionModel().getSelectedItem();
        boolean sstatus = cmbSearchEmployeestatus.getSelectionModel().getSelectedIndex() != -1;
        Designation designation = cmbSearchDesignation.getSelectionModel().getSelectedItem();
        boolean sdesignation = cmbSearchDesignation.getSelectionModel().getSelectedIndex() != -1;

        if (!sname && !sstatus && !sdesignation) {
            fillTable(EmployeeDao.getAll());
        }
        if (sname && !sstatus && !sdesignation) {
            fillTable(EmployeeDao.getAllByName(name));
        }
        if (!sname && !sstatus && sdesignation) {
            fillTable(EmployeeDao.getAllByDesignation(designation));
        }
        if (!sname && sstatus && !sdesignation) {
            fillTable(EmployeeDao.getAllByStatus(status));
        }
        if (sname && sstatus && !sdesignation) {
            fillTable(EmployeeDao.getAllByNameStatus(name, status));
        }
        if (sname && !sstatus && sdesignation) {
            fillTable(EmployeeDao.getAllByNameDesignation(name, designation));
        }
        if (!sname && sstatus && sdesignation) {
            fillTable(EmployeeDao.getAllByStatusDesignation(status, designation));
        }
        if (sname && sstatus && sdesignation) {
            fillTable(EmployeeDao.getAllByNameStatusDesignation(name, status, designation));
        }
    }

    @FXML
    private void cmbSearchEmployeestatusAP(ActionEvent event) {

        if (cmbSearchEmployeestatus.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                txtSearchName.setText("");
                cmbSearchDesignation.getSelectionModel().select(-1);
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
        }
    }

    @FXML
    private void btnSearchLockAP(ActionEvent event) {

        if (lock) {
            btnSearchLock.setText("Lock");
            lock = false;
            fillTable(EmployeeDao.getAll());
        } else {
            btnSearchLock.setText("Unlock");
            lock = true;
            updateTable();
        }
    }

    @FXML
    private void txtSearchNameKR(KeyEvent event) {

        if (!lock) {
            cmbSearchEmployeestatus.getSelectionModel().select(-1);
            cmbSearchDesignation.getSelectionModel().select(-1);
        }
        updateTable();
    }

    @FXML
    private void cmbSearchDesignationAP(ActionEvent event) {

        if (cmbSearchDesignation.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                txtSearchName.setText("");
                cmbSearchEmployeestatus.getSelectionModel().select(-1);
            }
            updateTable();
        }
    }

    @FXML
    private void btnHomeAP() throws IOException {
        System.gc();
        Main.showHome();
    }
}
