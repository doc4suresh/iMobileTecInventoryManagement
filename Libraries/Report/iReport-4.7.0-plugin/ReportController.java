/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.dialog.Dialog; 

import org.controlsfx.dialog.Dialogs;
import report.ReportView;
import util.MyResourceBundle;

/**
 * FXML Controller class
 *
 * @author SahaN
 */
public class ReportController implements Initializable {

    private DatePicker doFrom;
    private DatePicker doTo;
    @FXML
    private Button btnPBD;
    @FXML
    private Button btnStudent;
    @FXML
    private Button btnBatch;
    @FXML
    private Button btnStuByBatch;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }


   

    @FXML
    private void btnPBDAP(ActionEvent event) {
            try {
            Dialog subUI = new Dialog(null, "Payment By Date Range");
            MyResourceBundle myResources = new MyResourceBundle();
            HashMap hm = new HashMap();

            hm.put("ui", subUI);
            myResources.setHashMap(hm);
            AnchorPane itemSearchUI = FXMLLoader.load(Main.class.getResource("PaymentByDateRange.fxml"), myResources);
            subUI.setContent(itemSearchUI);
            subUI.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnStudentAP(ActionEvent event) {
            try {
            Dialog subUI = new Dialog(null, "Student ByReg no");
            MyResourceBundle myResources = new MyResourceBundle();
            HashMap hm = new HashMap();

            hm.put("ui", subUI);
            myResources.setHashMap(hm);
            AnchorPane itemSearchUI = FXMLLoader.load(Main.class.getResource("ReportByRegno.fxml"), myResources);
            subUI.setContent(itemSearchUI);
            subUI.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnBatchAP(ActionEvent event) {
              try {
            Dialog subUI = new Dialog(null, "Batch ByReg no");
            MyResourceBundle myResources = new MyResourceBundle();
            HashMap hm = new HashMap();

            hm.put("ui", subUI);
            myResources.setHashMap(hm);
            AnchorPane itemSearchUI = FXMLLoader.load(Main.class.getResource("BatchReportByRegno.fxml"), myResources);
            subUI.setContent(itemSearchUI);
            subUI.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnStuByBatchAP(ActionEvent event) {
               try {
            Dialog subUI = new Dialog(null, "Student By Batch");
            MyResourceBundle myResources = new MyResourceBundle();
            HashMap hm = new HashMap();

            hm.put("ui", subUI);
            myResources.setHashMap(hm);
            AnchorPane itemSearchUI = FXMLLoader.load(Main.class.getResource("StudentByBatchReport.fxml"), myResources);
            subUI.setContent(itemSearchUI);
            subUI.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
