/*******************************************************************************
 *  Main.java
 *  Author: Richard Bennett
 *  Date: 2015-07-24
 *  
 *  Description: Creates a GUI from Container.java that uses functions from 
 *  SQLConnect.java to access and update a mySQL Database used in a Student
 *  Registration System.
 *  
 *  Libraries needed: mysql-connector-java.jar
 * 
 *  Requires: Container.java, SQLConnect.java
 *******************************************************************************/

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main
{
        public static void main(String args[]) {
        //Sets the appearance and Behavior of the GUI
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Container.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        //Creates the GUI
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Container().setVisible(true);
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
