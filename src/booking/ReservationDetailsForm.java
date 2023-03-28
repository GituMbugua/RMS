/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package booking;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Gitu
 */

public class ReservationDetailsForm extends javax.swing.JDialog {

    private Connection con = MySqlConnection.myConnection();
    private ResultSet rs;   
    private Statement st;
    private String guestName;
    private Reservation resToEdit;
    private HashMap<Object, Object> res = null;
    private ArrayList<HashMap> additionalCostsList;
    private double sumAdditionalCharges = 0;
    /**
     * Creates new form ViewReservationDetails
     */
    public ReservationDetailsForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    public ReservationDetailsForm(java.awt.Frame parent, boolean modal, String guestName, Reservation resToEdit) {
        super(parent, modal);
        initComponents();
        this.guestName = guestName;
        this.resToEdit = resToEdit;
        getReservationDetails();
        setValues();
    }    
    
    private void getReservationDetails() {
        int resId = 0;
        
        if (resToEdit != null)
            resId = resToEdit.getReservationId();
        try {
            PreparedStatement p;
            String query = "SELECT reservation.reservation_id, user.first_name, guest.first_name, guest.surname, guest_category.category_id, guest_category.guest_category, accommodation_type.accommodation_type_id, accommodation_type.type, reservation.booking_date, reservation.arrival_date, reservation.departure_date, reservation.num_of_nights, reservation.num_of_adults, reservation.num_of_children_u12, reservation.num_of_children_teen, reservation.charges, reservation.status, reservation.party_id FROM reservation LEFT JOIN user ON reservation.user_id=user.user_id LEFT JOIN guest ON reservation.guest_id=guest.guest_id LEFT JOIN guest_category ON reservation.guest_category=guest_category.category_id LEFT JOIN accommodation_type ON reservation.accommodation_type=accommodation_type.accommodation_type_id WHERE `reservation_id`=?";

            p = con.prepareStatement(query);
            p.setInt(1, resId);

            rs = p.executeQuery();

            if (rs.next()) {
                int i = 1;
                
                int reservationId = rs.getInt(i++);
                String userFirstName = rs.getString(i++);
                String guestFirstName = rs.getString(i++);
                String guestSurname = rs.getString(i++);
                int guestCategoryId = rs.getInt(i++);
                String guestCategory = rs.getString(i++);
                int accommodationTypeId = rs.getInt(i++);
                String accommodationType = rs.getString(i++);
                String bookingDate = rs.getString(i++);
                String arrivalDate = rs.getString(i++);
                String departureDate = rs.getString(i++);
                int numOfNights = rs.getInt(i++);
                int numOfAdults = rs.getInt(i++);
                int numOfChildrenU12 = rs.getInt(i++);
                int numOfChildrenTeen = rs.getInt(i++);
                double charges = rs.getDouble(i++);
                String status = rs.getString(i++);
                Integer partyId = (Integer) rs.getObject(i++);
                
                res = new HashMap<>();
                res.put("reservationId", reservationId);
                res.put("userFirstName", userFirstName);
                res.put("guestFirstName", guestFirstName);
                res.put("guestSurname", guestSurname);
                res.put("guestCategoryId", guestCategoryId);
                res.put("guestCategory", guestCategory);
                res.put("accommodationTypeId", accommodationTypeId);
                res.put("accommodationType", accommodationType);
                res.put("bookingDate", bookingDate);
                res.put("arrivalDate", arrivalDate);
                res.put("departureDate", departureDate);
                res.put("numOfNights", numOfNights);
                res.put("numOfAdults", numOfAdults);
                res.put("numOfChildrenU12", numOfChildrenU12);
                res.put("numOfChildrenTeen", numOfChildrenTeen);
                res.put("charges", charges);
                res.put("status", status);
                res.put("partyId", partyId);
                                
               
                resToEdit.setArrivalDate(arrivalDate);
                resToEdit.setDepartureDate(departureDate);
                resToEdit.setNumOfNights(numOfNights);
                resToEdit.setNumOfAdults(numOfAdults);
                resToEdit.setNumOfChildrenU12(numOfChildrenU12);
                resToEdit.setNumOfChildrenTeen(numOfChildrenTeen);
                resToEdit.setGuestCategory(guestCategoryId);
                resToEdit.setAccommodationType(accommodationTypeId);
                resToEdit.setCharges(charges);
                resToEdit.setStatus(status);
                resToEdit.setPartyId(partyId);
            }    
        }
        catch (Exception ex) {
            System.out.println("Error! Cannot fetch. " + ex);
        }
    }
    
    private ArrayList<Room> getReservedRooms() {
        ArrayList<Room> roomList = new ArrayList();
        int resId = 0;
        if (resToEdit != null)
            resId = resToEdit.getReservationId();
        try {
            //myConnection();
            PreparedStatement p;
            String query = "SELECT reserved_rooms.id, room.* FROM `reserved_rooms` JOIN room ON reserved_rooms.room_id=room.room_id WHERE `reservation_id`=?";

            p = con.prepareStatement(query);
            p.setInt(1, resId);

            rs = p.executeQuery();

            Room room;
            while (rs.next()) {
                room = new Room(rs.getInt("room_id"), rs.getString("room_name"), rs.getInt("num_of_beds"), rs.getBoolean("is_available"), rs.getInt("cottage_id"));
                
                roomList.add(room);
            }    
        }
        catch (Exception ex) {
            System.out.println("Error! Cannot fetch. Problem... " + ex);
        }
        return roomList;
    }
    
    private ArrayList<String> getRoomNames(ArrayList<Room> list) {  
        //ArrayList<Room> list = getAvailableRooms();
        ArrayList<String> roomList = new ArrayList();
        HashSet hs = new HashSet();
        
        try {
            for (int i=0; i<list.size(); i++) {
                roomList.add(list.get(i).getRoomName());
            }
            hs.addAll(roomList);
            roomList.clear();
            roomList.addAll(hs);
        }
        catch (Exception ex) {
            System.out.println("Error! Cannot get type. " + ex);
        }
        
        return roomList;
    }
    
    private void removeRooms() {
        ArrayList<Room> rooms = getReservedRooms();
        int resId = 0;
        if (resToEdit != null)
            resId = resToEdit.getReservationId();
        for (int i = 0; i<rooms.size(); i++) {
            int roomId = rooms.get(i).getRoomId();
            
            try {
                PreparedStatement p;

                String newRowSql = "UPDATE `room` SET `is_available`=? WHERE room_id=?";
                p = con.prepareStatement(newRowSql);
                
                p.setBoolean(1, true);
                p.setInt(2, roomId);
                
                p.executeUpdate();
            }
            catch (Exception ex) {
                 System.out.println("Error! Cannot update record. " + ex);
             }
        }
        try {
            PreparedStatement p;

            String newRowSql = "DELETE FROM `reserved_rooms` WHERE reservation_id=?";
            p = con.prepareStatement(newRowSql);

            p.setBoolean(1, true);
            p.setInt(2, resId);

            p.executeUpdate();
        }
        catch (Exception ex) {
             System.out.println("Error! Cannot update record. " + ex);
         }
    }

    private void getAdditionalCosts() {
        additionalCostsList = new ArrayList();
        HashMap<Object, Object> addCost;
        int resId = 0;
        if (resToEdit != null)
            resId = resToEdit.getReservationId();
        try {
            PreparedStatement p;
            String query = "SELECT id, reservation_id, date, category, charges, (SELECT SUM(charges) FROM `additional_costs`) as sum_additional_charges FROM `additional_costs` WHERE reservation_id=?";

            p = con.prepareStatement(query);
            p.setInt(1, resId);

            rs = p.executeQuery();
            while (rs.next()) {
                int i = 1;
                
                int id = rs.getInt(i++);
                int reservationId = rs.getInt(i++);
                String date = rs.getString(i++);
                String category = rs.getString(i++);
                double charges = rs.getDouble(i++);
                sumAdditionalCharges = rs.getDouble(i++);
                
                addCost = new HashMap();
                addCost.put("id", id);
                addCost.put("reservationId", reservationId);
                addCost.put("date", date);
                addCost.put("category", category);
                addCost.put("charges", charges);
                //addCost.put("sumAdditionalCharges", sumAdditionalCharges);
                
                additionalCostsList.add(addCost);
            }    
        }
        catch (Exception ex) {
            System.out.println("Error! Cannot fetch. " + ex);
        }
    }
    
    private double calculateTotalCharges() {
        getAdditionalCosts();
        double totalCharges;
        
        totalCharges = sumAdditionalCharges + resToEdit.getCharges();
        return totalCharges;
    }
    
    private double calculateTotalPayments() {
        int resId = 0;
        double totalPayments = 0;
        if (resToEdit != null)
            resId = resToEdit.getReservationId();
        try {
            PreparedStatement p;
            String query = "SELECT SUM(amount) as total_payments FROM `payments` WHERE reservation_id=?";

            p = con.prepareStatement(query);
            p.setInt(1, resId);

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
        
    private void setValues() {
        txtCurrentGuest.setText(guestName);
        txtCurrentUser.setText(res.get("userFirstName").toString());
        txtGuestCategory.setText(res.get("guestCategory").toString());
        txtBookingDate.setText(res.get("bookingDate").toString());
        txtArrivalDate.setText(res.get("arrivalDate").toString());
        txtDepartureDate.setText(res.get("departureDate").toString());
        txtNumOfNights.setText(res.get("numOfNights").toString());
        txtNumOfAdults.setText(res.get("numOfAdults").toString());
        txtNumOfChildrenU12.setText(res.get("numOfChildrenU12").toString());
        txtNumOfChildrenTeen.setText(res.get("numOfChildrenTeen").toString());
        txtAccommodationType.setText(res.get("accommodationType").toString());
        txtAccommodationCharges.setText(res.get("charges").toString());
        txtStatus.setText(res.get("status").toString());
        txtTotalCharges.setText(String.valueOf(calculateTotalCharges()));
        txtPaid.setText(String.valueOf(calculateTotalPayments()));
        txtBalance.setText(String.valueOf(calculateTotalCharges()-calculateTotalPayments()));
        
        roomSelector.setModel(new javax.swing.AbstractListModel() {
            Object[] strings = getRoomNames(getReservedRooms()).toArray();
            @Override
            public int getSize() { return strings.length; }
            @Override
            public Object getElementAt(int i) { return strings[i]; }
        });
        
    }
    
    private void checkOut() {
         try {
            PreparedStatement p;
            String status = null;
            String newRowSql = "UPDATE `reservation` SET `status`=? WHERE reservation_id=?";

            if (calculateTotalCharges()-calculateTotalPayments() <= 0)
                status = "Checked Out";
            else
                status = "Pending Check Out";
            
            p = con.prepareStatement(newRowSql);
            p.setString(1, status);
            p.setInt(2, resToEdit.getReservationId());

            int updatedRows = p.executeUpdate();
            //reserveRooms();
            if (updatedRows > 0) {
                txtStatus.setText(status);
            }
            else
                JOptionPane.showMessageDialog(null, "Error! Unable to add record.");
            //con.close();

        }
        catch (Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error! Cannot add reservation. " + ex.getMessage());
        }
    }

    private boolean checkInvoice() {
        int resId = 0;
        boolean exists = false;
        if (resToEdit != null)
            resId = resToEdit.getReservationId();
        try {
            PreparedStatement p;
            String query = "SELECT reservation_id FROM `invoice` WHERE reservation_id=?";

            p = con.prepareStatement(query);
            p.setInt(1, resId);

            rs = p.executeQuery();

            if (rs.next()) {
                exists = true;
            }    
        }
        catch (Exception ex) {
            System.out.println("Error! Cannot fetch. " + ex);
        }
        return exists;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        btnEdit = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnAdditionalCosts = new javax.swing.JButton();
        btnCheckOut = new javax.swing.JButton();
        btnManagePayments = new javax.swing.JButton();
        btnGenerateInvoice = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtCurrentUser = new javax.swing.JTextField();
        txtBookingDate = new javax.swing.JTextField();
        txtArrivalDate = new javax.swing.JTextField();
        txtDepartureDate = new javax.swing.JTextField();
        txtNumOfNights = new javax.swing.JTextField();
        txtCurrentGuest = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtGuestCategory = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtTotalCharges = new javax.swing.JTextField();
        txtBalance = new javax.swing.JTextField();
        txtPaid = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtNumOfAdults = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtNumOfChildrenU12 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtNumOfChildrenTeen = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtAccommodationType = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        roomSelector = new javax.swing.JList();
        jLabel12 = new javax.swing.JLabel();
        txtAccommodationCharges = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtStatus = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnEdit.setBackground(new java.awt.Color(80, 170, 195));
        btnEdit.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Edit");
        btnEdit.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Corbel", 1, 24)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(60, 60, 60));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Options");

        btnAdditionalCosts.setBackground(new java.awt.Color(80, 170, 195));
        btnAdditionalCosts.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        btnAdditionalCosts.setForeground(new java.awt.Color(255, 255, 255));
        btnAdditionalCosts.setText("Manage Additional Costs");
        btnAdditionalCosts.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnAdditionalCosts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdditionalCostsActionPerformed(evt);
            }
        });

        btnCheckOut.setBackground(new java.awt.Color(105, 205, 130));
        btnCheckOut.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        btnCheckOut.setForeground(new java.awt.Color(255, 255, 255));
        btnCheckOut.setText("Check Out");
        btnCheckOut.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnCheckOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckOutActionPerformed(evt);
            }
        });

        btnManagePayments.setBackground(new java.awt.Color(80, 170, 195));
        btnManagePayments.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        btnManagePayments.setForeground(new java.awt.Color(255, 255, 255));
        btnManagePayments.setText("Manage Payments");
        btnManagePayments.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnManagePayments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManagePaymentsActionPerformed(evt);
            }
        });

        btnGenerateInvoice.setBackground(new java.awt.Color(80, 170, 195));
        btnGenerateInvoice.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        btnGenerateInvoice.setForeground(new java.awt.Color(255, 255, 255));
        btnGenerateInvoice.setText("Generate Invoice");
        btnGenerateInvoice.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnGenerateInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateInvoiceActionPerformed(evt);
            }
        });

        btnExit.setBackground(new java.awt.Color(245, 108, 108));
        btnExit.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setText("Exit");
        btnExit.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGenerateInvoice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator1)
                            .addComponent(btnEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)))
                    .addComponent(btnAdditionalCosts, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                    .addComponent(btnCheckOut, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                    .addComponent(btnManagePayments, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                    .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAdditionalCosts, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnManagePayments, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGenerateInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCheckOut, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel1.setText("Reservation For:");

        jLabel2.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel2.setText("Booking Date");

        jLabel3.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel3.setText("Arrival Date");

        jLabel4.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel4.setText("Departure Date");

        jLabel5.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel5.setText("Number of Nights");

        jLabel9.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel9.setText("Reserved By:");

        txtCurrentUser.setEditable(false);
        txtCurrentUser.setBackground(new java.awt.Color(250, 250, 250));
        txtCurrentUser.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        txtCurrentUser.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));

        txtBookingDate.setEditable(false);
        txtBookingDate.setBackground(new java.awt.Color(250, 250, 250));
        txtBookingDate.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        txtBookingDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));

        txtArrivalDate.setEditable(false);
        txtArrivalDate.setBackground(new java.awt.Color(250, 250, 250));
        txtArrivalDate.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        txtArrivalDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));

        txtDepartureDate.setEditable(false);
        txtDepartureDate.setBackground(new java.awt.Color(250, 250, 250));
        txtDepartureDate.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        txtDepartureDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));

        txtNumOfNights.setEditable(false);
        txtNumOfNights.setBackground(new java.awt.Color(250, 250, 250));
        txtNumOfNights.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        txtNumOfNights.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));

        txtCurrentGuest.setEditable(false);
        txtCurrentGuest.setBackground(new java.awt.Color(250, 250, 250));
        txtCurrentGuest.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        txtCurrentGuest.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));

        jLabel15.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel15.setText("Guest Category");

        txtGuestCategory.setEditable(false);
        txtGuestCategory.setBackground(new java.awt.Color(250, 250, 250));
        txtGuestCategory.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        txtGuestCategory.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNumOfNights, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                                .addGap(17, 17, 17)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCurrentGuest)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtCurrentUser)
                                        .addGap(1, 1, 1))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtGuestCategory))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtBookingDate))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtArrivalDate)
                                    .addComponent(txtDepartureDate))))
                        .addGap(25, 25, 25))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCurrentUser, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCurrentGuest, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtGuestCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBookingDate, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtArrivalDate, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDepartureDate, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumOfNights, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(200, 200, 200));
        jPanel3.setForeground(new java.awt.Color(60, 60, 60));

        jLabel13.setFont(new java.awt.Font("Corbel", 1, 20)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(60, 60, 60));
        jLabel13.setText("Total Charges");

        jLabel14.setFont(new java.awt.Font("Corbel", 1, 20)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(60, 60, 60));
        jLabel14.setText("Balance");

        txtTotalCharges.setFont(new java.awt.Font("Corbel", 0, 20)); // NOI18N
        txtTotalCharges.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));

        txtBalance.setFont(new java.awt.Font("Corbel", 0, 20)); // NOI18N
        txtBalance.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));

        txtPaid.setFont(new java.awt.Font("Corbel", 0, 20)); // NOI18N
        txtPaid.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));

        jLabel17.setFont(new java.awt.Font("Corbel", 1, 20)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(60, 60, 60));
        jLabel17.setText("Paid");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addComponent(txtTotalCharges, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                .addComponent(jLabel17)
                .addGap(18, 18, 18)
                .addComponent(txtPaid, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(106, 106, 106)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotalCharges, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPaid, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel6.setText("Number of Adults");

        txtNumOfAdults.setEditable(false);
        txtNumOfAdults.setBackground(new java.awt.Color(250, 250, 250));
        txtNumOfAdults.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtNumOfAdults.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));

        jLabel7.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel7.setText("Number of Children U12");

        txtNumOfChildrenU12.setEditable(false);
        txtNumOfChildrenU12.setBackground(new java.awt.Color(250, 250, 250));
        txtNumOfChildrenU12.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtNumOfChildrenU12.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));

        jLabel8.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel8.setText("Number of Children 12+");

        txtNumOfChildrenTeen.setEditable(false);
        txtNumOfChildrenTeen.setBackground(new java.awt.Color(250, 250, 250));
        txtNumOfChildrenTeen.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtNumOfChildrenTeen.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));

        jLabel10.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel10.setText("Accommodation Type");

        txtAccommodationType.setEditable(false);
        txtAccommodationType.setBackground(new java.awt.Color(250, 250, 250));
        txtAccommodationType.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtAccommodationType.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));

        jLabel11.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel11.setText("Rooms");

        roomSelector.setBackground(new java.awt.Color(250, 250, 250));
        roomSelector.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));
        roomSelector.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        roomSelector.setModel(new javax.swing.AbstractListModel() {
            Object[] strings = getRoomNames(getReservedRooms()).toArray();
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(roomSelector);

        jLabel12.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel12.setText("Accommodation Charges");

        txtAccommodationCharges.setBackground(new java.awt.Color(250, 250, 250));
        txtAccommodationCharges.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtAccommodationCharges.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));

        jLabel18.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel18.setText("Status");

        txtStatus.setBackground(new java.awt.Color(250, 250, 250));
        txtStatus.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtStatus.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(txtAccommodationType, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAccommodationCharges)
                            .addComponent(txtStatus, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtNumOfAdults, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNumOfChildrenTeen, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNumOfChildrenU12, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumOfAdults, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumOfChildrenU12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumOfChildrenTeen, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAccommodationType, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAccommodationCharges, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        EditReservationForm editRes = new EditReservationForm(this, true, guestName, resToEdit);
        editRes.setTitle("Edit Reservation Form");
        editRes.setLocationRelativeTo(editRes.getParent());
        editRes.setVisible(true);
        editRes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editRes.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                getReservationDetails();
                setValues();
            }
        });
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnAdditionalCostsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdditionalCostsActionPerformed
        AdditionalCostsForm acForm = new AdditionalCostsForm(this, true, guestName, resToEdit.getReservationId());
        acForm.setTitle("Additional Costs Management");
        acForm.setLocationRelativeTo(acForm.getParent());
        acForm.setVisible(true);
        acForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        acForm.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                getReservationDetails();
                setValues();
            }
        });
    }//GEN-LAST:event_btnAdditionalCostsActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnManagePaymentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManagePaymentsActionPerformed
        PaymentsForm payForm = new PaymentsForm(this, true, guestName, resToEdit.getReservationId());
        payForm.setTitle("Payment Management");
        payForm.setLocationRelativeTo(payForm.getParent());
        payForm.setVisible(true);
        payForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        payForm.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                getReservationDetails();
                setValues();
            }
        });
    }//GEN-LAST:event_btnManagePaymentsActionPerformed

    private void btnCheckOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckOutActionPerformed
        checkOut();
        removeRooms();
        this.dispose();
    }//GEN-LAST:event_btnCheckOutActionPerformed

    private void btnGenerateInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateInvoiceActionPerformed
        int resId = 0;
        double totalCharges = calculateTotalCharges();
        double agentCommission = 0.0;
        double discount = 0.0;
        if (resToEdit != null)
            resId = resToEdit.getReservationId();
        try {
            PreparedStatement p;
            if (checkInvoice()) {
                String newRowSql = "UPDATE `invoice` SET `total_charges`=?,`agent_commission`=?,`discount`=? WHERE reservation_id=?";

                p = con.prepareStatement(newRowSql);
                p.setDouble(1, totalCharges);
                p.setDouble(2, agentCommission);
                p.setDouble(3, discount);
                p.setInt(4, resId);

                int updatedRows = p.executeUpdate();
                if (updatedRows > 0)
                    JOptionPane.showMessageDialog(null, "Invoice Updated");
                else
                    JOptionPane.showMessageDialog(null, "Error! Unable to add.");
            }
            else {
                String newRowSql = "INSERT INTO `invoice`(`reservation_id`, `total_charges`, `agent_commission`, `discount`) VALUES (?,?,?,?)";

                p = con.prepareStatement(newRowSql);
                p.setInt(1, resId);
                p.setDouble(2, totalCharges);
                p.setDouble(3, agentCommission);
                p.setDouble(4, discount);

                int updatedRows = p.executeUpdate();
                if (updatedRows > 0)
                    JOptionPane.showMessageDialog(null, "Invoice Saved.!");
                else
                    JOptionPane.showMessageDialog(null, "Error! Unable to add.");
            }
            this.dispose();
        } catch(Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error! " + ex.getMessage());
        }
        
        GenerateInvoice invoice = new GenerateInvoice();
        String pdfFilename = "invoice_" + java.time.LocalDate.now().toString() + ".pdf";
        invoice.setInvoiceDetails(res, additionalCostsList);
        invoice.createPDF(pdfFilename);
        
        String saveFolder = res.get("guestFirstName").toString() + res.get("guestSurname").toString();
        try {
            File pdfFile = new File("invoices/" + saveFolder + "/" + pdfFilename);
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("Awt Desktop is not supported!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "File does not exist.");
            }

      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }//GEN-LAST:event_btnGenerateInvoiceActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ReservationDetailsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReservationDetailsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReservationDetailsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReservationDetailsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ReservationDetailsForm dialog = new ReservationDetailsForm(new javax.swing.JFrame(), true);
                dialog.setLocationRelativeTo(null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdditionalCosts;
    private javax.swing.JButton btnCheckOut;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnGenerateInvoice;
    private javax.swing.JButton btnManagePayments;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JList roomSelector;
    private javax.swing.JTextField txtAccommodationCharges;
    private javax.swing.JTextField txtAccommodationType;
    private javax.swing.JTextField txtArrivalDate;
    private javax.swing.JTextField txtBalance;
    private javax.swing.JTextField txtBookingDate;
    private javax.swing.JTextField txtCurrentGuest;
    private javax.swing.JTextField txtCurrentUser;
    private javax.swing.JTextField txtDepartureDate;
    private javax.swing.JTextField txtGuestCategory;
    private javax.swing.JTextField txtNumOfAdults;
    private javax.swing.JTextField txtNumOfChildrenTeen;
    private javax.swing.JTextField txtNumOfChildrenU12;
    private javax.swing.JTextField txtNumOfNights;
    private javax.swing.JTextField txtPaid;
    private javax.swing.JTextField txtStatus;
    private javax.swing.JTextField txtTotalCharges;
    // End of variables declaration//GEN-END:variables
}
