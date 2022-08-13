/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.controlsfx.dialog.Dialogs;
import report.ReportView;

/**
 * FXML Controller class
 *
 * @author SahaN
 */
public class ReportByRegnoController implements Initializable {

    @FXML
    private TextField txtRegno;

    ResourceBundle rb;
    @FXML
    private Button btnShow;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void txtRegnoKR(KeyEvent event) {
     

    }

    @FXML
    private void btnShowAP(ActionEvent event) {

     

                if (txtRegno.getText() != null) {
                    HashMap hashMap = new HashMap();
                    String reg = txtRegno.getText();

                    hashMap.put("reg", reg);

                    new ReportView("/report/Student.jasper", hashMap);
                    
                    txtRegno.setText("");
                } else {
                    Dialogs.create().title("Error Message").message("Please Enter valid Reg No").showError();

                }

            }
   

    

    @FXML
    private void btnShowKP(KeyEvent event) {
    }

}
