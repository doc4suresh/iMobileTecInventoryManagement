/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.ItemDao;
import dao.RepairDao;
import entity.Brand;
import entity.Item;
import entity.Repair;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import static ui.LoginController.user;
import static ui.Main.primaryStage;
import static ui.Main.stage;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class IMobileTec_MainWindowController implements Initializable {

    public static File lastDirectory;

    //Binding with the Form
    private Item item;
    private ObservableList<Item> items;

    private Repair repair;
    private ObservableList<Repair> repairs;

    private Main main;
    @FXML
    private Label lblUsername;
    @FXML
    private Button btnSignOut;
    @FXML
    private ImageView imgUser;
    @FXML
    private TableView<Item> tblReorder;
    @FXML
    private TableColumn<Item, Brand> colReorderBrand;
    @FXML
    private TableColumn<Item, String> colReorderItem;
    @FXML
    private TableColumn<Item, String> colReorderQty;
    @FXML
    private TableView<Repair> tblRepair;
    @FXML
    private TableColumn<Repair, String> colRepairItem;
    @FXML
    private TableColumn<Repair, String> colRepairDate;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblUsername.setText(user.getName());

        if (user.getEmployeeId().getImage() != null) {
            imgUser.setImage(new Image(new ByteArrayInputStream(user.getEmployeeId().getImage())));
        } else {
            imgUser.setImage((new Image("/image/p1.png")));
        }
        colReorderBrand.setCellValueFactory(new PropertyValueFactory("brandId"));
        colReorderItem.setCellValueFactory(new PropertyValueFactory("name"));
        colReorderQty.setCellValueFactory(new PropertyValueFactory("qty"));

        colRepairItem.setCellValueFactory(new PropertyValueFactory("item"));
        colRepairDate.setCellValueFactory(new PropertyValueFactory("agreeddate"));

        loadItemTbl();
        loadRepairTbl();
    }

    public void loadItemTbl() {
        this.items = ItemDao.getAllReorder(item);
        tblReorder.setItems(FXCollections.observableArrayList(items));
    }

    public void loadRepairTbl() {
        this.repairs = RepairDao.getAllImmediat(repair);
        tblRepair.setItems(FXCollections.observableArrayList(repairs));
    }

    @FXML
    private void btnSignOutAP() throws IOException {

        user = null;

        try {
            BorderPane root = FXMLLoader.load(Main.class.getResource("Login.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("i Mobile Tec System - Login");
            primaryStage.setScene(scene);

            primaryStage.show();
            stage = primaryStage;
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void lblReorderRefreshMC(MouseEvent event) {
        loadItemTbl();
    }

    @FXML
    private void lblRepairRefreshMC(MouseEvent event) {
        loadRepairTbl();
    }

}
