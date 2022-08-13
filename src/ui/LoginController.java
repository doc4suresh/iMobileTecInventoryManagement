package ui;

import dao.ModuleDao;
import dao.PrivilageDao;
import dao.UserDao;
import entity.Module;
import entity.Privilage;
import entity.User;
import java.io.IOException;
import java.util.HashMap;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import util.Security;

public class LoginController implements Initializable {

    private static final String lblback = "-fx-background-color:#da5454;";

    //<editor-fold defaultstate="collapsed" desc="FXML-Data">
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField pswPassword;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblAttempt;
//</editor-fold>

    private int attempt;
    public static User user;
    public static HashMap<String, Boolean> privilage;

    //<editor-fold defaultstate="collapsed" desc="Initialize-Methods">
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        attempt = 2;

    }
//</editor-fold>

    @FXML
    private void btnCancelAP(ActionEvent event) {
        System.exit(3);
    }

    @FXML
    private void btnLoginAP() throws SQLException {

        if (!txtUsername.getText().isEmpty() && !pswPassword.getText().isEmpty()) {

            if (txtUsername.getText().equals("admin") && pswPassword.getText().equals("Password123")) {

                privilage = new HashMap<String, Boolean>();

                ObservableList<Module> x = ModuleDao.getAll();

                for (Module module : x) {
                    privilage.put(module.getName() + "_select", true);
                    privilage.put(module.getName() + "_insert", true);
                    privilage.put(module.getName() + "_update", true);
                    privilage.put(module.getName() + "_delete", true);
                }

                user = UserDao.getByName("admin");

            } else if (attempt == 0) {
                System.exit(0);
            } else {

                Connection connection = null;

                String location = "jdbc:mysql://localhost:3306/imobiletecv4db";
                String username = "root";
                String password = "6395";
                try {
                    connection = DriverManager.getConnection(location, username, password);
                } catch (SQLException ex) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText("Login Fail");
                    alert.setContentText("Could not connect to the server");
                    alert.showAndWait();
                }

                String query = "SELECT * FROM user WHERE name =? AND password = ?";

                try {

                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, txtUsername.getText());
                    statement.setString(2, Security.getHash(pswPassword.getText()));
                    ResultSet results = statement.executeQuery();

                    if (results.next()) {

                        user = UserDao.getById(results.getInt("id"));
                        privilage = new HashMap<String, Boolean>();

                        ObservableList<Module> x = ModuleDao.getAll();

                        for (Module module : x) {
                            privilage.put(module.getName() + "_select", false);
                            privilage.put(module.getName() + "_insert", false);
                            privilage.put(module.getName() + "_update", false);
                            privilage.put(module.getName() + "_delete", false);
                        }

                        ObservableList<Privilage> privilages = PrivilageDao.getAllByUser(user);

                        for (Privilage privi : privilages) {

                            String moduleName = privi.getModuleId().getName();

                            if (privi.getSel() == 1) {
                                if (!privilage.get(moduleName + "_select")) {
                                    privilage.put(moduleName + "_select", true);
                                }
                            }

                            if (privi.getIns() == 1) {
                                if (!privilage.get(moduleName + "_insert")) {
                                    privilage.put(moduleName + "_insert", true);
                                }
                            }

                            if (privi.getUpd() == 1) {
                                if (!privilage.get(moduleName + "_update")) {
                                    privilage.put(moduleName + "_update", true);
                                }
                            }

                            if (privi.getDel() == 1) {
                                if (!privilage.get(moduleName + "_delete")) {
                                    privilage.put(moduleName + "_delete", true);
                                }
                            }

                        }

                        statement.close();

                    } else {
                        lblAttempt.setText("Login Faild. You have " + (attempt--) + " more attemts.");
                        lblAttempt.setStyle(lblback);
                        pswPassword.setText("");
                    }

                } catch (SQLException ex) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText("Login Fail");
                    alert.setContentText("Could not retrieve date to from Database");
                    alert.showAndWait();
                }

            }
            if (privilage != null) {

                try {

                    Main.showMainWindow();

                    Main.showHome();

                } catch (IOException ex) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText("Login Fail");
                    alert.setContentText("Could not Load MainWindow");
                    alert.showAndWait();
                }
            }
        } else {
            lblAttempt.setText("Please Enter valid Username and Password");
            lblAttempt.setStyle(lblback);
        }
    }

    @FXML
    private void pswPasswordKP(KeyEvent event) throws SQLException {
        if (event.getCode() == KeyCode.ENTER) {
            btnLoginAP();
        }
    }
}

//</editor-fold>

