/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.ItemDao;
import entity.Item;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import report.ReportView;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class ReportController implements Initializable {

    @FXML
    private DatePicker dtpProfitStart;
    @FXML
    private DatePicker dtpProfitEnd;
    @FXML
    private DatePicker dtpSaleEnd;
    @FXML
    private DatePicker dtpSaleStart;
    @FXML
    private DatePicker dtpRepairStart;
    @FXML
    private DatePicker dtpRepairEnd;
    @FXML
    private DatePicker dtpPOrderEnd;
    @FXML
    private DatePicker dtpPOrderStart;
    @FXML
    private DatePicker dtpGRNStart;
    @FXML
    private DatePicker dtpGRNEnd;
    @FXML
    private ComboBox<Item> cmbItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbItem.setItems(ItemDao.getAll());
        cmbItem.getSelectionModel().select(-1);
    }

    @FXML
    private void btnProfitAP(ActionEvent event) {
        if (dtpProfitStart.getValue() != null && dtpProfitEnd.getValue() != null) {
            java.sql.Date fromDate = java.sql.Date.valueOf(dtpProfitStart.getValue());
            java.sql.Date toDate = java.sql.Date.valueOf(dtpProfitEnd.getValue());
            int itemId = cmbItem.getSelectionModel().getSelectedItem().getId();

            String path = "/report/SalesProfitByDate.jasper";
            HashMap<String, Object> param = new HashMap<>();
            param.put("itemId", itemId);
            param.put("FromDate", fromDate);
            param.put("ToDate", toDate);
            
            new ReportView(path, param);

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Sales Profit Report");
            alert.setHeaderText("Dates not set");
            alert.setContentText("Enter valid 'From' & 'To' dates \n\tFrom \tMM/DD/YYYY \n\tTo \t\tMM/DD/YYYY");

            alert.showAndWait();
        }
    }

    @FXML
    private void btnStockAP(ActionEvent event) {
        new ReportView("/report/StockReport.jasper");
    }

    @FXML
    private void btnSupplierAP(ActionEvent event) {
        new ReportView("/report/SupplierReport.jasper");
    }

    @FXML
    private void btnEmpAP(ActionEvent event) {
        new ReportView("/report/EmpReport.jasper");
    }

    @FXML
    private void btnSaleAP(ActionEvent event) {
        if (dtpSaleStart.getValue() != null && dtpSaleEnd.getValue() != null) {
            java.sql.Date fromDate = java.sql.Date.valueOf(dtpSaleStart.getValue());
            java.sql.Date toDate = java.sql.Date.valueOf(dtpSaleEnd.getValue());

            String path = "/report/SalesReportByDate.jasper";
            HashMap<String, Date> param = new HashMap<>();
            param.put("FromDate", fromDate);
            param.put("ToDate", toDate);

            new ReportView(path, param);

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Sales Report");
            alert.setHeaderText("Dates not set");
            alert.setContentText("Enter valid 'From' & 'To' dates \n\tFrom \tMM/DD/YYYY \n\tTo \t\tMM/DD/YYYY");

            alert.showAndWait();
        }
    }

    @FXML
    private void btnRepairAP(ActionEvent event) {
        if (dtpRepairStart.getValue() != null && dtpRepairEnd.getValue() != null) {
            java.sql.Date fromDate = java.sql.Date.valueOf(dtpRepairStart.getValue());
            java.sql.Date toDate = java.sql.Date.valueOf(dtpRepairEnd.getValue());

            String path = "/report/RepairReportByDate.jasper";
            HashMap<String, Date> param = new HashMap<>();
            param.put("FromDate", fromDate);
            param.put("ToDate", toDate);

            new ReportView(path, param);

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Repair Profit Report");
            alert.setHeaderText("Dates not set");
            alert.setContentText("Enter valid 'From' & 'To' dates \n\tFrom \tMM/DD/YYYY \n\tTo \t\tMM/DD/YYYY");

            alert.showAndWait();
        }
    }

    @FXML
    private void btnPOrderAP(ActionEvent event) {

        if (dtpPOrderStart.getValue() != null && dtpPOrderEnd.getValue() != null) {
            java.sql.Date fromDate = java.sql.Date.valueOf(dtpPOrderStart.getValue());
            java.sql.Date toDate = java.sql.Date.valueOf(dtpPOrderEnd.getValue());

            String path = "/report/POReportByDate.jasper";
            HashMap<String, Date> param = new HashMap<>();
            param.put("FromDate", fromDate);
            param.put("ToDate", toDate);

            new ReportView(path, param);

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Purchase Order Report");
            alert.setHeaderText("Dates not set");
            alert.setContentText("Enter valid 'From' & 'To' dates \n\tFrom \tMM/DD/YYYY \n\tTo \t\tMM/DD/YYYY");

            alert.showAndWait();
        }
    }

    @FXML
    private void btnGRNAP(ActionEvent event) {

        if (dtpGRNStart.getValue() != null && dtpGRNEnd.getValue() != null) {
            java.sql.Date fromDate = java.sql.Date.valueOf(dtpGRNStart.getValue());
            java.sql.Date toDate = java.sql.Date.valueOf(dtpGRNEnd.getValue());

            String path = "/report/GRNReportByDate.jasper";
            HashMap<String, Date> param = new HashMap<>();
            param.put("FromDate", fromDate);
            param.put("ToDate", toDate);

            new ReportView(path, param);

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Good Recive Note Report");
            alert.setHeaderText("Dates not set");
            alert.setContentText("Enter valid 'From' & 'To' dates \n\tFrom \tMM/DD/YYYY \n\tTo \t\tMM/DD/YYYY");

            alert.showAndWait();
        }
    }

    @FXML
    private void btnHomeAP() throws IOException {
        System.gc();
        Main.showHome();
    }
}
