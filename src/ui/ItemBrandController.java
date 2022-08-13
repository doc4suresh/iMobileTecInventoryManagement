/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.BrandDao;
import dao.DaoException;
import dao.ItemDao;
import entity.Brand;
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
public class ItemBrandController implements Initializable {

    Brand brand;

    Brand oldBrand;

    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    @FXML
    private TextField txtBrand;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private ListView<Brand> lstBrand;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadForm();
    }

    private void loadForm() {

        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;

        brand = new Brand();
        oldBrand = null;

        lstBrand.getItems().clear();
        lstBrand.setItems(BrandDao.getAll());

        txtBrand.setText("");

        dissableButtons(false, false, true, true);
        setStyle(initial);
    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Brand_insert"));
        btnUpdate.setDisable(update || !privilage.get("Brand_update"));
        btnDelete.setDisable(delete || !privilage.get("Brand_delete"));
    }

    private void setStyle(String style) {
        txtBrand.setStyle(style);
    }

    @FXML
    private void txtBrandKR(KeyEvent event) {
        if (brand.setName(txtBrand.getText().trim())) {
            if (oldBrand != null && !brand.getName().equals(oldBrand.getName())) {
                txtBrand.setStyle(updated);
            } else {
                txtBrand.setStyle(valid);
            }
        } else {
            txtBrand.setStyle(invalid);
        }
    }

    @FXML
    private void btnAddAP(ActionEvent event) throws DaoException {
        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "Ara you sure you need to add this Brand \n "
                    + "\nBrand :         \t\t" + brand.getName();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Add Brand with following Details");
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
                tray.setMessage("Brand " + brand.getName() + " Saved.");
                tray.setRectangleFill(Paint.valueOf("#00b84c"));
                tray.showAndWait();
                tray.setImage(rightImg);
                tray.showAndDismiss(Duration.seconds(4));

                BrandDao.add(brand);
                loadForm();
            } else if (result.get() == no) {
            }
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
                alert.setHeaderText("Brand Update");
                alert.setContentText("Update Brand " + brand.getName());

                ButtonType ok = new ButtonType("UPDATE");
                ButtonType no = new ButtonType("DON'T");

                alert.getButtonTypes().setAll(ok, no);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ok) {

                    //----------- Notification Success -------------
                    TrayNotification tray = new TrayNotification();
                    Image rightImg = new Image("/image/NotifyIcon/Right.png");
                    tray.setTitle("SUCCESS");
                    tray.setMessage("Brand " + brand.getName() + " updated.");
                    tray.setRectangleFill(Paint.valueOf("#00b84c"));
                    tray.showAndWait();
                    tray.setImage(rightImg);
                    tray.showAndDismiss(Duration.seconds(4));

                    BrandDao.update(brand);
                    loadForm();
                    
                } else if (result.get() == no) {
                    // ... Canceled
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText("Brand Update");
                alert.setContentText("Nothing Updated");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("Brand Update");
            alert.setContentText("Brand not updated!");
            alert.showAndWait();
        }
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Brand Delete");
        alert.setContentText("Delete Brand " + brand.getName());

        ButtonType ok = new ButtonType("DELETE");
        ButtonType no = new ButtonType("DON'T");

        alert.getButtonTypes().setAll(ok, no);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ok) {

            if (ItemDao.getAllByBrand(brand) == null) {
                BrandDao.delete(brand);
                loadForm();

                //----------- Notification Success -------------
                TrayNotification tray = new TrayNotification();
                Image rightImg = new Image("/image/NotifyIcon/Right.png");
                tray.setTitle("SUCCESS");
                tray.setMessage("Brand " + brand.getName() + " deleted.");
                tray.setRectangleFill(Paint.valueOf("#00b84c"));
                tray.showAndWait();
                tray.setImage(rightImg);
                tray.showAndDismiss(Duration.seconds(4));

            } else {
                Alert alert1 = new Alert(Alert.AlertType.WARNING);
                alert1.setTitle("Error Message");
                alert1.setHeaderText("Brand Delete");
                alert1.setContentText("You can't delete\n" + oldBrand.getName() + " Items are in the system");
                alert1.showAndWait();
            }

        } else if (result.get() == no) {
        } else {
            Alert alert1 = new Alert(Alert.AlertType.WARNING);
            alert1.setTitle("Brand Delete");
            alert1.setHeaderText(oldBrand.getName() + " Delete ?");
            alert1.setContentText("You can't delete\nSome of the fields are updated");
            alert1.showAndWait();
        }
    }

    @FXML
    private void lstBandMC(MouseEvent event) {
        fillForm();
    }

    private String getErrors() {

        String errors = "";

        try {

            if (brand.getName() == null) {
                errors = errors + "Brand \t\tis Invalid\n";

            }
        } catch (Exception e) {
            //----------- Notification Unsuccess-------------
                    TrayNotification tray = new TrayNotification();
                    Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                    tray.setTitle("UN-SUCCESS");
                    tray.setMessage("Brand " + brand.getName() + " Error Cheking Error.\n" + e.getClass());
                    tray.setRectangleFill(Paint.valueOf("#dc0000"));
                    tray.showAndWait();
                    tray.setImage(wrongImg);
                    tray.showAndDismiss(Duration.seconds(4));
                    
            System.out.println(e.getClass());
            JOptionPane.showMessageDialog(null, e.getClass(), "Error Cheking Error", JOptionPane.ERROR_MESSAGE);
        }
        return errors;
    }

    private String getUpdates() {

        String updates = "";

        try {

            if (oldBrand != null) {

                if (brand.getName() != null && !brand.getName().equals(oldBrand.getName())) {
                    updates = updates + oldBrand.getName() + " chnaged to " + brand.getName() + "\n";
                }
            }
        } catch (Exception e) {
            //----------- Notification Unsuccess-------------
                    TrayNotification tray = new TrayNotification();
                    Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                    tray.setTitle("UN-SUCCESS");
                    tray.setMessage("Brand " + brand.getName() + " not Updated!\n" + e.getClass());
                    tray.setRectangleFill(Paint.valueOf("#dc0000"));
                    tray.showAndWait();
                    tray.setImage(wrongImg);
                    tray.showAndDismiss(Duration.seconds(4));
                    
            System.out.println(e.getClass());
            JOptionPane.showMessageDialog(null, e.getClass(), "Update Cheking Error", JOptionPane.ERROR_MESSAGE);
        }
        return updates;
    }

    private void fillForm() {
        if (lstBrand.getItems() != null) {
            dissableButtons(false, true, false, false);
            setStyle(valid);

            oldBrand = BrandDao.getById(lstBrand.getSelectionModel().getSelectedItem().getId());
            brand = BrandDao.getById(lstBrand.getSelectionModel().getSelectedItem().getId());

            txtBrand.setText(brand.getName());
        }
    }
}
