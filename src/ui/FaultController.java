
package ui;

import dao.DaoException;
import dao.Faults1Dao;
import dao.Faults2Dao;
import dao.RepairDao;
import entity.Faults1;
import entity.Faults2;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import tray.notification.TrayNotification;
import static ui.LoginController.privilage;


public class FaultController implements Initializable {
    
    Faults1 fault1;
    Faults2 fault2;
            
    Faults1 oldFault;
    
    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    @FXML
    private TextField txtFault;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;
    @FXML
    private ListView<Faults1> lstFault;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadForm();
    }
    
    private void loadForm(){
        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;
        
        fault1 = new Faults1();
        fault2 = new Faults2();
        oldFault = null;
        
        lstFault.getItems().clear();
        lstFault.setItems(Faults1Dao.getAll());
        
        txtFault.setText("");

        dissableButtons(false, false, true, true);
        setStyle(initial);
    }
    
    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Repair_insert"));
        btnUpdate.setDisable(update || !privilage.get("Repair_update"));
        btnDelete.setDisable(delete || !privilage.get("Repair_delete"));
    }
    
    private void setStyle(String style) {
        txtFault.setStyle(style);
    }

    private String getErrors() {

        String errors = "";

        try {

            if (fault1.getName() == null) {
                errors = errors + "Fault \t\tis Invalid\n";

            }
        } catch (Exception e) {
            System.out.println(e.getClass());
            JOptionPane.showMessageDialog(null, e.getClass(), "Error Cheking Error", JOptionPane.ERROR_MESSAGE);
        }
        return errors;
    }
    
    private String getUpdates() {

        String updates = "";

        try {

            if (oldFault != null) {

                if (fault1.getName() != null && !fault1.getName().equals(oldFault.getName())) {
                    updates = updates + oldFault.getName() + " chnaged to " + fault1.getName() + "\n";
                }
            }
        } catch (Exception e) {

            System.out.println(e.getClass());
            JOptionPane.showMessageDialog(null, e.getClass(), "Update Cheking Error", JOptionPane.ERROR_MESSAGE);
        }
        return updates;
    }
    
    private void fillForm() {
        if (lstFault.getItems() != null) {
            dissableButtons(false, true, false, false);
            setStyle(valid);

            oldFault = Faults1Dao.getById(lstFault.getSelectionModel().getSelectedItem().getId());
            fault1 = Faults1Dao.getById(lstFault.getSelectionModel().getSelectedItem().getId());
            fault2 = Faults2Dao.getById(lstFault.getSelectionModel().getSelectedItem().getId());

            txtFault.setText(fault1.getName());
        }
    }

    @FXML
    private void btnAddAP(ActionEvent event) throws DaoException {
        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "Ara you sure you need to add this Designation \n "
                    + "\nFault :         \t\t" + fault1.getName();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Add Fault with following Details");
            alert.setContentText(confermation);

            ButtonType ok = new ButtonType("ADD");
            ButtonType no = new ButtonType("DON'T");

            alert.getButtonTypes().setAll(ok, no);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ok) {

                //----------- Notification Success -------------
                TrayNotification tray = new TrayNotification();
                Image rightImg = new Image("/image/NotifyIcon/Right.png");
                tray.setTitle("SUCCESS");
                tray.setMessage("Fault " + fault1.getName() + " Saved.");
                tray.setRectangleFill(Paint.valueOf("#00b84c"));
                tray.showAndWait();
                tray.setImage(rightImg);
                tray.showAndDismiss(Duration.seconds(4));

                Faults1Dao.add(fault1);
                Faults2Dao.add(fault2);
                loadForm();
            } else if (result.get() == no) {
            }
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
                alert.setHeaderText("Fault Update");
                alert.setContentText("Update Fault " + fault1.getName());

                ButtonType ok = new ButtonType("UPDATE");
                ButtonType no = new ButtonType("DON'T");

                alert.getButtonTypes().setAll(ok, no);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ok) {
                    
                    try {
                        Faults1Dao.update(fault1);
                        Faults2Dao.update(fault2);

                        //----------- Notification Success -------------
                        TrayNotification tray = new TrayNotification();
                        Image rightImg = new Image("/image/NotifyIcon/Right.png");
                        tray.setTitle("SUCCESS");
                        tray.setMessage("Fault " + fault1.getName() + " updated.");
                        tray.setRectangleFill(Paint.valueOf("#00b84c"));
                        tray.showAndWait();
                        tray.setImage(rightImg);
                        tray.showAndDismiss(Duration.seconds(4));

                        loadForm();
                    } catch (Exception ex) {
                        
                        //----------- Notification Unsuccess -------------
                        TrayNotification tray = new TrayNotification();
                        Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                        tray.setTitle("UN-SUCCESS");
                        tray.setMessage("Fault " + fault1.getName() + " not Updated!\n" + ex);
                        tray.setRectangleFill(Paint.valueOf("#dc0000"));
                        tray.showAndWait();
                        tray.setImage(wrongImg);
                        tray.showAndDismiss(Duration.seconds(4));
                    }
                } else if (result.get() == no) {
                    // ... Canceled
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information Message");
                alert.setHeaderText("Fault Update");
                alert.setContentText("Nothing Updated");
                alert.showAndWait();
            }
        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("Fault Update");
            alert.setContentText("Fault not updated!");
            alert.showAndWait();
        }
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Fault Delete");
        alert.setContentText("Delete Fault " + fault1.getName());

        ButtonType ok = new ButtonType("DELETE");
        ButtonType no = new ButtonType("DON'T");

        alert.getButtonTypes().setAll(ok, no);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ok) {

            if (RepairDao.getAllByFaults1(fault1) == null) {
                Faults1Dao.delete(fault1);
                Faults2Dao.delete(fault2);
                loadForm();

                //----------- Notification Success -------------
                TrayNotification tray = new TrayNotification();
                Image rightImg = new Image("/image/NotifyIcon/Right.png");
                tray.setTitle("SUCCESS");
                tray.setMessage("Fault " + fault1.getName() + " deleted.");
                tray.setRectangleFill(Paint.valueOf("#00b84c"));
                tray.showAndWait();
                tray.setImage(rightImg);
                tray.showAndDismiss(Duration.seconds(4));

            } else {
                Alert alert1 = new Alert(Alert.AlertType.WARNING);
                alert1.setTitle("Fault Delete");
                alert1.setHeaderText(oldFault.getName() + " Delete?");
                alert1.setContentText("You can't delete\n" + oldFault.getName() + " Faults are in the system");
                alert1.showAndWait();
            }

        }else if (result.get() == no) {}
    }

    @FXML
    private void btnClearAP(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Clear Text Field");
        alert.setContentText("Do you want to clear the Text Field?");

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
    private void txtFaultKR(KeyEvent event) {
        if (fault1.setName(txtFault.getText().trim()) && fault2.setName(txtFault.getText().trim())) {
            if (oldFault != null && !fault1.getName().equals(oldFault.getName())) {
                txtFault.setStyle(updated);
            } else {
                txtFault.setStyle(valid);
            }
        } else {
            txtFault.setStyle(invalid);
        }
    }

    @FXML
    private void lstFaultMC(MouseEvent event) {
        fillForm();
    }

    
}
