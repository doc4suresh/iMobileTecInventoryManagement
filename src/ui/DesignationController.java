/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.DaoException;
import dao.DesignationDao;
import dao.EmployeeDao;
import entity.Designation;
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

/**
 * FXML Controller class
 *
 * @author USER
 */
public class DesignationController implements Initializable {

    Designation designation;

    Designation oldDesignation;

    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    @FXML
    private TextField txtDesignation;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private ListView<Designation> lstDesignation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadForm();
    }

    private void loadForm() {

        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;

        designation = new Designation();
        oldDesignation = null;

        lstDesignation.getItems().clear();
        lstDesignation.setItems(DesignationDao.getAll());

        txtDesignation.setText("");

        dissableButtons(false, false, true, true);
        setStyle(initial);
    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Designation_insert"));
        btnUpdate.setDisable(update || !privilage.get("Designation_update"));
        btnDelete.setDisable(delete || !privilage.get("Designation_delete"));
    }

    private void setStyle(String style) {
        txtDesignation.setStyle(style);
    }

    @FXML
    private void txtDesignationKR(KeyEvent event) {
        if (designation.setName(txtDesignation.getText().trim())) {
            if (oldDesignation != null && !designation.getName().equals(oldDesignation.getName())) {
                txtDesignation.setStyle(updated);
            } else {
                txtDesignation.setStyle(valid);
            }
        } else {
            txtDesignation.setStyle(invalid);
        }
    }

    private String getErrors() {

        String errors = "";

        try {

            if (designation.getName() == null) {
                errors = errors + "Designation \t\tis Invalid\n";

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

            if (oldDesignation != null) {

                if (designation.getName() != null && !designation.getName().equals(oldDesignation.getName())) {
                    updates = updates + oldDesignation.getName() + " chnaged to " + designation.getName() + "\n";
                }
            }
        } catch (Exception e) {

            System.out.println(e.getClass());
            JOptionPane.showMessageDialog(null, e.getClass(), "Update Cheking Error", JOptionPane.ERROR_MESSAGE);
        }
        return updates;
    }

    private void fillForm() {
        if (lstDesignation.getItems() != null) {
            dissableButtons(false, true, false, false);
            setStyle(valid);

            oldDesignation = DesignationDao.getById(lstDesignation.getSelectionModel().getSelectedItem().getId());
            designation = DesignationDao.getById(lstDesignation.getSelectionModel().getSelectedItem().getId());

            txtDesignation.setText(designation.getName());
        }
    }

    @FXML
    private void btnAddAP(ActionEvent event) throws DaoException {
        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "Ara you sure you need to add this Designation \n "
                    + "\nDesignation :         \t\t" + designation.getName();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Add Designation with following Details");
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
                tray.setMessage("Designation " + designation.getName() + " Saved.");
                tray.setRectangleFill(Paint.valueOf("#00b84c"));
                tray.showAndWait();
                tray.setImage(rightImg);
                tray.showAndDismiss(Duration.seconds(4));

                DesignationDao.add(designation);
                loadForm();
            } else if (result.get() == no) {
            }
        }
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
    private void btnUpdateAP(ActionEvent event) {
        String errors = getErrors();

        if (errors.isEmpty()) {

            String updates = getUpdates();

            if (!updates.isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Designation Update");
                alert.setContentText("Update Designation " + designation.getName());

                ButtonType ok = new ButtonType("UPDATE");
                ButtonType no = new ButtonType("DON'T");

                alert.getButtonTypes().setAll(ok, no);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ok) {
                    
                    try {
                        DesignationDao.update(designation);

                        //----------- Notification Success -------------
                        TrayNotification tray = new TrayNotification();
                        Image rightImg = new Image("/image/NotifyIcon/Right.png");
                        tray.setTitle("SUCCESS");
                        tray.setMessage("Designation " + designation.getName() + " updated.");
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
                        tray.setMessage("Designation " + designation.getName() + " not Updated!\n" + ex);
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
                alert.setHeaderText("Designation Update");
                alert.setContentText("Nothing Updated");
                alert.showAndWait();
            }
        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("Designation Update");
            alert.setContentText("Designation not updated!");
            alert.showAndWait();
        }
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Designation Delete");
        alert.setContentText("Delete Designation " + designation.getName());

        ButtonType ok = new ButtonType("DELETE");
        ButtonType no = new ButtonType("DON'T");

        alert.getButtonTypes().setAll(ok, no);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ok) {

            if (EmployeeDao.getAllByDesignation(designation) == null) {
                DesignationDao.delete(designation);
                loadForm();

                //----------- Notification Success -------------
                TrayNotification tray = new TrayNotification();
                Image rightImg = new Image("/image/NotifyIcon/Right.png");
                tray.setTitle("SUCCESS");
                tray.setMessage("Designation " + designation.getName() + " deleted.");
                tray.setRectangleFill(Paint.valueOf("#00b84c"));
                tray.showAndWait();
                tray.setImage(rightImg);
                tray.showAndDismiss(Duration.seconds(4));

            } else {
                Alert alert1 = new Alert(Alert.AlertType.WARNING);
                alert1.setTitle("Designation Delete");
                alert1.setHeaderText(oldDesignation.getName() + " Delete?");
                alert1.setContentText("You can't delete\n" + oldDesignation.getName() + " Designations are in the system");
                alert1.showAndWait();
            }

        }else if (result.get() == no) {}
    }

    @FXML
    private void lstDesignationMC(MouseEvent event) {
        fillForm();
    }

}
