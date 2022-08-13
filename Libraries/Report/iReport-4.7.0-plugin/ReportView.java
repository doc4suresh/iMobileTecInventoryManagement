package report;

import dao.HibernateUtil;
import java.awt.Toolkit;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;
import org.hibernate.internal.SessionImpl;


/**
 *
 * @author Hasitha
 */
public class ReportView {
    Connection connection = null;

    // default constructor
    public ReportView() {
    }

    // constructor with String parameter
    public ReportView(String fileName) {
        this(fileName, null);
    }

    // constructor with String & Hashmap parameter
    public ReportView(String fileName, Map hashMap) {

        initConnection();
//        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        try {
            InputStream reportStream = this.getClass().getResourceAsStream(fileName);
            //Fill the report with parameter, connection and the stream reader
            JasperPrint jp = JasperFillManager.fillReport(reportStream, hashMap, connection);

            //Viewer for JasperReport
            JRViewer jv = new JRViewer(jp);
            //Insert viewer to a JFrame to make it showable
            JFrame jf = new JFrame();
            jf.getContentPane().add(jv); 
          //  jf.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("windows/rlanka/images/RLogo.png")));
            jf.validate();
            jf.setVisible(true);
            jf.setSize(Toolkit.getDefaultToolkit().getScreenSize());
            jf.setLocation(0, 0);
            jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        } catch (JRException ex) {
           
            System.out.println(ex.getMessage());
        }
    }
    
    // Connection get from hibernate
    public void initConnection() {

//        try {
////            Class.forName("com.mysql.jdbc.Driver");
//            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
//            SessionFactoryImplementor sessionFacotoryImplementer = (SessionFactoryImplementor)sessionFactory;
//            ConnectionProvider connectionProvider = (ConnectionProvider) sessionFacotoryImplementer.getConnectionProvider();
//            connection =connectionProvider.getConnection();
//         
//
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//        }
        
        SessionImpl sessionImpl = (SessionImpl)HibernateUtil.getSessionFactory().openSession(); 
        connection = sessionImpl.connection();
        
    }
    
}
