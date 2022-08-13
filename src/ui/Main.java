package ui;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.Date;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

    public static Stage stage;
    public static Stage primaryStage;
    public static BorderPane mainLayout;

    @Override
    public void start(Stage primaryStage) throws IOException, ParseException {

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date expireDate = fmt.parse("2018-03-31");

        Date today = new Date();

        if (today.after(expireDate)) {
            try {
                mainLayout = FXMLLoader.load(Main.class.getResource("ExpireUI.fxml"));
                Scene scene = new Scene(mainLayout);
                primaryStage.setTitle("i Mobile Tec System - ********** Expired ***********");
                primaryStage.setScene(scene);

                primaryStage.show();
                stage = primaryStage;
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {
                mainLayout = FXMLLoader.load(Main.class.getResource("Login.fxml"));
                Scene scene = new Scene(mainLayout);
                primaryStage.setTitle("i Mobile Tec System - Login");
                primaryStage.setScene(scene);

                primaryStage.show();
                stage = primaryStage;
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("i Mobile Tec System");
        this.primaryStage.getIcons().add(new Image("/image/Icon.png"));

        this.primaryStage.setOnCloseRequest(e -> {
            e.consume();

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation System Exit");
            alert.setHeaderText("Exit the System");
            alert.setContentText("");

            ButtonType exit = new ButtonType("Exit");
            ButtonType cancel = new ButtonType("Cancel");

            alert.getButtonTypes().setAll(exit, cancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == exit) {

                System.exit(3);

            } else if (result.get() == cancel) {
                // ... Canceled
            }
        });
    }

    public static void showMainWindow() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("iMobileTec_MainWindow.fxml"));
        mainLayout = loader.load();
        Scene scene = new Scene(mainLayout);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showLoging() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Login.fxml"));
        BorderPane loging = loader.load();
        mainLayout.setCenter(loging);
    }

    public static void showHome() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Home.fxml"));
        BorderPane home = loader.load();
        mainLayout.setCenter(home);
    }

    public static void showOutlet() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Outlet.fxml"));
        BorderPane outlet = loader.load();
        mainLayout.setCenter(outlet);
    }

    public static void showOutletReturn() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("OutletReturn.fxml"));
        BorderPane outletReturn = loader.load();
        mainLayout.setCenter(outletReturn);
    }

    public static void showItemMan() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("ItemManagment.fxml"));
        BorderPane itemMan = loader.load();
        mainLayout.setCenter(itemMan);
    }

    public static void showEmployee() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Employee.fxml"));
        BorderPane employee = loader.load();
        mainLayout.setCenter(employee);
    }

    public static void showUser() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("User.fxml"));
        BorderPane user = loader.load();
        mainLayout.setCenter(user);
    }

    public static void showPrivilage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Privilage.fxml"));
        BorderPane privilage = loader.load();
        mainLayout.setCenter(privilage);
    }

    public static void showSupplier() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Supplier.fxml"));
        BorderPane supplier = loader.load();
        mainLayout.setCenter(supplier);
    }

    public static void showPorder() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("PurchaseOrderUI.fxml"));
        BorderPane porder = loader.load();
        mainLayout.setCenter(porder);
    }

    public static void showGRN() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("GRNote.fxml"));
        BorderPane grn = loader.load();
        mainLayout.setCenter(grn);
    }

    public static void showReport() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Report.fxml"));
        BorderPane report = loader.load();
        mainLayout.setCenter(report);
    }

    public static void showRepair() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("RepairManagment.fxml"));
        BorderPane repair = loader.load();
        mainLayout.setCenter(repair);
    }
    
    public static void showChart() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Chart.fxml"));
        BorderPane chart = loader.load();
        mainLayout.setCenter(chart);
    }

    public static void showNewSupplier() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Supplier.fxml"));
        BorderPane newSupplier = loader.load();

        Stage addSupplier = new Stage();
        addSupplier.setTitle("Supplier Management");
        addSupplier.initModality(Modality.WINDOW_MODAL);
        addSupplier.initOwner(primaryStage);
        Scene scene = new Scene(newSupplier);
        addSupplier.setScene(scene);
        addSupplier.showAndWait();
    }

    public static void showItemType() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("ItemType.fxml"));
        AnchorPane newItemType = loader.load();

        Stage addItemType = new Stage();
        addItemType.setTitle("Item Type Management");
        addItemType.initModality(Modality.WINDOW_MODAL);
        addItemType.initOwner(primaryStage);
        Scene scene = new Scene(newItemType);
        addItemType.setScene(scene);
        addItemType.showAndWait();
    }

    public static void showItemBrand() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("ItemBrand.fxml"));
        AnchorPane newItemBrand = loader.load();

        Stage addItemBrand = new Stage();
        addItemBrand.setTitle("Item Brand Management");
        addItemBrand.initModality(Modality.WINDOW_MODAL);
        addItemBrand.initOwner(primaryStage);
        Scene scene = new Scene(newItemBrand);
        addItemBrand.setScene(scene);
        addItemBrand.showAndWait();
    }

    public static void showDesignation() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Designation.fxml"));
        AnchorPane newDesignation = loader.load();

        Stage addDesignation = new Stage();
        addDesignation.setTitle("Designation Management");
        addDesignation.initModality(Modality.WINDOW_MODAL);
        addDesignation.initOwner(primaryStage);
        Scene scene = new Scene(newDesignation);
        addDesignation.setScene(scene);
        addDesignation.showAndWait();
    }
    
    public static void showFault() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Fault.fxml"));
        AnchorPane newFault = loader.load();
        
        Stage addFault = new Stage();
        addFault.setTitle("Fault Management");
        addFault.initModality(Modality.WINDOW_MODAL);
        addFault.initOwner(primaryStage);
        Scene scene = new Scene(newFault);
        addFault.setScene(scene);
        addFault.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
