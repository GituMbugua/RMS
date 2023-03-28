/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package booking;

import java.awt.Font;
import javax.swing.UIManager;

/**
 *
 * @author Gitu
 */
public class Booking {
    public static void main(String[] args) {
        //UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("Century Gothic", Font.PLAIN, 16));
        LoginForm login = new LoginForm();
        
        login.setLocationRelativeTo(null);
        login.setVisible(true);
        
    }
}
