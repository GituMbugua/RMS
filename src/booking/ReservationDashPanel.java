/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package booking;

import static booking.LoginForm.currentUser;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author Gitu
 */
public class ReservationDashPanel extends javax.swing.JPanel {

    private Connection con = MySqlConnection.myConnection();
    private ResultSet rs;   
    private Statement st;
    private JDatePickerImpl arrivalPicker;
    private JDatePickerImpl departurePicker;
    private ArrayList<HashMap> reservationsList;
    Reservation resToEdit;
    private int selectedReservationId;
    private String selectedGuestName;
    private ArrayList<Guest> guestList;
    NewReservationPanel nrPanel = new NewReservationPanel();
    /**
     * Creates new form ReservationDashPanel
     */
    public ReservationDashPanel() {
        initComponents();
        
        arrivalDatePicker();
        departureDatePicker();
        /*
        getReservationRecords();
        displayReservationRecords();
        */
        customInit();
    }

    private void customInit() {
        String userRole = currentUser.getRole().toLowerCase();
        switch (userRole) {
            case "admin":
                btnAddNew.setEnabled(true);
                break;
            case "general":
                btnAddNew.setEnabled(true);
                break;
            case "audit":
                btnAddNew.setEnabled(false);
                btnAddNew.setBackground(new Color(240, 240, 240));
                break;
        }
        btnViewDetails.setEnabled(false);
        lblFname.setVisible(false);
        lblSurname.setVisible(false);
        searchFirstName.setVisible(false);
        searchSurname.setVisible(false);
        btnSearch.setVisible(false);
        tblReservationRecords.getTableHeader().setFont(new Font("Corbel", Font.PLAIN, 18));
    }
    
    // ********** date pickers **********
    private void arrivalDatePicker() {
        UtilDateModel model  = new UtilDateModel();
        JDatePanelImpl datePanel;
        
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        datePanel = new JDatePanelImpl(model, p);
        arrivalPicker = new JDatePickerImpl(datePanel, new DateFormatter());
        arrivalPicker.setBounds(270, 90, 150, 40);
        model.setSelected(true);
        arrivalPicker.setBackground(new Color(255, 255, 255));
        arrivalPicker.setVisible(true);
        arrivalPicker.setEnabled(false);
        //controlsPanel.add(arrivalPicker);
    }
    
    private void departureDatePicker() {
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel;
        
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        datePanel = new JDatePanelImpl(model, p);
        departurePicker = new JDatePickerImpl(datePanel, new DateFormatter());
        departurePicker.setBounds(500, 90, 150, 40);
        model.setSelected(true);
        departurePicker.setBackground(new Color(255, 255, 255));
        departurePicker.setVisible(true);
        departurePicker.setEnabled(false);
        //controlsPanel.add(departurePicker);
    }
    // ********** end date pickers **********1
 
    private void getReservationRecords() {
        try {
            //myConnection();
            reservationsList = new ArrayList();
                
            String query = "SELECT reservation.reservation_id, user.first_name, guest.first_name, guest.surname, guest_category.guest_category, accommodation_type.type, reservation.booking_date, reservation.arrival_date, reservation.departure_date, reservation.num_of_nights, reservation.num_of_adults, reservation.num_of_children_u12, reservation.num_of_children_teen, reservation.charges, reservation.status FROM reservation LEFT JOIN user ON reservation.user_id=user.user_id LEFT JOIN guest ON reservation.guest_id=guest.guest_id LEFT JOIN guest_category ON reservation.guest_category=guest_category.category_id LEFT JOIN accommodation_type ON reservation.accommodation_type=accommodation_type.accommodation_type_id";
            
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            HashMap<Object, Object> res;
            
            while (rs.next()) {
                int i = 1;
                
                res = new HashMap<>();
                res.put("reservationId", rs.getInt(i++));
                res.put("userFirstName", rs.getString(i++));
                res.put("guestFirstName", rs.getString(i++));
                res.put("guestSurname", rs.getString(i++));
                res.put("guestCategory", rs.getString(i++));
                res.put("accommodationType", rs.getString(i++));
                res.put("bookingDate", rs.getString(i++));
                res.put("arrivalDate", rs.getString(i++));
                res.put("departureDate", rs.getString(i++));
                res.put("numOfNights", rs.getString(i++));
                res.put("numOfAdults", rs.getInt(i++));
                res.put("numOfChildrenU12", rs.getInt(i++));
                res.put("numOfChildrenTeen", rs.getInt(i++));
                res.put("charges", rs.getDouble(i++));
                res.put("status", rs.getString(i++));
                                
                reservationsList.add(res);
            }
        }
        catch (Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error!");
        }
    }
   
    private void displayReservationRecords() {
        DefaultTableModel model = (DefaultTableModel) tblReservationRecords.getModel();
        if (!reservationsList.isEmpty()) {
            model.setRowCount(0);
            Object[] col = new Object[13];
            for (int i=0; i<reservationsList.size(); i++) {
                col[0] = reservationsList.get(i).get("reservationId");
                col[1] = reservationsList.get(i).get("userFirstName");
                col[2] = reservationsList.get(i).get("guestFirstName")+ " " + reservationsList.get(i).get("guestSurname");
                col[3] = reservationsList.get(i).get("guestCategory");
                col[4] = reservationsList.get(i).get("accommodationType");
                col[5] = reservationsList.get(i).get("bookingDate");
                col[6] = reservationsList.get(i).get("arrivalDate");
                col[7] = reservationsList.get(i).get("departureDate");
                col[8] = reservationsList.get(i).get("numOfAdults");
                col[9] = reservationsList.get(i).get("numOfChildrenU12");
                col[10] = reservationsList.get(i).get("numOfChildrenTeen");
                col[11] = reservationsList.get(i).get("status");
                
                model.addRow(col);
            }         
        } else {
            String noRecords = "No Reservation Records.";
            model.setRowCount(0);
            Object[] col = new Object[1];
            col[0] = noRecords;

            model.addRow(col);
        }
    }
    
    private void getSelectedReservation() {
        DefaultTableModel model = (DefaultTableModel) tblReservationRecords.getModel();
        int selectedRowIndex = tblReservationRecords.getSelectedRow();
        
        if (tblReservationRecords.getSelectedRowCount() != 0) {
            resToEdit = new Reservation();

            int reservationId = Integer.parseInt(model.getValueAt(selectedRowIndex, 0).toString());
            selectedGuestName = model.getValueAt(selectedRowIndex, 2).toString();
            String guestCategory = model.getValueAt(selectedRowIndex, 3).toString();
            String accommodationType = model.getValueAt(selectedRowIndex, 4).toString();
            String arrivalDate = model.getValueAt(selectedRowIndex, 6).toString();
            String departureDate = model.getValueAt(selectedRowIndex, 7).toString();
            int numOfAdults = Integer.parseInt(model.getValueAt(selectedRowIndex, 8).toString());
            int numOfChildrenU12 = Integer.parseInt(model.getValueAt(selectedRowIndex, 9).toString());
            int numOfChildrenTeen = Integer.parseInt(model.getValueAt(selectedRowIndex, 10).toString());
            String status = model.getValueAt(selectedRowIndex, 11).toString();
            this.selectedReservationId = reservationId;
            
            resToEdit.setReservationId(selectedReservationId);
            resToEdit.setArrivalDate(arrivalDate);
            resToEdit.setDepartureDate(departureDate);
            resToEdit.setNumOfAdults(numOfAdults);
            resToEdit.setNumOfChildrenU12(numOfChildrenU12);
            resToEdit.setNumOfChildrenTeen(numOfChildrenTeen);
            resToEdit.setStatus(status);
            resToEdit.setGuestCategory(nrPanel.searchCategoryId(guestCategory));
            resToEdit.setAccommodationType(nrPanel.searchAccommodationId(accommodationType));
        } else {
            JOptionPane opt = new JOptionPane("No selected records to edit.", JOptionPane.ERROR_MESSAGE);
            final JDialog jdg = opt.createDialog("Error");
            new Thread(new Runnable(){
                public void run() {
                    try {
                        Thread.sleep(2000);
                        jdg.dispose();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(EditReservationForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();
            jdg.setVisible(true);
        }
    }
    
    private ArrayList<Guest> searchGuest() {                                          
        try {
            PreparedStatement p = null;
            String query = "";
            guestList = new ArrayList();

            String firstName, surname;
            firstName = searchFirstName.getText();
            surname = searchSurname.getText();
            
            if (!firstName.isEmpty() && !surname.isEmpty()) {
                query = "SELECT * FROM `guest` WHERE `first_name` = ? AND `surname` = ?";
                p = con.prepareStatement(query);
                p.setString(1, firstName);
                p.setString(2, surname);
            }
            else if (firstName.isEmpty() && !surname.isEmpty()) {
                query = "SELECT * FROM `guest` WHERE `surname` = ?";
                p = con.prepareStatement(query);
                p.setString(1, surname);
            }
            else if (!firstName.isEmpty() && surname.isEmpty()) {
                query = "SELECT * FROM `guest` WHERE `first_name` = ?";
                p = con.prepareStatement(query);
                p.setString(1, firstName);
            }
            else
                JOptionPane.showMessageDialog(null, "No input detected!");

            rs = p.executeQuery();
            
            Guest guest;
            while (rs.next()) {
                guest = new Guest(rs.getString("first_name"), rs.getString("middle_name"), rs.getString("surname"), rs.getString("date_of_birth"), rs.getString("phone_number"), rs.getString("address"), rs.getString("email"), rs.getString("national_id_num"), rs.getString("passport_num"), rs.getString("nationality"), rs.getString("country_of_residence"), rs.getString("occupation"), rs.getString("health_considerations"), rs.getString("preferred_room"));
                guest.setId(rs.getInt("guest_id"));
                guestList.add(guest);
            }
        }
        catch (Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error!");
        }
        return guestList;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        controlsPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        searchByCombo = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        btnRefresh = new javax.swing.JButton();
        btnViewDetails = new javax.swing.JButton();
        btnAddNew = new javax.swing.JButton();
        lblFname = new javax.swing.JLabel();
        lblSurname = new javax.swing.JLabel();
        searchFirstName = new javax.swing.JTextField();
        searchSurname = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        tblPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblReservationRecords = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(1240, 500));

        controlsPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel1.setText("Search By");

        searchByCombo.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        searchByCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None Selected", "All Records", "Guest Name" }));
        searchByCombo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 10));
        searchByCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchByComboActionPerformed(evt);
            }
        });

        btnRefresh.setBackground(new java.awt.Color(80, 170, 195));
        btnRefresh.setFont(new java.awt.Font("Corbel", 1, 18)); // NOI18N
        btnRefresh.setForeground(new java.awt.Color(255, 255, 255));
        btnRefresh.setText("Refresh");
        btnRefresh.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnViewDetails.setBackground(new java.awt.Color(250, 250, 250));
        btnViewDetails.setFont(new java.awt.Font("Corbel", 1, 18)); // NOI18N
        btnViewDetails.setForeground(new java.awt.Color(255, 255, 255));
        btnViewDetails.setText("View Details");
        btnViewDetails.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnViewDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewDetailsActionPerformed(evt);
            }
        });

        btnAddNew.setBackground(new java.awt.Color(105, 205, 130));
        btnAddNew.setFont(new java.awt.Font("Corbel", 1, 18)); // NOI18N
        btnAddNew.setForeground(new java.awt.Color(255, 255, 255));
        btnAddNew.setText("Add New");
        btnAddNew.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnAddNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNewActionPerformed(evt);
            }
        });

        lblFname.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        lblFname.setText("First Name");

        lblSurname.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        lblSurname.setText("Surname");

        searchFirstName.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N

        searchSurname.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N

        btnSearch.setBackground(new java.awt.Color(250, 250, 250));
        btnSearch.setFont(new java.awt.Font("Corbel", 1, 18)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setText("Search");
        btnSearch.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout controlsPanelLayout = new javax.swing.GroupLayout(controlsPanel);
        controlsPanel.setLayout(controlsPanelLayout);
        controlsPanelLayout.setHorizontalGroup(
            controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlsPanelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(controlsPanelLayout.createSequentialGroup()
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnViewDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(btnAddNew, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(controlsPanelLayout.createSequentialGroup()
                        .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchByCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(77, 77, 77)
                        .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(searchFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblFname, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(76, 76, 76)
                        .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(controlsPanelLayout.createSequentialGroup()
                                .addComponent(searchSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(69, 69, 69)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 392, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(controlsPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jSeparator1)
                    .addContainerGap()))
        );
        controlsPanelLayout.setVerticalGroup(
            controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlsPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(controlsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(searchByCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(controlsPanelLayout.createSequentialGroup()
                        .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblFname, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(37, 37, 37)
                .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddNew, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnViewDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(controlsPanelLayout.createSequentialGroup()
                    .addGap(147, 147, 147)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(95, 95, 95)))
        );

        tblPanel.setBackground(new java.awt.Color(255, 255, 255));
        tblPanel.setLayout(new java.awt.GridLayout(1, 0));

        tblReservationRecords.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 10));
        tblReservationRecords.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        tblReservationRecords.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Reservation Number", "Reserved By", "Guest Name", "Guest Category", "Accommodation Type", "Booking Date", "Arrival Date", "Departure Date", "Adults", "Children U12", "Children 12+", "Status"
            }
        ));
        tblReservationRecords.setRowHeight(25);
        tblReservationRecords.setRowMargin(0);
        tblReservationRecords.setSelectionBackground(new java.awt.Color(105, 205, 131));
        tblReservationRecords.setShowVerticalLines(false);
        tblReservationRecords.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblReservationRecordsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblReservationRecords);
        if (tblReservationRecords.getColumnModel().getColumnCount() > 0) {
            tblReservationRecords.getColumnModel().getColumn(8).setPreferredWidth(20);
            tblReservationRecords.getColumnModel().getColumn(11).setPreferredWidth(20);
        }

        tblPanel.add(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(controlsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tblPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(controlsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tblPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNewActionPerformed
        // TODO add your handling code here:
        ReservationForm resForm = new ReservationForm((JFrame)this.getRootPane().getParent(), true, "newRes");
        resForm.setTitle("Reservation Form");
        resForm.setLocationRelativeTo(resForm.getParent());
        resForm.setVisible(true);
        resForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resForm.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    getReservationRecords();
                    displayReservationRecords();
                }
            });
    }//GEN-LAST:event_btnAddNewActionPerformed

    private void tblReservationRecordsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblReservationRecordsMouseClicked
        // TODO add your handling code here:
        btnViewDetails.setEnabled(true);
        btnViewDetails.setBackground(new Color(80, 170, 195));
    }//GEN-LAST:event_tblReservationRecordsMouseClicked

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        getReservationRecords();
        displayReservationRecords();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnViewDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewDetailsActionPerformed
        getSelectedReservation();
        ReservationDetailsForm resDetails = new ReservationDetailsForm((JFrame)this.getRootPane().getParent(), true, selectedGuestName, resToEdit);
        resDetails.setTitle("Reservation Details");
        resDetails.setLocationRelativeTo(resDetails.getParent());
        resDetails.setVisible(true);
        resDetails.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resDetails.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                getReservationRecords();
                displayReservationRecords();
            }
        });
    }//GEN-LAST:event_btnViewDetailsActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        //displayGuestRecords(searchGuest());
    }//GEN-LAST:event_btnSearchActionPerformed

    private void searchByComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchByComboActionPerformed
        int value = searchByCombo.getSelectedIndex();
        switch (value) {
            case 1:
                lblFname.setEnabled(false);
                lblSurname.setEnabled(false);
                searchFirstName.setEnabled(false);
                searchSurname.setEnabled(false);
                btnSearch.setEnabled(false);
                btnSearch.setBackground(new Color(250, 250, 250));
                
                getReservationRecords();
                displayReservationRecords();
                break;
            case 2:
                /*
                lblFname.setEnabled(true);
                lblSurname.setEnabled(true);
                searchFirstName.setEnabled(true);
                searchSurname.setEnabled(true);
                btnSearch.setEnabled(true);
                btnSearch.setBackground(new Color(105, 205, 130));
                */
                break;
        }
    }//GEN-LAST:event_searchByComboActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddNew;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnViewDetails;
    private javax.swing.JPanel controlsPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblFname;
    private javax.swing.JLabel lblSurname;
    private javax.swing.JComboBox searchByCombo;
    private javax.swing.JTextField searchFirstName;
    private javax.swing.JTextField searchSurname;
    private javax.swing.JPanel tblPanel;
    private javax.swing.JTable tblReservationRecords;
    // End of variables declaration//GEN-END:variables
}
