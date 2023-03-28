/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package booking;

import static booking.Guest.currentGuest;
import static booking.LoginForm.currentUser;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;


/**
 *
 * @author Gitu
 */
public class NewReservationPanel extends javax.swing.JPanel {

    private Connection con = MySqlConnection.myConnection();
    private ResultSet rs;   
    private Statement st;
    private JDatePickerImpl arrivalPicker;
    private JDatePickerImpl departurePicker;
    private DateFormatter df;
    private int newReservationId;
    private ArrayList selectedRoomList = new ArrayList();
    private Guest selectedGuest;
    private HashMap<Object, Object> selectedParty;
    
    /**
     * Creates new form NewReservationPanel
     */
    public NewReservationPanel() {
        initComponents();
        arrivalDatePicker();  
        departureDatePicker();
        customInit();
    }

    private void customInit() {
        selectedGuest = currentGuest;
        guestCategoryCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (guestCategoryCombo.getSelectedItem().equals("Day/Walk-In")) {
                    accommodationTypeCombo.setSelectedItem("Lunch");
                    jLabel7.setEnabled(false);
                    accommodationTypeCombo.setEnabled(false);
                    jLabel4.setEnabled(false);
                    numOfAdultsSpinner.setEnabled(false);
                    jLabel5.setEnabled(false);
                    numOfChildrenU12Spinner.setEnabled(false);
                    jLabel9.setEnabled(false);
                    numOfChildrenTeenSpinner.setEnabled(false);
                    jLabel8.setEnabled(false);
                    availableRoomsList.setEnabled(false);
                    jLabel10.setEnabled(false);
                    reservedRoomsList.setEnabled(false);
                    btnAddRoom.setEnabled(false);
                    btnRemoveRoom.setEnabled(false);
                    jLabel11.setEnabled(false);
                    rbNo.setEnabled(false);
                    rbYes.setEnabled(false);
                } else {
                    accommodationTypeCombo.setSelectedIndex(0);
                    jLabel7.setEnabled(true);
                    accommodationTypeCombo.setEnabled(true);
                    jLabel4.setEnabled(true);
                    numOfAdultsSpinner.setEnabled(true);
                    jLabel5.setEnabled(true);
                    numOfChildrenU12Spinner.setEnabled(true);
                    jLabel9.setEnabled(true);
                    numOfChildrenTeenSpinner.setEnabled(true);
                    jLabel8.setEnabled(true);
                    availableRoomsList.setEnabled(true);
                    jLabel10.setEnabled(true);
                    reservedRoomsList.setEnabled(true);
                    btnAddRoom.setEnabled(true);
                    btnRemoveRoom.setEnabled(true);
                    jLabel11.setEnabled(true);
                    rbNo.setEnabled(true);
                    rbYes.setEnabled(true);
                }
            }
        });
        guestCategoryCombo.setSelectedIndex(1);
        btnManageParty.setVisible(false);
        rbNo.setSelected(true);
    }
    
    public void currentGuestDetails() {
        //System.out.println("New Reservation Panel - Guest ID: " + currentGuest.getId());
        txtCurrentGuest.setText(selectedGuest.getFirstName() + " " + selectedGuest.getSurname());
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
        arrivalPicker.setBounds(270, 125, 200, 35);
        arrivalPicker.setBackground(Color.WHITE);
        model.setSelected(true);
        arrivalPicker.setVisible(true);
        leftPanel.add(arrivalPicker);
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
        departurePicker.setBounds(270, 195, 200, 35);
        departurePicker.setBackground(Color.WHITE);
        model.setSelected(true);
        departurePicker.setVisible(true);
        leftPanel.add(departurePicker);
    }
 
    // ********** end date pickers **********
    
    private String getArrivalDate() throws ParseException {
        String arrivalDate, hour, minute;
        df = new DateFormatter();
 
        Date selectedArrivalDate = (Date) arrivalPicker.getModel().getValue();
        
        hour = arrivalHour.getValue().toString();
        minute = arrivalMinute.getValue().toString();
        arrivalDate = df.convertToLocalDate(selectedArrivalDate) +" "+ hour +":"+ minute;
        
        return arrivalDate;
    }
  
    private String getDepartureDate() throws ParseException {
        String departureDate;
        df = new DateFormatter();
        
        Date selectedDepartureDate = (Date) departurePicker.getModel().getValue();
        departureDate = df.convertToLocalDate(selectedDepartureDate) + "";

        return departureDate;
    }
    
    //get all objects from table "Rates" in database
    public ArrayList<Rates> getRates() {  
        ArrayList<Rates> list = new ArrayList();
       
        try {
            //myConnection();
           
            String query = "SELECT * FROM rates";
            st = con.createStatement();
            rs = st.executeQuery(query);

            Rates rates;
            while (rs.next()) {
               rates = new Rates(rs.getInt("rate_id"),  rs.getInt("guest_category"), rs.getInt("accommodation_type"), rs.getDouble("rate"));
               list.add(rates); 
           }
        }
       catch (Exception ex) {
            System.out.println("Error! Cannot fetch. " + ex);
        }
        return list;
    }
    //get all objects from table "accommodation_type" in database
    public ArrayList<AccommodationType> getAccommodationType() {  
        ArrayList<AccommodationType> list = new ArrayList();
       
        try {
            //myConnection();
           
            String query = "SELECT * FROM accommodation_type";
            st = con.createStatement();
            rs = st.executeQuery(query);

            AccommodationType acc;
            while (rs.next()) {
               acc = new AccommodationType(rs.getInt("accommodation_type_id"),  rs.getString("type"));
               list.add(acc);
           }
        }
       catch (Exception ex) {
            System.out.println("Error! Cannot fetch. " + ex);
        }
        return list;
    }
    
    //get list of unique accommodation types 
    public ArrayList<String> getTypeList() {  
        ArrayList<AccommodationType> list = getAccommodationType();
        ArrayList<String> typeList = new ArrayList();
        HashSet hs = new HashSet();
        
        try {
            for (int i=0; i<list.size(); i++) {
                typeList.add(list.get(i).getType());
            }
            hs.addAll(typeList);
            typeList.clear();
            typeList.addAll(hs);
            Collections.sort(typeList);
        }
        catch (Exception ex) {
            System.out.println("Error! Cannot get type. " + ex);
        }
        
        return typeList;
    }
    
    //get all objects from table "guest_category" in database
    public ArrayList<GuestCategory> getGuestCategory() {  
        ArrayList<GuestCategory> list = new ArrayList();
       
        try {
            //myConnection();
           
            String query = "SELECT * FROM `guest_category`";
            st = con.createStatement();
            rs = st.executeQuery(query);

            GuestCategory cat;
            while (rs.next()) {
               cat = new GuestCategory(rs.getInt("category_id"), rs.getString("guest_category"));
               list.add(cat);
           }
        }
       catch (Exception ex) {
            System.out.println("Error! Cannot fetch. " + ex);
        }
        return list;
    }
    
    //get list of unique guest categories
    public ArrayList<String> getCategoryList() {  
        ArrayList<GuestCategory> list = getGuestCategory();
        ArrayList<String> categoryList = new ArrayList();
        HashSet hs = new HashSet();
        
        try {
            for (int i=0; i<list.size(); i++)
                categoryList.add(list.get(i).getCategory());
            hs.addAll(categoryList);
            categoryList.clear();
            categoryList.addAll(hs);
            Collections.sort(categoryList);
        }
        catch (Exception ex) {
            System.out.println("Error! Cannot get category. " + ex);
        }
        return categoryList;
    }
    
    //get list of all rooms from database
    private ArrayList<Room> getAvailableRooms() {  
        ArrayList<Room> roomList = new ArrayList();
        try {
            //myConnection();
            String query = "SELECT * FROM `room` WHERE is_available=true";
            st = con.createStatement();
            rs = st.executeQuery(query);

            Room room;
            while (rs.next()) {
                room = new Room(rs.getInt("room_id"), rs.getString("room_name"), rs.getInt("num_of_beds"), rs.getBoolean("is_available"), rs.getInt("cottage_id"));
                
                roomList.add(room);
           }
        }
       catch (Exception ex) {
            System.out.println("Error! Cannot fetch. " + ex);
        }
        return roomList;
    }
    
    private ArrayList<String> getRoomNames() {  
        ArrayList<Room> list = getAvailableRooms();
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
    
    public int searchCategoryId(String search) {
        ArrayList<GuestCategory> categoryList = getGuestCategory();
        int categoryId = 0;
        for(GuestCategory cat : categoryList){
            if(cat.getCategory() != null && cat.getCategory().equals(search)) {
                categoryId = cat.getCategoryId();
            }
        }
        return categoryId; 
    }
    
    public int searchAccommodationId(String search) {
        ArrayList<AccommodationType> typeList = getAccommodationType();
        int accommodationId = 0;
        for(AccommodationType acc : typeList){
            if(acc.getType() != null && acc.getType().equals(search)) {
                accommodationId = acc.getId();
            }
        }
        return accommodationId; 
    }
    
    public double searchRate(int guestCategoryId, int accommodationTypeId) {
        ArrayList<Rates> list = getRates();
        double rate = 0;
        
        for(Rates rt : list){
            if(rt.getGuestCategoryId() == guestCategoryId && rt.getAccomodationTypeId() == accommodationTypeId) {
                rate = rt.getRate();
            }
        }
        return rate;
    }
    
    private long calculateNumOfNights(String arrivalDate, String departureDate) {
        long daysBetween, nights = 0;
        try {
            LocalDateTime arrival = df.stringToLocalDateTime(arrivalDate);
            LocalDateTime departure = df.stringToLocalDate(departureDate).atStartOfDay();
            daysBetween = Duration.between(arrival, departure).toDays();
            nights = daysBetween+1;
        } catch (ParseException | NullPointerException ex) {
            Logger.getLogger(NewReservationPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nights;
    }  
   
    public double calculateCharges(long numOfNights, String guestCategory, String accommodationType) { 
        String cat;
        int numOfAdults, numOfChildrenU12, numOfChildrenTeen, guestCategoryId, accommodationTypeId;
        double rateAdults, rateChildrenU12 = 0, rateChildrenTeen = 0;
        
        guestCategoryId = searchCategoryId(guestCategory);
        accommodationTypeId = searchAccommodationId(accommodationType);
        cat = guestCategory.toLowerCase();
        
        numOfAdults = Integer.parseInt(numOfAdultsSpinner.getValue().toString());
        numOfChildrenU12 = Integer.parseInt(numOfChildrenU12Spinner.getValue().toString());
        numOfChildrenTeen = Integer.parseInt(numOfChildrenTeenSpinner.getValue().toString());
        
        rateAdults = searchRate(guestCategoryId, accommodationTypeId) * numOfAdults;
        switch (cat) {
            case "non-resident adult":
                rateChildrenU12 = searchRate(searchCategoryId("Non-Resident Child U12"), accommodationTypeId) * numOfChildrenU12;
                rateChildrenTeen = searchRate(searchCategoryId("Non-Resident Child 12+"), accommodationTypeId) * numOfChildrenTeen;
                break;
            case "resident adult":
                rateChildrenU12 = searchRate(searchCategoryId("Resident Child U12"), accommodationTypeId) * numOfChildrenU12;
                rateChildrenTeen = searchRate(searchCategoryId("Resident Child 12+"), accommodationTypeId) * numOfChildrenTeen;
                break;
            case "missionary":
            case "tf-staff":
            case "complementary":
                rateChildrenU12 = searchRate(searchCategoryId("Complementary"), accommodationTypeId) * numOfChildrenU12;
                rateChildrenTeen = searchRate(searchCategoryId("Complementary"), accommodationTypeId) * numOfChildrenTeen;
                break;
        }
        
        return (rateAdults + rateChildrenU12 + rateChildrenTeen) * numOfNights;
    }
    
    private void addRooms() {
        List rooms = availableRoomsList.getSelectedValuesList();
        //int updatedRows = 0;
        for (Object x : rooms) {
            int roomId = 0;
            String room = x.toString();
            selectedRoomList.add(x);
            try {
                //myConnection();
                PreparedStatement p;

                String query = "SELECT `room_id` FROM `room` WHERE `room_name`=?";
                
                p = con.prepareStatement(query);
                p.setString(1, room);
                
                rs = p.executeQuery();

                if (rs.next()) {
                    roomId = rs.getInt("room_id");
                }    
            }
            catch (Exception ex) {
                 System.out.println("Error! Cannot fetch. " + ex);
            }
            
            try {
                PreparedStatement p;

                String newRowSql = "UPDATE `room` SET `is_available`=? WHERE room_id=?";
                p = con.prepareStatement(newRowSql);
                
                p.setBoolean(1, false);
                p.setInt(2, roomId);
                
                p.executeUpdate();
            }
            catch (Exception ex) {
                 System.out.println("Error! Cannot update record. " + ex);
             }
        }
        availableRoomsList.setModel(new javax.swing.AbstractListModel() {
            Object[] strings =  getRoomNames().toArray();
            @Override
            public int getSize() { return strings.length; }
            @Override
            public Object getElementAt(int i) { return strings[i]; }
        });
        reservedRoomsList.setModel(new javax.swing.AbstractListModel() {
            Object[] strings = selectedRoomList.toArray();
            @Override
            public int getSize() { return strings.length; }
            @Override
            public Object getElementAt(int i) { return strings[i]; }
        });
    }
    
    private void removeRooms() {
        List rooms = reservedRoomsList.getSelectedValuesList();
        //int updatedRows = 0;
        for (Object x : rooms) {
            int roomId = 0;
            String room = x.toString();
            selectedRoomList.remove(x);
            try {
                //myConnection();
                PreparedStatement p;

                String query = "SELECT `room_id` FROM `room` WHERE `room_name`=?";
                
                p = con.prepareStatement(query);
                p.setString(1, room);
                
                rs = p.executeQuery();

                if (rs.next()) {
                    roomId = rs.getInt("room_id");
                }    
            }
            catch (Exception ex) {
                 System.out.println("Error! Cannot fetch. " + ex);
            }
            
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
            reservedRoomsList.setSelectedValue(x, false);
        }
        availableRoomsList.setModel(new javax.swing.AbstractListModel() {
            Object[] strings = getRoomNames().toArray();
            @Override
            public int getSize() { return strings.length; }
            @Override
            public Object getElementAt(int i) { return strings[i]; }
        });
        reservedRoomsList.setModel(new javax.swing.AbstractListModel() {
            Object[] strings = selectedRoomList.toArray();
            @Override
            public int getSize() { return strings.length; }
            @Override
            public Object getElementAt(int i) { return strings[i]; }
        });
    }
            
    private void reserveRooms() {
        int begn = 0;
        int end = reservedRoomsList.getModel().getSize() - 1;
        if (end >= 0) {
           reservedRoomsList.setSelectionInterval(begn, end);
        }
        List rooms = reservedRoomsList.getSelectedValuesList();
        int updatedRows = 0;
        for (Object x : rooms) {
            System.out.println("(NRP) Rooms List Item: " + x.toString());
            
            int roomId = 0;
            String room = x.toString();
            try {
                //myConnection();
                PreparedStatement p;
                
                String query = "SELECT `room_id` FROM `room` WHERE `room_name`=?";
                
                p = con.prepareStatement(query);
                p.setString(1, room);
                
                rs = p.executeQuery();

                if (rs.next()) {
                    roomId = rs.getInt("room_id");
                }    
            }
            catch (Exception ex) {
                 System.out.println("Error! Cannot fetch. " + ex);
            }
            System.out.println("(NRP) Room ID: " + roomId);
            
            try {
                PreparedStatement p;

                String newRowSql = "INSERT INTO `reserved_rooms`(`reservation_id`, `room_id`) VALUES (?,?) ";
                p = con.prepareStatement(newRowSql);
                
                p.setInt(1, newReservationId);
                p.setInt(2, roomId);
                
                updatedRows = p.executeUpdate();
            }
            catch (Exception ex) {
                 System.out.println("Error! Cannot save record. " + ex);
             }
            try {
                PreparedStatement p;

                String newRowSql = "UPDATE `room` SET `is_available`=? WHERE room_id=?";
                p = con.prepareStatement(newRowSql);
                
                p.setBoolean(1, false);
                p.setInt(2, roomId);
                
                p.executeUpdate();
            }
            catch (Exception ex) {
                 System.out.println("Error! Cannot update record. " + ex);
             }
        }
        if (updatedRows > 0) {
            JOptionPane opt = new JOptionPane("Rooms Reserved.", JOptionPane.INFORMATION_MESSAGE);
            final JDialog jdg = opt.createDialog("Success");
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
    
    private HashMap searchSelectedParty() {
        HashMap<Object, Object> party = null;
        String searchKey = selectedGuest.getFirstName() + selectedGuest.getSurname() + java.time.LocalDate.now() + "";
        try {
            Statement st;

            String query = "SELECT * FROM `party` WHERE `party_name`='" + searchKey + "'";
 
            st = con.createStatement();
            rs = st.executeQuery(query);

            if (rs.next()) {
                party = new HashMap();
                party.put("partyId", rs.getInt("party_id"));
                party.put("partyName", rs.getString("party_name"));
            }
        }
        catch (Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error!");
        }
        return party;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        leftPanel = new javax.swing.JPanel();
        lblGuestDetails = new javax.swing.JLabel();
        txtCurrentGuest = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        guestCategoryCombo = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        accommodationTypeCombo = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        numOfAdultsSpinner = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        numOfChildrenU12Spinner = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        numOfChildrenTeenSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        arrivalHour = new javax.swing.JSpinner();
        arrivalMinute = new javax.swing.JSpinner();
        rightPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        availableRoomsList = new javax.swing.JList();
        btnAddRoom = new javax.swing.JButton();
        btnRemoveRoom = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        reservedRoomsList = new javax.swing.JList();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        rbNo = new javax.swing.JRadioButton();
        rbYes = new javax.swing.JRadioButton();
        btnManageParty = new javax.swing.JButton();

        setName("NewReservationPanel"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1200, 655));

        leftPanel.setBackground(new java.awt.Color(255, 255, 255));

        lblGuestDetails.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        lblGuestDetails.setText("Reservation for: ");

        txtCurrentGuest.setEditable(false);
        txtCurrentGuest.setBackground(java.awt.SystemColor.controlHighlight);
        txtCurrentGuest.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        txtCurrentGuest.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));

        jLabel2.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel2.setText("Arrival Date");

        jLabel3.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel3.setText("Departure Date");

        jLabel6.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel6.setText("Guest Category");

        guestCategoryCombo.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        guestCategoryCombo.setModel(new javax.swing.DefaultComboBoxModel(getCategoryList().toArray()));
        guestCategoryCombo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel7.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel7.setText("Accommodation Type");

        accommodationTypeCombo.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        accommodationTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(getTypeList().toArray()));
        accommodationTypeCombo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel4.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel4.setText("Number of Adults");

        numOfAdultsSpinner.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        numOfAdultsSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 0, 50, 1));

        jLabel5.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel5.setText("Number of Children U12");

        numOfChildrenU12Spinner.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        numOfChildrenU12Spinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 50, 1));

        jLabel9.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel9.setText("12+");

        numOfChildrenTeenSpinner.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        numOfChildrenTeenSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 50, 1));

        jLabel1.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel1.setText("Arrival Time");

        arrivalHour.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        arrivalHour.setModel(new javax.swing.SpinnerNumberModel(10, 1, 23, 1));
        arrivalHour.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), null));

        arrivalMinute.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        arrivalMinute.setModel(new javax.swing.SpinnerListModel(new String[] {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"}));
        arrivalMinute.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), null));
        arrivalMinute.setFocusCycleRoot(true);

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(240, 240, 240)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(arrivalHour, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(arrivalMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(leftPanelLayout.createSequentialGroup()
                                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(leftPanelLayout.createSequentialGroup()
                                        .addComponent(lblGuestDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtCurrentGuest, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(leftPanelLayout.createSequentialGroup()
                                        .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(numOfAdultsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(numOfChildrenU12Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(leftPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(numOfChildrenTeenSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(leftPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(guestCategoryCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(leftPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(accommodationTypeCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCurrentGuest, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGuestDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(arrivalHour, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(arrivalMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(guestCategoryCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(accommodationTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addComponent(numOfAdultsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(numOfChildrenU12Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(34, 34, 34)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numOfChildrenTeenSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(62, Short.MAX_VALUE))
        );

        arrivalMinute.getAccessibleContext().setAccessibleDescription("");

        rightPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel8.setText("Select Room(s) to Reserve ");

        jLabel10.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel10.setText("Reserved");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        availableRoomsList.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));
        availableRoomsList.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        availableRoomsList.setModel(new javax.swing.AbstractListModel() {
            Object[] strings = getRoomNames().toArray();
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(availableRoomsList);

        btnAddRoom.setText(">>");
        btnAddRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRoomActionPerformed(evt);
            }
        });

        btnRemoveRoom.setText("<<");
        btnRemoveRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveRoomActionPerformed(evt);
            }
        });

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        reservedRoomsList.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 16, 4, 16));
        reservedRoomsList.setFont(new java.awt.Font("Corbel", 0, 16)); // NOI18N
        jScrollPane2.setViewportView(reservedRoomsList);

        btnCancel.setBackground(new java.awt.Color(245, 108, 108));
        btnCancel.setFont(new java.awt.Font("Corbel", 0, 22)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setText("Cancel");
        btnCancel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(105, 205, 130));
        btnSave.setFont(new java.awt.Font("Corbel", 0, 22)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Save");
        btnSave.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        jLabel11.setText("Party");

        rbNo.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(rbNo);
        rbNo.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        rbNo.setText("No");
        rbNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbNoActionPerformed(evt);
            }
        });

        rbYes.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(rbYes);
        rbYes.setFont(new java.awt.Font("Corbel", 0, 18)); // NOI18N
        rbYes.setText("Yes");
        rbYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbYesActionPerformed(evt);
            }
        });

        btnManageParty.setBackground(new java.awt.Color(80, 170, 195));
        btnManageParty.setFont(new java.awt.Font("Corbel", 1, 16)); // NOI18N
        btnManageParty.setForeground(new java.awt.Color(255, 255, 255));
        btnManageParty.setText("Manage Party");
        btnManageParty.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnManageParty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManagePartyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addGap(299, 299, 299)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(rightPanelLayout.createSequentialGroup()
                                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnRemoveRoom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnAddRoom))
                                .addGap(18, 18, 18)
                                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(rightPanelLayout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(rightPanelLayout.createSequentialGroup()
                                .addComponent(rbNo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(rbYes, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnManageParty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(btnAddRoom)
                        .addGap(18, 18, 18)
                        .addComponent(btnRemoveRoom))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbYes, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnManageParty, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(leftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE)
                .addComponent(rightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(rightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleName("NewReservationPanel");
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        try {
            PreparedStatement p;
            String arrivalDate = null, departureDate, guestCategory = null, accommodationType = null;
            Integer numOfAdults, numOfChildrenU12, numOfChildrenTeen, userId = 0, guestId = 0, partyId = 0;
            long numOfNights;
            double charges;
            
            arrivalDate = getArrivalDate();
            departureDate = getDepartureDate();
            guestCategory = guestCategoryCombo.getSelectedItem().toString();
            userId = currentUser.getId();
            guestId = selectedGuest.getId();
            accommodationType = accommodationTypeCombo.getSelectedItem().toString();
            if (rbYes.isSelected())
                partyId = Integer.parseInt(selectedParty.get("partyId").toString());
            else
                partyId = null;
            
            if (guestCategoryCombo.getSelectedItem().equals("Day/Walk-In")) {
                numOfNights = 0;
                numOfAdults = 0;
                numOfChildrenU12 = 0;
                numOfChildrenTeen = 0;
                charges = 0.0;
            } else {
                numOfNights = calculateNumOfNights(arrivalDate, departureDate);
                numOfAdults = Integer.parseInt(numOfAdultsSpinner.getValue().toString());
                numOfChildrenU12 = Integer.parseInt(numOfChildrenU12Spinner.getValue().toString());
                numOfChildrenTeen = Integer.parseInt(numOfChildrenTeenSpinner.getValue().toString());
                charges = calculateCharges(numOfNights, guestCategory, accommodationType);
            }
                
            String newRowSql = "INSERT INTO `reservation`(`arrival_date`, `departure_date`, `num_of_nights`, `num_of_adults`, `num_of_children_u12`, `num_of_children_teen`, `charges`, `guest_category`, `user_id`, `guest_id`, `accommodation_type`, `party_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

            p = con.prepareStatement(newRowSql, Statement.RETURN_GENERATED_KEYS);
            p.setString(1, arrivalDate);
            p.setString(2, departureDate);
            p.setLong(3, numOfNights);
            p.setInt(4, numOfAdults);
            p.setInt(5, numOfChildrenU12);
            p.setInt(6, numOfChildrenTeen);
            p.setDouble(7, charges);
            p.setInt(8, searchCategoryId(guestCategory));
            p.setInt(9, userId);
            p.setInt(10, guestId);
            p.setInt(11, searchAccommodationId(accommodationType));
            p.setObject(12, partyId, Types.INTEGER);

            int updatedRows = p.executeUpdate();
            rs = p.getGeneratedKeys();
            if (rs != null && rs.next() ) {
                newReservationId = rs.getInt(1);
                reserveRooms();
            } else {
                System.out.println("Cannot get result set.");
            }
            if (updatedRows > 0) {
                JOptionPane.showMessageDialog(null, "Record Saved Successfully!");
                ((JDialog)NewReservationPanel.this.getTopLevelAncestor()).dispose();
            }
            else
                JOptionPane.showMessageDialog(null, "Error! Unable to add record.");
        }
        catch (Exception ex) {
            System.out.println("Error! " + ex);
            JOptionPane.showMessageDialog(null, "Error! Cannot add reservation. " + ex.getMessage());
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnAddRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRoomActionPerformed
        addRooms();
    }//GEN-LAST:event_btnAddRoomActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        //((JFrame)NewReservationPanel.this.getTopLevelAncestor()).dispose();
        ((JDialog) this.getTopLevelAncestor()).dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnRemoveRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveRoomActionPerformed
        removeRooms();
    }//GEN-LAST:event_btnRemoveRoomActionPerformed

    private void rbNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNoActionPerformed
        btnManageParty.setVisible(false);
    }//GEN-LAST:event_rbNoActionPerformed

    private void rbYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbYesActionPerformed
        btnManageParty.setVisible(true);
    }//GEN-LAST:event_rbYesActionPerformed

    private void btnManagePartyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManagePartyActionPerformed
        selectedParty = searchSelectedParty();
        //System.out.println("HashMap status: " + selectedParty.isEmpty());
        if (selectedParty == null) {
            ManagePartyForm manageParty = new ManagePartyForm((Frame) this.getTopLevelAncestor().getParent(), true);
            manageParty.setTitle("Manage Party Details");
            manageParty.setLocationRelativeTo(null);
            manageParty.setVisible(true);
            manageParty.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        } else {
            ManagePartyForm manageParty = new ManagePartyForm((Frame) this.getTopLevelAncestor().getParent(), true, selectedParty);
            manageParty.setTitle("Manage Party Details");
            manageParty.setLocationRelativeTo(null);
            manageParty.setVisible(true);
            manageParty.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }//GEN-LAST:event_btnManagePartyActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox accommodationTypeCombo;
    private javax.swing.JSpinner arrivalHour;
    private javax.swing.JSpinner arrivalMinute;
    private javax.swing.JList availableRoomsList;
    private javax.swing.JButton btnAddRoom;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnManageParty;
    private javax.swing.JButton btnRemoveRoom;
    private javax.swing.JButton btnSave;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox guestCategoryCombo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblGuestDetails;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JSpinner numOfAdultsSpinner;
    private javax.swing.JSpinner numOfChildrenTeenSpinner;
    private javax.swing.JSpinner numOfChildrenU12Spinner;
    private javax.swing.JRadioButton rbNo;
    private javax.swing.JRadioButton rbYes;
    private javax.swing.JList reservedRoomsList;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JTextField txtCurrentGuest;
    // End of variables declaration//GEN-END:variables

}
