package ui;

import dao.DaoException;
import dao.EmployeeDao;
import dao.RoleDao;
import dao.UserDao;
import dao.UserStatusDao;
import entity.Employee;
import entity.Role;
import entity.User;
import entity.Userstatus;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.PasswordField;
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
import javax.swing.JOptionPane;
import tray.notification.TrayNotification;
import static ui.LoginController.privilage;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class UserController implements Initializable {

    private Main main;

    @FXML
    private ComboBox<Employee> cmbEmployee;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField pswPassword;
    @FXML
    private PasswordField pswRetypePassword;
    @FXML
    private ComboBox<Userstatus> cmbStatus;
    @FXML
    private ListView<Role> lstLeft;
    @FXML
    private ListView<Role> lstRight;
    @FXML
    private Button btnRight;
    @FXML
    private Button btnRightAll;
    @FXML
    private Button btnLeftAll;
    @FXML
    private Button btnLeft;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnAdd;
    @FXML
    private ComboBox<Role> cmbSearchRole;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<User> tblUser;
    @FXML
    private TableColumn<User, Employee> colEmployee;
    @FXML
    private TableColumn<User, String> colUsername;
    @FXML
    private TableColumn<User, Userstatus> colStatus;
    @FXML
    private TextField txtSearchEmpName;
    @FXML
    private Button btnSearchClear;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Module-Data">
    //Color Indication of Input Controls
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    //Binding with the Form
    private User user;

    //Update Identification
    private User oldUser;

    //Table Row, Page Selected
    private int page;
    private int row;

//</editor-fold> 
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

        user = new User();
        oldUser = null;

        cmbEmployee.getItems().clear();
        cmbEmployee.setItems(EmployeeDao.getAllExeptUsers());
        cmbEmployee.getSelectionModel().select(-1);
        cmbStatus.setItems(UserStatusDao.getAll());
        cmbStatus.getSelectionModel().select(-1);
        txtUsername.setText("");
        pswPassword.setText("");
        pswRetypePassword.setText("");

        lstLeft.setItems(RoleDao.getAll());
        lstRight.getItems().clear();

        dissableButtons(false, false, true, true);
        validateList();
        setStyle(initial);
    }

    private void loadTable() {

        cmbSearchRole.setItems(RoleDao.getAll());
        cmbSearchRole.getSelectionModel().select(-1);

        txtSearchEmpName.setText("");

        colUsername.setCellValueFactory(new PropertyValueFactory("name"));
        colStatus.setCellValueFactory(new PropertyValueFactory("userstatusId"));
        colEmployee.setCellValueFactory(new PropertyValueFactory("employeeId"));

        fillTable(UserDao.getAll());
        pagination.setCurrentPageIndex(0);
    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("User_insert"));
        btnUpdate.setDisable(update || !privilage.get("User_update"));
        btnDelete.setDisable(delete || !privilage.get("User_delete"));

        txtSearchEmpName.setDisable(select || !privilage.get("User_select"));

        cmbSearchRole.setDisable(select || !privilage.get("User_select"));

        btnSearchClear.setDisable(select || !privilage.get("User_select"));

        btnLeft.setDisable(true);
        btnLeftAll.setDisable(true);
    }

    private void setStyle(String style) {
        cmbEmployee.setStyle(style);
        cmbStatus.setStyle(style);
        lstLeft.setStyle(style);
        lstRight.setStyle(style);

        txtUsername.setStyle(style);
        pswPassword.setStyle(style);
        pswRetypePassword.setStyle(style);
    }

    private void validateList() {

        if (user.setRoleList(lstRight.getItems())) {
            lstRight.setStyle(valid);
        } else {
            lstRight.setStyle(invalid);
        }

        if (oldUser != null && user.getRoleList() != null && !(user.getRoleList().containsAll(oldUser.getRoleList()) && oldUser.getRoleList().containsAll(user.getRoleList()))) {
            lstRight.setStyle(updated);
        }

        if (lstLeft.getItems().isEmpty()) {
            btnRight.setDisable(true);
            btnRightAll.setDisable(true);
            btnLeft.setDisable(false);
            btnLeftAll.setDisable(false);
        } else if (lstRight.getItems().isEmpty()) {
            btnRight.setDisable(false);
            btnRightAll.setDisable(false);
            btnLeft.setDisable(true);
            btnLeftAll.setDisable(true);
        } else {
            btnRight.setDisable(false);
            btnRightAll.setDisable(false);
            btnLeft.setDisable(false);
            btnLeftAll.setDisable(false);
        }
    }

    private void fillTable(ObservableList<User> users) {

        if (privilage.get("User_select") && users != null && users.size() != 0) {

            int rowsCount = 4;
            int pageCount = ((users.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? users.size() : pageIndex * rowsCount + rowsCount;
                    tblUser.getItems().clear();
                    tblUser.setItems(FXCollections.observableArrayList(users.subList(start, end)));
                    return tblUser;
                }
            });

        } else {

            pagination.setPageCount(1);
            tblUser.getItems().clear();
        }
    }

    @FXML
    private void cmbEmployeeAP(ActionEvent event) {
        if (cmbEmployee.getSelectionModel().getSelectedItem() != null) {

            user.setEmployeeId(cmbEmployee.getSelectionModel().getSelectedItem());

            if (oldUser != null && !user.getEmployeeId().equals(oldUser.getEmployeeId())) {

                cmbEmployee.setStyle(updated);
            } else {
                cmbEmployee.setStyle(valid);
            }
        }
    }

    @FXML
    private void txtUsernameKR(KeyEvent event) {
        if (user.setName(txtUsername.getText().trim()) && UserDao.getByName(txtUsername.getText().trim()) == null) {
            if (oldUser != null && !user.getName().equals(oldUser.getName())) {
                txtUsername.setStyle(updated);
            } else {
                txtUsername.setStyle(valid);
            }
        } else if (oldUser != null && user.getName() != null && user.getName().equals(oldUser.getName())) {
            txtUsername.setStyle(valid);
        } else {
            txtUsername.setStyle(invalid);
        }
    }

    @FXML
    private void pswPasswordKR(KeyEvent event) {
        pswRetypePassword.setText("");
        pswRetypePassword.setStyle(initial);
        if (user.setPassword(pswPassword.getText().trim())) {
            if (oldUser != null && !user.getPassword().equals(oldUser.getPassword())) {
                pswPassword.setStyle(updated);
            } else {
                pswPassword.setStyle(valid);
            }
        } else {
            pswPassword.setStyle(invalid);
        }
    }

    @FXML
    private void pswRetypePasswordKR(KeyEvent event) {
        if (pswPassword.getText().trim().equals(pswRetypePassword.getText().trim())) {
            pswRetypePassword.setStyle(valid);

        } else {
            pswRetypePassword.setStyle(invalid);
        }

    }

    @FXML
    private void cmbStatusAP(ActionEvent event) {
        user.setUserstatusId(cmbStatus.getSelectionModel().getSelectedItem());

        if (oldUser != null && !user.getUserstatusId().equals(oldUser.getUserstatusId())) {

            cmbStatus.setStyle(updated);
        } else {
            cmbStatus.setStyle(valid);
        }
    }

    @FXML
    private void btnRightAllAP(ActionEvent event) {
        lstRight.setItems(RoleDao.getAll());
        lstLeft.getItems().clear();
        validateList();
    }

    @FXML
    private void btnRightAP(ActionEvent event) {
        lstRight.getItems().addAll(lstLeft.getSelectionModel().getSelectedItems());
        lstLeft.getItems().removeAll(lstLeft.getSelectionModel().getSelectedItems());
        validateList();
    }

    @FXML
    private void btnLeftAP(ActionEvent event) {
        lstLeft.getItems().addAll(lstRight.getSelectionModel().getSelectedItems());
        lstRight.getItems().removeAll(lstRight.getSelectionModel().getSelectedItems());
        validateList();

    }

    @FXML
    private void btnLeftAllAP(ActionEvent event) {
        lstLeft.setItems(RoleDao.getAll());
        lstRight.getItems().clear();
        validateList();
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Form-Operation">
    private String getErrors() {
        String errors = "";
        try {
            if (user.getEmployeeId() == null) {
                errors = errors + "Employee \t\tis Not Selected\n";
            }
            if (user.getName() == null) {
                errors = errors + "Username \t\tis Invalid\n";
            }
            if (user.getPassword() == null) {
                errors = errors + "Password \t\tis Invalid\n";
            }

            if (!pswPassword.getText().equals(pswRetypePassword.getText())) {
                errors = errors + "Confirm Password \t\tis Mismatch\n";
            }
            if (user.getUserstatusId() == null) {
                errors = errors + "Userstatus \t\tis Not Selected\n";
            }
            if (user.getRoleList() == null) {
                errors = errors + "Roles            \t\tare Not Selected\n";
            }

        } catch (Exception e) {
            
            //----------- Notification Unsuccess-------------
                    TrayNotification tray = new TrayNotification();
                    Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                    tray.setTitle("UN-SUCCESS");
                    tray.setMessage("User " + user.getName() + " Error checking Error.\n" + e.getClass());
                    tray.setRectangleFill(Paint.valueOf("#dc0000"));
                    tray.showAndWait();
                    tray.setImage(wrongImg);
                    tray.showAndDismiss(Duration.seconds(4));
            
            System.out.println(e.getClass());
            JOptionPane.showMessageDialog(null, e.getClass(), "Error checking Error", JOptionPane.ERROR_MESSAGE);
        }
        return errors;

    }

    private String getUpdates() {

        String updates = "";

        try {

            if (oldUser != null) {
                if (user.getEmployeeId() != null && !user.getEmployeeId().equals(oldUser.getEmployeeId())) {
                    updates = updates + oldUser.getEmployeeId() + " chnaged to " + user.getEmployeeId() + "\n";
                }

                if (user.getName() != null && !user.getName().equals(oldUser.getName())) {
                    updates = updates + oldUser.getName() + " chnaged to " + user.getName() + "\n";
                }

                if (user.getPassword() != null && !user.getPassword().equals(oldUser.getPassword())) {
                    updates = updates + " Password chnaged  ";
                }

                if (user.getRoleList() != null && !user.getRoleList().equals(oldUser.getRoleList())) {
                    updates = updates + oldUser.getRoleList().toString() + " chnaged to " + user.getRoleList().toString() + "\n";
                }
                if (user.getUserstatusId() != null && !user.getUserstatusId().equals(oldUser.getUserstatusId())) {
                    updates = updates + oldUser.getUserstatusId() + " chnaged to " + user.getUserstatusId() + "\n";
                }

            }
        } catch (Exception e) {
            //----------- Notification Unsuccess-------------
                    TrayNotification tray = new TrayNotification();
                    Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                    tray.setTitle("UN-SUCCESS");
                    tray.setMessage("User " + user.getName() + " Update checking Error. \n" + e.getClass());
                    tray.setRectangleFill(Paint.valueOf("#dc0000"));
                    tray.showAndWait();
                    tray.setImage(wrongImg);
                    tray.showAndDismiss(Duration.seconds(4));
            
            System.out.println(e.getClass());
            JOptionPane.showMessageDialog(null, e.getClass(), "Update checking Error", JOptionPane.ERROR_MESSAGE);
        }
        return updates;
    }

    private void fillForm() {
        if (tblUser.getSelectionModel().getSelectedItem() != null) {
            dissableButtons(false, true, false, false);
            setStyle(valid);

            oldUser = UserDao.getById(tblUser.getSelectionModel().getSelectedItem().getId());
            user = UserDao.getById(tblUser.getSelectionModel().getSelectedItem().getId());

            cmbEmployee.getItems().clear();
            cmbEmployee.getItems().add(user.getEmployeeId());
            cmbEmployee.getSelectionModel().select(user.getEmployeeId());

            cmbStatus.getSelectionModel().select((Userstatus) user.getUserstatusId());

            txtUsername.setText(user.getName());
            pswPassword.setText("********");
            pswRetypePassword.setText("********");

            lstRight.setItems(FXCollections.observableArrayList(user.getRoleList()));
            lstLeft.setItems(RoleDao.getAll());
            lstLeft.getItems().removeAll(FXCollections.observableArrayList(user.getRoleList()));

            validateList();

            page = pagination.getCurrentPageIndex();
            row = tblUser.getSelectionModel().getSelectedIndex();
        }
    }

    @FXML
    private void btnAddAP(ActionEvent event) {
        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "Ara you sure you need to add this User with following details\n "
                    + "\nEmployee :  \t\t" + user.getEmployeeId().getName()
                    + "\nUsername :  \t\t" + user.getName()
                    + "\nRoles :     \t\t\t" + user.getRoleList().toString();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Add User with following Details");
            alert.setContentText(confermation);

            ButtonType ok = new ButtonType("ADD");
            ButtonType no = new ButtonType("DON'T");

            alert.getButtonTypes().setAll(ok, no);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ok) {

                try {
                    
                    UserDao.add(user);

                    //----------- Notification Success -------------
                    TrayNotification tray = new TrayNotification();
                    Image rightImg = new Image("/image/NotifyIcon/Right.png");
                    tray.setTitle("SUCCESS");
                    tray.setMessage("User " + user.getName() + " saved.");
                    tray.setRectangleFill(Paint.valueOf("#00b84c"));
                    tray.showAndWait();
                    tray.setImage(rightImg);
                    tray.showAndDismiss(Duration.seconds(4));

                    loadForm();
                    fillTable(UserDao.getAll());
                    pagination.setCurrentPageIndex(pagination.getPageCount() - 1);
                    tblUser.getSelectionModel().select(tblUser.getItems().size() - 1);

                } catch (DaoException ex) {

                    //----------- Notification Unsuccess-------------
                    TrayNotification tray = new TrayNotification();
                    Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                    tray.setTitle("UN-SUCCESS");
                    tray.setMessage("User " + user.getName() + " not saved. \n Try Again.\n" + ex);
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
            alert.setContentText("User Detail Error");
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
    private void btnUpdateAP(ActionEvent event) {

        String errors = getErrors();

        if (errors.isEmpty()) {

            String updates = getUpdates();

            if (!updates.isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("User Update");
                alert.setContentText("Update User " + user.getName());

                ButtonType ok = new ButtonType("UPDATE");
                ButtonType no = new ButtonType("DON'T");

                alert.getButtonTypes().setAll(ok, no);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ok) {

                    try {

                        UserDao.update(user);

                        //----------- Notification Success -------------
                        TrayNotification tray = new TrayNotification();
                        Image rightImg = new Image("/image/NotifyIcon/Right.png");
                        tray.setTitle("SUCCESS");
                        tray.setMessage("User " + user.getName() + " updated.");
                        tray.setRectangleFill(Paint.valueOf("#00b84c"));
                        tray.showAndWait();
                        tray.setImage(rightImg);
                        tray.showAndDismiss(Duration.seconds(4));

                        loadTable();
                        loadForm();
                        pagination.setCurrentPageIndex(page);
                        tblUser.getSelectionModel().select(row);

                    } catch (Exception ex) {
                        //----------- Notification Unsuccess-------------
                        TrayNotification tray = new TrayNotification();
                        Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                        tray.setTitle("UN-SUCCESS");
                        tray.setMessage("User " + user.getName() + " not update!\n" + ex);
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
                alert.setHeaderText("User Update");
                alert.setContentText("Nothing Updated");
                alert.showAndWait();
            }
        } else {

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("User Update");
            alert.setContentText("User not updated!");
            alert.showAndWait();

        }
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {

        if (getUpdates().isEmpty() && getErrors().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("User Delete");
            alert.setContentText("Delete User " + user.getName());

            ButtonType ok = new ButtonType("DELETE");
            ButtonType no = new ButtonType("DON'T");

            alert.getButtonTypes().setAll(ok, no);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ok) {

                try {
                    UserDao.delete(user);

                    //----------- Notification Success -------------
                    TrayNotification tray = new TrayNotification();
                    Image rightImg = new Image("/image/NotifyIcon/Right.png");
                    tray.setTitle("SUCCESS");
                    tray.setMessage("User " + user.getName() + " deleted.");
                    tray.setRectangleFill(Paint.valueOf("#00b84c"));
                    tray.showAndWait();
                    tray.setImage(rightImg);
                    tray.showAndDismiss(Duration.seconds(4));

                    loadForm();
                    loadTable();
                    pagination.setCurrentPageIndex(page);
                    tblUser.getSelectionModel().select(row);

                } catch (Exception ex) {
                    //----------- Notification Unsuccess-------------
                    TrayNotification tray = new TrayNotification();
                    Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                    tray.setTitle("UN-SUCCESS");
                    tray.setMessage("User " + user.getName() + " not Deleted!\n" + ex);
                    tray.setRectangleFill(Paint.valueOf("#dc0000"));
                    tray.showAndWait();
                    tray.setImage(wrongImg);
                    tray.showAndDismiss(Duration.seconds(4));
                }

            } else if (result.get() == no) {
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("User Delete");
            alert.setHeaderText(oldUser.getName() + " Delete ?");
            alert.setContentText("You can't delete\nSome of the fields are updated");
            alert.showAndWait();
        }

    }

    @FXML
    private void tblUserMC(MouseEvent event) {
        fillForm();
    }

    @FXML
    private void tblUserKR(KeyEvent event) {
        fillForm();
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Search-Methods">
    private void updateTable() {

        String name = txtSearchEmpName.getText().trim();
        boolean uname = !name.isEmpty();

        Role role = cmbSearchRole.getSelectionModel().getSelectedItem();
        boolean srole = cmbSearchRole.getSelectionModel().getSelectedIndex() != -1;

        if (!uname) {

            if (srole) {

                fillTable(UserDao.getAllByRole(role));

            } else {
                fillTable(UserDao.getAll());
            }

        } else if (srole) {
            fillTable(UserDao.getAllByNameRole(name, role));

        } else {
            fillTable(UserDao.getAllByName(name));
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
    private void cmbSearchRoleAP(ActionEvent event) {
        updateTable();
    }

    @FXML
    private void txtSearchEmpNameKR(KeyEvent event) {
        updateTable();
    }

    @FXML
    private void btnHomeAP() throws IOException {
        System.gc();
        Main.showHome();
    }

}
