/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package booking;

import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gitu
 */
public class DashboardPanel extends javax.swing.JPanel {

    private Connection con = MySqlConnection.myConnection();
    private Statement st;
    private ResultSet rs;
    private DateFormatter df;
    private ArrayList<HashMap> arrivalsList;
    private ArrayList<HashMap> departuresList;
    private int countArrivals = 0;
    private int countDepartures = 0;
    
    /**
     * Creates new form DashboardPanel
     */
    public DashboardPanel() {
        initComponents();
        getArrivals();
        getDepartures();
        displayArrivals();
        displayDepartures();
        customInit();
    }
    
    private void customInit() {
        lblCurrentGuests.setText(String.valueOf(countCurrentGuests()));
        lblOccupiedRooms.setText(String.valueOf(countOccupiedRooms()));
        lblAvailableRooms.setText(String.valueOf(countAvailableRooms()));
        lblArrivals.setText(String.valueOf(countArrivals));
        lblDepartures.setText(String.valueOf(countDepartures));
        tblArrivals.getTableHeader().setFont(new Font("Corbel", Font.PLAIN, 18));
        tblDepartures.getTableHeader().setFont(new Font("Corbel", Font.PLAIN, 18));
    }
    
    private int countCurrentGuests(){
        int count = 0;
        try {
            String query = "SELECT COUNT(reservation_id) AS count_current_guests FROM `reservation` WHERE status='checked in'";
            
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashboardPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
            return count;
    }
    
    private int countOccupiedRooms(){
        int count = 0;
        try {
            String query = "SELECT COUNT(room_id) AS count_occupied_rooms FROM `room` WHERE is_available=false";
            st = con.createStatement();
            rs = st.executeQuery(query);

            if (rs.next()) {
                count = rs.getInt(1);
           }
        }
        catch (Exception ex) {
            Logger.getLogger(DashboardPanel.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error! Cannot fetch. " + ex);
        }
        return count;
    }
    
    private int countAvailableRooms(){
        int count = 0;
        try {
            String query = "SELECT COUNT(room_id) AS count_occupied_rooms FROM `room` WHERE is_available=true";
            st = con.createStatement();
            rs = st.executeQuery(query);

            if (rs.next()) {
                count = rs.getInt(1);
           }
        }
        catch (Exception ex) {
            Logger.getLogger(DashboardPanel.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error! Cannot fetch. " + ex);
        }
        return count;
    }
    
    private void getArrivals() {
        try {
            //myConnection();
            PreparedStatement p;
            arrivalsList = new ArrayList();
            df = new DateFormatter();

            Date date = Calendar.getInstance().getTime();
            String today = df.convertToLocalDateTime(date).toLocalDate().toString();

            String query = "SELECT reservation.reservation_id, guest.first_name, guest.surname, reservation.arrival_date FROM reservation LEFT JOIN guest ON reservation.guest_id=guest.guest_id WHERE arrival_date LIKE ?";

            p = con.prepareStatement(query);
            p.setString(1, today + "%");

            rs = p.executeQuery();
            
            HashMap<Object, Object> res;

            while (rs.next()) {
                int i = 1;
                
                res = new HashMap<>();
                res.put("reservationId", rs.getInt(i++));
                res.put("guestFirstName", rs.getString(i++));
                res.put("guestSurname", rs.getString(i++));
                res.put("arrivalDate", rs.getString(i++));
                
                arrivalsList.add(res);
            }
        }
        catch (Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error!");
        }
        countArrivals = arrivalsList.size();
    }
    
    public void displayArrivals() {
        DefaultTableModel model = (DefaultTableModel)tblArrivals.getModel();
        df = new DateFormatter();
        if (!arrivalsList.isEmpty()) {
            model.setRowCount(0);
            Object[] col = new Object[3];
            for (int i=0; i<arrivalsList.size(); i++) {
                LocalDateTime date = null;
                String arrivalTime;
                try {
                    date = df.stringToLocalDateTime(arrivalsList.get(i).get("arrivalDate").toString());
                } catch (ParseException ex) {
                    Logger.getLogger(DashboardPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NullPointerException ex) {
                    Logger.getLogger(DashboardPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                int hour = date.getHour();
                int min = date.getMinute();
                if (min == 0)
                    arrivalTime = hour + ":00";
                else
                    arrivalTime = hour + ":" + min;
                
                col[0] = arrivalsList.get(i).get("reservationId");
                col[1] = arrivalsList.get(i).get("guestFirstName") + " " + arrivalsList.get(i).get("guestSurname");
                col[2] = arrivalTime;


                model.addRow(col);
            }         
        } else {
            String noRecords = "No Arrival Records.";
            model.setRowCount(0);
            Object[] col = new Object[1];
            col[0] = noRecords;

            model.addRow(col);
        }
    }
    
    private void getDepartures() {
        try {
           // myConnection();
            PreparedStatement p;
            departuresList = new ArrayList();
            df = new DateFormatter();
        
            Date date = Calendar.getInstance().getTime();
            String today = df.convertToLocalDateTime(date).toLocalDate().toString();
            //String query = "SELECT * FROM `reservation` WHERE `departure_date` LIKE ?";
            String query = "SELECT reservation.reservation_id, guest.first_name, guest.surname, reservation.charges FROM reservation LEFT JOIN guest ON reservation.guest_id=guest.guest_id WHERE `departure_date` LIKE ?";
            p = con.prepareStatement(query);
            p.setString(1, today + "%");
            
            rs = p.executeQuery();
            
            HashMap<Object, Object> res;
            while (rs.next()) {
              //  res = new Reservation(rs.getInt("reservation_id"),rs.getString("booking_date"), rs.getString("arrival_date"), rs.getString("departure_date"), rs.getLong("num_of_nights"), rs.getInt("num_of_adults"),  rs.getInt("num_of_children_u12"), rs.getInt("num_of_children_teen"), rs.getDouble("charges"), rs.getString("status"), rs.getInt("guest_category"), rs.getInt("user_id"), rs.getInt("guest_id"), rs.getInt("accommodation_type"));
                int i = 1;
                
                res = new HashMap<>();
                res.put("reservationId", rs.getInt(i++));
                res.put("guestFirstName", rs.getString(i++));
                res.put("guestSurname", rs.getString(i++));
                res.put("charges", rs.getString(i++));
                
                departuresList.add(res);
            }
        }
        catch (Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error!");
        }
        countDepartures = departuresList.size();
    }
    
    public void displayDepartures() {
        DefaultTableModel model = (DefaultTableModel)tblDepartures.getModel();
        if (!departuresList.isEmpty()) {
            model.setRowCount(0);
            Object[] col = new Object[3];
            for (int i=0; i<departuresList.size(); i++) {
                col[0] = departuresList.get(i).get("reservationId");
                col[1] = departuresList.get(i).get("guestFirstName").toString() + " " + departuresList.get(i).get("guestSurname").toString();
                col[2] = calculateTotalCharges(Integer.parseInt(departuresList.get(i).get("reservationId").toString()), Double.parseDouble(departuresList.get(i).get("charges").toString())) - calculateTotalPayments(Integer.parseInt(departuresList.get(i).get("reservationId").toString()));

                model.addRow(col);
            }         
        } else {
            String noRecords = "No Departure Records.";
            model.setRowCount(0);
            Object[] col = new Object[1];
            col[0] = noRecords;

            model.addRow(col);
        }
    }
    
    private double calculateTotalCharges(int reservationId, double charges) {
        double sumAdditionalCharges = 0;
        double totalCharges;
        try {
            PreparedStatement p;
            String query = "SELECT SUM(charges) as sum_additional_charges FROM `additional_costs` WHERE reservation_id=?";

            p = con.prepareStatement(query);
            p.setInt(1, reservationId);

            rs = p.executeQuery();

            if (rs.next()) {
                sumAdditionalCharges = rs.getDouble("sum_additional_charges");
            }    
        }
        catch (Exception ex) {
            System.out.println("Error! Cannot fetch. " + ex);
        }
        totalCharges = sumAdditionalCharges + charges;
        return totalCharges;
    }
    
    private double calculateTotalPayments(int reservationId) {
        double totalPayments = 0;
        try {
            PreparedStatement p;
            String query = "SELECT SUM(amount) as total_payments FROM `payments` WHERE reservation_id=?";

            p = con.prepareStatement(query);
            p.setInt(1, reservationId);

            rs = p.executeQuery();

            if (rs.next()) {
                totalPayments = rs.getDouble("total_payments");
            }    
        }
        catch (Exception ex) {
            System.out.println("Error! Cannot fetch. " + ex);
        }
        return totalPayments;
    }
     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        bannerPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblCurrentGuests = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblAvailableRooms = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblOccupiedRooms = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblArrivals = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblDepartures = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        arrivalsPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblArrivals = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        departuresPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDepartures = new javax.swing.JTable();

        setBackground(new java.awt.Color(240, 240, 250));
        setMinimumSize(new java.awt.Dimension(1237, 455));
        setPreferredSize(new java.awt.Dimension(1237, 455));
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWeights = new double[] {1.0};
        layout.rowWeights = new double[] {1.0};
        setLayout(layout);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setPreferredSize(new java.awt.Dimension(80, 80));

        jLabel13.setFont(new java.awt.Font("Century Gothic", 0, 24)); // NOI18N
        jLabel13.setText("DASHBOARD");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1003;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        add(jPanel8, gridBagConstraints);

        jPanel1.setBackground(new java.awt.Color(240, 240, 250));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        bannerPanel.setMinimumSize(new java.awt.Dimension(1270, 240));
        bannerPanel.setLayout(new java.awt.GridLayout(1, 5));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Corbel", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("CURRENT GUESTS");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_staff_70px_1.png"))); // NOI18N

        lblCurrentGuests.setFont(new java.awt.Font("Corbel", 1, 40)); // NOI18N
        lblCurrentGuests.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(20, 20, 20))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(lblCurrentGuests, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblCurrentGuests, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(56, 56, 56))
        );

        bannerPanel.add(jPanel5);

        jPanel10.setBackground(new java.awt.Color(250, 250, 250));

        jLabel7.setFont(new java.awt.Font("Corbel", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("AVAILABLE ROOMS");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_empty_bed_70px.png"))); // NOI18N

        lblAvailableRooms.setFont(new java.awt.Font("Corbel", 1, 40)); // NOI18N
        lblAvailableRooms.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel10)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(lblAvailableRooms, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(lblAvailableRooms, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55))))
        );

        bannerPanel.add(jPanel10);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Corbel", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("OCCUPIED ROOMS");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_occupied_bed_70px_1.png"))); // NOI18N

        lblOccupiedRooms.setFont(new java.awt.Font("Corbel", 1, 40)); // NOI18N
        lblOccupiedRooms.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(lblOccupiedRooms, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblOccupiedRooms, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );

        bannerPanel.add(jPanel4);

        jPanel9.setBackground(new java.awt.Color(250, 250, 250));

        jLabel4.setFont(new java.awt.Font("Corbel", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText(" ARRIVALS");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_signin_70px.png"))); // NOI18N

        lblArrivals.setFont(new java.awt.Font("Corbel", 1, 40)); // NOI18N
        lblArrivals.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(lblArrivals, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(lblArrivals, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57))))
        );

        bannerPanel.add(jPanel9);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Corbel", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("DEPARTURES");

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_logout_rounded_up_70px_2.png"))); // NOI18N

        lblDepartures.setFont(new java.awt.Font("Corbel", 1, 40)); // NOI18N
        lblDepartures.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel12)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(lblDepartures, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(lblDepartures, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57))))
        );

        bannerPanel.add(jPanel11);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(50, 50, 0, 0);
        jPanel1.add(bannerPanel, gridBagConstraints);

        jPanel3.setBackground(new java.awt.Color(240, 240, 250));
        jPanel3.setPreferredSize(new java.awt.Dimension(1270, 450));
        jPanel3.setLayout(new java.awt.GridLayout(1, 2, 100, 0));

        arrivalsPanel.setBackground(new java.awt.Color(105, 205, 130));
        arrivalsPanel.setPreferredSize(new java.awt.Dimension(595, 500));

        tblArrivals.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        tblArrivals.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Reservation ID", "Guest Name", "Arrival Time"
            }
        ));
        tblArrivals.setRowHeight(25);
        tblArrivals.setSelectionBackground(new java.awt.Color(105, 205, 131));
        tblArrivals.setShowVerticalLines(false);
        jScrollPane2.setViewportView(tblArrivals);

        jLabel1.setFont(new java.awt.Font("Corbel", 1, 26)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Expected Arrivals");

        javax.swing.GroupLayout arrivalsPanelLayout = new javax.swing.GroupLayout(arrivalsPanel);
        arrivalsPanel.setLayout(arrivalsPanelLayout);
        arrivalsPanelLayout.setHorizontalGroup(
            arrivalsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        arrivalsPanelLayout.setVerticalGroup(
            arrivalsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(arrivalsPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
        );

        jPanel3.add(arrivalsPanel);

        departuresPanel.setBackground(new java.awt.Color(245, 108, 108));
        departuresPanel.setPreferredSize(new java.awt.Dimension(595, 500));

        jLabel2.setFont(new java.awt.Font("Corbel", 1, 26)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Expected Departures");

        tblDepartures.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        tblDepartures.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Reservation ID", "Guest Name", "Balance"
            }
        ));
        tblDepartures.setRowHeight(25);
        tblDepartures.setSelectionBackground(new java.awt.Color(243, 108, 108));
        tblDepartures.setShowVerticalLines(false);
        jScrollPane1.setViewportView(tblDepartures);

        javax.swing.GroupLayout departuresPanelLayout = new javax.swing.GroupLayout(departuresPanel);
        departuresPanel.setLayout(departuresPanelLayout);
        departuresPanelLayout.setHorizontalGroup(
            departuresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        departuresPanelLayout.setVerticalGroup(
            departuresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(departuresPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
        );

        jPanel3.add(departuresPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 702;
        gridBagConstraints.ipady = 339;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(50, 50, 0, 0);
        jPanel1.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 32, 0);
        add(jPanel1, gridBagConstraints);

        getAccessibleContext().setAccessibleName("DashboardPanel");
        getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel arrivalsPanel;
    private javax.swing.JPanel bannerPanel;
    private javax.swing.JPanel departuresPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblArrivals;
    private javax.swing.JLabel lblAvailableRooms;
    private javax.swing.JLabel lblCurrentGuests;
    private javax.swing.JLabel lblDepartures;
    private javax.swing.JLabel lblOccupiedRooms;
    private javax.swing.JTable tblArrivals;
    private javax.swing.JTable tblDepartures;
    // End of variables declaration//GEN-END:variables
}
