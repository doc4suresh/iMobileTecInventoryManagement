/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import report.ReportView;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class ChartController implements Initializable {

    @FXML
    private DatePicker dtpRepairStart;
    @FXML
    private DatePicker dtpRepairEnd;
    @FXML
    private DatePicker dtpPOrderStart;
    @FXML
    private DatePicker dtpPOrderEnd;
    @FXML
    private Button btnRepairCh;
    @FXML
    private Button btnPOrderCh;
    @FXML
    private Button btnStockCh;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnRepairChAP(ActionEvent event) {
        if (dtpRepairStart.getValue() != null && dtpRepairEnd.getValue() != null) {
            java.sql.Date fromDate = java.sql.Date.valueOf(dtpRepairStart.getValue());
            java.sql.Date toDate = java.sql.Date.valueOf(dtpRepairEnd.getValue());

            String path = "/chart/RepairTypesDate.jasper";
            HashMap<String, Date> param = new HashMap<>();
            param.put("FromDate", fromDate);
            param.put("ToDate", toDate);

            new ReportView(path, param);

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Repair Chart");
            alert.setHeaderText("Dates not set");
            alert.setContentText("Enter valid 'From' & 'To' dates \n\tFrom \tMM/DD/YYYY \n\tTo \t\tMM/DD/YYYY");

            alert.showAndWait();
        }
    }

    @FXML
    private void btnPOrderChAP(ActionEvent event) {
        if (dtpPOrderStart.getValue() != null && dtpPOrderEnd.getValue() != null) {
            java.sql.Date fromDate = java.sql.Date.valueOf(dtpPOrderStart.getValue());
            java.sql.Date toDate = java.sql.Date.valueOf(dtpPOrderEnd.getValue());

            String path = "/chart/ChPerOrder.jasper";
            HashMap<String, Date> param = new HashMap<>();
            param.put("FromDate", fromDate);
            param.put("ToDate", toDate);

            new ReportView(path, param);

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Perchase Order Chart");
            alert.setHeaderText("Dates not set");
            alert.setContentText("Enter valid 'From' & 'To' dates \n\tFrom \tMM/DD/YYYY \n\tTo \t\tMM/DD/YYYY");

            alert.showAndWait();
        }
    }

    @FXML
    private void btnStockChAP(ActionEvent event) {
        new ReportView("/chart/chStock.jasper");
    }

    @FXML
    private void btnHomeAP(ActionEvent event) throws IOException {
        System.gc();
        Main.showHome();
    }
    
}
