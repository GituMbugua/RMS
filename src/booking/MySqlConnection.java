/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package booking;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Gitu
 */
public class MySqlConnection {
    public static Connection myConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String database = "jdbc:mysql://localhost:3306/rondo";
            con = DriverManager.getConnection(database, "root", "");
        }
        catch (Exception ex) {
            System.out.println("Error. " + ex);
        }
        return con;
    }
}
