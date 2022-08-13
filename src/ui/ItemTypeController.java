/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.DaoException;
import dao.ItemDao;
import dao.ItemtypeDao;
import entity.Itemtype;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

public class ItemTypeController implements Initializable {

    Itemtype itemtype;

    Itemtype oldItemtype;

    private String valid;
    private String invalid;
    private String updated;
    private String initial;

    @FXML
    private TextField txtItemType;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private ListView<Itemtype> lstItemType;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadForm();
    }

    private void loadForm() {

        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;

        itemtype = new Itemtype();
        oldItemtype = null;

        lstItemType.getItems().clear();
        lstItemType.setItems(ItemtypeDao.getAll());

        txtItemType.setText("");

        dissableButtons(false, false, true, true);
        setStyle(initial);
    }

    private void dissableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilage.get("Itemtype_insert"));
        btnUpdate.setDisable(update || !privilage.get("Itemtype_update"));
        btnDelete.setDisable(delete || !privilage.get("Itemtype_delete"));
    }

    private void setStyle(String style) {
        txtItemType.setStyle(style);
    }

    @FXML
    private void txtItemTypeKR(KeyEvent event) {
        if (itemtype.setName(txtItemType.getText().trim())) {
            if (oldItemtype != null && !itemtype.getName().equals(oldItemtype.getName())) {
                txtItemType.setStyle(updated);
            } else {
                txtItemType.setStyle(valid);
            }
        } else {
            txtItemType.setStyle(invalid);
        }
    }

    @FXML
    private void btnAddAP(ActionEvent event) throws DaoException {
        String errors = getErrors();

        if (errors.isEmpty()) {

            String confermation = "Ara you sure you need to add this Type \n "
                    + "\nItem Type :         \t\t" + itemtype.getName();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Add Item Type with following Details");
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
                tray.setMessage("Item Type " + itemtype.getName() + " Saved.");
                tray.setRectangleFill(Paint.valueOf("#00b84c"));
                tray.showAndWait();
                tray.setImage(rightImg);
                tray.showAndDismiss(Duration.seconds(4));

                ItemtypeDao.add(itemtype);
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
                alert.setHeaderText("Item Type Update");
                alert.setContentText("Update Item Type " + itemtype.getName());

                ButtonType ok = new ButtonType("UPDATE");
                ButtonType no = new ButtonType("DON'T");

                alert.getButtonTypes().setAll(ok, no);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ok) {

                    //----------- Notification Success -------------
                    TrayNotification tray = new TrayNotification();
                    Image rightImg = new Image("/image/NotifyIcon/Right.png");
                    tray.setTitle("SUCCESS");
                    tray.setMessage("Item Type " + itemtype.getName() + " updated.");
                    tray.setRectangleFill(Paint.valueOf("#00b84c"));
                    tray.showAndWait();
                    tray.setImage(rightImg);
                    tray.showAndDismiss(Duration.seconds(4));

                    ItemtypeDao.update(itemtype);
                    loadForm();
                } else if (result.get() == no) {
                    // ... Canceled
                }
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText("Item Type Update");
                alert.setContentText("Nothing Updated");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("Item Type Update");
            alert.setContentText("Item Type not updated!");
            alert.showAndWait();
        }
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Item Type Delete");
        alert.setContentText("Delete Item Type " + itemtype.getName());

        ButtonType ok = new ButtonType("DELETE");
        ButtonType no = new ButtonType("DON'T");

        alert.getButtonTypes().setAll(ok, no);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ok) {

            if (ItemDao.getAllByType(itemtype) == null) {
                ItemtypeDao.delete(itemtype);
                loadForm();

                //----------- Notification Success -------------
                TrayNotification tray = new TrayNotification();
                Image rightImg = new Image("/image/NotifyIcon/Right.png");
                tray.setTitle("SUCCESS");
                tray.setMessage("Item Type " + itemtype.getName() + " deleted.");
                tray.setRectangleFill(Paint.valueOf("#00b84c"));
                tray.showAndWait();
                tray.setImage(rightImg);
                tray.showAndDismiss(Duration.seconds(4));

            } else {
                Alert alert1 = new Alert(Alert.AlertType.WARNING);
                alert1.setTitle("Item Type Delete");
                alert1.setHeaderText(oldItemtype.getName() + " Delete ?");
                alert1.setContentText("You can't delete\n" + oldItemtype.getName() + " Items are in the system");
                alert1.showAndWait();
            }
        } else if (result.get() == no) {
        } else {
            Alert alert1 = new Alert(Alert.AlertType.WARNING);
            alert1.setTitle("Item Type Delete");
            alert1.setHeaderText(oldItemtype.getName() + " Delete ?");
            alert1.setContentText("You can't delete\nSome of the fields are updated");
            alert1.showAndWait();
        }
    }

    @FXML
    private void lstItemTypeMC(MouseEvent event) {
        fillForm();
    }

    private String getErrors() {

        String errors = "";

        try {

            if (itemtype.getName() == null) {
                errors = errors + "Item Type \t\tis Invalid\n";

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

            if (oldItemtype != null) {

                if (itemtype.getName() != null && !itemtype.getName().equals(oldItemtype.getName())) {
                    updates = updates + oldItemtype.getName() + " chnaged to " + itemtype.getName() + "\n";
                }
            }
        } catch (Exception e) {
            
            //----------- Notification Unsuccess-------------
                    TrayNotification tray = new TrayNotification();
                    Image wrongImg = new Image("/image/NotifyIcon/Wrong.png");
                    tray.setTitle("UN-SUCCESS");
                    tray.setMessage("Item Type " + itemtype.getName() + " not Updated. \n" + e.getClass());
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
        if (lstItemType.getItems() != null) {
            dissableButtons(false, true, false, false);
            setStyle(valid);

            oldItemtype = ItemtypeDao.getById(lstItemType.getSelectionModel().getSelectedItem().getId());
            itemtype = ItemtypeDao.getById(lstItemType.getSelectionModel().getSelectedItem().getId());

            txtItemType.setText(itemtype.getName());
        }
    }
}
