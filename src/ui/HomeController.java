package ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class HomeController implements Initializable {

    private Main main;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void btnOutletAP() throws IOException {
        Main.showOutlet();
    }

    @FXML
    private void btnEmployeeAp() throws IOException {
        Main.showEmployee();
    }

    @FXML
    private void btnUserAP() throws IOException {
        Main.showUser();
    }

    @FXML
    private void btnItemManagmentAP() throws IOException {
        Main.showItemMan();
    }

    @FXML
    private void btnPrivilageAP() throws IOException {
        Main.showPrivilage();
    }

    @FXML
    private void btnSpplierAP() throws IOException {
        Main.showSupplier();
    }

    @FXML
    private void btnPoderAP() throws IOException {
        Main.showPorder();
    }

    @FXML
    private void btnGRNAP() throws IOException {
        Main.showGRN();
    }

    @FXML
    private void btnReportAP() throws IOException {
        Main.showReport();
    }

    @FXML
    private void btnRepairAP() throws IOException {
        Main.showRepair();
    }

    @FXML
    private void btnChartAP(ActionEvent event) throws IOException {
        Main.showChart();
    }

    @FXML
    private void btnGuideAP(ActionEvent event) {
    }

}
